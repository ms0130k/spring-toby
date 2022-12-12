package springbook.user.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;

import springbootk.user.domain.Level;
import springbootk.user.domain.User;

public class UserDaoJdbc implements UserDao {
    private JdbcOperations jdbcOperations;
    private RowMapper<User> userMapper = new RowMapper<User>() {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            return User.builder()
                    .id(rs.getString("id"))
                    .name(rs.getString("name"))
                    .password(rs.getString("password"))
                    .level(Level.valueOf(rs.getInt("level")))
                    .recommend(rs.getInt("recommend"))
                    .login(rs.getInt("login"))
                    .build();
        }
    };
    
    public void setJdbcOperations(JdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }

    public void add(final User user) throws DuplicateKeyException {
        try {
            jdbcOperations.update("INSERT INTO users (id, name, password, level, recommend, login) VALUES (?, ?, ?, ?, ?, ?)", user.getId(), user.getName(), user.getPassword(), user.getLevel().intValue(), user.getRecommend(), user.getLogin());
        } catch (DuplicateKeyException e) {
            System.out.println("중복키 발생");
            throw e;
        }
    }

    public User get(String id) {
        return jdbcOperations.queryForObject("SELECT * FROM users WHERE id = ?", new Object[] { id }, userMapper);
    }
    
    public void deleteAll() {
        jdbcOperations.update("DELETE FROM users");
    }

    public int getCount() {
        return jdbcOperations.queryForInt("SELECT count(*) FROM users");
    }
    
    public List<User> getAll() {
        return jdbcOperations.query("SELECT * FROM users ORDER BY id", userMapper);
    }

    @Override
    public int update(User user) {
        return jdbcOperations.update("UPDATE users SET name = ?, level = ?, recommend = ?, login = ? WHERE id = ?" ,
                user.getName(), user.getLevel().intValue(), user.getRecommend(), user.getLogin(), user.getId());
    }
}
