package assignment1;
public abstract class MilitaryUnit extends Unit {
    private double attackDamage;
    private int attackRange;

    private int armor;

    public MilitaryUnit(Tile position, double hp, int movingRange, String faction, double attackDamage, int attackRange, int armor) {
        super(position, hp, movingRange, faction);
        this.armor = armor;
        this.attackDamage = attackDamage;
        this.attackRange = attackRange;
    }

    @Override
    public void takeAction(Tile tile) {
        if (!(Tile.getDistance(tile, this.getPosition()) > this.attackRange)){
            Unit weakestEnemy = tile.selectWeakEnemy(this.getFaction());
            if (weakestEnemy != null){
                double dmg;
                if (this.getPosition().isImproved()){
                     dmg = this.attackDamage * 1.05;
                }
                else {
                     dmg = this.attackDamage;
                }
                weakestEnemy.receiveDamage(dmg);
            }
        }
    }

    @Override
    public void receiveDamage(double dmg) {
        double multiplier = ((double) 100 /(100+this.armor));
        super.receiveDamage(dmg * multiplier);
    }
}
