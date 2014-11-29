package ab.testing.router.calculator;

public interface GroupCalculator {

    /**
     * calculates the destination group name according to current user distribution
     *
     * @param userId given user ID
     * @return group name
     */
    String calculateGroupNameForUser(String userId);
}
