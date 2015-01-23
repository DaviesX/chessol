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
 * 运行状态机
 *
 * @author davis
 */
public abstract class RunnableState {

        protected StateGraph m_stategraph = null;

        public void on_add(StateGraph sg) {
                m_stategraph = sg;
        }

        abstract public void state_init(ObjectCache oc);

        abstract public void state_loop(ObjectCache oc);

        abstract public int state_transit(ObjectCache oc);
}
