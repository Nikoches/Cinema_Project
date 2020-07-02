package Persistence;

import org.apache.commons.dbcp2.BasicDataSource;

import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class DbConnect {
    private static final BasicDataSource SOURCE = new BasicDataSource();
    private static final DbConnect INSTANCE = new DbConnect();

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
            SOURCE.setMinIdle(5);
            SOURCE.setMaxIdle(10);
            SOURCE.setMaxOpenPreparedStatements(100);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int[][] getPlaces() {
        Connection connection = null;
        try {
            connection = SOURCE.getConnection();
        }catch (SQLException ex){
            ex.getMessage();
        };
        String sqlc = "select * from place;";
        int[][] array = new int[3][3];
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlc); ResultSet resultSet = preparedStatement.executeQuery()) {
            int x = 0;
            while (resultSet.next()) {
                array[x][0] = resultSet.getBoolean(1) ? 1 : 0;
                array[x][1] = resultSet.getBoolean(2) ? 1 : 0;
                array[x][2] = resultSet.getBoolean(3) ? 1 : 0;
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
        try (Connection connection = SOURCE.getConnection();PreparedStatement preparedStatement = connection.prepareStatement(sqlc)) {
                preparedStatement.setString(1, name);
                ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next() && resultSet.getString(3).equals(pwd);
        } catch (Exception x) {
            x.printStackTrace();
            System.out.println("Error");
        }
        return false;
    }

    public boolean setPlace(int[] place,int id,int sum) {
        String row = "row" + place[0];
        String update = "update orders_c set " + row + " = true where line = ?;";
        String insert = "insert into orders_cin(place_n, sum, user_id) VALUES (?,?,?)";
        Savepoint savepointOne = null;
        try (Connection connection = SOURCE.getConnection();PreparedStatement preparedStatement = connection.prepareStatement(update);
             PreparedStatement preparedStatement1 = connection.prepareStatement(insert)) {
            savepointOne = connection.setSavepoint("SavepointOne");
            connection.setAutoCommit(false);
            Array array = connection.createArrayOf("int", new int[][]{place});
            preparedStatement.setInt(0, place[1]);
            preparedStatement1.setArray(0,array);
            preparedStatement1.setInt(1,sum);
            preparedStatement1.setInt(2,id);
            preparedStatement.execute();
            preparedStatement1.execute();
            connection.commit();
        } catch (Exception x) {
            x.printStackTrace();
            try(Connection connection = SOURCE.getConnection()) {
                connection.rollback(savepointOne);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            System.out.println("Error");
            return false;
        }
        return true;
    }
}
