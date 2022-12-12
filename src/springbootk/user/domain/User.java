package springbootk.user.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class User {
    private String id;
    private String name; 
    private String password;
    private Level level;
    private int login;
    private int recommend;
    
    public User(String id, String name, String password, Level level, int login, int recommend) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.level = level;
        this.login = login;
        this.recommend = recommend;
    }

    public User() {
    }

    public void upgradeLevel() {
        Level nextLevel = this.level.nextLevel();
        if (nextLevel == null)
            throw new IllegalStateException(this.level + "은(는) 업그레이드가 불가능합니다.");
        else
            this.level = nextLevel;
    }
}
