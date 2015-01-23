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
import GameCore.ComponentNames;
import GameCore.GameAudio;
import GameCore.GameGraphics;
import GameCore.GameNetwork;
import GameCore.IPoint2d;
import GameCore.NetworkDataPacket;
import GameCore.ObjectCache;
import GameCore.PacketGeneralData;
import GameCore.PacketIdentityVerification;
import GameCore.RunnableState;
import GameCore.StateGraph;
import javax.swing.JOptionPane;

class R_SelectChess extends RunnableState {

        private GameRoom m_roomgui;
        private GameGraphics m_graphics;

        private final IPoint2d m_tmp;
        private boolean has_selected = false;

        private final PlayerMode m_player_mode;
        private final MoveVerifier m_verifer;

        public R_SelectChess(PlayerMode player_mode, MoveVerifier verifier) {
                m_player_mode = player_mode;
                m_verifer = verifier;
                m_tmp = new IPoint2d();
        }

        @Override
        public void state_init(ObjectCache oc) {
                m_roomgui = oc.<GameRoom>use("roomgui");
                m_graphics = oc.<App>use("app").get_game_graphics();
        }

        @Override
        public void state_loop(ObjectCache oc) {
                IPoint2d p = m_roomgui.getMouseClickedPosition();
                boolean x = m_graphics.calculateSelectedPoint(p.x, p.y, 30, m_tmp);
                boolean y = m_verifer.is_valid(m_tmp, m_player_mode.get_player().get_side());
                has_selected = x && y;
        }

        @Override
        public int state_transit(ObjectCache oc) {
                if (has_selected) {
                        oc.declare("selected_position", m_tmp);
                        return R_DoublePlayers.States.MoveChess.ordinal();
                } else {
                        oc.undeclare("selected_position");
                        return super.m_stategraph.get_current_state();
                }
        }
}

class R_MoveChess extends RunnableState {

        private GameRoom m_roomgui = null;
        private GameGraphics m_graphics = null;
        private final IPoint2d m_tmp;
        private boolean has_selected;

        public R_MoveChess() {
                m_tmp = new IPoint2d();
        }

        @Override
        public void state_init(ObjectCache oc) {
                m_roomgui = oc.<GameRoom>use("roomgui");
                m_graphics = oc.<App>use("app").get_game_graphics();
        }

        @Override
        public void state_loop(ObjectCache oc) {
                IPoint2d p = m_roomgui.getMouseClickedPosition();
                if ((has_selected = m_graphics.calculateSelectedPoint(p.x, p.y, 30, m_tmp)) == true) {
                        oc.declare("moved_position", m_tmp);
                }
        }

        @Override
        public int state_transit(ObjectCache oc) {
                if (has_selected) {
                        return R_DoublePlayers.States.VerifyChess.ordinal();
                } else {
                        return super.m_stategraph.get_current_state();
                }
        }

}

class R_VerifyChess extends RunnableState {

        private final MoveVerifier m_verifer;
        private final PlayerMode m_player_mode;
        private boolean m_movable;

        private GameAudio m_audio;

        public R_VerifyChess(PlayerMode player_mode, MoveVerifier verifier) {
                m_verifer = verifier;
                m_player_mode = player_mode;
        }

        @Override
        public void state_init(ObjectCache oc) {
                App app = oc.<App>use("app");
                m_audio = app.get_game_audio();

                IPoint2d src = oc.<IPoint2d>use("selected_position");
                IPoint2d dest = oc.<IPoint2d>use("moved_position");
                MoveEvent event = new MoveEvent();
                Player player = m_player_mode.get_player();
                m_movable = m_verifer.move(player.get_side(), src, dest, event);
                if (event.captured_將) {
                        m_audio.play(GameAudio.c_Jiang);
                        m_player_mode.notify_win();
                } else if (event.captured != 0) {
                        m_audio.play(GameAudio.c_Captured);
                } else if (m_movable) {
                        m_audio.play(GameAudio.c_Seat);
                }
                if (event.repetition_of_3) {
                        JOptionPane.showMessageDialog(null, "重复3次了！");
                }
                if (m_movable == false) {
                        JOptionPane.showMessageDialog(null, "不能这样走棋！");
                }
        }

        @Override
        public void state_loop(ObjectCache oc) {
                // do nothing
        }

        @Override
        public int state_transit(ObjectCache oc) {
                if (m_movable) {
                        return R_DoublePlayers.States.UploadChess.ordinal();
                } else {
                        return R_DoublePlayers.States.SelectChess.ordinal();
                }
        }
}

class R_UploadChess extends RunnableState {

        private final MoveVerifier m_verifer;
        private final PlayerMode m_player_mode;
        private GameNetwork m_network;
        private PacketIdentityVerification m_local_id;
        private PacketIdentityVerification m_oppo_id;

