package com.haystack.util;

import java.sql.*;

public class DBConnect {
    public static Connection getConn() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        String sqlurl = "jdbc:mysql://localhost:3306/YourDatabaseName?useSSL=false&serverTimezone=UTC";
        String sqlusername = "root";
        String sqlpassword = "YourDatabasePassword";
        Connection connection = DriverManager.getConnection(sqlurl, sqlusername, sqlpassword);
        return connection;
    }
    public static void closeConnection(Statement statement, Connection connection) throws SQLException {
        statement.close();
        connection.close();
    }

    public static void closeConnection(ResultSet resultSet, Statement statement, Connection connection) throws SQLException {
        statement.close();
        closeConnection(statement,connection);
    }
}
