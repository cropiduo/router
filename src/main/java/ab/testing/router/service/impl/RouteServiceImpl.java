package ab.testing.router.service.impl;

import ab.testing.router.configuration.RouteConfiguration;
import ab.testing.router.domain.UserGroup;
import ab.testing.router.repository.UserGroupRepository;
import ab.testing.router.service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RouteServiceImpl implements RouteService {

    private UserGroupRepository userGroupRepository;
    private RouteConfiguration routeConfiguration;

    @Autowired
    public RouteServiceImpl(UserGroupRepository userGroupRepository, RouteConfiguration routeConfiguration) {
        this.userGroupRepository = userGroupRepository;
        this.routeConfiguration = routeConfiguration;
    }

    @Override
    public UserGroup getUserGroupByUserId(String userId) {
        return userGroupRepository.getUserGroupByUserId(userId);
    }
}
