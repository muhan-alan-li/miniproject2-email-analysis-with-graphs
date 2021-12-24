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

public class UDWInteractionGraph extends Graph {

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
    // The Map<Integer, Map<Integer, List<Integer>>> represents a undirected weighted graph
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
    // List represents the weight of the edge between the two nodes.

    /* ------- Task 1 ------- */
    /* Building the Constructors */
    protected Map<Integer, Map<Integer, List<Integer>>> adjacencyList = new HashMap<>();

    /**
     * @return
     */
    protected boolean checkRep() {

        if (adjacencyList == null) {
            return false;
        }

        Set<Integer> senderSet = adjacencyList.keySet();
        Set<Integer> receiverSet = new HashSet<>();

        for (Map<Integer, List<Integer>> receiverMap : adjacencyList.values()) {

            if (receiverMap.isEmpty()) {
                return false;
            }

            receiverSet.addAll(receiverMap.keySet());
            for (List<Integer> timestamps : receiverMap.values()) {

                if (timestamps.isEmpty()) {
                    return false;
                }
            }
        }

        return senderSet.equals(receiverSet);
    }

    /**
     * Creates a new UDWInteractionGraph using an email interaction file.
     * The email interaction file will be in the resources directory.
     *
     * @param fileName the name of the file in the resources
     *                 directory containing email interactions
     */
    public UDWInteractionGraph(String fileName) {
        super(fileName);
        construct(fileName, new int[]{0, Integer.MAX_VALUE});
    }

    /**
     * Creates a new UDWInteractionGraph from a UDWInteractionGraph object
     * and considering a time window filter.
     *
     * @param inputUDWIG a UDWInteractionGraph object
     * @param timeFilter an integer array of length 2: [t0, t1]
     *                   where t0 <= t1. The created UDWInteractionGraph
     *                   should only include those emails in the input
     *                   UDWInteractionGraph with send time t in the
     *                   t0 <= t <= t1 range.
     */
    public UDWInteractionGraph(UDWInteractionGraph inputUDWIG, int[] timeFilter) {
        super(inputUDWIG);
        this.adjacencyList = construct(inputUDWIG, timeFilter);
    }

    /**
     * Creates a new UDWInteractionGraph from a UDWInteractionGraph object
     * and considering a list of User IDs.
     *
     * @param inputUDWIG a UDWInteractionGraph object
     * @param userFilter a List of User IDs. The created UDWInteractionGraph
     *                   should exclude those emails in the input
     *                   UDWInteractionGraph for which neither the sender
     *                   nor the receiver exist in userFilter.
     */
    public UDWInteractionGraph(UDWInteractionGraph inputUDWIG, List<Integer> userFilter) {
        super(inputUDWIG);
        this.adjacencyList = construct(inputUDWIG, userFilter);
    }

    /**
     * Creates a new UDWInteractionGraph from a DWInteractionGraph object.
     *
     * @param inputDWIG a DWInteractionGraph object
     */
    public UDWInteractionGraph(DWInteractionGraph inputDWIG) {
        super(inputDWIG);
        for (int sender : inputDWIG.adjacencyList.keySet()) {
            for (int receiver : inputDWIG.adjacencyList.get(sender).keySet()) {
                if (receiver >= sender) {
                    Map<Integer, List<Integer>> receiverdata = new HashMap<>();
                    Map<Integer, List<Integer>> senderdata = new HashMap<>();
                    List<Integer> timestamps = new ArrayList<>();

                    if (receiver == sender) {
                        if (inputDWIG.adjacencyList.get(sender).containsKey(receiver)) {
                            timestamps.addAll(inputDWIG.adjacencyList.get(sender).get(receiver));
                            receiverdata.put(receiver, timestamps);
                        }
                        this.adjacencyList.put(sender, receiverdata);
                    } else {
                        if (inputDWIG.adjacencyList.get(sender).containsKey(receiver)) {
                            timestamps.addAll(inputDWIG.adjacencyList.get(sender).get(receiver));
                        }


                        if (inputDWIG.adjacencyList.containsKey(receiver)) {
                            if (inputDWIG.adjacencyList.get(receiver).containsKey(sender)) {
                                timestamps.addAll(inputDWIG.adjacencyList.get(receiver).get(sender));
                            }
                        }

                        receiverdata.put(receiver, timestamps);
                        senderdata.put(sender, timestamps);
                        this.adjacencyList.put(sender, receiverdata);
                        this.adjacencyList.put(receiver, senderdata);
                    }
                }
            }
        }
    }

    /**
     * Method to insert interactions into new graph
     *
     * @param interaction length 3 array containing the sender, receiver, and timestamp.
     */
    protected void insertInteraction(int[] interaction) {
        if (interaction[1] == interaction[0]) {
            insertHelper(interaction[0], interaction[1], interaction[2]);
        } else {
            insertHelper(interaction[0], interaction[1], interaction[2]);
            insertHelper(interaction[1], interaction[0], interaction[2]);
        }
    }

