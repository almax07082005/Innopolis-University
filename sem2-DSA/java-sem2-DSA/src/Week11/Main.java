// Maksim Al Dandan
package Week11;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Graph<String, Integer> graph = new Graph<>();
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();

        for (int i = 0; i < n; i++) {
            graph.addVertex(String.valueOf(i));
        }
        for (int i = 0; i < n; i++) {
            String[] line = scanner.nextLine().split(" ");
            for (int j = 0; j < n; j++) {
                if (line[j].equals("1")) {
                    graph.addEdge(graph.vertices.get(i), graph.vertices.get(j), 1);
                }
            }
        }

        System.out.println(isFullyConnected(graph) ? "YES" : "NO");
    }

    public static <V, E> boolean isFullyConnected(Graph<V, E> graph) {
        Set<Graph<V, E>.Vertex> visited = new HashSet<>();
        MaksimAlDandan_dfs(graph, graph.vertices.getFirst(), visited);
        return visited.size() == graph.vertices.size();
    }

    public static <V, E> void MaksimAlDandan_dfs(Graph<V, E> graph, Graph<V, E>.Vertex start, Set<Graph<V, E>.Vertex> visited) {
        visited.add(start);
        for (Graph<V, E>.Edge e : start.adjacent) {
            Graph<V, E>.Vertex v = e.from == start ? e.to : e.from;
            if (!visited.contains(v)) {
                MaksimAlDandan_dfs(graph, v, visited);
            }
        }
    }

    public static <V, E> void MaksimAlDandan_bfs(Graph<V, E> graph, Graph<V, E>.Vertex start, Set<Graph<V, E>.Vertex> visited) {
        Queue<Graph<V, E>.Vertex> queue = new LinkedList<>();
        queue.add(start);
        visited.add(start);
        while (!queue.isEmpty()) {
            Graph<V, E>.Vertex v = queue.poll();
            for (Graph<V, E>.Edge e : v.adjacent) {
                Graph<V, E>.Vertex u = e.from == v ? e.to : e.from;
                if (!visited.contains(u)) {
                    queue.add(u);
                    visited.add(u);
                }
            }
        }
    }

// Taken from lab 10
    static class Graph<V, E> {

        class Vertex {
            V label;
            List<Edge> adjacent;

            public Vertex(V label) {
                this.label = label;
                this.adjacent = new LinkedList<>();
            }
        }

        class Edge {
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
}
