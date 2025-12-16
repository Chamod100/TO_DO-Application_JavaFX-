package org.example.dbConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

        private static DBConnection instance;
        private Connection connection;

        public DBConnection() throws SQLException {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/TO_DO_Application","root","1234");
        }

        public static DBConnection getInstance() throws SQLException {
            if (instance == null){
                instance = new DBConnection();
            }
            return instance;
        }
        public Connection getConnection(){
            return connection;
        }

}
