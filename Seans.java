/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spectrex;

//import database.Database;
//import formas.ChoosePatient;
import java.io.File;
import java.io.IOException;
import static spectrex.MedLogin.idhs;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;
import javax.swing.JFileChooser;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import static spectrex.PatientCard.idpat;



/**
 *
 * @author Kirill
 */
public class Seans extends javax.swing.JFrame {

    /**
     * Creates new form Seans
     */
    public int idhs = MedLogin.idhs;
    public int idp = ChoosePatient.idp;
    public int idkv = KVVVreg.idkvvv;
    public int iddg = ResultClassifier.dgroup;
    public String numcard = DoctorMain.numcard;
    public static int ids;
    public Connection cn = MedLogin.cn;
    //значения не меняются (0), исправить
    private String param_seans = "param_seans";
    private String param_patient = "param_patient";
    private String param_doctor = "param_doctor";

    public Seans() {
        initComponents();
        ButtonChoosePatient.setVisible(false);
        ButtonSeansReport.setVisible(false);
      //  ButtonSave.setEnabled(false);
        ButtonSendEmail.setVisible(false);
        java.awt.Dimension dim = getToolkit().getScreenSize();
        this.setLocation(dim.width/2 - this.getWidth()/2,
                dim.height/2 - this.getHeight()/2);
                         try
           {
               DriverManager.registerDriver(new com.mysql.jdbc.Driver());
            //   cn = DriverManager.getConnection("jdbc:mysql://localhost:3306/patients2", "root", "");
           }
           catch (SQLException ex) {
               Logger.getLogger(PatientRegistration.class.getName()).log(Level.SEVERE, null, ex);
           }
    }
    
    
     public Seans(int ids) {
          try {         
            Statement s = cn.createStatement();      
            ResultSet rs = s.executeQuery("SELECT * FROM seans s where s.IdS = '" + ids + "';");
            
            if(!rs.first())
            {
                JOptionPane.showMessageDialog(null, "Нет такого сеанса");
            }
            else 
            {
                initComponents();
                java.awt.Dimension dim = getToolkit().getScreenSize();
                this.setLocation(dim.width/2 - this.getWidth()/2,
                dim.height/2 - this.getHeight()/2);
 
               
                    idpat = rs.getInt("IdP");
                    TextAreaDiagnosis.setText(rs.getString("Diagnosis"));
                    TextAreaAppointment.setText(rs.getString("Appointment"));                                    

                    SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        DateChooserSessionDate.setDate(format1.parse(rs.getString("Session_date")));
                    } catch (java.text.ParseException ex) {
                        Logger.getLogger(PatientRegistration.class.getName()).log(Level.SEVERE, null, ex);
                    }
                  ButtonChoosePatient.setVisible(false);
                ButtonSeansReport.setVisible(false);
                //  ButtonSave.setEnabled(false);
                ButtonSendEmail.setVisible(false);      
                
            }

        } catch (SQLException ex) {
            Logger.getLogger(PatientRegistration.class.getName()).log(Level.SEVERE, null, ex);
        }                     
     
