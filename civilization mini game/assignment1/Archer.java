package assignment1;
public class Archer extends MilitaryUnit{
    private int arrowsAvailable;
    public Archer(Tile position, double hp, String faction) {
        super(position, hp, 2, faction, 15.0, 2, 0);
        this.arrowsAvailable = 5;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public void takeAction(Tile tile) {
        if (this.arrowsAvailable == 0){
            this.arrowsAvailable = 5;
        }
        else {
            this.arrowsAvailable--;
            super.takeAction(tile);
        }
    }
}
