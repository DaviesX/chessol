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

import com.sun.corba.se.impl.orbutil.concurrent.Mutex;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

//单个棋子的显示数据
class DrawableChess {

        private final double m_xspan = 0.07f;
        private final double m_yspan = 0.07f;
        private final double m_xpos;
        private final double m_ypos;
        Image m_img;

        public DrawableChess(double x, double y, Image image) {

                m_xpos = x;
                m_ypos = y;
                m_img = image;
        }

        public void draw(Graphics g, int xres, int yres) {
                g.drawImage(m_img,
                    (int) ((m_xpos - 0.5f * m_xspan) * xres),
                    (int) ((m_ypos - 0.5f * m_yspan) * yres),
                    (int) (m_xspan * xres),
                    (int) (m_yspan * yres),
                    null);

        }
}

class DrawableChessboard {

        private final Image m_img;

        public DrawableChessboard(Image image) {
                m_img = image;
        }

        public void draw(Graphics g, int xres, int yres) {
                g.drawImage(m_img,
                    0, 0, xres, yres,
                    0, 0, m_img.getWidth(null), m_img.getHeight(null),
                    null);
        }
}

class DrawableFPS {

        private Float m_fps = 0.0f;
        private int m_counter = 0;
        private final GameTimer m_timer;
        private final float xpos = 0.1f;
        private final float ypos = 0.1f;
        private final int c_Font_Point = 16;
        private final Color c_Color = Color.red;

        private Float get_fps() {
                float t;
                if ((t = m_timer.get_counter()) >= 1000) {
                        m_fps = 1000.0f * m_counter / t;
                        m_timer.reset();
                        m_counter = 0;
                }
                m_counter++;
                return m_fps;
        }

        public DrawableFPS() {
                this.m_timer = new GameTimer(1);
        }

        public void draw(Graphics g, int xres, int yres) {
                g.setFont(new Font("default", Font.BOLD, c_Font_Point));
                g.setColor(c_Color);
                String text = String.format("%.2f", get_fps()) + " FPS";
                g.drawString(text, (int) (xres * xpos), (int) (yres * ypos));
        }
}

//辅助类
class Point {

        public double x;
        public double y;

        public Point(double x, double y) {
                this.x = x;
                this.y = y;
        }
}

/**
 * 绘图类
 *
 * @author Zhenghui Ou
 */
public class GameGraphics extends JPanel {

        private final Point m_convert[][];
        private final Image m_img[];
        private final DrawableChess m_chess[];
        private int m_num = 0;
        private final DrawableChessboard m_chessboard;
        private final DrawableFPS m_fps;

        private final Mutex m_draw_mutex;

//现有内容：一个转换数组，内含理论坐标
        /**
         * 构造绘图类
         */
        public GameGraphics() {

                super();
                m_draw_mutex = new Mutex();
//正规化01坐标生成
                m_convert = new Point[10][9];
                for (int i = 0; i < 10; i++) {
                        for (int j = 0; j < 9; j++) {
                                m_convert[i][j] = new Point((33 + 48 * j) / (double) 452, (33 + 48 * i) / (double) 501);
                        }
                }
//一个包含所有棋子图片的数组
                m_img = new Image[33];
                try {
                        for (int i = 1; i <= 32; i++) {
                                m_img[i - 1] = ImageIO.read(new File("./res/qizi/" + i + ".gif"));
                        }
                        m_img[32] = ImageIO.read(new File("./res/qizi/select.gif"));
                } catch (IOException e) {
                        System.out.println(e.getMessage());
                }
//一个包含可画棋子的数组
                m_chess = new DrawableChess[33];
                // 背景
                Image background = null;
                try {
                        // 背景
                        background = ImageIO.read(new File("./res/qizi/xqboard.gif"));
                } catch (IOException ex) {
                        Logger.getLogger(GameGraphics.class.getName()).log(Level.SEVERE, null, ex);
                }
                m_chessboard = new DrawableChessboard(background);
                m_fps = new DrawableFPS();
        }

        public void beginDraw() {
                try {
                        m_draw_mutex.acquire();
                        m_num = 0;
                } catch (InterruptedException ex) {
                        Logger.getLogger(GameGraphics.class.getName()).log(Level.SEVERE, null, ex);
                }
        }

        public void endDraw() {
                repaint();
                m_draw_mutex.release();
        }

        /**
         * 新增一个棋子到绘制容器.
         *
         * @param x 逻辑坐标x分量.
         * @param y 逻辑坐标y分量.
         * @param type 棋子类型.
         */
        public void addChess(int x, int y, int type) {
                m_chess[m_num] = new DrawableChess(
                    m_convert[y][x].x,
                    m_convert[y][x].y,
                    m_img[type - 1]);
                m_num++;
        }

        public void addSelection(int x, int y) {
                m_chess[m_num] = new DrawableChess(
                    m_convert[y][x].x,
                    m_convert[y][x].y,
                    m_img[32]);
                m_num++;
        }

        public boolean calculateSelectedPoint(int x, int y, double error, IPoint2d selected) {
                IPoint2d tmp = new IPoint2d();
                boolean has_point = false;
                double distance = 1e10;

                for (int j = 0; j < 10; j++) {
                        for (int i = 0; i < 9; i++) {
                                double tx = m_convert[j][i].x * super.getWidth();
                                double ty = m_convert[j][i].y * super.getHeight();
                                double t = (tx - (double) x) * (tx - (double) x)
                                    + (ty - (double) y) * (ty - (double) y);
                                if (t <= error * error && t < distance) {
                                        tmp.x = i;
                                        tmp.y = j;
                                        distance = t;
                                        has_point = true;
                                }
                        }
                }

                selected.x = tmp.x;
                selected.y = tmp.y;
                return has_point;
        }

//现有内容：显示棋子的函数的调用，循环体
        /**
         * Canvas的绘制事件，绘制Drawable
         *
         * @param g
         */
        @Override
        public void paint(Graphics g)////////////////////////////////////////////关键/////////////////////////////////////////////////////////
        {
                try {
                        m_draw_mutex.acquire();
                } catch (InterruptedException ex) {
                        Logger.getLogger(GameGraphics.class.getName()).log(Level.SEVERE, null, ex);
                }
                BufferedImage back = new BufferedImage(super.getWidth(), super.getHeight(), BufferedImage.TYPE_INT_RGB);
                Graphics backbuffer = back.getGraphics();
                // 画棋盘
                m_chessboard.draw(backbuffer, super.getWidth(), super.getHeight());
                //画棋子
                for (int i = 0; i < m_num; i++) {
                        m_chess[i].draw(backbuffer, super.getWidth(), super.getHeight());
                }
                // 画fps
                m_fps.draw(backbuffer, super.getWidth(), super.getHeight());
                // 显示到前缓存
                g.drawImage(back, 0, 0, null);
                m_draw_mutex.release();
        }
}
