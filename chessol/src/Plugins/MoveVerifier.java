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

import GameCore.IPoint2d;

interface FunctionPtr {

        public abstract boolean check(IPoint2d src, IPoint2d dest, Player.PlayerSide side);
}

class 空 implements FunctionPtr {

        @Override
        public boolean check(IPoint2d src, IPoint2d dest, Player.PlayerSide side) {
                throw new UnsupportedOperationException("Not supported yet.");
        }
}

class 車 implements FunctionPtr {

        @Override
        public boolean check(IPoint2d src, IPoint2d dest, Player.PlayerSide side) {
                int dx = dest.x - src.x;
                int dy = dest.y - src.y;
                int n, ix, iy;
                if (dx == 0) {
                        // 不能穿y
                        ix = 0;
                        if (dy > 0) {
                                n = dy;
                                iy = 1;
                        } else {
                                n = -dy;
                                iy = -1;
                        }
                } else if (dy == 0) {
                        // 不能穿x
                        iy = 0;
                        if (dx > 0) {
                                n = dx;
                                ix = 1;
                        } else {
                                n = -dx;
                                ix = -1;
                        }
                } else {
                        // 不能打斜走
                        return false;
                }
                for (int i = 1; i < n; i++) {
                        int x = src.x + ix * i;
                        int y = src.y + iy * i;
                        if (MoveVerifier.m_navigrid[y][x] != 0) {
                                return false;
                        }
                }
                // 不能吃自己
                return MoveVerifier.side(dest.x, dest.y) != side;
        }
}

class 馬 implements FunctionPtr {

        @Override
        public boolean check(IPoint2d src, IPoint2d dest, Player.PlayerSide side) {
                int dx = dest.x - src.x;
                int dy = dest.y - src.y;
                if (dx * dy != 2 && dx * dy != -2) {
                        // 只能走日
                        return false;
                }
                int mx = src.x + dx / 2;
                int my = src.y + dy / 2;
                //int mx2 = mx + dx % 2;
                //int my2 = my + dy % 2;
                if (MoveVerifier.m_navigrid[my][mx] != 0 /*|| MoveVerifier.m_navigrid[my2][mx2] != 0*/) {
                        //绊马脚
                        return false;
                }
                // 不能吃自己
                return MoveVerifier.side(dest.x, dest.y) != side;
        }
}

class 象 implements FunctionPtr {

        static final int confine_y[][] = {{5, 9}, {0, 4}};

        @Override
        public boolean check(IPoint2d src, IPoint2d dest, Player.PlayerSide side) {
                int iside = side.ordinal();
                if (dest.y < confine_y[iside][0] || dest.y > confine_y[iside][1]) {
                        // 不能跑出界
                        return false;
                }
                int dx = dest.x - src.x;
                int dy = dest.y - src.y;
                if ((dx != 2 && dx != -2) || (dy != 2 && dy != -2)) {
                        // 只能走田
                        return false;
                }
                int mx = src.x + dx / 2;
                int my = src.y + dy / 2;
                if (MoveVerifier.m_navigrid[my][mx] != 0) {
                        // 塞象眼
                        return false;
                }
                // 不能吃自己
                return MoveVerifier.side(dest.x, dest.y) != side;
        }
}

class 仕 implements FunctionPtr {

        static final int confine_x[][] = {{3, 5}, {3, 5}};
        static final int confine_y[][] = {{7, 9}, {0, 2}};

        @Override
        public boolean check(IPoint2d src, IPoint2d dest, Player.PlayerSide side) {
                int iside = side.ordinal();
                if (dest.x < confine_x[iside][0] || dest.x > confine_x[iside][1]
                    || dest.y < confine_y[iside][0] || dest.y > confine_y[iside][1]) {
                        // 不能跑出界
                        return false;
                }
                int dx = dest.x - src.x;
                int dy = dest.y - src.y;
                if (dx * dy != 1 && dx * dy != -1) {
                        // 只能打斜走一格
                        return false;
                }
                // 不能吃自己
                return MoveVerifier.side(dest.x, dest.y) != side;
        }
}

class 將 implements FunctionPtr {

        static final int confine_x[][] = {{3, 5}, {3, 5}};
        static final int confine_y[][] = {{7, 9}, {0, 2}};

