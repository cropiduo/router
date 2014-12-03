package ab.testing.router.configuration;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public interface RouteConfiguration {
    Map<String, AtomicInteger> getCurrentDistributionTemplate();

    int getDenominator();

    Map<String, AtomicInteger> getCurrentDistribution();
}
