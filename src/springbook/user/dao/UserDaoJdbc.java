package springbook.user.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import springbook.user.domain.User;
import springbook.user.sqlservice.SqlService;

@Repository
public class UserDaoJdbc implements UserDao {
    private JdbcTemplate jdbcTemplate;
    private RowMapper<User> userMapper = new RowMapper<User>() {
        @Override
        public User mapRow(ResultSet rs, int i) throws SQLException {
            return new User(rs.getString("id"), rs.getString("name"), rs.getString("password"), Level.valueOf(rs.getInt("level")), rs.getInt("login"), rs.getInt("recommend"), rs.getString("email"));
        }
    };
    
    @Autowired
    private SqlService sqlService;
    
    @Autowired
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }
    
    public void add(User user) {
        jdbcTemplate.update(sqlService.getSql("userAdd"), user.getId(), user.getName(), user.getPassword(), user.getLevel().intValue(), user.getLogin(), user.getRecommend(), user.getEmail());
    }
    
    public User get(String id) {
        RowMapper<User> rowMapper = userMapper;
        return jdbcTemplate.queryForObject(sqlService.getSql("userGet"), new Object[] { id }, rowMapper);
    }
    
    public void deleteAll() {
        jdbcTemplate.update(sqlService.getSql("userDeleteAll"));
    }
    
    public int getCount() {
        return jdbcTemplate.queryForInt(sqlService.getSql("userGetCount"));
    }

    public List<User> getAll() {
        RowMapper<User> rowMapper = userMapper;
        return jdbcTemplate.query(sqlService.getSql("userGetAll"), rowMapper);
    }

    @Override
    public void update(User user) {
        jdbcTemplate.update(sqlService.getSql("userUpdate"), user.getName(), user.getPassword(), user.getLevel().intValue(), user.getLogin(), user.getRecommend(), user.getEmail(), user.getId());
    }
}