package ab.testing.router.calculator.impl;

import ab.testing.router.configuration.RouteConfiguration;
import com.google.common.collect.ImmutableMap;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GroupCalculatorImplTest {

    public static final String USER_ID = "USER_ID";
    public static final String GROUP_WITH_SMALLER_EXPECTED_AMOUNT_OF_USERS = "A";
    public static final String GROUP_WITH_BIGGER_EXPECTED_AMOUNT_OF_USERS = "B";

    private GroupCalculatorImpl groupCalculator;

    @Mock
    private RouteConfiguration mockRouteConfiguration;
    private Map<String, AtomicInteger> config;

    @Before
    public void setUp() {
        config = ImmutableMap.of(
                GROUP_WITH_SMALLER_EXPECTED_AMOUNT_OF_USERS, new AtomicInteger(2),
                GROUP_WITH_BIGGER_EXPECTED_AMOUNT_OF_USERS, new AtomicInteger(3)
        );
        when(mockRouteConfiguration.getCurrentDistributionTemplate()).thenReturn(config);
        when(mockRouteConfiguration.getDenominator()).thenReturn(config.values().stream().mapToInt(AtomicInteger::get).sum());
        when(mockRouteConfiguration.getCurrentDistribution()).thenReturn(config.keySet().stream().collect(Collectors.toMap(k -> k, v -> new AtomicInteger())));

        groupCalculator = new GroupCalculatorImpl(mockRouteConfiguration);
    }

    @Test
    public void properlyChoosesGroupForTheFirstRequest() {
        // given

        // when
        String group = groupCalculator.calculateGroupNameForUser(USER_ID);

        // then
        Assert.assertEquals(GROUP_WITH_BIGGER_EXPECTED_AMOUNT_OF_USERS, group);
    }

    @Test
    public void properlyDistributesUsersForGivenConfig() {
        // given
        List<String> groups = new ArrayList<>();
        int denominator = config.values().stream().mapToInt(AtomicInteger::get).sum();

        // when
        for (int i = 0; i < denominator; i++) {
            groups.add(groupCalculator.calculateGroupNameForUser(USER_ID));
        }

        // then
        Assert.assertEquals(config.get(GROUP_WITH_SMALLER_EXPECTED_AMOUNT_OF_USERS).get(),
                groups.stream().filter(GROUP_WITH_SMALLER_EXPECTED_AMOUNT_OF_USERS::equals).count());
        Assert.assertEquals(config.get(GROUP_WITH_BIGGER_EXPECTED_AMOUNT_OF_USERS).get(),
                groups.stream().filter(GROUP_WITH_BIGGER_EXPECTED_AMOUNT_OF_USERS::equals).count());
    }
}
