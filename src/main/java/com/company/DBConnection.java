package com.company;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by gp on 21.10.15.
 */
public class DBConnection {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/MyBase1?characterEncoding=utf8";
    private static final String USER = "root";
    private static final String PASSWORD = "123";

    //TODO:change input data type
    public void putIntoDB(ArrayList<String> InfoIntoDB) throws SQLException {
        Connection connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
        PreparedStatement stmt = connection.prepareStatement("INSERT INTO MyTable1 (MyNumber, MyTitle, " +
                "MyMainText, MyDate, MyLink, MyMainLink) VALUES (?, ?, ?, ?, ?, ?)");
        stmt.setString(1, InfoIntoDB.get(0));
        stmt.setString(2, InfoIntoDB.get(1));
        stmt.setString(3, InfoIntoDB.get(2));
        stmt.setString(4, InfoIntoDB.get(3));
        stmt.setString(5, InfoIntoDB.get(4));
        stmt.setString(6, InfoIntoDB.get(5));
        stmt.executeUpdate();
        System.out.println(stmt);
        //connection.close();
    }

    public static ArrayList<String> takeFromDB(String nameOfNewsPortal) throws SQLException {

        ArrayList<String> ArrayListInformFromDB = new ArrayList<String>();
        String query = "select MyLink from MyTable1 WHERE MyMainLink LIKE \"%" + nameOfNewsPortal + "%\"";
        System.out.println(query);
        Connection connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(query);

        while (rs.next()) {
            ArrayListInformFromDB.add(rs.getString(1));
        }
        return ArrayListInformFromDB;
    }

    public static int showNumOfNewsInDB() {
        int count = 0;
        String query = "select count(*) from MyTable1";

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                count = resultSet.getInt(1);
            }
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException se) { /*can't do anything */ }
            try {
                statement.close();
            } catch (SQLException se) { /*can't do anything */ }
            try {
                resultSet.close();
            } catch (SQLException se) { /*can't do anything */ }
        }
        return count;
    }

    public void putSentimentToDB(String score, String myNumber, String myDate) throws SQLException {
        Connection connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
        /*PreparedStatement stmt = connection.prepareStatement("INSERT INTO MyTable1 sentiment values ?" +
                "WHERE MyNumber=? AND MyDate=?");*/
        PreparedStatement stmt = connection.prepareStatement("UPDATE MyTable1 SET sentiment = ? " +
                "WHERE MyNumber=?");
        stmt.setString(1, score);
        stmt.setString(2, myNumber);
        //stmt.setString(3, myDate);
        System.out.println(stmt);
        stmt.executeUpdate();
    }
/*
    public static String takeLastNewsLinkFromDB() throws SQLException {
        String LinkOfLastNewsInDB = null;
        String query1 = "select * FROM MyTable1 WHERE MyNumber = 0";

        Connection connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(query1);

        while (rs.next()) {
            LinkOfLastNewsInDB = ((rs.getString(5)));
        }
        return LinkOfLastNewsInDB;
    }
*/
}
