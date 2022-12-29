package springbook.user.domain;

import lombok.Getter;
import lombok.Setter;
import springbook.user.dao.Level;

@Setter
@Getter
public class User {
    private String id;
    private String name;
    private String password;
    private Level level;
    private int login;
    private int recommend;
    private String email;
    
    public User(String id, String name, String password, Level level, int login, int recommend, String email) {
        super();
        this.id = id;
        this.name = name;
        this.password = password;
        this.level = level;
        this.login = login;
        this.recommend = recommend;
        this.email = email;
    }
    
    public void upgradeLevel() {
        Level nextLevel = level.nextLevel();
        if (nextLevel == null) {
            throw new IllegalArgumentException(this.level + "은 업그레이드가 불가능합니다.");
        }
        setLevel(nextLevel);
    }

    public User() {
    }
}
