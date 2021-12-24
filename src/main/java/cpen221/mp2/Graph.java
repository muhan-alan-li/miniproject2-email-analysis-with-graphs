package cpen221.mp2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

abstract class Graph {

    protected static Map<Integer, Map<Integer, List<Integer>>> adjacencyList = new HashMap<>();

    /* ------- Rep Invariant ------- */
    // For a given graph, its representation adjacencyList should be immutable
    //
    // In an adjacencyList, the List<Integer> value corresponding to an Integer key should never
    // be empty
    //
    // In the adjacencyList, an Integer key representing a sender should also never have an empty
    // Map<Integer, List<Integer>> value, representing a Map of receivers
    //
    // the above two requirements enforces that any sender in the adjacencyList must have at least
    // one corresponding receiver, and any pair of sender-receiver must have at least one email
    // timestamp recorded in the List<Integer>

    /* ------- Abstraction Function ------- */
    // The Map<Integer, Map<Integer, List<Integer>>> represents a graph


    protected abstract boolean checkRep();

    protected Graph(DWInteractionGraph dwig) {
        this.adjacencyList = dwig.adjacencyList;
    }

    protected Graph(UDWInteractionGraph udwig) {
        this.adjacencyList = udwig.adjacencyList;
    }

    protected Graph(String fileName) {
        System.out.println(fileName);
    }

    /**
     * Constructor to create a graph using a file of email details in a certain time interval.
     *
     * @param fileName   Name of file.
     * @param timeWindow Timewindow to limit graph design.
     */
    protected void construct(String fileName, int[] timeWindow) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            for (String fileLine = reader.readLine(); fileLine != null;
                 fileLine = reader.readLine()) {

                int[] interaction = readLineToInteraction(fileLine);

                if (checkIfWithinTimeWindow(interaction[2], timeWindow)) {
                    this.insertInteraction(interaction);
                }
            }
            reader.close();
        } catch (IOException ioe) {
            System.out.println("Problem reading file!");
        }
    }

    /**
     * Helper method to check if timestamp falls within certain interval
     *
     * @param currentTime timestamp
     * @param timewindow  time window of [t0,t1]
     * @return true/false depending on if timestamp falls within that time interval.
     */
    private static boolean checkIfWithinTimeWindow(int currentTime, int[] timewindow) {
        return currentTime >= timewindow[0] && currentTime <= timewindow[1];
    }

    /**
     * Class-specific method to insert interactions into the adjacency list.
     *
     * @param interaction length 3 array containing the sender, receiver, and timestamp.
     */
    protected abstract void insertInteraction(int[] interaction);

    /**
     * Constructor to create a graph using DWig or UDWig in a certain time interval.
     *
     * @param inputDWIG  Provided Graph with info to create a new Graph.
     * @param timeFilter Time window to limit graph.
     * @return a new adjacency list.
     */
    protected Map<Integer, Map<Integer, List<Integer>>> construct(Graph inputDWIG,
                                                                  int[] timeFilter) {
        Map<Integer, Map<Integer, List<Integer>>> out = new HashMap<>();
        inputDWIG.adjacencyList.forEach((sender, receiverMap) -> {
            Map<Integer, List<Integer>> filteredReceiverMap = new HashMap<>();

            receiverMap.forEach((receiver, timestamps) -> {
                List<Integer> filteredTimestamps =
                        timestamps.stream().filter(
                                        time -> time <= timeFilter[1] && time >= timeFilter[0])
                                .collect(Collectors.toList());

                if (!filteredTimestamps.isEmpty()) {
                    filteredReceiverMap.put(receiver, filteredTimestamps);
                }
            });
            if (!filteredReceiverMap.isEmpty()) {
                out.put(sender, filteredReceiverMap);
            }
        });
        return out;
    }

    /**
     * Constructor for creating a graph where users are limited.
     *
     * @param inputDWIG  input graph to provide information.
     * @param userFilter userid of users to keep in new graph.
     * @return a new adjacency list.
     */
    protected Map<Integer, Map<Integer, List<Integer>>> construct(Graph inputDWIG, List<Integer> userFilter) {

        Map<Integer, Map<Integer, List<Integer>>> out = new HashMap<>();
        inputDWIG.adjacencyList.forEach((sender, receiverMap) -> {
            if (userFilter.contains(sender)) {
                out.put(sender, receiverMap);
            } else {
                Map<Integer, List<Integer>> filteredReceiverMap = new HashMap<>();
                receiverMap.forEach((receiver, timestamps) -> {
                    if (userFilter.contains(receiver)) {
                        filteredReceiverMap.put(receiver, timestamps);
                    }
                    if (!filteredReceiverMap.isEmpty()) {
                        out.put(sender, filteredReceiverMap);
                    }
                });
            }
        });
        return out;
    }

    /**
     * Helper method to parse info from a String.
     *
     * @param line String of data from file separated by spaces.
     * @return integer array containing sender, receiver, and timestamp data.
     */
    private static int[] readLineToInteraction(String line) {

        int[] output = new int[3];
        int i = 0;
        for (String str : line.split(" +")) {
            try {
                output[i] = Integer.parseInt(str);
            } catch (NumberFormatException e) {
                System.out.println("File contains value that is not an integer");
            }
            i++;
        }
        return output;
    }
}
