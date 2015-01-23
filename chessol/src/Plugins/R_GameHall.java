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

import GameCore.ObjectCache;
import GameCore.RunnableState;
import GameCore.StateGraph;

/**
 *
 * @author Wen, Chifeng <https://sourceforge.net/u/daviesx/profile/>
 */
public class R_GameHall extends RunnableState {

        private GameHall m_hallgui;
        private final int m_enter_game_state;

        public R_GameHall(int enter_game_state) {
                m_enter_game_state = enter_game_state;
        }

        @Override
        public void state_init(ObjectCache oc) {
                m_hallgui = new GameHall(oc);
                m_hallgui.setVisible(true);
                oc.declare("hallgui", m_hallgui);
        }

        @Override
        public void state_loop(ObjectCache oc) {
                // do nothing
        }

        @Override
        public int state_transit(ObjectCache oc) {
                if (m_hallgui.isDisplayable()) {
                        return super.m_stategraph.get_current_state();
                } else {
                        if (m_hallgui.is_entering_game()) {
                                return m_enter_game_state;
                        } else {
                                return StateGraph.c_EndState;
                        }
                }
        }
}

