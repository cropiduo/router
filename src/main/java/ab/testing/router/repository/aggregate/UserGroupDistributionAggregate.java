package ab.testing.router.repository.aggregate;

import ab.testing.router.domain.GroupDistribution;

import java.util.List;

public interface UserGroupDistributionAggregate {
    List<GroupDistribution> getCurrentDistribution();
}
