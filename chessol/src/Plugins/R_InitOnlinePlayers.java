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
package Plugins;

import GameCore.App;
import GameCore.GameNetwork;
import GameCore.ObjectCache;
import GameCore.RunnableState;

/**
 * 获得参与玩家
 *
 * @author Wen, Chifeng <https://sourceforge.net/u/daviesx/profile/>
 */
public class R_InitOnlinePlayers extends RunnableState {

        private final int m_normal_next;
        private final int m_error_next;
        private boolean m_has_error = true;

        private final int c_Packet_Signature = 0XA;

        /**
         * 初始化网络玩家
         *
         * @param normal_next 正常初始化后的状态id
         * @param error_next 初始化失败后的状态id
         */
        public R_InitOnlinePlayers(int normal_next, int error_next) {
                m_normal_next = normal_next;
                m_error_next = error_next;
        }

        @Override
        public void state_init(ObjectCache oc) {
                GameHall hallgui = oc.<GameHall>use("hallgui");
                App app = oc.<App>use("app");
                GameNetwork network = app.get_game_network();

                /*if (hallgui.is_inviter()) {
                 // 如果是房主：构建，验证然后发送玩家信息
                        
                 int n_nets1 = hallgui.get_friend_players_id().length;
                 int n_nets2 = hallgui.get_opponent_players_id().length;
                 oc.declare("num_local_players", 1);
                 oc.declare("num_net_players", n_nets1 + n_nets2 - 1);
                 // 构造玩家
                 String a0[] = hallgui.get_friend_players_id();
                 String a1[] = hallgui.get_opponent_players_id();
                 // 构造本地玩家
                 String[] local = {a0[0]};
                 PacketIdentityVerification[] local_players = new PacketIdentityVerification[1];
                 local_players[0] = new PacketIdentityVerification(a0[0]);
                 for (int i = 0; i < local_players.length; i++) {
                 network.client_create(local_players[i]);
                 network.client_login(local_players[i]);
                 }
                 // 处理网络玩家
                 String all_nets[] = new String[n_nets1 + n_nets2 - 1];
                 for (int i = 1; i < a0.length; i++) {
                 all_nets[i - 1] = a0[i];
                 }
                 for (int i = 0; i < a1.length; i++) {
                 all_nets[i + n_nets1 - 1] = a1[i];
                 }
                 PacketIdentityVerification[] net_players = new PacketIdentityVerification[all_nets.length];
                 for (int i = 0; i < all_nets.length; i++) {
                 net_players[i] = new PacketIdentityVerification(all_nets[i]);
                 }
                 // 发送玩家信息
                 PacketGeneralData gen_data = new PacketGeneralData(100 * 1024);
                 gen_data.write_int(c_Packet_Signature);
                 gen_data.write_int(all_nets.length + 1);
                 for (String c_local : local) {
                 gen_data.write_string(c_local);
                 }
                 for (String c_net : all_nets) {
                 gen_data.write_string(c_net);
                 }
                 for (int i = 0; i < net_players.length; i++) {
                 network.client_send_packet(local_players[0], gen_data);
                 }
                 //@TODO: 验证信息
                 boolean[] net_responses = new boolean[all_nets.length];

                 oc.declare("is_inviter", true);
                 oc.declare("local_players", local_players);
                 oc.declare("net_players", net_players);
                 m_has_error = false;
                 } else {
                 // 构造本地玩家
                 String a0[] = hallgui.get_friend_players_id();
                 PacketIdentityVerification[] local_players = {new PacketIdentityVerification(a0[0])};
                 for (int i = 0; i < local_players.length; i++) {
                 network.client_create(local_players[i]);
                 }
                 // 获取网上玩家
                 NetworkDataPacket[] packets = network.client_receive_packet(local_players[0], true);
                 PacketIdentityVerification[] net_players = null;
                 for (int i = 0; i < packets.length; i++) {
                 if (packets[i].packet_nature() != NetworkDataPacketFactory.c_GeneralData_Packet) {
                 continue;
                 }
                 PacketGeneralData gp = (PacketGeneralData) packets[i];
                 if (gp.read_int() == c_Packet_Signature) {
                 int length = gp.read_int();
                 net_players = new PacketIdentityVerification[length];
                 for (int j = 0, k = 0; j < length; j++) {
                 String player_id = gp.read_string();
                 if (!player_id.equals(local_players[0].id())) {
                 net_players[k++] = new PacketIdentityVerification(player_id);
                 }
                 }
                 break;
                 }
                 }
                 }*/
                oc.declare("is_inviter", hallgui.is_inviter());
                oc.declare("local_players", hallgui.get_friend_players_id());
                oc.declare("net_players", hallgui.get_opponent_players_id());
                m_has_error = false;
        }

        @Override
        public void state_loop(ObjectCache oc) {
                // do nothing
        }

        @Override
        public int state_transit(ObjectCache oc) {
                if (m_has_error) {
                        return m_error_next;
                } else {
                        return m_normal_next;
                }
        }

}
