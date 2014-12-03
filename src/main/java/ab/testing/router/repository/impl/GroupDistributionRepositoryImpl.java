package ab.testing.router.repository.impl;

import ab.testing.router.domain.GroupDistribution;
import ab.testing.router.repository.GroupDistributionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class GroupDistributionRepositoryImpl implements GroupDistributionRepository {

    private MongoTemplate mongoTemplate;

    @Autowired
    public GroupDistributionRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<GroupDistribution> findAll() {
        return mongoTemplate.findAll(GroupDistribution.class);
    }

    @Override
    public void dropCollection() {
        mongoTemplate.dropCollection(GroupDistribution.class);
    }

    @Override
    public void insert(List<GroupDistribution> groupDistributionList) {
        mongoTemplate.insert(groupDistributionList, GroupDistribution.class);
    }
}
