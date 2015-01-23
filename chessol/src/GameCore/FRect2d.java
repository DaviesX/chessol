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
 * @author Wen, Chifeng <https://sourceforge.net/u/daviesx/profile/>
 */
public class FRect2d {

        public float[] p;

        public FRect2d() {
                p = new float[4];
                p[0] = 0.0f;
                p[1] = 0.0f;
                p[2] = 0.0f;
                p[3] = 0.0f;
        }

        public FRect2d(float x0, float y0, float x1, float y1) {
                p[0] = x0;
                p[1] = y0;
                p[2] = x1;
                p[3] = y1;
        }

        @Override
        public String toString() {
                return "(" + p[0] + "," + p[1] + ")" + "，（" + p[2] + "，" + p[3] + "）";
        }
}
