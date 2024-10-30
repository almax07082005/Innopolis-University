package assign11;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;

public class Assignment1 {
    public static void main(String[] args) {
        Algorithm algorithm = new Algorithm();
        algorithm.run();
    }
}

/**
 * The Board class represents a 9x9 grid of nodes, each of which can have different types and costs.
 * It initializes the board with default nodes and sets specific nodes for Neo and the KeyMaker.
 */
class Board {
    private final List<List<Node>> board = new ArrayList<>();

    /**
     * Constructs a new Board and sets up the initial configuration.
     * The node at (0, 0) is set to Neo with a cost of 0.
     * The node at the KeyMaker's position is set to KeyMaker.
     */
    public Board() {
        setupBoard();
        getNode(new Point(0, 0)).setNodeType(NodeType.Neo);
        getNode(new Point(0, 0)).setCost(0);
        getNode(Algorithm.getKeyMakerPosition()).setNodeType(NodeType.KeyMaker);
    }

    /**
     * Initializes the board with default nodes of type NodeType.Nothing.
     * The board is a 9x9 grid.
     */
    private void setupBoard() {
        for (int i = 0; i < 9; i++) {
            List<Node> row = new ArrayList<>();
            for (int j = 0; j < 9; j++) {
                row.add(new Node(
                        new Point(j, i),
                        null,
                        null,
                        NodeType.Nothing
                ));
            }
            board.add(row);
        }
    }

    /**
     * Retrieves the node at the specified point on the board.
     *
     * @param point the point on the board to retrieve the node from
     * @return the node at the specified point
     */
    public Node getNode(Point point) {
        return board.get(point.y).get(point.x);
    }
}

/**
 * The Algorithm class implements A* algorithm to navigate a board
 * and find the shortest path to a key maker position. It uses a priority queue
 * to manage open nodes and sets to track closed and blocked nodes.
 */
class Algorithm {
    private final Scanner scanner;
    private static Point keyMakerPosition;

    private final Board board;
    private final Queue<Node> openQueue;
    private final Set<Node> closedSet;
    private final Set<Node> blockedSet;

    /**
     * Constructs an Algorithm instance, initializes the board, and sets up the
     * initial state for the pathfinding algorithm.
     */
    public Algorithm() {
        scanner = new Scanner(System.in);
        scanner.nextInt();
        keyMakerPosition = new Point(scanner.nextInt(), scanner.nextInt());
        board = new Board();

        openQueue = new PriorityQueue<>();
        openQueue.offer(board.getNode(new Point(0, 0)));

        closedSet = new HashSet<>();
        blockedSet = new HashSet<>();
    }

    /**
     * Runs the pathfinding algorithm to navigate the board and find the shortest
     * path to the key maker position.
     */
    public void run() {
        Node currentNode = board.getNode(new Point(0, 0));
        makeMove(currentNode.getPoint(), currentNode.getPoint());
        getPerception();
        closedSet.add(openQueue.peek());

        while (!openQueue.isEmpty()) {
            openQueue.remove();
            calculateMove(currentNode);
            Node nextNode = openQueue.peek();

            if (nextNode == null) {
                System.out.println("e -1");
                return;
            }
            if (nextNode.getNodeType() == NodeType.KeyMaker) {
                System.out.println("e " + nextNode.getCost());
                return;
            }

            closedSet.add(nextNode);
            boolean flag = makeMove(currentNode.getPoint(), nextNode.getPoint());
            if (!flag) {
                System.out.println("e -1");
                return;
            }
            currentNode = nextNode;
            getPerception();
        }
    }

    /**
     * Calculates the possible moves from the current node and updates the open
     * queue with valid moves.
     *
     * @param currentNode the current node being evaluated
     */
    private void calculateMove(Node currentNode) {
        List<Point> possibleMoves = getPoints(currentNode.getPoint());

        for (Point point : possibleMoves) {
            if (point.x < 0 || point.x >= 9 || point.y < 0 || point.y >= 9) {
                continue;
            }

            Node node = board.getNode(point);
            if (closedSet.contains(node) || blockedSet.contains(node)) {
                continue;
            }

            switch (node.getNodeType()) {
                case Nothing, KeyMaker, Backdoor -> {
                    if (node.getCost() == null || currentNode.getCost() + 1 < node.getCost()) {
                        node.setCost(currentNode.getCost() + 1);
                        node.setParent(currentNode);
                    }
                    openQueue.offer(node);
                } case Sentinel, Agent, Perception -> blockedSet.add(node);
            }
        }
    }

    /**
     * Generates a list of possible move points from the current point.
     *
     * @param current the current point
     * @return a list of possible move points
     */
    private List<Point> getPoints(Point current) {
        List<Point> possibleMoves = new ArrayList<>();
        possibleMoves.add(new Point(current.x, current.y + 1));
        possibleMoves.add(new Point(current.x, current.y - 1));
        possibleMoves.add(new Point(current.x + 1, current.y));
        possibleMoves.add(new Point(current.x - 1, current.y));
        return possibleMoves;
    }

    /**
     * Gets the position of the key maker.
     *
     * @return the key maker position
     */
    public static Point getKeyMakerPosition() {
        return keyMakerPosition;
    }

    /**
     * Retrieves the list of parent nodes for a given point.
     *
     * @param point the point for which to retrieve parent nodes
     * @return a list of parent nodes
     */
    private List<Node> getParents(Point point) {
        List<Node> parents = new ArrayList<>();
        Node node = board.getNode(point);
        while (node != null) {
            parents.add(node);
            node = node.getParent();
        }
        return parents;
    }

