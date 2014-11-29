package ab.testing.router.repository.impl;

import ab.testing.router.domain.UserGroup;
import ab.testing.router.repository.UserGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Repository
public class UserGroupRepositoryImpl implements UserGroupRepository {

    private MongoTemplate mongoTemplate;

    @Autowired
    public UserGroupRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public UserGroup getUserGroupByUserId(String userId) {
        return mongoTemplate.findOne(query(where("userId").is(userId)), UserGroup.class);
    }
}
