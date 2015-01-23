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
import GameCore.GameNetwork;
import GameCore.Login;
import GameCore.NetworkDataPacket;
import GameCore.ObjectCache;
import GameCore.PacketIdentityVerification;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

/**
 * 游戏大厅 GUI
 *
 * @author ZhengHui Ou
 */
public class GameHall extends javax.swing.JFrame {

        private boolean m_is_entering_game = false;
        private boolean m_is_inviter = true;
        private final int c_Invite_Signature = 10086;
        private PacketIdentityVerification m_friend_players_id = null;
        private PacketIdentityVerification m_opponent_players_id = null;

        private final GameNetwork m_network;
        private final ObjectCache m_oc;

        class Refresh implements ActionListener {

                @Override
                public void actionPerformed(ActionEvent e) {
                        // 本地用户
                        m_is_inviter = true;
                        
                        Login login = m_oc.<Login>use(ComponentNames.c_Login_GUI);
                        m_friend_players_id = login.get_user();

                        label_local_user_name.setText(m_friend_players_id.id());
                        label_local_user_portray.setText(m_friend_players_id.id() + "头像");
                        // 获取网络对手
                        String oppo_name = (String) lt_user_list.getSelectedValue();
                        PacketIdentityVerification[] users = m_network.client_query_online_users(m_friend_players_id);
                        String[] user_name = new String[users.length];
                        
                        for (int i = 0; i < users.length; i++) {
                                user_name[i] = users[i].id();
                                if ( oppo_name != null && oppo_name.equals(user_name[i])) {
                                       m_opponent_players_id = users[i]; 
                                }
                        }
                        // 刷新列表
                        lt_user_list.setListData(user_name);
                        
                        // 检查邀请
                        NetworkDataPacket[] input = 
                            m_network.client_receive_packet(m_friend_players_id, c_Invite_Signature, false);
                        if ( input.length != 0 ) {
                                PacketIdentityVerification inviter = (PacketIdentityVerification) input[0];
                                int ans = JOptionPane.showConfirmDialog(null, "接受" +inviter.id() + "的邀请吗?");
                                if ( ans == JOptionPane.YES_OPTION ) {
                                        m_opponent_players_id = inviter;
                                        m_is_inviter = false;
                                }
                        }
                        
                        // 显示对手
                        if (m_opponent_players_id != null) {
                                label_opponent_name.setText(m_opponent_players_id.id());
                                label_opponent_user_name.setText(m_opponent_players_id.id());
                                label_opponent_user_portray.setText(m_opponent_players_id.id() + "头像");
                        }
                }
        }

        /**
         * Creates new form room
         *
         * @param oc
         */
        public GameHall(ObjectCache oc) {
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
                
                m_oc = oc;
                m_network = oc.<App>use(ComponentNames.c_App).get_game_network();
                bt_refresh.addActionListener(new Refresh());
        }

        /**
         * 是否在进入游戏
         *
         * @return
         */
        public boolean is_entering_game() {
                return m_is_entering_game;
        }

        /**
         * 是否是房主
         *
         * @return
         */
        public boolean is_inviter() {
                return m_is_inviter;
        }

        /**
         * 队友的id
         *
         * @return
         */
        public PacketIdentityVerification[] get_friend_players_id() {
                PacketIdentityVerification[] id = {m_friend_players_id};
                return id;
        }

