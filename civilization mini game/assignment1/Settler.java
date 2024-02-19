package assignment1;
public class Settler extends Unit {
    public Settler(Tile position, double hp, String faction) {
        super(position, hp, 2, faction);
    }

    @Override
    public void takeAction(Tile tile) {
        if (tile.equals(this.getPosition()) && !this.getPosition().isCity()){
            this.getPosition().buildCity();
            this.getPosition().removeUnit(this);
        }
        return;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
