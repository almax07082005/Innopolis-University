package Week10;

import java.util.ArrayList;
import java.util.List;

public class Graph<V, E> {

    public class Vertex {
        V label;
        List<Edge> adjacent;

        public Vertex(V label) {
            this.label = label;
            this.adjacent = new ArrayList<>();
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
        this.vertices = new ArrayList<>();
        this.edges = new ArrayList<>();
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
