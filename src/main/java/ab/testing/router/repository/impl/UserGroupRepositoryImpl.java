package ab.testing.router.repository.impl;

import ab.testing.router.domain.UserGroup;
import ab.testing.router.repository.UserGroupRepository;
import org.springframework.stereotype.Repository;

@Repository
public class UserGroupRepositoryImpl implements UserGroupRepository {

    @Override
    public UserGroup getUserGroupByUserId(String userId) {
        return new UserGroup(userId, "fake_group");
    }
}
