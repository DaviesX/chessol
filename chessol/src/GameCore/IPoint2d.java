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
 *
 * @author Wen, Chifeng
 */
public class IPoint2d extends Object {

        public int x;
        public int y;

        public IPoint2d() {
                this.x = 0;
                this.y = 0;
        }

        public IPoint2d(int x, int y) {
                this.x = x;
                this.y = y;
        }

        @Override
        public String toString() {
                return "(" + x + "," + y + ")";
        }
}
