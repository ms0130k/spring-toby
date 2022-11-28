package springbook.user.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import springbootk.user.domain.User;

public class UserDao_초난감 {
    public void add(User user) throws ClassNotFoundException, SQLException {
//        Class.forName("org.postgresql.Driver");
        Connection c = getConnection();
        
        PreparedStatement ps = c.prepareStatement("insert into users (id, name, password) values (?, ?, ?)");
        ps.setString(1, user.getId());
        ps.setString(2, user.getName());
        ps.setString(3, user.getPassword());
        
        ps.executeUpdate();
        
        ps.close();
        c.close();
    }
    
    public User get(String id) throws SQLException {
        Connection c = getConnection();
        PreparedStatement ps = c.prepareStatement("SELECT * FROM users WHERE id = ?");
        ps.setString(1, id);
        ResultSet rs = ps.executeQuery();
        rs.next();
        
        User user = User.builder()
                .id(rs.getString("id"))
                .name(rs.getString("name"))
                .password(rs.getString("password"))
                .build();
        
        rs.close();
        ps.close();
        c.close();
        
        return user;
    }
    

    private Connection getConnection() throws SQLException {
        Connection c = DriverManager.getConnection("jdbc:postgresql://130.162.157.215:5432/shock", "shock", "Kth973897G@#$");
        return c;
    }
}
