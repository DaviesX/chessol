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
import GameCore.ObjectCache;
import GameCore.RunnableState;
import GameCore.StateGraph;

/**
 *
 * @author Wen, Chifeng <https://sourceforge.net/u/daviesx/profile/>
 */
public class R_GameRoom extends RunnableState {

        enum States {

                InitOnlinePlayers,
                DoublePlayers
        };

        private GameRoom m_roomgui;
        private App m_app;
        private final int m_quit_room_state;
        private final boolean m_is_online_room;

        public R_GameRoom(int quit_room_state, boolean is_online_room) {
                m_quit_room_state = quit_room_state;
                m_is_online_room = is_online_room;
        }

        @Override
        public void state_init(ObjectCache oc) {
                m_app = oc.<App>use("app");

                m_roomgui = new GameRoom(oc);
                m_roomgui.setVisible(true);

                // 游戏进程
                R_InitOnlinePlayers init_online_players
                    = new R_InitOnlinePlayers(States.DoublePlayers.ordinal(), StateGraph.c_EndState);

                R_DoublePlayers online_double_players = new R_DoublePlayers(
                    StateGraph.c_EndState,
                    StateGraph.c_EndState, m_is_online_room);

                StateGraph game_sg = new StateGraph();
                game_sg.add_state(init_online_players, States.InitOnlinePlayers.ordinal());
                game_sg.add_state(online_double_players, States.DoublePlayers.ordinal());

                m_app.get_state_machine().add_state_graph("游戏进程", game_sg);

                oc.declare("roomgui", m_roomgui);
        }

        @Override
        public void state_loop(ObjectCache oc) {
                // do nothing
        }

        @Override
        public int state_transit(ObjectCache oc) {
                if (m_roomgui.isDisplayable()) {
                        return super.m_stategraph.get_current_state();
                } else {
                        // m_game.terminate();
                        m_app.get_state_machine().remove_state_graph("游戏进程");
                        return m_quit_room_state;
                }
        }
}
