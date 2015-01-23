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

import GameCore.StateGraph;

/**
 * 游戏玩家
 *
 * @author Wen, Chifeng <https://sourceforge.net/u/daviesx/profile/>
 */
public class Player {

        public enum PlayerSide {

                RedSide,
                BlueSide,
                NullSide
        }

        private final PlayerSide m_side;
        private final StateGraph m_player_sg;
        private boolean m_is_win = false;
        private boolean m_is_lost = false;

        public Player(PlayerSide side, StateGraph sg) {
                m_side = side;
                m_player_sg = sg;
        }

        public PlayerSide get_side() {
                return m_side;
        }

        public StateGraph get_stategraph() {
                return m_player_sg;
        }

        public void win() {
                m_is_win = true;
                m_is_lost = false;
        }

        public void lose() {
                m_is_lost = true;
                m_is_win = false;
        }

        public boolean is_win() {
                return m_is_win;
        }

        public boolean is_lost() {
                return m_is_lost;
        }
}
