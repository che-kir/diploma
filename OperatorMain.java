/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spectrex;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.TableRowSorter;
import static spectrex.Accommodation.checkWithRegExpFlat;
import static spectrex.Accommodation.checkWithRegExpHouse;
import static spectrex.Accommodation.checkWithRegExpPostIndex;
import static spectrex.MedLogin.cn;
import static spectrex.MedLogin.idhs;
import static spectrex.PatientFound.resultSetToTableModel;
import static spectrex.PatientRegistration.checkWithRegExpEmail;
import static spectrex.PatientRegistration.checkWithRegExpPass;
import static spectrex.PatientRegistration.checkWithRegExpTel;

/**
 *
 * @author Kirill
 */
public class OperatorMain extends javax.swing.JFrame {

    /**
     * Creates new form OperatorMain
     *
     * @throws java.sql.SQLException
     */
    public Connection cn = MedLogin.cn;
    public OperatorMain() {
        initComponents();
        java.awt.Dimension dim = getToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getWidth() / 2,
                dim.height / 2 - this.getHeight() / 2);
        GenderList.addItem("Мужской");
        GenderList.addItem("Женский");

        Statement s = null;
        try {
            s = cn.createStatement();
        } catch (SQLException ex) {
            Logger.getLogger(OperatorMain.class.getName()).log(Level.SEVERE, null, ex);
        }
        ResultSet rs = null;
        try {
            rs = s.executeQuery("SELECT Inst_name FROM institution");
        } catch (SQLException ex) {
            Logger.getLogger(OperatorMain.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            while (rs.next()) {
                InstList.addItem(rs.getString("Inst_name"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(OperatorMain.class.getName()).log(Level.SEVERE, null, ex);
        }
        ResultSet rs1 = null;
        try {
            rs1 = s.executeQuery("SELECT GroupName FROM diagnosticgroup");
        } catch (SQLException ex) {
            Logger.getLogger(OperatorMain.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            while (rs1.next()) {
                DGList.addItem(rs1.getString("GroupName"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(OperatorMain.class.getName()).log(Level.SEVERE, null, ex);
        }

        //view and updating cards
        try {
            Statement sta = cn.createStatement();
            // ResultSet rs;
            rs = sta.executeQuery("select N_med_card as \"Номер карты\", FIO as \"ФИО\", Gender as \"Пол\", B_day as \"Дата рождения\", Tel as \"Телефон\",\n"
                    + "Acc_date as \"Дата поступления\", Diagnosis as \"Последний диагноз\", Session_date as \"Дата последнего сеанса\", Allergy as \"Аллергия\", Inst_name as \"Направившее учреждение\", GroupName as \"Диагностическая группа\"\n"
                    + "from ((patient p join institution i on p.IdInst = i.IdInst) join diagnosticgroup d on p.IdDG = d.IdDG)\n"
                    + " join seans s on p.IdP = s.IdP where \n"
                    + "Session_date = ( select Session_date from seans z where Session_date >= all \n"
                    + "(select Session_date from seans x where z.IdP = x.IdP ) and z.IdP = p.IdP group by IdP )\n"
                    + "union\n"
                    + "select N_med_card, FIO, Gender, B_day, Tel, Acc_date, \"\", \"\", Allergy, Inst_name, GroupName\n"
                    + "from ((patient p join institution i on p.IdInst = i.IdInst) join diagnosticgroup d on p.IdDG = d.IdDG)\n"
                    + "where not exists(select IdP from seans v where v.IdP = p.IdP)");
            TableResult.setModel(resultSetToTableModel(rs));
            Comparator myComparator = new java.util.Comparator() {
                /**
                 * Custom compare to sort numbers as numbers. Strings as
                 * strings, with numbers ordered before strings.
                 *
                 * @param o1
                 * @param o2
                 * @return
                 */
                @Override
                public int compare(Object oo1, Object oo2) {
                    boolean isFirstNumeric, isSecondNumeric;
                    String o1 = oo1.toString(), o2 = oo2.toString();

                    isFirstNumeric = o1.matches("\\d+");
                    isSecondNumeric = o2.matches("\\d+");

                    if (isFirstNumeric) {
                        if (isSecondNumeric) {
                            return Integer.valueOf(o1).compareTo(Integer.valueOf(o2));
                        } else {
                            return -1; // numbers always smaller than letters
                        }
                    } else {
                        if (isSecondNumeric) {
                            return 1; // numbers always smaller than letters
                        } else {
                // Those lines throw ArrayIndexOutOfBoundsException
                            //                        isFirstNumeric = o1.split("[^0-9]")[0].matches("\\d+");
                            //                        isSecondNumeric = o2.split("[^0-9]")[0].matches("\\d+");

                // Trying to parse String to Integer.
                            // If there is no Exception then Object is numeric, else it's not.
                            try {
                                Integer.parseInt(o1);
                                isFirstNumeric = true;
                            } catch (NumberFormatException e) {
                                isFirstNumeric = false;
                            }
                            try {
                                Integer.parseInt(o2);
                                isSecondNumeric = true;
                            } catch (NumberFormatException e) {
                                isSecondNumeric = false;
                            }

                            if (isFirstNumeric) {
                                if (isSecondNumeric) {
                                    int intCompare = Integer.valueOf(o1.split("[^0-9]")[0]).compareTo(Integer.valueOf(o2.split("[^0-9]")[0]));
                                    if (intCompare == 0) {
                                        return o1.compareToIgnoreCase(o2);
                                    }
                                    return intCompare;
                                } else {
                                    return -1; // numbers always smaller than letters
                                }
                            } else {
                                if (isSecondNumeric) {
                                    return 1; // numbers always smaller than letters
                                } else {
                                    return o1.compareToIgnoreCase(o2);
                                }
                            }
                        }
                    }
                }
            };

            TableRowSorter sorter = new TableRowSorter();
            sorter.setModel(TableResult.getModel());
            sorter.setComparator(0, myComparator);
            sorter.toggleSortOrder(1); //sort column FIO
            TableResult.setRowSorter(sorter);
            TableResult.addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent me) {
                    JTable table = (JTable) me.getSource();
                    Point p = me.getPoint();
                    int row = table.rowAtPoint(p);
                    if (me.getClickCount() == 2) {
                        // JOptionPane.showMessageDialog(null, "Выбрана строка " + row);
                        String numcard = table.getValueAt(row, 0).toString();
                        new PatientCard(numcard).setVisible(true);
                    }
                }
            });
            TableResult.setEnabled(false);

            tableOfQueries.addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent me) {
                    JTable table = (JTable) me.getSource();
                    Point p = me.getPoint();
                    int row = table.rowAtPoint(p);
                    String dateFrom = "", dateTo = "", text = "";
                    if (tableOfQueries.getValueAt(row, 1) != null) {
                        text = tableOfQueries.getValueAt(row, 1).toString();
                    }
                    if (tableOfQueries.getValueAt(row, 2) != null) {
                        dateFrom = tableOfQueries.getValueAt(row, 2).toString();
                    }
                    if (tableOfQueries.getValueAt(row, 3) != null) {
                        dateTo = tableOfQueries.getValueAt(row, 3).toString();
                    }
                    if (me.getClickCount() == 3) {
                        QueryOfPatient query = new QueryOfPatient();
                        query.getQuery(row + 1, text, dateFrom, dateTo);
                    }
                }
            });

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

        jTabbedPane1 = new javax.swing.JTabbedPane();
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
        jPanel2 = new javax.swing.JPanel();
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
        ButtonPatientRegistration = new javax.swing.JButton();
        ButtonAddInstitution = new javax.swing.JButton();
        jLabel27 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        TableResult = new javax.swing.JTable();
        jScrollPane4 = new javax.swing.JScrollPane();
        TableForFinding = new javax.swing.JTable();
        ButtonFindPatientByFields = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tableOfQueries = new javax.swing.JTable();
        ButtonExit = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

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

        InstList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                InstListActionPerformed(evt);
            }
        });

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

