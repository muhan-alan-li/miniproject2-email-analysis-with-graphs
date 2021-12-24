package cpen221.mp2;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.Comparator;
import java.util.Collections;

public class DWInteractionGraph extends Graph {

    /* ------- Task 1 ------- */
    /* Building the Constructors */
    protected Map<Integer, Map<Integer, List<Integer>>> adjacencyList = new HashMap<>();

    /* ------- Rep Invariant ------- */
    // For a given graph, its representation adjacencyList should be immutable
    //
    // In an adjacencyList, the List<Integer> value corresponding to an Integer key should never
    // be empty
    //
    // In the adjacencyList, an Integer key representing a parent-node should also never have an
    // empty Map<Integer, List<Integer>> value, representing a Map of child nodes
    //
    // the above two requirements enforces that any parent-node in the adjacencyList must have at
    // least one corresponding child-node, and any pair of parent-child nodes must have at least
    // one interaction recorded between them in the List<Integer>

    /* ------- Abstraction Function ------- */
    // The Map<Integer, Map<Integer, List<Integer>>> represents a direct weighted graph
    //
    // Starting with the outside Map<Integer, ...>, each key in this map represents a parent-node
    //
    // Then, each of the value in Map<Integer, Map<Integer, ...>> is a Map of child-nodes that are
    // children of the parent-node
    //
    // Then, in the Map<Integer, List<Integer>>, each key represents an individual child-node
    //
    // Finally, in each List<Integer> corresponding to a given child node, each Integer inside
    // represents an interaction between the parent and child nodes. Conveniently, the size of this
    // List represents the weight of the edge between the two nodes