        public R_UploadChess(PlayerMode mode, MoveVerifier verifier) {
                m_player_mode = mode;
                m_verifer = verifier;
        }

        @Override
        public void state_init(ObjectCache oc) {
                m_network = oc.<App>use(ComponentNames.c_App).get_game_network();
                m_local_id = oc.<PacketIdentityVerification[]>use("local_players")[0];
                m_oppo_id = oc.<PacketIdentityVerification[]>use("net_players")[0];

                PacketGeneralData gpacket = new PacketGeneralData(10 * 9 * 40);
                gpacket.port(m_oppo_id.get_this_port());
                int[][] grid = new int[10][9];
                m_verifer.export_navigrid(grid);
                for (int i = 0; i < 10; i++) {
                        for (int j = 0; j < 9; j++) {
                                gpacket.write_int(grid[i][j]);
                        }
                }

                m_network.client_send_packet(m_local_id, 0X4A, gpacket);
        }

        @Override
        public void state_loop(ObjectCache oc) {
                // do nothing
        }

        @Override
        public int state_transit(ObjectCache oc) {
                m_player_mode.notify_next();
                return StateGraph.c_EndState;
        }

}

class R_UpdateChess extends RunnableState {

        private final PlayerMode m_player_mode;
        private final MoveVerifier m_verifier;
        private GameGraphics m_graphics = null;

        public R_UpdateChess(PlayerMode player_mode, MoveVerifier verifier) {
                m_player_mode = player_mode;
                m_verifier = verifier;
        }

        @Override
        public void state_init(ObjectCache oc) {
                m_graphics = oc.<App>use("app").get_game_graphics();
        }

        @Override
        public void state_loop(ObjectCache oc) {
                m_graphics.beginDraw();
                LogicalChess[] chesses = m_verifier.generate_chess();
                for (LogicalChess c : chesses) {
                        m_graphics.addChess(c.m_x, c.m_y, c.m_type);
                }
                IPoint2d selected;
                if ((selected = oc.<IPoint2d>use("selected_position")) != null) {
                        m_graphics.addSelection(selected.x, selected.y);
                }
                m_graphics.endDraw();
        }

        @Override
        public int state_transit(ObjectCache oc) {
                if (m_player_mode.is_game_valid()) {
                        return super.m_stategraph.get_current_state();
                } else {
                        return StateGraph.c_EndState;
                }
        }
}

class R_NetInputChess extends RunnableState {

        private GameNetwork m_network;
        private PacketIdentityVerification m_local_id;
        private boolean m_has_packets;

        @Override
        public void state_init(ObjectCache oc) {
                m_network = oc.<App>use(ComponentNames.c_App).get_game_network();
                m_local_id = oc.<PacketIdentityVerification[]>use("local_players")[0];
        }

        @Override
        public void state_loop(ObjectCache oc) {
                NetworkDataPacket[] packets = m_network.client_receive_packet(m_local_id, 0X4A, false);
                if (packets.length != 0) {
                        PacketGeneralData gpacket = (PacketGeneralData) packets[0];
                        int[][] grid = new int[10][9];
                        for (int i = 0; i < 10; i++) {
                                for (int j = 0; j < 9; j++) {
                                        grid[i][j] = gpacket.read_int();
                                }
                        }
                        oc.declare("net_navigrid", grid);
                        m_has_packets = true;
                } else {
                        m_has_packets = false;
                }
        }

        @Override
        public int state_transit(ObjectCache oc) {
                if (m_has_packets) {
                        return R_DoublePlayers.States3.SaveNetInput.ordinal();
                } else {
                        return super.m_stategraph.get_current_state();
                }
        }
}

class R_SaveNetInput extends RunnableState {

        private final MoveVerifier m_verifier;
        private final PlayerMode m_player_mode;

        public R_SaveNetInput(PlayerMode player_mode, MoveVerifier verifier) {
                m_verifier = verifier;
                m_player_mode = player_mode;
        }

        @Override
        public void state_init(ObjectCache oc) {
                int[][] navigrid = oc.<int[][]>use("net_navigrid");
                m_verifier.import_navigrid(navigrid);
        }

        @Override
        public void state_loop(ObjectCache oc) {
                // do nothing
        }

        @Override
        public int state_transit(ObjectCache oc) {
                m_player_mode.notify_next();
                return StateGraph.c_EndState;
        }

}

/**
 * 线上双人游戏
 *
 * @author davis
 */
public class R_DoublePlayers extends RunnableState implements PlayerMode {

        private final OnlinePlayer[] m_players;
        private final int m_win_id;
        private final int m_lost_id;
        private final MoveVerifier m_verifier;
        private int m_counter;
        private boolean m_go_next = true;
        private boolean m_is_game_valid = true;
        private GameRoom m_roomgui = null;
        private final boolean m_isonline;

