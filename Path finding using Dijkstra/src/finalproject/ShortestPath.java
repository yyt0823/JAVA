package finalproject;


import finalproject.system.Tile;
import finalproject.system.TileType;
import finalproject.tiles.MetroTile;

import java.util.ArrayList;

public class ShortestPath extends PathFindingService {
    //TODO level 4: find distance prioritized path


    public ShortestPath(Tile start) {

        super(start);
        //call method
        generateGraph();
    }

    @Override
    public void generateGraph() {
        // TODO Auto-generated method stub

        //generate graph with all reachable tiles
        ArrayList<Tile> reachableTiles = GraphTraversal.BFS(this.source);
        Graph graph = new Graph(reachableTiles);

        // Generate a list of all reachable tiles from the start tile

        // Add each tile to the graph and connect them with weighted edges

        for (Tile tile : reachableTiles) {
            for (Tile neighbor : tile.neighbors) {
                double distanceCost;
                if (neighbor.type == TileType.Metro && tile.type == TileType.Metro) {
                    ((MetroTile) neighbor).fixMetro(tile);
                    distanceCost = ((MetroTile) neighbor).metroDistanceCost;
                } else {
                    distanceCost = neighbor.distanceCost;
                }
                graph.addEdge(tile, neighbor, distanceCost);
            }
        }
        super.g = graph;
    }


}