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
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * 对象缓存
 *
 * @author davis
 */
public class ObjectCache {

        private final Set m_objects;

        private class Data implements Comparable<Data> {

                public int m_name_hash;
                public String m_name;
                public Object m_obj;

                Data(String name, Object obj) {
                        m_name_hash = name.hashCode();
                        m_name = name;
                        m_obj = obj;
                }

                @Override
                public int compareTo(Data o) {
                        return this.m_name.compareTo(o.m_name);
                }
        }

        private Data find(String name) {
                int hash = name.hashCode();
                for (Iterator<Data> it = m_objects.iterator(); it.hasNext();) {
                        Data tmp = it.next();
                        if (tmp.m_name_hash == hash && tmp.m_name.equals(name)) {
                                return tmp;
                        }
                }
                return null;
        }

        public ObjectCache() {
                m_objects = new ConcurrentSkipListSet();
                m_objects.clear();
        }

        public void declare(String name, Object obj) {
                Data exist = find(name);
                if (exist != null) {
                        exist.m_obj = obj;
                } else {
                        m_objects.add(new Data(name, obj));
                }
        }

        public <T> T use(String name) {
                Data tmp = find(name);
                if (tmp != null) {
                        return (T) tmp.m_obj;
                } else {
                        return null;
                }
        }

        public boolean undeclare(String name) {
                Data exist = find(name);
                if (exist != null) {
                        return m_objects.remove(exist);
                } else {
                        return false;
                }
        }

        public Iterator iterator() {
                return m_objects.iterator();
        }

        public <T> T get_next(Iterator it) {
                if (it.hasNext()) {
                        Data tmp = (Data) it.next();
                        return (T) tmp.m_obj;
                } else {
                        return null;
                }
        }

        public boolean is_empty() {
                return m_objects.isEmpty();
        }
        /*
         public boolean is_contain(String name) {
         return m_objects.
         }*/
}
