package ab.testing.router.calculator.impl;

import ab.testing.router.calculator.GroupCalculator;
import ab.testing.router.configuration.RouteConfiguration;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public final class GroupCalculatorImpl implements GroupCalculator {

    private final Map<String, AtomicInteger> currentDistribution;
    private final Map<String, Double> expectedDistribution;
    private final AtomicInteger usersCounter;

    @Autowired
    public GroupCalculatorImpl(RouteConfiguration routeConfiguration) {
        currentDistribution = routeConfiguration.getCurrentDistribution();
        expectedDistribution = ImmutableMap.copyOf(getPercentageDistribution(routeConfiguration.getCurrentDistributionTemplate(), routeConfiguration.getDenominator()));
        usersCounter = new AtomicInteger(currentDistribution.values().stream().mapToInt(AtomicInteger::get).sum());
    }

    @Override
    public synchronized String calculateGroupNameForUser(String userId) {
        Map<String, Double> currentPercentageDistribution = getPercentageDistribution(currentDistribution, usersCounter.get());
        Map<String, Double> difference = Stream.of(expectedDistribution, currentPercentageDistribution).map(Map::entrySet).flatMap(Collection::stream).collect(
                Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> a - b));
        String chosenGroup = difference.entrySet().stream().max(Map.Entry.comparingByValue()).get().getKey();
        currentDistribution.get(chosenGroup).getAndIncrement();
        usersCounter.getAndIncrement();

        return chosenGroup;
    }

    private Map<String, Double> getPercentageDistribution(Map<String, AtomicInteger> distribution, int denominator) {
        return distribution.keySet().stream().collect(Collectors.toMap(k -> k , v -> (double) distribution.get(v).get() / (denominator > 0 ? denominator : 1)));
    }
}
