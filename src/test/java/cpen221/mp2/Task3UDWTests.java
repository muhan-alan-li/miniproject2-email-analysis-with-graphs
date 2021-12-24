package cpen221.mp2;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class Task3UDWTests {

    private static UDWInteractionGraph UDWig1;
    private static UDWInteractionGraph UDWig2;
    private static UDWInteractionGraph UDWig3;
    private static UDWInteractionGraph UDWig4;

    @BeforeAll
    public static void setupTests() {
        UDWig1 = new UDWInteractionGraph("resources/email-Eu-core-temporal-Dept1.txt");
        UDWig2 = new UDWInteractionGraph("resources/email-Eu-core-temporal-Dept2.txt");
        UDWig3 = new UDWInteractionGraph("resources/email-Eu-core-temporal-Dept3.txt");
        UDWig4 = new UDWInteractionGraph("resources/email-Eu-core-temporal-Dept4.txt");
    }

    @Test
    public void testNumComponent() {
        UDWInteractionGraph t = new UDWInteractionGraph("resources/Task3-components-test.txt");
        Assertions.assertEquals(1, t.NumberOfComponents());
    }

    @Test
    public void testNumComponent1() {
        UDWInteractionGraph t = new UDWInteractionGraph("resources/Task3-components-test1.txt");
        Assertions.assertEquals(2, t.NumberOfComponents());
    }

    @Test
    public void testPathExists() {
        UDWInteractionGraph t = new UDWInteractionGraph("resources/Task3-components-test.txt");
        Assertions.assertTrue(t.PathExists(1, 2));
        Assertions.assertTrue(t.PathExists(1, 3));
        Assertions.assertTrue(t.PathExists(1, 4));
        Assertions.assertTrue(t.PathExists(2, 3));
        Assertions.assertTrue(t.PathExists(2, 4));
        Assertions.assertTrue(t.PathExists(3, 4));
    }

    @Test
    public void testPathExists1() {
        UDWInteractionGraph t = new UDWInteractionGraph("resources/Task3-components-test1.txt");
        Assertions.assertTrue(t.PathExists(1, 2));
        Assertions.assertTrue(t.PathExists(3, 4));
        Assertions.assertFalse(t.PathExists(1, 4));
        Assertions.assertFalse(t.PathExists(2, 3));
    }

    @Test
    public void testSingleUser() {
        UDWInteractionGraph t = new UDWInteractionGraph("resources/Task3-components-test2.txt");
        Assertions.assertEquals(2, t.getEmailCount(1, 1));
        Assertions.assertEquals(1, t.NumberOfComponents());
        Assertions.assertTrue(t.PathExists(1, 1));
    }

    @Test
    public void testNoUser() {
        UDWInteractionGraph t = new UDWInteractionGraph("resources/Task3-components-test2.txt");
        UDWInteractionGraph t1 = new UDWInteractionGraph(t, List.of(2));
        Assertions.assertEquals(0, t1.NumberOfComponents());
    }

    @Test
    public void testNumberOfComponents1() {
        Assertions.assertEquals(9, UDWig1.NumberOfComponents());
    }

    @Test
    public void testNumberOfComponents2() {
        Assertions.assertEquals(6, UDWig2.NumberOfComponents());
    }

    @Test
    public void testNumberOfComponents3() {
        Assertions.assertEquals(1, UDWig3.NumberOfComponents());
    }

    @Test
    public void testNumberOfComponents4() {
        Assertions.assertEquals(6, UDWig4.NumberOfComponents());
    }

    @Test
    public void testPathExistFile1() {
        Assertions.assertEquals(true, UDWig1.PathExists(151, 53));
    }

    @Test
    public void testPathExistFile2() {
        Assertions.assertEquals(true, UDWig2.PathExists(171, 75));
    }

    @Test
    public void testPathExistFile3() {
        Assertions.assertEquals(true, UDWig3.PathExists(69, 16));
    }

    @Test
    public void testPathExistFile4() {
        Assertions.assertEquals(true, UDWig4.PathExists(125, 132));
    }

}
