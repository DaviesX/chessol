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

import java.awt.Graphics;
import java.awt.Image;

/**
 * 方形图像可绘制物
 *
 * @author Wen, Chifeng <https://sourceforge.net/u/daviesx/profile/>
 */
public class DrawableImage implements Drawable {

        private FRect2d m_rect;
        private DrawableAnimator m_animator;

        public DrawableImage(FRect2d rect, Image img) {
        }

        public void set_rect(FRect2d rect) {
                m_rect = rect;
        }

        public void set_rect(FRect2d rect, FPoint2d scale) {
        }

        public FRect2d get_rect() {
                return m_rect;
        }

        public FRect2d get_rect(FPoint2d scale) {
                return m_rect;
        }

        public void attach_animator(DrawableAnimator anim) {
        }

        @Override
        public void draw(Graphics g, FPoint2d scale) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

}
