# graphpaths

Weighted directed graphs with Dijkstra shortest paths.

## Usage

```java
Graph g = new Graph();
g.addEdge("A", "B", 4);
g.addEdge("A", "C", 1);
g.addEdge("C", "B", 2);
g.shortestPath("A", "B");  // Path[cost=3.0, nodes=[A, C, B]]
```

## Test

```bash
mvn test
```

## License

MIT
