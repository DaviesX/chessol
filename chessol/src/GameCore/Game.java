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
 * 进入游戏
 *
 * @author davis
 */
public class Game extends RunnableState {

        private boolean m_isrunning = true;

        public void terminate() {
                m_isrunning = false;
        }

        public boolean is_running() {
                return m_isrunning;
        }

        @Override
        public void state_init(ObjectCache oc) {
                App app = oc.<App>use("app");

                PluginLoader ploader = app.get_plugin_loader();
                Plugin[] plugins = ploader.load();

                StateMachine app_statemachine = app.get_state_machine();
                app_statemachine.push_state_graph();
                for (int i = 0; i < plugins.length; i++) {
                        app_statemachine.add_state_graph(plugins[i].get_name(),
                            plugins[i].import_state_graph(app));
                }
        }

        @Override
        public void state_loop(ObjectCache oc) {
        }

        @Override
        public int state_transit(ObjectCache oc) {
                return StateGraph.c_EndState;
                /*if (!m_isrunning) {
                 return -1;
                 } else {
                 return super.m_stategraph.get_current_state();
                 }*/
        }

}
