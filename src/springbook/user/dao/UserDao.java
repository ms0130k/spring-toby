package springbook.user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import springbootk.user.domain.User;

public class UserDao {
    private JdbcContext jdbcContext;
    
    private DataSource dataSource;
    
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcContext = new JdbcContext();
        this.jdbcContext.setDataSource(dataSource);
    }

    public void add(final User user) throws SQLException {
        String query = "insert into users (id, name, password) values (?, ?, ?)"; 
        String id = user.getId();
        String name = user.getName();
        String password = user.getPassword();
        jdbcContext.executeSql(query, id, name, password);
    }

    public User get(String id) throws SQLException {
        Connection c = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        User user = null;
        try {
            c = dataSource.getConnection();
            ps = c.prepareStatement("SELECT * FROM users WHERE id = ?");
            ps.setString(1, id);
            rs = ps.executeQuery();
            
            user = null;
            if (rs.next()) {
                user = User.builder()
                        .id(rs.getString("id"))
                        .name(rs.getString("name"))
                        .password(rs.getString("password"))
                        .build();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null)
                    rs.close();
            } catch (SQLException e) {
            }
            try {
                if (ps != null)
                    ps.close();
            } catch (SQLException e) {
            }
            try {
                if (c != null)
                    c.close();
            } catch (SQLException e) {
            }
        }
        
        
        if (user == null) throw new IllegalArgumentException("없는 ID");

        return user;
    }
    
    public void deleteAll() throws SQLException {
        jdbcContext.executeSql("DELETE FROM users");
    }

    public int getCount() throws SQLException {
        Connection c = dataSource.getConnection();
        
        PreparedStatement ps = c.prepareStatement("SELECT count(*) FROM users");
        ResultSet rs = ps.executeQuery();
        rs.next();
        int count = rs.getInt(1);
        
        rs.close();
        ps.close();
        c.close();
        
        return count;
    }
}
