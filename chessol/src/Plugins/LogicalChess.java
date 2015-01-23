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

/**
 * 逻辑棋子
 *
 * @author Wen, Chifeng <https://sourceforge.net/u/daviesx/profile/>
 */
public class LogicalChess {

        public int m_rule;

        protected final int m_x;
        protected final int m_y;
        protected final Integer m_type;

        public LogicalChess(int rule) {
                m_x = -1;
                m_y = -1;
                m_type = 0;
                m_rule = rule;
        }

        public LogicalChess(int x, int y, Integer type) {
                m_x = x;
                m_y = y;
                m_type = type;
        }
}
