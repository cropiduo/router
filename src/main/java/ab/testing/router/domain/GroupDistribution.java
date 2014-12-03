package ab.testing.router.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "distributions")
public class GroupDistribution {

    @Id
    private String groupName;
    private int distribution;

    public GroupDistribution(String groupName, int distribution) {
        this.groupName = groupName;
        this.distribution = distribution;
    }

    public String getGroupName() {
        return groupName;
    }

    public int getDistribution() {
        return distribution;
    }
}
