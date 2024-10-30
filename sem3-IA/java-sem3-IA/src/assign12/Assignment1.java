package assign12;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Assignment1 {
    public static void main(String[] args) {
        Algorithm algorithm = new Algorithm();
        algorithm.run();
    }
}

/**
 * Represents a game board consisting of a 9x9 grid of nodes.
 */
class Board {
    private final List<List<Node>> board = new ArrayList<>();

    /**
     * Constructs a new Board and initializes it with default values.
     * Sets the initial positions for Neo and the KeyMaker.
     */
    public Board() {
        setupBoard();
        getNode(new Point(0, 0)).setNodeType(NodeType.Neo);
        getNode(new Point(0, 0)).setCost(0);
        getNode(Algorithm.getKeyMakerPosition()).setNodeType(NodeType.KeyMaker);
    }

    /**
     * Initializes the board with nodes. Each node is set to have a default cost of Integer.MAX_VALUE
     * and a type of NodeType.Nothing.
     */
    private void setupBoard() {
        for (int i = 0; i < 9; i++) {
            List<Node> row = new ArrayList<>();
            for (int j = 0; j < 9; j++) {
                row.add(new Node(
                        new Point(j, i),
                        Integer.MAX_VALUE,
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
 * The Algorithm class represents the main logic for navigating a board to find the KeyMaker.
 * It uses a Scanner to read input and a Board to manage the nodes.
 */
class Algorithm {
    private final Scanner scanner;
    private static Point keyMakerPosition;
    private final Board board;

    /**
     * Constructs an Algorithm instance, initializes the scanner, reads the KeyMaker position,
     * and initializes the board.
     */
    public Algorithm() {
        scanner = new Scanner(System.in);
        scanner.nextInt();
        keyMakerPosition = new Point(scanner.nextInt(), scanner.nextInt());
        board = new Board();
    }

    /**
     * Gets the position of the KeyMaker.
     *
     * @return the position of the KeyMaker as a Point.
     */
    public static Point getKeyMakerPosition() {
        return keyMakerPosition;
    }

    /**
     * Runs the algorithm to navigate the board and find the KeyMaker.
     * It prints the moves and the final cost to reach the KeyMaker.
     */
    public void run() {
        Node current = board.getNode(new Point(0, 0));
        System.out.println("m " + current.getPoint().x + " " + current.getPoint().y);

        while (true) {
            getPerception();
            Node nextNode = makeMove(current);

            if (nextNode == null) {
                break;
            }
            System.out.println("m " + nextNode.getPoint().x + " " + nextNode.getPoint().y);
            current = nextNode;
        }

        Node keyMakerNode = board.getNode(keyMakerPosition);
        if (keyMakerNode.getParent() == null) {
            System.out.println("e -1");
        } else {
            System.out.println("e " + keyMakerNode.getCost());
        }
    }

    /**
     * Gets the possible points to move to from the current point.
     *
     * @param current the current point.
     * @return a list of possible points to move to.
     */
    private List<Point> getPoints(Point current) {
        List<Point> possibleMoves = new ArrayList<>(List.of(
                new Point(current.x, current.y + 1),
                new Point(current.x, current.y - 1),
                new Point(current.x + 1, current.y),
                new Point(current.x - 1, current.y)
        ));

        possibleMoves.removeIf(possibleMove -> possibleMove.x < 0 || possibleMove.x >= 9 || possibleMove.y < 0 || possibleMove.y >= 9);

        return possibleMoves;
    }

    /**
     * Makes a move from the current node to the next node based on the possible moves.
     *
     * @param currentNode the current node.
     * @return the next node to move to, or the parent node if no valid move is found.
     */
    private Node makeMove(Node currentNode) {
        List<Point> possibleMoves = getPoints(currentNode.getPoint());

        for (Point point : possibleMoves) {
            Node nextNode = board.getNode(point);

            if (currentNode.getNodeType() == NodeType.KeyMaker) {
                return currentNode.getParent();
            }

            if (!nextNode.getIsBlocked() &&
                    (currentNode.getParent() == null || !currentNode.getParent().equals(nextNode)) &&
                    currentNode.getCost() + 1 < nextNode.getCost()) {
                nextNode.setCost(currentNode.getCost() + 1);
                nextNode.setParent(currentNode);
                return nextNode;
            }
        }

        return currentNode.getParent();
    }

    /**
     * Gets the perception of the board by reading input and updating the nodes accordingly.
     */
    private void getPerception() {
        int size = scanner.nextInt();
        for (int i = 0; i < size; i++) {
            Node node = board.getNode(new Point(
                    scanner.nextInt(),
                    scanner.nextInt()
            ));
            node.setNodeType(NodeType.fromString(scanner.next()));
            switch (node.getNodeType()) {
                case Sentinel, Agent, Perception -> node.setIsBlocked(true);
                case Nothing, KeyMaker, Backdoor, Neo -> node.setIsBlocked(false);
            }
        }
    }
}

/**
 * Represents a node in the board with a specific point, cost, parent node, node type, and blocked status.
 */
class Node {
    private final Point point;
    private Integer cost;
    private Node parent;
    private NodeType nodeType;
    private Boolean isBlocked;

    /**
     * Constructs a Node with the specified point, cost, parent node, and node type.
     *
     * @param point    the point associated with this node
     * @param cost     the cost associated with this node
     * @param parent   the parent node of this node
     * @param nodeType the type of this node
     */
    public Node(Point point, Integer cost, Node parent, NodeType nodeType) {
        this.point = point;
        this.cost = cost;
        this.parent = parent;
        this.nodeType = nodeType;
        isBlocked = false;
    }

    /**
     * Sets the type of this node.
     *
     * @param nodeType the new type of this node
     */
    public void setNodeType(NodeType nodeType) {
        this.nodeType = nodeType;
    }

    /**
     * Returns the type of this node.
     *
     * @return the type of this node
     */
    public NodeType getNodeType() {
        return nodeType;
    }

    /**
     * Returns whether this node is blocked.
     *
     * @return true if this node is blocked, false otherwise
     */
    public Boolean getIsBlocked() {
        return isBlocked;
    }

    /**
     * Sets whether this node is blocked.
     *
     * @param isBlocked true to block this node, false to unblock it
     */
    public void setIsBlocked(Boolean isBlocked) {
        this.isBlocked = isBlocked;
    }

    /**
     * Sets the cost associated with this node.
     *
     * @param cost the new cost of this node
     */
    public void setCost(Integer cost) {
        this.cost = cost;
    }

    /**
     * Returns the cost associated with this node.
     *
     * @return the cost of this node
     */
    public Integer getCost() {
        return cost;
    }

    /**
     * Returns the point associated with this node.
     *
     * @return the point of this node
     */
    public Point getPoint() {
        return point;
    }

    /**
     * Returns the parent node of this node.
     *
     * @return the parent node
     */
    public Node getParent() {
        return parent;
    }

    /**
     * Sets the parent node of this node.
     *
     * @param parent the new parent node
     */
    public void setParent(Node parent) {
        this.parent = parent;
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
     * Constructor for NodeType enum.
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
