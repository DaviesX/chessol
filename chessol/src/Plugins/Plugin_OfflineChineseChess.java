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
import GameCore.Plugin;
import GameCore.StateGraph;

/**
 *
 * @author Wen, Chifeng <https://sourceforge.net/u/daviesx/profile/>
 */
public class Plugin_OfflineChineseChess implements Plugin {

        @Override
        public String get_name() {
                return getClass().getName();
        }

        private enum States {
                GameHall,
                GameRoom
        };

        @Override
        public StateGraph import_state_graph(App app) {
                StateGraph sg = new StateGraph();
                sg.add_state(new R_GameHall(States.GameRoom.ordinal()), States.GameHall.ordinal());
                sg.add_state(new R_GameRoom(States.GameHall.ordinal(), false), States.GameRoom.ordinal());
                return sg;
        }

}
