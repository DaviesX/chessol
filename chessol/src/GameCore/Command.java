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

import java.util.ArrayList;

/**
 * 虚拟命令
 *
 * @author Wen, Chifeng <https://sourceforge.net/u/daviesx/profile/>
 */
public abstract class Command extends Object {

        private final String m_cmd_name;
        private final ArrayList<Object> m_params;
        private int iterator = 0;

        public Command(String cmd_name) {
                m_cmd_name = cmd_name;
                m_params = new ArrayList();
        }

        public Command arg(Object obj) {
                m_params.add(obj);
                return this;
        }

        public void reset() {
                iterator = 0;
        }

        public String cmd_name() {
                return m_cmd_name;
        }

        public <T> T param() {
                return (T) m_params.get(iterator++);
        }

        @Override
        public String toString() {
                String value = "命令：" + m_cmd_name + " 参数：";
                int i;
                for (i = 0; i < m_params.size() - 1; i++) {
                        value += m_params.get(i).toString() + "，";
                }
                value += m_params.get(i).toString();
                return value;
        }
}
