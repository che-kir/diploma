/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spectrex;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import static spectrex.MedLogin.cn;

/**
 *
 * @author Kirill
 */
public class QueryOfPatient {

    public QueryOfPatient() {

    }

    public void getQuery(int number, String text, String dateFrom, String dateTo) {
        switch (number) {
            case 1: {
                try {
                    Statement sta = cn.createStatement();
                    ResultSet rs;
                    rs = sta.executeQuery("select count(`IdP`)\n"
                            + "from seans s where s.`Diagnosis` = '" + text + "' and month(s.`Session_date`) =  month(current_date)-1 and year(s.`Session_date`) = year(current_date)");
                    new PatientFound(rs);
                } catch (SQLException ex) {
                    Logger.getLogger(PatientRegistration.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            break;
            case 2: {
                try {
                    Statement sta = cn.createStatement();
                    ResultSet rs;
                    rs = sta.executeQuery("select fio from patient where B_day between '" + dateFrom + "' and '" + dateTo + "'");
                    new PatientFound(rs);
                } catch (SQLException ex) {
                    Logger.getLogger(PatientRegistration.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            break;
            case 3: {

                try {
                    Statement sta = cn.createStatement();
                    ResultSet rs;
                    rs = sta.executeQuery("select fio from patient where Acc_date between '" + dateFrom + "' and '" + dateTo + "'");

                    new PatientFound(rs);
                } catch (SQLException ex) {
                    Logger.getLogger(PatientRegistration.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            break;
            case 4: {
                try {
                    Statement sta = cn.createStatement();
                    ResultSet rs;
                    rs = sta.executeQuery("select fio from (patient p join factaccomodation f on "
                            + "p.`IdP` = f.`IdP`) where District = '" + text + "'");
                    new PatientFound(rs);
                } catch (SQLException ex) {
                    Logger.getLogger(PatientRegistration.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            break;
            case 5: {
                try {
                    Statement sta = cn.createStatement();
                    ResultSet rs;
                    rs = sta.executeQuery("select fio from (patient p join diagnosticgroup d on "
                            + "p.`IdDG` = d.`IdDG`) where GroupName = '" + text + "'");
                    new PatientFound(rs);
                } catch (SQLException ex) {
                    Logger.getLogger(PatientRegistration.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            break;
            case 6: {
                try {
                    Statement sta = cn.createStatement();
                    ResultSet rs;
                    rs = sta.executeQuery("select FIO from patient p left join seans s on p.IdP = s.IdP where IdS is null");
                    new PatientFound(rs);
                } catch (SQLException ex) {
                    Logger.getLogger(PatientRegistration.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            break;
            case 7: {
                try {
                    Statement sta = cn.createStatement();
                    ResultSet rs;
                    rs = sta.executeQuery("select Inst_name, count(`IdP`)\n"
                            + "from (patient p join institution i on p.`IdInst` = i.`IdInst`) group by p.`IdInst`");
                    new PatientFound(rs);
                } catch (SQLException ex) {
                    Logger.getLogger(PatientRegistration.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            break;
            default:
                break;
        }
    }
}
