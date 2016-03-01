/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spectrex;

import static spectrex.Seans.ids;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Comparator;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author Kirill
 */
public class PatientFound extends javax.swing.JFrame {

    /**
     * Creates new form PatientFound
     */
    public Connection cn = MedLogin.cn;
    public int updatemod;
    
    public PatientFound() {

        initComponents();
              try {             
            Statement sta = cn.createStatement();
            ResultSet rs;
            rs = sta.executeQuery("select N_med_card, FIO, Gender, B_day, Tel, Passport, Acc_date, Disease, Allergy, E_mail, Inst_name, GroupName"
                    + " from (patient p join institution i on p.IdInst = i.IdInst) join diagnosticgroup d on p.IdDG = d.IdDG");
            TableResult.setModel(resultSetToTableModel(rs));
                        Comparator myComparator = new java.util.Comparator() {
    /**
     * Custom compare to sort numbers as numbers.
     * Strings as strings, with numbers ordered before strings.
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
                try{
                    Integer.parseInt(o1);
                    isFirstNumeric = true;
                }catch(NumberFormatException e){
                    isFirstNumeric = false;
                }
                try{
                    Integer.parseInt(o2);
                    isSecondNumeric = true;
                }catch(NumberFormatException e){
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
TableResult.setRowSorter(sorter);
sorter.setModel(TableResult.getModel());
sorter.setComparator(0, myComparator);
            }                   
        catch(SQLException ex)
        {
            Logger.getLogger(PatientRegistration.class.getName()).log(Level.SEVERE, null, ex);
        }    
        java.awt.Dimension dim = getToolkit().getScreenSize();
        this.setLocation(dim.width/2 - this.getWidth()/2,
                dim.height/2 - this.getHeight()/2);
    }
    
    
    
    public PatientFound(ResultSet r)
    {
        initComponents();
       /* ButtonGoToUpdateMedCard.setEnabled(false);
        ButtonUpdateMedStaff.setEnabled(false);
        ButtonViewHistogram.setEnabled(false);
        ButtonToSession.setEnabled(false);
        FieldCardNumber.setEnabled(false);
        TextFieldIdSession.setEnabled(false);*/
        
        java.awt.Dimension dim = getToolkit().getScreenSize();
        this.setLocation(dim.width/2 - this.getWidth()/2,
                dim.height/2 - this.getHeight()/2);
        TableResult.setModel(resultSetToTableModel(r));
                                Comparator myComparator = new java.util.Comparator() {
    /**
     * Custom compare to sort numbers as numbers.
     * Strings as strings, with numbers ordered before strings.
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
                try{
                    Integer.parseInt(o1);
                    isFirstNumeric = true;
                }catch(NumberFormatException e){
                    isFirstNumeric = false;
                }
                try{
                    Integer.parseInt(o2);
                    isSecondNumeric = true;
                }catch(NumberFormatException e){
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
TableResult.setRowSorter(sorter);
sorter.setModel(TableResult.getModel());
sorter.setComparator(0, myComparator);
        this.setVisible(true);
    }
    
    public PatientFound(ResultSet r, int upd)
    {
        initComponents();
        java.awt.Dimension dim = getToolkit().getScreenSize();
        this.setLocation(dim.width/2 - this.getWidth()/2,
        dim.height/2 - this.getHeight()/2);
        TableResult.setModel(resultSetToTableModel(r));
                                Comparator myComparator = new java.util.Comparator() {
    /**
     * Custom compare to sort numbers as numbers.
     * Strings as strings, with numbers ordered before strings.
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
                try{
                    Integer.parseInt(o1);
                    isFirstNumeric = true;
                }catch(NumberFormatException e){
                    isFirstNumeric = false;
                }
                try{
                    Integer.parseInt(o2);
                    isSecondNumeric = true;
                }catch(NumberFormatException e){
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
TableResult.setRowSorter(sorter);
sorter.setModel(TableResult.getModel());
sorter.setComparator(0, myComparator);
        this.setVisible(true);
        updatemod = upd;
        
        ListOfDiagnosticGroups.addMouseListener( new MouseAdapter() {
        public void mousePressed(MouseEvent me) {
        if (me.getClickCount() == 2) {
        JList list =(JList) me.getSource();
        Point p = me.getPoint();
        int row = list.locationToIndex(p);
        float D[] = new float[32];
            switch(row)
            {
                case 0: {
            for(int i=0; i<32; i++)
                D[i] = 0.f;
            
            D[1] = 35.21f;            
            D[2] = 47.40f;            
            D[3] = 11.18f;            
            D[4] = 6.23f;            
            D[5] = 0.44f;
            D[6] = 1.09f;
            D[7] = 1.21f;
            D[8] = 2.07f;
            D[18] = 5.18f;
            D[19] = 3.67f;	            
            D[20] = 1.74f;            
            D[21] = 1.77f;
            D[22] = 0.14f;            
            D[23] = 0.16f;

                 ChartData data = new ChartData();
            data.addData("normal", D);
        Charts.createBarChart(data, "", "Moisture condensation of exhaled air (particle size), nm", "Average value of percentage contribution, %", new Dimension(800, 600));
        }; break;
                case 1:
        {
            for(int i=0; i<32; i++)
                D[i] = 0;

            D[8] = 0.10f;
            D[9] = 0.36f;
            D[10] = 0.74f;
            D[11] = 1.80f;
            D[12] = 7.26f;
            D[13] = 8.40f;
            D[14] = 3.52f;
            D[15] = 4.43f;
            D[16] = 12.19f;
            D[17] = 7.50f;
            D[18] = 12.72f;
            D[19] = 6.27f;
            D[20] = 6.84f;
            D[21] = 12.38f;
            D[22] = 5.73f;
            D[23] = 4.92f;
            D[24] = 3.51f;
            D[25] = 1.06f;
            D[26] = 0.21f;
            D[27] = 0.05f;


                 ChartData data = new ChartData();
            data.addData("bronchitis", D);
        Charts.createBarChart(data, "", "Moisture condensation of exhaled air (particle size), nm", "Average value of percentage contribution, %", new Dimension(800, 600));
        }; break;
                case 2:
        {
            for(int i=0; i<32; i++)
                D[i] = 0;
            
            D[0] = 68.35f;            
            D[1] = 30.56f;           
            D[2] = 44.04f;
            D[3] = 9.78f;
            D[4] = 2.18f;
            D[5] = 2.57f;
            D[6] = 2.20f;
            D[7] = 1.69f;
            D[8] = 5.76f;
            D[9] = 6.95f;
            D[10] = 3.08f;
            D[11] = 0.07f;	
            D[12] = 6.76f;
            D[13] = 8.86f;
            D[14] = 3.43f;
            D[15] = 6.36f;
            D[16] = 9.64f;
            D[17] = 3.11f;
            D[18] = 2.34f;           
            D[19] = 0.88f;
            D[20] = 0.45f;
            D[21] = 1.77f;
            D[22] = 0.28f;	
            D[28] = 0.22f;
            D[29] = 0.23f;
            D[30] = 0.05f;	
                 ChartData data = new ChartData();
            data.addData("pneumonia", D);
        Charts.createBarChart(data, "", "Moisture condensation of exhaled air (particle size), nm", "Average value of percentage contribution, %", new Dimension(800, 600));
        } break;
                default:  JOptionPane.showMessageDialog(null, "Мимо!"); break;
            }
        }
    }
    }
        );
        TableResult.addMouseListener(new MouseAdapter() {
    public void mousePressed(MouseEvent me) {
        JTable table =(JTable) me.getSource();
        Point p = me.getPoint();
        int row = table.rowAtPoint(p);
        if (me.getClickCount() == 2) {
            //JOptionPane.showMessageDialog(null, "Выбрана строка " + row);
            int ids = Integer.parseInt(table.getValueAt(row, 0).toString());
            new Seans(ids).setVisible(true);
            
            
             float D[] = new float[32];
            try {
                Statement s = cn.createStatement();
            
                ResultSet rs = s.executeQuery("SELECT * FROM kvvvfloat k join seans s on k.IdKV = s.IdKV");
            
                int i = 0;
                while(rs.next())
                {
                    D[i++] = Float.parseFloat(rs.getString(i));  
                }
                                             
                }
            
                catch(NumberFormatException e)
                {
                  JOptionPane.showMessageDialog(null, e);
                } catch (SQLException ex) {
                 Logger.getLogger(PatientFound.class.getName()).log(Level.SEVERE, null, ex);
                }
        
       // BarGraph bg = new BarGraph(D, 800, 520, 25, 5);
            ChartData data = new ChartData();
            data.addData("patient", D);
            Charts.createBarChart(data, "", "Moisture condensation of exhaled air", "Values", new Dimension(800, 600));
            
            
            NNClassifier nn = new NNClassifier("trmse0.net");
            int k = nn.classify(D);
          //  JOptionPane.showMessageDialog(null, k);
            new ResultClassifier(k).setVisible(true);
        }
    }
});
TableResult.setEnabled(false);
       /* if(updatemod == 1)//update patient
        {            
            ButtonAllTable.setEnabled(false);
            ButtonToQueries.setEnabled(false);
            ButtonViewHistogram.setEnabled(false);
            ButtonToSession.setEnabled(false);
            ButtonUpdateMedStaff.setEnabled(false);
            TextFieldColumn.setEnabled(false);
            TextFieldValueOfColumn.setEnabled(false);
            TextFieldIdSession.setEnabled(false);
        }
        else if (updatemod == 2)//view histogram
        {
            ButtonAllTable.setEnabled(false);
            ButtonToQueries.setEnabled(false);
            ButtonGoToUpdateMedCard.setEnabled(false);
            ButtonUpdateMedStaff.setEnabled(false);
            TextFieldColumn.setEnabled(false);
            TextFieldValueOfColumn.setEnabled(false);
            FieldCardNumber.setEnabled(false);
        }
        else if (updatemod == 3)//admin operator
        {
            ButtonToSession.setEnabled(false);
            ButtonUpdateMedStaff.setEnabled(false);
            ButtonGoToUpdateMedCard.setEnabled(false);
            ButtonViewHistogram.setEnabled(false);
            TextFieldIdSession.setEnabled(false);
            FieldCardNumber.setEnabled(false);
        }
        else if(updatemod == 4)//update staff
        {
            ButtonAllTable.setEnabled(false);
            ButtonToQueries.setEnabled(false);
            ButtonViewHistogram.setEnabled(false);
            ButtonToSession.setEnabled(false);
            ButtonGoToUpdateMedCard.setEnabled(false);
            TextFieldColumn.setEnabled(false);
            TextFieldValueOfColumn.setEnabled(false);
            TextFieldIdSession.setEnabled(false);
        }
        else if(updatemod == 5)//advanced search for patients
        {
            ButtonViewHistogram.setEnabled(false);
            ButtonToSession.setEnabled(false);
            ButtonUpdateMedStaff.setEnabled(false);
            TextFieldIdSession.setEnabled(false);
        }*/
    }
    
    public static TableModel resultSetToTableModel(ResultSet rs) {
        try {
            ResultSetMetaData metaData = rs.getMetaData();
            int numberOfColumns = metaData.getColumnCount();
            Vector columnNames = new Vector();

            // Get the column names
            for (int column = 0; column < numberOfColumns; column++) {
                columnNames.addElement(metaData.getColumnLabel(column + 1));
            }

            // Get all rows.
            Vector rows = new Vector();

            while (rs.next()) {
                Vector newRow = new Vector();

                for (int i = 1; i <= numberOfColumns; i++) {
                    newRow.addElement(rs.getObject(i));
                }

                rows.addElement(newRow);
            }

            return new DefaultTableModel(rows, columnNames);
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        TableResult = new javax.swing.JTable();
        ButtonCancel = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        ListOfDiagnosticGroups = new javax.swing.JList();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("spectrex/Bundle"); // NOI18N
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("PatientFound.jPanel1.border.title"))); // NOI18N

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

        ButtonCancel.setText(bundle.getString("PatientFound.ButtonCancel.text")); // NOI18N
        ButtonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonCancelActionPerformed(evt);
            }
        });

