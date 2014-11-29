package ab.testing.router.service;

import ab.testing.router.domain.UserGroup;

public interface RouteService {
    UserGroup getUserGroupByUserId(String userId);
}
