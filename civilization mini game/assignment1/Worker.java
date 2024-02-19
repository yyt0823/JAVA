package assignment1;
public class Worker extends Unit {
    private int jobsPerformed;

    public Worker(Tile position, double hp, String faction) {
        super(position, hp, 2, faction);
        this.jobsPerformed = 0;
    }

    @Override
    public void takeAction(Tile tile) {
        if (tile.equals(this.getPosition()) && !this.getPosition().isImproved()){
            tile.buildImprovement();
            this.jobsPerformed++;
        }
        if (jobsPerformed == 10){
            tile.removeUnit(this);
        }
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
