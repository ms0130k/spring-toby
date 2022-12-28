package springbook.user.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import springbook.user.dao.Level;

@Builder
@Setter
@Getter
public class User {
    String id;
    String name;
    String password;
    Level level;
    int login;
    int recommend;
}