       /* java.awt.Dimension dim = getToolkit().getScreenSize();
        this.setLocation(dim.width/2 - this.getWidth()/2,
                dim.height/2 - this.getHeight()/2);
                         try
           {
               DriverManager.registerDriver(new com.mysql.jdbc.Driver());
            //   cn = DriverManager.getConnection("jdbc:mysql://localhost:3306/patients2", "root", "");
           }
           catch (SQLException ex) {
               Logger.getLogger(PatientRegistration.class.getName()).log(Level.SEVERE, null, ex);
           }*/
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
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        TextAreaDiagnosis = new javax.swing.JTextArea();
        jLabel3 = new javax.swing.JLabel();
        ButtonSave = new javax.swing.JButton();
        ButtonChoosePatient = new javax.swing.JButton();
        ButtonKVVV = new javax.swing.JButton();
        ButtonCancel = new javax.swing.JButton();
        DateChooserSessionDate = new com.toedter.calendar.JDateChooser();
        ButtonSeansReport = new javax.swing.JButton();
        ButtonSendEmail = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        TextAreaAppointment = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("spectrex/Bundle"); // NOI18N
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("Seans.jPanel1.border.title"))); // NOI18N

        jLabel2.setText(bundle.getString("Seans.jLabel2.text")); // NOI18N

        TextAreaDiagnosis.setColumns(20);
        TextAreaDiagnosis.setRows(5);
        jScrollPane1.setViewportView(TextAreaDiagnosis);

        jLabel3.setText(bundle.getString("Seans.jLabel3.text")); // NOI18N

        ButtonSave.setText(bundle.getString("Seans.ButtonSave.text")); // NOI18N
        ButtonSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonSaveActionPerformed(evt);
            }
        });

        ButtonChoosePatient.setText(bundle.getString("Seans.ButtonChoosePatient.text")); // NOI18N
        ButtonChoosePatient.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonChoosePatientActionPerformed(evt);
            }
        });

        ButtonKVVV.setText(bundle.getString("Seans.ButtonKVVV.text")); // NOI18N
        ButtonKVVV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonKVVVActionPerformed(evt);
            }
        });

        ButtonCancel.setText(bundle.getString("Seans.ButtonCancel.text")); // NOI18N
        ButtonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonCancelActionPerformed(evt);
            }
        });

        ButtonSeansReport.setText(bundle.getString("Seans.ButtonSeansReport.text")); // NOI18N
        ButtonSeansReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonSeansReportActionPerformed(evt);
            }
        });

        ButtonSendEmail.setText(bundle.getString("Seans.ButtonSendEmail.text")); // NOI18N
        ButtonSendEmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonSendEmailActionPerformed(evt);
            }
        });

        jLabel1.setText(bundle.getString("Seans.jLabel1.text")); // NOI18N

        TextAreaAppointment.setColumns(20);
        TextAreaAppointment.setRows(5);
        jScrollPane2.setViewportView(TextAreaAppointment);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(24, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1)
                    .addComponent(ButtonSeansReport, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(ButtonSendEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(ButtonSave, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(ButtonCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addComponent(DateChooserSessionDate, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 498, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(ButtonKVVV, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(34, 34, 34)
                        .addComponent(ButtonChoosePatient, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane2))
                .addContainerGap(24, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(DateChooserSessionDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ButtonKVVV)
                    .addComponent(ButtonChoosePatient))
                .addGap(45, 45, 45)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ButtonCancel)
                    .addComponent(ButtonSave)
                    .addComponent(ButtonSendEmail))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(ButtonSeansReport)
                .addContainerGap(41, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void logQuery(String strChange)
    {                     
            try {                                               
            Statement s = cn.createStatement();
            java.util.Date now = new java.util.Date();
            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat format2 = new SimpleDateFormat("HH:mm:ss");
            String d1 = format1.format(now);
            String t1 = format2.format(now);
            System.out.println(d1 + " " + t1);
         
            String Sql = "";
  
            Sql = "insert into journal (DateOfWork, TimeOfWork, IdHS, Enter, Quit, InputDataInTable, UseQuery) values (?, ?, ?, ?, ?, ?, ?)";
          
            try {
                PreparedStatement ps = cn.prepareStatement(Sql);
                ps.setString(1, d1);
                ps.setString(2, t1);
                ps.setInt(3, idhs);
                ps.setString(4, "0");
                ps.setString(5, "0");
                ps.setString(6, "-");
                ps.setString(7, strChange);
                
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
                    
    }
    
    private void ButtonSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonSaveActionPerformed

         String diagnosis, sessiondatestr;
         
         String Sql = "";
         Date sessiondate;
         diagnosis = TextAreaDiagnosis.getText();
         SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
         sessiondatestr = format.format(DateChooserSessionDate.getDate());
         
        
        try {
            sessiondate = format.parse(sessiondatestr);
        } catch (Exception ex) {
            System.out.println("Это не должно произойти " + ex);
        }
 
        Sql = "insert into seans (Session_date, Diagnosis, IdP, IdHS, IdKV, IdDG) values "
                + "(?, ?, ?, ?, ?, ?)";
        try {
            idp = ChoosePatient.idp;
            idkv = KVVVreg.idkvvv;
            iddg = ResultClassifier.dgroup;
        //    JOptionPane.showMessageDialog(null, "id: " + idp + " "+ idkv);
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());
          //  cn = DriverManager.getConnection("jdbc:mysql://localhost:3306/patients2", "root", "");
            PreparedStatement ps = cn.prepareStatement(Sql);
            ps.setString(1, sessiondatestr);
            ps.setString(2, diagnosis);
            ps.setInt(3, idp);
            ps.setInt(4, idhs);
            ps.setInt(5, idkv);
            ps.setInt(6, iddg);

            
            int n = ps.executeUpdate();

            if(n > 0)
            {
                JOptionPane.showMessageDialog(null, "OK");
            }
            
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
             ///////////////////////////
         try
         {
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());
           // cn = DriverManager.getConnection("jdbc:mysql://localhost:3306/patients2", "root", "");
            Statement sta = cn.createStatement();
            ResultSet rs;

            rs = sta.executeQuery("select IdS from seans where "
                    + "Session_date = '" + sessiondatestr + "' and "
                    + "Diagnosis = '" + diagnosis + "' and "
                    + "IdP = " + idp + " and "
                    + "IdHS = " + idhs + " and "
                    + "IdKV = " + idkv + " and "
                    + "IdDG = " + iddg + ";");
            //new PatientFound(rs).setVisible(true);
            if(!rs.first())
                JOptionPane.showMessageDialog(null, "Неверный ids");
            else {
                ids = rs.getInt("IdS");
            //     JOptionPane.showMessageDialog(null, "ids: " + ids);
                 }
          } 
         catch (SQLException ex) {
            Logger.getLogger(MedLogin.class.getName()).log(Level.SEVERE, null, ex);
        }
        //////////////////////////
                  
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
                ps.setString(6, "seans");
                ps.setString(7, "insert into seans values(...)");
                
                int n = ps.executeUpdate();
                
                if(n <= 0)
                {
                    JOptionPane.showMessageDialog(null, "message_journal_BAD_QueryEntry");
                }
            }
                         catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "1" + ex);
            }
                    
            }      
                           catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "2" + ex);
                           }
                   
