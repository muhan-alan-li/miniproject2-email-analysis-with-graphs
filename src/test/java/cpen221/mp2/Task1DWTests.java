package cpen221.mp2;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Task1DWTests {

    private static DWInteractionGraph dwig;
    private static DWInteractionGraph dwig1;
    private static DWInteractionGraph dwig2;
    private static DWInteractionGraph dwig3;
    private static DWInteractionGraph emptyDwig;

    private static DWInteractionGraph empty1;
    private static DWInteractionGraph empty2;
    private static DWInteractionGraph empty3;

    @BeforeAll
    public static void setupTests() {
        dwig = new DWInteractionGraph("resources/Task1-2Transactions.txt");
        dwig1 = new DWInteractionGraph(dwig, new int[]{3, 9});
        dwig2 = new DWInteractionGraph(dwig, Arrays.asList(2, 3, 4));
        dwig3 = new DWInteractionGraph("resources/Task1-2Transactions.txt", new int[]{3, 9});
        emptyDwig = new DWInteractionGraph("resources/empty.txt");

        empty1 = new DWInteractionGraph("resources/empty.txt");
        empty2 = new DWInteractionGraph(empty1,new int[]{1,2});
        empty3 = new DWInteractionGraph(empty1,Arrays.asList(1,2));
    }

    @Test
    public void test1GetUserIDsBase() {
        Set<Integer> expected = new HashSet<>(Arrays.asList(0, 1, 2, 3, 4, 8));
        Assertions.assertEquals(expected, dwig.getUserIDs());
        //System.out.println(dwig.adjacencyList);
    }

    @Test
    public void test1GetUserIDsGraph1() {
        Set<Integer> expected = new HashSet<>(Arrays.asList(0, 1, 4, 8));
        Assertions.assertEquals(expected, dwig1.getUserIDs());
        //System.out.println(dwig1.adjacencyList);
    }

    @Test
    public void test1GetUserIDsGraph2() {
        //System.out.println(dwig2.adjacencyList);
        Set<Integer> expected = new HashSet<>(Arrays.asList(2, 3, 4, 8));
        Assertions.assertEquals(expected, dwig2.getUserIDs());
    }

    @Test
    public void test1GetEmailCountBase() {
        Assertions.assertEquals(2, dwig.getEmailCount(2, 3));
        Assertions.assertEquals(0, dwig.getEmailCount(8, 4));
    }

    @Test
    public void test1GetEmailCountGraph1() {
        Assertions.assertEquals(1, dwig1.getEmailCount(1, 0));
        Assertions.assertEquals(1, dwig1.getEmailCount(8, 0));

    }

    @Test
    public void test1GetEmailCountGraph2() {
        Assertions.assertEquals(1, dwig2.getEmailCount(4, 8));
        Assertions.assertEquals(2, dwig2.getEmailCount(2, 3));

    }
    @Test
    public void testConstructor2() {
        Assertions.assertEquals(0, dwig3.getEmailCount(2,3));
        Assertions.assertEquals(1, dwig3.getEmailCount(0, 0));
    }

    @Test
    public void testMediumFile(){
        DWInteractionGraph dwigLarge = new DWInteractionGraph("resources/email-EU-core-temporal-Dept3.txt");
        System.out.println(dwigLarge.MaxBreachedUserCount(10));
    }

    @Test
    public void testEmptyInputFile(){
        Assertions.assertEquals(new HashSet<>(), emptyDwig.getUserIDs());
    }

    @Test
    public void testNoActiveUser() {
        Assertions.assertEquals(-1,emptyDwig.NthMostActiveUser(1,SendOrReceive.SEND));

    }

    @Test
    public void testEmptyEverything(){

        Assertions.assertEquals(0, empty1.getEmailCount(2, 3));
        Assertions.assertEquals(0, empty2.getEmailCount(2, 3));
        Assertions.assertEquals(0, empty3.getEmailCount(2, 3));

        Assertions.assertEquals(new HashSet<Integer>(), empty1.getUserIDs());
        Assertions.assertEquals(new HashSet<Integer>(), empty2.getUserIDs());
        Assertions.assertEquals(new HashSet<Integer>(), empty3.getUserIDs());

        Assertions.assertEquals(-1, empty1.NthMostActiveUser(1,SendOrReceive.SEND));
        Assertions.assertEquals(-1, empty1.NthMostActiveUser(1,SendOrReceive.RECEIVE));
        Assertions.assertEquals(-1, empty2.NthMostActiveUser(1,SendOrReceive.SEND));
        Assertions.assertEquals(-1, empty2.NthMostActiveUser(1,SendOrReceive.RECEIVE));
        Assertions.assertEquals(-1, empty3.NthMostActiveUser(1,SendOrReceive.SEND));
        Assertions.assertEquals(-1, empty3.NthMostActiveUser(1,SendOrReceive.RECEIVE));

        Assertions.assertEquals(0, empty1.ReportActivityInTimeWindow(new int[]{1,2})[0]);
        Assertions.assertEquals(0, empty1.ReportActivityInTimeWindow(new int[]{1,2})[1]);
        Assertions.assertEquals(0, empty2.ReportActivityInTimeWindow(new int[]{1,2})[0]);
        Assertions.assertEquals(0, empty2.ReportActivityInTimeWindow(new int[]{1,2})[1]);
        Assertions.assertEquals(0, empty3.ReportActivityInTimeWindow(new int[]{1,2})[0]);
        Assertions.assertEquals(0, empty3.ReportActivityInTimeWindow(new int[]{1,2})[1]);

        Assertions.assertEquals(0, empty1.ReportOnUser(1)[0]);
        Assertions.assertEquals(0, empty1.ReportOnUser(1)[1]);
        Assertions.assertEquals(0, empty2.ReportOnUser(1)[0]);
        Assertions.assertEquals(0, empty2.ReportOnUser(1)[1]);
        Assertions.assertEquals(0, empty3.ReportOnUser(1)[0]);
        Assertions.assertEquals(0, empty3.ReportOnUser(1)[1]);

        Assertions.assertEquals(0, empty1.MaxBreachedUserCount(1));
        Assertions.assertEquals(0, empty2.MaxBreachedUserCount(1));
        Assertions.assertEquals(0, empty3.MaxBreachedUserCount(1));
    }


}
