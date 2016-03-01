/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spectrex;

import java.awt.Dimension;
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
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.TableRowSorter;
import static spectrex.MedLogin.cn;
import static spectrex.MedLogin.idhs;
import static spectrex.PatientFound.resultSetToTableModel;
import static spectrex.PatientRegistration.checkWithRegExpTel;

/**
 *
 * @author Kirill
 */
public class AdminMain extends javax.swing.JFrame {

    /**
     * Creates new form AdminMain
     */
    public Connection cn = MedLogin.cn;
    public AdminMain() {
        try {
            initComponents();
            java.awt.Dimension dim = getToolkit().getScreenSize();
            this.setLocation(dim.width / 2 - this.getWidth() / 2,
                    dim.height / 2 - this.getHeight() / 2);
            Statement s = cn.createStatement();
            ResultSet rs = s.executeQuery("SELECT PostName FROM post");
            while (rs.next()) {
                newMed_ComboBoxPost.addItem(rs.getString("PostName"));
            }
            ResultSet rs1 = s.executeQuery("SELECT CategoryName FROM category");
            while (rs1.next()) {
                newMed_ComboBoxCategory.addItem(rs1.getString("CategoryName"));
            }
            ResultSet rs2 = s.executeQuery("SELECT GroupName FROM diagnosticgroup");
            DefaultListModel listModel = new DefaultListModel();

            while (rs2.next()) {
                listModel.addElement(rs2.getString("GroupName"));
            }
            DiagnosticGroups_ListOfDiagnosticGroups.setModel(listModel);
            try {
                Statement sta = cn.createStatement();
                rs = sta.executeQuery("select IdHS as \"ID сотрудника\", Login as \"Логин\", HSFIO as \"ФИО\", B_day as \"Дата рождения\","
                        + " Date_to_join as \"Работает с\", Tel as \"Телефон\", Describes as \"О сотруднике\", PostName as \"Должность\", CategoryName as \"Категория\""
                        + " from (hospitalstaff h join post p on h.IdPost = p.IdPost) join category c on h.IdCat = c.IdCat");
                viewAndUpdatingMedData_TableResult.setModel(resultSetToTableModel(rs));
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
                sorter.setModel(viewAndUpdatingMedData_TableResult.getModel());
                sorter.setComparator(0, myComparator);
                sorter.toggleSortOrder(2); //sort column HSFIO
                viewAndUpdatingMedData_TableResult.setRowSorter(sorter);
                viewAndUpdatingMedData_TableResult.addMouseListener(new MouseAdapter() {
                    public void mousePressed(MouseEvent me) {
                        JTable table = (JTable) me.getSource();
                        Point p = me.getPoint();
                        int row = table.rowAtPoint(p);
                        if (me.getClickCount() == 2) {
                            // JOptionPane.showMessageDialog(null, "Выбрана строка " + row);
                            String idhs = table.getValueAt(row, 0).toString();
                            new MedRegistration(idhs).setVisible(true);
                        }
                    }
                });

                DiagnosticGroups_ListOfDiagnosticGroups.addMouseListener(new MouseAdapter() {
                    public void mousePressed(MouseEvent me) {
                        if (me.getClickCount() == 1) {
                            try {
                              
                                String groupName = DiagnosticGroups_ListOfDiagnosticGroups.getSelectedValue().toString();
                                Statement s = cn.createStatement();
                                ResultSet rs;
                                rs = s.executeQuery("select IdKV from diagnosticgroup where GroupName = '" + groupName + "';");
                                if(rs.next())
                                    try {
                                        rs = s.executeQuery("select GroupName, Description, D2, D3, D4, D5, D6, D8, D11, D15, " +
                                                "D20, D26, D36, D40, D65, D85, D120, D150, " +
                                                "D210, D290, D300, D520, D700, D950, D1300, D1700, " +
                                                "D2300, D3100, D4200, D5600, D7600, D10200, D13800, D18500 from diagnosticgroup d join kvvvfloat k on d.IdKV = k.IdKV where d.IdKV = " + rs.getInt(1)+ ";");
                                        if(rs.next())
                                        {
                                            diagnosticGroups_TextFieldGroupName.setText(rs.getString("GroupName"));
                                            DiagnosticGroups_TextAreaGroupDescription.setText(rs.getString("Description"));
                                          
                                            for(int i = 2; i < 10; i++)
                                                tableFirstQuarter.setValueAt(rs.getFloat(i+1), i-2, 1);
                                            for(int i = 10; i < 18; i++)
                                                tableSecondQuarter.setValueAt(rs.getFloat(i+1), i-10, 1);
                                            for(int i = 18; i < 26; i++)
                                                tableThirdQuarter.setValueAt(rs.getFloat(i+1), i-18, 1);
                                            for(int i = 26; i < 34; i++)
                                                tableFourthQuarter.setValueAt(rs.getFloat(i+1), i-26, 1);
                                          
                                        }
                                      
                                    } catch (NullPointerException e) {
                                        JOptionPane.showMessageDialog(null, "ошибка подключения");
                                        
                                    } catch (SQLException ex) {
                                        Logger.getLogger(MedRegistration.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                            } catch (SQLException ex) {
                                Logger.getLogger(AdminMain.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        } else if (me.getClickCount() == 2) {
                            try {
                               
                                int kv = 0;
                                float D[] = new float[32];
                                String groupName = DiagnosticGroups_ListOfDiagnosticGroups.getSelectedValue().toString();
                                Statement s = cn.createStatement();
                                ResultSet rs;
                                rs = s.executeQuery("select IdKV from diagnosticgroup where GroupName = '" + groupName + "';");
                                if(rs.next())
                                   kv = rs.getInt(1);
                                    try {
                                       
                                        ResultSet rs0 = s.executeQuery("SELECT * FROM kvvvfloat where IdKV = " + kv);
                                        
                                        if (rs0.next()) {
                                            for (int i = 0; i < 32; i++) {
                                                D[i] = (rs0.getFloat(i+2));
                                            }
                                        }
                                        ChartData data = new ChartData();
                                        ResultSet rs1 = s.executeQuery("SELECT GroupName FROM diagnosticgroup where IdKV = " + kv);
                                        if(rs1.next())
                                            data.addData(rs1.getString(1), D);
                                        Charts.createBarChart(data, "", "Размеры частиц КВВВ, нм", "Среднее значение процентного вклада, %", new Dimension(800, 600));
                                    } catch (SQLException ex) {
                                        Logger.getLogger(AdminMain.class.getName()).log(Level.SEVERE, null, ex);
                                    }                        
                            } catch (SQLException ex) {
                                Logger.getLogger(AdminMain.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                }
                );
                viewAndUpdatingMedData_TableResult.setEnabled(false);

            } catch (SQLException ex) {
                Logger.getLogger(PatientRegistration.class.getName()).log(Level.SEVERE, null, ex);
            }

        } catch (SQLException ex) {
            Logger.getLogger(AdminMain.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        newMed_tableOfQueries.addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent me) {
                    JTable table = (JTable) me.getSource();
                    Point p = me.getPoint();
                    int row = table.rowAtPoint(p);
                    String dateFrom = "", dateTo = "", text = "";
                    if (newMed_tableOfQueries.getValueAt(row, 1) != null) {
                        text = newMed_tableOfQueries.getValueAt(row, 1).toString();
                    }
                    if (newMed_tableOfQueries.getValueAt(row, 2) != null) {
                        dateFrom = newMed_tableOfQueries.getValueAt(row, 2).toString();
                    }
                    if (newMed_tableOfQueries.getValueAt(row, 3) != null) {
                        dateTo = newMed_tableOfQueries.getValueAt(row, 3).toString();
                    }
                    if (me.getClickCount() == 3) {
                        QueryOfMedicals query = new QueryOfMedicals();
                        query.getQuery(row + 1, text, dateFrom, dateTo);
                    }
                }
            });
    }
     // }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        adminMain_TabbedPane = new javax.swing.JTabbedPane();
        newMed_CommonPanel = new javax.swing.JPanel();
        NewMed_labelCategory = new javax.swing.JLabel();
        newMed_LabelBirthday = new javax.swing.JLabel();
        newMed_ComboBoxCategory = new javax.swing.JComboBox();
        newMed_LabelRecruitmentDate = new javax.swing.JLabel();
        newMed_LabelTelephone = new javax.swing.JLabel();
        newMed_FieldTelephone = new javax.swing.JTextField();
        newMed_ButtonSave = new javax.swing.JButton();
        newMed_DateChooserBDay = new com.toedter.calendar.JDateChooser();
        newMed_DateChooserJoinDay = new com.toedter.calendar.JDateChooser();
        newMed_ComboBoxPost = new javax.swing.JComboBox();
        newMed_LabelPost = new javax.swing.JLabel();
        newMed_LabelLogin = new javax.swing.JLabel();
        newMed_TextFieldLogin = new javax.swing.JTextField();
        newMed_LabelPassword = new javax.swing.JLabel();
        newMed_PasswordField = new javax.swing.JPasswordField();
        newMed_ScrollPaneDescribes = new javax.swing.JScrollPane();
        TextAreaDescribes = new javax.swing.JTextArea();
        newMed_LabelDescribes = new javax.swing.JLabel();
        newMed_PanelFullName = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        FieldSurname = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        FieldName = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        FieldMiddleName = new javax.swing.JTextField();
        newMed_LabelFillFields = new javax.swing.JLabel();
        newMed_ButtonAddPost = new javax.swing.JButton();
        newMed_ButtonAddCategory = new javax.swing.JButton();
        viewAndUpdatingMedData_CommonPanel = new javax.swing.JPanel();
        viewAndUpdatingMedData_ScrollPaneTableResult = new javax.swing.JScrollPane();
        viewAndUpdatingMedData_TableResult = new javax.swing.JTable();
        viewAndUpdatingMedData_ScrollPaneTableForFinding = new javax.swing.JScrollPane();
        ViewAndUpdatingMedData_TableForFinding = new javax.swing.JTable();
        viewAndUpdatingMedData_ButtonFindPatientByFields = new javax.swing.JButton();
        diagnosticGroups_CommonPanel = new javax.swing.JPanel();
        diagnosticGroups_ScrollPaneGroupList = new javax.swing.JScrollPane();
        DiagnosticGroups_ListOfDiagnosticGroups = new javax.swing.JList();
        diagnosticGroups_PanelGroupInfo = new javax.swing.JPanel();
        diagnosticGroups_label_InputGroupName = new javax.swing.JLabel();
        diagnosticGroups_TextFieldGroupName = new javax.swing.JTextField();
        diagnosticGroups_label_DescriptionOfNewGroup = new javax.swing.JLabel();
        diagnosticGroups_ScrollPaneDescriptionGroup = new javax.swing.JScrollPane();
        DiagnosticGroups_TextAreaGroupDescription = new javax.swing.JTextArea();
        diagnosticGroups_ButtonSaveGroup = new javax.swing.JButton();
        diagnosticGroups_ScrollPaneQuarter2 = new javax.swing.JScrollPane();
        tableSecondQuarter = new javax.swing.JTable();
        diagnosticGroups_ScrollPaneQuarter3 = new javax.swing.JScrollPane();
        tableThirdQuarter = new javax.swing.JTable();
        diagnosticGroups_ScrollPaneQuarter1 = new javax.swing.JScrollPane();
        tableFirstQuarter = new javax.swing.JTable();
        diagnosticGroups_ScrollPaneQuarter4 = new javax.swing.JScrollPane();
        tableFourthQuarter = new javax.swing.JTable();
        diagnosticGroups_ButtonAddNewGroup = new javax.swing.JButton();
        diagnosticGroups_ButtonHistoryOfChanges = new javax.swing.JButton();
        diagnosticGroups_labelMoreInfo = new javax.swing.JLabel();
        medQueries_CommonPanel = new javax.swing.JPanel();
        newMed_ScrollPaneTableOfQueries = new javax.swing.JScrollPane();
        newMed_tableOfQueries = new javax.swing.JTable();
        adminMain_ButtonExit = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setResizable(false);

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("spectrex/Bundle"); // NOI18N
        newMed_CommonPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("AdminMain.newMed_CommonPanel.border.title"))); // NOI18N

        NewMed_labelCategory.setText(bundle.getString("AdminMain.NewMed_labelCategory.text")); // NOI18N

        newMed_LabelBirthday.setText(bundle.getString("AdminMain.newMed_LabelBirthday.text")); // NOI18N

        newMed_ComboBoxCategory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newMed_ComboBoxCategoryActionPerformed(evt);
            }
        });

        newMed_LabelRecruitmentDate.setText(bundle.getString("AdminMain.newMed_LabelRecruitmentDate.text")); // NOI18N

        newMed_LabelTelephone.setText(bundle.getString("AdminMain.newMed_LabelTelephone.text")); // NOI18N

        newMed_ButtonSave.setText(bundle.getString("AdminMain.newMed_ButtonSave.text")); // NOI18N
        newMed_ButtonSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newMed_ButtonSaveActionPerformed(evt);
            }
        });

        newMed_LabelPost.setText(bundle.getString("AdminMain.newMed_LabelPost.text")); // NOI18N

        newMed_LabelLogin.setText(bundle.getString("AdminMain.newMed_LabelLogin.text")); // NOI18N

        newMed_LabelPassword.setText(bundle.getString("AdminMain.newMed_LabelPassword.text")); // NOI18N

        newMed_PasswordField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newMed_PasswordFieldActionPerformed(evt);
            }
        });

        TextAreaDescribes.setColumns(20);
        TextAreaDescribes.setRows(5);
        newMed_ScrollPaneDescribes.setViewportView(TextAreaDescribes);

        newMed_LabelDescribes.setText(bundle.getString("AdminMain.newMed_LabelDescribes.text")); // NOI18N

        newMed_PanelFullName.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("AdminMain.newMed_PanelFullName.border.title"))); // NOI18N

        jLabel1.setText(bundle.getString("AdminMain.jLabel1.text")); // NOI18N

        jLabel2.setText(bundle.getString("AdminMain.jLabel2.text")); // NOI18N

        FieldName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FieldNameActionPerformed(evt);
            }
        });

        jLabel3.setText(bundle.getString("AdminMain.jLabel3.text")); // NOI18N

        javax.swing.GroupLayout newMed_PanelFullNameLayout = new javax.swing.GroupLayout(newMed_PanelFullName);
        newMed_PanelFullName.setLayout(newMed_PanelFullNameLayout);
        newMed_PanelFullNameLayout.setHorizontalGroup(
            newMed_PanelFullNameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(newMed_PanelFullNameLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(newMed_PanelFullNameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 67, Short.MAX_VALUE)
                .addGroup(newMed_PanelFullNameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(FieldName, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 192, Short.MAX_VALUE)
                    .addComponent(FieldSurname, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(FieldMiddleName))
                .addContainerGap())
        );
        newMed_PanelFullNameLayout.setVerticalGroup(
            newMed_PanelFullNameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(newMed_PanelFullNameLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(newMed_PanelFullNameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(FieldSurname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(18, 18, 18)
                .addGroup(newMed_PanelFullNameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(FieldName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(18, 18, 18)
                .addGroup(newMed_PanelFullNameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(FieldMiddleName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        newMed_LabelFillFields.setText(bundle.getString("AdminMain.newMed_LabelFillFields.text")); // NOI18N

        newMed_ButtonAddPost.setText(bundle.getString("AdminMain.newMed_ButtonAddPost.text")); // NOI18N
        newMed_ButtonAddPost.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newMed_ButtonAddPostActionPerformed(evt);
            }
        });

        newMed_ButtonAddCategory.setText(bundle.getString("AdminMain.newMed_ButtonAddCategory.text")); // NOI18N
        newMed_ButtonAddCategory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newMed_ButtonAddCategoryActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout newMed_CommonPanelLayout = new javax.swing.GroupLayout(newMed_CommonPanel);
        newMed_CommonPanel.setLayout(newMed_CommonPanelLayout);
        newMed_CommonPanelLayout.setHorizontalGroup(
            newMed_CommonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(newMed_CommonPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(newMed_CommonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(newMed_CommonPanelLayout.createSequentialGroup()
                        .addComponent(newMed_LabelDescribes, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(newMed_CommonPanelLayout.createSequentialGroup()
                        .addGroup(newMed_CommonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(newMed_CommonPanelLayout.createSequentialGroup()
                                .addGroup(newMed_CommonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(newMed_LabelLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(newMed_LabelPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(newMed_LabelPost, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(NewMed_labelCategory, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(29, 29, 29)
                                .addGroup(newMed_CommonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(newMed_CommonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(newMed_DateChooserJoinDay, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(newMed_CommonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(newMed_PasswordField, javax.swing.GroupLayout.DEFAULT_SIZE, 148, Short.MAX_VALUE)
                                            .addComponent(newMed_TextFieldLogin)))
                                    .addComponent(newMed_ScrollPaneDescribes, javax.swing.GroupLayout.PREFERRED_SIZE, 420, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(newMed_CommonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, newMed_CommonPanelLayout.createSequentialGroup()
                                            .addComponent(newMed_ComboBoxPost, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(18, 18, 18)
                                            .addComponent(newMed_ButtonAddPost, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGroup(newMed_CommonPanelLayout.createSequentialGroup()
                                            .addComponent(newMed_ComboBoxCategory, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(18, 18, 18)
                                            .addComponent(newMed_ButtonAddCategory)))))
                            .addComponent(newMed_LabelRecruitmentDate, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(newMed_CommonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(newMed_PanelFullName, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, newMed_CommonPanelLayout.createSequentialGroup()
                                .addGroup(newMed_CommonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(newMed_LabelBirthday, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(newMed_LabelTelephone, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(23, 23, 23)
                                .addGroup(newMed_CommonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(newMed_FieldTelephone, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(newMed_DateChooserBDay, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(61, 61, 61))
                            .addComponent(newMed_ButtonSave, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(180, 180, 180))
                    .addGroup(newMed_CommonPanelLayout.createSequentialGroup()
                        .addComponent(newMed_LabelFillFields)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        newMed_CommonPanelLayout.setVerticalGroup(
            newMed_CommonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, newMed_CommonPanelLayout.createSequentialGroup()
                .addGroup(newMed_CommonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(newMed_CommonPanelLayout.createSequentialGroup()
                        .addGroup(newMed_CommonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(newMed_CommonPanelLayout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(newMed_PanelFullName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(newMed_CommonPanelLayout.createSequentialGroup()
                                .addGap(34, 34, 34)
                                .addGroup(newMed_CommonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(newMed_LabelLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(newMed_TextFieldLogin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(28, 28, 28)
                                .addGroup(newMed_CommonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(newMed_LabelPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(newMed_PasswordField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(newMed_CommonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(newMed_LabelRecruitmentDate, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(newMed_DateChooserJoinDay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGroup(newMed_CommonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(newMed_CommonPanelLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(newMed_CommonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(newMed_LabelBirthday)
                                    .addComponent(newMed_DateChooserBDay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(newMed_CommonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(newMed_FieldTelephone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(newMed_LabelTelephone))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, newMed_CommonPanelLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                                .addGroup(newMed_CommonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(newMed_ButtonAddPost)
                                    .addComponent(newMed_ComboBoxPost, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(newMed_LabelPost))
                                .addGap(18, 18, 18)
                                .addGroup(newMed_CommonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(newMed_ButtonAddCategory)
                                    .addComponent(newMed_ComboBoxCategory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(NewMed_labelCategory))
                                .addGap(30, 30, 30)))
                        .addGroup(newMed_CommonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(newMed_LabelDescribes)
                            .addComponent(newMed_ScrollPaneDescribes, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(newMed_CommonPanelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(newMed_ButtonSave)))
                .addGap(51, 51, 51)
                .addComponent(newMed_LabelFillFields)
                .addContainerGap(31, Short.MAX_VALUE))
        );

        adminMain_TabbedPane.addTab(bundle.getString("AdminMain.newMed_CommonPanel.TabConstraints.tabTitle"), newMed_CommonPanel); // NOI18N

        viewAndUpdatingMedData_CommonPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        viewAndUpdatingMedData_TableResult.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        viewAndUpdatingMedData_ScrollPaneTableResult.setViewportView(viewAndUpdatingMedData_TableResult);

        ViewAndUpdatingMedData_TableForFinding.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, "", "", "", null, null, null, null, null}
            },
            new String [] {
                "ID сотрудника", "Логин", "ФИО", "Дата рождения", "Работает с", "Телефон", "О сотруднике", "Должность", "Категория"
            }
        ));
        ViewAndUpdatingMedData_TableForFinding.setCellSelectionEnabled(true);
        ViewAndUpdatingMedData_TableForFinding.setSelectionBackground(new java.awt.Color(102, 255, 255));
        ViewAndUpdatingMedData_TableForFinding.setSurrendersFocusOnKeystroke(true);
        viewAndUpdatingMedData_ScrollPaneTableForFinding.setViewportView(ViewAndUpdatingMedData_TableForFinding);
        ViewAndUpdatingMedData_TableForFinding.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        if (ViewAndUpdatingMedData_TableForFinding.getColumnModel().getColumnCount() > 0) {
            ViewAndUpdatingMedData_TableForFinding.getColumnModel().getColumn(0).setHeaderValue(bundle.getString("AdminMain.ViewAndUpdatingMedData_TableForFinding.columnModel.title0")); // NOI18N
            ViewAndUpdatingMedData_TableForFinding.getColumnModel().getColumn(1).setHeaderValue(bundle.getString("AdminMain.ViewAndUpdatingMedData_TableForFinding.columnModel.title1")); // NOI18N
            ViewAndUpdatingMedData_TableForFinding.getColumnModel().getColumn(2).setHeaderValue(bundle.getString("AdminMain.ViewAndUpdatingMedData_TableForFinding.columnModel.title2")); // NOI18N
            ViewAndUpdatingMedData_TableForFinding.getColumnModel().getColumn(3).setHeaderValue(bundle.getString("AdminMain.ViewAndUpdatingMedData_TableForFinding.columnModel.title3")); // NOI18N
            ViewAndUpdatingMedData_TableForFinding.getColumnModel().getColumn(4).setHeaderValue(bundle.getString("AdminMain.TableForFinding.columnModel.title5")); // NOI18N
            ViewAndUpdatingMedData_TableForFinding.getColumnModel().getColumn(5).setHeaderValue(bundle.getString("AdminMain.TableForFinding.columnModel.title4")); // NOI18N
            ViewAndUpdatingMedData_TableForFinding.getColumnModel().getColumn(6).setHeaderValue(bundle.getString("AdminMain.ViewAndUpdatingMedData_TableForFinding.columnModel.title6")); // NOI18N
            ViewAndUpdatingMedData_TableForFinding.getColumnModel().getColumn(7).setHeaderValue(bundle.getString("AdminMain.ViewAndUpdatingMedData_TableForFinding.columnModel.title7")); // NOI18N
            ViewAndUpdatingMedData_TableForFinding.getColumnModel().getColumn(8).setHeaderValue(bundle.getString("AdminMain.ViewAndUpdatingMedData_TableForFinding.columnModel.title8")); // NOI18N
        }

        viewAndUpdatingMedData_ButtonFindPatientByFields.setText(bundle.getString("AdminMain.viewAndUpdatingMedData_ButtonFindPatientByFields.text")); // NOI18N
        viewAndUpdatingMedData_ButtonFindPatientByFields.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewAndUpdatingMedData_ButtonFindPatientByFieldsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout viewAndUpdatingMedData_CommonPanelLayout = new javax.swing.GroupLayout(viewAndUpdatingMedData_CommonPanel);
        viewAndUpdatingMedData_CommonPanel.setLayout(viewAndUpdatingMedData_CommonPanelLayout);
        viewAndUpdatingMedData_CommonPanelLayout.setHorizontalGroup(
            viewAndUpdatingMedData_CommonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(viewAndUpdatingMedData_CommonPanelLayout.createSequentialGroup()
                .addGroup(viewAndUpdatingMedData_CommonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(viewAndUpdatingMedData_ScrollPaneTableResult, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(viewAndUpdatingMedData_ScrollPaneTableForFinding, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, viewAndUpdatingMedData_CommonPanelLayout.createSequentialGroup()
                        .addGap(0, 838, Short.MAX_VALUE)
                        .addComponent(viewAndUpdatingMedData_ButtonFindPatientByFields, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        viewAndUpdatingMedData_CommonPanelLayout.setVerticalGroup(
            viewAndUpdatingMedData_CommonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(viewAndUpdatingMedData_CommonPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(viewAndUpdatingMedData_ScrollPaneTableResult, javax.swing.GroupLayout.DEFAULT_SIZE, 329, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(viewAndUpdatingMedData_ScrollPaneTableForFinding, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(viewAndUpdatingMedData_ButtonFindPatientByFields)
                .addGap(20, 20, 20))
        );

        adminMain_TabbedPane.addTab(bundle.getString("AdminMain.viewAndUpdatingMedData_CommonPanel.TabConstraints.tabTitle"), viewAndUpdatingMedData_CommonPanel); // NOI18N

        diagnosticGroups_ScrollPaneGroupList.setViewportView(DiagnosticGroups_ListOfDiagnosticGroups);

        diagnosticGroups_PanelGroupInfo.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("AdminMain.diagnosticGroups_PanelGroupInfo.border.title"))); // NOI18N

        diagnosticGroups_label_InputGroupName.setText(bundle.getString("AdminMain.diagnosticGroups_label_InputGroupName.text")); // NOI18N

        diagnosticGroups_label_DescriptionOfNewGroup.setText(bundle.getString("AdminMain.diagnosticGroups_label_DescriptionOfNewGroup.text")); // NOI18N

        DiagnosticGroups_TextAreaGroupDescription.setColumns(20);
        DiagnosticGroups_TextAreaGroupDescription.setRows(5);
        diagnosticGroups_ScrollPaneDescriptionGroup.setViewportView(DiagnosticGroups_TextAreaGroupDescription);

        diagnosticGroups_ButtonSaveGroup.setText(bundle.getString("AdminMain.diagnosticGroups_ButtonSaveGroup.text")); // NOI18N
        diagnosticGroups_ButtonSaveGroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                diagnosticGroups_ButtonSaveGroupActionPerformed(evt);
            }
        });

        tableSecondQuarter.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"20", null},
                {"26", null},
                {"36", null},
                {"40", null},
                {"65", null},
                {"85", null},
                {"120", null},
                {"150", null}
            },
            new String [] {
                "nm", "value"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        diagnosticGroups_ScrollPaneQuarter2.setViewportView(tableSecondQuarter);
        if (tableSecondQuarter.getColumnModel().getColumnCount() > 0) {
            tableSecondQuarter.getColumnModel().getColumn(0).setResizable(false);
            tableSecondQuarter.getColumnModel().getColumn(0).setHeaderValue(bundle.getString("AdminMain.tableSecondQuarter.columnModel.title0")); // NOI18N
            tableSecondQuarter.getColumnModel().getColumn(1).setResizable(false);
            tableSecondQuarter.getColumnModel().getColumn(1).setHeaderValue(bundle.getString("AdminMain.tableSecondQuarter.columnModel.title1")); // NOI18N
        }

        tableThirdQuarter.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"210", null},
                {"290", null},
                {"300", null},
                {"520", null},
                {"700", null},
                {"950", null},
                {"1300", null},
                {"1700", null}
            },
            new String [] {
                "nm", "value"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        diagnosticGroups_ScrollPaneQuarter3.setViewportView(tableThirdQuarter);
        if (tableThirdQuarter.getColumnModel().getColumnCount() > 0) {
            tableThirdQuarter.getColumnModel().getColumn(0).setResizable(false);
            tableThirdQuarter.getColumnModel().getColumn(0).setHeaderValue(bundle.getString("AdminMain.tableSecondQuarter.columnModel.title0")); // NOI18N
            tableThirdQuarter.getColumnModel().getColumn(1).setResizable(false);
            tableThirdQuarter.getColumnModel().getColumn(1).setHeaderValue(bundle.getString("AdminMain.tableSecondQuarter.columnModel.title1")); // NOI18N
        }

        tableFirstQuarter.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"2", null},
                {"3", null},
                {"4", null},
                {"5", null},
                {"6", null},
                {"8", null},
                {"11", null},
                {"15", null}
            },
            new String [] {
                "nm", "%"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        diagnosticGroups_ScrollPaneQuarter1.setViewportView(tableFirstQuarter);
        if (tableFirstQuarter.getColumnModel().getColumnCount() > 0) {
            tableFirstQuarter.getColumnModel().getColumn(0).setResizable(false);
            tableFirstQuarter.getColumnModel().getColumn(0).setHeaderValue(bundle.getString("AdminMain.tableSecondQuarter.columnModel.title0")); // NOI18N
            tableFirstQuarter.getColumnModel().getColumn(1).setResizable(false);
            tableFirstQuarter.getColumnModel().getColumn(1).setHeaderValue(bundle.getString("AdminMain.tableSecondQuarter.columnModel.title1")); // NOI18N
        }

        tableFourthQuarter.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"2300", null},
                {"3100", null},
                {"4200", null},
                {"5600", null},
                {"7600", null},
                {"10200", null},
                {"13800", null},
                {"18500", null}
            },
            new String [] {
                "nm", "value"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        diagnosticGroups_ScrollPaneQuarter4.setViewportView(tableFourthQuarter);
        if (tableFourthQuarter.getColumnModel().getColumnCount() > 0) {
            tableFourthQuarter.getColumnModel().getColumn(0).setResizable(false);
            tableFourthQuarter.getColumnModel().getColumn(0).setHeaderValue(bundle.getString("AdminMain.tableSecondQuarter.columnModel.title0")); // NOI18N
            tableFourthQuarter.getColumnModel().getColumn(1).setResizable(false);
            tableFourthQuarter.getColumnModel().getColumn(1).setHeaderValue(bundle.getString("AdminMain.tableSecondQuarter.columnModel.title1")); // NOI18N
        }

        diagnosticGroups_ButtonAddNewGroup.setText(bundle.getString("AdminMain.diagnosticGroups_ButtonAddNewGroup.text")); // NOI18N
        diagnosticGroups_ButtonAddNewGroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                diagnosticGroups_ButtonAddNewGroupActionPerformed(evt);
            }
        });

        diagnosticGroups_ButtonHistoryOfChanges.setText(bundle.getString("AdminMain.diagnosticGroups_ButtonHistoryOfChanges.text")); // NOI18N
        diagnosticGroups_ButtonHistoryOfChanges.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                diagnosticGroups_ButtonHistoryOfChangesActionPerformed(evt);
            }
        });

        diagnosticGroups_labelMoreInfo.setText(bundle.getString("AdminMain.diagnosticGroups_labelMoreInfo.text")); // NOI18N

        javax.swing.GroupLayout diagnosticGroups_PanelGroupInfoLayout = new javax.swing.GroupLayout(diagnosticGroups_PanelGroupInfo);
        diagnosticGroups_PanelGroupInfo.setLayout(diagnosticGroups_PanelGroupInfoLayout);
        diagnosticGroups_PanelGroupInfoLayout.setHorizontalGroup(
            diagnosticGroups_PanelGroupInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(diagnosticGroups_PanelGroupInfoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(diagnosticGroups_PanelGroupInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(diagnosticGroups_label_InputGroupName)
                    .addComponent(diagnosticGroups_label_DescriptionOfNewGroup, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(28, 28, 28)
                .addGroup(diagnosticGroups_PanelGroupInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(diagnosticGroups_PanelGroupInfoLayout.createSequentialGroup()
                        .addComponent(diagnosticGroups_ButtonSaveGroup, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(diagnosticGroups_ButtonHistoryOfChanges))
                    .addComponent(diagnosticGroups_ScrollPaneDescriptionGroup)
                    .addComponent(diagnosticGroups_TextFieldGroupName))
                .addGap(77, 77, 77)
                .addGroup(diagnosticGroups_PanelGroupInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(diagnosticGroups_labelMoreInfo)
                    .addGroup(diagnosticGroups_PanelGroupInfoLayout.createSequentialGroup()
                        .addComponent(diagnosticGroups_ScrollPaneQuarter1, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(diagnosticGroups_ScrollPaneQuarter2, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(diagnosticGroups_ScrollPaneQuarter3, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(diagnosticGroups_ScrollPaneQuarter4, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(diagnosticGroups_ButtonAddNewGroup))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        diagnosticGroups_PanelGroupInfoLayout.setVerticalGroup(
            diagnosticGroups_PanelGroupInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(diagnosticGroups_PanelGroupInfoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(diagnosticGroups_PanelGroupInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(diagnosticGroups_PanelGroupInfoLayout.createSequentialGroup()
                        .addGroup(diagnosticGroups_PanelGroupInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(diagnosticGroups_label_InputGroupName)
                            .addComponent(diagnosticGroups_TextFieldGroupName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(30, 30, 30)
                        .addGroup(diagnosticGroups_PanelGroupInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(diagnosticGroups_label_DescriptionOfNewGroup)
                            .addComponent(diagnosticGroups_ScrollPaneDescriptionGroup)))
                    .addComponent(diagnosticGroups_ScrollPaneQuarter4, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(diagnosticGroups_ScrollPaneQuarter3, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(diagnosticGroups_ScrollPaneQuarter2, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(diagnosticGroups_ScrollPaneQuarter1, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(diagnosticGroups_PanelGroupInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(diagnosticGroups_PanelGroupInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(diagnosticGroups_ButtonSaveGroup)
                        .addComponent(diagnosticGroups_ButtonHistoryOfChanges))
                    .addComponent(diagnosticGroups_ButtonAddNewGroup))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
                .addComponent(diagnosticGroups_labelMoreInfo)
                .addContainerGap())
        );

        javax.swing.GroupLayout diagnosticGroups_CommonPanelLayout = new javax.swing.GroupLayout(diagnosticGroups_CommonPanel);
        diagnosticGroups_CommonPanel.setLayout(diagnosticGroups_CommonPanelLayout);
        diagnosticGroups_CommonPanelLayout.setHorizontalGroup(
            diagnosticGroups_CommonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(diagnosticGroups_CommonPanelLayout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(diagnosticGroups_CommonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(diagnosticGroups_CommonPanelLayout.createSequentialGroup()
                        .addComponent(diagnosticGroups_ScrollPaneGroupList, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 789, Short.MAX_VALUE))
                    .addComponent(diagnosticGroups_PanelGroupInfo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        diagnosticGroups_CommonPanelLayout.setVerticalGroup(
            diagnosticGroups_CommonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(diagnosticGroups_CommonPanelLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(diagnosticGroups_ScrollPaneGroupList, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(diagnosticGroups_PanelGroupInfo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        adminMain_TabbedPane.addTab(bundle.getString("AdminMain.diagnosticGroups_CommonPanel.TabConstraints.tabTitle"), diagnosticGroups_CommonPanel); // NOI18N

        newMed_tableOfQueries.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"Сколько врачей имеют стаж больше заданного?", null, null, null},
                {"Кто из врачей не проводил диагностику в этом месяце?", null, null, null},
                {"Среди всех категорий определить медработника, который диагностирует максимальное количество пациентов.", null, null, null}
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
        newMed_ScrollPaneTableOfQueries.setViewportView(newMed_tableOfQueries);
        if (newMed_tableOfQueries.getColumnModel().getColumnCount() > 0) {
            newMed_tableOfQueries.getColumnModel().getColumn(0).setHeaderValue(bundle.getString("AdminMain.newMed_tableOfQueries.columnModel.title0")); // NOI18N
            newMed_tableOfQueries.getColumnModel().getColumn(1).setHeaderValue(bundle.getString("AdminMain.newMed_tableOfQueries.columnModel.title1")); // NOI18N
            newMed_tableOfQueries.getColumnModel().getColumn(2).setHeaderValue(bundle.getString("AdminMain.newMed_tableOfQueries.columnModel.title2")); // NOI18N
            newMed_tableOfQueries.getColumnModel().getColumn(3).setHeaderValue(bundle.getString("AdminMain.newMed_tableOfQueries.columnModel.title3")); // NOI18N
        }

        javax.swing.GroupLayout medQueries_CommonPanelLayout = new javax.swing.GroupLayout(medQueries_CommonPanel);
        medQueries_CommonPanel.setLayout(medQueries_CommonPanelLayout);
        medQueries_CommonPanelLayout.setHorizontalGroup(
            medQueries_CommonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(medQueries_CommonPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(newMed_ScrollPaneTableOfQueries, javax.swing.GroupLayout.DEFAULT_SIZE, 932, Short.MAX_VALUE)
                .addContainerGap())
        );
        medQueries_CommonPanelLayout.setVerticalGroup(
            medQueries_CommonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(medQueries_CommonPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(newMed_ScrollPaneTableOfQueries, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(29, Short.MAX_VALUE))
        );

        adminMain_TabbedPane.addTab(bundle.getString("AdminMain.medQueries_CommonPanel.TabConstraints.tabTitle"), medQueries_CommonPanel); // NOI18N

        adminMain_ButtonExit.setText(bundle.getString("AdminMain.adminMain_ButtonExit.text")); // NOI18N
        adminMain_ButtonExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                adminMain_ButtonExitActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(adminMain_ButtonExit, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25))
            .addGroup(layout.createSequentialGroup()
                .addComponent(adminMain_TabbedPane, javax.swing.GroupLayout.PREFERRED_SIZE, 957, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 20, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(adminMain_TabbedPane, javax.swing.GroupLayout.PREFERRED_SIZE, 495, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22)
                .addComponent(adminMain_ButtonExit)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void FieldNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FieldNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_FieldNameActionPerformed

    private void newMed_ComboBoxCategoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newMed_ComboBoxCategoryActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_newMed_ComboBoxCategoryActionPerformed

    private void newMed_ButtonSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newMed_ButtonSaveActionPerformed
        try {
            Statement s = cn.createStatement();
            String fio, tel, bdaystr = "", joindatestr = "", login, password, descr = "";
            String Sql = "";
            int post = 0, tt = 0, flag = 0, cat = 0;
            Date bday, joindate;

            fio = FieldSurname.getText() + " " + FieldName.getText() + " " + FieldMiddleName.getText();
            tel = newMed_FieldTelephone.getText();
            login = newMed_TextFieldLogin.getText();
            descr = TextAreaDescribes.getText();
            password = new String(newMed_PasswordField.getPassword());
            System.out.println(password + "\n\n");
            ResultSet r = s.executeQuery("select IdHS from hospitalstaff where Login = '" + newMed_TextFieldLogin.getText() + "';");

            if (newMed_TextFieldLogin.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Введите логин");
                flag = 1;
            } else if (r.first()) {
                //JOptionPane.showMessageDialog(null, "Input another medcard number");
                JOptionPane.showMessageDialog(null, "Такой логин уже существует");
                flag = 1;
            } else if (newMed_PasswordField.getPassword().toString().equals("")) {
                JOptionPane.showMessageDialog(null, "Введите пароль");
                flag = 1;
            } else if (FieldSurname.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Введите фамилию");
                flag = 1;
            } else if (FieldName.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Введитя имя");
                flag = 1;
            } else if (FieldMiddleName.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Введите отчество");
                flag = 1;
            } else if (newMed_DateChooserBDay.getDate() == null) {
                JOptionPane.showMessageDialog(null, "Введите дату рождения");
                flag = 1;
            } else if (newMed_DateChooserJoinDay.getDate() == null) {
                JOptionPane.showMessageDialog(null, "Введите дату принятия на работу");
                flag = 1;
            }

            if (!checkWithRegExpTel(tel)) {
                JOptionPane.showMessageDialog(null, "Номер телефона в формате +38(000)1234567");
            } else if (flag == 0) {    //JOptionPane.showMessageDialog(null, "OK");

                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                bdaystr = format.format(newMed_DateChooserBDay.getDate());
                joindatestr = format.format(newMed_DateChooserJoinDay.getDate());
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

                ResultSet rs2 = s.executeQuery("select IdPost from post where PostName = '" + newMed_ComboBoxPost.getSelectedItem().toString() + "'");
                while (rs2.next()) {
                    post = rs2.getInt("IdPost");
                }

                ResultSet rs0 = s.executeQuery("select IdCat from category where CategoryName = '" + newMed_ComboBoxCategory.getSelectedItem().toString() + "'");
                while (rs0.next()) {
                    cat = rs0.getInt("IdCat");
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

                    if (n > 0) {
                        JOptionPane.showMessageDialog(null, "Сотрудник зарегистрирован");
                        ResultSet rs3 = s.executeQuery("select IdHS from hospitalstaff where Login = '" + login + "'");
                        if (rs3.next()) {
                        }
                        idhs = Integer.parseInt(rs3.getString("IdHS"));
                    }

                } catch (NullPointerException e) {
                    JOptionPane.showMessageDialog(null, "Input date");
                    flag = 1;
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, ex);
                }
                newMed_TextFieldLogin.setText("");
                newMed_PasswordField.setText("");
                FieldSurname.setText("");
                FieldName.setText("");
                FieldMiddleName.setText("");
                newMed_FieldTelephone.setText("");
                TextAreaDescribes.setText("");
            }
        } catch (SQLException ex) {
            Logger.getLogger(PatientRegistration.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            Statement sta = cn.createStatement();
            ResultSet rs;
            rs = sta.executeQuery("select IdHS as \"ID сотрудника\", Login as \"Логин\", HSFIO as \"ФИО\", B_day as \"Дата рождения\","
                    + " Date_to_join as \"Работает с\", Tel as \"Телефон\", Describes as \"О сотруднике\", PostName as \"Должность\", CategoryName as \"Категория\""
                    + " from (hospitalstaff h join post p on h.IdPost = p.IdPost) join category c on h.IdCat = c.IdCat");
            viewAndUpdatingMedData_TableResult.setModel(resultSetToTableModel(rs));
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
            sorter.setModel(viewAndUpdatingMedData_TableResult.getModel());
            sorter.setComparator(0, myComparator);
            sorter.toggleSortOrder(2); //sort column HSFIO
            viewAndUpdatingMedData_TableResult.setRowSorter(sorter);
            viewAndUpdatingMedData_TableResult.addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent me) {
                    JTable table = (JTable) me.getSource();
                    Point p = me.getPoint();
                    int row = table.rowAtPoint(p);
                    if (me.getClickCount() == 2) {
                        // JOptionPane.showMessageDialog(null, "Выбрана строка " + row);
                        String idhs = table.getValueAt(row, 0).toString();
                        new MedRegistration(idhs).setVisible(true);
                    }
                }
            });
            viewAndUpdatingMedData_TableResult.setEnabled(false);
        } catch (SQLException ex) {
            Logger.getLogger(AdminMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_newMed_ButtonSaveActionPerformed

    private void adminMain_ButtonExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_adminMain_ButtonExitActionPerformed
        this.dispose();
        new MedLogin().setVisible(true);
    }//GEN-LAST:event_adminMain_ButtonExitActionPerformed

    private void newMed_PasswordFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newMed_PasswordFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_newMed_PasswordFieldActionPerformed

    private void viewAndUpdatingMedData_ButtonFindPatientByFieldsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewAndUpdatingMedData_ButtonFindPatientByFieldsActionPerformed
        ResultSet rs = null;
        String nameOfColumsSQL[] = {"IdHS", "Login", "HSFIO", "B_day", "Date_to_join", "Tel", "Describes", "PostName", "CategoryName"};
        try {
            Statement sta = cn.createStatement();
            String query;

            query = "select IdHS as \"ID сотрудника\", Login as \"Логин\", HSFIO as \"ФИО\", B_day as \"Дата рождения\","
                    + " Date_to_join as \"Работает с\", Tel as \"Телефон\", Describes as \"О сотруднике\", PostName as \"Должность\", CategoryName as \"Категория\""
                    + " from (hospitalstaff h join post p on h.IdPost = p.IdPost) join category c on h.IdCat = c.IdCat ";
            for (int i = 0; i < ViewAndUpdatingMedData_TableForFinding.getColumnCount(); i++) {
                if (ViewAndUpdatingMedData_TableForFinding.getValueAt(0, i) != null) {
                    query += " where " + nameOfColumsSQL[i] + " like '%" + ViewAndUpdatingMedData_TableForFinding.getValueAt(0, i).toString() + "%' ";
                    break;
                }
            }
            for (int i = 0; i < ViewAndUpdatingMedData_TableForFinding.getColumnCount(); i++) {
                if (ViewAndUpdatingMedData_TableForFinding.getValueAt(0, i) != null) {
                    query += "and " + nameOfColumsSQL[i] + " like '%" + ViewAndUpdatingMedData_TableForFinding.getValueAt(0, i).toString() + "%' ";
                }
            }

            rs = sta.executeQuery(query);
            viewAndUpdatingMedData_TableResult.setModel(resultSetToTableModel(rs));
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
            sorter.setModel(viewAndUpdatingMedData_TableResult.getModel());
            sorter.setComparator(0, myComparator);
            sorter.toggleSortOrder(2); //sort column HSFIO
            viewAndUpdatingMedData_TableResult.setRowSorter(sorter);
            viewAndUpdatingMedData_TableResult.setEnabled(false);

        } catch (SQLException ex) {
            Logger.getLogger(PatientRegistration.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_viewAndUpdatingMedData_ButtonFindPatientByFieldsActionPerformed

    private void newMed_ButtonAddPostActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newMed_ButtonAddPostActionPerformed
        new AddPost().setVisible(true);
        //ButtonAddCategory.set
    }//GEN-LAST:event_newMed_ButtonAddPostActionPerformed

    private void newMed_ButtonAddCategoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newMed_ButtonAddCategoryActionPerformed
        new AddCategory().setVisible(true);
    }//GEN-LAST:event_newMed_ButtonAddCategoryActionPerformed

    private void diagnosticGroups_ButtonSaveGroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_diagnosticGroups_ButtonSaveGroupActionPerformed
        try {
            UIManager.put("OptionPane.yesButtonText", "Да");
            UIManager.put("OptionPane.noButtonText", "Нет");
            int response = JOptionPane.showConfirmDialog(null, "Вы точно хотите изменить данные о группе?"
                    + " (Обследования, которые были проведены ранее, пересчитываться не будут)", "Подтверждение",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (response == JOptionPane.YES_OPTION) {
                try {
                    
                    float D[] = new float[32];
                    Statement s = cn.createStatement();
                    ResultSet rs;
                    int idkv = 0;
                    
                    String nameOfGroup = DiagnosticGroups_ListOfDiagnosticGroups.getSelectedValue().toString();
                    rs = s.executeQuery("select IdKV from diagnosticgroup where GroupName = '" + nameOfGroup + "';");
                    if(rs.next())
                        idkv = rs.getInt(1);
                    PreparedStatement sta = cn.prepareStatement("update diagnosticgroup d set "
                            + " d.Description = '"
                            + DiagnosticGroups_TextAreaGroupDescription.getText() + "', d.IdKV = '" + idkv + "' where d.GroupName= '" + diagnosticGroups_TextFieldGroupName.getText() + "';");
                    int n = sta.executeUpdate();
                    if (n <= 0) {
                        JOptionPane.showMessageDialog(null, "Ошибка обновления");
                    }
                    for (int i = 0; i < 8; i++) {
                        D[i] = Float.parseFloat(tableFirstQuarter.getValueAt(i, 1).toString());
                    }
                    for (int i = 8; i < 16; i++) {
                        D[i] = Float.parseFloat(tableSecondQuarter.getValueAt(i - 8, 1).toString());
                    }
                    for (int i = 16; i < 24; i++) {
                        D[i] = Float.parseFloat(tableThirdQuarter.getValueAt(i - 16, 1).toString());
                    }
                    for (int i = 24; i < 32; i++) {
                        D[i] = Float.parseFloat(tableFourthQuarter.getValueAt(i - 24, 1).toString());
                    }
                    PreparedStatement sta1 = cn.prepareStatement("update kvvvfloat set D2 = " + D[0] + ", D3 = " + D[1]
                            + ", D4 = " + D[2]
                            + ",  D5 = " + D[3]
                            + ",  D6 = " + D[4]
                            + ",  D8 = " + D[5]
                            + ",  D11 = " + D[6]
                            + ",  D15 = " + D[7]
                            + ",  D20 = " + D[8]
                            + ",  D26 = " + D[9]
                            + ",  D36 = " + D[10]
                            + ",  D40 = " + D[11]
                            + ",   D65 = " + D[12]
                            + ",   D85 = " + D[13]
                            + ",   D120 = " + D[14]
                            + ",   D150 = " + D[15]
                            + ",   D210 = " + D[16]
                            + ",   D290 = " + D[17]
                            + ",   D300 = " + D[18]
                            + ",   D520 = " + D[19]
                            + ",   D700 = " + D[20]
                            + ",   D950 = " + D[21]
                            + ",   D1300 = " + D[22]
                            + ",   D1700 = " + D[23]
                            + ",   D2300 = " + D[24]
                            + ",   D3100 = " + D[25]
                            + ",   D4200 = " + D[26]
                            + ",   D5600 = " + D[27]
                            + ",   D7600 = " + D[28]
                            + ",   D10200 = " + D[29]
                            + ",   D13800 = " + D[30]
                            + ",   D18500 = " + D[31] + " where IdKV = " + DiagnosticGroups_ListOfDiagnosticGroups.getSelectedIndex() + 1);
                    int n1 = sta1.executeUpdate();
                    if (n1 <= 0) {
                        JOptionPane.showMessageDialog(null, "Ошибка обновления");
                    } else {
                        rs = s.executeQuery("select IdDG from diagnosticgroup where GroupName = '" + nameOfGroup + "';");
                        if(rs.next())
                            logQuery(rs.getInt(1));
                        JOptionPane.showMessageDialog(null, "Данные о диагностической группе успешно обновлены");
                    }
                } catch (NullPointerException e) {
                    JOptionPane.showMessageDialog(null, "ошибка подключения");
                    
                } catch (SQLException ex) {
                    Logger.getLogger(MedRegistration.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            DefaultListModel listModel = new DefaultListModel();
            Statement s = cn.createStatement();
            ResultSet rs2 = s.executeQuery("SELECT GroupName FROM diagnosticgroup");
            listModel.clear();
            while (rs2.next()) {
                listModel.addElement(rs2.getString("GroupName"));
            }
            DiagnosticGroups_ListOfDiagnosticGroups.setModel(listModel);
        } catch (SQLException ex) {
            Logger.getLogger(AdminMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_diagnosticGroups_ButtonSaveGroupActionPerformed

    private void diagnosticGroups_ButtonAddNewGroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_diagnosticGroups_ButtonAddNewGroupActionPerformed
        try {
            UIManager.put("OptionPane.yesButtonText", "Да");
            UIManager.put("OptionPane.noButtonText", "Нет");
            int response = JOptionPane.showConfirmDialog(null, "Вы точно хотите добавить новую группу?", "Подтверждение",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (response == JOptionPane.YES_OPTION) {
                try {
                    Statement s = cn.createStatement();
                    ResultSet rs;
                    rs = s.executeQuery("select IdDG from diagnosticgroup where GroupName = '" + diagnosticGroups_TextFieldGroupName.getText() + "';");
                    if(rs.next())
                    {
                        JOptionPane.showMessageDialog(null, "Такая группа уже существует, измените название");
                    }
                    else
                    {
                        float D[] = new float[32];
                        for (int i = 0; i < 8; i++) {
                            D[i] = Float.parseFloat(tableFirstQuarter.getValueAt(i, 1).toString());
                        }
                        for (int i = 8; i < 16; i++) {
                            D[i] = Float.parseFloat(tableSecondQuarter.getValueAt(i - 8, 1).toString());
                        }
                        for (int i = 16; i < 24; i++) {
                            D[i] = Float.parseFloat(tableThirdQuarter.getValueAt(i - 16, 1).toString());
                        }
                        for (int i = 24; i < 32; i++) {
                            D[i] = Float.parseFloat(tableFourthQuarter.getValueAt(i - 24, 1).toString());
                        }
                        
                        String Sql = "insert into kvvvfloat (D2, D3, D4, D5, D6, D8, D11, D15, "
                                + "D20, D26, D36, D40, D65, D85, D120, D150, "
                                + "D210, D290, D300, D520, D700, D950, D1300, D1700, "
                                + "D2300, D3100, D4200, D5600, D7600, D10200, D13800, D18500) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,"
                                + " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                        try {
                            PreparedStatement ps = cn.prepareStatement(Sql);
                            
                            for (int i = 0; i < 32; i++) {
                                ps.setFloat(i + 1, D[i]);
                            }
                            
                            int n = ps.executeUpdate();
                            
                            if (n <= 0) {
                                JOptionPane.showMessageDialog(null, "Ошибка сохранения");
                            }
                            
                        } catch (SQLException ex) {
                            JOptionPane.showMessageDialog(null, ex);
                        }
                        
                        String groupName, description;
                        int idkv = 0;
                        groupName = diagnosticGroups_TextFieldGroupName.getText();
                        description = DiagnosticGroups_TextAreaGroupDescription.getText();
                        
                        rs = s.executeQuery("select IdKV from kvvvfloat where " +
                                "D2 = " + D[0] +
                                " and D3 = " + D[1] +
                                " and D4 = " + D[2] +
                                " and D5 = " + D[3] +
                                " and D6 = " + D[4] +
                                " and D8 = " + D[5] +
                                " and D11 = " + D[6] +
                                " and D15 = " + D[7] +
                                " and D20 = " + D[8] +
                                " and D26 = " + D[9] +
                                " and D36 = " + D[10] +
                                " and D40 = " + D[11] +
                                " and D65 = " + D[12] +
                                " and D85 = " + D[13] +
                                " and D120 = " + D[14] +
                                " and D150 = " + D[15] +
                                " and D210 = " + D[16] +
                                " and D290 = " + D[17] +
                                " and D300 = " + D[18] +
                                " and D520 = " + D[19] +
                                " and D700 = " + D[20] +
                                " and D950 = " + D[21] +
                                " and D1300 = " + D[22] +
                                " and D1700 = " + D[23] +
                                " and D2300 = " + D[24] +
                                " and D3100 = " + D[25] +
                                " and D4200 = " + D[26] +
                                " and D5600 = " + D[27] +
                                " and D7600 = " + D[28] +
                                " and D10200 = " + D[29] +
                                " and D13800 = " + D[30] +
                                " and D18500 = " + D[31] + ";");
                        if(!rs.first())
                            JOptionPane.showMessageDialog(null, "Неверные КВВВ-данные");
                        else {
                            idkv = rs.getInt("IdKV");
                        }
                        
                        Sql = "insert into diagnosticgroup (GroupName, Description, IdKV) values "
                                + "(?, ?, ?)";
                        try {
                            PreparedStatement ps = cn.prepareStatement(Sql);
                            ps.setString(1, groupName);
                            ps.setString(2, description);
                            ps.setInt(3, idkv);
                            
                            int n = ps.executeUpdate();
                            
                            if (n > 0) {
                                rs = s.executeQuery("select IdDG from diagnosticgroup where GroupName = '" + diagnosticGroups_TextFieldGroupName.getText()+ "';");
                                if (rs.next()) {
                                    logQuery(rs.getInt(1));
                                }
                                JOptionPane.showMessageDialog(null, "Данные успешно сохранены");
                            }

                        } catch (SQLException ex) {
                            JOptionPane.showMessageDialog(null, ex);
                        }
                    }   } catch (SQLException ex) {
                        Logger.getLogger(AdminMain.class.getName()).log(Level.SEVERE, null, ex);
                    }
            }
            DefaultListModel listModel = new DefaultListModel();
            Statement s = cn.createStatement();
            ResultSet rs2 = s.executeQuery("SELECT GroupName FROM diagnosticgroup");
            listModel.clear();
            while (rs2.next()) {
                listModel.addElement(rs2.getString("GroupName"));
            }
            DiagnosticGroups_ListOfDiagnosticGroups.setModel(listModel);
        } catch (SQLException ex) {
            Logger.getLogger(AdminMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_diagnosticGroups_ButtonAddNewGroupActionPerformed

   private void logQuery(int iddg)
    {                     
        try {
            Statement s = cn.createStatement();
            java.util.Date now = new java.util.Date();
            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat format2 = new SimpleDateFormat("HH:mm:ss");
            String d1 = format1.format(now);
            String t1 = format2.format(now);

            String Sql = "";

            Sql = "insert into historyofchanges (DateOfWork, TimeOfWork, IdHS, IdDG) values (?, ?, ?, ?)";

            try {
                PreparedStatement ps = cn.prepareStatement(Sql);
                ps.setString(1, d1);
                ps.setString(2, t1);
                ps.setInt(3, idhs);
                ps.setInt(4, iddg);

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

    }
    
    
    private void diagnosticGroups_ButtonHistoryOfChangesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_diagnosticGroups_ButtonHistoryOfChangesActionPerformed
             try {
            Statement sta = cn.createStatement();
            ResultSet rs;
            rs = sta.executeQuery("select DateOfWork, TimeOfWork, HSFIO, GroupName, Description, D2, D3, D4, D5, D6, D8, D11, D15, " +
                    "D20, D26, D36, D40, D65, D85, D120, D150, " +
                    "D210, D290, D300, D520, D700, D950, D1300, D1700, " +
                    "D2300, D3100, D4200, D5600, D7600, D10200, D13800, D18500"
                    + " from ((historyofchanges hc join hospitalstaff h on hc.IdHS = h.IdHS) join diagnosticgroup d on hc.IdDG = d.IdDG) join kvvvfloat k on d.IdKV = k.IdKV");
            new PatientFound(rs, 3);
        }
        catch(SQLException ex)
        {
            Logger.getLogger(PatientRegistration.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_diagnosticGroups_ButtonHistoryOfChangesActionPerformed

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
            java.util.logging.Logger.getLogger(AdminMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AdminMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AdminMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AdminMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AdminMain().setVisible(true);
            }
        });
    }

    

    private javax.swing.JButton newMed_ButtonAddCategory;
    private javax.swing.JButton diagnosticGroups_ButtonSaveGroup;
    private javax.swing.JButton newMed_ButtonAddPost;
    private javax.swing.JButton viewAndUpdatingMedData_ButtonFindPatientByFields;
    private javax.swing.JButton newMed_ButtonSave;
    private javax.swing.JButton adminMain_ButtonExit;
    private javax.swing.JButton diagnosticGroups_ButtonHistoryOfChanges;
    //private javax.swing.JButton ButtonEdit;
    private javax.swing.JButton diagnosticGroups_ButtonAddNewGroup;
    public static javax.swing.JComboBox newMed_ComboBoxCategory;
    public static javax.swing.JComboBox newMed_ComboBoxPost;
    private com.toedter.calendar.JDateChooser newMed_DateChooserBDay;
    private com.toedter.calendar.JDateChooser newMed_DateChooserJoinDay;
    private javax.swing.JTextField FieldMiddleName;
    private javax.swing.JTextField FieldName;
    private javax.swing.JTextField FieldSurname;
    private javax.swing.JTextField newMed_FieldTelephone;
    private javax.swing.JList DiagnosticGroups_ListOfDiagnosticGroups;
    private javax.swing.JPasswordField newMed_PasswordField;
    private javax.swing.JTable ViewAndUpdatingMedData_TableForFinding;
    private javax.swing.JTable viewAndUpdatingMedData_TableResult;
    private javax.swing.JTextArea TextAreaDescribes;
    private javax.swing.JTextArea DiagnosticGroups_TextAreaGroupDescription;
    private javax.swing.JTextField newMed_TextFieldLogin;
    private javax.swing.JTextField diagnosticGroups_TextFieldGroupName;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel newMed_LabelPassword;
    private javax.swing.JLabel newMed_LabelDescribes;
    private javax.swing.JLabel diagnosticGroups_label_InputGroupName;
    private javax.swing.JLabel diagnosticGroups_label_DescriptionOfNewGroup;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel newMed_LabelFillFields;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel NewMed_labelCategory;
    private javax.swing.JLabel newMed_LabelBirthday;
    private javax.swing.JLabel newMed_LabelRecruitmentDate;
    private javax.swing.JLabel newMed_LabelTelephone;
    private javax.swing.JLabel newMed_LabelPost;
    private javax.swing.JLabel newMed_LabelLogin;
    private javax.swing.JPanel newMed_PanelFullName;
    private javax.swing.JPanel diagnosticGroups_PanelGroupInfo;
    private javax.swing.JPanel diagnosticGroups_CommonPanel;
    private javax.swing.JPanel medQueries_CommonPanel;
    private javax.swing.JPanel newMed_CommonPanel;
    private javax.swing.JPanel viewAndUpdatingMedData_CommonPanel;
    private javax.swing.JScrollPane newMed_ScrollPaneDescribes;
    private javax.swing.JScrollPane viewAndUpdatingMedData_ScrollPaneTableResult;
    private javax.swing.JScrollPane diagnosticGroups_ScrollPaneGroupList;
    private javax.swing.JScrollPane viewAndUpdatingMedData_ScrollPaneTableForFinding;
    private javax.swing.JScrollPane diagnosticGroups_ScrollPaneDescriptionGroup;
    private javax.swing.JScrollPane diagnosticGroups_ScrollPaneQuarter2;
    private javax.swing.JScrollPane diagnosticGroups_ScrollPaneQuarter3;
    private javax.swing.JScrollPane diagnosticGroups_ScrollPaneQuarter1;
    private javax.swing.JScrollPane diagnosticGroups_ScrollPaneQuarter4;
    private javax.swing.JTabbedPane adminMain_TabbedPane;
    private javax.swing.JTable tableFirstQuarter;
    private javax.swing.JTable tableFourthQuarter;
    private javax.swing.JTable tableSecondQuarter;
    private javax.swing.JTable tableThirdQuarter;
    private javax.swing.JTable newMed_tableOfQueries;
    private javax.swing.JScrollPane newMed_ScrollPaneTableOfQueries;
    private javax.swing.JLabel diagnosticGroups_labelMoreInfo;
    
} 
/*    public static javax.swing.JComboBox ComboBoxCategory;
    public static javax.swing.JComboBox ComboBoxPost;*/
    /*
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList DiagnosticGroups_ListOfDiagnosticGroups;
    private javax.swing.JTextArea DiagnosticGroups_TextAreaGroupDescription;
    private javax.swing.JTextField FieldMiddleName;
    private javax.swing.JTextField FieldName;
    private javax.swing.JTextField FieldSurname;
    private javax.swing.JLabel NewMed_labelCategory;
    private javax.swing.JTextArea TextAreaDescribes;
    private javax.swing.JTable ViewAndUpdatingMedData_TableForFinding;
    private javax.swing.JButton adminMain_ButtonExit;
    private javax.swing.JTabbedPane adminMain_TabbedPane;
    private javax.swing.JButton diagnosticGroups_ButtonAddNewGroup;
    private javax.swing.JButton diagnosticGroups_ButtonHistoryOfChanges;
    private javax.swing.JButton diagnosticGroups_ButtonSaveGroup;
    private javax.swing.JPanel diagnosticGroups_CommonPanel;
    private javax.swing.JPanel diagnosticGroups_PanelGroupInfo;
    private javax.swing.JScrollPane diagnosticGroups_ScrollPaneDescriptionGroup;
    private javax.swing.JScrollPane diagnosticGroups_ScrollPaneGroupList;
    private javax.swing.JScrollPane diagnosticGroups_ScrollPaneQuarter1;
    private javax.swing.JScrollPane diagnosticGroups_ScrollPaneQuarter2;
    private javax.swing.JScrollPane diagnosticGroups_ScrollPaneQuarter3;
    private javax.swing.JScrollPane diagnosticGroups_ScrollPaneQuarter4;
    private javax.swing.JTextField diagnosticGroups_TextFieldGroupName;
    private javax.swing.JLabel diagnosticGroups_labelMoreInfo;
    private javax.swing.JLabel diagnosticGroups_label_DescriptionOfNewGroup;
    private javax.swing.JLabel diagnosticGroups_label_InputGroupName;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel medQueries_CommonPanel;
    private javax.swing.JButton newMed_ButtonAddCategory;
    private javax.swing.JButton newMed_ButtonAddPost;
    private javax.swing.JButton newMed_ButtonSave;
    private javax.swing.JComboBox newMed_ComboBoxCategory;
    private javax.swing.JComboBox newMed_ComboBoxPost;
    private javax.swing.JPanel newMed_CommonPanel;
    private com.toedter.calendar.JDateChooser newMed_DateChooserBDay;
    private com.toedter.calendar.JDateChooser newMed_DateChooserJoinDay;
    private javax.swing.JTextField newMed_FieldTelephone;
    private javax.swing.JLabel newMed_LabelBirthday;
    private javax.swing.JLabel newMed_LabelDescribes;
    private javax.swing.JLabel newMed_LabelFillFields;
    private javax.swing.JLabel newMed_LabelLogin;
    private javax.swing.JLabel newMed_LabelPassword;
    private javax.swing.JLabel newMed_LabelPost;
    private javax.swing.JLabel newMed_LabelRecruitmentDate;
    private javax.swing.JLabel newMed_LabelTelephone;
    private javax.swing.JPanel newMed_PanelFullName;
    private javax.swing.JPasswordField newMed_PasswordField;
    private javax.swing.JScrollPane newMed_ScrollPaneDescribes;
    private javax.swing.JScrollPane newMed_ScrollPaneTableOfQueries;
    private javax.swing.JTextField newMed_TextFieldLogin;
    private javax.swing.JTable newMed_tableOfQueries;
    private javax.swing.JTable tableFirstQuarter;
    private javax.swing.JTable tableFourthQuarter;
    private javax.swing.JTable tableSecondQuarter;
    private javax.swing.JTable tableThirdQuarter;
    private javax.swing.JButton viewAndUpdatingMedData_ButtonFindPatientByFields;
    private javax.swing.JPanel viewAndUpdatingMedData_CommonPanel;
    private javax.swing.JScrollPane viewAndUpdatingMedData_ScrollPaneTableForFinding;
    private javax.swing.JScrollPane viewAndUpdatingMedData_ScrollPaneTableResult;
    private javax.swing.JTable viewAndUpdatingMedData_TableResult;
    // End of variables declaration//GEN-END:variables

}

*/






/*
    
     private javax.swing.JButton ButtonAddCategory;
     private javax.swing.JButton ButtonAddPost;
     private javax.swing.JButton ButtonFindPatientByFields;
     private javax.swing.JButton ButtonSave;
     private javax.swing.JButton ButtonSignIn;
     public static javax.swing.JComboBox ComboBoxCategory;
     public static javax.swing.JComboBox ComboBoxPost;
     private com.toedter.calendar.JDateChooser DateChooserBDay;
     private com.toedter.calendar.JDateChooser DateChooserJoinDay;
     private javax.swing.JTextField FieldMiddleName;
     private javax.swing.JTextField FieldName;
     private javax.swing.JTextField FieldSurname;
     private javax.swing.JTextField FieldTelephone;
     private javax.swing.JPasswordField PasswordField;
     private javax.swing.JTable TableForFinding;
     private javax.swing.JTable TableResult;
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
     private javax.swing.JPanel jPanel3;
     private javax.swing.JPanel jPanel4;
     private javax.swing.JPanel jPanel5;
     private javax.swing.JPanel jPanel6;
     private javax.swing.JScrollPane jScrollPane1;
     private javax.swing.JScrollPane jScrollPane2;
     private javax.swing.JScrollPane jScrollPane4;
     private javax.swing.JTabbedPane jTabbedPane1;
     *//*
     private javax.swing.JScrollPane jScrollPane3;
     private javax.swing.JButton ButtonAddCategory;
     private javax.swing.JButton ButtonAddGroup;
     private javax.swing.JButton ButtonAddPost;
     private javax.swing.JButton ButtonFindPatientByFields;
     private javax.swing.JButton ButtonSave;
     private javax.swing.JButton ButtonSignIn;
     public static javax.swing.JComboBox ComboBoxCategory;
     public static javax.swing.JComboBox ComboBoxPost;
     private com.toedter.calendar.JDateChooser DateChooserBDay;
     private com.toedter.calendar.JDateChooser DateChooserJoinDay;
     private javax.swing.JTextField FieldD0;
     private javax.swing.JTextField FieldD1;
     private javax.swing.JTextField FieldD10;
     private javax.swing.JTextField FieldD11;
     private javax.swing.JTextField FieldD12;
     private javax.swing.JTextField FieldD13;
     private javax.swing.JTextField FieldD14;
     private javax.swing.JTextField FieldD15;
     private javax.swing.JTextField FieldD16;
     private javax.swing.JTextField FieldD17;
     private javax.swing.JTextField FieldD18;
     private javax.swing.JTextField FieldD19;
     private javax.swing.JTextField FieldD2;
     private javax.swing.JTextField FieldD20;
     private javax.swing.JTextField FieldD21;
     private javax.swing.JTextField FieldD22;
     private javax.swing.JTextField FieldD23;
     private javax.swing.JTextField FieldD24;
     private javax.swing.JTextField FieldD25;
     private javax.swing.JTextField FieldD26;
     private javax.swing.JTextField FieldD27;
     private javax.swing.JTextField FieldD28;
     private javax.swing.JTextField FieldD29;
     private javax.swing.JTextField FieldD3;
     private javax.swing.JTextField FieldD30;
     private javax.swing.JTextField FieldD31;
     private javax.swing.JTextField FieldD4;
     private javax.swing.JTextField FieldD5;
     private javax.swing.JTextField FieldD6;
     private javax.swing.JTextField FieldD7;
     private javax.swing.JTextField FieldD8;
     private javax.swing.JTextField FieldD9;
     private javax.swing.JTextField FieldMiddleName;
     private javax.swing.JTextField FieldName;
     private javax.swing.JTextField FieldSurname;
     private javax.swing.JTextField FieldTelephone;
     private javax.swing.JList ListOfDiagnosticGroups;
     private javax.swing.JPasswordField PasswordField;
     private javax.swing.JTable TableForFinding;
     private javax.swing.JTable TableResult;
     private javax.swing.JTextArea TextAreaDescribes;
     private javax.swing.JTextArea TextAreaDescription;
     private javax.swing.JTextField TextFieldLogin;
     private javax.swing.JTextField TextFieldNameOfGroup;
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
     private javax.swing.JLabel jLabel28;
     private javax.swing.JLabel jLabel29;
     private javax.swing.JLabel jLabel3;
     private javax.swing.JLabel jLabel30;
     private javax.swing.JLabel jLabel31;
     private javax.swing.JLabel jLabel32;
     private javax.swing.JLabel jLabel33;
     private javax.swing.JLabel jLabel34;
     private javax.swing.JLabel jLabel35;
     private javax.swing.JLabel jLabel36;
     private javax.swing.JLabel jLabel37;
     private javax.swing.JLabel jLabel38;
     private javax.swing.JLabel jLabel39;
     private javax.swing.JLabel jLabel4;
     private javax.swing.JLabel jLabel40;
     private javax.swing.JLabel jLabel41;
     private javax.swing.JLabel jLabel42;
     private javax.swing.JLabel jLabel43;
     private javax.swing.JLabel jLabel44;
     private javax.swing.JLabel jLabel45;
     private javax.swing.JLabel jLabel46;
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
     private javax.swing.JPanel jPanel6;
     private javax.swing.JScrollPane jScrollPane1;
     private javax.swing.JScrollPane jScrollPane2;
     // private javax.swing.JScrollPane jScrollPane3;
     private javax.swing.JScrollPane jScrollPane4;
     private javax.swing.JScrollPane jScrollPane5;
     private javax.swing.JTabbedPane jTabbedPane1;
     */