        ListOfDiagnosticGroups.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Норма", "Бронхит", "Пневмония" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(ListOfDiagnosticGroups);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(ButtonCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 915, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(ButtonCancel))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void ButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonCancelActionPerformed
        this.dispose();
    }//GEN-LAST:event_ButtonCancelActionPerformed

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
            java.util.logging.Logger.getLogger(PatientFound.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PatientFound.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PatientFound.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PatientFound.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PatientFound().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton ButtonCancel;
    private javax.swing.JList ListOfDiagnosticGroups;
    private javax.swing.JTable TableResult;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    // End of variables declaration//GEN-END:variables
}


/*

    private void ButtonAllTableActionPerformed(java.awt.event.ActionEvent evt) {                                               
        try {             
            String query = TextFieldColumn.getText();
            Statement sta = cn.createStatement();
            ResultSet rs;
            if(updatemod != 3)
            rs = sta.executeQuery("select N_med_card, FIO, Gender, B_day, Tel, Passport, Acc_date, Disease, Allergy, E_mail, Inst_name, GroupName"
                    + " from (patient p join institution i on p.IdInst = i.IdInst) join diagnosticgroup d on p.IdDG = d.IdDG where " + query + " like '%" + TextFieldValueOfColumn.getText() + "%'");
            else
                rs = sta.executeQuery("select IdJ, DateOfWork, TimeOfWork, HSFIO, Enter, Quit, InputDataInTable, UseQuery"
                    + " from journal j join hospitalstaff h on j.IdHS = h.IdHS where " + query + " like '%" + TextFieldValueOfColumn.getText() + "%'");   
            new PatientFound(rs, 3);
            }                   
        catch(SQLException ex)
        {
            Logger.getLogger(PatientRegistration.class.getName()).log(Level.SEVERE, null, ex);
        }    
    }                                              



    private void ButtonViewHistogramActionPerformed(java.awt.event.ActionEvent evt) {                                                    
         float D[] = new float[32];  
         String surname = "";
         String hssurname = "";
         String date = "";
        try
         {
            Statement sta = cn.createStatement();
            ResultSet rs;

            rs = sta.executeQuery("select D2, D3, D4, D5, D6, D8, D11, D15, " +
                    "D20, D26, D36, D40, D65, D85, D120, D150, " +
                    "D210, D290, D300, D520, D700, D950, D1300, D1700, " +
                    "D2300, D3100, D4200, D5600, D7600, D10200, D13800, D18500 from kvvvfloat k "
                    + "join seans s on k.IdKV = s.IdKV where IdS = " + TextFieldIdSession.getText());
            if(!rs.first())
                JOptionPane.showMessageDialog(null, "Неверный ids");
            else {
                for(int i = 0; i < 32; i++)
                    D[i] = rs.getFloat(i+1);
                 }
            
            ////////
            Statement sta2 = cn.createStatement();
            ResultSet rs2;

            rs2 = sta2.executeQuery("select Session_date, FIO, HSFIO from (seans s join "
                    + " patient p on s.IdP = p.IdP) join hospitalstaff h on s.IdHS = h.IdHS"
                    + " where s.IdS = " + TextFieldIdSession.getText());
            //new PatientFound(rs).setVisible(true);
            if(!rs2.first())
                JOptionPane.showMessageDialog(null, "Неверный ids");
            else {
                 surname = rs2.getString("FIO");
                 hssurname = rs2.getString("HSFIO");
                 date = rs2.getString("Session_date");
                 //JOptionPane.showMessageDialog(null, "ids: " + ids);
                 }
          } 
         catch (SQLException ex) {
            Logger.getLogger(MedLogin.class.getName()).log(Level.SEVERE, null, ex);
        }
        ChartData data = new ChartData();
            data.addData(date + ", patient: " + surname + ", doctor: " + hssurname, D);
        Charts.createBarChart(data, "Histogram", "Moisture condensation of exhaled air", "Values", new Dimension(800, 600));
    }                                                   

*/