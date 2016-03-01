/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spectrex;

import static spectrex.PatientRegistration.checkWithRegExpTel;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;

/**
 *
 * @author Kirill
 */
public class MedRegistration extends javax.swing.JFrame {

    /**
     * Creates new form MedRegistration
     */
    public Connection cn = MedLogin.cn;
    public int idhs = MedLogin.idhs;
    public int currentUpdateIdHS;
    public MedRegistration() {
        try {
            initComponents();
            ButtonUpdate.setEnabled(false);
            java.awt.Dimension dim = getToolkit().getScreenSize();
            this.setLocation(dim.width/2 - this.getWidth()/2,
                dim.height/2 - this.getHeight()/2);

            Statement s = cn.createStatement();
            ResultSet rs = s.executeQuery("SELECT PostName FROM post");
            
            while (rs.next()) {
                ComboBoxPost.addItem(rs.getString("PostName"));
            }
            
            ResultSet rs1 = s.executeQuery("SELECT CategoryName FROM category");
            
            while (rs1.next()) {
                ComboBoxCategory.addItem(rs1.getString("CategoryName"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(MedRegistration.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    MedRegistration(String idhs) {
       try {            
            
            Statement s = cn.createStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM hospitalstaff where IdHS = " + idhs + ";");
            
            if(!rs.first())
            {
                JOptionPane.showMessageDialog(null, "Wrong idhs");
            }
            else 
            {
                currentUpdateIdHS = Integer.parseInt(idhs);
                initComponents();
                PasswordField.setEditable(false);
                TextFieldLogin.setEditable(false);
                java.awt.Dimension dim = getToolkit().getScreenSize();
                this.setLocation(dim.width/2 - this.getWidth()/2,
                dim.height/2 - this.getHeight()/2);
                
                    String sur, nam, mid, fio = rs.getString("HSFIO");
                    int i = 0;
                    while(fio.charAt(i) != ' ')
                        i++;
                    sur = fio.substring(0, i);
                    int j = ++i;
                    while(fio.charAt(i) != ' ')
                        i++;
                    nam = fio.substring(j, i);
                    mid = fio.substring(i+1);
                    FieldSurname.setText(sur);
                    FieldName.setText(nam);
                    FieldMiddleName.setText(mid);

                    SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        DateChooserBDay.setDate(format1.parse(rs.getString("B_day")));
                    } catch (ParseException ex) {
                        Logger.getLogger(PatientRegistration.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    try {
                        DateChooserJoinDay.setDate(format1.parse(rs.getString("Date_to_join")));
                    } catch (ParseException ex) {
                        Logger.getLogger(PatientRegistration.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    FieldTelephone.setText(rs.getString("Tel"));
                    TextFieldLogin.setText(rs.getString("Login"));
                    PasswordField.setText("Pass");
                    if(rs.getString("Describes").isEmpty())
                    {
                        TextAreaDescribes.setText("");
                    }
                       else 
                        TextAreaDescribes.setText(rs.getString("Describes"));


                    ResultSet rs2 = s.executeQuery("SELECT PostName FROM post");
                    while (rs2.next()) {
                         ComboBoxPost.addItem(rs2.getString("PostName"));
                    }
                    
                    ResultSet rs4 = s.executeQuery("SELECT CategoryName FROM category");
                    while (rs4.next()) {
                        ComboBoxCategory.addItem(rs4.getString("CategoryName"));
                    }
                   ResultSet rs3 = s.executeQuery("SELECT * FROM hospitalstaff where IdHS = " + idhs + ";");
                   int a = 0, b = 0;
                   if(rs3.first())
                   {
                       a = rs3.getInt("IdPost");
                       b = rs3.getInt("IdCat");
                   }
                   else JOptionPane.showMessageDialog(null, "Error with connection ");
                   ComboBoxPost.setSelectedIndex(a-1);
                   ComboBoxCategory.setSelectedIndex(b-1);
                   
                    ButtonAddPost.setEnabled(false);
                    ButtonAddCategory.setEnabled(false);
                    TextFieldLogin.setEditable(false);
                    FieldSurname.setEditable(false);
                    FieldName.setEditable(false);
                    FieldMiddleName.setEditable(false);
                    FieldTelephone.setEditable(false);
                    PasswordField.setEditable(false);
                    TextAreaDescribes.setEditable(false);
                    DateChooserJoinDay.setEnabled(false);
                    DateChooserBDay.setEnabled(false);
                    ComboBoxPost.setEnabled(false);
                    ComboBoxCategory.setEnabled(false);    
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

        jPanel1 = new javax.swing.JPanel();
        ButtonSave = new javax.swing.JButton();
        ButtonSignIn = new javax.swing.JButton();
        ButtonUpdate = new javax.swing.JButton();
        ComboBoxCategory = new javax.swing.JComboBox();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        FieldTelephone = new javax.swing.JTextField();
        DateChooserBDay = new com.toedter.calendar.JDateChooser();
        DateChooserJoinDay = new com.toedter.calendar.JDateChooser();
        ComboBoxPost = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        TextFieldLogin = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        PasswordField = new javax.swing.JPasswordField();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        FieldSurname = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        FieldName = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        FieldMiddleName = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        TextAreaDescribes = new javax.swing.JTextArea();
        jLabel11 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        ButtonAddPost = new javax.swing.JButton();
        ButtonAddCategory = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("spectrex/Bundle"); // NOI18N
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("MedRegistration.jPanel1.border.title"))); // NOI18N

        ButtonSave.setText(bundle.getString("MedRegistration.ButtonSave.text")); // NOI18N
        ButtonSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonSaveActionPerformed(evt);
            }
        });

        ButtonSignIn.setText(bundle.getString("MedRegistration.ButtonSignIn.text")); // NOI18N
        ButtonSignIn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonSignInActionPerformed(evt);
            }
        });

        ButtonUpdate.setForeground(new java.awt.Color(51, 51, 255));
        ButtonUpdate.setText(bundle.getString("MedRegistration.ButtonUpdate.text")); // NOI18N
        ButtonUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonUpdateActionPerformed(evt);
            }
        });

        ComboBoxCategory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ComboBoxCategoryActionPerformed(evt);
            }
        });

        jLabel6.setText(bundle.getString("MedRegistration.jLabel6.text")); // NOI18N

        jLabel7.setText(bundle.getString("MedRegistration.jLabel7.text")); // NOI18N

        jLabel4.setText(bundle.getString("MedRegistration.jLabel4.text")); // NOI18N

        jLabel8.setText(bundle.getString("MedRegistration.jLabel8.text")); // NOI18N

        jLabel5.setText(bundle.getString("MedRegistration.jLabel5.text")); // NOI18N

        jLabel9.setText(bundle.getString("MedRegistration.jLabel9.text")); // NOI18N

        jLabel10.setText(bundle.getString("MedRegistration.jLabel10.text")); // NOI18N

        PasswordField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PasswordFieldActionPerformed(evt);
            }
        });

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("MedRegistration.jPanel2.border.title"))); // NOI18N

        jLabel1.setText(bundle.getString("MedRegistration.jLabel1.text")); // NOI18N

        jLabel2.setText(bundle.getString("MedRegistration.jLabel2.text")); // NOI18N

        FieldName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FieldNameActionPerformed(evt);
            }
        });

        jLabel3.setText(bundle.getString("MedRegistration.jLabel3.text")); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 54, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(FieldName, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 177, Short.MAX_VALUE)
                    .addComponent(FieldSurname, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(FieldMiddleName))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(FieldSurname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(FieldName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(FieldMiddleName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        TextAreaDescribes.setColumns(20);
        TextAreaDescribes.setRows(5);
        jScrollPane1.setViewportView(TextAreaDescribes);

        jLabel11.setText(bundle.getString("MedRegistration.jLabel11.text")); // NOI18N

        jLabel27.setText(bundle.getString("MedRegistration.jLabel27.text")); // NOI18N

        ButtonAddPost.setText(bundle.getString("MedRegistration.ButtonAddPost.text")); // NOI18N
        ButtonAddPost.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonAddPostActionPerformed(evt);
            }
        });

        ButtonAddCategory.setText(bundle.getString("MedRegistration.ButtonAddCategory.text")); // NOI18N
        ButtonAddCategory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonAddCategoryActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(29, 29, 29)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 507, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(DateChooserJoinDay, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(ButtonUpdate)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(ButtonSave, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(ButtonSignIn, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(ComboBoxCategory, 0, 135, Short.MAX_VALUE)
                                            .addComponent(ComboBoxPost, 0, 135, Short.MAX_VALUE)
                                            .addComponent(PasswordField)
                                            .addComponent(TextFieldLogin))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 24, Short.MAX_VALUE)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(ButtonAddCategory)
                                            .addComponent(ButtonAddPost))
                                        .addGap(70, 70, 70)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel1Layout.createSequentialGroup()
                                                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                    .addComponent(FieldTelephone, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                    .addComponent(DateChooserBDay, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addGap(59, 59, 59)))
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(11, 11, 11)))))
                                .addGap(26, 26, 26))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel27)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(TextFieldLogin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(PasswordField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(39, 39, 39)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(ButtonAddPost)
                            .addComponent(jLabel8)
                            .addComponent(ComboBoxPost, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 31, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(DateChooserBDay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel5)
                        .addComponent(ButtonAddCategory)
                        .addComponent(ComboBoxCategory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel4)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 44, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel6)
                        .addComponent(DateChooserJoinDay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(FieldTelephone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel7)))
                .addGap(29, 29, 29)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel27)
                .addGap(12, 12, 12)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ButtonSave)
                    .addComponent(ButtonSignIn)
                    .addComponent(ButtonUpdate))
                .addGap(52, 52, 52))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    
    private void ButtonSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonSaveActionPerformed
         try
            {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String bdaystr = format.format(DateChooserBDay.getDate());
            String joindatestr = format.format(DateChooserJoinDay.getDate());
            int idpost = 0, idcat = 0;
            Statement s = cn.createStatement();
            ResultSet rs2 = s.executeQuery("select IdPost from post where PostName = '" + ComboBoxPost.getSelectedItem().toString() + "'");
            while(rs2.next())
            {
            idpost = rs2.getInt("IdPost");
            }
            
            ResultSet rs3 = s.executeQuery("select IdCat from category where CategoryName = '" + ComboBoxCategory.getSelectedItem().toString() + "'");
            while(rs3.next())
            { 
             idcat = rs3.getInt("IdCat");
            }

            PreparedStatement sta = cn.prepareStatement("update hospitalstaff a set a.Describes = '" + TextAreaDescribes.getText()
                    + "', a.HSFIO = '" + 
                    FieldSurname.getText() + " " + 
                    FieldName.getText() + " " + 
                    FieldMiddleName.getText() + "', a.B_day = '" + bdaystr + "',\n" +
                    "a.Tel = '"+ FieldTelephone.getText() +"', a.Date_to_join = '" + joindatestr 
                    + "', a.IdPost = " + idpost + ", a.IdCat = "+ idcat +"\n" +
             "where a.IdHS = " + currentUpdateIdHS + ";");
            int n = sta.executeUpdate();
             if(n <= 0)
                {
                    JOptionPane.showMessageDialog(null, "Ошибка обновления");
                }        
             else JOptionPane.showMessageDialog(null, "Данные о сотруднике успешно обновлены");
             }
             catch(NullPointerException e)
            {
                JOptionPane.showMessageDialog(null, "Введите данные");
                
            } catch (SQLException ex) {
            Logger.getLogger(MedRegistration.class.getName()).log(Level.SEVERE, null, ex);
        }         
         /*try {                                               
            Statement s = cn.createStatement();
            String fio, tel, bdaystr, joindatestr, login, password, descr = "";
            String Sql = "";
            int post = 0, tt = 0, flag = 0, cat = 0;
            Date bday, joindate;
            
            fio = FieldSurname.getText() + " " + FieldName.getText() + " " + FieldMiddleName.getText();
            tel = FieldTelephone.getText();
            login = TextFieldLogin.getText(); 
            descr = TextAreaDescribes.getText();
            password = new String(PasswordField.getPassword());
            System.out.println(password + "\n\n");
            ResultSet r = s.executeQuery("select IdHS from hospitalstaff where Login = '" + TextFieldLogin.getText() + "';");
 
            if(TextFieldLogin.getText().equals(""))
            {
                JOptionPane.showMessageDialog(null, "Введите логин");
                flag = 1;
            } 
            else if(r.first())
            {
                //JOptionPane.showMessageDialog(null, "Input another medcard number");
                JOptionPane.showMessageDialog(null, "Такой логин уже существует");
                flag = 1;
            }
            else
            if(PasswordField.getPassword().toString().equals(""))
            {
                JOptionPane.showMessageDialog(null, "Введите пароль");
                flag = 1;
            } 
            else
            if(FieldSurname.getText().equals(""))
            {
                JOptionPane.showMessageDialog(null, "Введите фамилию");
                flag = 1;
            } 
            else if(FieldName.getText().equals(""))
            {
                JOptionPane.showMessageDialog(null, "Введитя имя");
                flag = 1;
            }
            else if(FieldMiddleName.getText().equals(""))
            {
                JOptionPane.showMessageDialog(null, "Введите отчество");
                flag = 1;
            }
            
            if(!checkWithRegExpTel(tel))
            {
                JOptionPane.showMessageDialog(null, "Номер телефона в формате +38(000)1234567");
            }
            else if(flag == 0)
            {    JOptionPane.showMessageDialog(null, "OK");

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            bdaystr = format.format(DateChooserBDay.getDate());
            joindatestr = format.format(DateChooserJoinDay.getDate());

            try {
                bday = format.parse(bdaystr);
            } catch (java.text.ParseException ex) {
                System.out.println("Это не должно произойти " + ex);
            }
            try {
                joindate = format.parse(joindatestr);
            } catch (java.text.ParseException ex) {
                System.out.println("Это не должно произойти " + ex);
            }
       
            ResultSet rs2 = s.executeQuery("select IdPost from post where PostName = '" + ComboBoxPost.getSelectedItem().toString() + "'");
            while(rs2.next())
            {
                post =  rs2.getInt("IdPost");
            }
            
            ResultSet rs0 = s.executeQuery("select IdCat from category where CategoryName = '" + ComboBoxCategory.getSelectedItem().toString() + "'");
            while(rs0.next())
            {
                cat =  rs0.getInt("IdCat");
            }
            Sql = "insert into hospitalstaff (Login, Pass, HSFIO, B_day, Date_to_join, Tel, IdPost, IdCat, Describes) values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
          
            try {
                PreparedStatement ps = cn.prepareStatement(Sql);
                ps.setString(1, login);
                ps.setString(2, password);
                ps.setString(3, fio);
                ps.setString(4, bdaystr);
                ps.setString(5, joindatestr);
                ps.setString(6, tel);
                ps.setInt(7, post);
                ps.setInt(8, cat);
                ps.setString(9, descr);
                
                int n = ps.executeUpdate();
                
                if(n > 0)
                {
                     ResultSet rs3 = s.executeQuery("select IdHS from hospitalstaff where Login = '" + login + "'");
            if(rs3.next())
            {
            }
                    idhs =  Integer.parseInt(rs3.getString("IdHS"));
                }
                
            }
             catch(NullPointerException e)
            {
                JOptionPane.showMessageDialog(null, "Input date");
                flag = 1;
            }
             catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex);
            }
            }
        } catch (SQLException ex) {
            Logger.getLogger(PatientRegistration.class.getName()).log(Level.SEVERE,null, ex);
        }
                  
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
                ps.setString(6, "hospitalstaff");
                ps.setString(7, "insert into hospitalstaff values(...)");
                
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
                           }  */
    }//GEN-LAST:event_ButtonSaveActionPerformed

    private void ButtonSignInActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonSignInActionPerformed
        this.dispose();
    }//GEN-LAST:event_ButtonSignInActionPerformed

    private void ButtonUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonUpdateActionPerformed
    if(!TextFieldLogin.isEditable())
    {
        ButtonAddPost.setEnabled(true);
        ButtonAddCategory.setEnabled(true);
        TextFieldLogin.setEditable(true);
        FieldSurname.setEditable(true);
        FieldName.setEditable(true);
        FieldMiddleName.setEditable(true);
        FieldTelephone.setEditable(true);
        PasswordField.setEditable(true);
        TextAreaDescribes.setEditable(true);
        DateChooserJoinDay.setEnabled(true);
        DateChooserBDay.setEnabled(true);
        ComboBoxPost.setEnabled(true);
        ComboBoxCategory.setEnabled(true);
    }
    else
    {
        ButtonAddPost.setEnabled(false);
        ButtonAddCategory.setEnabled(false);
        TextFieldLogin.setEditable(false);
        FieldSurname.setEditable(false);
        FieldName.setEditable(false);
        FieldMiddleName.setEditable(false);
        FieldTelephone.setEditable(false);
        PasswordField.setEditable(false);
        TextAreaDescribes.setEditable(false);
        DateChooserJoinDay.setEnabled(false);
        DateChooserBDay.setEnabled(false);
        ComboBoxPost.setEnabled(false);
        ComboBoxCategory.setEnabled(false);       
    }                    
        
        
        
        /*            try
            {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String bdaystr = format.format(DateChooserBDay.getDate());
            String joindatestr = format.format(DateChooserJoinDay.getDate());
            int idpost = 0, idcat = 0;
            Statement s = cn.createStatement();
            ResultSet rs2 = s.executeQuery("select IdPost from post where PostName = '" + ComboBoxPost.getSelectedItem().toString() + "'");
            while(rs2.next())
            {
            idpost = rs2.getInt("IdPost");
            }
            
            ResultSet rs3 = s.executeQuery("select IdCat from category where CategoryName = '" + ComboBoxCategory.getSelectedItem().toString() + "'");
            while(rs3.next())
            { 
             idcat = rs3.getInt("IdCat");
            }

            PreparedStatement sta = cn.prepareStatement("update hospitalstaff a set a.Describes = '" + TextAreaDescribes.getText()
                    + "', a.HSFIO = '" + 
                    FieldSurname.getText() + " " + 
                    FieldName.getText() + " " + 
                    FieldMiddleName.getText() + "', a.B_day = '" + bdaystr + "',\n" +
                    "a.Tel = '"+ FieldTelephone.getText() +"', a.Date_to_join = '" + joindatestr 
                    + "', a.IdPost = " + idpost + ", a.IdCat = "+ idcat +"\n" +
             "where a.IdHS = " + currentUpdateIdHS + ";");
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
                
            } catch (SQLException ex) {
            Logger.getLogger(MedRegistration.class.getName()).log(Level.SEVERE, null, ex);
        }
        logQuery("update info about hospital staff");*/
    }//GEN-LAST:event_ButtonUpdateActionPerformed

    private void ComboBoxCategoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ComboBoxCategoryActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ComboBoxCategoryActionPerformed

    private void PasswordFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PasswordFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_PasswordFieldActionPerformed

    private void FieldNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FieldNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_FieldNameActionPerformed

    private void ButtonAddPostActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonAddPostActionPerformed
        new AddPost().setVisible(true);
    }//GEN-LAST:event_ButtonAddPostActionPerformed

    private void ButtonAddCategoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonAddCategoryActionPerformed
        new AddCategory().setVisible(true);
    }//GEN-LAST:event_ButtonAddCategoryActionPerformed

    
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
                ps.setString(6, "hospitalstaff");
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
            java.util.logging.Logger.getLogger(MedRegistration.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MedRegistration.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MedRegistration.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MedRegistration.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MedRegistration().setVisible(true);
            }
        });
    }

    
    
    
     private javax.swing.JButton ButtonAddCategory;
    private javax.swing.JButton ButtonAddPost;
    private javax.swing.JButton ButtonSave;
    private javax.swing.JButton ButtonSignIn;
    private javax.swing.JButton ButtonUpdate;
    public static javax.swing.JComboBox ComboBoxCategory;
    public static javax.swing.JComboBox ComboBoxPost;
    private com.toedter.calendar.JDateChooser DateChooserBDay;
    private com.toedter.calendar.JDateChooser DateChooserJoinDay;
    private javax.swing.JTextField FieldMiddleName;
    private javax.swing.JTextField FieldName;
    private javax.swing.JTextField FieldSurname;
    private javax.swing.JTextField FieldTelephone;
    private javax.swing.JPasswordField PasswordField;
    private javax.swing.JTextArea TextAreaDescribes;
    private javax.swing.JTextField TextFieldLogin;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    
    /*
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton ButtonAddCategory;
    private javax.swing.JButton ButtonAddPost;
    private javax.swing.JButton ButtonSave;
    private javax.swing.JButton ButtonSignIn;
    private javax.swing.JButton ButtonUpdate;
    private javax.swing.JComboBox ComboBoxCategory;
    private javax.swing.JComboBox ComboBoxPost;
    private com.toedter.calendar.JDateChooser DateChooserBDay;
    private com.toedter.calendar.JDateChooser DateChooserJoinDay;
    private javax.swing.JTextField FieldMiddleName;
    private javax.swing.JTextField FieldName;
    private javax.swing.JTextField FieldSurname;
    private javax.swing.JTextField FieldTelephone;
    private javax.swing.JPasswordField PasswordField;
    private javax.swing.JTextArea TextAreaDescribes;
    private javax.swing.JTextField TextFieldLogin;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
*/
    }
