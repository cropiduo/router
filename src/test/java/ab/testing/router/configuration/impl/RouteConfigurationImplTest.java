package ab.testing.router.configuration.impl;

import ab.testing.router.domain.GroupDistribution;
import ab.testing.router.io.reader.reader.ConfigFileReader;
import ab.testing.router.repository.GroupDistributionRepository;
import ab.testing.router.repository.UserGroupRepository;
import ab.testing.router.repository.aggregate.UserGroupDistributionAggregate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RouteConfigurationImplTest {

    public static final String GROUP_A = "A";
    public static final String GROUP_B = "B";
    public static final String GROUP_C = "C";

    @Mock
    private UserGroupRepository mockUserGroupRepository;
    @Mock
    private GroupDistributionRepository mockGroupDistributionRepository;
    @Mock
    private UserGroupDistributionAggregate mockAggregate;
    @Mock
    private ConfigFileReader mockConfigFileReader;
    @InjectMocks
    private RouteConfigurationImpl routeConfiguration;

    private List<GroupDistribution> previousConfigTheSame = ImmutableList.of(
            new GroupDistribution(GROUP_A, 2),
            new GroupDistribution(GROUP_B, 3),
            new GroupDistribution(GROUP_C, 3)
    );

    private List<GroupDistribution> previousConfigDifferent = ImmutableList.of(
            new GroupDistribution(GROUP_A, 2),
            new GroupDistribution(GROUP_B, 3)
    );

    private List<Integer> expectedDistributionWhenConfigChanged = ImmutableList.of(
            0,
            0,
            0
    );
    private List<Integer> expectedDistributionWhenLackOfUserGroupsInDb = ImmutableList.of(
            0,
            0,
            0
    );

    private Map<String, AtomicInteger> expectedConfig = ImmutableMap.of(
            GROUP_A, new AtomicInteger(2),
            GROUP_B, new AtomicInteger(3),
            GROUP_C, new AtomicInteger(3)
    );

    @Before
    public void setUp() {
        when(mockConfigFileReader.readConfigFromFile()).thenReturn(expectedConfig);
    }

    @Test
    public void loadsDistributionFromPreviousRunIfConfigDidNotChange() {
        // given
        when(mockGroupDistributionRepository.findAll()).thenReturn(previousConfigTheSame);
        when(mockAggregate.getCurrentDistribution()).thenReturn(previousConfigTheSame);

        // when
        routeConfiguration = new RouteConfigurationImpl(mockUserGroupRepository, mockGroupDistributionRepository, mockAggregate, mockConfigFileReader);

        // then
        verify(mockAggregate, atLeast(1)).getCurrentDistribution();


        Assert.assertEquals(expectedConfig.keySet(), routeConfiguration.getCurrentDistribution().keySet());

        List<Integer> expectedValues = expectedConfig.values().stream().map(AtomicInteger::get).collect(Collectors.toList());
        List<Integer> currentDistributionValues = routeConfiguration.getCurrentDistribution().values().stream().map(AtomicInteger::get).collect(Collectors.toList());

        Assert.assertEquals(expectedValues, currentDistributionValues);
    }

    @Test
    public void properlyInitializesCurrentDistributionWhenLackOfUserGroupsInDb() {
        // given
        when(mockGroupDistributionRepository.findAll()).thenReturn(previousConfigTheSame);
        when(mockAggregate.getCurrentDistribution()).thenReturn(Collections.<GroupDistribution>emptyList());

        // when
        routeConfiguration = new RouteConfigurationImpl(mockUserGroupRepository, mockGroupDistributionRepository, mockAggregate, mockConfigFileReader);

        // then
        verify(mockAggregate, atLeast(1)).getCurrentDistribution();


        Assert.assertEquals(expectedConfig.keySet(), routeConfiguration.getCurrentDistribution().keySet());

        List<Integer> currentDistributionValues = routeConfiguration.getCurrentDistribution().values().stream().map(AtomicInteger::get).collect(Collectors.toList());

        Assert.assertEquals(expectedDistributionWhenLackOfUserGroupsInDb, currentDistributionValues);
    }

    @Test
    public void overwritesDistributionWhenConfigChanged() {
        // given
        when(mockGroupDistributionRepository.findAll()).thenReturn(previousConfigDifferent);

        // when
        routeConfiguration = new RouteConfigurationImpl(mockUserGroupRepository, mockGroupDistributionRepository, mockAggregate, mockConfigFileReader);

        // then
        verify(mockUserGroupRepository, atLeast(1)).dropCollection();
        verify(mockGroupDistributionRepository, atLeast(1)).dropCollection();
        verify(mockGroupDistributionRepository, atLeast(1)).insert(anyListOf(GroupDistribution.class));

        List<Integer> currentDistributionValues = routeConfiguration.getCurrentDistribution().values().stream().map(AtomicInteger::get).collect(Collectors.toList());

        Assert.assertEquals(expectedDistributionWhenConfigChanged, currentDistributionValues);
    }
}
