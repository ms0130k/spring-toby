package springbook.user.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import springbook.user.domain.User;

public class UserDaoJdbc implements UserDao {
    private Map<String, String> sqlMap;
    private JdbcTemplate jdbcTemplate;
    private RowMapper<User> userMapper = new RowMapper<User>() {
        @Override
        public User mapRow(ResultSet rs, int i) throws SQLException {
            return new User(rs.getString("id"), rs.getString("name"), rs.getString("password"), Level.valueOf(rs.getInt("level")), rs.getInt("login"), rs.getInt("recommend"), rs.getString("email"));
        }
    };
    
    public void setSqlMap(Map<String, String> sqlMap) {
        this.sqlMap = sqlMap;
    }
    
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }
    
    public void add(User user) {
//        String sqlAdd = "INSERT INTO users (id, name, password, level, login, recommend, email) VALUES (?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sqlMap.get("add"), user.getId(), user.getName(), user.getPassword(), user.getLevel().intValue(), user.getLogin(), user.getRecommend(), user.getEmail());
    }
    
    public User get(String id) {
        RowMapper<User> rowMapper = userMapper;
        return jdbcTemplate.queryForObject(sqlMap.get("get"), new Object[] { id }, rowMapper);
    }
    
    public void deleteAll() {
        jdbcTemplate.update(sqlMap.get("deleteAll"));
    }
    
    public int getCount() {
        return jdbcTemplate.queryForInt(sqlMap.get("getCount"));
    }

    public List<User> getAll() {
        RowMapper<User> rowMapper = userMapper;
        return jdbcTemplate.query(sqlMap.get("getAll"), rowMapper);
    }

    @Override
    public void update(User user) {
        jdbcTemplate.update(sqlMap.get("update"), user.getName(), user.getPassword(), user.getLevel().intValue(), user.getLogin(), user.getRecommend(), user.getEmail(), user.getId());
    }
}