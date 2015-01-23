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
import GameCore.Plugin;
import GameCore.RunnableState;
import GameCore.StateGraph;

class R_ChatMain extends RunnableState {

        @Override
        public void state_init(ObjectCache oc) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void state_loop(ObjectCache oc) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public int state_transit(ObjectCache oc) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

}

/**
 * 在线聊天插件
 *
 * @author davis
 */
public class Plugin_OnlineChat implements Plugin {

        @Override
        public String get_name() {
                return getClass().getName();
        }

        @Override
        public StateGraph import_state_graph(App app) {
                StateGraph chat_sg = new StateGraph();
                chat_sg.add_state(new R_ChatMain(), 0);
                return chat_sg;
        }
}
