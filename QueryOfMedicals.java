/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spectrex;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import static spectrex.MedLogin.cn;

/**
 *
 * @author Kirill
 */
public class QueryOfMedicals {
   public QueryOfMedicals() {

    }

    public void getQuery(int number, String text, String dateFrom, String dateTo) {
        switch (number) {
            case 1: {
                try {

                    Statement sta = cn.createStatement();
                    ResultSet rs;

                    rs = sta.executeQuery("select count(`IdHS`)\n"
                            + "from hospitalstaff h where h.`IdPost` = 2 and year(current_date) - year(h.`Date_to_join`) >= '" + text + "'");

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
                    rs = sta.executeQuery("select HSFIO from hospitalstaff h left join (select * from seans s\n"
                            + "where month(s.Session_date) = month(current_date) and year(s.Session_date) = year(current_date)) a \n"
                            + "on h.IdHS = a.IdHS where a.IdHS is null and h.IdPost = 2");

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
                    rs = sta.executeQuery("select CategoryName, HSFIO, count(distinct(s.`IdP`))\n"
                            + "from (hospitalstaff h join seans s on h.`IdHS` = s.`IdHS`) join category c on h.IdCat = c.IdCat \n"
                            + "having count(*) >= all \n"
                            + "(select count(*)\n"
                            + "from (hospitalstaff h join seans s on h.`IdHS` = s.`IdHS`) join category c on h.IdCat = c.IdCat)");

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