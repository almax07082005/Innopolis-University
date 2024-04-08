package Week12;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {

    interface DisjointIntSet {
        int find(int i);
        void union(int i, int j);
    }

    public static class Kruskal {
        static class Edge implements Comparable<Edge> {
            int src;
            int dest;
            int weight;

            public Edge(int src, int dest, int weight) {
                this.src = src;
                this.dest = dest;
                this.weight = weight;
            }

            @Override
            public int compareTo(Edge o) {
                return Integer.compare(this.weight, o.weight);
            }
        }

        public static List<Edge> kruskalMST(List<Edge> edges, int n) {
            DisjointIntSet disjoint = new DisjointIntSet() {
                final int[] parent = new int[n];

                @Override
                public int find(int i) {
                    if (parent[i] == 0) return i;
                    parent[i] = find(parent[i]);
                    return parent[i];
                }

                @Override
                public void union(int i, int j) {
                    parent[find(i)] = find(j);
                }
            };

            List<Edge> mst = new ArrayList<>();
            Collections.sort(edges);

            for (Edge e : edges) {
                if (disjoint.find(e.src) != disjoint.find(e.dest)) {
                    disjoint.union(e.src, e.dest);
                    mst.add(e);
                }
            }

            return mst;
        }
    }

    public static void main(String[] args) {
        List<Kruskal.Edge> edges = new ArrayList<>();

        edges.add(new Kruskal.Edge(0, 1, 1));
        edges.add(new Kruskal.Edge(0, 2, 3));
        edges.add(new Kruskal.Edge(1, 2, 1));
        edges.add(new Kruskal.Edge(1, 3, 1));
        edges.add(new Kruskal.Edge(2, 3, 1));

        List<Kruskal.Edge> mst = Kruskal.kruskalMST(edges, 4);

        System.out.println("Edges in MST:");
        for (Kruskal.Edge e : mst) {
            System.out.println(e.src + " " + e.dest + " with weight " + e.weight);
        }
    }
}