        /**
         * 对手的id
         *
         * @return
         */
        public PacketIdentityVerification[] get_opponent_players_id() {
                PacketIdentityVerification[] id = {m_opponent_players_id};
                return id;
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
                jPanel3 = new javax.swing.JPanel();
                label_portray = new javax.swing.JLabel();
                label_opponent_name = new javax.swing.JLabel();
                jLabel7 = new javax.swing.JLabel();
                jTabbedPane3 = new javax.swing.JTabbedPane();
                jPanel4 = new javax.swing.JPanel();
                jScrollPane2 = new javax.swing.JScrollPane();
                lt_user_list = new javax.swing.JList();
                jPanel5 = new javax.swing.JPanel();
                addButton = new javax.swing.JButton();
                exitButton = new javax.swing.JButton();
                jLabel10 = new javax.swing.JLabel();
                bt_refresh = new javax.swing.JButton();
                jSeparator1 = new javax.swing.JSeparator();
                jpanel = new javax.swing.JPanel();
                label_local_user_name = new javax.swing.JLabel();
                label_local_user_portray = new javax.swing.JLabel();
                label_opponent_user_name = new javax.swing.JLabel();
                label_opponent_user_portray = new javax.swing.JLabel();
                jLabel8 = new javax.swing.JLabel();
                jLabel9 = new javax.swing.JLabel();

                setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

                jTabbedPane1.setMaximumSize(new java.awt.Dimension(708, 272));

                label_opponent_name.setText("无");

                jLabel7.setText("头像");

                javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
                jPanel3.setLayout(jPanel3Layout);
                jPanel3Layout.setHorizontalGroup(
                        jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(19, 19, 19)
                                .addComponent(label_opponent_name)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(label_portray, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                );
                jPanel3Layout.setVerticalGroup(
                        jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(label_portray, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE)
                                        .addComponent(label_opponent_name))
                                .addContainerGap())
                );

                jTabbedPane2.addTab("个人信息", jPanel3);

                jScrollPane2.setViewportView(lt_user_list);

                javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
                jPanel4.setLayout(jPanel4Layout);
                jPanel4Layout.setHorizontalGroup(
                        jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 131, Short.MAX_VALUE)
                );
                jPanel4Layout.setVerticalGroup(
                        jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 8, Short.MAX_VALUE))
                );

                jTabbedPane3.addTab("服务器信息", jPanel4);

                jPanel5.setForeground(new java.awt.Color(51, 51, 255));

                addButton.setText("加入");
                addButton.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                addButtonActionPerformed(evt);
                        }
                });

                exitButton.setText("退出");
                exitButton.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                exitButtonActionPerformed(evt);
                        }
                });

                jLabel10.setText("-- 登陆游戏 --");

                bt_refresh.setText("刷新");

                javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
                jPanel5.setLayout(jPanel5Layout);
                jPanel5Layout.setHorizontalGroup(
                        jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(bt_refresh)
                                .addGap(18, 18, 18)
                                .addComponent(addButton)
                                .addGap(18, 18, 18)
                                .addComponent(exitButton)
                                .addGap(18, 18, 18))
                );
                jPanel5Layout.setVerticalGroup(
                        jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(jPanel5Layout.createSequentialGroup()
                                                .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addGap(4, 4, 4))
                                        .addGroup(jPanel5Layout.createSequentialGroup()
                                                .addGap(0, 0, Short.MAX_VALUE)
                                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(addButton)
                                                        .addComponent(exitButton)
                                                        .addComponent(bt_refresh)))))
                );

                label_local_user_name.setText("自己");
                label_local_user_name.setToolTipText("");

                label_local_user_portray.setText("自己头像");

                label_opponent_user_name.setText("对手");

                label_opponent_user_portray.setText("对手头像");

                jLabel8.setText("  -1-");

                jLabel9.setText("  -2-");

                javax.swing.GroupLayout jpanelLayout = new javax.swing.GroupLayout(jpanel);
                jpanel.setLayout(jpanelLayout);
                jpanelLayout.setHorizontalGroup(
                        jpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jpanelLayout.createSequentialGroup()
                                .addGroup(jpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jpanelLayout.createSequentialGroup()
                                                .addGap(35, 35, 35)
                                                .addComponent(label_local_user_name)
                                                .addGap(18, 18, 18)
                                                .addComponent(label_local_user_portray)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 139, Short.MAX_VALUE)
                                                .addComponent(label_opponent_user_name))
                                        .addGroup(jpanelLayout.createSequentialGroup()
                                                .addGap(65, 65, 65)
                                                .addComponent(jLabel8)
                                                .addGap(0, 0, Short.MAX_VALUE)))
                                .addGap(18, 18, 18)
                                .addGroup(jpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel9)
                                        .addComponent(label_opponent_user_portray))
                                .addGap(84, 84, 84))
                );
                jpanelLayout.setVerticalGroup(
                        jpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jpanelLayout.createSequentialGroup()
                                .addGap(41, 41, 41)
                                .addGroup(jpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(label_local_user_name, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(label_local_user_portray, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(label_opponent_user_name, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(label_opponent_user_portray, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel8)
                                        .addComponent(jLabel9))
                                .addGap(292, 292, 292))
                );

                javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
                jPanel1.setLayout(jPanel1Layout);
                jPanel1Layout.setHorizontalGroup(
                        jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jpanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jTabbedPane2)
                                        .addComponent(jTabbedPane3)))
                );

                jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jTabbedPane2, jTabbedPane3});

                jPanel1Layout.setVerticalGroup(
                        jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jTabbedPane2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTabbedPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jSeparator1)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jpanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap())
                );

                jTabbedPane1.addTab("游戏大厅", jPanel1);

                javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
                getContentPane().setLayout(layout);
                layout.setHorizontalGroup(
                        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                );
                layout.setVerticalGroup(
                        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 491, Short.MAX_VALUE)
                                .addGap(0, 0, 0))
                );

                pack();
        }// </editor-fold>//GEN-END:initComponents

        private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
                // TODO add your handling code here:
                if ( m_friend_players_id == null || m_opponent_players_id == null) {
                        JOptionPane.showMessageDialog(null, "请选择用户");
                        return ;
                }
                if ( m_friend_players_id.id().equals(m_opponent_players_id.id())) {
                        JOptionPane.showMessageDialog(null, "你将选择自己为对手！");
                }
                // 发送邀请
                m_friend_players_id.set_dest_port(m_opponent_players_id.get_this_port());
                m_network.client_send_packet(m_friend_players_id, c_Invite_Signature, m_friend_players_id);
                
                m_is_entering_game = true;
                //m_is_inviter = true;
                super.dispose();
        }//GEN-LAST:event_addButtonActionPerformed

        private void exitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitButtonActionPerformed
                // TODO add your handling code here:
                m_is_entering_game = false;
                super.dispose();
        }//GEN-LAST:event_exitButtonActionPerformed

        // Variables declaration - do not modify//GEN-BEGIN:variables
        private javax.swing.JButton addButton;
        private javax.swing.JButton bt_refresh;
        private javax.swing.JButton exitButton;
        private javax.swing.JLabel jLabel10;
        private javax.swing.JLabel jLabel7;
        private javax.swing.JLabel jLabel8;
        private javax.swing.JLabel jLabel9;
        private javax.swing.JPanel jPanel1;
        private javax.swing.JPanel jPanel3;
        private javax.swing.JPanel jPanel4;
        private javax.swing.JPanel jPanel5;
        private javax.swing.JScrollPane jScrollPane2;
        private javax.swing.JSeparator jSeparator1;
        private javax.swing.JTabbedPane jTabbedPane1;
        private javax.swing.JTabbedPane jTabbedPane2;
        private javax.swing.JTabbedPane jTabbedPane3;
        private javax.swing.JPanel jpanel;
        private javax.swing.JLabel label_local_user_name;
        private javax.swing.JLabel label_local_user_portray;
        private javax.swing.JLabel label_opponent_name;
        private javax.swing.JLabel label_opponent_user_name;
        private javax.swing.JLabel label_opponent_user_portray;
        private javax.swing.JLabel label_portray;
        private javax.swing.JList lt_user_list;
        // End of variables declaration//GEN-END:variables
}
