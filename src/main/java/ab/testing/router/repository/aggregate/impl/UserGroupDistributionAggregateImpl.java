package ab.testing.router.repository.aggregate.impl;

import ab.testing.router.domain.GroupDistribution;
import ab.testing.router.domain.UserGroup;
import ab.testing.router.repository.aggregate.UserGroupDistributionAggregate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;

@Repository
public class UserGroupDistributionAggregateImpl implements UserGroupDistributionAggregate {

    MongoTemplate mongoTemplate;

    @Autowired
    public UserGroupDistributionAggregateImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<GroupDistribution> getCurrentDistribution() {
        TypedAggregation<UserGroup> aggregation = newAggregation(UserGroup.class, group("groupName").count().as("distribution"));

        return mongoTemplate.aggregate(aggregation, GroupDistribution.class).getMappedResults();
    }
}
