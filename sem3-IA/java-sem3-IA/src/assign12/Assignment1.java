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

class Board {
    private final List<List<Node>> board = new ArrayList<>();

    public Board() {
        setupBoard();
        getNode(new Point(0, 0)).setNodeType(NodeType.Neo);
        getNode(new Point(0, 0)).setCost(0);
        getNode(Algorithm.getKeyMakerPosition()).setNodeType(NodeType.KeyMaker);
    }

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

    public Node getNode(Point point) {
        return board.get(point.y).get(point.x);
    }
}

class Algorithm {
    private final Scanner scanner;
    private static Point keyMakerPosition;
    private final Board board;

    public Algorithm() {
        scanner = new Scanner(System.in);
        scanner.nextInt();
        keyMakerPosition = new Point(scanner.nextInt(), scanner.nextInt());
        board = new Board();
    }

    public static Point getKeyMakerPosition() {
        return keyMakerPosition;
    }

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

class Node {
    private final Point point;
    private Integer cost;
    private Node parent;
    private NodeType nodeType;
    private Boolean isBlocked;

    public Node(Point point, Integer cost, Node parent, NodeType nodeType) {
        this.point = point;
        this.cost = cost;
        this.parent = parent;
        this.nodeType = nodeType;
        isBlocked = false;
    }

    public void setNodeType(NodeType nodeType) {
        this.nodeType = nodeType;
    }

    public NodeType getNodeType() {
        return nodeType;
    }

    public Boolean getIsBlocked() {
        return isBlocked;
    }

    public void setIsBlocked(Boolean isBlocked) {
        this.isBlocked = isBlocked;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public Integer getCost() {
        return cost;
    }

    public Point getPoint() {
        return point;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }
}

enum NodeType {
    Perception("P"),
    Agent("A"),
    Backdoor("B"),
    Sentinel("S"),
    KeyMaker("K"),
    Neo("N"),
    Nothing("");

    private final String agentType;
    NodeType(String agentType) {
        this.agentType = agentType;
    }

    public static NodeType fromString(String agentType) {
        return Arrays.stream(NodeType.values())
                     .filter(type -> type.agentType.equals(agentType))
                     .findFirst()
                     .orElseThrow(() -> new IllegalArgumentException("Invalid agent type: " + agentType));
    }
}
