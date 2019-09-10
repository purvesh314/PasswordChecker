/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package passwordcheck;

import java.io.*;
import java.util.*;
import java.net.*;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.time.LocalDate;
import java.time.Period;

public class PasswordCheck {

    static String url = "jdbc:mysql://10.10.11.59:3306/";
    static String dbName = "te3157db";
    static String driver = "com.mysql.jdbc.Driver";
    static String db_username = "te3157";
    static String db_password = "te3157";
    static Connection conn = null;
    static Statement st = null;
    static int port = 0;

    void connectToDatabase() {
        try {
            Class.forName(driver);
            System.out.println(conn);
            PasswordCheck.conn = DriverManager.getConnection(url + dbName, db_username, db_password);
            System.out.println(conn);
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e);
        }
    }

    boolean hasUpperCase(String password) {
        char[] pw = password.toCharArray();
        for (int i = 0; i < password.length(); i++) {
            if (Character.isUpperCase(pw[i])) {
                return true;
            }
        }
        return false;
    }

    boolean hasLowerCase(String password) {
        char[] pw = password.toCharArray();
        for (int i = 0; i < password.length(); i++) {
            if (Character.isLowerCase(pw[i])) {
                return true;
            }
        }
        return false;
    }

    boolean hasNumber(String password) {
        char[] pw = password.toCharArray();
        int x;
        for (int i = 0; i < password.length(); i++) {
            if ((int)(pw[i]) >= 48 && (int)(pw[i]) <= 57) {
                return true;
            }
        }
        return false;
    }

    boolean hasSpecialCharacter(String password) {
        char[] pw = password.toCharArray();
        String specialCharacters = "@#$%\\*/";
        for (int i = 0; i < password.length(); i++) {
            if (specialCharacters.indexOf(pw[i]) >= 0) {
                return true;
            }
        }
        return false;
    }

    int daysElapsedSinceLastLogin(String u_name) {
        try {
            System.out.println(u_name);
            System.out.println(conn);
            PreparedStatement ps = conn.prepareStatement("select lastDate from Users where username=?");
            ps.setString(1, u_name);
            ResultSet rs = ps.executeQuery();
            System.out.println(rs.next()+"Here");
            java.sql.Date ts=rs.getDate("lastDate");
            System.out.println(ts);
            LocalDate dt=ts.toLocalDate();
            LocalDate ldt = LocalDate.now();
            Period diff=Period.between(dt, ldt);
            int days=diff.getDays();
            System.out.println(days);

        } catch (SQLException ex) {
            Logger.getLogger(PasswordCheck.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;

    }

    boolean isRedundantPassword(String password,String username) throws SQLException {
        PreparedStatement ps=conn.prepareStatement("select * from Users where username=?");
        ps.setString(1, username);
        ResultSet rs=ps.executeQuery();
        System.out.println(rs.next()+"Here");
        String pw=rs.getString("password");
        String pw1=rs.getString("password1");
        String pw2=rs.getString("password2");
        String pw3=rs.getString("password3");
        String pw4=rs.getString("password4");
        
        return !(!password.equals(pw) && !password.equals(pw1) && !password.equals(pw2)&& !password.equals(pw3)&& !password.equals(pw4));
    }

    boolean isLocked(String username) throws SQLException {
        PreparedStatement ps=conn.prepareStatement("select * from Users where username=?");
        ps.setString(1, username);
        ResultSet rs=ps.executeQuery();
        System.out.println(rs.next()+"Here");
        int x=rs.getInt("isLocked");
        System.out.println(x);
        
        return x != 0;
    }

    boolean isDefaultPassword(String password) {
        return password.equals("purVES3$");

    }

    public static void main(String[] args) throws SQLException {
        // TODO code application logic here
        PasswordCheck pw=new PasswordCheck();
        pw.connectToDatabase();
        LocalDate ldt = LocalDate.now();
        //System.out.println(ldt);
        pw.daysElapsedSinceLastLogin("manav");
//        boolean locked;
//        locked = pw.isLocked("bhushan");
//        boolean hasLowerCase = pw.hasLowerCase("ABCabc0@");
//        boolean hasUpperCase = pw.hasUpperCase("ABCabc0@");
//        boolean hasNumber = pw.hasNumber("ABCabc0@");
//        boolean hasSpecialCharacter = pw.hasSpecialCharacter("ABCabc0@");
//        System.out.println(hasLowerCase + " " + hasUpperCase +" "+" "+hasNumber+" "+hasSpecialCharacter);
        boolean redundantPassword = pw.isRedundantPassword("ABsabc0@", "bhushan");
        System.out.println(redundantPassword);
    }

}
