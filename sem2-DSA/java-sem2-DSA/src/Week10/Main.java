package Week10;

public class Main {
    public static void main(String[] args) {
        Graph<String, Integer> graph = new Graph<>();
        Graph<String, Integer>.Vertex a = graph.addVertex("A");
        Graph<String, Integer>.Vertex b = graph.addVertex("B");
        Graph<String, Integer>.Vertex c = graph.addVertex("C");

        for (Graph<String, Integer>.Vertex v : graph.vertices) {
            System.out.println(v.label);
        }

        graph.removeVertex(a);
        graph.removeVertex(b);

        for (Graph<String, Integer>.Vertex v : graph.vertices) {
            System.out.println(v.label);
        }

        Graph<String, Integer>.Edge ab = graph.addEdge(a, b, 1);
        Graph<String, Integer>.Edge bc = graph.addEdge(b, c, 2);
        Graph<String, Integer>.Edge ca = graph.addEdge(c, a, 3);

        for (Graph<String, Integer>.Edge e : graph.edges) {
            System.out.println(e.from.label + " -> " + e.to.label + " : " + e.weight);
        }

        graph.removeEdge(ab);
        graph.removeEdge(bc);

        for (Graph<String, Integer>.Edge e : graph.edges) {
            System.out.println(e.from.label + " -> " + e.to.label + " : " + e.weight);
        }
    }
}
