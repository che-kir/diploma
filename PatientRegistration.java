/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spectrex;
//package database;
/**
 *
 * @author Kirill
 */
//import database.Database;
import java.sql.*;
import java.util.Date;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import com.toedter.calendar.*;
import java.beans.PropertyChangeEvent;
import java.util.EventListener.*;
import java.beans.PropertyChangeListener;
import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.event.*;
public class PatientRegistration extends javax.swing.JFrame {

    /**
     * Creates new form PatientRegistration
     */
    public int idhs = MedLogin.idhs;
    public Connection cn = MedLogin.cn;
    public PatientRegistration() {
        //registration
        try {
            initComponents();
            ButtonUpdate.setEnabled(false);
           // ButtonUpdateAccommodation.setEnabled(false);
            java.awt.Dimension dim = getToolkit().getScreenSize();
            this.setLocation(dim.width/2 - this.getWidth()/2,
                dim.height/2 - this.getHeight()/2);
            GenderList.addItem("Мужской");
            GenderList.addItem("Женский");
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());
          //  cn = DriverManager.getConnection("jdbc:mysql://localhost:3306/patients2", "root", "");

            Statement s = cn.createStatement();
            ResultSet rs = s.executeQuery("SELECT Inst_name FROM institution");
            
            while (rs.next()) {
                InstList.addItem(rs.getString("Inst_name"));
            }
            ResultSet rs1 = s.executeQuery("SELECT GroupName FROM diagnosticgroup");
            
            while (rs1.next()) {
                DGList.addItem(rs1.getString("GroupName"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(PatientRegistration.class.getName()).log(Level.SEVERE, null, ex);
        }
                
                
    }
     public PatientRegistration(String medcard) {
        try {
            initComponents();
            ButtonAddAccommodation.setEnabled(false);
            ButtonRegister.setEnabled(false);
            java.awt.Dimension dim = getToolkit().getScreenSize();
            this.setLocation(dim.width/2 - this.getWidth()/2,
                dim.height/2 - this.getHeight()/2);
            GenderList.addItem("Мужской");
            GenderList.addItem("Женский");
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());
          //  cn = DriverManager.getConnection("jdbc:mysql://localhost:3306/patients2", "root", "");

            Statement s = cn.createStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM patient where N_med_card = '" + medcard + "';");
            
            if(!rs.first())
            {
                JOptionPane.showMessageDialog(null, "Wrong medcard number");
            }
            else 
            {
                if (medcard.equals(rs.getString("N_med_card")))
                {
                    N_med_cardText.setText(medcard);
                    String sur, nam, mid, fio = rs.getString("FIO");
                    int i = 0;
                    while(fio.charAt(i) != ' ')
                        i++;
                    sur = fio.substring(0, i);
                    int j = ++i;
                    while(fio.charAt(i) != ' ')
                        i++;
                    nam = fio.substring(j, i);
                    mid = fio.substring(i+1);
                    SurnameText.setText(sur);
                    NameText.setText(nam);
                    MiddleNameText.setText(mid);
                 //   EmailText.setText(rs.getString("E_mail"));
                    if(rs.getString("E_mail").isEmpty())
                    {
                        //JOptionPane.showMessageDialog(null, "здесь");
                        EmailText.setText("");
                    }
                     else                   EmailText.setText(rs.getString("E_mail"));

                    SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        DateChooserBDay.setDate(format1.parse(rs.getString("B_day")));
                    } catch (ParseException ex) {
                        Logger.getLogger(PatientRegistration.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    try {
                        DateChooserAccDay.setDate(format1.parse(rs.getString("Acc_date")));
                    } catch (ParseException ex) {
                        Logger.getLogger(PatientRegistration.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    TelephoneText.setText(rs.getString("Tel"));
                //    PassportText.setText(rs.getString("Passport"));
                    if(rs.getString("Passport").isEmpty())
                    {
                        JOptionPane.showMessageDialog(null, "здесь");
                        PassportText.setText("");
                    }
                       else                 PassportText.setText(rs.getString("Passport"));

                  //  DiseaseText.setText(rs.getString("Disease"));
                    if(rs.getString("Disease").isEmpty())
                    {
                        DiseaseText.setText("");
                    }
                     else                   DiseaseText.setText(rs.getString("Disease"));

                  //  AllergyText.setText(rs.getString("Allergy"));
                    if(rs.getString("Allergy").isEmpty())
                    {
                        AllergyText.setText("");
                    }
                          else              AllergyText.setText(rs.getString("Allergy"));

                  //  JOptionPane.showMessageDialog(null, "здесь");
                    ResultSet rs2 = s.executeQuery("SELECT Inst_name FROM institution");
                    while (rs2.next()) {
                         InstList.addItem(rs2.getString("Inst_name"));
                    }
                    ResultSet rs1 = s.executeQuery("SELECT GroupName FROM diagnosticgroup");
            
                    while (rs1.next()) {
                         DGList.addItem(rs1.getString("GroupName"));
                    }
                   ResultSet rs3 = s.executeQuery("SELECT * FROM patient where N_med_card = '" + medcard + "';");
                   int a = 0, b = 0;
                   String c = "";
                   if(rs3.first())
                   {
                   a = rs3.getInt("IdInst");
                    b = rs3.getInt("IdDG");
                    c = rs3.getString("Gender");
                   }
                   else JOptionPane.showMessageDialog(null, "Error with connection ");
                   InstList.setSelectedIndex(a-1);
                    DGList.setSelectedIndex(b-1);
                   if(c.equals("М"))
                        GenderList.setSelectedItem("Мужской");
                    else GenderList.setSelectedItem("Женский");        
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(PatientRegistration.class.getName()).log(Level.SEVERE, null, ex);
        }                     
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jPanel1 = new javax.swing.JPanel();
        SurnameText = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        N_med_cardText = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        NameText = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        MiddleNameText = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        TelephoneText = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        PassportText = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        DiseaseText = new javax.swing.JTextArea();
        jScrollPane3 = new javax.swing.JScrollPane();
        AllergyText = new javax.swing.JTextArea();
        EmailText = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        InstList = new javax.swing.JComboBox();
        DGList = new javax.swing.JComboBox();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        GenderList = new javax.swing.JComboBox();
        jLabel12 = new javax.swing.JLabel();
        DateChooserBDay = new com.toedter.calendar.JDateChooser();
        DateChooserAccDay = new com.toedter.calendar.JDateChooser();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        ButtonRegister = new javax.swing.JButton();
        ButtonBackToMain = new javax.swing.JButton();
        ButtonAddAccommodation = new javax.swing.JButton();
        ButtonUpdate = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("spectrex/Bundle"); // NOI18N
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("PatientRegistration.jPanel1.border.title"))); // NOI18N

        SurnameText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SurnameTextActionPerformed(evt);
            }
        });

        jLabel1.setText(bundle.getString("PatientRegistration.jLabel1.text")); // NOI18N

        jLabel2.setText(bundle.getString("PatientRegistration.jLabel2.text")); // NOI18N

        N_med_cardText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                N_med_cardTextActionPerformed(evt);
            }
        });

        jLabel3.setText(bundle.getString("PatientRegistration.jLabel3.text")); // NOI18N

        NameText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NameTextActionPerformed(evt);
            }
        });

        jLabel4.setText(bundle.getString("PatientRegistration.jLabel4.text")); // NOI18N

        MiddleNameText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MiddleNameTextActionPerformed(evt);
            }
        });

        jLabel5.setText(bundle.getString("PatientRegistration.jLabel5.text")); // NOI18N

        jLabel6.setText(bundle.getString("PatientRegistration.jLabel6.text")); // NOI18N

        TelephoneText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TelephoneTextActionPerformed(evt);
            }
        });

        jLabel7.setText(bundle.getString("PatientRegistration.jLabel7.text")); // NOI18N

        PassportText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PassportTextActionPerformed(evt);
            }
        });

        jLabel8.setText(bundle.getString("PatientRegistration.jLabel8.text")); // NOI18N

        DiseaseText.setColumns(20);
        DiseaseText.setRows(5);
        jScrollPane2.setViewportView(DiseaseText);

        AllergyText.setColumns(20);
        AllergyText.setRows(5);
        jScrollPane3.setViewportView(AllergyText);

        EmailText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EmailTextActionPerformed(evt);
            }
        });

        jLabel9.setText(bundle.getString("PatientRegistration.jLabel9.text")); // NOI18N

        jLabel10.setText(bundle.getString("PatientRegistration.jLabel10.text")); // NOI18N

        jLabel11.setText(bundle.getString("PatientRegistration.jLabel11.text")); // NOI18N

        GenderList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GenderListActionPerformed(evt);
            }
        });

        jLabel12.setText(bundle.getString("PatientRegistration.jLabel12.text")); // NOI18N

        jLabel13.setText(bundle.getString("PatientRegistration.jLabel13.text")); // NOI18N

        jLabel14.setText(bundle.getString("PatientRegistration.jLabel14.text")); // NOI18N

        jLabel15.setText(bundle.getString("PatientRegistration.jLabel15.text")); // NOI18N

        jLabel16.setText(bundle.getString("PatientRegistration.jLabel16.text")); // NOI18N

        jLabel17.setText(bundle.getString("PatientRegistration.jLabel17.text")); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jLabel9)
                    .addComponent(jLabel12)
                    .addComponent(jLabel13)
                    .addComponent(jLabel14)
                    .addComponent(jLabel15))
                .addGap(36, 36, 36)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(SurnameText, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(N_med_cardText, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(NameText, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(MiddleNameText, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(EmailText, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(GenderList, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(44, 44, 44)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel16)
                            .addComponent(jLabel17))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(TelephoneText, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(38, 38, 38)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(DateChooserBDay, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(PassportText, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(DateChooserAccDay, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(InstList, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(DGList, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(30, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(N_med_cardText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel5))
                    .addComponent(DateChooserBDay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2)
                        .addComponent(jLabel6))
                    .addComponent(SurnameText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TelephoneText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3)
                        .addComponent(jLabel7))
                    .addComponent(NameText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(PassportText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel4)
                        .addComponent(jLabel8))
                    .addComponent(DateChooserAccDay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(MiddleNameText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel9)
                        .addComponent(jLabel10))
                    .addComponent(InstList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(EmailText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(DGList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel11)
                        .addComponent(GenderList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(37, 37, 37)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE)
                    .addComponent(jScrollPane2)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel15))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel17))))
        );

        ButtonRegister.setText(bundle.getString("PatientRegistration.ButtonRegister.text")); // NOI18N
        ButtonRegister.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonRegisterActionPerformed(evt);
            }
        });

        ButtonBackToMain.setText(bundle.getString("PatientRegistration.ButtonBackToMain.text")); // NOI18N
        ButtonBackToMain.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonBackToMainActionPerformed(evt);
            }
        });

        ButtonAddAccommodation.setText(bundle.getString("PatientRegistration.ButtonAddAccommodation.text")); // NOI18N
        ButtonAddAccommodation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonAddAccommodationActionPerformed(evt);
            }
        });

        ButtonUpdate.setText(bundle.getString("PatientRegistration.ButtonUpdate.text")); // NOI18N
        ButtonUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonUpdateActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(ButtonAddAccommodation, javax.swing.GroupLayout.PREFERRED_SIZE, 145, Short.MAX_VALUE)
                    .addComponent(ButtonBackToMain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(243, 243, 243)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(ButtonRegister, javax.swing.GroupLayout.DEFAULT_SIZE, 148, Short.MAX_VALUE)
                    .addComponent(ButtonUpdate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ButtonAddAccommodation)
                    .addComponent(ButtonRegister))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ButtonUpdate)
                    .addComponent(ButtonBackToMain))
                .addContainerGap(25, Short.MAX_VALUE))
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
                ps.setString(6, "patient");
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
        public static boolean checkWithRegExpTel(String Tel){  
        Pattern p = Pattern.compile("^((8|\\+38)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}$");  
        Matcher m = p.matcher(Tel);  
        return m.matches();  
    }  
        
        public static boolean checkWithRegExpEmail(String email){  
        Pattern p = Pattern.compile("^([a-z0-9_-]+\\.)*[a-z0-9_-]+@[a-z0-9_-]+(\\.[a-z0-9_-]+)*\\.[a-z]{2,6}$");  
        Matcher m = p.matcher(email);  
        return m.matches();  
    }      
    
    
        public static boolean checkWithRegExpPass(String pass){  
        Pattern p = Pattern.compile("^[Є-ЇА-Я][Є-ЇА-Я]\\s\\d{6}$");  
        Matcher m = p.matcher(pass);  
        return m.matches();  
    }
        
        public static boolean checkWithRegExpFIO(String pass){  
        Pattern p = Pattern.compile("^/([а-яА-Я\\`ґєҐЄ´ІіЇї\\s]+)/u$");  
        Matcher m = p.matcher(pass);  
        return m.matches();  
    } 
    
    private void ButtonRegisterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonRegisterActionPerformed
        try {                                               
            Statement s = cn.createStatement();
            String medcard, fio, tel, passport, disease, allergy, email, bdaystr, accdatestr;
            String Sql;
            Date bday, accdate;
            String sex;
            int dgroup = 0, inst = 0, flag = 0;
            medcard = N_med_cardText.getText();
            ResultSet r = s.executeQuery("select N_med_card from patient where N_med_card = " + medcard);
            if(medcard.equals(""))
            {
               // JOptionPane.showMessageDialog(null, "Input medcard number");
                JOptionPane.showMessageDialog(null, "Введите номер медкарты");
                flag = 1;
            }
            else if(r.first())
            {
                //JOptionPane.showMessageDialog(null, "Input another medcard number");
                JOptionPane.showMessageDialog(null, "Такой номер медкарты уже существует");
                flag = 1;
            }
            
            fio = SurnameText.getText() + " " + NameText.getText() + " " + MiddleNameText.getText();
            if(SurnameText.getText().equals(""))
            {
               // JOptionPane.showMessageDialog(null, "Input surname");
                JOptionPane.showMessageDialog(null, "Введите фамилию");
                flag = 1;
            } 
            else if(NameText.getText().equals(""))
            {
                //JOptionPane.showMessageDialog(null, "Input name");
                JOptionPane.showMessageDialog(null, "Введите имя");
                flag = 1;
            }
            else if(MiddleNameText.getText().equals(""))
            {
              //  JOptionPane.showMessageDialog(null, "Input middle name");
                JOptionPane.showMessageDialog(null, "Введите отчество");
                flag = 1;
            }
            
            tel = TelephoneText.getText();
            passport = PassportText.getText();
            email = EmailText.getText();
            
            if(!checkWithRegExpTel(tel))
            {
                //JOptionPane.showMessageDialog(null, "Wrong telephone format");
                JOptionPane.showMessageDialog(null, "Номер телефона в формате +38(000)1234567");
                flag = 1;
            }
            else if(!checkWithRegExpEmail(email) && !email.equals(""))
            {
               // JOptionPane.showMessageDialog(null, "Wrong e-mail");
                JOptionPane.showMessageDialog(null, "Неверный формат e-mail");
                flag = 1;
            }
            else if(!checkWithRegExpPass(passport) && !passport.equals(""))
            {
              // JOptionPane.showMessageDialog(null, "Wrong passport");
                JOptionPane.showMessageDialog(null, "Формат паспорта: АА 123456");
                flag = 1;
            }
           /* else 
            {    
                JOptionPane.showMessageDialog(null, "OK");
            }*/
            
            disease = DiseaseText.getText();
            allergy = AllergyText.getText();
            

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            
            try
            {
            bdaystr = format.format(DateChooserBDay.getDate());
            accdatestr = format.format(DateChooserAccDay.getDate());

            
            try {
                bday = format.parse(bdaystr);
            } catch (java.text.ParseException ex) {
                System.out.println(ex);
            }
            try {
                accdate = format.parse(accdatestr);
            } catch (java.text.ParseException ex) {
                System.out.println(ex);
            }
            

            
            if(GenderList.getSelectedItem().toString().equals("Мужской"))
            {
                sex = "М";
            }
            else sex = "Ж";
       
            ResultSet rs2 = s.executeQuery("select IdInst from institution where Inst_name = '" + InstList.getSelectedItem().toString() + "'");
            while(rs2.next())
            {
            System.out.println(rs2.getString("IdInst"));
            inst =  Integer.parseInt(rs2.getString("IdInst"));
            }
            
            ResultSet rs3 = s.executeQuery("select IdDG from diagnosticgroup where GroupName = '" + DGList.getSelectedItem().toString() + "'");
            while(rs3.next())
            { 
              dgroup =  Integer.parseInt(rs3.getString("IdDG"));
            }
            Sql = "insert into patient (N_med_card, FIO, Gender, B_day, Tel, Passport, "
                    + "Acc_date, Disease, Allergy, E_mail, IdInst, IdDG) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try {
                if(flag == 0)
                {
                PreparedStatement ps = cn.prepareStatement(Sql);
                ps.setString(1, medcard);
                ps.setString(2, fio);
                ps.setString(3, sex);
                ps.setString(4, bdaystr);
                ps.setString(5, tel);
                ps.setString(6, passport);
                ps.setString(7, accdatestr);
                ps.setString(8, disease);
                ps.setString(9, allergy);
                ps.setString(10, email);
                ps.setInt(11, inst);
                ps.setInt(12, dgroup);
                
                int n = ps.executeUpdate();
                
                if(n > 0)
                {
                    JOptionPane.showMessageDialog(null, "OK");
                }
                }
                
            }
             catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex);
            }
          }
            catch(NullPointerException e)
            {
                //JOptionPane.showMessageDialog(null, "Input date");
                JOptionPane.showMessageDialog(null, "Введите даты");
                flag = 1;
            }
        } catch (SQLException ex) {
            Logger.getLogger(PatientRegistration.class.getName()).log(Level.SEVERE,null, ex);
        }
           ///////////////////////////
                  
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
                ps.setString(6, "patient");
                ps.setString(7, "insert into patient values(...)");
                
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
             logQuery("add new patient");       
