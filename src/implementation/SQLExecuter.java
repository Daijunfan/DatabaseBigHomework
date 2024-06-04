package implementation;

import java.sql.*;

public class SQLExecuter {
    String message;
    Runner.Phone phone;
    String url;
    String username;
    String password;
    Connection connection;
    Statement statement;

    public SQLExecuter(Runner.Phone phone) throws SQLException {
        this.phone = phone;
        url = "jdbc:mysql://127.0.0.1:3306/educationalsystem";
        username = "root";
        password = "Abc343429257";
        connection = DriverManager.getConnection(url, username, password);
        statement = connection.createStatement();
    }

    public Object execute(String sql) throws SQLException {
        System.out.println("Executing SQL: " + sql);
        if (sql.toLowerCase().startsWith("select")) {
            ResultSet resultSet = statement.executeQuery(sql);
            return resultSet;
        } else if (sql.toLowerCase().startsWith("insert")) {
            statement.executeUpdate(sql);
        } else if (sql.toLowerCase().startsWith("update")) {
            statement.executeUpdate(sql);
        } else if (sql.toLowerCase().startsWith("delete")) {
            statement.executeUpdate(sql);
        }
        return null;
    }

    public void closeDatabaseSource() throws SQLException {
        System.out.println("release statement and connection");
        statement.close();
        connection.close();
        System.out.println("release successful!");
    }


}
