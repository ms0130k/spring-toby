package springbootk.user.domain;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

public class UserTest {
    User user;
    @Before
    public void setup() {
        user = new User();
    }

    @Test
    public void builder로_set해도_setter를_통한다_는_거짓이나_커스터마이징을_다_할_수_있구나() {
//        System.out.println(User.builder()
//            .name("이름")
//            .id("id")
//            .build().getId());
    }
    
    @Test
    public void 롬복위에_setter() {
//        new User().setName("이름");
        System.out.println(Level.BASIC.toString());
        System.out.println(new Date());
    }
    
    @Test
    public void upgradeLevel() {
        Level[] levels = Level.values();
        for (Level level : levels) {
            if (level.nextLevel() == null) continue;
            user.setLevel(level);
            user.upgradeLevel();
            assertThat(user.getLevel(), is(level.nextLevel()));
        }
    }
    
    @Test(expected = IllegalStateException.class)
    public void cannotUpgradeLevel() {
        Level[] levels = Level.values();
        for (Level level : levels) {
            if (level.nextLevel() != null) continue;
            user.setLevel(level);
            user.upgradeLevel();
        }
    }
}
