package com.company;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by gp on 21.10.15.
 */
public class DBConnection {
    private static final String url = "jdbc:mysql://localhost:3306/MyBase1";
    private static final String user = "root";
    private static final String password = "123";
    private static Connection con;
    private static Statement stmt;
    private static ResultSet rs;

    public static void PutIntoDB(ArrayList<String> InfoIntoDB) throws SQLException {
        PreparedStatement stmt = null;
        con = DriverManager.getConnection(url, user, password);
        ;
        stmt = con.prepareStatement("INSERT INTO MyTable1 (MyNumber, MyTitle, MyMainText, MyDate, MyLink, MyMainLink) VALUES (?, ?, ?, ?, ?, ?)");
        stmt.setString(1, InfoIntoDB.get(0));
        stmt.setString(2, InfoIntoDB.get(1));
        stmt.setString(3, InfoIntoDB.get(2));
        stmt.setString(4, InfoIntoDB.get(3));
        stmt.setString(5, InfoIntoDB.get(4));
        stmt.setString(6, InfoIntoDB.get(5));
        stmt.executeUpdate();
    }

    public static ArrayList<String> TakeFromDB() throws SQLException {

        ArrayList<String> ArrayListInformFromDB = new ArrayList<String>();
        String query1 = "select * from MyTable1";
        con = DriverManager.getConnection(url, user, password);
        stmt = con.createStatement();
        rs = stmt.executeQuery(query1);

        while (rs.next()) {
            ArrayListInformFromDB.add(rs.getString(1));
            ArrayListInformFromDB.add(rs.getString(2));
            ArrayListInformFromDB.add(rs.getString(3));
            ArrayListInformFromDB.add(rs.getString(4));
            ArrayListInformFromDB.add(rs.getString(5));
            ArrayListInformFromDB.add(rs.getString(6));
        }
        System.out.println(query1);
        con = DriverManager.getConnection(url, user, password);
        stmt = con.createStatement();
        return ArrayListInformFromDB;
    }

    public static int ShowNumOfNewsInDB() {
        int count = 0;
        String query = "select count(*) from MyTable1";

        try {
            con = DriverManager.getConnection(url, user, password);
            stmt = con.createStatement();
            rs = stmt.executeQuery(query);

            while (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (SQLException se) { /*can't do anything */ }
            try {
                stmt.close();
            } catch (SQLException se) { /*can't do anything */ }
            try {
                rs.close();
            } catch (SQLException se) { /*can't do anything */ }
        }
        return count;
    }

    public static String TakeLastNewsLinkFromDB() throws SQLException {
        String LinkOfLastNewsInDB = null;
        String query1 = "select * FROM MyTable1 WHERE MyNumber = 0";

        con = DriverManager.getConnection(url, user, password);
        stmt = con.createStatement();
        rs = stmt.executeQuery(query1);

        while (rs.next()) {
            LinkOfLastNewsInDB = ((rs.getString(5)));
        }
        return LinkOfLastNewsInDB;
    }

}
