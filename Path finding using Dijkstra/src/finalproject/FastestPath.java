package finalproject;

import finalproject.system.Tile;
import finalproject.system.TileType;
import finalproject.tiles.MetroTile;


import java.util.ArrayList;

public class FastestPath extends PathFindingService {
    //TODO level 6: find time prioritized path
    public FastestPath(Tile start) {
        super(start);
        generateGraph();
    }

    @Override
    public void generateGraph() {
        // TODO Auto-generated method stub
        ArrayList<Tile> reachableTiles = GraphTraversal.BFS(this.source);
        this.g = new Graph(reachableTiles);

        for (Tile tile : reachableTiles) {
            for (Tile neighbor : tile.neighbors) {
                double distanceCost;
                if (neighbor.type == TileType.Metro && tile.type == TileType.Metro) {
                    ((MetroTile) neighbor).fixMetro(tile);
                    distanceCost = ((MetroTile) neighbor).metroTimeCost;
                } else {
                    distanceCost = neighbor.timeCost;
                }
                this.g.addEdge(tile, neighbor, distanceCost);
            }
        }
    }

}
