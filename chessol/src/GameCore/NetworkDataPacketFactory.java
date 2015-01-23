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
 * 数据包工厂
 *
 * @author Wen, Chifeng <https://sourceforge.net/u/daviesx/profile/>
 */
public class NetworkDataPacketFactory {

        public static final int c_IdentityVerification_Packet = 1;
        public static final int c_GeneralData_Packet = 2;
        public static final int c_UserQuery_Packet = 3;

        public static NetworkDataPacket create(int nature) {
                switch (nature) {
                        case c_IdentityVerification_Packet: {
                                return new PacketIdentityVerification();
                        }
                        case c_GeneralData_Packet: {
                                return new PacketGeneralData();
                        }
                        case c_UserQuery_Packet: {
                                return new PacketUserQuery();
                        }
                        default: {
                                System.out.println("参数不是合法的数据包类型");
                                return null;
                        }
                }
        }
}
