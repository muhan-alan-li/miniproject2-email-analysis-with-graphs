package cpen221.mp2;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class Task3DWTests {

    private static DWInteractionGraph DWig1;
    private static DWInteractionGraph DWig2;
    private static DWInteractionGraph DWig3;
    private static DWInteractionGraph DWig4;
    private static DWInteractionGraph DWigFile1;
    private static DWInteractionGraph DWigFile2;
    private static DWInteractionGraph DWigFile3;
    private static DWInteractionGraph DWigFile4;


    @BeforeAll
    public static void setupTests() {
        DWig1 = new DWInteractionGraph("resources/Task3Transactions1.txt");
        DWig2 = new DWInteractionGraph("resources/Task3Transactions2.txt");
        DWig3 = new DWInteractionGraph("resources/invalid.txt");
        DWig4 = new DWInteractionGraph("resources/empty.txt");

        DWigFile1 = new DWInteractionGraph("resources/email-Eu-core-temporal-Dept1.txt");
        DWigFile2 = new DWInteractionGraph("resources/email-Eu-core-temporal-Dept2.txt");
        DWigFile3 = new DWInteractionGraph("resources/email-Eu-core-temporal-Dept3.txt");
        DWigFile4 = new DWInteractionGraph("resources/email-Eu-core-temporal-Dept4.txt");
    }

    @Test
    public void testBFSGraph1() {
        List<Integer> expected = Arrays.asList(1, 2, 3, 4, 6);
        Assertions.assertEquals(expected, DWig1.BFS(1, 6));
    }

    @Test
    public void testDFSGraph1() {
        List<Integer> expected = Arrays.asList(1, 2, 3, 4, 6);
        Assertions.assertEquals(expected, DWig1.DFS(1, 6));
    }

    @Test
    public void testBFSGraph2() {
        List<Integer> expected = Arrays.asList(1, 3, 5, 6, 4, 8, 7, 2, 9, 10);
        Assertions.assertEquals(expected, DWig2.BFS(1, 10));
    }

    @Test
    public void testDFSGraph2() {
        List<Integer> expected = Arrays.asList(1, 3, 4, 8, 5, 7, 2, 9, 10);
        Assertions.assertEquals(expected, DWig2.DFS(1, 10));
    }

    @Test
    public void testBFSNull1() {
        Assertions.assertEquals(null, DWig3.BFS(1, 10));
    }

    @Test
    public void testDFSNull1() {
        Assertions.assertEquals(null, DWig3.DFS(1, 10));
    }

    @Test
    public void testBFSNull2() {
        Assertions.assertEquals(null, DWig4.BFS(1, 10));
    }

    @Test
    public void testDFSNull2() {
        Assertions.assertEquals(null, DWig4.DFS(1, 10));
    }

    @Test
    public void testBFSGraphFile1() {
        List<Integer> expected = Arrays.asList(151, 26, 28, 30, 54, 81, 152, 223, 280, 1, 4, 9, 12, 18, 53);
        Assertions.assertEquals(expected, DWigFile1.BFS(151, 53));
    }

    @Test
    public void testDFSGraphFile1() {
        List<Integer> expected = Arrays.asList(151, 26, 1, 4, 12, 18, 9, 28, 30, 54, 64, 69, 121, 81, 142, 152, 176, 127, 53);
        Assertions.assertEquals(expected, DWigFile1.DFS(151, 53));
    }

    @Test
    public void testBFSGraphFile2() {
        List<Integer> expected = Arrays.asList(171, 0, 25, 28, 62, 70, 75);
        Assertions.assertEquals(expected, DWigFile2.BFS(171, 75));
    }

    @Test
    public void testDFSGraphFile2() {
        List<Integer> expected = Arrays.asList(171, 0, 28, 70, 98, 62, 145, 75);
        Assertions.assertEquals(expected, DWigFile2.DFS(171, 75));
    }

    @Test
    public void testBFSGraphFile3() {
        List<Integer> expected = Arrays.asList(69, 21, 23, 30, 35, 60, 66, 81, 5, 25, 44, 49, 55, 58, 62, 88, 1, 7, 8, 9, 11, 17, 29, 31, 33, 39, 46, 47, 54, 57, 59, 71, 78, 87, 3, 4, 12, 13, 26, 48, 63, 67, 68, 72, 73, 80, 84, 85, 0, 10, 14, 16);
        Assertions.assertEquals(expected, DWigFile3.BFS(69, 16));
    }

    @Test
    public void testDFSGraphFile3() {
        List<Integer> expected = Arrays.asList(69, 21, 5, 60, 1, 4, 3, 15, 70, 0, 19, 9, 8, 7, 23, 11, 12, 25, 2, 14, 10, 16);
        Assertions.assertEquals(expected, DWigFile3.DFS(69, 16));
    }

    @Test
    public void testBFSGraphFile4() {
        List<Integer> expected = Arrays.asList(125, 2, 20, 22, 25, 31, 35, 94, 95, 102, 116, 119, 132);
        Assertions.assertEquals(expected, DWigFile4.BFS(125, 132));
    }

    @Test
    public void testDFSGraphFile4() {
        List<Integer> expected = Arrays.asList(125, 2, 22, 4, 25, 12, 20, 31, 23, 35, 69, 10, 70, 94, 48, 102, 95, 90, 109, 116, 123, 131, 65, 140, 119, 132);
        Assertions.assertEquals(expected, DWigFile4.DFS(125, 132));
    }

}