///////////////////////////   
    }//GEN-LAST:event_ButtonRegisterActionPerformed

    private void GenderListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_GenderListActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_GenderListActionPerformed

    private void EmailTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EmailTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_EmailTextActionPerformed

    private void PassportTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PassportTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_PassportTextActionPerformed

    private void TelephoneTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TelephoneTextActionPerformed
       
    }//GEN-LAST:event_TelephoneTextActionPerformed

    private void MiddleNameTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MiddleNameTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_MiddleNameTextActionPerformed

    private void NameTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NameTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_NameTextActionPerformed

    private void N_med_cardTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_N_med_cardTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_N_med_cardTextActionPerformed

    private void SurnameTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SurnameTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_SurnameTextActionPerformed

    private void ButtonBackToMainActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonBackToMainActionPerformed
        this.dispose();
   /*     try {
            new MedMain().setVisible(true);
        } catch (SQLException ex) {
            Logger.getLogger(PatientRegistration.class.getName()).log(Level.SEVERE, null, ex);
        }*/
    }//GEN-LAST:event_ButtonBackToMainActionPerformed

    private void ButtonAddAccommodationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonAddAccommodationActionPerformed
        new Accommodation().setVisible(true);
    }//GEN-LAST:event_ButtonAddAccommodationActionPerformed

    private void ButtonUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonUpdateActionPerformed
        // doooooooo
        //исправить отображение в combobox
           try {
               String sex;
               if(GenderList.getSelectedItem().toString().equals("Мужской"))
            {
                sex = "М";
            }
            else sex = "Ж";
               
                try
            {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String bdaystr = format.format(DateChooserBDay.getDate());
            String accdatestr = format.format(DateChooserAccDay.getDate());
            int inst = 0, dgroup = 0;
            Statement s = cn.createStatement();
            ResultSet rs2 = s.executeQuery("select IdInst from institution where Inst_name = '" + InstList.getSelectedItem().toString() + "'");
            while(rs2.next())
            {
            System.out.println(rs2.getString("IdInst"));
            inst =  Integer.parseInt(rs2.getString("IdInst"));
            }
            
            ResultSet rs3 = s.executeQuery("select IdDG from diagnosticgroup where GroupName = '" + DGList.getSelectedItem().toString() + "'");
            while(rs3.next())
            { 
             dgroup =  Integer.parseInt(rs3.getString("IdDG"));
            }

                ///////
            PreparedStatement sta = cn.prepareStatement("update patient a set a.Passport = '" + PassportText.getText()
                    + "', a.Allergy = '" + AllergyText.getText() + "', a.FIO = '" + 
                    SurnameText.getText() + " " + 
                    NameText.getText() + " " + 
                    MiddleNameText.getText() + "', a.Gender = '" + sex + "', a.B_day = '" + bdaystr + "',\n" +
                    "a.Tel = '"+ TelephoneText.getText() +"', a.Acc_date = '" + accdatestr + "', a.Disease = '"+ 
                    DiseaseText.getText() +"', a.E_mail = '"+ EmailText.getText() + "',"
                    + " a.IdInst = " + inst + ", a.IdDG = "+ dgroup +"\n" +
             "where a.N_med_card = " + N_med_cardText.getText() + ";");
            int n = sta.executeUpdate();
             if(n <= 0)
                {
                    JOptionPane.showMessageDialog(null, "Error with update");
                }        
             else JOptionPane.showMessageDialog(null, "Record successfully updated");
             }
             catch(NullPointerException e)
            {
                JOptionPane.showMessageDialog(null, "Input date");
                
            }
        }
        catch(SQLException ex)
        {
            Logger.getLogger(PatientRegistration.class.getName()).log(Level.SEVERE, null, ex);
        }
           logQuery("update info about patient");
    }//GEN-LAST:event_ButtonUpdateActionPerformed

    /**
     * @param args the command line arguments
     */
   /* public static void main(String args[]) {
      
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PatientRegistration().setVisible(true);
            }
        });
    }
*/
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea AllergyText;
    private javax.swing.JButton ButtonAddAccommodation;
    private javax.swing.JButton ButtonBackToMain;
    private javax.swing.JButton ButtonRegister;
    private javax.swing.JButton ButtonUpdate;
    private javax.swing.JComboBox DGList;
    private com.toedter.calendar.JDateChooser DateChooserAccDay;
    private com.toedter.calendar.JDateChooser DateChooserBDay;
    private javax.swing.JTextArea DiseaseText;
    private javax.swing.JTextField EmailText;
    private javax.swing.JComboBox GenderList;
    private javax.swing.JComboBox InstList;
    private javax.swing.JTextField MiddleNameText;
    private javax.swing.JTextField N_med_cardText;
    private javax.swing.JTextField NameText;
    private javax.swing.JTextField PassportText;
    private javax.swing.JTextField SurnameText;
    private javax.swing.JTextField TelephoneText;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    // End of variables declaration//GEN-END:variables
}
