//by 정원

package main.java;

import java.sql.*;
import java.util.*;

class SqlContorller {
    private String url;
    private String user;
    private String password;

    SqlContorller(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    public boolean checkData(String query) {
        boolean result = false;
        try (
                Connection connection = DriverManager.getConnection(url, user, password);
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query)
        ) {
            while (resultSet.next()) {
                result = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("SELECT query failed.");
        }
        return result;
    }

    public int insert(String query) {
        int rowsAffected = 0;

        try (
                Connection connection = DriverManager.getConnection(url, user, password);
                Statement statement = connection.createStatement()
        ) {
            rowsAffected = statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("INSERT query failed.");
        }
        return rowsAffected;
    }

    public int delete(String query) {
        int rowsAffected = 0;

        try (
                Connection connection = DriverManager.getConnection(url, user, password);
                Statement statement = connection.createStatement()
        ) {
            rowsAffected = statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("INSERT query failed.");
        }
        return rowsAffected;
    }
}

