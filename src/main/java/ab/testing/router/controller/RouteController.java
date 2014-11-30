package ab.testing.router.controller;

import ab.testing.router.exception.TooManyRequestsException;
import ab.testing.router.service.RouteService;
import com.google.common.util.concurrent.RateLimiter;
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

    private final RateLimiter limiter;

    private static Logger log = Logger.getLogger(RouteController.class);

    @Autowired
    public RouteController(RouteService routeService, RateLimiter rateLimiter) {
        this.routeService = routeService;
        this.limiter = rateLimiter;
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

        log.info("Looking for group name for given userId: " + userId);
        if (!limiter.tryAcquire()) {
            log.error("Too many simultaneous requests per second - rejected request for given userId: " + userId);
            throw new TooManyRequestsException();
        }

        return routeService.getGroupNameByUserId(userId);
    }
}
