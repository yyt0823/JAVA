package assignment1;
public abstract class Unit {
    private Tile position;
    private double hp;
    private int movingRange;
    private String faction;

    public Unit(Tile position, double hp, int movingRange, String faction) {
        this.position = position;
        this.faction = faction;
        this.hp = hp;
        this.movingRange = movingRange;
        if (!position.addUnit(this)){
            throw new IllegalArgumentException("can not add unit: enemy on tile");
        }
    }

    public final Tile getPosition() {
        return this.position;
    }

    public final double getHP() {
        return this.hp;
    }

    public final String getFaction() {
        return this.faction;
    }

    public boolean moveTo(Tile tile) {
        if (tile == null){
            return false;
        }
        if (Tile.getDistance(this.position, tile) <= this.movingRange && tile.addUnit(this)) {
            this.position.removeUnit(this);
            this.position = tile;
            return true;
        } else return false;

    }

    public void receiveDamage(double dmg){
        if (dmg < 0){return;}
        if (this.position.isCity()){
            dmg *= 0.9;
        }
        if (this.hp-dmg <= 0){
            this.position.removeUnit(this);
        }
        else this.hp = this.hp - dmg;
    }

    public abstract void takeAction(Tile tile);

    public boolean equals(Object obj){
        if (this.getClass() == obj.getClass()
                && this.position == ((Unit) obj).getPosition()
                && ((this.hp - ((Unit) obj).getHP()) * (this.hp - ((Unit) obj).getHP()) < 0.001)
                && this.faction.equals(((Unit) obj).getFaction())
        ){
            return true;
        }
        else return false;
    }

}
