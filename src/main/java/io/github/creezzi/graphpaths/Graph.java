package io.github.creezzi.graphpaths;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.PriorityQueue;

/** Directed graph with non-negative edge weights. */
public final class Graph {
    public record Path(double cost, List<String> nodes) {}

    private record Edge(String to, double weight) {}

    private record Entry(String node, double dist) {}

    private final Map<String, List<Edge>> adjacency = new HashMap<>();

    public void addEdge(String from, String to, double weight) {
        if (weight < 0 || Double.isNaN(weight)) {
            throw new IllegalArgumentException("weight must be non-negative");
        }
        adjacency.computeIfAbsent(from, k -> new ArrayList<>()).add(new Edge(to, weight));
        adjacency.computeIfAbsent(to, k -> new ArrayList<>());
    }

    public void addUndirectedEdge(String a, String b, double weight) {
        addEdge(a, b, weight);
        addEdge(b, a, weight);
    }

    public Optional<Path> shortestPath(String from, String to) {
        if (!adjacency.containsKey(from) || !adjacency.containsKey(to)) {
            return Optional.empty();
        }
        Map<String, Double> dist = new HashMap<>();
        Map<String, String> prev = new HashMap<>();
        PriorityQueue<Entry> queue = new PriorityQueue<>((a, b) -> Double.compare(a.dist(), b.dist()));
        dist.put(from, 0.0);
        queue.add(new Entry(from, 0.0));
        while (!queue.isEmpty()) {
            Entry cur = queue.poll();
            if (cur.dist() > dist.getOrDefault(cur.node(), Double.POSITIVE_INFINITY)) {
                continue; // stale queue entry
            }
            if (cur.node().equals(to)) {
                break;
            }
            for (Edge e : adjacency.get(cur.node())) {
                double next = cur.dist() + e.weight();
                if (next < dist.getOrDefault(e.to(), Double.POSITIVE_INFINITY)) {
                    dist.put(e.to(), next);
                    prev.put(e.to(), cur.node());
                    queue.add(new Entry(e.to(), next));
                }
            }
        }
        if (!dist.containsKey(to)) {
            return Optional.empty();
        }
        List<String> nodes = new ArrayList<>();
        for (String at = to; at != null; at = prev.get(at)) {
            nodes.add(at);
        }
        Collections.reverse(nodes);
        return Optional.of(new Path(dist.get(to), nodes));
    }
}
