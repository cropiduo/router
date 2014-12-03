package ab.testing.router.controller;

import ab.testing.router.exception.TooManyRequestsException;
import ab.testing.router.service.RouteService;
import com.google.common.util.concurrent.RateLimiter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RouteControllerTest {

    public static final String USER_ID = "USER_ID";
    public static final String GROUP_NAME = "GROUP_NAME";

    @Mock
    private RateLimiter mockRateLimiter;
    @Mock
    private RouteService mockRouteService;
    @InjectMocks
    private RouteController routeController;

    @Test(expected = IllegalArgumentException.class)
    public void throwsIllegalArgumentExceptionWhenNoParamsGiven() {
        // given
        String userId = null;

        // when
        routeController.getUserGroupForUserId(userId);

        // then
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwsIllegalArgumentExceptionWhenEmptyParamGiven() {
        // given
        String userId = "";

        // when
        routeController.getUserGroupForUserId(userId);

        // then
    }

    @Test
    public void returnsGroupNameForGivenProperUserId() {
        // given
        String userId = USER_ID;
        when(mockRouteService.getGroupNameByUserId(userId)).thenReturn(GROUP_NAME);
        when(mockRateLimiter.tryAcquire()).thenReturn(true);

        // when
        routeController.getUserGroupForUserId(userId);

        // then
        verify(mockRateLimiter).tryAcquire();
        verify(mockRouteService).getGroupNameByUserId(userId);
    }

    @Test(expected = TooManyRequestsException.class)
    public void throwsTooManyRequestsExceptionWhenRequestsLimitReached() {
        // given
        String userId = USER_ID;
        when(mockRouteService.getGroupNameByUserId(userId)).thenReturn(GROUP_NAME);
        when(mockRateLimiter.tryAcquire()).thenReturn(false);

        // when
        routeController.getUserGroupForUserId(userId);

        // then
        verify(mockRateLimiter).tryAcquire();
    }
}