        static final boolean p_dead(IPoint2d p, Player.PlayerSide side) {
                if (side == Player.PlayerSide.BlueSide) {
                        return (p.x == 4 && p.y == 0)
                            || (p.x == 4 && p.y == 2)
                            || (p.x == 3 && p.y == 1)
                            || (p.x == 5 && p.y == 1);
                } else {
                        return (p.x == 4 && p.y == 7)
                            || (p.x == 4 && p.y == 9)
                            || (p.x == 3 && p.y == 8)
                            || (p.x == 5 && p.y == 8);
                }
        }

        @Override
        public boolean check(IPoint2d src, IPoint2d dest, Player.PlayerSide side) {
                int dx = dest.x - src.x;
                int dy = dest.y - src.y;
                int iside = side.ordinal();
                if (dest.x < confine_x[iside][0] || dest.x > confine_x[iside][1]
                    || dest.y < confine_y[iside][0] || dest.y > confine_y[iside][1]) {
                        // 不能跑出界，除非是长捉
                        if (MoveVerifier.m_navigrid[dest.y][dest.x] != 5
                            && MoveVerifier.m_navigrid[dest.y][dest.x] != 21 || dx != 0) {
                                return false;
                        }
                        // 判断长捉
                        int n, iy, c = 0;
                        if (dy > 0) {
                                n = dy;
                                iy = 1;
                        } else {
                                n = -dy;
                                iy = -1;
                        }
                        for (int i = 1; i < n; i++) {
                                int x = src.x;
                                int y = src.y + iy * i;
                                if (MoveVerifier.m_navigrid[y][x] != 0) {
                                        return false;
                                }
                        }
                        return true;
                }
                if (dx > 1 || dy > 1 || dx < -1 || dy < -1) {
                        // 只能走一格
                        return false;
                }
                if (p_dead(src, side) && p_dead(dest, side)) {
                        // 要在线上走
                        return false;
                }
                // 不能吃自己
                return MoveVerifier.side(dest.x, dest.y) != side;
        }
}

class 炮 implements FunctionPtr {

        @Override
        public boolean check(IPoint2d src, IPoint2d dest, Player.PlayerSide side) {
                int dx = dest.x - src.x;
                int dy = dest.y - src.y;
                int n, ix, iy, c = 0;
                if (dx == 0) {
                        // 不能穿y
                        ix = 0;
                        if (dy > 0) {
                                n = dy;
                                iy = 1;
                        } else {
                                n = -dy;
                                iy = -1;
                        }
                } else if (dy == 0) {
                        // 不能穿x
                        iy = 0;
                        if (dx > 0) {
                                n = dx;
                                ix = 1;
                        } else {
                                n = -dx;
                                ix = -1;
                        }
                } else {
                        // 不能打斜走
                        return false;
                }
                for (int i = 1; i < n; i++) {
                        int x = src.x + ix * i;
                        int y = src.y + iy * i;
                        if (MoveVerifier.m_navigrid[y][x] != 0) {
                                c++;
                        }
                }
                if (c == 0 && MoveVerifier.m_navigrid[dest.y][dest.x] == 0) {
                        return true;
                } else {
                        return c == 1 && MoveVerifier.opposite_side(dest.x, dest.y) == side;
                }
        }
}

class 兵 implements FunctionPtr {

        static final int confine_y[][] = {{5, 9}, {0, 4}};

        @Override
        public boolean check(IPoint2d src, IPoint2d dest, Player.PlayerSide side) {
                int dx = dest.x - src.x;
                int dy = dest.y - src.y;
                int iside = side.ordinal();
                if (dx != 0 && dest.y >= confine_y[iside][0] && dest.y <= confine_y[iside][1]) {
                        // 过河前不能打横
                        return false;
                }
                if (dx == 0) {
                        // 只能向前走一格
                        if (side == Player.PlayerSide.BlueSide) {
                                if (dy != 1) {
                                        return false;
                                }
                        } else if (side == Player.PlayerSide.RedSide) {
                                if (dy != -1) {
                                        return false;
                                }
                        }
                } else if (dy == 0) {
                        // 只能打横走一步
                        if (dx != 1 && dx != -1) {
                                return false;
                        }
                } else {
                        // 不能打斜走
                        return false;
                }
                // 不能吃自己
                return MoveVerifier.side(dest.x, dest.y) != side;
        }
}

