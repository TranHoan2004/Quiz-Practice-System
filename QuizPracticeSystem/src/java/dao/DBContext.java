/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import common.Constants;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author TranHoan
 */
public class DBContext {
    private static final String SERVER_NAME = "localhost";
    private static final String DB_NAME = "swp391";
    private static final String PORT_NUMBER = "3306";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "hoan2709";

    public Connection getConnection() throws Exception {
        String url = "jdbc:mysql://" + SERVER_NAME + ":" + PORT_NUMBER
                + "/" + DB_NAME;
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(
                url, USERNAME, PASSWORD);
    }

}