    /**
     * Helper method for UDWig to add both sender/receiver and receiver/sender pairs.
     *
     * @param user1     Sender
     * @param user2     Receiver
     * @param timestamp Time of which email was sent between them.
     */
    private void insertHelper(int user1, int user2, int timestamp) {
        if (adjacencyList.containsKey(user1)) {
            Map<Integer, List<Integer>> adjacents = adjacencyList.get(user1);
            // checking if this sender already sent email to this receiver
            if (adjacents.containsKey(user2)) {
                adjacents.get(user2).add(timestamp);
            }
            // add new receiver since this is a new link
            else {
                List<Integer> timestamps = new ArrayList<>();
                timestamps.add(timestamp);
                adjacents.put(user2, timestamps);
            }
        }
        // since this sender doesn't exist yet, create a new node for both sender and receiver because UDW
        else {
            Map<Integer, List<Integer>> adjacentsReceiver = new HashMap<>();
            List<Integer> timestamps = new ArrayList<>();
            timestamps.add(timestamp);
            adjacentsReceiver.put(user2, timestamps);
            adjacencyList.put(user1, adjacentsReceiver);
        }
    }


    /**
     * @return a Set of Integers, where every element in the set is a User ID
     * in this UDWInteractionGraph.
     */
    public Set<Integer> getUserIDs() {
        return new HashSet<>(adjacencyList.keySet());
    }

    /**
     * @param user1 the User ID of the first user.
     * @param user2 the User ID of the second user.
     * @return An int representing the number of email interactions (send/receive) between user1 and user2
     */
    public int getEmailCount(int user1, int user2) {
        int numinteractions = 0;
        if (!this.adjacencyList.containsKey(user1)) {
            return numinteractions;
        }
        if (this.adjacencyList.get(user1).containsKey(user2)) {
            numinteractions = this.adjacencyList.get(user1).get(user2).size();
        }
        return numinteractions;
    }

    /* ------- Task 2 ------- */

    /**
     * @param timeWindow is an int array of size 2 [t0, t1]
     *                   where t0<=t1
     * @return an int array of length 2, with the following structure:
     * [NumberOfUsers, NumberOfEmailTransactions]
     */
    public int[] ReportActivityInTimeWindow(int[] timeWindow) {
        int[] output =
                new int[2]; // output[0] = NumberOfUsers  and output[1] = NumberOfEmailTransactions

        UDWInteractionGraph filtered = new UDWInteractionGraph(this, timeWindow);

        output[0] = filtered.adjacencyList.keySet().size();

        for (int sender : filtered.adjacencyList.keySet()) {
            for (int receiver : filtered.adjacencyList.get(sender).keySet()) {
                output[1] += filtered.adjacencyList.get(sender).get(receiver).size();
            }
        }
        output[1] = output[1] / 2;

        return output;
    }

    /**
     * @param userID the User ID of the user for which
     *               the report will be created
     * @return an int array of length 2 with the following structure:
     * [NumberOfEmails, UniqueUsersInteractedWith]
     * If the specified User ID does not exist in this instance of a graph,
     * returns [0, 0].
     */
    public int[] ReportOnUser(int userID) {
        int[] output = new int[]{0, 0};

        if (adjacencyList.containsKey(userID)) {
            for (int receiver : adjacencyList.get(userID).keySet()) {
                output[1]++;
                output[0] += adjacencyList.get(userID).get(receiver).size();
            }
        }

        return output;
    }

    /**
     * @param N a positive number representing rank. N=1 means the most active.
     * @return the User ID for the Nth most active user
     */
    public int NthMostActiveUser(int N) {

        List<IntegerPair> ranks = new ArrayList<>();

        for (int user : adjacencyList.keySet()) {
            int[] report = ReportOnUser(user);
            IntegerPair pair = new IntegerPair(user, report[0]);
            ranks.add(pair);
        }

        ranks.sort(Comparator.comparing(IntegerPair::getValue).reversed());

        return (N < ranks.size()) ? ranks.get(N - 1).getKey() : 0;
    }

    /* ------- Task 3 ------- */

    /**
     * @return the number of completely disjoint graph
     * components in the UDWInteractionGraph object.
     */
    public int NumberOfComponents() {

        /* For all users, checks if a user is in the graph's components.
         * if a path exists between the two users, the number of component
         * count is increased accordingly. The number of components is returned. */

        int currentComponent;
        List<Set<Integer>> components = new ArrayList<>();

        for (Integer user1 : getUserIDs()) {

            if (findUserIndex(user1, components) == -1) {

                components.add(new HashSet<>(Collections.singletonList(user1)));
            }

            currentComponent = findUserIndex(user1, components);

            for (Integer user2 : getUserIDs()) {

                if (PathExists(user1, user2)) {

                    components.get(currentComponent).add(user2);
                }
            }
        }

        return components.size();
    }

    /**
     * @param user       the userID
     * @param components a list of sets including all disjoint components
     * @return the index of the user in the components if it exists, and
     * return -1 if it does not exist
     */
    private static int findUserIndex(int user, List<Set<Integer>> components) {

        for (int i = 0; i < components.size(); i++) {

            if (components.get(i).contains(user)) {

                return i;
            }
        }

        return -1;
    }

    /**
     * Same algorithm as BFS, except true is returned if a path
     * exists, and false otherwise.
     *
     * @param userID1 the user ID for the first user
     * @param userID2 the user ID for the second user
     * @return whether a path exists between the two users or not
     */
    public boolean PathExists(int userID1, int userID2) {

        /* Same algorithm as BFS, except true is returned if a path
         * exists between the two users, and false otherwise. Initially,
         * algorithm also checks for exception where user1 is the same
         * as user2. */

        LinkedList<Integer> checked = new LinkedList<>();
        Set<Integer> visited = new HashSet<>();

        if (userID1 == userID2) {

            return true;
        }

        checked.add(userID1);
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

                    if (_node == userID2) {
                        return true;
                    }

                    checked.add(_node);
                }
            }
        }

        return false;
    }

}
