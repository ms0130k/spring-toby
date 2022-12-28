package springbook.user.dao;

import java.sql.Connection;
import java.sql.SQLException;

public class CountingConnectionMaker implements ConnectionMaker {
    private int count;
    private ConnectionMaker connectionMaker;
    
    public CountingConnectionMaker(ConnectionMaker connectionMaker) {
        this.connectionMaker = connectionMaker;
    }

    @Override
    public Connection getConnection() throws SQLException {
        count++;
        return connectionMaker.getConnection();
    }

    public int getCount() {
        return count;
    }
}
