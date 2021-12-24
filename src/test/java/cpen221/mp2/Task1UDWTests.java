package cpen221.mp2;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class Task1UDWTests {
    private static UDWInteractionGraph testGraphBase;
    private static UDWInteractionGraph testGraph1;
    private static UDWInteractionGraph testGraph2;
    private static UDWInteractionGraph empty1;
    private static UDWInteractionGraph empty2;
    private static UDWInteractionGraph empty3;
    private static UDWInteractionGraph empty4;

    private static DWInteractionGraph emptyDwig;


    @BeforeAll
    public static void setupTests() {
        testGraphBase = new UDWInteractionGraph("resources/Task1-2UDWTransactions.txt");
        testGraph1 = new UDWInteractionGraph(testGraphBase, new int[] {0, 9});
        testGraph2 = new UDWInteractionGraph(testGraphBase, new int[] {10, 11});
        empty1 = new UDWInteractionGraph("resources/empty.txt");
        empty2 = new UDWInteractionGraph(empty1, new int[] {1, 2});
        empty3 = new UDWInteractionGraph(empty1, Arrays.asList(1, 2));
        emptyDwig = new DWInteractionGraph("resources/empty.txt");
        empty4 = new UDWInteractionGraph(emptyDwig);

    }

    @Test
    public void testGetUserIds() {
        Assertions
            .assertEquals(new HashSet<>(Arrays.asList(0, 1, 2, 3)), testGraphBase.getUserIDs());
    }

    @Test
    public void testGetUserIds1() {
        Assertions.assertEquals(new HashSet<>(Arrays.asList(1, 2, 3)), testGraph2.getUserIDs());
    }

    @Test
    public void testGetEmailCount() {
        Assertions.assertEquals(2, testGraphBase.getEmailCount(1, 0));
        Assertions.assertEquals(2, testGraphBase.getEmailCount(0, 1));
        Assertions.assertEquals(0, testGraphBase.getEmailCount(5, 6));

    }

    @Test
    public void testGetEmailCount1() {
        Assertions.assertEquals(2, testGraph1.getEmailCount(1, 0));
        Assertions.assertEquals(2, testGraph1.getEmailCount(0, 3));
    }

    @Test
    public void testGetEmailCount2() {
        Assertions.assertEquals(0, testGraph2.getEmailCount(1, 0));
        Assertions.assertEquals(1, testGraph2.getEmailCount(1, 3));
    }

    @Test
    public void testUserConstructor() {
        List<Integer> userFilter = Arrays.asList(0, 1);
        UDWInteractionGraph t = new UDWInteractionGraph(testGraphBase, userFilter);
        Assertions.assertEquals(new HashSet<>(Arrays.asList(0, 1, 2, 3)), t.getUserIDs());

        Assertions.assertEquals(2, t.getEmailCount(0, 1));
        Assertions.assertEquals(2, t.getEmailCount(0, 3));
    }

    @Test
    public void testConstructionFromDW() {
        DWInteractionGraph dwig = new DWInteractionGraph("resources/Task1-2UDWTransactions.txt");
        UDWInteractionGraph udwig = new UDWInteractionGraph(dwig);
        Assertions.assertEquals(new HashSet<>(Arrays.asList(0, 1, 2, 3)), udwig.getUserIDs());
        Assertions.assertEquals(2, udwig.getEmailCount(2, 3));
    }

    @Test
    public void testConstructionFromDW1() {
        DWInteractionGraph dwig = new DWInteractionGraph("resources/Task1-2Transactions.txt");
        UDWInteractionGraph udwig = new UDWInteractionGraph(dwig);
        Assertions.assertEquals(new HashSet<>(Arrays.asList(0, 1, 2, 3, 4, 8)), udwig.getUserIDs());
        Assertions.assertEquals(2, udwig.getEmailCount(2, 3));
    }

    @Test
    public void testEmptyEverything() {
        Assertions.assertEquals(0, empty1.getEmailCount(2, 3));
        Assertions.assertEquals(0, empty2.getEmailCount(2, 3));
        Assertions.assertEquals(0, empty3.getEmailCount(2, 3));
        Assertions.assertEquals(0, empty4.getEmailCount(2, 3));


        Assertions.assertEquals(new HashSet<Integer>(), empty1.getUserIDs());
        Assertions.assertEquals(new HashSet<Integer>(), empty2.getUserIDs());
        Assertions.assertEquals(new HashSet<Integer>(), empty3.getUserIDs());
        Assertions.assertEquals(new HashSet<Integer>(), empty4.getUserIDs());

        Assertions.assertEquals(0, empty1.NthMostActiveUser(1));
        Assertions.assertEquals(0, empty2.NthMostActiveUser(1));
        Assertions.assertEquals(0, empty3.NthMostActiveUser(1));
        Assertions.assertEquals(0, empty4.NthMostActiveUser(1));

        Assertions.assertEquals(0, empty1.ReportActivityInTimeWindow(new int[] {1, 2})[0]);
        Assertions.assertEquals(0, empty1.ReportActivityInTimeWindow(new int[] {1, 2})[1]);
        Assertions.assertEquals(0, empty2.ReportActivityInTimeWindow(new int[] {1, 2})[0]);
        Assertions.assertEquals(0, empty2.ReportActivityInTimeWindow(new int[] {1, 2})[1]);
        Assertions.assertEquals(0, empty3.ReportActivityInTimeWindow(new int[] {1, 2})[0]);
        Assertions.assertEquals(0, empty3.ReportActivityInTimeWindow(new int[] {1, 2})[1]);
        Assertions.assertEquals(0, empty4.ReportActivityInTimeWindow(new int[] {1, 2})[0]);
        Assertions.assertEquals(0, empty4.ReportActivityInTimeWindow(new int[] {1, 2})[1]);

        Assertions.assertEquals(0, empty1.ReportOnUser(1)[0]);
        Assertions.assertEquals(0, empty1.ReportOnUser(1)[1]);
        Assertions.assertEquals(0, empty2.ReportOnUser(1)[0]);
        Assertions.assertEquals(0, empty2.ReportOnUser(1)[1]);
        Assertions.assertEquals(0, empty3.ReportOnUser(1)[0]);
        Assertions.assertEquals(0, empty3.ReportOnUser(1)[1]);
        Assertions.assertEquals(0, empty4.ReportOnUser(1)[0]);
        Assertions.assertEquals(0, empty4.ReportOnUser(1)[1]);
    }

    @Test
    public void fatter() {
        UDWInteractionGraph u = new UDWInteractionGraph("resources/email-Eu-core-temporal.txt");
        DWInteractionGraph d = new DWInteractionGraph("resources/email-Eu-core-temporal.txt");
        DWInteractionGraph w = new DWInteractionGraph("resources/email-Eu-core-temporal-Dept1.txt");
        List<Integer> bfs = Arrays.asList(
            987, 28, 40, 53, 56, 65, 68, 75, 90, 91, 95, 105, 107, 120, 127, 131, 134, 135, 147,
            150,
            159, 185, 190, 193, 196, 197, 214, 220, 229, 231, 239, 245, 248, 251, 257, 269, 311,
            329, 334,
            338, 361, 364, 367, 415, 432, 433);
        List<Integer> dfs = Arrays.asList(
            987, 28, 231, 2, 4, 82, 54, 155, 771, 60, 187, 40, 26, 379, 272, 53, 11, 148, 193, 48,
            25, 73, 41, 499, 44, 65, 52, 30, 61, 6, 16, 14, 13, 120, 8, 24, 31, 90, 0, 5, 29, 68,
            506, 10, 173, 199, 270, 189, 32, 19, 127, 75, 56, 91, 38, 42, 70, 50, 402, 69, 303, 103,
            1, 46, 88, 106, 96, 132, 139, 64, 102, 125, 154, 324, 84, 97, 223, 164, 174, 510, 206,
            477, 310, 322, 153, 122, 114, 149, 78, 130, 178, 99, 207, 35, 161, 83, 86, 152, 362, 12,
            405, 135, 34, 581, 72, 159, 15, 268, 98, 107, 118, 335, 119, 126, 37, 77, 104, 74, 76,
            185, 166, 9, 109, 133, 111, 168, 45, 575, 66, 227, 966, 171, 147, 27, 134, 117, 151,
            165, 17, 92, 142, 136, 160, 87, 167, 141, 188, 200, 224, 316, 112, 110, 674, 138, 245,
            95, 239, 67, 181, 190, 191, 150, 229, 157, 248, 49, 205, 63, 364, 383, 163, 62, 113,
            337, 170, 214, 124, 143, 144, 175, 101, 184, 201, 225, 407, 299, 323, 247, 369, 232, 43,
            94, 240, 250, 332, 256, 186, 267, 80, 202, 204, 274, 280, 241, 326, 146, 197, 437, 516,
            131, 327, 192, 93, 752, 55, 129, 577, 58, 338, 196, 259, 278, 220, 290, 253, 566, 257,
            401, 291, 429, 415, 89, 718, 176, 329, 233, 306, 137, 273, 235, 638, 334, 281, 293, 246,
            275, 409, 249, 158, 292, 502, 228, 333, 312, 314, 169, 372, 325, 195, 18, 896, 237, 595,
            71, 318, 216, 319, 347, 230, 454, 39, 116, 692, 264, 343, 368, 399, 194, 528, 340, 356,
            182, 209, 260, 266, 177, 439, 313, 294, 305, 416, 7, 418, 105, 374, 213, 317, 304, 441,
            371, 522, 145, 265, 243, 457, 365, 377, 59, 423, 315, 658, 301, 428, 302, 802, 198, 488,
            568, 254, 450, 398, 602, 425, 367, 435, 448, 500, 447, 557, 573, 515, 586, 307, 659,
            400, 390, 203, 769, 443, 483, 283, 226, 700, 277, 285, 308, 255, 355, 590, 336, 715,
            546, 458, 321, 558, 349, 376, 559, 346, 388, 427, 422, 284, 406, 469, 421, 424, 547,
            387, 445, 353, 21, 373, 391, 288, 360, 493, 484, 507, 530, 413, 221, 550, 526, 414, 563,
            580, 480, 482, 251, 540, 242, 994, 311, 269, 925, 697, 720, 456, 527, 680, 520, 731,
            381, 236, 208, 320, 411, 537, 440, 361, 591, 420, 509, 282, 474, 534, 309, 514, 378,
            395, 505, 603, 612, 548, 853, 663, 22, 410, 433
        );
        Assertions.assertEquals(987, u.NthMostActiveUser(1));

        Assertions.assertEquals(10571, u.ReportOnUser(987)[0]);
        Assertions.assertEquals(109, u.ReportOnUser(987)[1]);

        Assertions.assertEquals(987, d.NthMostActiveUser(1, SendOrReceive.SEND));
        Assertions.assertEquals(168, d.NthMostActiveUser(1, SendOrReceive.RECEIVE));

        Assertions.assertEquals(9782, d.ReportOnUser(987)[0]);
        Assertions.assertEquals(789, d.ReportOnUser(987)[1]);
        Assertions.assertTrue(u.PathExists(987, 433));
        Assertions.assertEquals(bfs,d.BFS(987,433));
        Assertions.assertEquals(dfs,d.DFS(987,433));
        Assertions.assertEquals(41,w.MaxBreachedUserCount(4));
    }
}
