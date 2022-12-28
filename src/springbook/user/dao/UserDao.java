package springbook.user.dao;

import java.util.List;

import javax.sql.DataSource;

import springbook.user.domain.User;

public interface UserDao {
    public void setDataSource(DataSource dataSource);

    public void add(User user);

    public User get(String id);

    public void deleteAll();

    public int getCount();

    public List<User> getAll();
}
