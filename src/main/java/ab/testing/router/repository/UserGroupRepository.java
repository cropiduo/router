package ab.testing.router.repository;

import ab.testing.router.domain.UserGroup;

public interface UserGroupRepository {
    UserGroup getUserGroupByUserId(String userId);
}
