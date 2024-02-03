package Lab11.Ex1;

public class InterfaceDemonstration {
    public static void main(String[] args) {
        Living[] livingThings = new Living[] {new Duck(), new Penguin()};

        for (Living livingThing : livingThings) {
            livingThing.live();
        }
    }
}
