// Maksim Al Dandan
package Week7;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        int nodesAmount = Integer.parseInt(reader.readLine());
        AVLTree<Integer> tree = new AVLTree<>();
        String[] nodes = reader.readLine().split(" ");
        for (String node : nodes) {
            tree.insert(Integer.parseInt(node));
        }

        System.out.println(nodesAmount);
        showTree(tree);

        reader.close();
    }

    public static void showTree(AVLTree<Integer> tree) {
        List<Node<Integer>> nodes = new ArrayList<>();
        nodes.add(tree.getRoot());
        int linePrinted = 0;

        for (int i = 0; i <= tree.height(); i++) {
            List<Node<Integer>> newNodes = new ArrayList<>();

            for (Node<Integer> node : nodes) {
                System.out.print(node.key + " ");

                if (node.left != null) {
                    newNodes.add(node.left);
                    System.out.print(linePrinted + nodes.size() + newNodes.size() + " ");
                } else {
                    System.out.print("-1 ");
                }

                if (node.right != null) {
                    newNodes.add(node.right);
                    System.out.print(linePrinted + nodes.size() + newNodes.size() + " ");
                } else {
                    System.out.print("-1 ");
                }

                System.out.println();
            }

            linePrinted += nodes.size();
            nodes = newNodes;
        }

        System.out.println(1);
    }
}

class Node<T extends Comparable<T>> {
    T key;
    int height;
    Node<T> left;
    Node<T> right;

    Node(T key) {
        this.key = key;
    }
}

class AVLTree<T extends Comparable<T>> {

    private Node<T> root;

    public Node<T> find(T key) {
        Node<T> current = root;
        while (current != null) {
            if (current.key.compareTo(key) == 0) {
               break;
            }
            current = current.key.compareTo(key) < 0 ? current.right : current.left;
        }
        return current;
    }

    public void insert(T key) {
        root = insert(root, key);
    }

    public void delete(T key) {
        root = delete(root, key);
    }

    public Node<T> getRoot() {
        return root;
    }

    public int height() {
        return root == null ? -1 : root.height;
    }

    private Node<T> insert(Node<T> root, T key) {
        if (root == null) {
            return new Node<>(key);
        } else if (root.key.compareTo(key) > 0) {
            root.left = insert(root.left, key);
        } else if (root.key.compareTo(key) < 0) {
            root.right = insert(root.right, key);
        } else {
            throw new RuntimeException("duplicate Key!");
        }
        return rebalance(root);
    }

    private Node<T> delete(Node<T> node, T key) {
        if (node == null) {
            return node;
        } else if (node.key.compareTo(key) > 0) {
            node.left = delete(node.left, key);
        } else if (node.key.compareTo(key) < 0) {
            node.right = delete(node.right, key);
        } else {
            if (node.left == null || node.right == null) {
                node = (node.left == null) ? node.right : node.left;
            } else {
                Node<T> mostLeftChild = mostLeftChild(node.right);
                node.key = mostLeftChild.key;
                node.right = delete(node.right, node.key);
            }
        }
        if (node != null) {
            node = rebalance(node);
        }
        return node;
    }

    private Node<T> mostLeftChild(Node<T> node) {
        Node<T> current = node;
        /* loop down to find the leftmost leaf */
        while (current.left != null) {
            current = current.left;
        }
        return current;
    }

    private Node<T> rebalance(Node<T> z) {
        updateHeight(z);
        int balance = getBalance(z);
        if (balance > 1) {
            if (height(z.right.right) > height(z.right.left)) {
                z = rotateLeft(z);
            } else {
                z.right = rotateRight(z.right);
                z = rotateLeft(z);
            }
        } else if (balance < -1) {
            if (height(z.left.left) > height(z.left.right)) {
                z = rotateRight(z);
            } else {
                z.left = rotateLeft(z.left);
                z = rotateRight(z);
            }
        }
        return z;
    }

    private Node<T> rotateRight(Node<T> y) {
        Node<T> x = y.left;
        Node<T> z = x.right;
        x.right = y;
        y.left = z;
        updateHeight(y);
        updateHeight(x);
        return x;
    }

    private Node<T> rotateLeft(Node<T> y) {
        Node<T> x = y.right;
        Node<T> z = x.left;
        x.left = y;
        y.right = z;
        updateHeight(y);
        updateHeight(x);
        return x;
    }

    private void updateHeight(Node<T> n) {
        n.height = 1 + Math.max(height(n.left), height(n.right));
    }

    private int height(Node<T> n) {
        return n == null ? -1 : n.height;
    }

    public int getBalance(Node<T> n) {
        return (n == null) ? 0 : height(n.right) - height(n.left);
    }
}