///////////////////////////   
    }//GEN-LAST:event_ButtonSaveActionPerformed

    private void ButtonKVVVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonKVVVActionPerformed
        new KVVVreg().setVisible(true);
    }//GEN-LAST:event_ButtonKVVVActionPerformed

    private void ButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonCancelActionPerformed
        this.dispose();
     /*   try {
            new MedMain().setVisible(true);
        } catch (SQLException ex) {
            Logger.getLogger(Seans.class.getName()).log(Level.SEVERE, null, ex);
        }*/
    }//GEN-LAST:event_ButtonCancelActionPerformed

    private void ButtonChoosePatientActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonChoosePatientActionPerformed
        new ChoosePatient().setVisible(true);
    }//GEN-LAST:event_ButtonChoosePatientActionPerformed

    private void ButtonSeansReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonSeansReportActionPerformed
                 try {

    String jasperReport;
    jasperReport = "spectrex/report_seans2_1.jasper";
    InputStream is = this.getClass().getClassLoader().getResourceAsStream(jasperReport);
    Map<String, Object> params = new HashMap<String, Object>();
    params.put(param_seans, ids);
    params.put(param_patient, idp);
    params.put(param_doctor, idhs);   
    JasperPrint jasperPrint = JasperFillManager.fillReport(is, params, cn);

    JasperViewer jv = new JasperViewer(jasperPrint);
    JDialog viewer = new JDialog(this, "Report", true);
    viewer.setBounds(jv.getBounds());
    viewer.getContentPane().add(jv.getContentPane());
    viewer.setResizable(true);
    viewer.setIconImage(jv.getIconImage());
    viewer.setVisible(true);
    logQuery("create report for seans");
} catch (JRException exc) {
   System.out.println(exc.getMessage());
} 
           catch (NullPointerException exc) {
   System.out.println(exc.getMessage());
} 
    }//GEN-LAST:event_ButtonSeansReportActionPerformed

    private void ButtonSendEmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonSendEmailActionPerformed
        new EmailSender().setVisible(true);
    }//GEN-LAST:event_ButtonSendEmailActionPerformed

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
            java.util.logging.Logger.getLogger(Seans.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Seans.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Seans.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Seans.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Seans().setVisible(true);
            }
        });
    }

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton ButtonCancel;
    private javax.swing.JButton ButtonChoosePatient;
    private javax.swing.JButton ButtonKVVV;
    private javax.swing.JButton ButtonSave;
    private javax.swing.JButton ButtonSeansReport;
    private javax.swing.JButton ButtonSendEmail;
    private com.toedter.calendar.JDateChooser DateChooserSessionDate;
    private javax.swing.JTextArea TextAreaAppointment;
    private javax.swing.JTextArea TextAreaDiagnosis;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    // End of variables declaration//GEN-END:variables
}
