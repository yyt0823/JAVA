import assignment1.*;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class JerrysTestingKit {
    public static void main(String[] args) {
        // Your code should pass the implementation test for all other tests to be functional.
        implementation.run();

        // Comment out tests you don't want to run. The implementation test is needed for all other tests,
        // please do not comment it out.
        equalities.run();
        listOfUnits.run();
        tile.run();
        unit.run();
        militaryUnit.run();
        // There's no test for warrior because it's just a military unit with specific stats.
        settler.run();
        worker.run();
        archer.run();
    }

    public static int spamAddTestSize = 1024;
    public static int manipulateTestSize = 512;

    // Sets up all the reflection fields.
    public static Field archerArrowsField;
    public static Field workerJobsField;
    public static Field tileUnitsField;

    public static TestGroup implementation = new TestGroup("Setup",
            "Extracts various methods and fields for further testing. This is a prerequisite for most other tests.",
            new Test[] {
                    // Extract worker jobs.
                    new Test("Archer Arrows Field", "Automatically fetches the arrows field of the archer class.", () -> {
                        // The archer should only declare one field, so this should be perfect.
                        // If this test fails, replace this code with
                        // archerArrowsField = Archer.class.getDeclaredField("FIELD NAME");
                        // Replace the "FIELD NAME" with your own field name.
                        archerArrowsField = Archer.class.getDeclaredFields()[0];
                        archerArrowsField.setAccessible(true);
                    }),
                    new Test("Worker Jobs Field", "Automatically fetches the jobs completed field of the archer class.", () -> {
                        // If this test fails, replace this code with
                        // workerJobsField = Worker.class.getDeclaredField("FIELD NAME");
                        // Replace the "FIELD NAME" with your own field name.
                        workerJobsField = Worker.class.getDeclaredFields()[0];
                        workerJobsField.setAccessible(true);
                    }),
                    new Test("Tile Units Field", "Automatically fetches the units field for a tile. If this crashes, you do not have a field for ListOfUnits.", () -> {
                        tileUnitsField = Arrays.stream(Tile.class.getDeclaredFields())
                                .filter((field) -> field.getType().equals(ListOfUnits.class))
                                .findFirst().orElseThrow();
                        tileUnitsField.setAccessible(true);
                    })
            }
    );

    public static TestGroup equalities = new TestGroup("Equalities",
            "Various implementation of `equals` made in this project. Passing the implementation tests is a prerequisite for this group.",
            new Test[]{
                    new Test("Base Unit.equals()", "Units should compare their stats properly.", () -> {
                        Tile dummyTile = new Tile(0, 0);

                        Unit unit1 = new Unit(dummyTile, 10.0, 1, "Test") {
                            @Override public void takeAction(Tile target) { }
                        };
                        Unit unit2 = new Unit(dummyTile, 10.0, 1, "Test") {
                            @Override public void takeAction(Tile target) { }
                        };

                        Test.assertEq(unit1, unit2);
                    }),
                    new Test("Unit != Non-Unit", "Equals should automatically reject something that's not a unit.", () -> {
                        Unit unit1 = new Unit(new Tile(0, 0), 10.0, 1, "Test") {
                            @Override public void takeAction(Tile target) { }
                        };

                        Test.assertNe(unit1, "Random Object");
                    }),
                    new Test("Unit.equals() spots HP difference", () -> {
                        Tile dummyTile = new Tile(0, 0);
                        Unit unit1 = new Unit(dummyTile, 20.0, 1, "Test") {
                            @Override public void takeAction(Tile target) { }
                        };
                        Unit unit2 = new Unit(dummyTile, 10.0, 1, "Test") {
                            @Override public void takeAction(Tile target) { }
                        };

                        Test.assertNe(unit1, unit2);
                    }),
                    new Test("Unit.equals() spots position difference", () -> {
                        Tile dummyTile = new Tile(0, 0);
                        Tile dummyTile2 = new Tile(0, 1);

                        Unit unit1 = new Unit(dummyTile, 10.0, 1, "Test") {
                            @Override public void takeAction(Tile target) { }
                        };
                        Unit unit2 = new Unit(dummyTile2, 10.0, 1, "Test") {
                            @Override public void takeAction(Tile target) { }
                        };

                        Test.assertNe(unit1, unit2);
                    }),
                    new Test("Unit.equals() spots faction difference", () -> {
                        Tile dummyTile = new Tile(0, 0);
                        Unit unit1 = new Unit(dummyTile, 10.0, 1, "Emus") {
                            @Override public void takeAction(Tile target) { }
                        };
                        Unit unit2 = new Unit(dummyTile, 10.0, 1, "Australia") {
                            @Override public void takeAction(Tile target) { }
                        };

                        Test.assertNe(unit1, unit2);
                    }),
                    new Test("Unit.equals() insensitive to moving range difference",
                            "The instructions did not ask about comparing moving range, likely because the moving range of a unit is determined by its type.", () -> {
                        Tile dummyTile = new Tile(0, 0);
                        Unit unit1 = new Unit(dummyTile, 10.0, 1, "Test") {
                            @Override public void takeAction(Tile target) { }
                        };
                        Unit unit2 = new Unit(dummyTile, 10.0, 2, "Test") {
                            @Override public void takeAction(Tile target) { }
                        };

                        Test.assertEq(unit1, unit2);
                    }),
                    new Test("Unit.equals() tolerates minor HP difference",
                            "The instructions required the comparison to tolerate a squared difference of up to 0.001.", () -> {
                        Tile dummyTile = new Tile(0, 0);
                        Unit unit1 = new Unit(dummyTile, 10.0, 1, "Test") {
                            @Override public void takeAction(Tile target) { }
                        };
                        Unit unit2 = new Unit(dummyTile, 10.0 + Math.sqrt(0.000999999), 2, "Test") {
                            @Override public void takeAction(Tile target) { }
                        };

                        Test.assertEq(unit1, unit2);
                    }),
                    new Test("Settler.equals()", () -> {
                        Tile dummyTile = new Tile(0, 0);
                        Settler settler1 = new Settler(dummyTile, 10.0, "Test");
                        Settler settler2 = new Settler(dummyTile, 10.0, "Test");

                        Test.assertEq(settler1, settler2);
                    }),
                    new Test("Settler != Non-Settler", () -> {
                        Tile dummyTile = new Tile(0, 0);
                        Settler settler = new Settler(dummyTile, 10.0, "Test");
                        Worker worker = new Worker(dummyTile, 10.0, "Test");

                        Test.assertNe(settler, worker);
                    }),
                    new Test("Worker.equals()", () -> {
                        Tile dummyTile = new Tile(0, 0);
                        Worker worker1 = new Worker(dummyTile, 10.0, "Test");
                        Worker worker2 = new Worker(dummyTile, 10.0, "Test");

                        Test.assertEq(worker1, worker2);
                    }),
                    new Test("Worker.equals() spots jobs performed difference.", () -> {
                        Tile dummyTile = new Tile(0, 0);
                        Worker worker1 = new Worker(dummyTile, 10.0, "Test");
                        workerJobsField.set(worker1, 2);

                        Worker worker2 = new Worker(dummyTile, 10.0, "Test");

                        Test.assertNe(worker1, worker2);
                    }),
                    new Test("Worker != Non-Worker", "This test requires Worker.takeAction() to be implemented properly.", () -> {
                        Tile dummyTile = new Tile(0, 0);
                        Settler settler = new Settler(dummyTile, 10.0, "Test");
                        Worker worker = new Worker(dummyTile, 10.0, "Test");

                        Test.assertNe(worker, settler);
                    }),
                    new Test("Warrior.equals()", () -> {
                        Tile dummyTile = new Tile(0, 0);
                        Warrior warrior1 = new Warrior(dummyTile, 10.0, "Test");
                        Warrior warrior2 = new Warrior(dummyTile, 10.0, "Test");

                        Test.assertEq(warrior1, warrior2);
                    }),
                    new Test("Warrior != Non-Warrior", () -> {
                        Tile dummyTile = new Tile(0, 0);
                        Warrior warrior = new Warrior(dummyTile, 10.0, "Test");
                        Worker worker = new Worker(dummyTile, 10.0, "Test");

                        Test.assertNe(warrior, worker);
                    }),
                    new Test("Archer.equals()", () -> {
                        Tile dummyTile = new Tile(0, 0);
                        Archer archer1 = new Archer(dummyTile, 10.0, "Test");
                        Archer archer2 = new Archer(dummyTile, 10.0, "Test");

                        Test.assertEq(archer1, archer2);
                    }),
                    new Test("Archer.equals() spots arrows difference.", () -> {
                        Tile dummyTile = new Tile(0, 0);
                        Archer archer1 = new Archer(dummyTile, 10.0, "Test");

                        // The archer should lose an arrow no matter what.
                        archerArrowsField.set(archer1, 4);

                        Archer archer2 = new Archer(dummyTile, 10.0, "Test");

                        Test.assertNe(archer1, archer2);
                    }),
                    new Test("Archer != Non-Archer", () -> {
                        Tile dummyTile = new Tile(0, 0);
                        Archer archer = new Archer(dummyTile, 10.0, "Test");
                        Worker worker = new Worker(dummyTile, 10.0, "Test");

                        Test.assertNe(archer, worker);
                    })
            }
    );

    public static TestGroup listOfUnits = new TestGroup("ListOfUnits",
            "Various limit-testing on the ListOfUnits class. " +
                    "These tests require comparing units, passing the equalities tests is a prerequisite for this group. " +
                    "A working getList() method is required for this test to work. ",
            new Test[]{
                    new Test("Constructor", "Verifies the default properties of a new ListOfUnits", () -> {
                        ListOfUnits list = new ListOfUnits();

                        // New list should be empty.
                        Test.assertEq(list.getSize(), 0);
                        Test.assertArrayEq(list.getList(), new Unit[0]);
                        Test.assertArrayEq(list.getArmy(), new Unit[0]);
                    }),
                    new Test("addUnit()", "List should handle adding a unit and update its size. A working getList() is required for this test.", () -> {
                        ListOfUnits list = new ListOfUnits();

                        Unit dummy = new Settler(new Tile(0, 0), 1.0, "Test");

                        Unit[] verifyList = new Unit[] { dummy };
                        list.addUnit(dummy);

                        Test.assertEq(list.getSize(), 1);
                        Test.assertArrayEq(list.getList(), verifyList);
                    }),
                    new Test("Spam addUnit()", "A bunch of things should be able to be added to the list.", () -> {
                        Unit[] verifyList = new Unit[spamAddTestSize];
                        Tile dummyTile = new Tile(0, 0);

                        ListOfUnits list = new ListOfUnits();

                        for (int i = 0; i < spamAddTestSize; i++) {
                            // Settler is the simplest unit.
                            Unit newUnit = new Settler(dummyTile, 1.0, "Test");

                            list.addUnit(newUnit);
                            verifyList[i] = newUnit;

                            Test.assertEq(list.getSize(), i + 1);
                        }

                        Test.assertEq(list.getSize(), spamAddTestSize);
                        Test.assertArrayEq(list.getList(), verifyList);
                    }),
                    new Test("getList() no nulls", () -> {
                        Tile dummyTile = new Tile(0, 0);

                        ListOfUnits list = new ListOfUnits();

                        for (int i = 0; i < spamAddTestSize; i++) {
                            // Settler is the simplest unit.
                            Unit newUnit = new Settler(dummyTile, 1.0, "Test");

                            list.addUnit(newUnit);
                        }

                        Unit[] array = list.getList();
                        for (int i = 0; i < array.length; i++) {
                            if (array[i] == null) {
                                throw new Exception(String.format("list[%d] is null.", i));
                            }
                        }
                    }),
                    new Test("getUnit()", "Indices should be in order of adding when there's no removals.", () -> {
                        Tile dummyTile = new Tile(0, 0);

                        ListOfUnits list = new ListOfUnits();

                        for (int i = 0; i < manipulateTestSize; i++) {
                            // Settler is the simplest unit. Give it an index as the faction.
                            Unit newUnit = new Settler(dummyTile, 1.0, Integer.toString(i));

                            list.addUnit(newUnit);
                        }

                        for (int i = 0; i < manipulateTestSize; i++) {
                            // Settler is the simplest unit. Give it an index as the faction.
                            Unit unit = new Settler(dummyTile, 1.0, Integer.toString(i));

                            Test.assertEq(list.getUnit(i), unit);
                        }
                    }),
                    new Test("getUnit() throws exception", "When the index is larger than the number of elements, the list should throw an exception.", () -> {
                        Tile dummyTile = new Tile(0, 0);

                        ListOfUnits list = new ListOfUnits();

                        Test.expectException(() -> {
                            list.getUnit(1);
                        }, IndexOutOfBoundsException.class);
                    }),
                    new Test("indexOf()", "The list should find a unit when nothing has been removed yet.", () -> {
                        Tile dummyTile = new Tile(0, 0);

                        ListOfUnits list = new ListOfUnits();

                        for (int i = 0; i < manipulateTestSize; i++) {
                            // Settler is the simplest unit. Give it an index as the faction.
                            Unit newUnit = new Settler(dummyTile, 1.0, Integer.toString(i));

                            list.addUnit(newUnit);
                        }

                        for (int i = 0; i < manipulateTestSize; i++) {
                            Unit unit = new Settler(dummyTile, 1.0, Integer.toString(i));
                            Test.assertEq(list.indexOf(unit), i);
                        }
                    }),
                    new Test("removeUnit()", "The list should remove a unit properly and change its size. The remove method should return `true` when the unit has been successfully removed.", () -> {
                        ListOfUnits list = new ListOfUnits();

                        Unit dummy1 = new Settler(new Tile(0, 0), 1.0, "Test1");
                        Unit dummy2 = new Settler(new Tile(0, 0), 1.0, "Test2");

                        Unit[] verifyList = new Unit[] { dummy2 };
                        list.addUnit(dummy1);
                        list.addUnit(dummy2);

                        Test.assertBool(list.removeUnit(dummy1));

                        Test.assertEq(list.getSize(), 1);
                        Test.assertArrayEq(list.getList(), verifyList);
                    }),
                    new Test("Spam removeUnit()", "Order should be preserved when units are removed. The remove method should return `true` when a unit has been successfully removed.", () -> {
                        Tile dummyTile = new Tile(0, 0);

                        ListOfUnits list = new ListOfUnits();

                        for (int i = 0; i < manipulateTestSize; i++) {
                            // Settler is the simplest unit. Give it an index as the faction.
                            Unit newUnit = new Settler(dummyTile, 1.0, Integer.toString(i));

                            list.addUnit(newUnit);
                        }

                        Unit[] testList = new Unit[manipulateTestSize / 2];
                        for (int i = 0; i < manipulateTestSize / 2; i++) {
                            Unit unit = new Settler(dummyTile, 1.0, Integer.toString(i * 2));

                            testList[i] = new Settler(dummyTile, 1.0, Integer.toString(i * 2 + 1));
                            Test.assertBool(list.removeUnit(unit));

                            Test.assertEq(list.getSize(), manipulateTestSize - i - 1);
                        }

                        Test.assertEq(list.getSize(), manipulateTestSize / 2);
                        Test.assertArrayEq(list.getList(), testList);
                    }),
                    new Test("indexOf() a nonexistent unit", "The index of any unit not in a list should return -1.", () -> {
                        Tile dummyTile = new Tile(0, 0);

                        ListOfUnits list = new ListOfUnits();

                        for (int i = 0; i < manipulateTestSize; i++) {
                            // Settler is the simplest unit. Give it an index as the faction.
                            Unit newUnit = new Settler(dummyTile, 1.0, Integer.toString(i * 2));

                            list.addUnit(newUnit);
                        }

                        for (int i = 0; i < manipulateTestSize; i++) {
                            // Settler is the simplest unit. Give it an index as the faction.
                            Unit testUnit = new Settler(dummyTile, 1.0, Integer.toString(i * 2 + 1));

                            Test.assertEq(list.indexOf(testUnit), -1);
                        }
                    }),
                    new Test("indexOf(null)", "The index of null should always be -1.", () -> {
                        ListOfUnits list = new ListOfUnits();
                        Tile dummyTile = new Tile(0, 0);

                        for (int i = 0; i < manipulateTestSize; i++) {
                            // Settler is the simplest unit. Give it an index as the faction.
                            Unit newUnit = new Settler(dummyTile, 1.0, Integer.toString(i * 2));

                            list.addUnit(newUnit);
                        }

                        Test.assertEq(list.indexOf(null), -1);
                    }),
                    new Test("removeUnit() a nonexistent unit.", "Removing a nonexistent unit shouldn't change anything. The remove method should return false.", () -> {
                        ListOfUnits list = new ListOfUnits();

                        Unit dummy1 = new Settler(new Tile(0, 0), 1.0, "Test1");
                        Unit dummy2 = new Settler(new Tile(0, 0), 1.0, "Test2");
                        Unit dummy3 = new Settler(new Tile(0, 0), 1.0, "Test3");

                        Unit[] verifyList = new Unit[] { dummy1, dummy2 };
                        list.addUnit(dummy1);
                        list.addUnit(dummy2);

                        Test.assertBool(!list.removeUnit(dummy3));

                        Test.assertEq(list.getSize(), 2);
                        Test.assertArrayEq(list.getList(), verifyList);
                    }),
                    new Test("removeUnit(null)", "Removing null shouldn't change the list. The remove method should return false.", () -> {
                        ListOfUnits list = new ListOfUnits();

                        Unit dummy1 = new Settler(new Tile(0, 0), 1.0, "Test1");
                        Unit dummy2 = new Settler(new Tile(0, 0), 1.0, "Test2");

                        Unit[] verifyList = new Unit[] { dummy1, dummy2 };
                        list.addUnit(dummy1);
                        list.addUnit(dummy2);

                        Test.assertBool(!list.removeUnit(null));

                        Test.assertEq(list.getSize(), 2);
                        Test.assertArrayEq(list.getList(), verifyList);
                    }),
                    new Test("removeUnit() then addUnit()", "Order should be preserved when removing and then adding an element.", () -> {
                        ListOfUnits list = new ListOfUnits();

                        Unit dummy1 = new Settler(new Tile(0, 0), 1.0, "Test1");
                        Unit dummy2 = new Settler(new Tile(0, 0), 1.0, "Test2");
                        Unit dummy3 = new Settler(new Tile(0, 0), 1.0, "Test3");

                        Unit[] verifyList = new Unit[] { dummy2, dummy3 };
                        list.addUnit(dummy1);
                        list.addUnit(dummy2);

                        list.removeUnit(dummy1);
                        list.addUnit(dummy3);

                        Test.assertEq(list.getSize(), 2);
                        Test.assertArrayEq(list.getList(), verifyList);
                    }),
                    new Test("getArmy()", "Check if the list properly filters military units.", () -> {

                        ListOfUnits list = new ListOfUnits();
                        Unit[] testList = new Unit[manipulateTestSize / 2];

                        for (int i = 0; i < manipulateTestSize; i++) {
                            Tile dummyTile = new Tile(0, i);
                            // Settler is the simplest unit. Give it an index as the faction.
                            Unit newUnit;

                            if (i % 2 == 0) {
                                newUnit = new Settler(dummyTile, 1.0, Integer.toString(i));
                            } else {
                                newUnit = new Warrior(dummyTile, 1.0, Integer.toString(i));
                                testList[i / 2] = newUnit;
                            }

                            list.addUnit(newUnit);
                        }

                        Test.assertArrayEq(list.getArmy(), testList);
                    }),
                    new Test("getList() no nulls after removeUnit()", "The list should contain no nulls after removing.", () -> {
                        Tile dummyTile = new Tile(0, 0);

                        ListOfUnits list = new ListOfUnits();

                        for (int i = 0; i < spamAddTestSize; i++) {
                            // Settler is the simplest unit.
                            Unit newUnit = new Settler(dummyTile, 1.0, "Test");

                            list.addUnit(newUnit);
                        }

                        list.removeUnit(list.getUnit(3));

                        Unit[] array = list.getList();
                        for (int i = 0; i < array.length; i++) {
                            if (array[i] == null) {
                                throw new Exception(String.format("list[%d] is null.", i));
                            }
                        }
                    }),
                    new Test("getList() no nulls after removeUnit() and addUnit()", "The list should contain no nulls after removing then adding.", () -> {
                        Tile dummyTile = new Tile(0, 0);

                        ListOfUnits list = new ListOfUnits();

                        for (int i = 0; i < spamAddTestSize; i++) {
                            // Settler is the simplest unit.
                            Unit newUnit = new Settler(dummyTile, 1.0, "Test");

                            list.addUnit(newUnit);
                        }

                        list.removeUnit(list.getUnit(3));
                        list.addUnit(new Settler(dummyTile, 1.0, "Test"));

                        Unit[] array = list.getList();
                        for (int i = 0; i < array.length; i++) {
                            if (array[i] == null) {
                                throw new Exception(String.format("list[%d] is null.", i));
                            }
                        }
                    }),
                    new Test("getList() no nulls after addUnit(null)", "Adding a null to the list shouldn't actually place it in there.", () -> {
                        ListOfUnits list = new ListOfUnits();

                        list.addUnit(null);

                        Test.assertEq(list.getSize(), 0);
                        Test.assertArrayEq(list.getList(), new Unit[0]);
                    }),
            }
    );

    public static TestGroup tile = new TestGroup("Tile",
            "Basic functioning of tiles. " +
                    "Since a tile has a ListOfUnits, passing the ListOfUnits tests is a prerequisite for this group.",
            new Test[] {
                    new Test("Constructor", "Properties should be initialised according to instructions.", () -> {
                        Tile tile = new Tile(0, 0);

                        Test.assertBool(!tile.isCity());
                        Test.assertBool(!tile.isImproved());
                    }),
                    new Test("buildCity()", () -> {
                        Tile tile = new Tile(0, 0);
                        tile.buildCity();
                        Test.assertBool(tile.isCity());
                    }),
                    new Test("buildImprovement()", () -> {
                        Tile tile = new Tile(0, 0);
                        tile.buildImprovement();
                        Test.assertBool(tile.isImproved());
                    }),
                    new Test("addUnit() to empty tile", "The unit should be in the tile's unit list, and ", () -> {
                        Tile origin = new Tile(0, 0);
                        Tile target = new Tile(1, 1);

                        Unit unit = new Settler(origin, 1.0, "Test");

                        Test.assertBool(target.addUnit(unit));

                        Test.assertArrayEq(((ListOfUnits)tileUnitsField.get(target)).getList(), new Unit[] { unit });
                    }),
                    new Test("addUnit() and removeUnit()", () -> {
                        Tile origin = new Tile(0, 0);
                        Tile target = new Tile(1, 1);

                        Unit unit = new Settler(origin, 1.0, "Test");

                        Test.assertBool(target.addUnit(unit));
                        Test.assertBool(target.removeUnit(unit));

                        Test.assertArrayEq(((ListOfUnits)tileUnitsField.get(target)).getList(), new Unit[] { });
                    }),
                    new Test("removeUnit() a non-existent unit", "Removing something not on the tile should return false.", () -> {
                        Tile origin = new Tile(0, 0);
                        Tile target = new Tile(1, 1);

                        Unit unit = new Settler(origin, 1.0, "Test");

                        Test.assertBool(!target.removeUnit(unit));
                    }),
                    new Test("addUnit() with friendly civilian units", () -> {
                        Tile origin = new Tile(0, 0);
                        Tile target = new Tile(1, 1);

                        Unit settler = new Settler(origin, 1.0, "Emus");
                        Unit worker = new Worker(origin, 1.0, "Emus");
                        Unit warrior = new Warrior(origin, 1.0, "Emus");

                        target.addUnit(settler);
                        Test.assertBool(target.addUnit(worker));
                        Test.assertBool(target.addUnit(warrior));
                    }),
                    new Test("addUnit() with enemy civilian units", () -> {
                        Tile origin = new Tile(0, 0);
                        Tile target = new Tile(1, 1);

                        Unit settler = new Settler(origin, 1.0, "Emus");
                        Unit worker = new Worker(origin, 1.0, "Australia");
                        Unit warrior = new Warrior(origin, 1.0, "Emus");

                        target.addUnit(settler);
                        Test.assertBool(target.addUnit(worker));
                        Test.assertBool(target.addUnit(warrior));
                    }),
                    new Test("addUnit() with friendly military units", () -> {
                        Tile origin = new Tile(0, 0);
                        Tile target = new Tile(1, 1);

                        Unit settler = new Settler(origin, 1.0, "Emus");
                        Unit worker = new Worker(origin, 1.0, "Emus");
                        Unit warrior = new Warrior(origin, 1.0, "Emus");
                        Unit archer = new Archer(origin, 1.0, "Emus");

                        target.addUnit(settler);
                        target.addUnit(worker);
                        target.addUnit(warrior);
                        Test.assertBool(target.addUnit(archer));
                    }),
                    new Test("addUnit() with enemy military units", () -> {
                        Tile origin = new Tile(0, 0);
                        Tile target = new Tile(1, 1);

                        Unit settler = new Settler(origin, 1.0, "Emus");
                        Unit worker = new Worker(origin, 1.0, "Emus");
                        // We'll put them on separate tiles so the constructor doesn't complain.
                        Unit warrior = new Warrior(new Tile(0, 1), 1.0, "Australia");
                        Unit archer = new Archer(origin, 1.0, "Emus");

                        target.addUnit(settler);
                        target.addUnit(worker);
                        target.addUnit(warrior);
                        Test.assertBool(!target.addUnit(archer));
                    }),
                    new Test("addUnit() civilian unit with enemy military units", () -> {
                        Tile origin = new Tile(0, 0);
                        Tile target = new Tile(1, 1);

                        Unit settler = new Settler(origin, 1.0, "Emus");
                        Unit worker = new Worker(origin, 1.0, "Emus");
                        // We'll put them on separate tiles so the constructor doesn't complain.
                        Unit warrior = new Warrior(new Tile(0, 1), 1.0, "Australia");
                        Unit settler2 = new Settler(origin, 1.0, "Emus");

                        target.addUnit(settler);
                        target.addUnit(worker);
                        target.addUnit(warrior);
                        // Civilian units should always be allowed in.
                        Test.assertBool(target.addUnit(settler2));
                    }),
                    new Test("selectWeakestEnemy() from empty tile", () -> {
                        Tile tile = new Tile(0, 0);

                        Test.assertNull(tile.selectWeakEnemy("Emus"));
                    }),
                    new Test("selectWeakestEnemy() from friendly tile", () -> {
                        Tile origin = new Tile(0, 0);
                        Tile target = new Tile(1, 1);

                        Unit settler = new Settler(origin, 1.0, "Emus");
                        Unit worker = new Worker(origin, 1.0, "Emus");
                        Unit warrior = new Warrior(origin, 1.0, "Emus");
                        Unit archer = new Archer(origin, 1.0, "Emus");

                        target.addUnit(settler);
                        target.addUnit(worker);
                        target.addUnit(warrior);
                        target.addUnit(archer);

                        Test.assertNull(target.selectWeakEnemy("Emus"));
                    }),
                    new Test("selectWeakestEnemy() from enemy tile", () -> {
                        Tile origin = new Tile(0, 0);
                        Tile target = new Tile(1, 1);

                        Unit warrior = new Warrior(origin, 2.0, "Emus");
                        Unit archer = new Archer(origin, 1.0, "Emus");

                        target.addUnit(warrior);
                        target.addUnit(archer);

                        Test.assertEq(target.selectWeakEnemy("Australia"), archer);
                    }),
                    new Test("selectWeakestEnemy() selects civilian units", () -> {
                        Tile origin = new Tile(0, 0);
                        Tile target = new Tile(1, 1);

                        Unit warrior = new Warrior(origin, 2.0, "Emus");
                        Unit worker = new Worker(origin, 1.0, "Emus");

                        target.addUnit(warrior);
                        target.addUnit(worker);

                        Test.assertEq(target.selectWeakEnemy("Australia"), worker);
                    }),
                    new Test("selectWeakestEnemy() between equal-hp enemies",
                            "When two enemies of the same HP are in a tile, the first one in the list should be selected.", () -> {
                        Tile origin = new Tile(0, 0);
                        Tile target = new Tile(1, 1);

                        Unit warrior = new Warrior(origin, 2.0, "Emus");
                        Unit archer = new Archer(origin, 2.0, "Emus");

                        target.addUnit(warrior);
                        target.addUnit(archer);

                        Test.assertEq(target.selectWeakEnemy("Australia"), warrior);
                    }),
                    new Test("getDistance()", () -> {
                        Test.assertEq(
                                Tile.getDistance(new Tile(1, 1), new Tile(2, 2)),
                                Math.sqrt(2)
                        );
                        Test.assertEq(
                                Tile.getDistance(new Tile(1, 1), new Tile(1, 1)),
                                0.0
                        );
                        Test.assertEq(
                                Tile.getDistance(new Tile(2, 3), new Tile(1, 1)),
                                Math.sqrt(5)
                        );
                    }),
                    new Test("getDistance() with overflow", "An edge case where the subtraction may overflow. It shouldn't matter for this game, just here to remind you that integers have a limit.", () -> {
                        Test.assertEq(
                                Tile.getDistance(new Tile(Integer.MAX_VALUE, Integer.MAX_VALUE), new Tile(Integer.MIN_VALUE, Integer.MIN_VALUE)),
                                Math.sqrt(
                                        (((double)Integer.MAX_VALUE) - ((double)Integer.MIN_VALUE)) *
                                                (((double)Integer.MAX_VALUE) - ((double)Integer.MIN_VALUE)) * 2
                                )
                        );
                    })
            }
    );

    public static TestGroup unit = new TestGroup("Base Unit",
            "Basic functioning of units. " +
                    "The unit constructor requires adding the unit to a tile, and movement requires checking tile distances. " +
                    "Certain tests involve building city or improvements. Passing the Tile tests is a prerequisite for this group.",
            new Test[] {
                    new Test("Constructor", "The constructor should properly initialise a unit's properties.", () -> {
                        Tile tile = new Tile(0, 0);
                        Unit unit1 = new Unit(tile, 10.0, 1, "Test") {
                            @Override public void takeAction(Tile target) { }
                        };

                        Test.assertEq(unit1.getHP(), 10.0);
                        Test.assertEq(unit1.getFaction(), "Test");
                        Test.assertEq(unit1.getPosition(), tile);
                    }),
                    new Test("Constructor adds unit to tile", "The unit's position tile should contain it.", () -> {
                        Tile tile = new Tile(0, 0);
                        Unit unit1 = new Unit(tile, 10.0, 1, "Test") {
                            @Override public void takeAction(Tile target) { }
                        };

                        // Removing should return true when the unit comes from there.
                        Test.assertBool(tile.removeUnit(unit1));
                    }),
                    new Test("Constructor throws exception", "The constructor should throw an IllegalArgumentException when the unit cannot be added to a tile.", () -> {
                        Tile tile = new Tile(0, 0);
                        Unit unit1 = new Warrior(tile, 1.0, "Emu");

                        // Since this tile is already occupied by the Emu warrior, the Australian unit should cause an exception.
                        Test.expectException(() -> {
                            new Warrior(tile, 10.0, "Australia");
                        }, IllegalArgumentException.class);
                    }),
                    new Test("Constructor allows civilian units to enter occupied tiles", "Civilian units can always be added to a tile.", () -> {
                        Tile tile = new Tile(0, 0);
                        Unit unit1 = new Warrior(tile, 1.0, "Emu");

                        new Settler(tile, 10.0, "Australia");
                    }),
                    new Test("moveTo() accepts movement in range", () -> {
                        Tile origin = new Tile(0, 0);
                        Tile target1 = new Tile(0, 1);

                        Unit unit1 = new Unit(origin, 10.0, 1, "Emus") {
                            @Override public void takeAction(Tile target) { }
                        };

                        Test.assertBool(unit1.moveTo(target1));
                        Test.assertBool(unit1.moveTo(origin));
                    }),
                    new Test("moveTo() updates unit position", () -> {
                        Tile origin = new Tile(0, 0);
                        Tile target1 = new Tile(0, 1);

                        Unit unit1 = new Unit(origin, 10.0, 1, "Emus") {
                            @Override public void takeAction(Tile target) { }
                        };

                        unit1.moveTo(target1);

                        Test.assertEq(unit1.getPosition(), target1);
                    }),
                    new Test("moveTo() rejects movement beyond range", () -> {
                        Tile origin = new Tile(0, 0);
                        Tile target2 = new Tile(0, 2);
                        Tile target3 = new Tile(1, 1);

                        Unit unit1 = new Unit(origin, 10.0, 1, "Emus") {
                            @Override public void takeAction(Tile target) { }
                        };

                        Test.assertBool(!unit1.moveTo(target2));
                        Test.assertBool(!unit1.moveTo(target3));

                        // The units should remain in their original tiles.
                        Test.assertBool(origin.removeUnit(unit1));
                    }),
                    new Test("moveTo() removes unit in original tile", "The unit should no longer be in the original tile after it's been to another tile.", () -> {
                        Tile origin = new Tile(0, 0);
                        Tile target = new Tile(0, 1);

                        Unit unit1 = new Unit(origin, 10.0, 1, "Emus") {
                            @Override public void takeAction(Tile target) { }
                        };

                        unit1.moveTo(target);

                        Test.assertBool(!origin.removeUnit(unit1));
                    }),
                    new Test("moveTo() adds unit to target tile", () -> {
                        Tile origin = new Tile(0, 0);
                        Tile target = new Tile(0, 1);

                        Unit unit1 = new Unit(origin, 10.0, 1, "Emus") {
                            @Override public void takeAction(Tile target) { }
                        };

                        unit1.moveTo(target);

                        Test.assertBool(target.removeUnit(unit1));
                    }),
                    new Test("moveTo() original tile", "Moving a unit to its current tile should keep it in the current tile.", () -> {
                        Tile origin = new Tile(0, 0);

                        Unit unit1 = new Unit(origin, 10.0, 1, "Emus") {
                            @Override public void takeAction(Tile target) { }
                        };

                        Test.assertBool(unit1.moveTo(origin));
                        Test.assertEq(((ListOfUnits)(tileUnitsField.get(origin))).getSize(), 1);
                        Test.assertBool(origin.removeUnit(unit1));
                    }),
                    new Test("moveTo(null)", "Moving a unit to null should be rejected and keep it in the original tile.", () -> {
                        Tile origin = new Tile(0, 0);

                        Unit unit1 = new Unit(origin, 10.0, 1, "Emus") {
                            @Override public void takeAction(Tile target) { }
                        };

                        Test.assertBool(!unit1.moveTo(null));
                        Test.assertBool(origin.removeUnit(unit1));
                    }),
                    new Test("receiveDamage()", () -> {
                        Unit unit1 = new Unit(new Tile(0, 0), 10.0, 1, "Emus") {
                            @Override public void takeAction(Tile target) { }
                        };

                        unit1.receiveDamage(5.0);

                        Test.assertEq(unit1.getHP(), 5.0);
                    }),
                    new Test("receiveDamage() in city", "A city should reduce the damage taken by 10%.", () -> {
                        Tile tile = new Tile(0, 0);
                        tile.buildCity();
                        Unit unit1 = new Unit(tile, 10.0, 1, "Emus") {
                            @Override public void takeAction(Tile target) { }
                        };

                        unit1.receiveDamage(5.0);

                        Test.assertEq(unit1.getHP(), 5.5);
                    }),
                    new Test("receiveDamage() removes dead unit", "A dead unit should be removed from its tile.", () -> {
                        Tile tile = new Tile(0, 0);
                        tile.buildCity();
                        Unit unit1 = new Unit(tile, 10.0, 1, "Emus") {
                            @Override public void takeAction(Tile target) { }
                        };

                        unit1.receiveDamage(20.0);

                        // The unit should be gone from the tile.
                        Test.assertBool(!tile.removeUnit(unit1));
                    }),
                    new Test("receiveDamage() with negative damage", "Receiving negative damage shouldn't change the unit's HP or remove it from its position.", () -> {
                        Tile tile = new Tile(0, 0);
                        Unit unit1 = new Unit(tile, 10.0, 1, "Emus") {
                            @Override public void takeAction(Tile target) { }
                        };

                        unit1.receiveDamage(-20.0);

                        Test.assertEq(unit1.getHP(), 10.0);
                        // The unit should remain in the tile.
                        Test.assertBool(tile.removeUnit(unit1));
                    }),
            }
    );

    public static TestGroup militaryUnit = new TestGroup("Base MilitaryUnit",
            "Basic functioning of military units. " +
                    "Since MilitaryUnit is a subclass of Unit, passing the Base Unit tests is a prerequisite for this group.",
            new Test[]{
                    new Test("takeAction() rejects attacks out of range", () -> {
                        Tile tile = new Tile(0, 0);
                        Tile target1 = new Tile(0, 2);
                        Unit enemy1 = new Settler(target1, 20.0, "Emus");
                        Tile target2 = new Tile(1, 1);
                        Unit enemy2 = new Settler(target2, 20.0, "Emus");
                        Unit unit1 = new MilitaryUnit(tile, 10.0, 1, "Australia", 10.0, 1, 0) {};

                        unit1.takeAction(target1);
                        unit1.takeAction(target2);

                        // The enemies should be unharmed.
                        Test.assertEq(enemy1.getHP(), 20.0);
                        Test.assertEq(enemy2.getHP(), 20.0);
                    }),
                    new Test("takeAction() rejects attacking friendly units", () -> {
                        Tile tile = new Tile(0, 0);
                        Tile target1 = new Tile(0, 1);
                        Unit enemy1 = new Settler(target1, 20.0, "Australia");
                        Unit unit1 = new MilitaryUnit(tile, 10.0, 1, "Australia", 10.0, 1, 0) {};

                        unit1.takeAction(target1);

                        // Friendly units should be unharmed.
                        Test.assertEq(enemy1.getHP(), 20.0);
                    }),
                    new Test("takeAction() attacks enemy units", () -> {
                        Tile tile = new Tile(0, 0);
                        Tile target1 = new Tile(0, 1);
                        Unit enemy1 = new Settler(target1, 20.0, "Emu");
                        Unit unit1 = new MilitaryUnit(tile, 10.0, 1, "Australia", 10.0, 1, 0) {};

                        unit1.takeAction(target1);

                        // Enemy should be harmed.
                        Test.assertNe(enemy1.getHP(), 20.0);
                    }),
                    new Test("takeAction() attacks weakest enemy unit", () -> {
                        Tile tile = new Tile(0, 0);
                        Tile target1 = new Tile(0, 1);
                        Unit enemy1 = new Settler(target1, 20.0, "Emu");
                        Unit enemy2 = new Settler(target1, 10.0, "Emu");
                        Unit unit1 = new MilitaryUnit(tile, 10.0, 1, "Australia", 10.0, 1, 0) {};

                        unit1.takeAction(target1);

                        // Only the weakest should be attacked.
                        Test.assertEq(enemy1.getHP(), 20.0);
                        Test.assertNe(enemy2.getHP(), 10.0);
                    }),
                    new Test("takeAction() deduces hp", () -> {
                        Tile tile = new Tile(0, 0);
                        Tile target = new Tile(0, 1);
                        Unit enemy = new Settler(target, 20.0, "Emu");
                        Unit unit1 = new MilitaryUnit(tile, 10.0, 1, "Australia", 10.0, 1, 0) {};

                        unit1.takeAction(target);

                        // HP should be deduced.
                        Test.assertEq(enemy.getHP(), 10.0);
                    }),
                    new Test("takeAction() removes killed unit", () -> {
                        Tile tile = new Tile(0, 0);
                        Tile target = new Tile(0, 1);
                        Unit enemy = new Settler(target, 20.0, "Emu");
                        Unit unit1 = new MilitaryUnit(tile, 10.0, 1, "Australia", 30.0, 1, 0) {};

                        unit1.takeAction(target);

                        // HP should be deduced.
                        Test.assertBool(!target.removeUnit(enemy));
                    }),
                    new Test("takeAction() benefits from improvement", "An improved tile provides a 5% attack bonus.", () -> {
                        Tile tile = new Tile(0, 0);
                        tile.buildImprovement();

                        Tile target = new Tile(0, 1);
                        Unit enemy = new Settler(target, 20.0, "Emu");
                        Unit unit1 = new MilitaryUnit(tile, 10.0, 1, "Australia", 10.0, 1, 0) {};

                        unit1.takeAction(target);

                        // HP should be deduced.
                        Test.assertEq(enemy.getHP(), 9.5);
                    }),
                    new Test("receiveDamage() works with armour", "Armour reduces damage using a special formula.", () -> {
                        Unit unit1 = new MilitaryUnit(new Tile(0, 0), 10.0, 1, "Australia", 10.0, 1, 100) {};
                        Unit unit2 = new MilitaryUnit(new Tile(0, 0), 10.0, 1, "Australia", 10.0, 1, 300) {};

                        unit1.receiveDamage(10.0);
                        unit2.receiveDamage(10.0);

                        // 100 armour should equate to 50% damage reduction.
                        Test.assertEq(unit1.getHP(), 5.0);
                        // 300 armour should equate to 75% damage reduction.
                        Test.assertEq(unit2.getHP(), 7.5);
                    }),
                    new Test("receiveDamage() works with armour and city", "", () -> {
                        Tile tile = new Tile(0, 0);
                        tile.buildCity();
                        Unit unit1 = new MilitaryUnit(tile, 10.0, 1, "Australia", 10.0, 1, 100) {};

                        unit1.receiveDamage(10.0);

                        // 100 armour should equate to 50% damage reduction, multiplied by a 10% damage reduction.
                        Test.assertEq(unit1.getHP(), 5.5);
                    }),
                    new Test("receiveDamage() removes dead unit", () -> {
                        Tile tile = new Tile(0, 0);
                        tile.buildCity();
                        Unit unit1 = new MilitaryUnit(tile, 10.0, 1, "Australia", 10.0, 1, 100) {};

                        unit1.receiveDamage(1000.0);

                        Test.assertBool(!tile.removeUnit(unit1));
                    }),
            }
    );

    public static TestGroup settler = new TestGroup("Settler",
            "Various functions of the settler. Since Settler is a subclass of Unit, passing the Base Unit tests is a prerequisites for this group.",
            new Test[]{
                    new Test("takeAction() rejects tiles out of range", "The settler can only settle on its own tile.", () -> {
                        Settler settler = new Settler(new Tile(0, 0), 10.0, "Emus");

                        Tile tile = new Tile(0, 1);
                        settler.takeAction(tile);

                        // The settler shouldn't be removed nor build a city.
                        Test.assertBool(!tile.isCity());
                        Test.assertBool(settler.getPosition().removeUnit(settler));
                    }),
                    new Test("takeAction() rejects if a city is already there", () -> {
                        Settler settler = new Settler(new Tile(0, 0), 10.0, "Emus");

                        settler.getPosition().buildCity();
                        settler.takeAction(settler.getPosition());

                        // The settler shouldn't be removed nor build a city.
                        Test.assertBool(settler.getPosition().removeUnit(settler));
                    }),
                    new Test("takeAction() builds city", () -> {
                        Settler settler = new Settler(new Tile(0, 0), 10.0, "Emus");

                        settler.takeAction(settler.getPosition());

                        Test.assertBool(settler.getPosition().isCity());
                    }),
                    new Test("takeAction() consumes settler", () -> {
                        Settler settler = new Settler(new Tile(0, 0), 10.0, "Emus");

                        settler.takeAction(settler.getPosition());

                        Test.assertBool(!settler.getPosition().removeUnit(settler));
                    })
            }
    );

    public static TestGroup worker = new TestGroup("Worker",
            "Various functions of the worker. Since Worker is a subclass of Unit, passing the Base Unit tests is a prerequisites for this group.",
            new Test[]{
                    new Test("takeAction() rejects tiles out of range", "The worker can only settle on its own tile.", () -> {
                        Worker worker = new Worker(new Tile(0, 0), 10.0, "Emus");

                        Tile tile = new Tile(0, 1);
                        worker.takeAction(tile);

                        // The settler shouldn't be removed.
                        Test.assertBool(!tile.isImproved());
                        // The work counter should remain the same.
                        Test.assertEq(workerJobsField.get(worker), 0);
                    }),
                    new Test("takeAction() rejects an already improved tile", () -> {
                        Worker worker = new Worker(new Tile(0, 0), 10.0, "Emus");

                        worker.getPosition().buildImprovement();
                        worker.takeAction(worker.getPosition());

                        // The work counter should remain the same.
                        Test.assertEq(workerJobsField.get(worker), 0);
                    }),
                    new Test("takeAction() improves tile", () -> {
                        Worker worker = new Worker(new Tile(0, 0), 10.0, "Emus");

                        worker.takeAction(worker.getPosition());

                        // The work counter should remain the same.
                        Test.assertBool(worker.getPosition().isImproved());
                    }),
                    new Test("takeAction() increments job counter", () -> {
                        Worker worker = new Worker(new Tile(0, 0), 10.0, "Emus");

                        worker.takeAction(worker.getPosition());

                        // The work counter should increment.
                        Test.assertEq(workerJobsField.get(worker), 1);
                    }),
                    new Test("takeAction() expends worker at 10 jobs", () -> {
                        Worker worker = new Worker(new Tile(0, 0), 10.0, "Emus");

                        workerJobsField.set(worker, 9);
                        worker.takeAction(worker.getPosition());

                        // The worker should be removed.
                        Test.assertBool(!worker.getPosition().removeUnit(worker));
                    }),
            }
    );

    public static TestGroup archer = new TestGroup("Archer",
            "Various functions of the archer. Since Archer is a subclass of MilitaryUnit, passing the MilitaryUnit test is a prerequisite for this group.",
            new Test[]{
                    new Test("takeAction() expends arrow on attack", () -> {
                        Tile tile = new Tile(0, 0);
                        Tile target1 = new Tile(0, 1);
                        Unit enemy1 = new Settler(target1, 20.0, "Emu");
                        Unit archer = new Archer(tile, 10.0, "Australia");

                        archer.takeAction(target1);

                        // Enemy should be harmed.
                        Test.assertNe(enemy1.getHP(), 20.0);
                        // Enemy should be harmed.
                        Test.assertEq(archerArrowsField.get(archer), 4);
                    }),
                    new Test("takeAction() expends arrow on failed attack", () -> {
                        Tile tile = new Tile(0, 0);
                        Tile target1 = new Tile(0, 5);
                        Unit enemy1 = new Settler(target1, 20.0, "Emu");
                        Unit archer = new Archer(tile, 10.0, "Australia");

                        archer.takeAction(target1);

                        // Enemy shouldn't be harmed.
                        Test.assertEq(enemy1.getHP(), 20.0);
                        // Arrow should be expended.
                        Test.assertEq(archerArrowsField.get(archer), 4);
                    }),
                    new Test("takeAction() reloads", "The archer should reload to 5 arrows when there are no more arrows.", () -> {
                        Tile tile = new Tile(0, 0);
                        Tile target1 = new Tile(0, 5);
                        Unit enemy1 = new Settler(target1, 20.0, "Emu");
                        Unit archer = new Archer(tile, 10.0, "Australia");

                        archerArrowsField.set(archer, 0);
                        archer.takeAction(target1);

                        // Arrows should be reloaded.
                        Test.assertEq(archerArrowsField.get(archer), 5);
                    }),
                    new Test("takeAction() reloads without harming enemy", "The archer shouldn't shoot an arrow while reloading.", () -> {
                        Tile tile = new Tile(0, 0);
                        Tile target1 = new Tile(0, 5);
                        Unit enemy1 = new Settler(target1, 20.0, "Emu");
                        Unit archer = new Archer(tile, 10.0, "Australia");

                        archerArrowsField.set(archer, 0);
                        archer.takeAction(target1);

                        // Enemy shouldn't be harmed.
                        Test.assertEq(enemy1.getHP(), 20.0);
                        // Arrows should be reloaded.
                        Test.assertEq(archerArrowsField.get(archer), 5);
                    }),
            }
    );
}

