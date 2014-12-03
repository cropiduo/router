package ab.testing.router.repository;

import ab.testing.router.domain.GroupDistribution;

import java.util.List;

public interface GroupDistributionRepository {

    List<GroupDistribution> findAll();

    void dropCollection();

    void insert(List<GroupDistribution> groupDistributionList);
}