class TemporalRecord {

        private final IPoint2d[][] m_last_move;
        private final int[] m_repeat;
        private final int[] m_type;

        public TemporalRecord() {
                m_type = new int[]{0, 0};
                m_repeat = new int[]{0, 0};
                m_last_move = new IPoint2d[2][2];
                for (int i = 0; i < 2; i++) {
                        for (int j = 0; j < 2; j++) {
                                m_last_move[i][j] = new IPoint2d();
                        }
                }
        }

        private void reset_last_move(IPoint2d src, IPoint2d dest, int iside, int type) {
                m_type[iside] = type;
                m_repeat[0] = 0;
                m_repeat[1] = 0;
                m_last_move[iside][0].x = src.x;
                m_last_move[iside][0].y = src.y;
                m_last_move[iside][1].x = dest.x;
                m_last_move[iside][1].y = dest.y;
        }

        public void input_move(IPoint2d src, IPoint2d dest, int iside) {
                int type = MoveVerifier.m_navigrid[src.y][src.x];
                if (type != m_type[iside]) {
                        reset_last_move(src, dest, iside, type);
                        return;
                }
                if (m_last_move[iside][0].x == src.x
                    && m_last_move[iside][0].y == src.y) {
                        if (m_last_move[iside][1].x == dest.x
                            && m_last_move[iside][1].y == dest.y) {
                                m_repeat[iside]++;
                        } else {
                                reset_last_move(src, dest, iside, type);
                        }
                } else if (m_last_move[iside][1].x == src.x
                    && m_last_move[iside][1].y == src.y) {
                        if (m_last_move[iside][0].x == dest.x
                            && m_last_move[iside][0].y == dest.y) {
                                m_repeat[iside]++;
                        } else {
                                reset_last_move(src, dest, iside, type);
                        }
                } else {
                        reset_last_move(src, dest, iside, type);
                }
        }

        public boolean check_repetition_of_3() {
                return m_repeat[0] == 3 && m_repeat[1] == 3;
        }

        public int get_repetitions(int iside) {
                return m_repeat[iside];
        }
}

/**
 * 判断移动是否有效
 *
 * @author davis
 */
public class MoveVerifier {

        private static final int c_Init_Grid[][]
            = {{17, 18, 19, 20, 21, 22, 23, 24, 25},
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 26, 0, 0, 0, 0, 0, 27, 0},
            {28, 0, 29, 0, 30, 0, 31, 0, 32},
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {12, 0, 13, 0, 14, 0, 15, 0, 16},
            {0, 10, 0, 0, 0, 0, 0, 11, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {1, 2, 3, 4, 5, 6, 7, 8, 9}};

        protected static int m_navigrid[][];
        private final FunctionPtr[] m_chess_check;
        private final TemporalRecord m_temporal;

        public MoveVerifier() {
                m_navigrid = new int[10][9];
                for (int i = 0; i < 10; i++) {
                        System.arraycopy(c_Init_Grid[i], 0, m_navigrid[i], 0, 9);
                }
                m_temporal = new TemporalRecord();
                m_chess_check = new FunctionPtr[33];
                m_chess_check[0] = new 空();

                m_chess_check[1] = new 車();
                m_chess_check[9] = new 車();
                m_chess_check[17] = new 車();
                m_chess_check[25] = new 車();

                m_chess_check[2] = new 馬();
                m_chess_check[8] = new 馬();
                m_chess_check[18] = new 馬();
                m_chess_check[24] = new 馬();

                m_chess_check[3] = new 象();
                m_chess_check[7] = new 象();
                m_chess_check[19] = new 象();
                m_chess_check[23] = new 象();

                m_chess_check[4] = new 仕();
                m_chess_check[6] = new 仕();
                m_chess_check[20] = new 仕();
                m_chess_check[22] = new 仕();

                m_chess_check[5] = new 將();
                m_chess_check[21] = new 將();

                m_chess_check[10] = new 炮();
                m_chess_check[11] = new 炮();
                m_chess_check[26] = new 炮();
                m_chess_check[27] = new 炮();

                m_chess_check[12] = new 兵();
                m_chess_check[13] = new 兵();
                m_chess_check[14] = new 兵();
                m_chess_check[15] = new 兵();
                m_chess_check[16] = new 兵();
                m_chess_check[28] = new 兵();
                m_chess_check[29] = new 兵();
                m_chess_check[30] = new 兵();
                m_chess_check[31] = new 兵();
                m_chess_check[32] = new 兵();
        }

