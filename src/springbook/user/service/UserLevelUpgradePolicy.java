package springbook.user.service;

import springbootk.user.domain.User;

public interface UserLevelUpgradePolicy {
    boolean canUpgradeLevel(User user);
    void upgrade(User user);
}