// https://stackoverflow.com/questions/5303539/didnt-java-once-have-a-pair-class
class Pair<K, V> {
    private final K left;
    private final V right;

    public Pair(K left, V right) {
        this.left = left;
        this.right = right;
    }

    public K getLeft() {
        return left;
    }

    public V getRight() {
        return right;
    }
}

class Test {
    public String name;
    public String description;
    public TestFunction testFunction;

    public Test(String name, String description, TestFunction testFunction) {
        this.name = name;
        this.description = description;
        this.testFunction = testFunction;
    }

    public Test(String name, TestFunction testFunction) {
        this(name, "", testFunction);
    }

    public void run() throws Exception {
        testFunction.apply();
    }

    public static void assertBool(boolean bool) throws Exception {
        if (!bool) {
            throw new Exception("Expected true, received false.");
        }
    }

    public static void assertEq(Object left, Object right) throws Exception {
        if (!left.equals(right)) {
            throw new Exception(String.format("Assertion failed (expected left == right): left=%s, right=%s.",
                    debugToString(left), debugToString(right)));
        }
    }
    public static void assertArrayEq(Object[] left, Object[] right) throws Exception {
        if (!Arrays.equals(left, right)) {
            throw new Exception(String.format("Assertion failed (expected left == right): left=%s, right=%s.",
                    debugToString(left), debugToString(right)));
        }
    }
    public static void assertNe(Object left, Object right) throws Exception {
        if (left.equals(right)) {
            throw new Exception(String.format("Assertion failed (expected left != right): left=%s, right=%s.",
                    debugToString(left), debugToString(right)));
        }
    }
    public static void expectException(TestFunction test, Class<?> expectedExceptionType) throws Exception {
        boolean exceptionThrown = true;
        try {
            test.apply();
            exceptionThrown = false;
        } catch (Exception e) {
            if (e.getClass() != expectedExceptionType) {
                throw new Exception(String.format("Expected %s, but %s was thrown instead.",
                        expectedExceptionType.getName(), e.getClass().getName()));
            }
        }
        if (!exceptionThrown) {
            // If nothing was caught.
            throw new Exception(String.format("Expected %s, none was thrown.", expectedExceptionType.getName()));
        }
    }
    public static void assertNull(Object o) throws Exception {
        if (o != null) {
            throw new Exception(String.format("Expected null, but received %s", o));
        }
    }

