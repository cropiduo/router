package ab.testing.router.configuration.impl;

import ab.testing.router.configuration.RouteConfiguration;
import ab.testing.router.domain.GroupDistribution;
import ab.testing.router.io.reader.reader.ConfigFileReader;
import ab.testing.router.repository.GroupDistributionRepository;
import ab.testing.router.repository.UserGroupRepository;
import ab.testing.router.repository.aggregate.UserGroupDistributionAggregate;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
public final class RouteConfigurationImpl implements RouteConfiguration {

    private static Logger log = Logger.getLogger(RouteConfigurationImpl.class);

    private final Map<String, AtomicInteger> currentDistributionTemplate;
    private final Map<String, AtomicInteger> currentDistribution;
    private final int denominator;

    @Autowired
    public RouteConfigurationImpl(UserGroupRepository userGroupRepository, GroupDistributionRepository groupDistributionRepository,
                                  UserGroupDistributionAggregate aggregate, ConfigFileReader configFileReader) {

        currentDistributionTemplate = configFileReader.readConfigFromFile();

        Map<String, AtomicInteger> previousDistributionTemplate = CollectionsManipulation.asMap(groupDistributionRepository.findAll());

        if (CollectionsManipulation.haveSameContent(previousDistributionTemplate, currentDistributionTemplate)) {
            log.info("Loading previous user distribution from DB.");
            currentDistribution = loadPreviousUserDistribution(aggregate);
        } else {
            log.info("Overwriting previous user distribution with new one.");
            currentDistribution = overwritePreviousUserDistribution(userGroupRepository, groupDistributionRepository);
        }

        denominator = currentDistributionTemplate.values().stream().mapToInt(AtomicInteger::get).sum();
    }

    private Map<String, AtomicInteger> loadPreviousUserDistribution(UserGroupDistributionAggregate aggregate) {
        List<GroupDistribution> distributions = aggregate.getCurrentDistribution();

        Map<String, AtomicInteger> result;

        if (distributions.size() != currentDistributionTemplate.size()) {
            result = currentDistributionTemplate.keySet().stream().collect(Collectors.toMap(k -> k, v -> new AtomicInteger()));
            for (GroupDistribution distribution : distributions) {
                result.get(distribution.getGroupName()).set(distribution.getDistribution());
            }
        } else {
            result = CollectionsManipulation.asMap(distributions);
        }

        return result;
    }

    private Map<String, AtomicInteger> overwritePreviousUserDistribution(UserGroupRepository userGroupRepository, GroupDistributionRepository groupDistributionRepository) {
        Map<String, AtomicInteger> result = currentDistributionTemplate.keySet().stream().collect(Collectors.toMap(k -> k, v -> new AtomicInteger()));
        userGroupRepository.dropCollection();
        groupDistributionRepository.dropCollection();
        groupDistributionRepository.insert(CollectionsManipulation.asList(currentDistributionTemplate));

        return result;
    }

    @Override
    public Map<String, AtomicInteger> getCurrentDistributionTemplate() {
        return ImmutableMap.copyOf(currentDistributionTemplate);
    }

    @Override
    public int getDenominator() {
        return denominator;
    }

    @Override
    public Map<String, AtomicInteger> getCurrentDistribution() {
        return Maps.newHashMap(ImmutableMap.copyOf(currentDistribution));
    }

    private static class CollectionsManipulation {
        private static boolean haveSameContent(Map<String, AtomicInteger> previousDistributionTemplate, Map<String, AtomicInteger> currentDistributionTemplate) {
            if (previousDistributionTemplate.size() != currentDistributionTemplate.size()) {
                return false;
            }

            for (String key : previousDistributionTemplate.keySet()) {
                if (currentDistributionTemplate.get(key) == null
                        || previousDistributionTemplate.get(key).get() != currentDistributionTemplate.get(key).get()) {
                    return false;
                }
            }

            return true;
        }

        private static List<GroupDistribution> asList(Map<String, AtomicInteger> map) {
            return map.entrySet().stream().map(k -> new GroupDistribution(k.getKey(), k.getValue().get())).collect(Collectors.toList());
        }

        private static Map<String, AtomicInteger> asMap(List<GroupDistribution> list) {
            return list.stream().collect(Collectors.toMap(GroupDistribution::getGroupName, d -> new AtomicInteger(d.getDistribution())));
        }
    }
}
