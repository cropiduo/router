package ab.testing.router.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "usergroups")
public class UserGroup {

    @Id
    private String userId;
    private String group;

    public UserGroup(String userId, String group) {
        this.userId = userId;
        this.group = group;
    }

    public String getGroup() {
        return group;
    }
}
