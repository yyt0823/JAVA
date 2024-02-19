package assignment1;
public class Tile {
    private int x;
    private int y;
    private boolean isCity;
    private boolean isImproved;
    private ListOfUnits unitsOnTile;

    public Tile(int x, int y) {
        this.x = x;
        this.y = y;
        this.isCity = false;
        this.isImproved = false;
        this.unitsOnTile = new ListOfUnits();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isCity() {
        return this.isCity;
    }

    public boolean isImproved() {
        return this.isImproved;
    }

    public void buildCity() {
        this.isCity = true;
    }

    public void buildImprovement() {
        this.isImproved = true;
    }

    public boolean addUnit(Unit unit) {
        if (!(unit instanceof MilitaryUnit)) {
            this.unitsOnTile.addUnit(unit);
            return true;
        } else {
            for (int i = 0; i < unitsOnTile.getArmy().length; i++) {
                if (!(unitsOnTile.getArmy()[i].getFaction().equals(unit.getFaction()))) {
                    return false;
                }
            }
            unitsOnTile.addUnit(unit);
            return true;
        }
    }

    public boolean removeUnit(Unit unit) {
        for (int i = 0; i < unitsOnTile.getSize(); i++) {
            if (unit.equals(unitsOnTile.getList()[i])) {
                unitsOnTile.removeUnit(unit);
                return true;
            }
        }
        return false;
    }

    public Unit selectWeakEnemy(String faction) {
        Unit weakEnemy = null;
        double lowestHP = Double.MAX_VALUE;
        for (int i = 0; i < unitsOnTile.getSize(); i++) {
            Unit current = unitsOnTile.getUnit(i);
            if (!current.getFaction().equals(faction) && current.getHP() < lowestHP) {
                lowestHP = current.getHP();
                weakEnemy = current;
            }
        }
        return weakEnemy;
    }

    public static double getDistance(Tile tile1, Tile tile2) {
        return Math.sqrt(((tile1.x - tile2.x) * (tile1.x - tile2.x) + (tile1.y - tile2.y) * (tile1.y - tile2.y)));
    }

    //testing !!!!!!!
    public ListOfUnits getUnitsOnTile() {
        return unitsOnTile;
    }

    //
}


