package Week13;
// Maksim Al Dandan

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        Graph<Integer, Integer> graph = new Graph<>();

        for (int i = 0; i < n; i++) {
            graph.addVertex(i);
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int weight = scanner.nextInt();
//                if (weight == 0) continue;
                graph.addEdge(graph.vertices.get(i), graph.vertices.get(j), weight);
            }
        }

        NegCycleBellmanFord(graph, 0);
        scanner.close();
    }

    // Taken from https://www.geeksforgeeks.org/print-negative-weight-cycle-in-a-directed-graph/amp/
    public static void NegCycleBellmanFord(Graph<Integer, Integer> graph, int src) {
        int V = graph.vertices.size();
        int E = graph.edges.size();
        int[] dist = new int[V];
        int[] parent = new int[V];

        for (int i = 0; i < V; i++) {
            dist[i] = Integer.MAX_VALUE;
            parent[i] = i;
        }

        dist[src] = 0;
        for (int i = 1; i <= V - 1; i++) {
            for (int j = 0; j < E; j++) {
                int u = graph.edges.get(j).from.label;
                int v = graph.edges.get(j).to.label;
                int weight = graph.edges.get(j).weight;

                if (dist[u] != Integer.MAX_VALUE && dist[u] + weight < dist[v]) {
                    dist[v] = dist[u] + weight;
                    parent[v] = u;
                }
            }
        }

        int C = -1;
        for (int i = 0; i < E; i++) {
            int u = graph.edges.get(i).from.label;
            int v = graph.edges.get(i).to.label;
            int weight = graph.edges.get(i).weight;

            if (dist[u] != Integer.MAX_VALUE && dist[u] + weight < dist[v]) {
                C = v;
                break;
            }
        }

        if (C != -1) {
            for (int i = 0; i < V; i++)
                C = parent[C];
            List<Integer> cycle = new ArrayList<>();
            for (int v = C;; v = parent[v]) {
                cycle.add(v);
                if (v == C && cycle.size() > 1)
                    break;
            }
            Collections.reverse(cycle);

            System.out.println("YES");
            System.out.println(cycle.size() - 1);

            for (int i = 0; i < cycle.size(); i++) {
                int v = cycle.get(i);
                if (i == cycle.size() - 1) break;
                System.out.print((v + 1) + " ");
            }
            System.out.println();
        } else
            System.out.println("NO");
    }
}

// Taken from lab10
class Graph<V, E> {

    public class Vertex {
        V label;
        List<Edge> adjacent;

        public Vertex(V label) {
            this.label = label;
            this.adjacent = new LinkedList<>();
        }
    }

    public class Edge {
        Vertex from;
        Vertex to;
        E weight;

        public Edge(Vertex from, Vertex to, E weight) {
            this.from = from;
            this.to = to;
            this.weight = weight;
        }
    }

    List<Vertex> vertices;
    List<Edge> edges;

    public Graph() {
        this.vertices = new LinkedList<>();
        this.edges = new LinkedList<>();
    }

    public Vertex addVertex(V label) {
        Vertex v = new Vertex(label);
        this.vertices.add(v);
        return v;
    }

    public void removeVertex(Vertex v) {
        this.vertices.remove(v);
        try {
            for (Edge e : v.adjacent) {
                this.edges.remove(e);
            }
        } catch (Exception ignored) {}
    }

    public Edge addEdge(Vertex from, Vertex to, E weight) {
        Edge e = new Edge(from, to, weight);
        this.edges.add(e);
        from.adjacent.add(e);
        to.adjacent.add(e);
        return e;
    }

    public void removeEdge(Edge e) {
        this.edges.remove(e);
        e.from.adjacent.remove(e);
        e.to.adjacent.remove(e);
    }
}
