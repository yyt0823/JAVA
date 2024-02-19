package finalproject;

import java.util.ArrayList;
import java.util.HashSet;

import java.util.Map;

import finalproject.system.Tile;
import finalproject.system.TileType;
import finalproject.tiles.*;

public class Graph {
    // TODO level 2: Add fields that can help you implement this data type

    public ArrayList<Tile> vertices;
    private ArrayList<ArrayList<Edge>> adjacencyList;

    // TODO level 2: initialize and assign all variables inside the constructor
    public Graph(ArrayList<Tile> vertices) {
        this.vertices = vertices;
        this.adjacencyList = new ArrayList<ArrayList<Edge>>();
        for (int i = 0; i < vertices.size(); i++) {
            adjacencyList.add(new ArrayList<>());
        }
    }

    // TODO level 2: add an edge to the graph
    public void addEdge(Tile origin, Tile destination, double weight) {
        if (origin.isWalkable() && destination.isWalkable()) {
            int originIndex = vertices.indexOf(origin);
            if (originIndex != -1) {
                //create edge and add to list
                Edge edge = new Edge(origin, destination, weight);
                adjacencyList.get(originIndex).add(edge);
            }

        }
        //find starting vertices


    }

    // TODO level 2: return a list of all edges in the graph
    public ArrayList<Edge> getAllEdges() {

        //create return array neighbors
        ArrayList<Edge> allEdges = new ArrayList<>();
        //add all edges
        for (ArrayList<Edge> edges : adjacencyList) {
            allEdges.addAll(edges);
        }
        return allEdges;
    }

    // TODO level 2: return list of tiles adjacent to t
    public ArrayList<Tile> getNeighbors(Tile t) {
        //check if input is null

        //!!!check!!!
        if (t == null) return new ArrayList<>();
        //create return array neighbors
        ArrayList<Tile> neighbors = new ArrayList<>();

        //find edge starting point
        int tileIndex = vertices.indexOf(t);
        if (tileIndex != -1) {
            //find all edge and get the destination(neighbors)
            for (Edge edge : adjacencyList.get(tileIndex)) {
                neighbors.add(edge.getEnd());
            }
        }
        return neighbors;
    }

    // TODO level 2: return total cost for the input path
    public double computePathCost(ArrayList<Tile> path) {

        //new variable totalCost
        double totalCost = 0.0;

        //iterate each edge in the path
        for (int i = 0; i < path.size() - 1; i++) {
            Tile start = path.get(i);
            int startIndex = vertices.indexOf(start);
            Tile end = path.get(i + 1);

            //find the path weight and add them up
            for (Edge edge : adjacencyList.get(startIndex)) {
                if (edge.getEnd().equals(end)) {
                    totalCost += edge.weight;
                    break;
                }
            }
        }
        return totalCost;
    }


    public static class Edge {
        Tile origin;
        Tile destination;
        double weight;

        // TODO level 2: initialize appropriate fields
        public Edge(Tile s, Tile d, double cost) {
            this.origin = s;
            this.destination = d;
            this.weight = cost;

        }

        // TODO level 2: getter function 1
        public Tile getStart() {
            return origin;
        }


        // TODO level 2: getter function 2
        public Tile getEnd() {
            return destination;
        }

    }

}