package springbook.user.dao;

import java.sql.Connection;
import java.sql.SQLException;

public class CountingConnectionMaker implements ConnectionMaker {

    private ConnectionMaker realConnectionMaker; 
    private int count;
    
    public CountingConnectionMaker(ConnectionMaker connectionMaker) {
        this.realConnectionMaker = connectionMaker;
    }

    @Override
    public Connection createConnection() throws SQLException {
        count++;
        return realConnectionMaker.createConnection();
    }

    public int getCount() {
        return count;
    }
}
