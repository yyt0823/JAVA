package finalproject;

import finalproject.system.Tile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;


public abstract class PathFindingService {
	Tile source;
    Graph g;


	
	public PathFindingService(Tile start) {
    	this.source = start;
    }

	public abstract void generateGraph();
    
    //TODO level 4: Implement basic dijkstra's algorithm to find a path to the final unknown destination
    public ArrayList<Tile> findPath(Tile startNode) {
        initSingleSource(startNode);
        ArrayList<Tile> S = new ArrayList<>();
        TilePriorityQ Q = new TilePriorityQ(g.vertices);
        while (Q.heap.size()>1) {

            Tile u = Q.removeMin();
            S.add(u);
            if (u.isDestination)break;
            for (Tile v : g.getNeighbors(u)) {
                relax(u,v,getWeight(u,v),Q);
            }
            
        }
        ArrayList<Tile> r = new ArrayList<>();
        r.add(S.getLast());
        Tile temp = S.getLast();
        while(temp.predecessor!=null){

            r.addFirst(temp.predecessor);
            temp = temp.predecessor;

        }


        return r;

    }
    
    //TODO level 5: Implement basic dijkstra's algorithm to path find to a known destination
    public ArrayList<Tile> findPath(Tile start, Tile end) {
        initSingleSource(start);
        ArrayList<Tile> S = new ArrayList<>();
        TilePriorityQ Q = new TilePriorityQ(g.vertices);
        while (Q.heap.size()>1) {

            Tile u = Q.removeMin();
            S.add(u);
            if (u==end) break;

            for (Tile v : g.getNeighbors(u)) {
                relax(u,v,getWeight(u,v),Q);
            }

        }
        ArrayList<Tile> r = new ArrayList<>();
        r.add(S.getLast());
        Tile temp = S.getLast();
        while(temp.predecessor!=null){

            r.addFirst(temp.predecessor);
            temp = temp.predecessor;

        }
        return r;


    }

    //TODO level 5: Implement basic dijkstra's algorithm to path find to the final destination passing through given waypoints
    public ArrayList<Tile> findPath(Tile start, LinkedList<Tile> waypoints){
        initSingleSource(start);
        if (waypoints.isEmpty()) return findPath(start);
        ArrayList<Tile> r = new ArrayList<>(findPath(start, waypoints.get(0)));
        for (int i = 0; i < waypoints.size()-1; i++) {
            ArrayList<Tile> pathList = findPath(waypoints.get(i),waypoints.get(i+1));
            r.addAll(pathList.subList(1,pathList.size()));
        }
        ArrayList<Tile> pathListLast = findPath(waypoints.getLast());
        r.addAll(pathListLast.subList(1,pathListLast.size()));
    	return r;
    }







    //helper method relax
    public void relax(Tile u, Tile v, double weight,TilePriorityQ Q) {
        double newCost = u.costEstimate + weight;
        if (v.costEstimate > newCost) {
            Q.updateKeys(v,u,newCost);
        }
    }

    public void initSingleSource(Tile source) {
        for (Tile v : g.vertices) {
            v.costEstimate = Double.MAX_VALUE;
            v.predecessor = null;
        }
        source.costEstimate = 0;
    }

    public double getWeight(Tile u, Tile v){
        for (Graph.Edge i: g.getAllEdges()) {
            if (i.getStart()==u && i.getEnd()==v) return i.weight;
        }
        return Double.MAX_VALUE;
    }


}

