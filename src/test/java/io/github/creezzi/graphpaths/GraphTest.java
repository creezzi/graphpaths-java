package io.github.creezzi.graphpaths;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.Test;

class GraphTest {
    @Test
    void prefersCheaperLongerRoute() {
        Graph g = new Graph();
        g.addEdge("A", "B", 4);
        g.addEdge("A", "C", 1);
        g.addEdge("C", "B", 2);
        g.addEdge("B", "D", 1);
        Graph.Path p = g.shortestPath("A", "D").orElseThrow();
        assertEquals(4.0, p.cost());
        assertEquals(List.of("A", "C", "B", "D"), p.nodes());
    }

    @Test
    void sameNodeCostsNothing() {
        Graph g = new Graph();
        g.addEdge("A", "B", 1);
        assertEquals(new Graph.Path(0.0, List.of("A")), g.shortestPath("A", "A").orElseThrow());
    }

    @Test
    void unreachableOrUnknownNodesGiveEmpty() {
        Graph g = new Graph();
        g.addEdge("A", "B", 1);
        assertTrue(g.shortestPath("B", "A").isEmpty());
        assertTrue(g.shortestPath("A", "Z").isEmpty());
    }

    @Test
    void directedEdgesRespectDirection() {
        Graph g = new Graph();
        g.addEdge("A", "B", 1);
        assertTrue(g.shortestPath("B", "A").isEmpty());
        g.addUndirectedEdge("B", "C", 2);
        assertEquals(3.0, g.shortestPath("A", "C").orElseThrow().cost());
        assertEquals(2.0, g.shortestPath("C", "B").orElseThrow().cost());
    }

    @Test
    void zeroWeightEdgesAreFine() {
        Graph g = new Graph();
        g.addEdge("A", "B", 0);
        g.addEdge("B", "C", 0);
        assertEquals(0.0, g.shortestPath("A", "C").orElseThrow().cost());
    }

    @Test
    void rejectsNegativeWeights() {
        assertThrows(IllegalArgumentException.class, () -> new Graph().addEdge("A", "B", -1));
    }
}
