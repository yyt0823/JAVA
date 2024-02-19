package assignment2;

import java.awt.Color;
import java.sql.PseudoColumnUsage;
import java.util.Random;
import java.util.Stack;

import assignment2.food.*;

import javax.swing.text.Segment;


public class Caterpillar {
    // All the fields have been declared public for testing purposes
    public Segment head;
    public Segment tail;
    public int length;
    public EvolutionStage stage;

    public Stack<Position> positionsPreviouslyOccupied;
    public int goal;
    public int turnsNeededToDigest;


    public static Random randNumGenerator = new Random(1);


    // Creates a Caterpillar with one Segment. It is up to students to decide how to implement this.
    public Caterpillar(Position p, Color c, int goal) {
        /*
         * TODO: ADD YOUR CODE HERE
         */
        this.head = new Segment(p, c);
        this.tail = this.head;
        this.length = 1;
        this.stage = EvolutionStage.FEEDING_STAGE;
        this.positionsPreviouslyOccupied = new Stack<>();
        this.goal = goal;
        this.turnsNeededToDigest = 0;
    }


    public EvolutionStage getEvolutionStage() {
        return this.stage;
    }

    public Position getHeadPosition() {
        return this.head.position;
    }

    public int getLength() {
        return this.length;
    }


    // returns the color of the segment in position p. Returns null if such segment does not exist
    public Color getSegmentColor(Position p) {
        /*
         * TODO: ADD YOUR CODE HERE
         */

        Segment iterator = this.head;
        while (iterator != null) {
            if (iterator.position.equals(p)) {
                return iterator.color;
            }
            iterator = iterator.next;
        }
        return null;
    }


    // shift all Segments to the previous Position while maintaining the old color
    public void move(Position p) {
        /*
         * TODO: ADD YOUR CODE HERE
         */
        if (this.stage == EvolutionStage.ENTANGLED) {
            throw new RuntimeException("Game Over");
        } else {
            if (Math.abs(this.getHeadPosition().getX() - p.getX()) + Math.abs(this.getHeadPosition().getY() - p.getY()) > 1) {
                throw new IllegalArgumentException("invalid input");
            }
            //check state.ENTANGLED
            Segment iterator = this.head;
            while (iterator != null) {
                if (p.equals(iterator.position)) {
                    this.stage = EvolutionStage.ENTANGLED;
                    throw new RuntimeException("You loss");
                }
                iterator = iterator.next;
            }
            //moving
            Segment newHead = new Segment(p, this.getSegmentColor(this.getHeadPosition()));
            newHead.next = this.head;
            this.head = newHead;
            Position previousDigest = null;
            if (!positionsPreviouslyOccupied.isEmpty()) {
                previousDigest = positionsPreviouslyOccupied.peek();
            }
            //check if space available for growth
            boolean available = true;
            iterator = this.head;
            while (iterator.next != null) {
                if (previousDigest!=null && previousDigest.equals(iterator.position)){
                    available=false;
                }
                if (iterator.next.next == null) {
                    iterator.color = iterator.next.color;
                    this.positionsPreviouslyOccupied.push(iterator.next.position);
                    this.tail = iterator;
                    iterator.next = null;
                } else {
                    iterator.color = iterator.next.color;
                    iterator = iterator.next;
                }
            }
            //append undigested segments
            if (turnsNeededToDigest != 0) {
                if (available){
                    turnsNeededToDigest--;
                    System.out.println(this.tail.position);
                    this.tail.next = new Segment(positionsPreviouslyOccupied.pop(),GameColors.SEGMENT_COLORS[randNumGenerator.nextInt(GameColors.SEGMENT_COLORS.length)]);
                    this.tail = this.tail.next;
                    this.length++;
                }
            }
            //check if Growing stage ends
            if (turnsNeededToDigest == 0) this.stage = EvolutionStage.FEEDING_STAGE;
            //check if stage BUTTERFLY
            if (this.length == goal){
                this.stage = EvolutionStage.BUTTERFLY;
                throw new RuntimeException("You win");
            }
        }
    System.out.println(this.length);
    }


    // a segment of the fruit's color is added at the end
    public void eat(Fruit f) {
        /*
         * TODO: ADD YOUR CODE HERE
         */
        if (!this.positionsPreviouslyOccupied.isEmpty()) {
            this.tail.next = new Segment(this.positionsPreviouslyOccupied.pop(), f.getColor());
            this.tail = this.tail.next;
            this.length++;
        }
        if (this.length == goal) {
            this.stage = EvolutionStage.BUTTERFLY;
            throw new RuntimeException("Game over, you win!!!");
        }
    }

    // the caterpillar moves one step backwards because of sourness
    public void eat(Pickle p) {
        /*
         * TODO: ADD YOUR CODE HERE
         */
        if (!positionsPreviouslyOccupied.isEmpty()) {
            if (this.length > 1) {
                Segment iter1 = this.head;
                Segment iter2 = this.head.next;
                while (true) {
                    if (iter1.next == null) {
                        iter1.position = positionsPreviouslyOccupied.pop();
                        break;
                    }
                    iter1.position = iter2.position;
                    iter1 = iter1.next;
                    iter2 = iter2.next;
                }
            } else this.head.position = positionsPreviouslyOccupied.pop();
        }
    }


