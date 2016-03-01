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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Kirill
 */
public class PatientCard extends javax.swing.JFrame {

    /**
     * Creates new form PatientCard
     */
    public Connection cn = MedLogin.cn;
    public static int idpat = 0;
    public PatientCard(String medcard) {    
        try {         
            Statement s = cn.createStatement();
            // ResultSet rs = s.executeQuery("SELECT * FROM patient where N_med_card = '" + medcard + "';");
            ResultSet rs = s.executeQuery("SELECT * FROM factaccomodation f join patient p on f.IdP = p.IdP where p.N_med_card = '" + medcard + "';");
            
            if(!rs.first())
            {
                initComponents();
                java.awt.Dimension dim = getToolkit().getScreenSize();
                this.setLocation(dim.width/2 - this.getWidth()/2,
                dim.height/2 - this.getHeight()/2);
                GenderList.addItem("Мужской");
                GenderList.addItem("Женский");
                s = cn.createStatement();
                rs = s.executeQuery("SELECT * FROM patient where N_med_card = '" + medcard + "';");
                
                if (rs.next() && medcard.equals(rs.getString("N_med_card")))
                {
                    idpat = rs.getInt("IdP");
                                 
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
               
                    if(rs.getString("E_mail").isEmpty())
                    {
                       
                        EmailText.setText("");
                    }
                    else 
                    {
                        EmailText.setText(rs.getString("E_mail"));
                    }

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
                        PassportText.setText("");
                    }
                    else PassportText.setText(rs.getString("Passport"));

                    //  DiseaseText.setText(rs.getString("Disease"));
                    if(rs.getString("Disease").isEmpty())
                    {
                        DiseaseText.setText("");
                    }
                    else DiseaseText.setText(rs.getString("Disease"));

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
            else 
            {
                initComponents();
                java.awt.Dimension dim = getToolkit().getScreenSize();
                this.setLocation(dim.width/2 - this.getWidth()/2,
                dim.height/2 - this.getHeight()/2);
                GenderList.addItem("Мужской");
                GenderList.addItem("Женский");
                if (medcard.equals(rs.getString("N_med_card")))
                {
                    idpat = rs.getInt("IdP");
                    TextFieldArea.setText(rs.getString("Area"));
                    TextFieldCountry.setText(rs.getString("Country"));
                    TextFieldDistrict.setText(rs.getString("District"));
                    TextFieldFlat.setText(String.valueOf(rs.getInt("Flat")));
                    TextFieldHouse.setText(rs.getString("House"));
                    TextFieldLocality.setText(rs.getString("Locality"));
                    TextFieldPostIndex.setText(String.valueOf(rs.getInt("PostIndex")));
                    TextFieldStreet.setText(rs.getString("Street"));
                 
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
                    else 
                    {
                        EmailText.setText(rs.getString("E_mail"));
                    }

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
                        PassportText.setText("");
                    }
                    else PassportText.setText(rs.getString("Passport"));

                    //  DiseaseText.setText(rs.getString("Disease"));
                    if(rs.getString("Disease").isEmpty())
                    {
                        DiseaseText.setText("");
                    }
                    else DiseaseText.setText(rs.getString("Disease"));

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
        ButtonAddInstitution.setEnabled(false);
        SurnameText.setEditable(false);
        NameText.setEditable(false);
        N_med_cardText.setEditable(false);
        MiddleNameText.setEditable(false);
        EmailText.setEditable(false);
        DiseaseText.setEditable(false);
        AllergyText.setEditable(false);
        TelephoneText.setEditable(false);
        PassportText.setEditable(false);
        TextFieldArea.setEditable(false);
        TextFieldCountry.setEditable(false);
        TextFieldDistrict.setEditable(false);
        TextFieldFlat.setEditable(false);
        TextFieldHouse.setEditable(false);
        TextFieldLocality.setEditable(false);
        TextFieldPostIndex.setEditable(false);
        TextFieldStreet.setEditable(false);
        InstList.setEnabled(false);
        DGList.setEnabled(false);
        GenderList.setEnabled(false);
        DateChooserAccDay.setEnabled(false);
        DateChooserBDay.setEnabled(false);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        ButtonSave = new javax.swing.JButton();
        ButtonCancel = new javax.swing.JButton();
        ButtonChangeData = new javax.swing.JButton();
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
        jLabel18 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        TextFieldPostIndex = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        TextFieldCountry = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        TextFieldArea = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        TextFieldDistrict = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        TextFieldLocality = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        TextFieldStreet = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        TextFieldHouse = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        TextFieldFlat = new javax.swing.JTextField();
        ButtonAddInstitution = new javax.swing.JButton();
        jLabel27 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("spectrex/Bundle"); // NOI18N
        ButtonSave.setText(bundle.getString("PatientCard.ButtonSave.text")); // NOI18N
        ButtonSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonSaveActionPerformed(evt);
            }
        });

        ButtonCancel.setText(bundle.getString("PatientCard.ButtonCancel.text")); // NOI18N
        ButtonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonCancelActionPerformed(evt);
            }
        });

        ButtonChangeData.setBackground(javax.swing.UIManager.getDefaults().getColor("CheckBoxMenuItem.selectionBackground"));
        ButtonChangeData.setForeground(new java.awt.Color(51, 0, 204));
        ButtonChangeData.setText(bundle.getString("PatientCard.ButtonChangeData.text")); // NOI18N
        ButtonChangeData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonChangeDataActionPerformed(evt);
            }
        });

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

        java.util.ResourceBundle bundle1 = java.util.ResourceBundle.getBundle("spectrex/Bundle_ru_RU"); // NOI18N
        jLabel18.setText(bundle1.getString("OperatorMain.jLabel18.text")); // NOI18N

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle1.getString("OperatorMain.jPanel3.border.title"))); // NOI18N

        jLabel19.setText(bundle1.getString("OperatorMain.jLabel19.text")); // NOI18N

        jLabel20.setText(bundle1.getString("OperatorMain.jLabel20.text_1")); // NOI18N

        TextFieldCountry.setText(bundle1.getString("OperatorMain.TextFieldCountry.text")); // NOI18N

        jLabel21.setText(bundle1.getString("OperatorMain.jLabel21.text")); // NOI18N

        TextFieldArea.setText(bundle1.getString("OperatorMain.TextFieldArea.text")); // NOI18N

        jLabel22.setText(bundle1.getString("OperatorMain.jLabel22.text")); // NOI18N

        jLabel23.setText(bundle1.getString("OperatorMain.jLabel23.text")); // NOI18N

        TextFieldLocality.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TextFieldLocalityActionPerformed(evt);
            }
        });

        jLabel24.setText(bundle1.getString("OperatorMain.jLabel24.text")); // NOI18N

        jLabel25.setText(bundle1.getString("OperatorMain.jLabel25.text")); // NOI18N

        jLabel26.setText(bundle1.getString("OperatorMain.jLabel26.text")); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE)
                            .addComponent(jLabel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(27, 27, 27))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel19)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(TextFieldHouse, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(TextFieldStreet, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(TextFieldCountry, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(TextFieldArea, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(TextFieldPostIndex, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(TextFieldFlat))
                .addGap(53, 53, 53)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel22)
                    .addComponent(jLabel23))
                .addGap(36, 36, 36)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(TextFieldLocality, javax.swing.GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE)
                    .addComponent(TextFieldDistrict))
                .addContainerGap(12, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(TextFieldPostIndex, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel22)
                    .addComponent(TextFieldDistrict, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(TextFieldCountry, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel23)
                    .addComponent(TextFieldLocality, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel21)
                    .addComponent(TextFieldArea, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(47, 47, 47)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel24)
                    .addComponent(TextFieldStreet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25)
                    .addComponent(TextFieldHouse, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel26)
                    .addComponent(TextFieldFlat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(128, Short.MAX_VALUE))
        );

        ButtonAddInstitution.setText(bundle.getString("PatientCard.ButtonAddInstitution.text")); // NOI18N
        ButtonAddInstitution.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonAddInstitutionActionPerformed(evt);
            }
        });

        jLabel27.setText(bundle.getString("PatientCard.jLabel27.text")); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
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
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel16)
                                    .addComponent(jLabel17)
                                    .addComponent(jLabel18))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 136, Short.MAX_VALUE)
                            .addComponent(TelephoneText, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(DateChooserBDay, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(PassportText)
                            .addComponent(DateChooserAccDay, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(InstList, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(DGList, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(ButtonAddInstitution, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 23, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel27)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(DateChooserBDay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(TelephoneText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(PassportText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(DateChooserAccDay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(InstList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(ButtonAddInstitution)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel1)
                                    .addComponent(N_med_cardText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel5))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel2)
                                        .addComponent(jLabel6))
                                    .addComponent(SurnameText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel3)
                                        .addComponent(jLabel7))
                                    .addComponent(NameText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel4)
                                        .addComponent(jLabel8))
                                    .addComponent(MiddleNameText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel9)
                                    .addComponent(EmailText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel18)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel10)))))
                        .addGap(7, 7, 7)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(DGList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11)
                            .addComponent(GenderList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel16)
                                    .addComponent(jLabel13))
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(6, 6, 6)
                                        .addComponent(jLabel14))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel17)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel15))
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel27))
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(ButtonChangeData)
                .addGap(18, 18, 18)
                .addComponent(ButtonSave)
                .addGap(18, 18, 18)
                .addComponent(ButtonCancel)
                .addGap(20, 20, 20))
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ButtonSave)
                    .addComponent(ButtonCancel)
                    .addComponent(ButtonChangeData))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void TextFieldLocalityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TextFieldLocalityActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TextFieldLocalityActionPerformed

    private void SurnameTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SurnameTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_SurnameTextActionPerformed

    private void N_med_cardTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_N_med_cardTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_N_med_cardTextActionPerformed

    private void NameTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NameTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_NameTextActionPerformed

    private void MiddleNameTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MiddleNameTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_MiddleNameTextActionPerformed

    private void TelephoneTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TelephoneTextActionPerformed

    }//GEN-LAST:event_TelephoneTextActionPerformed

    private void PassportTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PassportTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_PassportTextActionPerformed

    private void EmailTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EmailTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_EmailTextActionPerformed

    private void GenderListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_GenderListActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_GenderListActionPerformed

    private void ButtonChangeDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonChangeDataActionPerformed
    if(!N_med_cardText.isEditable())
    {
        ButtonAddInstitution.setEnabled(true);
        SurnameText.setEditable(true);
        NameText.setEditable(true);
        N_med_cardText.setEditable(true);
        MiddleNameText.setEditable(true);
        EmailText.setEditable(true);
        DiseaseText.setEditable(true);
        AllergyText.setEditable(true);
        TelephoneText.setEditable(true);
        PassportText.setEditable(true);
        TextFieldArea.setEditable(true);
        TextFieldCountry.setEditable(true);
        TextFieldDistrict.setEditable(true);
        TextFieldFlat.setEditable(true);
        TextFieldHouse.setEditable(true);
        TextFieldLocality.setEditable(true);
        TextFieldPostIndex.setEditable(true);
        TextFieldStreet.setEditable(true);        
        InstList.setEnabled(true);
        DGList.setEnabled(true);
        GenderList.setEnabled(true);
        DateChooserAccDay.setEnabled(true);
        DateChooserBDay.setEnabled(true);
    }
    else
    {
        ButtonAddInstitution.setEnabled(false);
        SurnameText.setEditable(false);
        NameText.setEditable(false);
        N_med_cardText.setEditable(false);
        MiddleNameText.setEditable(false);
        EmailText.setEditable(false);
        DiseaseText.setEditable(false);
        AllergyText.setEditable(false);
        TelephoneText.setEditable(false);
        PassportText.setEditable(false);
        TextFieldArea.setEditable(false);
        TextFieldCountry.setEditable(false);
        TextFieldDistrict.setEditable(false);
        TextFieldFlat.setEditable(false);
        TextFieldHouse.setEditable(false);
        TextFieldLocality.setEditable(false);
        TextFieldPostIndex.setEditable(false);
        TextFieldStreet.setEditable(false);       
        InstList.setEnabled(false);
        DGList.setEnabled(false);
        GenderList.setEnabled(false);
        DateChooserAccDay.setEnabled(false);
        DateChooserBDay.setEnabled(false);
    }
    }//GEN-LAST:event_ButtonChangeDataActionPerformed

    private void ButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonCancelActionPerformed
        this.dispose();
    }//GEN-LAST:event_ButtonCancelActionPerformed

    private void ButtonSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonSaveActionPerformed
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
                    JOptionPane.showMessageDialog(null, "Ошибка с обновлением карты");
                }        
          //   else JOptionPane.showMessageDialog(null, "Record successfully updated");
             }
             catch(NullPointerException e)
            {
                JOptionPane.showMessageDialog(null, "Введите данные");
                
            }
        }
        catch(SQLException ex)
        {
            Logger.getLogger(PatientRegistration.class.getName()).log(Level.SEVERE, null, ex);
        }
             
             
        //update for accommodation
        try
          {
            if(TextFieldArea.getText().equals("") ||
               TextFieldCountry.getText().equals("") ||
               TextFieldHouse.getText().equals("") ||
               TextFieldLocality.getText().equals("") ||
               TextFieldStreet.getText().equals("") )
            {
                JOptionPane.showMessageDialog(null, "Заполните поля, которые обязательны для ввода (*)");
            }
            else
            {
                int same = 1;
            PreparedStatement sta;
                try {
                    sta = cn.prepareStatement("update factaccomodation a set a.Area = '" + TextFieldArea.getText()
                            + "', a.Country = '" + TextFieldCountry.getText() + "', a.House = '" +
                            TextFieldHouse.getText() + "', a.Locality = '" + TextFieldLocality.getText() +
                            "', a.Street = '" + TextFieldStreet.getText() + "',\n" +
                            "a.PostIndex = '"+ TextFieldPostIndex.getText() +"', a.Flat = '" + TextFieldFlat.getText() +
                            "', a.District = '" + TextFieldDistrict.getText() + "', a.Same = '" + same + 
                            "' where a.IdP= " + idpat + ";");
                
            int n = 0;
                 try {
                     n = sta.executeUpdate();
                 } catch (SQLException ex) {
                     Logger.getLogger(PatientCard.class.getName()).log(Level.SEVERE, null, ex);
                 }
             if(n <= 0)
                {
                    JOptionPane.showMessageDialog(null, "Ошибка обновления карты");
                }        
             else JOptionPane.showMessageDialog(null, "Карта успешно обновлена");
             }  catch (SQLException ex) {
                    Logger.getLogger(PatientCard.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
          }
             catch(NullPointerException e)
            {
                JOptionPane.showMessageDialog(null, "Input date");
                
            }
        this.dispose();
          // logQuery("update info about patient");
    }//GEN-LAST:event_ButtonSaveActionPerformed

    private void ButtonAddInstitutionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonAddInstitutionActionPerformed
        new AddInst().setVisible(true);
    }//GEN-LAST:event_ButtonAddInstitutionActionPerformed

    /**
     * @param args the command line arguments
     */
/*    public static void main(String args[]) {
       
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PatientCard().setVisible(true);
            }
        });
    }*/
    
    
    
    // Variables declaration - do not modify                     
    private javax.swing.JTextArea AllergyText;
    private javax.swing.JButton ButtonAddInstitution;
    private javax.swing.JButton ButtonCancel;
    private javax.swing.JButton ButtonChangeData;
    private javax.swing.JButton ButtonSave;
    private javax.swing.JComboBox DGList;
    private com.toedter.calendar.JDateChooser DateChooserAccDay;
    private com.toedter.calendar.JDateChooser DateChooserBDay;
    private javax.swing.JTextArea DiseaseText;
    private javax.swing.JTextField EmailText;
    private javax.swing.JComboBox GenderList;
    public static javax.swing.JComboBox InstList;
    private javax.swing.JTextField MiddleNameText;
    private javax.swing.JTextField N_med_cardText;
    private javax.swing.JTextField NameText;
    private javax.swing.JTextField PassportText;
    private javax.swing.JTextField SurnameText;
    private javax.swing.JTextField TelephoneText;
    private javax.swing.JTextField TextFieldArea;
    private javax.swing.JTextField TextFieldCountry;
    private javax.swing.JTextField TextFieldDistrict;
    private javax.swing.JTextField TextFieldFlat;
    private javax.swing.JTextField TextFieldHouse;
    private javax.swing.JTextField TextFieldLocality;
    private javax.swing.JTextField TextFieldPostIndex;
    private javax.swing.JTextField TextFieldStreet;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
/*
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea AllergyText;
    private javax.swing.JButton ButtonAddInstitution;
    private javax.swing.JButton ButtonCancel;
    private javax.swing.JButton ButtonChangeData;
    private javax.swing.JButton ButtonSave;
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
    private javax.swing.JTextField TextFieldArea;
    private javax.swing.JTextField TextFieldCountry;
    private javax.swing.JTextField TextFieldDistrict;
    private javax.swing.JTextField TextFieldFlat;
    private javax.swing.JTextField TextFieldHouse;
    private javax.swing.JTextField TextFieldLocality;
    private javax.swing.JTextField TextFieldPostIndex;
    private javax.swing.JTextField TextFieldStreet;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    // End of variables declaration//GEN-END:variables
*/
    }
