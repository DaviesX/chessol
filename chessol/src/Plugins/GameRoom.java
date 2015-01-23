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

import GameCore.App;
import GameCore.ComponentNames;
import GameCore.GameGraphics;
import GameCore.GameNetwork;
import GameCore.IPoint2d;
import GameCore.ObjectCache;
import GameCore.PacketIdentityVerification;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.Timer;

class MyMouseListener implements MouseListener {

        protected int m_mouse_x_pos = -100;
        protected int m_mouse_y_pos = -100;

        @Override
        public void mouseClicked(MouseEvent e) {
                m_mouse_x_pos = e.getX();
                m_mouse_y_pos = e.getY();
        }

        @Override
        public void mousePressed(MouseEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void mouseReleased(MouseEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void mouseEntered(MouseEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void mouseExited(MouseEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
}

/**
 * 游戏房间 GUI
 *
 * @author Zhenghui Ou
 */
public class GameRoom extends javax.swing.JFrame implements WindowListener {

        private final GameGraphics m_graphics;
        private final GameNetwork m_network;
        private final MyMouseListener m_mouse;

        private final PacketIdentityVerification m_local;
        private final PacketIdentityVerification m_opponent;

        private final Timer m_timer;

        @Override
        public void windowOpened(WindowEvent e) {
        }

        @Override
        public void windowClosing(WindowEvent e) {
                m_timer.stop();
        }

        @Override
        public void windowClosed(WindowEvent e) {
        }

        @Override
        public void windowIconified(WindowEvent e) {
        }

        @Override
        public void windowDeiconified(WindowEvent e) {
        }

        @Override
        public void windowActivated(WindowEvent e) {
        }

        @Override
        public void windowDeactivated(WindowEvent e) {
        }

        class Refresh implements ActionListener {

                @Override
                public void actionPerformed(ActionEvent e) {
                        PacketIdentityVerification[] users = m_network.client_query_online_users(m_local);
                        String[] user_name = new String[users.length];
                        for (int i = 0; i < users.length; i++) {
                                user_name[i] = users[i].id();
                        }
                        lt_user_list.setListData(user_name);
                }
        }

        /**
         * Creates new form Room
         *
         * @param oc
         */
        public GameRoom(ObjectCache oc) {
                /* Set the Nimbus look and feel */
                //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
                /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
                 * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
                 */
                try {
                        for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                                if ("Nimbus".equals(info.getName())) {
                                        javax.swing.UIManager.setLookAndFeel(info.getClassName());
                                        break;
                                }
                        }
                } catch (ClassNotFoundException ex) {
                        java.util.logging.Logger.getLogger(GameHall.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                } catch (InstantiationException ex) {
                        java.util.logging.Logger.getLogger(GameHall.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                } catch (IllegalAccessException ex) {
                        java.util.logging.Logger.getLogger(GameHall.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                } catch (javax.swing.UnsupportedLookAndFeelException ex) {
                        java.util.logging.Logger.getLogger(GameHall.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                }
                //</editor-fold>
                initComponents();

                super.setTitle(App.c_VersionString);

                App app = oc.<App>use("app");
                m_graphics = app.get_game_graphics();
                m_network = app.get_game_network();

                m_graphics.setSize(chessboard.getWidth(), chessboard.getHeight());
                m_graphics.setDoubleBuffered(rootPaneCheckingEnabled);
                chessboard.add(m_graphics);

                m_mouse = new MyMouseListener();
                m_graphics.addMouseListener(m_mouse);

                GameHall hallgui = oc.<GameHall>use(ComponentNames.c_Hall_GUI);
                m_local = hallgui.get_friend_players_id()[0];
                m_opponent = hallgui.get_opponent_players_id()[0];
                lb_server_info.setText(m_local.id() + " vs " + m_opponent.id());

                m_timer = new Timer(10000, new Refresh());
                m_timer.start();
        }

        /**
         * 获得鼠标单击位置
         *
         * @return
         */
        public IPoint2d getMouseClickedPosition() {
                IPoint2d p = new IPoint2d(m_mouse.m_mouse_x_pos, m_mouse.m_mouse_y_pos);
                m_mouse.m_mouse_x_pos = -100;
                m_mouse.m_mouse_y_pos = -100;
                return p;
        }

        /**
         * This method is called from within the constructor to initialize the
         * form. WARNING: Do NOT modify this code. The content of this method is
         * always regenerated by the Form Editor.
         */
        @SuppressWarnings("unchecked")
        // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
        private void initComponents() {

                jTabbedPane1 = new javax.swing.JTabbedPane();
                jPanel1 = new javax.swing.JPanel();
                jTabbedPane2 = new javax.swing.JTabbedPane();
                jPanel2 = new javax.swing.JPanel();
                jTabbedPane3 = new javax.swing.JTabbedPane();
                jPanel3 = new javax.swing.JPanel();
                jPanel4 = new javax.swing.JPanel();
                jPanel7 = new javax.swing.JPanel();
                lb_server_info = new javax.swing.JLabel();
                force_close_button = new javax.swing.JButton();
                chessboard = new javax.swing.JPanel();
                jPanel9 = new javax.swing.JPanel();
                bt_悔棋 = new javax.swing.JButton();
                bt_投降 = new javax.swing.JButton();
                bt_认输 = new javax.swing.JButton();
                bt_求和 = new javax.swing.JButton();
                lb_timer_display = new javax.swing.JLabel();
                jTabbedPane4 = new javax.swing.JTabbedPane();
                jPanel5 = new javax.swing.JPanel();
                jScrollPane3 = new javax.swing.JScrollPane();
                lt_user_list = new javax.swing.JList();
                jTabbedPane5 = new javax.swing.JTabbedPane();
                jPanel6 = new javax.swing.JPanel();
                jScrollPane1 = new javax.swing.JScrollPane();
                ta_messages = new javax.swing.JTextArea();
                tf_send_input = new javax.swing.JTextField();

                setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

                javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
                jPanel2.setLayout(jPanel2Layout);
                jPanel2Layout.setHorizontalGroup(
                        jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 156, Short.MAX_VALUE)
                );
                jPanel2Layout.setVerticalGroup(
                        jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 269, Short.MAX_VALUE)
                );

                jTabbedPane2.addTab("对手", jPanel2);

                jTabbedPane3.setBackground(new java.awt.Color(102, 102, 255));
                jTabbedPane3.setForeground(new java.awt.Color(0, 102, 255));

                javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
                jPanel3.setLayout(jPanel3Layout);
                jPanel3Layout.setHorizontalGroup(
                        jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 156, Short.MAX_VALUE)
                );
                jPanel3Layout.setVerticalGroup(
                        jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 297, Short.MAX_VALUE)
                );

                jTabbedPane3.addTab("自己", jPanel3);

                lb_server_info.setText("jLabel1");

                force_close_button.setText("强退");
                force_close_button.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                force_close_buttonActionPerformed(evt);
                        }
                });

                javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
                jPanel7.setLayout(jPanel7Layout);
                jPanel7Layout.setHorizontalGroup(
                        jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel7Layout.createSequentialGroup()
                                .addGap(24, 24, 24)
                                .addComponent(lb_server_info)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(force_close_button)
                                .addGap(32, 32, 32))
                );
                jPanel7Layout.setVerticalGroup(
                        jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                                .addGap(0, 16, Short.MAX_VALUE)
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lb_server_info)
                                        .addComponent(force_close_button)))
                );

                chessboard.setPreferredSize(new java.awt.Dimension(452, 501));
                chessboard.addComponentListener(new java.awt.event.ComponentAdapter() {
                        public void componentResized(java.awt.event.ComponentEvent evt) {
                                chessboardComponentResized(evt);
                        }
                });

                javax.swing.GroupLayout chessboardLayout = new javax.swing.GroupLayout(chessboard);
                chessboard.setLayout(chessboardLayout);
                chessboardLayout.setHorizontalGroup(
                        chessboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 0, Short.MAX_VALUE)
                );
                chessboardLayout.setVerticalGroup(
                        chessboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 0, Short.MAX_VALUE)
                );

                bt_悔棋.setText("悔棋");

                bt_投降.setText("投降");

                bt_认输.setText("认输");

                bt_求和.setText("求和");

                lb_timer_display.setText("时间：");

                javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
                jPanel9.setLayout(jPanel9Layout);
                jPanel9Layout.setHorizontalGroup(
                        jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel9Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(bt_悔棋, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(bt_投降, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(bt_认输, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(bt_求和, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 153, Short.MAX_VALUE)
                                .addComponent(lb_timer_display, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
                );
                jPanel9Layout.setVerticalGroup(
                        jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                                .addContainerGap(14, Short.MAX_VALUE)
                                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(bt_悔棋)
                                        .addComponent(bt_投降)
                                        .addComponent(bt_认输)
                                        .addComponent(bt_求和)
                                        .addComponent(lb_timer_display))
                                .addContainerGap())
                );

                javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
                jPanel4.setLayout(jPanel4Layout);
                jPanel4Layout.setHorizontalGroup(
                        jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(chessboard, javax.swing.GroupLayout.DEFAULT_SIZE, 488, Short.MAX_VALUE)
                );
                jPanel4Layout.setVerticalGroup(
                        jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(chessboard, javax.swing.GroupLayout.DEFAULT_SIZE, 562, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                );

                jScrollPane3.setViewportView(lt_user_list);

                javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
                jPanel5.setLayout(jPanel5Layout);
                jPanel5Layout.setHorizontalGroup(
                        jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 222, Short.MAX_VALUE)
                );
                jPanel5Layout.setVerticalGroup(
                        jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 189, Short.MAX_VALUE)
                );

                jTabbedPane4.addTab("用户列表", jPanel5);

                ta_messages.setEditable(false);
                ta_messages.setColumns(20);
                ta_messages.setRows(5);
                jScrollPane1.setViewportView(ta_messages);

                tf_send_input.setText("jTextField1");

                javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
                jPanel6.setLayout(jPanel6Layout);
                jPanel6Layout.setHorizontalGroup(
                        jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jScrollPane1)
                                        .addComponent(tf_send_input))
                                .addGap(0, 0, Short.MAX_VALUE))
                );
                jPanel6Layout.setVerticalGroup(
                        jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 350, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tf_send_input, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(6, 6, 6))
                );

                jTabbedPane5.addTab("聊天", jPanel6);

                javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
                jPanel1.setLayout(jPanel1Layout);
                jPanel1Layout.setHorizontalGroup(
                        jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(jTabbedPane3)
                                        .addComponent(jTabbedPane2))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jTabbedPane4)
                                        .addComponent(jTabbedPane5)))
                );
                jPanel1Layout.setVerticalGroup(
                        jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTabbedPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jTabbedPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTabbedPane5))
                );

                jTabbedPane1.addTab("象棋游戏", jPanel1);

                javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
                getContentPane().setLayout(layout);
                layout.setHorizontalGroup(
                        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jTabbedPane1)
                );
                layout.setVerticalGroup(
                        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jTabbedPane1)
                );

                pack();
        }// </editor-fold>//GEN-END:initComponents

        private void chessboardComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_chessboardComponentResized
                // 刷新GameGraphics大小
                if (m_graphics != null) {
                        m_graphics.setSize(chessboard.getWidth(), chessboard.getHeight());
                }
        }//GEN-LAST:event_chessboardComponentResized

        private void force_close_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_force_close_buttonActionPerformed
                // TODO add your handling code here:
                super.dispose();
        }//GEN-LAST:event_force_close_buttonActionPerformed

        // Variables declaration - do not modify//GEN-BEGIN:variables
        private javax.swing.JButton bt_悔棋;
        private javax.swing.JButton bt_投降;
        private javax.swing.JButton bt_求和;
        private javax.swing.JButton bt_认输;
        private javax.swing.JPanel chessboard;
        private javax.swing.JButton force_close_button;
        private javax.swing.JPanel jPanel1;
        private javax.swing.JPanel jPanel2;
        private javax.swing.JPanel jPanel3;
        private javax.swing.JPanel jPanel4;
        private javax.swing.JPanel jPanel5;
        private javax.swing.JPanel jPanel6;
        private javax.swing.JPanel jPanel7;
        private javax.swing.JPanel jPanel9;
        private javax.swing.JScrollPane jScrollPane1;
        private javax.swing.JScrollPane jScrollPane3;
        private javax.swing.JTabbedPane jTabbedPane1;
        private javax.swing.JTabbedPane jTabbedPane2;
        private javax.swing.JTabbedPane jTabbedPane3;
        private javax.swing.JTabbedPane jTabbedPane4;
        private javax.swing.JTabbedPane jTabbedPane5;
        private javax.swing.JLabel lb_server_info;
        private javax.swing.JLabel lb_timer_display;
        private javax.swing.JList lt_user_list;
        private javax.swing.JTextArea ta_messages;
        private javax.swing.JTextField tf_send_input;
        // End of variables declaration//GEN-END:variables

}
