package assignment2;

import assignment2.food.*;

import java.awt.*;

public class test {


    public static void main(String[] args) {
        Position startingPoint = new Position(3, 2);
        Position p2 = new Position(1, 3);
        Position p3 = new Position(5, 3);
        Position p4 = new Position(8, 3);
        Position p5 = new Position(1, 3);
        Position p6 = new Position(3, 3);

        Caterpillar gus = new Caterpillar(startingPoint, GameColors.GREEN, 10);
        System.out.println("1) Gus: " + gus);
        System.out.println("Stack of previously occupied positions: " + gus.positionsPreviouslyOccupied);
        System.out.println(gus.getSegmentColor(startingPoint));


        gus.positionsPreviouslyOccupied.push(startingPoint);
        gus.positionsPreviouslyOccupied.push(p2);
        System.out.println("Stack of previously occupied positions: " + gus.positionsPreviouslyOccupied);


        Fruit f1 = new Fruit(GameColors.RED);
        Fruit f2 = new Fruit(GameColors.BLUE);
        Fruit f3 = new Fruit(GameColors.YELLOW);
        Fruit f4 = new Fruit(GameColors.ORANGE);

        gus.eat(f1);
        System.out.println("Stack of previously occupied positions: " + gus.positionsPreviouslyOccupied);
        System.out.println("2) Gus: " + gus);


        gus.eat(f2);

        System.out.println("3) Gus: " + gus);
        System.out.println("Stack of previously occupied positions: " + gus.positionsPreviouslyOccupied);



        gus.positionsPreviouslyOccupied.push(p3);
        gus.eat(f3);
        System.out.println("4) Gus: " + gus);
        gus.positionsPreviouslyOccupied.push(p4);
        System.out.println("Stack of previously occupied positions: " + gus.positionsPreviouslyOccupied);
        Pickle p = new Pickle();
        gus.eat(p);
        System.out.println("5) Gus: " + gus);
        System.out.println("Stack of previously occupied positions: " + gus.positionsPreviouslyOccupied);


        Lollipop l = new Lollipop();
        gus.eat(l);
        System.out.println("6) Gus: " + gus);
        System.out.println(gus.getLength());


        //####################################//
        gus.positionsPreviouslyOccupied.push(p5);

        IceCream i = new IceCream();
        gus.eat(i);
        System.out.println("7) Gus: " + gus);
        System.out.println("Stack of previously occupied positions: " + gus.positionsPreviouslyOccupied);
        System.out.println("gus has length of :"+ gus.getLength());


        SwissCheese s = new SwissCheese();
        gus.eat(s);
        System.out.println("8) Gus: " + gus);
        System.out.println("Stack of previously occupied positions: " + gus.positionsPreviouslyOccupied);
        System.out.println();
        System.out.println("gus has length of :"+ gus.getLength());

        //#####################################################//

        Cake c = new Cake(2);
        gus.eat(c);
        gus.eat(i);
        System.out.println("9) Gus: " + gus);
        System.out.println("Stack of previously occupied positions: " + gus.positionsPreviouslyOccupied);
        System.out.println(gus.turnsNeededToDigest);
        System.out.println("gus has length of :"+ gus.getLength());

        gus.eat(i);
        System.out.println("10) Gus: " + gus);
        System.out.println(gus.getHeadPosition());
        System.out.println(gus.length);



    }




}
