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
            return new User(rs.getString("id"), rs.getString("name"), rs.getString("password"), Level.valueOf(rs.getInt("level")), rs.getInt("login"), rs.getInt("recommend"), rs.getString("email"));
        }
    };
    
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }
    
    public void add(User user) {
        jdbcTemplate.update("INSERT INTO users (id, name, password, level, login, recommend, email) VALUES (?, ?, ?, ?, ?, ?, ?)", user.getId(), user.getName(), user.getPassword(), user.getLevel().intValue(), user.getLogin(), user.getRecommend(), user.getEmail());
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

    @Override
    public void update(User user) {
        String sql = "UPDATE users SET name = ?, password = ?, level = ?, login = ?, recommend = ?, email = ? WHERE id = ?";
        jdbcTemplate.update(sql, user.getName(), user.getPassword(), user.getLevel().intValue(), user.getLogin(), user.getRecommend(), user.getId(), user.getEmail());
    }
}