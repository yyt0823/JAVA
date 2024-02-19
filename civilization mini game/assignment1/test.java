package assignment1;

public class test {
    public static void main(String[] args) {
        Tile t11 = new Tile(1,1);
        Tile t12 = new Tile(1,2);
        Tile t13 = new Tile(1,3);
        Tile t21 = new Tile(2,1);
        Tile t22 = new Tile(2,2);
        Tile t23 = new Tile(2,3);

        Settler s1 = new Settler(t11,10,"f1");
        Warrior w1 = new Warrior(t12,10,"f1");
        s1.takeAction(t11);
        System.out.println(t11.getUnitsOnTile());
        System.out.println(t11.getUnitsOnTile().indexOf(s1));
        System.out.println(t12.getUnitsOnTile());



    }
}