        public R_DoublePlayers(int win_state_id, int lost_state_id, boolean is_online) {
                m_isonline = is_online;
                m_players = new OnlinePlayer[2];
                m_players[0] = null;
                m_players[1] = null;

                m_win_id = win_state_id;
                m_lost_id = lost_state_id;
                m_verifier = new MoveVerifier();
        }

        @Override
        public boolean is_game_valid() {
                return m_is_game_valid;
        }

        @Override
        public Player get_player() {
                return m_players[(m_counter + 1) & 1];
        }

        @Override
        public void notify_next() {
                m_go_next = true;
        }

        @Override
        public void notify_win() {
                get_player().win();
        }

        @Override
        public void notify_lost() {
                get_player().lose();
        }

        public enum States {

                SelectChess,
                MoveChess,
                VerifyChess,
                UploadChess,
        }

        public enum States3 {

                NetInputChess,
                SaveNetInput
        }

        public enum States2 {

                UpdateChess
        }

        @Override
        public void state_init(ObjectCache oc) {
                m_roomgui = oc.<GameRoom>use("roomgui");

                // 构建双人玩家
                PacketIdentityVerification[] locals = oc.<PacketIdentityVerification[]>use("local_players");
                StateGraph local_sg = new StateGraph();
                local_sg.add_state(new R_SelectChess(this, m_verifier), States.SelectChess.ordinal());
                local_sg.add_state(new R_MoveChess(), States.MoveChess.ordinal());
                local_sg.add_state(new R_VerifyChess(this, m_verifier), States.VerifyChess.ordinal());
                local_sg.add_state(new R_UploadChess(this, m_verifier), States.UploadChess.ordinal());

                PacketIdentityVerification[] nets = oc.<PacketIdentityVerification[]>use("net_players");
                StateGraph net_sg = new StateGraph();
                net_sg.add_state(new R_NetInputChess(), States3.NetInputChess.ordinal());
                net_sg.add_state(new R_SaveNetInput(this, m_verifier), States3.SaveNetInput.ordinal());

                Player.PlayerSide[] side = new Player.PlayerSide[2];
                side[0] = Player.PlayerSide.RedSide;
                side[1] = Player.PlayerSide.BlueSide;

                if (oc.<Boolean>use("is_inviter")) {
                        m_players[0] = new OnlinePlayer(side[0], local_sg, locals[0]);
                        if (!m_isonline) {
                                m_players[1] = new OnlinePlayer(side[1], local_sg, nets[0]);
                        } else {
                                m_players[1] = new OnlinePlayer(side[1], net_sg, nets[0]);
                        }
                } else {
                        m_players[1] = new OnlinePlayer(side[1], local_sg, locals[0]);
                        if (!m_isonline) {
                                m_players[0] = new OnlinePlayer(side[0], local_sg, nets[0]);
                        } else {
                                m_players[0] = new OnlinePlayer(side[0], net_sg, nets[0]);
                        }
                }
                m_counter = 0;

                App app = oc.<App>use("app");
                StateGraph global_sg = new StateGraph();
                global_sg.add_state(new R_UpdateChess(this, m_verifier), States2.UpdateChess.ordinal());
                app.get_state_machine().add_state_graph("Update_Chess_State", global_sg);
        }

        @Override
        public void state_loop(ObjectCache oc) {
                if (m_go_next) {
                        App app = oc.<App>use("app");
                        app.get_state_machine().remove_state_graph("Player_State");
                        app.get_state_machine().
                            add_state_graph("Player_State", m_players[m_counter++ & 1].get_stategraph());
                        m_go_next = false;
                }
        }

        private void end_game(ObjectCache oc) {
                App app = oc.<App>use("app");
                app.get_state_machine().remove_state_graph("Player_State");
                m_is_game_valid = false;
        }

        @Override
        public int state_transit(ObjectCache oc) {
                if (m_players[0].is_win() || m_players[1].is_lost()) {
                        JOptionPane.showMessageDialog(null, "玩家"
                            + m_players[0].get_side().name() + "胜利，玩家"
                            + m_players[1].get_side().name() + "失败");
                        end_game(oc);
                        return m_win_id;
                } else if (m_players[0].is_lost() || m_players[1].is_win()) {
                        JOptionPane.showMessageDialog(null, "玩家"
                            + m_players[0].get_side().name() + "失败，玩家"
                            + m_players[1].get_side().name() + "胜利");
                        end_game(oc);
                        return m_lost_id;
                } else if (!m_roomgui.isDisplayable()) {
                        JOptionPane.showMessageDialog(null, "玩家"
                            + m_players[m_counter & 1].get_side().name() + "认输");
                        end_game(oc);
                        return m_lost_id;
                } else {
                        m_is_game_valid = true;
                        return super.m_stategraph.get_current_state();
                }
        }

}
