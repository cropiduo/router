package ab.testing.router.controller;

import ab.testing.router.service.RouteService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@RestController
@RequestMapping("/route")
public class RouteController {

    private RouteService routeService;

    private static Logger log = Logger.getLogger(RouteController.class);

    @Autowired
    public RouteController(RouteService routeService) {
        this.routeService = routeService;
    }

    /**
     * returns group name which user with given ID should be or is already assigned to
     *
     * @param userId given user ID
     * @return group name
     */
    @RequestMapping(method = RequestMethod.GET)
    public String getUserGroupForUserId(@RequestParam(value = "userId", required = true) String userId) {
        checkArgument(isNotEmpty(userId));

        log.info("looking for group name for given userId: " + userId);

        return routeService.getGroupNameByUserId(userId);
    }
}
