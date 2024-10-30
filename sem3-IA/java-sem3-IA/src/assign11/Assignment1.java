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
                        null,
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
    private final Queue<Node> openQueue;
    private final Set<Node> closedSet;
    private final Set<Node> blockedSet;

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

    private List<Point> getPoints(Point current) {
        List<Point> possibleMoves = new ArrayList<>();
        possibleMoves.add(new Point(current.x, current.y + 1));
        possibleMoves.add(new Point(current.x, current.y - 1));
        possibleMoves.add(new Point(current.x + 1, current.y));
        possibleMoves.add(new Point(current.x - 1, current.y));
        return possibleMoves;
    }

    public static Point getKeyMakerPosition() {
        return keyMakerPosition;
    }

    private List<Node> getParents(Point point) {
        List<Node> parents = new ArrayList<>();
        Node node = board.getNode(point);
        while (node != null) {
            parents.add(node);
            node = node.getParent();
        }
        return parents;
    }

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

class Node implements Comparable<Node> {
    private final Point point;
    private Integer cost;
    private Integer manhattanDistance;
    private Integer totalCost;
    private Node parent;
    private NodeType nodeType;

    private void calculate() {
        if (cost == null) return;
        manhattanDistance = Math.abs(point.x - Algorithm.getKeyMakerPosition().x) + Math.abs(point.y - Algorithm.getKeyMakerPosition().y);
        totalCost = cost + manhattanDistance;
    }

    public Node(Point point, Integer cost, Node parent, NodeType nodeType) {
        this.point = point;
        this.cost = cost;
        this.parent = parent;
        this.nodeType = nodeType;
        calculate();
    }

    public Point getPoint() {
        return point;
    }

    public Node getParent() {
        return parent;
    }

    public void setNodeType(NodeType nodeType) {
        this.nodeType = nodeType;
    }

    public NodeType getNodeType() {
        return nodeType;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
        calculate();
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    @Override
    public int compareTo(Node node) {
        if (totalCost < node.totalCost) return -1;
        else if (totalCost > node.totalCost) return 1;
        else return manhattanDistance.compareTo(node.manhattanDistance);
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