    private static Class<?> getToStringImplementor(Class<?> clazz) {
        try {
            // Tries to fetch the toString method.
            clazz.getDeclaredMethod("toString");

            // If this passes, then we know that this class is the implementor.
            return clazz;
        } catch (NoSuchMethodException e) {
            return getToStringImplementor(clazz.getSuperclass());
        }
    }

    private static Field[] getAllFields(Class<?> clazz) {
        // We don't look at anything in object.
        if (clazz == Object.class) { return new Field[]{}; }

        Field[] declaredFields = clazz.getDeclaredFields();
        Field[] inheritedFields = getAllFields(clazz.getSuperclass());

        ArrayList<Field> allFields = new ArrayList<>();

        allFields.addAll(Arrays.asList(declaredFields));
        allFields.addAll(Arrays.asList(inheritedFields));

        return allFields.toArray(new Field[0]);
    }

    public static String debugToString(Object o) {
        if (o == null) { return "null"; }
        // If there is already a toString implementation, then just use that.
        if (Object.class != getToStringImplementor(o.getClass())) { return o.toString(); }
        // If it's an array, do it piecewise.
        if (o.getClass().isArray()) {
            StringBuilder builder = new StringBuilder("[ ");
            for (int i = 0; i < Array.getLength(o); i++) {
                builder.append(debugToString(Array.get(o, i)));
                if (i < Array.getLength(o) - 1) {
                    builder.append(", ");
                }
            }
            builder.append(" ]");

            return builder.toString();
        }
        StringBuilder builder = new StringBuilder("{ ");

        for (Field field: getAllFields(o.getClass())) {
            field.setAccessible(true);
            try {
                builder.append(String.format("%s: %s, ", field.getName(), field.get(o)));
            } catch (IllegalAccessException e) {
                System.out.println("Hmm Java's security doesn't let me access your fields.");
            }
        }

        builder.delete(builder.length() - 2, builder.length());
        builder.append(" }");

        return builder.toString();
    }
}

