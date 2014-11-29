package ab.testing.router.domain;

public class UserGroup {

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
