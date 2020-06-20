package Persistence;

import org.apache.commons.dbcp2.BasicDataSource;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

public class DbConnect {
    private static final BasicDataSource SOURCE = new BasicDataSource();
    private static final DbConnect INSTANCE = new DbConnect();
    private Connection connection;

    private DbConnect() {
        this.setConnection();
    }

    public static DbConnect getInstance() {
        return INSTANCE;
    }

    private void setConnection() {
        try (InputStream in = DbConnect.class.getClassLoader().getResourceAsStream("app.properties")) {
            Properties config = new Properties();
            config.load(in);
            SOURCE.setDriverClassName(config.getProperty("connection.driver_class"));
            SOURCE.setUrl(config.getProperty("connection.url"));
            SOURCE.setUsername(config.getProperty("connection.username"));
            SOURCE.setPassword(config.getProperty("connection.password"));
            connection = SOURCE.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int[][] getPlaces() {
        String sqlc = "select * from place;";
        int[][] array = new int[3][3];
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlc); ResultSet resultSet = preparedStatement.executeQuery()) {
            int x = 0;
            while (resultSet.next()) {
                array[x][0] = resultSet.getBoolean(1) == true ? 1 : 0;
                array[x][1] = resultSet.getBoolean(2) == true ? 1 : 0;
                array[x][2] = resultSet.getBoolean(3) == true ? 1 : 0;
                x++;
            }
        } catch (Exception x) {
            x.printStackTrace();
            System.out.println("Error");
        }
        return array;
    }
    public boolean isAuthorized(String name, String pwd) {
        String sqlc = "select * from users where name = ?;";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlc)) {
                preparedStatement.setString(1, name);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next() && resultSet.getString(3).equals(pwd)) {
                    return true;
                }
                return false;
        } catch (Exception x) {
            x.printStackTrace();
            System.out.println("Error");
        }
        return false;
    }
}
