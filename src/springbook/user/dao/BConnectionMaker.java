package springbook.user.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class BConnectionMaker implements ConnectionMaker {

    @Override
    public Connection createConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:postgresql://130.162.157.215:5432/shock", "shock", "Kth973897G@#$");
    }

}
