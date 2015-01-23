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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ConcurrentLinkedDeque;
import javax.swing.Timer;

/**
 * 计时器
 *
 * @author Wen, Chifeng <https://sourceforge.net/u/daviesx/profile/>
 */
public class GameTimer implements ActionListener {

        private final Timer m_timer;
        private int m_counter = 0;
        private final ConcurrentLinkedDeque<ActionListener> m_events;

        public GameTimer() {
                m_events = new ConcurrentLinkedDeque();
                m_timer = new Timer(1, this);
                m_timer.start();
        }

        public GameTimer(int interval) {
                m_events = new ConcurrentLinkedDeque();
                m_timer = new Timer(interval, this);
                m_timer.start();
        }

        /**
         * 添加时钟事件
         *
         * @param listener
         */
        public void add_event(ActionListener listener) {
                m_events.add(listener);
        }

        public int get_counter() {
                return m_counter;
        }

        public void reset() {
                m_counter = 0;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
                m_counter++;
                for (ActionListener listener : m_events) {
                        listener.actionPerformed(e);
                }
        }
}
