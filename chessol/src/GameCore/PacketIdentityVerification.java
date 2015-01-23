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
 * 身份验证数据包
 *
 * @author Wen, Chifeng <https://sourceforge.net/u/daviesx/profile/>
 */
public class PacketIdentityVerification extends NetworkDataPacket {
        
        private final int c_Packet_Nature = NetworkDataPacketFactory.c_IdentityVerification_Packet;
        private String m_port_nature = NetworkDataPacket.c_Upload_port;
        private String m_port;
        private String m_uid;
        private String m_passcode;

        /**
         * 或将在deserialize中构建.
         */
        public PacketIdentityVerification() {
                m_uid = null;
                m_passcode = null;
        }

        /**
         * 构建身份
         *
         * @param id 用户id
         */
        public PacketIdentityVerification(String id) {
                m_uid = id;
                //@TODO: 需要MD5加密
                Integer encry = "".hashCode();
                m_passcode = encry.toString();
        }

        /**
         * 构建身份
         *
         * @param id 用户id
         * @param password 密码
         */
        public PacketIdentityVerification(String id, String password) {
                m_uid = id;
                //@TODO: 需要MD5加密
                Integer encry = password.hashCode();
                m_passcode = encry.toString();
        }

        /**
         * @return 用户id
         */
        public String id() {
                return m_uid;
        }

        /**
         * @return 密文
         */
        public String passcode() {
                return m_passcode;
        }

        public String get_this_port() {
                return m_port;
        }
        
        public void update_this_port(String port) {
                m_port = port;
        }

        public void set_dest_port(String port) {
                m_port_nature = port;
        }

        @Override
        public byte[] serialize() {
                int size = string_size(m_port) + string_size(m_uid) + string_size(m_passcode);

                byte[] b = new byte[size + c_Header_Length];
                encode_port_value(b, m_port_nature);
                encode_packet_nature(b, c_Packet_Nature);
                encode_packet_length(b, b.length);

                int iterator = c_Header_Length;
                iterator = write_string(b, iterator, m_port);
                iterator = write_string(b, iterator, m_uid);
                iterator = write_string(b, iterator, m_passcode);
                return b;
        }

        @Override
        public void deserialize(byte[] data) {
                int iterator = c_Header_Length;
                String[] port = new String[1];
                String[] uid = new String[1];
                String[] passcode = new String[1];
                iterator = read_string(data, iterator, port);
                iterator = read_string(data, iterator, uid);
                iterator = read_string(data, iterator, passcode);
                
                m_port = port[0];
                m_uid = uid[0];
                m_passcode = passcode[0];
        }

        @Override
        public int packet_nature() {
                return NetworkDataPacketFactory.c_IdentityVerification_Packet;
        }
}
