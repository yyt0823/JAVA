package assignment1;
public class ListOfUnits {
    private Unit[] unitList;
    private int size;

    public ListOfUnits() {
        this.unitList = new Unit[0];
        this.size = 0;
    }

    public int getSize() {
        return size;
    }

    public Unit[] getList() {
        return unitList;
    }


    public Unit getUnit(int n) {
        if (n >= 0 && n < size) {
            return unitList[n];
        } else throw new IndexOutOfBoundsException();
    }

    public void addUnit(Unit unit) {
        if (unit == null){
            return;
        }
        Unit[] newList = new Unit[size + 1];
        for (int i = 0; i < size; i++) {
            newList[i] = unitList[i];
        }
        newList[size] = unit;
        unitList = newList;
        size++;
    }


    public int indexOf(Unit unit) {
        if (unit == null){
            return -1;
        }
        int n = -1;
        for (int i = 0; i < size; i++) {
            if (unitList[i].equals(unit)) {
                n = i;
                break;
            }
        }
        return n;
    }

    public boolean removeUnit(Unit unit) {
        if (indexOf(unit) != -1) {
            int counter = 0;
            Unit[] newList = new Unit[size - 1];
            for (int i = 0; i < size; i++) {
                if (i != indexOf(unit)) {
                    newList[counter] = unitList[i];
                    counter++;
                }
            }
            unitList = newList;
            size--;
            return true;
        } else {
            return false;
        }
    }

    public MilitaryUnit[] getArmy() {
        int counter = 0;
        for (int i = 0; i < size; i++) {
            if (unitList[i] instanceof MilitaryUnit) {
                counter++;
            }
        }
        MilitaryUnit[] armyList = new MilitaryUnit[counter];
        counter = 0;
        for (int i = 0; i < size; i++) {
            if (unitList[i] instanceof MilitaryUnit) {
                armyList[counter] = (MilitaryUnit) unitList[i];
                counter++;
            }
        }
        return armyList;
    }
}
