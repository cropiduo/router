package ab.testing.router.service.impl;

import ab.testing.router.calculator.GroupCalculator;
import ab.testing.router.domain.UserGroup;
import ab.testing.router.repository.UserGroupRepository;
import ab.testing.router.service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RouteServiceImpl implements RouteService {

    private UserGroupRepository userGroupRepository;
    private GroupCalculator groupCalculator;

    @Autowired
    public RouteServiceImpl(UserGroupRepository userGroupRepository, GroupCalculator groupCalculator) {
        this.userGroupRepository = userGroupRepository;
        this.groupCalculator = groupCalculator;
    }

    @Override
    public String getGroupNameByUserId(String userId) {
        UserGroup userGroup = userGroupRepository.getUserGroupByUserId(userId);
        String groupName;

        if (userGroup == null) {
            synchronized (this) {
                userGroup = userGroupRepository.getUserGroupByUserId(userId);
                if (userGroup != null) {
                    groupName = userGroup.getGroupName();
                } else {
                    groupName = groupCalculator.calculateGroupNameForUser(userId);
                }
            }
        } else {
            groupName = userGroup.getGroupName();
        }

        return groupName;
    }
}
