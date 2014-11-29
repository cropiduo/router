package ab.testing.router.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "usergroups")
public class UserGroup {

    @Id
    private String userId;
    private String groupName;

    public UserGroup(String userId, String groupName) {
        this.userId = userId;
        this.groupName = groupName;
    }

    public String getGroupName() {
        return groupName;
    }
}
