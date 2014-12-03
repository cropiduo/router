package ab.testing.router.repository;

import ab.testing.router.domain.UserGroup;

public interface UserGroupRepository {
    UserGroup getUserGroupByUserId(String userId);

    void saveUserGroup(String userId, String groupName);

    void dropCollection();
}
