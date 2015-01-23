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

class LoginState extends RunnableState {

        private Login m_login;

        @Override
        public void state_init(ObjectCache oc) {
                m_login = new Login(oc);
                m_login.setVisible(true);
                oc.declare("logingui", m_login);
        }

        @Override
        public void state_loop(ObjectCache oc) {
                // do nothing
        }

        @Override
        public int state_transit(ObjectCache oc) {
                if (m_login.isDisplayable()) {
                        return super.m_stategraph.get_current_state();
                } else {
                        if (m_login.is_entering()) {
                                return App.AppStates.PluginConfigState.ordinal();
                        } else {
                                return StateGraph.c_EndState;
                        }
                }
        }

}

class PluginConfigState extends RunnableState {

        @Override
        public void state_init(ObjectCache oc) {
                App app = oc.<App>use("app");
                Login logingui = oc.<Login>use("logingui");

                PluginLoader loader = app.get_plugin_loader();
                if (logingui.is_checked_cpu_alleviator()) {
                        loader.enable_plugin(PluginLoader.c_CpuAlleviator);
                }
                if (logingui.is_checked_chatter()) {
                        loader.enable_plugin(PluginLoader.c_OnlineChat);
                }
                if (logingui.is_checked_online_double_player()) {
                        loader.enable_plugin(PluginLoader.c_OnlineChineseChess);
                }
                if (logingui.is_checked_offline_double_player()) {
                        loader.enable_plugin(PluginLoader.c_OfflineChineseChess);
                }
        }

        @Override
        public void state_loop(ObjectCache oc) {
                // do nothing
        }

        @Override
        public int state_transit(ObjectCache oc) {
                return App.AppStates.GameStates.ordinal();
        }

}


/**
 * 控制游戏程序运行状态
 *
 * @author Wen, Chifeng
 */
public class App {

        public enum AppStates {

                LoginState,
                PluginConfigState,
                GameStates,
        }

        public static final String c_VersionString = "Chess game framework v0.2 alpha";

        private final StateMachine m_appstatemachine;
        private final ObjectCache m_global_oc;
        private final GameGraphics m_gamegraphics;
        private final GameTimer m_gametimer;
        private final GameNetwork m_gamenetwork;
        private final GameAudio m_gameaudio;
        private final PluginLoader m_pluginloader;

        /**
         * 新建一个游戏程序.
         */
        public App() {
                m_appstatemachine = new StateMachine();
                m_global_oc = new ObjectCache();
                m_gamegraphics = new GameGraphics();
                m_gametimer = new GameTimer();
                m_gamenetwork = new GameNetwork();
                m_gameaudio = new GameAudio();
                m_pluginloader = new PluginLoader();
                // 游戏程序进程
                StateGraph app_process = new StateGraph();
                app_process.add_state(new LoginState(), AppStates.LoginState.ordinal());
                app_process.add_state(new PluginConfigState(), AppStates.PluginConfigState.ordinal());
                app_process.add_state(new Game(), AppStates.GameStates.ordinal());
                m_appstatemachine.push_state_graph();
                m_appstatemachine.add_state_graph("游戏程序进程", app_process);

                m_global_oc.declare("app", this);
                m_global_oc.declare("app_process", app_process);
        }

        /**
         * 运行游戏程序.
         */
        public void run() {
                m_appstatemachine.run(m_global_oc);
                m_gamenetwork.close();
        }

        /**
         * @return GameGraphics 实例
         */
        public GameGraphics get_game_graphics() {
                return m_gamegraphics;
        }

        /**
         * @return GameTimer 实例
         */
        public GameTimer get_game_timer() {
                return m_gametimer;
        }

        /**
         * @return GameNetwork 实例
         */
        public GameNetwork get_game_network() {
                return m_gamenetwork;
        }

        /**
         * @return GameAudio 实例
         */
        public GameAudio get_game_audio() {
                return m_gameaudio;
        }

        /**
         * @return PluginLoader 实例
         */
        public PluginLoader get_plugin_loader() {
                return m_pluginloader;
        }

        /**
         * @return app状态机
         */
        public StateMachine get_state_machine() {
                return m_appstatemachine;
        }
}