    // all the caterpillar's colors shuffles around
    public void eat(Lollipop lolly) {
        /*
         * TODO: ADD YOUR CODE HERE
         */
        System.out.println(this.length);
        System.out.println(this.getHeadPosition());
        System.out.println(this.tail.position);
        Color[] colors = new Color[this.getLength()];
        Segment iterator = this.head;
        int index = 0;
        while (iterator != null) {
            colors[index] = iterator.color;
            index++;
            iterator = iterator.next;
        }
        for (int i = colors.length - 1; i > 0; i--) {
            int j = randNumGenerator.nextInt(i + 1);
            Color temp = colors[i];
            colors[i] = colors[j];
            colors[j] = temp;
        }
        index = 0;
        iterator = this.head;
        while (iterator != null) {
            iterator.color = colors[index];
            index++;
            iterator = iterator.next;
        }
    }

    // brain freeze!!
    // It reverses and its (new) head turns blue
    public void eat(IceCream gelato) {
        /*
         * TODO: ADD YOUR CODE HERE
         */
        Segment iter = this.head;
        Color[] c = new Color[this.length];
        for (int i = 0; iter != null; i++) {
            c[i] = iter.color;
            iter = iter.next;
        }
        Segment previous = null;
        Segment current = head;
        Segment next = null;
        while (current != null) {
            next = current.next;
            current.next = previous;
            previous = current;
            current = next;
        }
        this.tail = this.head;
        this.head = previous;

        iter = this.head;
        for (int i = 0; iter != null; i++) {
            iter.color = c[i];
            iter = iter.next;
        }
        if (this.head != null) {
            this.head.color = GameColors.BLUE;
        }
        this.positionsPreviouslyOccupied = new Stack<>();

    }

    // the caterpillar embodies a slide of Swiss cheese loosing half of its segments.
    public void eat(SwissCheese cheese) {
        /*
         * TODO: ADD YOUR CODE HERE
         */
        Segment iterator1 = this.head;
        Segment iterator2 = this.head;
        //update size
        this.length = (this.length + 1) / 2;
        //find second half
        Segment temp = this.head;
        for (int i = 0; i < this.length ; i++){
            temp = temp.next;
        }

        //push to temp stack
        Stack<Position> positions = new Stack<>();
        while(temp!=null){
            positions.push(temp.position);
            temp = temp.next;
        }
        //reverse stack
        while(!positions.isEmpty()){
            positionsPreviouslyOccupied.push(positions.pop());
        }

        //cutting
        while (iterator2 != null) {
            iterator1.color = iterator2.color;
            if (iterator2.next == null) break;
            iterator1 = iterator1.next;
            iterator2 = iterator2.next.next;
        }

        //update tail
        temp = head;
        for (int i = 0; i < this.length - 1; i++){
            temp = temp.next;
        }
        this.tail = temp;
        this.tail.next=null;
    }


    public void eat(Cake cake) {
        /*
         * TODO: ADD YOUR CODE HERE
         */

        this.stage = EvolutionStage.GROWING_STAGE;
        int energy = cake.getEnergyProvided();
        int grow = Math.min(this.positionsPreviouslyOccupied.size(), energy);


        int extended = 0;

        //add segments
        for (int i = 0; i < grow; i++) {
            Segment iterator = this.head;
            Color c = GameColors.SEGMENT_COLORS[randNumGenerator.nextInt(GameColors.SEGMENT_COLORS.length)];
            Position p = this.positionsPreviouslyOccupied.pop();
            while (iterator != null) {
                if (iterator.position.equals(p)) {
                    break;
                }
                iterator = iterator.next;
            }
            if (iterator != null && iterator.next != null) break;
            this.tail.next = new Segment(p, c);
            this.tail = this.tail.next;
            this.length++;
            extended++;

            //check if stage BUTTERFLY goal meet
            if (this.getLength() == this.goal) {
                this.stage = EvolutionStage.BUTTERFLY;
                throw new RuntimeException("You win!!!");
            }
        }

        //left over energy
        this.turnsNeededToDigest = energy-extended;
        if (turnsNeededToDigest == 0) {
            this.stage = EvolutionStage.FEEDING_STAGE;
        }
    }


    // This nested class was declared public for testing purposes
    public class Segment {
        private Position position;
        private Color color;
        private Segment next;

        public Segment(Position p, Color c) {
            this.position = p;
            this.color = c;
        }

    }


    public String toString() {
        Segment s = this.head;
        String gus = "";
        while (s != null) {
            String coloredPosition = GameColors.colorToANSIColor(s.color) +
                    s.position.toString() + GameColors.colorToANSIColor(Color.WHITE);
            gus = coloredPosition + " " + gus;
            s = s.next;
        }
        return gus;
    }

}