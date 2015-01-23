/* 
 * Copyright (C) 2015 Wen, Chifeng <https://sourceforge.net/u/daviesx/profile/>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package GameCore;

/**
 * 网络数据包
 *
 * @author Wen, Chifeng <https://sourceforge.net/u/daviesx/profile/>
 */
public abstract class NetworkDataPacket {

        public static final String c_Broadcast_Port = "-1";
        public static final String c_Upload_port = "-2";

        /**
         * 将数据包序列化成byte[]
         *
         * @return byte[] 序列化数据
         */
        public abstract byte[] serialize();

        /**
         * 将序列化数据转换成数据包
         *
         * @param data 序列化数据
         */
        public abstract void deserialize(byte[] data);

        /**
         * 数据包类型
         *
         * @return
         */
        public abstract int packet_nature();

        public static final int c_Header_Port = 0;
        public static final int c_Header_Packet_Nature = 1;
        public static final int c_Header_Packet_Size = 2;
        public static final int c_Header_Signature = 3;
        public static final int c_Offset_Table[] = {
                0 * PrimitiveType.sizeof(PrimitiveType.Integer),
                1 * PrimitiveType.sizeof(PrimitiveType.Integer),
                2 * PrimitiveType.sizeof(PrimitiveType.Integer),
                3 * PrimitiveType.sizeof(PrimitiveType.Integer)
        };
        public static final int c_Header_Length = 4 * PrimitiveType.sizeof(PrimitiveType.Integer);

        public static int write_int(byte[] data, int i, Integer value) {
                data[i + 0] = (byte) (0XFF & (value >>> 0));
                data[i + 1] = (byte) (0XFF & (value >>> 8));
                data[i + 2] = (byte) (0XFF & (value >>> 16));
                data[i + 3] = (byte) (0XFF & (value >>> 24));
                return i + PrimitiveType.sizeof(value);
        }

        public static int read_int(byte[] data, int i, Integer[] value) {
                value[0] = ((int) (0XFF & data[i + 3]) << 24)
                    | ((int) (0XFF & data[i + 2]) << 16)
                    | ((int) (0XFF & data[i + 1]) << 8)
                    | ((int) (0XFF & data[i + 0]) << 0);
                return i + PrimitiveType.sizeof(value[0]);
        }

        public static int write_string(byte[] data, int i, String s) {
                i = write_int(data, i, s.getBytes().length);
                byte[] b = s.getBytes();
                System.arraycopy(b, 0, data, i, b.length);
                return i + s.getBytes().length;
        }

        public static int read_string(byte[] data, int i, String[] s) {
                Integer[] length = new Integer[1];
                i = read_int(data, i, length);
                byte[] b = new byte[length[0]];
                System.arraycopy(data, i, b, 0, b.length);
                s[0] = new String(b);
                return i + b.length;
        }
        
        public static int write_byte_array(byte[] data, int i, byte[] array) {
                i = write_int(data, i, array.length);
                System.arraycopy(array, 0, data, i, array.length);
                return i + array.length;
        }
        
        public static int read_byte_array(byte[] data, int i, byte[][] array) {
                Integer[] length = new Integer[1];
                i = read_int(data, i, length);
                array[0] = new byte[length[0]];
                System.arraycopy(data, i, array[0], 0, array[0].length);
                return i + array[0].length;
        }
        
        public static int int_size(Integer i) {
                return PrimitiveType.sizeof(PrimitiveType.Integer);
        }

        public static int string_size(String s) {
                return PrimitiveType.sizeof(PrimitiveType.Integer) + s.getBytes().length;
        }
        
        public static int byte_array_size(byte[] array) {
                return PrimitiveType.sizeof(PrimitiveType.Integer) + array.length;
        }

        /**
         * 提取端口
         *
         * @param data
         * @return String 端口
         */
        public static String decode_port_value(byte[] data) {
                Integer[] port = new Integer[1];
                read_int(data, c_Offset_Table[c_Header_Port], port);
                return Integer.toString(port[0]);
        }

        /**
         * 填写目标端口
         *
         * @param data 序列化数据
         * @param port 端口名
         */
        public static void encode_port_value(byte[] data, String port) {
                Integer iport = Integer.parseInt(port);
                write_int(data, c_Offset_Table[c_Header_Port], iport);
        }

        /**
         * 提取数据包类型
         *
         * @param data 序列化数据
         * @return int 数据包类型
         */
        public static int decode_packet_nature(byte[] data) {
                Integer[] nature = new Integer[1];
                read_int(data, c_Offset_Table[c_Header_Packet_Nature], nature);
                return nature[0];
        }

        /**
         * 填写数据包类型
         *
         * @param data 序列化数据
         * @param nature 数据包类型
         */
        public static void encode_packet_nature(byte[] data, int nature) {
                write_int(data, c_Offset_Table[c_Header_Packet_Nature], nature);
        }

        /**
         * 提取数据包长度
         *
         * @param data 序列化数据
         * @return int 数据包长度
         */
        public static int decode_packet_length(byte[] data) {
                Integer[] length = new Integer[1];
                read_int(data, c_Offset_Table[c_Header_Packet_Size], length);
                return length[0];
        }

        /**
         * 填写数据包长度
         *
         * @param data
         * @param length
         */
        public static void encode_packet_length(byte[] data, int length) {
                write_int(data, c_Offset_Table[c_Header_Packet_Size],
                    length - c_Header_Length);
        }
        
        /**
         * 提取签名
         * @param data
         * @return 
         */
        public static int decode_packet_signature(byte[] data) {
                Integer signature[] = new Integer[1];
                read_int(data, c_Offset_Table[c_Header_Signature], signature);
                return signature[0];
        }
        
        /**
         * 填写签名
         * @param data
         * @param signature
         */
        public static void encode_packet_signature(byte[] data, int signature) {
                write_int(data, c_Offset_Table[c_Header_Signature], signature);
        }
}
