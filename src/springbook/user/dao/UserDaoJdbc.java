package springbook.user.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import springbook.user.domain.User;

public class UserDaoJdbc implements UserDao {
    private JdbcTemplate jdbcTemplate;
    private RowMapper<User> userMapper = new RowMapper<User>() {
        @Override
        public User mapRow(ResultSet rs, int i) throws SQLException {
            return User.builder()
                    .id(rs.getString("id"))
                    .name(rs.getString("name"))
                    .password(rs.getString("password"))
                    .level(Level.valueOf(rs.getInt("level")))
                    .login(rs.getInt("login"))
                    .recommend(rs.getInt("recommend"))
                    .build();
        }
    };
    
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }
    
    public void add(User user) {
        jdbcTemplate.update("INSERT INTO users (id, name, password, level, login, recommend) VALUES (?, ?, ?, ?, ?, ?)", user.getId(), user.getName(), user.getPassword(), user.getLevel().intValue(), user.getLogin(), user.getRecommend());
    }
    
    public User get(String id) {
        RowMapper<User> rowMapper = userMapper;
        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE id = ?", new Object[] { id }, rowMapper);
    }
    
    public void deleteAll() {
        jdbcTemplate.update("DELETE FROM users");
    }
    
    public int getCount() {
        return jdbcTemplate.queryForInt("SELECT COUNT(*) FROM users");
    }

    public List<User> getAll() {
        RowMapper<User> rowMapper = userMapper;
        return jdbcTemplate.query("SELECT * FROM users", rowMapper);
    }
}