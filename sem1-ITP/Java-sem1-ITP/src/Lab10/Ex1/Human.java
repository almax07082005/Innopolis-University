package Lab10.Ex1;

public class Human extends Creature {

    @Override
    public void bear(String name) {
        this.name = name;
        System.out.println("The Human " + this.name + " was born");
    }

    @Override
    public void die() {
        System.out.println("The Human " + this.name + " has died");
    }
}