    /**
     * @return
     */
    protected boolean checkRep() {

        if (adjacencyList == null) {
            return false;
        }
        if (!adjacencyList.isEmpty()) {
            for (Map<Integer, List<Integer>> receiverMap : adjacencyList.values()) {

                if (receiverMap.isEmpty()) {
                    return false;
                }

                for (List<Integer> timestamps : receiverMap.values()) {
                    if (timestamps.isEmpty()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Creates a new DWInteractionGraph using an email interaction file.
     * The email interaction file will be in the resources directory.
     *
     * @param fileName the name of the file in the resources
     *                 directory containing email interactions
     */
    public DWInteractionGraph(String fileName) {
        super(fileName);
        construct(fileName, new int[]{0, Integer.MAX_VALUE});
    }

    /**
     * @param fileName   a string representing the name of the file
     * @param timeWindow
     */
    public DWInteractionGraph(String fileName, int[] timeWindow) {
        super(fileName);
        construct(fileName, timeWindow);
    }

    /**
     * Creates a new DWInteractionGraph from a DWInteractionGraph object
     * and considering a time window filter.
     *
     * @param inputDWIG  a DWInteractionGraph object
     * @param timeFilter an integer array of length 2: [t0, t1]
     *                   where t0 <= t1. The created DWInteractionGraph
     *                   should only include those emails in the input
     *                   DWInteractionGraph with send time t in the
     *                   t0 <= t <= t1 range.
     */
    public DWInteractionGraph(DWInteractionGraph inputDWIG, int[] timeFilter) {
        super(inputDWIG);
        this.adjacencyList = construct(inputDWIG, timeFilter);
    }

    /**
     * Creates a new DWInteractionGraph from a DWInteractionGraph object
     * and considering a list of User IDs.
     *
     * @param inputDWIG  a DWInteractionGraph object
     * @param userFilter a List of User IDs. The created DWInteractionGraph
     *                   should exclude those emails in the input
     *                   DWInteractionGraph for which neither the sender
     *                   nor the receiver exist in userFilter.
     */
    public DWInteractionGraph(DWInteractionGraph inputDWIG, List<Integer> userFilter) {
        super(inputDWIG);
        this.adjacencyList = construct(inputDWIG, userFilter);
    }

    /**
     * @return a Set of Integers, where every element in the set is a User ID
     * in this DWInteractionGraph.
     */
    public Set<Integer> getUserIDs() {
        Set<Integer> userIDs = new HashSet<>(this.adjacencyList.keySet());
        this.adjacencyList.forEach((sender, receiverMap) ->
                userIDs.addAll(receiverMap.keySet()));
        return userIDs;
    }

    /**
     * @param sender   the User ID of the sender in the email transaction.
     * @param receiver the User ID of the receiver in the email transaction.
     * @return the number of emails sent from the specified sender to the specified
     * receiver in this DWInteractionGraph.
     */
    public int getEmailCount(int sender, int receiver) {
        int output = 0;
        if (this.adjacencyList.containsKey(sender)) {
            if (this.adjacencyList.get(sender).containsKey(receiver)) {
                output = this.adjacencyList.get(sender).get(receiver).size();
            }
        }
        return output;
    }

    /**
     * @param interaction length 3 array containing the sender, receiver, and timestamp.
     */
    protected void insertInteraction(int[] interaction) {
        // checking if there already exists a node for this sender
        if (adjacencyList.containsKey(interaction[0])) {
            Map<Integer, List<Integer>> adjacents = adjacencyList.get(interaction[0]);
            // checking if this sender already sent email to this receiver
            if (adjacents.containsKey(interaction[1])) {
                adjacents.get(interaction[1]).add(interaction[2]);
            }
            // add new receiver since this is a new link
            else {
                List<Integer> timestamps = new ArrayList<>();
                timestamps.add(interaction[2]);
                adjacents.put(interaction[1], timestamps);
            }
        }
        // since this sender doesn't exist yet, create a new node(sender)
        else {
            Map<Integer, List<Integer>> adjacents = new HashMap<>();
            List<Integer> timestamps = new ArrayList<>();
            timestamps.add(interaction[2]);
            adjacents.put(interaction[1], timestamps);
            adjacencyList.put(interaction[0], adjacents);
        }
    }
    /* ------- Task 2 ------- */

    /**
     * Given an int array, [t0, t1], reports email transaction details.
     * Suppose an email in this graph is sent at time t, then all emails
     * sent where t0 <= t <= t1 are included in this report.
     *
     * @param timeWindow is an int array of size 2 [t0, t1] where t0<=t1.
     * @return an int array of length 3, with the following structure:
     * [NumberOfSenders, NumberOfReceivers, NumberOfEmailTransactions]
     */
    public int[] ReportActivityInTimeWindow(int[] timeWindow) {
        // user timeWindow to create a DWig that is filtered
        DWInteractionGraph timeFilteredDWig = new DWInteractionGraph(this, timeWindow);

        Set<Integer> receiverSet = new HashSet<>();
        List<Integer> timestampList = new ArrayList<>();

        // collecting receivers and timestamps
        timeFilteredDWig.adjacencyList.forEach((sender, receiverMap) -> {
            receiverSet.addAll(receiverMap.keySet());
            for (List<Integer> timestamps : receiverMap.values()) {
                timestampList.addAll(timestamps);
            }
        });

        return new int[]{
                timeFilteredDWig.adjacencyList.keySet().size(),
                receiverSet.size(),
                timestampList.size()
        };
    }

    /**
     * Given a User ID, reports the specified User's email transaction history.
     *
     * @param userID the User ID of the user for which the report will be
     *               created.
     * @return an int array of length 3 with the following structure:
     * [NumberOfEmailsSent, NumberOfEmailsReceived, UniqueUsersInteractedWith]
     * If the specified User ID does not exist in this instance of a graph,
     * returns [0, 0, 0].
     */
    public int[] ReportOnUser(int userID) {

        // using userID to create a new DWig that is filtered to have only desired user
        List<Integer> userFilter = new ArrayList<>();
        userFilter.add(userID);
        DWInteractionGraph userFilteredDWig = new DWInteractionGraph(this, userFilter);

        List<Integer> received = new ArrayList<>();
        List<Integer> sent = new ArrayList<>();
        Set<Integer> interactionList = new HashSet<>();

        // iterate through each node
        userFilteredDWig.adjacencyList.forEach((sender, receiverMap) -> {
            if (sender == userID) {
                receiverMap.forEach((receiver, timestamps) -> {
                    if (receiver == userID) {
                        received.addAll(timestamps);
                    }
                    sent.addAll(timestamps);
                });
                interactionList.addAll(receiverMap.keySet());
            } else {
                interactionList.add(sender);
                receiverMap.forEach((receiver, timestamps) -> received.addAll(timestamps));
            }
        });

        return new int[]{
                sent.size(),
                received.size(),
                interactionList.size()
        };
    }

    /**
     * @param N               a positive number representing rank. N=1 means the most active.
     * @param interactionType Represent the type of interaction to calculate the rank for
     *                        Can be SendOrReceive.Send or SendOrReceive.RECEIVE
     * @return the User ID for the Nth most active user in specified interaction type.
     * Sorts User IDs by their number of sent or received emails first. In the case of a
     * tie, secondarily sorts the tied User IDs in ascending order. Return -1 if no such user exists
     */
    public int NthMostActiveUser(int N, SendOrReceive interactionType) {
        if (interactionType == SendOrReceive.SEND) {
            return this.NthMostActiveSender(N);
        }
        if (interactionType == SendOrReceive.RECEIVE) {
            return this.NthMostActiveReceiver(N);
        }
        return -1;
    }

    /**
     * @param N
     * @return
     */
    private int NthMostActiveSender(int N) {
        List<IntegerPair> sendStats = new ArrayList<>();

        //collecting sender stats
        this.adjacencyList.forEach((sender, receiverMap) -> {
            List<Integer> sentList = new ArrayList<>();

            receiverMap.forEach((receiver, timestampList) -> sentList.addAll(timestampList));
            sendStats.add(new IntegerPair(sender, sentList.size()));
        });

        //sorting sendStats as specified in documentation
        sendStats.sort(Comparator
                .comparing(IntegerPair::getValue).reversed()
                .thenComparing(IntegerPair::getKey));

        //output the userID if such a user exists, -1 otherwise
        return sendStats.size() >= N ?
                sendStats.get(N - 1).getKey() :
                -1;
    }

    /**
     * @param N
     * @return
     */
    private int NthMostActiveReceiver(int N) {
        List<IntegerPair> receiveStats = new ArrayList<>();

        //collecting all receiver stats, must undergo further data sanitization
        this.adjacencyList.forEach((sender, receiverMap) ->
                receiverMap.forEach((receiver, timestampList) ->
                        receiveStats.add(new IntegerPair(receiver, timestampList.size()))));

        // sorting receiveStats by userID so identical userIDs are bunched together
        receiveStats.sort(Comparator.comparing(IntegerPair::getKey));

        mergeIdenticalKeys(receiveStats);

        //sorting receiveStats as specified in documentation
        receiveStats.sort(Comparator
                .comparing(IntegerPair::getValue).reversed()
                .thenComparing(IntegerPair::getKey));

        //output the userID if such a user exists, -1 otherwise
        return receiveStats.size() >= N ? receiveStats.get(N - 1).getKey() : -1;
    }

    /**
     * @param inputList
     */
    private static void mergeIdenticalKeys(List<IntegerPair> inputList) {
        for (int i = 0; i < inputList.size() - 1; i++) {
            IntegerPair thisP = inputList.get(i);
            IntegerPair nextP = inputList.get(i + 1);

            if (thisP.getKey() == nextP.getKey()) {
                int valueSum = thisP.getValue() + nextP.getValue();
                inputList.set(i, new IntegerPair(thisP.getKey(), valueSum));
                inputList.remove(i + 1);
                i--;
            }
        }
    }


    /* ------- Task 3 ------- */

    /**
     * performs breadth first search on the DWInteractionGraph object
     * to check path between user with userID1 and user with userID2.
     *
     * @param userID1 the user ID for the first user
     * @param userID2 the user ID for the second user
     * @return if a path exists, returns a list of user IDs
     * in the order encountered in the search.
     * if no path exists, should return null.
     */
    public List<Integer> BFS(int userID1, int userID2) {

        /* Starting from the node "userID1", this method visits all adjacent
         * nodes one-by-one. After visiting and checking the starting userID1,
         * it visits the nearest unchecked node. This continues until the final
         * node, userID2, is reached. If a path between the two users exists,
         * the resulting path is returned. If not, the method returns null. */

        List<Integer> path = new ArrayList<>();
        LinkedList<Integer> checked = new LinkedList<>();
        Set<Integer> visited = new HashSet<>();

        checked.add(userID1);
        path.add(userID1);
        visited.add(userID1);

        while (checked.size() > 0) {

            List<Integer> connected = new LinkedList<>();
            int node = checked.get(0);
            checked.remove(0);

            if (adjacencyList.get(node) != null) {

                adjacencyList.get(node);

                for (int _node : adjacencyList.get(node).keySet()) {

                    if (!visited.contains(_node)) {

                        visited.add(_node);
                        connected.add(_node);
                    }
                }

                connected.sort(Comparator.naturalOrder());

                for (int _node : connected) {

                    path.add(_node);

                    if (_node == userID2) {
                        return path;
                    }

                    checked.add(_node);
                }
            }
        }

        return null;
    }

    /**
     * @param userID1 the user ID for the first user
     * @param userID2 the user ID for the second user
     * @param path    the path between User 1 and User 2
     * @return true if a path exists between the two users, false otherwise
     */
    private boolean DFSHelper(int userID1, int userID2, List<Integer> path) {

        /* If the userID of user 1 exists in the adjacency list, all connected
         * nodes are added to a list if they are not in the path already. After
         * sorting the list of connected nodes in ascending order, all connected
         * nodes are added to the path. This algorithm keeps recursively calling
         * if a path still exists, and stops once userID2 has been reached. */

        List<Integer> connected = new LinkedList<>();

        if (adjacencyList.get(userID1) != null) {

            for (int _node : adjacencyList.get(userID1).keySet()) {

                if (!path.contains(_node)) {

                    connected.add(_node);
                }
            }

            connected.sort(Comparator.naturalOrder());

            for (int _node : connected) {

                path.add(_node);

                if (_node == userID2 || DFSHelper(_node, userID2, path)) {

                    return true;
                }
            }
        }

        return false;
    }

    /**
     * performs depth first search on the DWInteractionGraph object
     * to check path between user with userID1 and user with userID2.
     *
     * @param userID1 the user ID for the first user
     * @param userID2 the user ID for the second user
     * @return if a path exists, returns a list of user IDs
     * in the order encountered in the search.
     * if no path exists, should return null.
     */
    public List<Integer> DFS(int userID1, int userID2) {

        /* Creates a List of the paths, adding the first user to it. Then,
         * the DFS helper is called, and returns the path if it exists.
         * Null is returned if no path exists. */

        List<Integer> path = new ArrayList<>();
        path.add(userID1);

        return !DFSHelper(userID1, userID2, path) ? null : path;
    }

    /* ------- Task 4 ------- */

    /**
     * Read the MP README file carefully to understand
     * what is required from this method.
     *
     * @param hours number of hours before firewall stops pollution
     * @return the maximum number of users that can be polluted in N hours
     */
    public int MaxBreachedUserCount(int hours) {
        int durationInSeconds = hours * 3600;
        Set<Integer> breachedUserCountSet = new HashSet<>();

        Set<Integer> possibleStartTimeSet = this.getPossibleStartTimes();

        possibleStartTimeSet.forEach(startTime -> {
            DWInteractionGraph timeFilteredDwig =
                    new DWInteractionGraph(this,
                            new int[]{startTime, startTime + durationInSeconds});

            timeFilteredDwig.adjacencyList.forEach((sender, receiverMap) -> {
                Set<Integer> patientZeroSet = new HashSet<>();
                receiverMap.values().forEach(timestampsList -> {
                    if (timestampsList.contains(startTime)) {
                        patientZeroSet.add(sender);
                    }
                });
                for (Integer pZero : patientZeroSet) {
                    Set<Integer> usersBreached = new HashSet<>();
                    usersBreached.add(pZero);
                    timeFilteredDwig.contactTracing(pZero, usersBreached);
                    breachedUserCountSet.add(usersBreached.size());
                }
            });
        });
        return breachedUserCountSet.isEmpty() ? 0 : Collections.max(breachedUserCountSet);
    }

    /**
     * Finds all possible starting times for a virus attack in a given graph of emails
     *
     * @return A set of Integers, each representing an unique starting time
     */
    private Set<Integer> getPossibleStartTimes(){
        Set<Integer> possibleStartTimeSet = new HashSet<>();
        this.adjacencyList.forEach((sender, receiverMap) ->
            receiverMap.values().forEach(possibleStartTimeSet::addAll));
        return possibleStartTimeSet;
    }

    /**
     * Performs recursive contact tracing starting from a given patient zero, assumes the entire
     * graph is within the timewindow for infection
     *
     * @param patientZero the patient zero to begin tracing from
     * @param traced      a set of integers, each integer represents a userID that has come into
     *                    contact with an infected user
     */
    private void contactTracing(int patientZero, Set<Integer> traced){
        if(this.adjacencyList.containsKey(patientZero)){
            this.adjacencyList.get(patientZero).keySet().stream()
                .filter(receiver -> !traced.contains(receiver)).forEach(receiver -> {
                        traced.add(receiver);
                        contactTracing(receiver, traced);
                    });
        }
    }

}
