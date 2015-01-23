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
 * 通用数据包
 *
 * @author Wen, Chifeng <https://sourceforge.net/u/daviesx/profile/>
 */
public class PacketGeneralData extends NetworkDataPacket {

        private String c_Port_Nature = NetworkDataPacket.c_Broadcast_Port;
        private final int c_Packet_Nature = NetworkDataPacketFactory.c_GeneralData_Packet;

        private byte[] m_data;
        private int m_num_bytes;

        /**
         * 带预分配大小的数据包
         *
         * @param pre_alloc_size 预分配大小
         */
        public PacketGeneralData(int pre_alloc_size) {
                m_data = new byte[pre_alloc_size];
                m_num_bytes = NetworkDataPacket.c_Header_Length;
        }

        /**
         * 创建空数据包.
         */
        public PacketGeneralData() {
                m_data = null;
                m_num_bytes = 0;
        }

        /**
         * 将数据包指针重设到开始位置.
         */
        public void reset() {
                m_num_bytes = NetworkDataPacket.c_Header_Length;
        }

        /**
         * 写入一个整数
         *
         * @param i
         */
        public void write_int(Integer i) {
                m_num_bytes = NetworkDataPacket.write_int(m_data, m_num_bytes, i);
        }

        /**
         * 读取一个整数.
         *
         * @return 数值
         */
        public Integer read_int() {
                Integer[] i = new Integer[1];
                m_num_bytes = NetworkDataPacket.read_int(m_data, m_num_bytes, i);
                return i[0];
        }

        /**
         * 写入一个字符串.
         *
         * @param s
         */
        public void write_string(String s) {
                m_num_bytes = NetworkDataPacket.write_string(m_data, m_num_bytes, s);
        }

        /**
         * 读取一个字符串
         *
         * @return 字符串数值
         */
        public String read_string() {
                String[] s = new String[1];
                m_num_bytes = NetworkDataPacket.read_string(m_data, m_num_bytes, s);
                return s[0];
        }
        
        public void port(String port) {
                c_Port_Nature = port;
        }
        
        public String port() {
                return c_Port_Nature;
        }

        @Override
        public byte[] serialize() {
                byte[] b = new byte[m_num_bytes];
                NetworkDataPacket.encode_port_value(m_data, c_Port_Nature);
                NetworkDataPacket.encode_packet_nature(m_data, c_Packet_Nature);
                NetworkDataPacket.encode_packet_length(m_data, m_num_bytes);
                System.arraycopy(m_data, 0, b, 0, b.length);
                return b;
        }

        @Override
        public void deserialize(byte[] data) {
                Integer length = NetworkDataPacket.decode_packet_length(data);
                m_data = new byte[length + NetworkDataPacket.c_Header_Length];
                System.arraycopy(data, 0, m_data, 0, m_data.length);
                m_num_bytes = NetworkDataPacket.c_Header_Length;
        }

        @Override
        public int packet_nature() {
                return NetworkDataPacketFactory.c_GeneralData_Packet;
        }
}
