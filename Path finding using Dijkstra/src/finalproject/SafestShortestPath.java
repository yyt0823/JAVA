package finalproject;


import java.util.ArrayList;
import java.util.LinkedList;

import finalproject.system.Tile;
import finalproject.system.TileType;
import finalproject.tiles.MetroTile;

public class SafestShortestPath extends ShortestPath {
    public int health;
    public Graph costGraph;
    public Graph damageGraph;
    public Graph aggregatedGraph;

    //TODO level 8: finish class for finding the safest shortest path with given health constraint
    public SafestShortestPath(Tile start, int health) {
        super(start);
        this.health = health;
    }


    public void generateGraph() {
        // TODO Auto-generated method stub
        //costGraph
        this.costGraph = new ShortestPath(source).g;
        //damage graph
        ArrayList<Tile> reachableTiles = GraphTraversal.BFS(super.source);
        Graph dmgGraph = new Graph(reachableTiles);
        Graph aggGraph = new Graph(reachableTiles);

        for (Tile tile : reachableTiles) {
            for (Tile neighbor : tile.neighbors) {
                double dmgCost = neighbor.damageCost;
                dmgGraph.addEdge(tile, neighbor, dmgCost);
                aggGraph.addEdge(tile, neighbor, dmgCost);
            }
        }

        this.damageGraph = dmgGraph;
        this.aggregatedGraph = aggGraph;

    }

    @Override
    public ArrayList<Tile> findPath(Tile start, LinkedList<Tile> waypoints) {

        //set to cost graph and compute
        super.g = costGraph;

        ArrayList<Tile> pc = super.findPath(start, waypoints);

        double dm = 0;
        for (Tile t: pc) {
            dm += t.damageCost;
        }
        if (dm < health) {
            return pc;
        }
        //set to damage graph and compute
        this.g = damageGraph;
        ArrayList<Tile> pd = super.findPath(start, waypoints);
        if (g.computePathCost(pd) > health) {
            return null;
        }

        //step 4
        this.g = aggregatedGraph;
        while (true) {
            computeAndUpdateMultiplier(pc, pd);
//            System.out.println("reached");
            ArrayList<Tile> pr = super.findPath(start, waypoints);

            if (g.computePathCost(pr) == g.computePathCost(pc)) {
                return pd;
            } else if (damageGraph.computePathCost(pr) <= health) {
                pd = pr;
            } else {
                pc = pr;
            }

            System.out.println("    "+ g.computePathCost(pr)+"         " + g.computePathCost(pc));

        }
    }


    //helper
    public void computeAndUpdateMultiplier(ArrayList<Tile> pc, ArrayList<Tile> pd) {
        double multiplier = (costGraph.computePathCost(pc) - costGraph.computePathCost(pd)) / (damageGraph.computePathCost(pd) - damageGraph.computePathCost(pc));
        //update
//        for (Graph.Edge i : this.g.getAllEdges()) {
//            double c = -1, d = -1;
//
//            for (Graph.Edge edges : costGraph.getAllEdges()) {
//                if (edges.getEnd() == i.getEnd() && edges.getStart()==i.getStart()) {
//                    c = edges.weight;
//                    break;
//                }
//            }
//            for (Graph.Edge edges : damageGraph.getAllEdges()) {
//                if (edges.getEnd() == i.getEnd() && edges.getStart()==i.getStart()) {
//                    d = edges.weight;
//                    break;
//                }
//            }
//            i.weight = c + multiplier * d;
//        }
        ArrayList<Graph.Edge> edges = g.getAllEdges();
        for (Graph.Edge edge: edges) {
            Tile destination = edge.destination;
            edge.weight=destination.distanceCost + multiplier*destination.damageCost;
        }


//        for (Graph.Edge e:g.getAllEdges()
//        ) {System.out.println(e.weight);
//
//        }
//        System.exit(5);
    }
}
