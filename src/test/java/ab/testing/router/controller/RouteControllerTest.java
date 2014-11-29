package ab.testing.router.controller;

import ab.testing.router.domain.UserGroup;
import ab.testing.router.service.RouteService;
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

    @InjectMocks
    private RouteController routeController;
    @Mock
    private RouteService mockRouteService;
    @Mock
    private UserGroup mockUserGroup;

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
        when(mockRouteService.getUserGroupByUserId(userId)).thenReturn(mockUserGroup);
        when(mockUserGroup.getGroup()).thenReturn(GROUP_NAME);

        // when
        routeController.getUserGroupForUserId(userId);

        // then
        verify(mockRouteService).getUserGroupByUserId(userId);
        verify(mockUserGroup).getGroup();
    }
}