    /**
     * Makes a move from one point to another, printing the move steps.
     *
     * @param from the starting point
     * @param to the destination point
     * @return true if the move is successful, false otherwise
     */
    private boolean makeMove(Point from, Point to) {
        List<Point> possibleMoves = getPoints(from);
        if (possibleMoves.contains(to) || from.equals(to)) {
            System.out.println("m " + to.x + " " + to.y);
            return true;
        }

        List<Node> parentsFrom = getParents(from);
        List<Node> parentsTo = getParents(to);

        Node commonNode = null;
        int index;
        for (index = 0; index < parentsTo.size(); index++) {
            Node node = parentsTo.get(index);

            if (parentsFrom.contains(node)) {
                commonNode = node;
                break;
            }
        }
        if (commonNode == null) {
            return false;
        }

        for (int i = 1; i < parentsFrom.size(); i++) {
            Node node = parentsFrom.get(i);

            if (node.getNodeType() == NodeType.Sentinel || node.getNodeType() == NodeType.Agent || node.getNodeType() == NodeType.Perception) {
                return false;
            }
            if (node.equals(commonNode)) {
                break;
            }
            System.out.println("m " + node.getPoint().x + " " + node.getPoint().y);
            getPerception();
        }
        for (int i = index; i >= 0; i--) {
            Node node = parentsTo.get(i);

            if (node.getNodeType() == NodeType.Sentinel || node.getNodeType() == NodeType.Agent || node.getNodeType() == NodeType.Perception) {
                return false;
            }

            System.out.println("m " + node.getPoint().x + " " + node.getPoint().y);
            if (i != 0) getPerception();
        }

        return true;
    }

    /**
     * Gets the perception data from the scanner and updates the board nodes
     * accordingly.
     */
    private void getPerception() {
        int size = scanner.nextInt();
        for (int i = 0; i < size; i++) {
            board.getNode(new Point(
                    scanner.nextInt(),
                    scanner.nextInt()
            )).setNodeType(NodeType.fromString(scanner.next()));
        }
    }
}

/**
 * Represents a node in A* algorithm, which includes information about its position,
 * cost, heuristic (Manhattan distance), total cost, parent node, and type.
 */
class Node implements Comparable<Node> {
    private final Point point;
    private Integer cost;
    private Integer manhattanDistance;
    private Integer totalCost;
    private Node parent;
    private NodeType nodeType;

    /**
     * Calculates the Manhattan distance from this node to the key maker's position
     * and updates the total cost.
     */
    private void calculate() {
        if (cost == null) return;
        manhattanDistance = Math.abs(point.x - Algorithm.getKeyMakerPosition().x) + Math.abs(point.y - Algorithm.getKeyMakerPosition().y);
        totalCost = cost + manhattanDistance;
    }

    /**
     * Constructs a new Node with the specified point, cost, parent node, and node type.
     *
     * @param point the point representing the position of the node
     * @param cost the cost to reach this node
     * @param parent the parent node from which this node was reached
     * @param nodeType the type of the node
     */
    public Node(Point point, Integer cost, Node parent, NodeType nodeType) {
        this.point = point;
        this.cost = cost;
        this.parent = parent;
        this.nodeType = nodeType;
        calculate();
    }

    /**
     * Returns the point representing the position of the node.
     *
     * @return the point of the node
     */
    public Point getPoint() {
        return point;
    }

    /**
     * Returns the parent node from which this node was reached.
     *
     * @return the parent node
     */
    public Node getParent() {
        return parent;
    }

    /**
     * Sets the type of the node.
     *
     * @param nodeType the new type of the node
     */
    public void setNodeType(NodeType nodeType) {
        this.nodeType = nodeType;
    }

    /**
     * Returns the type of the node.
     *
     * @return the type of the node
     */
    public NodeType getNodeType() {
        return nodeType;
    }

    /**
     * Returns the cost to reach this node.
     *
     * @return the cost to reach this node
     */
    public Integer getCost() {
        return cost;
    }

    /**
     * Sets the cost to reach this node and recalculates the total cost.
     *
     * @param cost the new cost to reach this node
     */
    public void setCost(Integer cost) {
        this.cost = cost;
        calculate();
    }

    /**
     * Sets the parent node from which this node was reached.
     *
     * @param parent the new parent node
     */
    public void setParent(Node parent) {
        this.parent = parent;
    }

    /**
     * Compares this node with another node based on their total costs and, if equal, their Manhattan distances.
     *
     * @param node the node to compare with
     * @return a negative integer, zero, or a positive integer as this node is less than, equal to, or greater than the specified node
     */
    @Override
    public int compareTo(Node node) {
        if (totalCost < node.totalCost) return -1;
        else if (totalCost > node.totalCost) return 1;
        else return manhattanDistance.compareTo(node.manhattanDistance);
    }
}

/**
 * Enum representing different types of nodes in the system.
 * Each node type is associated with a specific agent type code.
 */
enum NodeType {
    Perception("P"),
    Agent("A"),
    Backdoor("B"),
    Sentinel("S"),
    KeyMaker("K"),
    Neo("N"),
    Nothing("");

    private final String agentType;

    /**
     * Constructor for NodeType.
     *
     * @param agentType the agent type code associated with the node type.
     */
    NodeType(String agentType) {
        this.agentType = agentType;
    }

    /**
     * Converts a string representation of an agent type code to its corresponding NodeType.
     *
     * @param agentType the agent type code as a string.
     * @return the corresponding NodeType.
     * @throws IllegalArgumentException if the agent type code does not match any NodeType.
     */
    public static NodeType fromString(String agentType) {
        return Arrays.stream(NodeType.values())
                     .filter(type -> type.agentType.equals(agentType))
                     .findFirst()
                     .orElseThrow(() -> new IllegalArgumentException("Invalid agent type: " + agentType));
    }
}
