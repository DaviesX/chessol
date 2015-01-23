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

import java.util.ArrayList;

/**
 * 用户查询
 *
 * @author Wen, Chifeng <https://sourceforge.net/u/daviesx/profile/>
 */
public class PacketUserQuery extends NetworkDataPacket {

        private final String c_Port_Nature;
        private final int c_Packet_Nature = NetworkDataPacketFactory.c_UserQuery_Packet;

        private final ArrayList<PacketIdentityVerification> m_users;

        public PacketUserQuery() {
                c_Port_Nature = NetworkDataPacket.c_Upload_port;
                m_users = new ArrayList();
        }
        /*
         public PacketUserQuery(String port) {
         c_Port_Nature = port;
         m_users = new ArrayList();
         }
         */

        public void add_packet(PacketIdentityVerification packet) {
                m_users.add(packet);
        }

        public PacketIdentityVerification[] get_packets() {
                return m_users.toArray(new PacketIdentityVerification[m_users.size()]);
        }

        @Override
        public byte[] serialize() {
                // 估计大小
                int size = PrimitiveType.sizeof(PrimitiveType.Integer);
                for (int i = 0; i < m_users.size(); i++) {
                        PacketIdentityVerification userid = m_users.get(i);
                        size += byte_array_size(userid.serialize());
                }
                // 序列化数据
                byte[] data = new byte[size + c_Header_Length];
                encode_port_value(data, c_Port_Nature);
                encode_packet_nature(data, c_Packet_Nature);
                encode_packet_length(data, size + c_Header_Length);

                int iterator = c_Header_Length;
                iterator = write_int(data, iterator, m_users.size());

                for (int i = 0; i < m_users.size(); i++) {
                        PacketIdentityVerification userid = m_users.get(i);
                        iterator = write_byte_array(data, iterator, userid.serialize());
                }
                return data;
        }

        @Override
        public void deserialize(byte[] data) {
                int iterator = c_Header_Length;
                Integer[] n = new Integer[1];
                iterator = read_int(data, iterator, n);

                m_users.clear();
                for (int i = 0; i < n[0]; i++) {
                        byte[][] b = new byte[1][];
                        iterator = read_byte_array(data, iterator, b);

                        PacketIdentityVerification userid = new PacketIdentityVerification();
                        userid.deserialize(b[0]);
                        m_users.add(userid);
                }
        }

        @Override
        public int packet_nature() {
                return c_Packet_Nature;
        }

}