interface TestFunction {
    void apply() throws Exception;
}

class TestGroup {
    String name;
    String description;
    ArrayList<Test> tests;

    public TestGroup(String name, String description, ArrayList<Test> tests) {
        this.name = name;
        this.description = description;
        this.tests = tests;
    }
    public TestGroup(String name, String description, Test[] tests) { this(name, description, new ArrayList<>(Arrays.asList(tests))); }
    public TestGroup(String name, Test[] tests) { this(name, "", tests); }
    public TestGroup(String name, ArrayList<Test> tests) { this(name, "", tests); }
    public TestGroup(String name) { this(name, new ArrayList<>()); }

    public void run() {
        // Immediately reject the test if there's nothing to test. Like, seriously?
        if (tests.size() == 0) { return; }

        // Set up the test
        System.out.printf("Running %d tests in %s:%n", tests.size(), name);
        // Print out the description if possible.
        if (!description.isEmpty()) System.out.printf("%s%n", description);

        int maxTestNameLength = tests.stream().max(Comparator.comparingInt(a -> a.name.length())).get().name.length();
        int lineLength = maxTestNameLength + 4;

        ArrayList<Pair<Test, Exception>> failedTests = new ArrayList<>();
        for (Test test : tests) {
            // Indent here.
            System.out.printf("    Running test - %s", test.name);
            System.out.print(" ".repeat(lineLength - test.name.length()));
            try {
                test.run();
                // If ran without exception, then it's a success.
                System.out.print("Passed");
            } catch (Exception e) {
                System.out.print("Failed");
                failedTests.add(new Pair<>(test, e));
            }
            System.out.println();
        }

        System.out.printf("Passed %d tests out of %d in %s.%n", tests.size() - failedTests.size(), tests.size(), name);

        System.out.println();

        // Finish the thing if nothing failed.
        if (failedTests.size() == 0) { return; }

        System.out.println();
        System.out.println("Failed tests:");

        for (Pair<Test, Exception> pair : failedTests) {
            System.out.printf("Failed test %s:%n", pair.getLeft().name);
            if (!pair.getLeft().description.isEmpty()) {
                System.out.println(pair.getLeft().description);
            }
            pair.getRight().printStackTrace(System.out);
            System.out.println();
        }
    }
}