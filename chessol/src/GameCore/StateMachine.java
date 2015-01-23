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

import java.util.Iterator;
import java.util.Stack;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 状态图列表
 *
 * @author davis
 */
class StateGraphList {

        class StateGraphContainer {

                protected String m_name;
                protected StateGraph m_stategraph;

                private StateGraphContainer(String name, StateGraph state_graph) {
                        m_name = name;
                        m_stategraph = state_graph;
                }
        }

        private final ConcurrentLinkedQueue<StateGraphContainer> m_sglist;
        private boolean m_has_runnable;
        private int m_isg;
        //private int m_num;

        public StateGraphList() {
                m_sglist = new ConcurrentLinkedQueue();
                //m_has_runnable = false;
                m_isg = 0;
                //m_num = 0;
        }

        public void add(String name, StateGraph state_graph) {
                // state_graph.start_from(0);
                System.out.println("状态机载入:" + name);
                m_sglist.add(new StateGraphContainer(name, state_graph));
                //m_num++;
        }

        public void remove(String name) {
                for (Iterator<StateGraphContainer> i = m_sglist.iterator(); i.hasNext();) {
                        StateGraphContainer sg = i.next();
                        if (sg.m_name.equals(name)) {
                                System.out.println("状态机卸除:" + sg.m_name);
                                i.remove();
                        }
                }
        }

        public void start_from(int i) {
                // m_has_runnable = false;
                m_isg = i;
        }

        public boolean run(ObjectCache oc) {
                m_has_runnable = false;
                for (Iterator<StateGraphContainer> i = m_sglist.iterator(); i.hasNext();) {
                        StateGraphContainer sg = i.next();
                        boolean x;
                        if ((x = sg.m_stategraph.run_state(oc)) == false) {
                                System.out.println("状态机卸除:" + sg.m_name);
                                i.remove();
                        }
                        m_has_runnable = x || m_has_runnable;
                        try {
                                // @FIXME Make it a plugin
                                Thread.sleep(30);
                        } catch (InterruptedException ex) {
                                Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
                        }
                }
                return m_has_runnable;
        }
}

/**
 * 状态机
 *
 * @author davis
 */
public class StateMachine {

        private Stack<StateGraphList> m_stack = null;
        private StateGraphList m_curr_list = null;

        /**
         * 构造一个状态机.
         */
        public StateMachine() {
                m_stack = new Stack();
        }

        /**
         * 压入一个新的状态图列表.
         */
        public void push_state_graph() {
                m_stack.push(m_curr_list);
                m_curr_list = new StateGraphList();
                m_curr_list.start_from(0);
        }

        /**
         * 弹出一个新的状态图列表.
         */
        public void pop_state_graph() {
                m_curr_list = m_stack.pop();
        }

        /**
         * 添加一个状态图
         *
         * @param name
         * @param state_graph
         */
        public void add_state_graph(String name, StateGraph state_graph) {
                state_graph.start_from(0);
                m_curr_list.add(name, state_graph);
        }

        /**
         * 删除一个状态图
         *
         * @param name
         */
        public void remove_state_graph(String name) {
                m_curr_list.remove(name);
        }

        /**
         * 运行状态机.
         *
         * @param oc
         */
        public void run(ObjectCache oc) {
                // 运行，直到状态图堆栈为空
                while (m_curr_list != null) {
                        if (!m_curr_list.run(oc)) {
                                pop_state_graph();
                        }
                }
        }
}
