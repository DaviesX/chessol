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
 * 状态图
 *
 * @author davis
 */
public class StateGraph {

        public static final int c_EndState = -1;

        private final int c_MaximumState = 10;
        private final RunnableState m_runnable[];

        private int m_curr_id;
        private boolean m_2b_init;

        /**
         * 构造一个状态图.
         */
        public StateGraph() {
                m_runnable = new RunnableState[c_MaximumState];
                m_curr_id = 0;
                m_2b_init = true;
        }

        /**
         * 插入一个状态
         *
         * @param state RunnableState（可运行状态）
         * @param state_id 状态代号
         */
        public void add_state(RunnableState state, int state_id) {
                state.on_add(this);
                m_runnable[state_id] = state;
        }

        /**
         * 确定开始结点
         *
         * @param state_id 状态代号
         */
        public void start_from(int state_id) {
                m_curr_id = state_id;
                m_2b_init = true;
        }

        /**
         * 执行状态图
         *
         * @param oc
         * @return 状态图是否到达终结节点
         */
        public boolean run_state(ObjectCache oc) {
                if (m_curr_id == c_EndState) {
                        return false;
                }
                if (m_2b_init) {
                        try {
                                m_runnable[m_curr_id].state_init(oc);
                        } catch (NullPointerException e) {
                                System.out.println("run_state:" + m_curr_id);
                                System.out.println(e.getMessage());
                        }
                        m_2b_init = false;
                }
                m_runnable[m_curr_id].state_loop(oc);
                int new_id = m_runnable[m_curr_id].state_transit(oc);
                if (new_id != m_curr_id) {
                        m_curr_id = new_id;
                        m_2b_init = true;
                }
                return true;
        }

        /**
         * @return 当前的状态代号.
         */
        public int get_current_state() {
                return m_curr_id;
        }

        /**
         * 可运行状态
         *
         * @param <T>
         * @param state 状态代号
         * @return
         */
        public <T> T get_runnable(int state) {
                return (T) m_runnable[state];
        }
}