        jLabel18.setText(bundle.getString("OperatorMain.jLabel18.text")); // NOI18N

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("OperatorMain.jPanel3.border.title"))); // NOI18N

        jLabel19.setText(bundle.getString("OperatorMain.jLabel19.text")); // NOI18N

        jLabel20.setText(bundle.getString("OperatorMain.jLabel20.text_1")); // NOI18N

        TextFieldCountry.setText(bundle.getString("OperatorMain.TextFieldCountry.text")); // NOI18N

        jLabel21.setText(bundle.getString("OperatorMain.jLabel21.text")); // NOI18N

        TextFieldArea.setText(bundle.getString("OperatorMain.TextFieldArea.text")); // NOI18N

        jLabel22.setText(bundle.getString("OperatorMain.jLabel22.text")); // NOI18N

        jLabel23.setText(bundle.getString("OperatorMain.jLabel23.text")); // NOI18N

        TextFieldLocality.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TextFieldLocalityActionPerformed(evt);
            }
        });

        jLabel24.setText(bundle.getString("OperatorMain.jLabel24.text")); // NOI18N

        jLabel25.setText(bundle.getString("OperatorMain.jLabel25.text")); // NOI18N

        jLabel26.setText(bundle.getString("OperatorMain.jLabel26.text")); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(27, 27, 27)
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
                .addContainerGap(38, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(TextFieldPostIndex, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel22)
                    .addComponent(TextFieldDistrict, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(TextFieldCountry, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel23)
                    .addComponent(TextFieldLocality, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21)
                    .addComponent(TextFieldArea, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(50, 50, 50)
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
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        ButtonPatientRegistration.setText(bundle.getString("OperatorMain.ButtonPatientRegistration.text")); // NOI18N
        ButtonPatientRegistration.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonPatientRegistrationActionPerformed(evt);
            }
        });

        ButtonAddInstitution.setText(bundle.getString("OperatorMain.ButtonAddInstitution.text")); // NOI18N
        ButtonAddInstitution.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonAddInstitutionActionPerformed(evt);
            }
        });

        jLabel27.setText(bundle.getString("OperatorMain.jLabel27.text")); // NOI18N

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
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(TelephoneText, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(DateChooserBDay, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(PassportText, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(DateChooserAccDay, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(InstList, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(DGList, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addComponent(ButtonAddInstitution, javax.swing.GroupLayout.DEFAULT_SIZE, 127, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel27)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(ButtonPatientRegistration)
                .addGap(23, 23, 23))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
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
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jLabel9)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addComponent(jLabel18)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabel10)
                                            .addGap(38, 38, 38)))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(EmailText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(33, 33, 33)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(GenderList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(DGList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel11)
                                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))))
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
                                    .addComponent(ButtonAddInstitution))))
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
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(14, 14, 14)
                        .addComponent(jLabel27)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 67, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)))
                .addComponent(ButtonPatientRegistration)
                .addContainerGap())
        );

        jTabbedPane1.addTab(bundle.getString("OperatorMain.jPanel1.TabConstraints.tabTitle"), jPanel1); // NOI18N

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        TableResult.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(TableResult);

        TableForFinding.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, "", "", "", null, null, null, null, null, null, null}
            },
            new String [] {
                "Номер карты", "ФИО", "Пол", "Дата рождения", "Телефон", "Дата поступления", "Последний диагноз", "Дата последнего сеанса", "Аллергия", "Направившее учреждение", "Диагностическая группа"
            }
        ));
        TableForFinding.setCellSelectionEnabled(true);
        TableForFinding.setSelectionBackground(new java.awt.Color(102, 255, 255));
        TableForFinding.setSurrendersFocusOnKeystroke(true);
        jScrollPane4.setViewportView(TableForFinding);
        if (TableForFinding.getColumnModel().getColumnCount() > 0) {
            TableForFinding.getColumnModel().getColumn(0).setHeaderValue(bundle.getString("OperatorMain.TableForFinding.columnModel.title0")); // NOI18N
            TableForFinding.getColumnModel().getColumn(1).setHeaderValue(bundle.getString("OperatorMain.TableForFinding.columnModel.title1")); // NOI18N
            TableForFinding.getColumnModel().getColumn(2).setHeaderValue(bundle.getString("OperatorMain.TableForFinding.columnModel.title2")); // NOI18N
            TableForFinding.getColumnModel().getColumn(3).setHeaderValue(bundle.getString("OperatorMain.TableForFinding.columnModel.title3")); // NOI18N
            TableForFinding.getColumnModel().getColumn(4).setHeaderValue(bundle.getString("OperatorMain.TableForFinding.columnModel.title4")); // NOI18N
            TableForFinding.getColumnModel().getColumn(5).setHeaderValue(bundle.getString("OperatorMain.TableForFinding.columnModel.title5")); // NOI18N
            TableForFinding.getColumnModel().getColumn(6).setHeaderValue(bundle.getString("OperatorMain.TableForFinding.columnModel.title6")); // NOI18N
            TableForFinding.getColumnModel().getColumn(7).setHeaderValue(bundle.getString("OperatorMain.TableForFinding.columnModel.title7")); // NOI18N
            TableForFinding.getColumnModel().getColumn(8).setHeaderValue(bundle.getString("OperatorMain.TableForFinding.columnModel.title8")); // NOI18N
            TableForFinding.getColumnModel().getColumn(9).setHeaderValue(bundle.getString("OperatorMain.TableForFinding.columnModel.title9")); // NOI18N
            TableForFinding.getColumnModel().getColumn(10).setHeaderValue(bundle.getString("OperatorMain.TableForFinding.columnModel.title10")); // NOI18N
        }

        ButtonFindPatientByFields.setText(bundle.getString("OperatorMain.ButtonFindPatientByFields.text")); // NOI18N
        ButtonFindPatientByFields.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonFindPatientByFieldsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1230, Short.MAX_VALUE)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(ButtonFindPatientByFields, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 381, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(ButtonFindPatientByFields)
                .addGap(20, 20, 20))
        );

        jTabbedPane1.addTab(bundle.getString("OperatorMain.jPanel4.TabConstraints.tabTitle"), jPanel4); // NOI18N

        tableOfQueries.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"Сколько раз пациентам за прошлый месяц поставили заданный диагноз?", null, null, null},
                {"Кто из пациентов родился в заданный период?", null, "ГГГГ-ММ-ДД", "ГГГГ-ММ-ДД"},
                {"Кто из пациентов поступил на учет в заданный период?", null, "ГГГГ-ММ-ДД", "ГГГГ-ММ-ДД"},
                {"Кто из пациентов проживает по заданному району? (факт. адрес)", null, null, null},
                {"Кто из пациентов относится к заданной диагностической группе?", null, null, null},
                {"Кто из пациентов не присутствовал ни на одном сеансе?", null, null, null},
                {"Сколько пациентов направило на обследование каждое учреждение? ", null, null, null}
            },
            new String [] {
                "Название запроса", "Поле ввода", "С ... даты", "По ... дату"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane5.setViewportView(tableOfQueries);
        if (tableOfQueries.getColumnModel().getColumnCount() > 0) {
            tableOfQueries.getColumnModel().getColumn(0).setHeaderValue(bundle.getString("OperatorMain.tableOfQueries.columnModel.title0")); // NOI18N
            tableOfQueries.getColumnModel().getColumn(1).setHeaderValue(bundle.getString("OperatorMain.tableOfQueries.columnModel.title1")); // NOI18N
            tableOfQueries.getColumnModel().getColumn(2).setHeaderValue(bundle.getString("OperatorMain.tableOfQueries.columnModel.title2")); // NOI18N
            tableOfQueries.getColumnModel().getColumn(3).setHeaderValue(bundle.getString("OperatorMain.tableOfQueries.columnModel.title3")); // NOI18N
        }

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 1224, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(81, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab(bundle.getString("OperatorMain.jPanel5.TabConstraints.tabTitle"), jPanel5); // NOI18N

        ButtonExit.setText(bundle.getString("OperatorMain.ButtonExit.text")); // NOI18N
        ButtonExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonExitActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(ButtonExit, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTabbedPane1)
                .addGap(18, 18, 18)
                .addComponent(ButtonExit)
                .addGap(22, 22, 22))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /* public void mouseClicked(MouseEvent e) {
     if (e.getClickCount() == 2) {
     JTable target = (JTable)e.getSource();
     int row = target.getSelectedRow();
     JOptionPane.showMessageDialog(null, "Выбрана строка " + row);
     }
     }*/

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

    private void TextFieldLocalityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TextFieldLocalityActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TextFieldLocalityActionPerformed

    private void ButtonPatientRegistrationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonPatientRegistrationActionPerformed
        try {
            Statement s = cn.createStatement();
            String medcard, fio, tel, passport, disease, allergy, email, bdaystr, accdatestr;
            String Sql;
            Date bday, accdate;
            String sex;
            int dgroup = 0, inst = 0, flag = 0;
            medcard = N_med_cardText.getText();
            ResultSet r = s.executeQuery("select N_med_card from patient where N_med_card = " + medcard);
            if (medcard.equals("")) {
                // JOptionPane.showMessageDialog(null, "Input medcard number");
                JOptionPane.showMessageDialog(null, "Введите номер медкарты");
                flag = 1;
            } else if (r.first()) {
                //JOptionPane.showMessageDialog(null, "Input another medcard number");
                JOptionPane.showMessageDialog(null, "Такой номер медкарты уже существует");
                flag = 1;
            }

            fio = SurnameText.getText() + " " + NameText.getText() + " " + MiddleNameText.getText();
            if (SurnameText.getText().equals("")) {
                // JOptionPane.showMessageDialog(null, "Input surname");
                JOptionPane.showMessageDialog(null, "Введите фамилию");
                flag = 1;
            } else if (NameText.getText().equals("")) {
                //JOptionPane.showMessageDialog(null, "Input name");
                JOptionPane.showMessageDialog(null, "Введите имя");
                flag = 1;
            } else if (MiddleNameText.getText().equals("")) {
                //  JOptionPane.showMessageDialog(null, "Input middle name");
                JOptionPane.showMessageDialog(null, "Введите отчество");
                flag = 1;
            }

            tel = TelephoneText.getText();
            passport = PassportText.getText();
            email = EmailText.getText();

            if (!checkWithRegExpTel(tel)) {
                //JOptionPane.showMessageDialog(null, "Wrong telephone format");
                JOptionPane.showMessageDialog(null, "Номер телефона в формате +38(000)1234567");
                flag = 1;
            } else if (!checkWithRegExpEmail(email) && !email.equals("")) {
                // JOptionPane.showMessageDialog(null, "Wrong e-mail");
                JOptionPane.showMessageDialog(null, "Неверный формат e-mail");
                flag = 1;
            } else if (!checkWithRegExpPass(passport) && !passport.equals("")) {
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

            try {
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

                if (GenderList.getSelectedItem().toString().equals("Мужской")) {
                    sex = "М";
                } else {
                    sex = "Ж";
                }

                ResultSet rs2 = s.executeQuery("select IdInst from institution where Inst_name = '" + InstList.getSelectedItem().toString() + "'");
                while (rs2.next()) {
                    System.out.println(rs2.getString("IdInst"));
                    inst = Integer.parseInt(rs2.getString("IdInst"));
                }

                ResultSet rs3 = s.executeQuery("select IdDG from diagnosticgroup where GroupName = '" + DGList.getSelectedItem().toString() + "'");
                while (rs3.next()) {
                    dgroup = Integer.parseInt(rs3.getString("IdDG"));
                }
                Sql = "insert into patient (N_med_card, FIO, Gender, B_day, Tel, Passport, "
                        + "Acc_date, Disease, Allergy, E_mail, IdInst, IdDG) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                try {
                    if (flag == 0) {
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

                        if (n <= 0) {
                            JOptionPane.showMessageDialog(null, "Oшибка регистрации");
                        }
                    }

                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, ex);
                }
            } catch (NullPointerException e) {
                //JOptionPane.showMessageDialog(null, "Input date");
                JOptionPane.showMessageDialog(null, "Введите даты");
                flag = 1;
            }
        } catch (SQLException ex) {
            Logger.getLogger(PatientRegistration.class.getName()).log(Level.SEVERE, null, ex);
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

                if (n <= 0) {
                    JOptionPane.showMessageDialog(null, "message_journal_BAD_QueryEntry");
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
            //log

        //home address
        try {
            Statement s = cn.createStatement();
            String indexstr, flatstr, country, area, district, locality, street, house;
            String Sql;
            Byte same = 0;
            int index = 0, flat = 0, flag = 0, idp = 1;
            indexstr = TextFieldPostIndex.getText();

            if (!checkWithRegExpPostIndex(indexstr) && !indexstr.equals("")) {
                JOptionPane.showMessageDialog(null, "Некорректный ввод почтового индекса");
                flag = 1;
            } else if (checkWithRegExpPostIndex(indexstr)) {
                index = Integer.parseInt(indexstr);
            }
            country = TextFieldCountry.getText();
            area = TextFieldArea.getText();
            district = TextFieldDistrict.getText();
            locality = TextFieldLocality.getText();
            street = TextFieldStreet.getText();
            house = TextFieldHouse.getText();
            flatstr = TextFieldFlat.getText();

            if (TextFieldCountry.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Введите Страну");
                flag = 1;
            } else if (TextFieldArea.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Введите Область");
                flag = 1;
            } else if (TextFieldLocality.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Введите населенный пункт");
                flag = 1;
            } else if (TextFieldStreet.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Введите улицу");
                flag = 1;
            } else if (TextFieldHouse.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Введите дом");
                flag = 1;
            }
            if (!checkWithRegExpHouse(house)) {
                JOptionPane.showMessageDialog(null, "Некорректный ввод номера дома");
                flag = 1;
            }
            if (!checkWithRegExpFlat(flatstr) && !flatstr.equals("")) {
                JOptionPane.showMessageDialog(null, "Некорректный ввод номера квартиры");
                flag = 1;
            } else if (checkWithRegExpFlat(flatstr)) {
                flat = Integer.parseInt(flatstr);
            }
            if (true) {
                // ButtonSaveFactAcc.setVisible(false);
                same = 1;
                Sql = "insert into factaccomodation (Same, PostIndex, Country, Area, District, Locality, Street, "
                        + "House, Flat, IdP) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                try {
                    if (flag == 0) {
                        PreparedStatement ps = cn.prepareStatement(Sql);
                        ps.setByte(1, same);
                        ps.setInt(2, index);
                        ps.setString(3, country);
                        ps.setString(4, area);
                        ps.setString(5, district);
                        ps.setString(6, locality);
                        ps.setString(7, street);
                        ps.setString(8, house);
                        ps.setInt(9, flat);
                        ps.setInt(10, idp);

                        int n = ps.executeUpdate();

                        if (n <= 0) {
                            JOptionPane.showMessageDialog(null, "Oшибка регистрации");
                        } else {
                            JOptionPane.showMessageDialog(null, "Пациент зарегистрирован");
                            N_med_cardText.setText("");
                            NameText.setText("");
                            SurnameText.setText("");
                            MiddleNameText.setText("");
                            EmailText.setText("");
                            DiseaseText.setText("");
                            AllergyText.setText("");
                            TelephoneText.setText("");
                            PassportText.setText("");
                            TextFieldArea.setText("");
                            TextFieldCountry.setText("");
                            TextFieldDistrict.setText("");
                            TextFieldFlat.setText("");
                            TextFieldHouse.setText("");
                            TextFieldLocality.setText("");
                            TextFieldPostIndex.setText("");
                            TextFieldStreet.setText("");
                        }
                    }

                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, ex);
                }
            }
            //else ButtonSaveFactAcc.setVisible(true);
           /* Sql = "insert into accommodation (PostIndex, Country, Area, District, Locality, Street, "
             + "House, Flat, IdP) values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
             try {
             if(flag == 0)
             {
             PreparedStatement ps = cn.prepareStatement(Sql);
             ps.setInt(1, index);
             ps.setString(2, country);
             ps.setString(3, area);
             ps.setString(4, district);
             ps.setString(5, locality);
             ps.setString(6, street);
             ps.setString(7, house);
             ps.setInt(8, flat);
             ps.setInt(9, idp);

                
             int n = ps.executeUpdate();
                
                
             }
                
             }*/
        } catch (SQLException ex) {
            Logger.getLogger(PatientRegistration.class.getName()).log(Level.SEVERE, null, ex);
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
                ps.setString(6, "accommodation");
                ps.setString(7, "insert into accommodation values(...)");

                int n = ps.executeUpdate();

                if (n <= 0) {
                    JOptionPane.showMessageDialog(null, "message_journal_BAD_QueryEntry");
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }

    }//GEN-LAST:event_ButtonPatientRegistrationActionPerformed

    private void ButtonFindPatientByFieldsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonFindPatientByFieldsActionPerformed
        ResultSet rs = null;
        String nameOfColumsSQL[] = {"N_med_card", "FIO", "Gender", "B_day", "Tel", "Acc_date", "Diagnosis", "Session_date", "Allergy", "Inst_name", "GroupName"};
        try {
            Statement sta = cn.createStatement();
            String query;

            query = "select N_med_card as \"Номер карты\", FIO as \"ФИО\", Gender as \"Пол\", B_day as \"Дата рождения\", Tel as \"Телефон\",\n"
                    + "Acc_date as \"Дата поступления\", Diagnosis as \"Последний диагноз\", Session_date as \"Дата последнего сеанса\", Allergy as \"Аллергия\", Inst_name as \"Направившее учреждение\", GroupName as \"Диагностическая группа\"\n"
                    + "from ((patient p join institution i on p.IdInst = i.IdInst) join diagnosticgroup d on p.IdDG = d.IdDG)\n"
                    + " join seans s on p.IdP = s.IdP where \n"
                    + "Session_date = ( select Session_date from seans z where Session_date >= all \n"
                    + "(select Session_date from seans x where z.IdP = x.IdP ) and z.IdP = p.IdP group by IdP )\n";
            for (int i = 0; i < TableForFinding.getColumnCount(); i++) {
                if (TableForFinding.getValueAt(0, i) != null) {
                    query += "and " + nameOfColumsSQL[i] + " like '%" + TableForFinding.getValueAt(0, i).toString() + "%' ";
                }
            }
            Boolean isQuery = false;
            if ((TableForFinding.getValueAt(0, 6) == null || TableForFinding.getValueAt(0, 6).equals(""))
                    && (TableForFinding.getValueAt(0, 7) == null || TableForFinding.getValueAt(0, 7).equals(""))) {
                isQuery = true;
            }
            if (isQuery) {
                query += "union\n"
                        + "select N_med_card, FIO, Gender, B_day, Tel, Acc_date, \"\", \"\", Allergy, Inst_name, GroupName\n"
                        + "from ((patient p join institution i on p.IdInst = i.IdInst) join diagnosticgroup d on p.IdDG = d.IdDG)\n"
                        + "where not exists(select IdP from seans v where v.IdP = p.IdP) ";
                for (int i = 0; i < TableForFinding.getColumnCount(); i++) {
                    if (TableForFinding.getValueAt(0, i) != null && i != 6 && i != 7) {
                        query += "and " + nameOfColumsSQL[i] + " like '%" + TableForFinding.getValueAt(0, i).toString() + "%' ";
                    }
                }
            }
            rs = sta.executeQuery(query);
            TableResult.setModel(resultSetToTableModel(rs));
            Comparator myComparator = new java.util.Comparator() {
                /**
                 * Custom compare to sort numbers as numbers. Strings as
                 * strings, with numbers ordered before strings.
                 *
                 * @param o1
                 * @param o2
                 * @return
                 */
                @Override
                public int compare(Object oo1, Object oo2) {
                    boolean isFirstNumeric, isSecondNumeric;
                    String o1 = oo1.toString(), o2 = oo2.toString();

                    isFirstNumeric = o1.matches("\\d+");
                    isSecondNumeric = o2.matches("\\d+");

                    if (isFirstNumeric) {
                        if (isSecondNumeric) {
                            return Integer.valueOf(o1).compareTo(Integer.valueOf(o2));
                        } else {
                            return -1; // numbers always smaller than letters
                        }
                    } else {
                        if (isSecondNumeric) {
                            return 1; // numbers always smaller than letters
                        } else {
                // Those lines throw ArrayIndexOutOfBoundsException
                            //                        isFirstNumeric = o1.split("[^0-9]")[0].matches("\\d+");
                            //                        isSecondNumeric = o2.split("[^0-9]")[0].matches("\\d+");

                // Trying to parse String to Integer.
                            // If there is no Exception then Object is numeric, else it's not.
                            try {
                                Integer.parseInt(o1);
                                isFirstNumeric = true;
                            } catch (NumberFormatException e) {
                                isFirstNumeric = false;
                            }
                            try {
                                Integer.parseInt(o2);
                                isSecondNumeric = true;
                            } catch (NumberFormatException e) {
                                isSecondNumeric = false;
                            }

                            if (isFirstNumeric) {
                                if (isSecondNumeric) {
                                    int intCompare = Integer.valueOf(o1.split("[^0-9]")[0]).compareTo(Integer.valueOf(o2.split("[^0-9]")[0]));
                                    if (intCompare == 0) {
                                        return o1.compareToIgnoreCase(o2);
                                    }
                                    return intCompare;
                                } else {
                                    return -1; // numbers always smaller than letters
                                }
                            } else {
                                if (isSecondNumeric) {
                                    return 1; // numbers always smaller than letters
                                } else {
                                    return o1.compareToIgnoreCase(o2);
                                }
                            }
                        }
                    }
                }
            };

            TableRowSorter sorter = new TableRowSorter();
            sorter.setModel(TableResult.getModel());
            sorter.setComparator(0, myComparator);
            sorter.toggleSortOrder(1); //sort column FIO
            TableResult.setRowSorter(sorter);
            /*TableResult.addMouseListener(new MouseAdapter() {
             public void mousePressed(MouseEvent me) {
             JTable table =(JTable) me.getSource();
             Point p = me.getPoint();
             int row = table.rowAtPoint(p);
             if (me.getClickCount() == 2) {
             // JOptionPane.showMessageDialog(null, "Выбрана строка " + row);
             String numcard = table.getValueAt(row, 0).toString();
             new PatientCard(numcard).setVisible(true);
             }
             }
             });*/
            TableResult.setEnabled(false);

        } catch (SQLException ex) {
            Logger.getLogger(PatientRegistration.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_ButtonFindPatientByFieldsActionPerformed

    private void ButtonAddInstitutionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonAddInstitutionActionPerformed
        new AddInst().setVisible(true);
    }//GEN-LAST:event_ButtonAddInstitutionActionPerformed

    private void InstListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_InstListActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_InstListActionPerformed

    private void ButtonExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonExitActionPerformed
        this.dispose();
        new MedLogin().setVisible(true);
    }//GEN-LAST:event_ButtonExitActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        /* try {
         for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
         if ("Nimbus".equals(info.getName())) {
         javax.swing.UIManager.setLookAndFeel(info.getClassName());
         break;
         }
         }
         } catch (ClassNotFoundException ex) {
         java.util.logging.Logger.getLogger(OperatorMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
         } catch (InstantiationException ex) {
         java.util.logging.Logger.getLogger(OperatorMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
         } catch (IllegalAccessException ex) {
         java.util.logging.Logger.getLogger(OperatorMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
         } catch (javax.swing.UnsupportedLookAndFeelException ex) {
         java.util.logging.Logger.getLogger(OperatorMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
         }*/
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new OperatorMain().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify                     
    private javax.swing.JTextArea AllergyText;
    private javax.swing.JButton ButtonAddInstitution;
    private javax.swing.JButton ButtonExit;
    private javax.swing.JButton ButtonFindPatientByFields;
    private javax.swing.JButton ButtonPatientRegistration;
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
    private javax.swing.JTable TableForFinding;
    private javax.swing.JTable TableResult;
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
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable tableOfQueries;
    /*
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea AllergyText;
    private javax.swing.JButton ButtonAddInstitution;
    private javax.swing.JButton ButtonExit;
    private javax.swing.JButton ButtonFindPatientByFields;
    private javax.swing.JButton ButtonPatientRegistration;
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
    private javax.swing.JTable TableForFinding;
    private javax.swing.JTable TableResult;
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
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable tableOfQueries;
    // End of variables declaration//GEN-END:variables
*/
}