        /**
         * 是否是己方
         *
         * @param p
         * @return
         */
        public static Player.PlayerSide side(IPoint2d p) {
                if (m_navigrid[p.y][p.x] == 0) {
                        return Player.PlayerSide.NullSide;
                } else if (m_navigrid[p.y][p.x] >= 17) {
                        return Player.PlayerSide.BlueSide;
                } else {
                        return Player.PlayerSide.RedSide;
                }
        }

        /**
         * 是否是己方
         *
         * @param x
         * @param y
         * @return
         */
        public static Player.PlayerSide side(int x, int y) {
                if (m_navigrid[y][x] == 0) {
                        return Player.PlayerSide.NullSide;
                } else if (m_navigrid[y][x] >= 17) {
                        return Player.PlayerSide.BlueSide;
                } else {
                        return Player.PlayerSide.RedSide;
                }
        }

        /**
         * 是否是对方
         *
         * @param x
         * @param y
         * @return
         */
        public static Player.PlayerSide opposite_side(int x, int y) {
                if (m_navigrid[y][x] == 0) {
                        return Player.PlayerSide.NullSide;
                } else if (m_navigrid[y][x] >= 17) {
                        return Player.PlayerSide.RedSide;
                } else {
                        return Player.PlayerSide.BlueSide;
                }
        }

        /**
         * 所选的棋子是否属于己方
         *
         * @param p
         * @param side
         * @return
         */
        public boolean is_valid(IPoint2d p, Player.PlayerSide side) {
                return (m_navigrid[p.y][p.x] != 0) && (side(p) == side);
        }

        /**
         * 计算一次移动所发生的事件
         *
         * @param side 移动方
         * @param src 起始位置
         * @param dest 目标位置
         * @param event 发生的事件
         * @return 移动的有效性
         */
        public boolean move(Player.PlayerSide side, IPoint2d src, IPoint2d dest, MoveEvent event) {
                int value = m_navigrid[src.y][src.x];
                if (value == 0 || side(src) != side) {
                        return false;
                }
                if (m_chess_check[value].check(src, dest, side)) {
                        m_temporal.input_move(src, dest, side.ordinal());
                        event.captured = m_navigrid[dest.y][dest.x];
                        event.captured_將 = event.captured == 5 || event.captured == 21;
                        event.repetition_of_3 = m_temporal.check_repetition_of_3();
                        m_navigrid[dest.y][dest.x] = m_navigrid[src.y][src.x];
                        m_navigrid[src.y][src.x] = 0;
                        return true;
                } else {
                        event.captured = 0;
                        event.captured_將 = false;
                        event.repetition_of_3 = false;
                        return false;
                }
        }

        /**
         * 生成棋盘上的所有棋子
         *
         * @return
         */
        public LogicalChess[] generate_chess() {
                LogicalChess[] tmp = new LogicalChess[32];
                int n_chesses = 0;
                for (int j = 0; j < 10; j++) {
                        for (int i = 0; i < 9; i++) {
                                if (m_navigrid[j][i] != 0) {
                                        tmp[n_chesses++] = new LogicalChess(i, j, m_navigrid[j][i]);
                                }
                        }
                }
                LogicalChess[] chesses = new LogicalChess[n_chesses];
                System.arraycopy(tmp, 0, chesses, 0, chesses.length);
                return chesses;
        }

        /**
         * 用输入的导航格覆盖现有的导航格
         *
         * @param navigrid
         */
        public void import_navigrid(int[][] navigrid) {
                for (int i = 0; i < 10; i++) {
                        for (int j = 0; j < 9; j++) {
                                m_navigrid[i][j] = navigrid[i][j];
                        }
                }
        }

        /**
         * 导出导航格
         *
         * @param navigrid
         */
        public void export_navigrid(int[][] navigrid) {
                for (int i = 0; i < 10; i++) {
                        for (int j = 0; j < 9; j++) {
                                navigrid[i][j] = m_navigrid[i][j];
                        }
                }
        }
}
