import finalproject.Graph;
import finalproject.GraphTraversal;
import finalproject.ShortestPath;
import finalproject.TilePriorityQ;
import finalproject.system.Tile;
import finalproject.tiles.*;

import java.util.ArrayList;

public class testClass {
    public static void main(String[] args) {
        Tile D1= new DesertTile();
        Tile D2= new DesertTile();
        Tile M1= new MountainTile();
        Tile F1= new FacilityTile();
        Tile F2= new FacilityTile();
        Tile P1= new PlainTile();
        Tile P2= new PlainTile();
        Tile P3= new PlainTile();
        Tile P4= new PlainTile();
        F1.xCoord = 1;F1.yCoord = 1;
        P1.xCoord = 1;P1.yCoord = 2;
        P2.xCoord = 1;P2.yCoord = 3;
        D1.xCoord = 2;D1.yCoord = 1;
        M1.xCoord = 2;M1.yCoord = 2;
        D2.xCoord = 2;D2.yCoord = 3;
        P3.xCoord = 3;P3.yCoord = 1;
        P4.xCoord = 3;P4.yCoord = 2;
        F2.xCoord = 3;F2.yCoord = 3;


        F1.costEstimate=1;
        P1.costEstimate=3;
        P2.costEstimate=3;
        D1.costEstimate=2;
        M1.costEstimate=100;
        D2.costEstimate=2;
        P3.costEstimate=3;
        P4.costEstimate=3;
        F2.costEstimate=1;




        ArrayList<Tile> vertices = new ArrayList<>();
        vertices.addLast(F1);
        vertices.addLast(P1);
        vertices.addLast(P2);
        vertices.addLast(D1);
        vertices.addLast(M1);
        vertices.addLast(D2);
        vertices.addLast(P3);
        vertices.addLast(P4);
        vertices.addLast(F2);

        Graph g = new Graph(vertices);
        F1.addNeighbor(D1);
        F1.addNeighbor(P1);
        F1.addNeighbor(M1);
        P1.addNeighbor(P2);
        P1.addNeighbor(M1);




        D2.addNeighbor(M1);

        M1.addNeighbor(P1);
        M1.addNeighbor(P4);
        M1.addNeighbor(D1);
        M1.addNeighbor(D2);
        D2.addNeighbor(P2);
        D2.addNeighbor(M1);
        D2.addNeighbor(F2);
        P3.addNeighbor(D1);
        P3.addNeighbor(P4);
        P4.addNeighbor(M1);
        P4.addNeighbor(P3);
        P4.addNeighbor(F2);
        F2.addNeighbor(D2);
        F2.addNeighbor(P4);






        g.addEdge(F1, P1, 1);
        g.addEdge(P1, P2, 1);
        g.addEdge(P2, D2, 1);
        g.addEdge(D1, P3, 1);
        g.addEdge(P3, P4, 1);
        g.addEdge(P4, F2, 1);


//        ArrayList<Tile> test = GraphTraversal.DFS(D2);
//        for (Tile i : test) {
//            System.out.println(i.xCoord+"   "+i.yCoord);
//
//        }
//
//        TilePriorityQ t = new TilePriorityQ(vertices);
//
//
//
//
//        ArrayList<Tile> list = t.heap;
//        for (Tile element : list) {
//            if (element==null) System.out.println("null");
//            else System.out.println(element.costEstimate);
//        }
        ArrayList<Tile> travers = GraphTraversal.BFS(F1);
        System.out.println("travers: "+ travers);
        ShortestPath p = new ShortestPath(F1);

        System.out.println("findpath F1             :"+p.findPath(F1,F2));


        MetroTile m1 = new MetroTile();
        double x = m1.metroTimeCost;








    }
}
