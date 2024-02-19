package finalproject;

import finalproject.system.Tile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;

public class GraphTraversal {


    //TODO level 1: implement BFS traversal starting from s
    public static ArrayList<Tile> BFS(Tile s) {
        //check if not null and is Walkable
        if (s != null && s.isWalkable()) {
            ArrayList<Tile> traversalOrder = new ArrayList<>();
            HashSet<Tile> visited = new HashSet<>();
            LinkedList<Tile> queue = new LinkedList<>();

            visited.add(s);
            queue.add(s);

            while (!queue.isEmpty()) {
                //get the first tile from list
                Tile currentTile = queue.poll();
                traversalOrder.add(currentTile);
                //add its neighbor
                for (Tile neighbor : currentTile.neighbors) {

                    if (!visited.contains(neighbor) && neighbor.isWalkable()) {
                        visited.add(neighbor);
                        queue.add(neighbor);
                    }
                }
            }

            return traversalOrder;
        }
        return new ArrayList<>();
    }


    //TODO level 1: implement DFS traversal starting from s
    public static ArrayList<Tile> DFS(Tile s) {

        ArrayList<Tile> traversalOrder = new ArrayList<>();
        HashSet<Tile> visited = new HashSet<>();

        if (s != null && s.isWalkable()) {
            dfsRecursive(s, visited, traversalOrder);
        }
        return traversalOrder;

    }

    //helper
    private static void dfsRecursive(Tile tile, HashSet<Tile> visited, ArrayList<Tile> traversalOrder) {
        visited.add(tile);
        traversalOrder.add(tile);

        for (Tile neighbor : tile.neighbors) {
            if (!visited.contains(neighbor) && neighbor.isWalkable()) {
                dfsRecursive(neighbor, visited, traversalOrder);
            }
        }
    }

}  