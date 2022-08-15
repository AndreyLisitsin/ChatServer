package servers.services.impl;

import servers.handlers.ClientHandler;
import servers.services.AuthenticationService;

import java.sql.*;

public class DBAuthenticationServiceImpl implements AuthenticationService {

    @Override
    public String getUsernameByLoginAndPassword(String login, String password) {
          Connection connection = null;
          Statement statement = null;
          ResultSet resultSet = null;
          String username = null;
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:C:\\MyProjects\\Server_for_Chat\\identifier.sqlite");
            statement = connection.createStatement();
            resultSet = statement.executeQuery(String.format("SELECT * from users WHERE login = '%s'", login));
            if (password.equals(resultSet.getString("password"))){
                username = resultSet.getString("username");
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        finally {
            try {
                connection.close();
                statement.close();
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return username;
    }

    public void createNewUSer(String user){

        Connection connection = null;
        Statement statement = null;
        user.trim();

        String[] newUser = user.split("\\s+");
        String username = newUser[1];
        String login = newUser[2];
        String password = newUser[3];

            try {
                Class.forName("org.sqlite.JDBC");
                connection = DriverManager.getConnection("jdbc:sqlite:C:\\MyProjects\\Server_for_Chat\\identifier.sqlite");
                statement = connection.createStatement();
                statement.executeUpdate(String.format("INSERT INTO users(username, login, password) VALUES ('%s','%s','%s')", username, login, password));
                System.out.println(String.format("Регистрация пользователя + %s с логином %s и паролем %s прошла успешно", username, login, password));
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
            finally {
                try {
                    if (connection!= null)
                    connection.close();
                    if (statement != null)
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
    }

    public void updateUsernameByNickname(ClientHandler handler, String newUsername){
        Connection connection = null;
        Statement statement = null;
        String oldUsername = handler.getUsername();

        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:C:\\MyProjects\\Server_for_Chat\\identifier.sqlite");
            statement = connection.createStatement();
            statement.executeUpdate(String.format("UPDATE users SET username = '%s' WHERE username ='%s'",
                    newUsername, handler.getUsername()));
            handler.setUsername(newUsername);
            System.out.println(String.format("Пользователь %s сменил username на %s"));
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (connection!= null)
                    connection.close();
                if (statement != null)
                    statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
