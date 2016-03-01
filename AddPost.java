/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spectrex;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import static spectrex.AdminMain.newMed_ComboBoxCategory;
import static spectrex.AdminMain.newMed_ComboBoxPost;

/**
 *
 * @author Kirill
 */
public class AddPost extends javax.swing.JFrame {

    public Connection cn = MedLogin.cn;
    public int idhs = MedLogin.idhs;
    public AddPost() {
        initComponents();
        java.awt.Dimension dim = getToolkit().getScreenSize();
        this.setLocation(dim.width/2 - this.getWidth()/2,
                dim.height/2 - this.getHeight()/2);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        TextFieldPost = new javax.swing.JTextField();
        ButtonAddPost = new javax.swing.JButton();
        ButtonBackToMenu = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("spectrex/Bundle"); // NOI18N
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("AddPost.jPanel1.border.title"))); // NOI18N

        jLabel1.setText(bundle.getString("AddPost.jLabel1.text")); // NOI18N

        TextFieldPost.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TextFieldPostActionPerformed(evt);
            }
        });

        ButtonAddPost.setText(bundle.getString("AddPost.ButtonAddPost.text")); // NOI18N
        ButtonAddPost.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonAddPostActionPerformed(evt);
            }
        });

        ButtonBackToMenu.setText(bundle.getString("AddPost.ButtonBackToMenu.text")); // NOI18N
        ButtonBackToMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonBackToMenuActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(ButtonAddPost, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 43, Short.MAX_VALUE)
                        .addComponent(ButtonBackToMenu))
                    .addComponent(TextFieldPost))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(TextFieldPost, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ButtonAddPost)
                    .addComponent(ButtonBackToMenu))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void ButtonAddPostActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonAddPostActionPerformed
        String postName;
         
         String Sql = "";
         
         postName = TextFieldPost.getText();
 
        Sql = "insert into post (PostName) values "
                + "(?)";
        try {
            PreparedStatement ps = cn.prepareStatement(Sql);
            ps.setString(1, postName);
            
            int n = ps.executeUpdate();

            if(n > 0)
            {
                JOptionPane.showMessageDialog(null, "ok");
            }
            
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
                  
            try {                                               
            Statement s = cn.createStatement();
            java.util.Date now = new java.util.Date();
            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat format2 = new SimpleDateFormat("HH:mm:ss");
            String d1 = format1.format(now);
            String t1 = format2.format(now);
            System.out.println(d1 + " " + t1);
         
            
  
            Sql = "insert into journal (DateOfWork, TimeOfWork, IdHS, Enter, Quit, InputDataInTable, UseQuery) values (?, ?, ?, ?, ?, ?, ?)";
          
            try {
                PreparedStatement ps = cn.prepareStatement(Sql);
                ps.setString(1, d1);
                ps.setString(2, t1);
                ps.setInt(3, idhs);
                ps.setString(4, "0");
                ps.setString(5, "0");
                ps.setString(6, "post");
                ps.setString(7, "insert into post values(...)");
                
                int n = ps.executeUpdate();
                
                if(n <= 0)
                {
                    JOptionPane.showMessageDialog(null, "message_journal_BAD_QueryEntry");
                }
            }
                         catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex);
            }
                    
            }      
                           catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex);
                           }
        try {
            Statement s = cn.createStatement();
            ResultSet rs = s.executeQuery("SELECT PostName FROM post");
            newMed_ComboBoxPost.removeAllItems();   
            MedRegistration.ComboBoxPost.removeAllItems(); 
            while (rs.next()) {
                newMed_ComboBoxPost.addItem(rs.getString("PostName"));           
                MedRegistration.ComboBoxPost.addItem(rs.getString("PostName")); 
        }
        } catch (SQLException ex) {
            Logger.getLogger(PatientCard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_ButtonAddPostActionPerformed

    private void ButtonBackToMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonBackToMenuActionPerformed
        this.dispose();
    }//GEN-LAST:event_ButtonBackToMenuActionPerformed

    private void TextFieldPostActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TextFieldPostActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TextFieldPostActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
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
            java.util.logging.Logger.getLogger(AddPost.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AddPost.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AddPost.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AddPost.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AddPost().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton ButtonAddPost;
    private javax.swing.JButton ButtonBackToMenu;
    private javax.swing.JTextField TextFieldPost;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}