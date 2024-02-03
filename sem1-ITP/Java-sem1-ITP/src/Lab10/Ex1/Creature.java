package Lab10.Ex1;

public abstract class Creature {
    String name = null;
    boolean isAlive = false;

    public abstract void bear(String name);
    public abstract void die();

    public final void shoutName() {
        if (this.name != null) System.out.println(name);
        else System.out.println("This creature has no name");
    }
}
