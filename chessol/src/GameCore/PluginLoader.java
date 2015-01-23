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

import Plugins.Plugin_OfflineChineseChess;
import Plugins.Plugin_OnlineChat;
import Plugins.Plugin_OnlineChineseChess;

/**
 * 插件载入器
 *
 * @author davis
 */
public class PluginLoader {

        public static final int c_OnlineChineseChess = 0;
        public static final int c_OnlineChat = 1;
        public static final int c_CpuAlleviator = 2;
        public static final int c_OfflineChineseChess = 3;
        private static final int c_Max = 100;

        private final boolean m_config[];

        public PluginLoader() {
                m_config = new boolean[c_Max];
                for (int i = 0; i < c_Max; i++) {
                        m_config[i] = false;
                }
        }

        public void enable_plugin(int plugin_id) {
                m_config[plugin_id] = true;
        }

        public void disable_plugin(int plugin_id) {
                m_config[plugin_id] = false;
        }

        public Plugin[] load() {
                // 计算启动的插件数
                int n_enabled = 0;
                for (int i = 0; i < c_Max - 1; i++) {
                        if (m_config[i]) {
                                n_enabled++;
                        }
                }
                // 载入插件
                Plugin[] plugins = new Plugin[n_enabled];
                int j = 0;
                for (int i = 0; i < c_Max; i++) {
                        if (m_config[i]) {
                                switch (i) {
                                        case c_OnlineChineseChess: {
                                                plugins[j++] = new Plugin_OnlineChineseChess();
                                                break;
                                        }

                                        case c_OnlineChat: {
                                                plugins[j++] = new Plugin_OnlineChat();
                                                break;
                                        }

                                        case c_CpuAlleviator: {
                                                //@TODO make this plugin
                                                break;
                                        }

                                        case c_OfflineChineseChess: {
                                                plugins[j++] = new Plugin_OfflineChineseChess();
                                        }
                                }
                        }
                }
                return plugins;
        }
}
