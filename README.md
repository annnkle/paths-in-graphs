# paths-in-graphs
Generating undirected graphs of a given number of nodes and finding Eulerian paths/cycles and/or Hamiltonian cycles within those graphs. 

![86py6c](https://github.com/annnkle/paths-in-graphs/assets/95099151/06f38a95-df8f-403e-8f55-6a57a06470b2)

### Eulerian paths and cycles
An Eulerian path in a graph is a path traversing each edge exactly once. A cycle is a path that starts and ends with the same vertex. To find an Eulerian path or cycle in a graph I'm using Fleury's algorithm.
#### The algorithm... 
...starts at a vertex of odd degree, or, if the graph has none, it starts with an arbitrarily chosen vertex. At each step it chooses the next edge in the path to be one whose deletion would not disconnect the graph, unless there is no such edge, in which case it picks the remaining edge left at the current vertex. It then moves to the other endpoint of that edge and deletes the edge. At the end of the algorithm there are no edges left, and the sequence from which the edges were chosen forms an Eulerian cycle if the graph has no vertices of odd degree, or an Eulerian trail if there are exactly two vertices of odd degree. 

![86rkzx](https://github.com/annnkle/paths-in-graphs/assets/95099151/e0474736-9f67-4387-a128-5c0d244e7bd6)

### Hamiltonian cycles
A Hamiltonian cycle is a cycle in a graph visiting each vertex exactly once. Finding such cycle in a graph is an NP-complete problem, which means there is no known efficient algorithm solving this in all graphs. I've circumvented this by first ensuring that the graph is Hamiltonian (per Dirac's theorem) and then using **backtracking algorithm**.
#### The algorithm...
    ...creates an empty path array and adds vertex 0 to it. Other vertices are added, starting from the vertex 1. Before adding a vertex, checks for whether it is adjacent to the previously added vertex and not already added. If such vertex is found, it's added as part of the solution. If not, then false is returned.

![86rmxg](https://github.com/annnkle/paths-in-graphs/assets/95099151/e7fd0261-4f44-49c9-b6f3-780ccef6ad09)

### Graph generation
Each edge is added to the graph semi-randomly, as I've implemented toggle for generation of graph with either Hamiltonian or Eulerian properties.
- Hamiltonian graph is created according to Dirac's theorem.
- Eulerian graph is created with maximum of two odd-degree vertices, as to comply with Fleury's algorithm.

Created for Algorithms and Data Structures course during my second term of Bioinformatics BSc. 
