package Lab11.Ex1;

public class Submarine implements Swimmable {
    private boolean isSwimming = false;

    @Override
    public void swim() {
        isSwimming = true;
        System.out.println("Submarine is swimming");
    }

    @Override
    public void stopSwimming() {
        if (isSwimming) {
            isSwimming = false;
            System.out.println("Submarine stopped swimming");
        } else {
            System.out.println("Submarine is not swimming");
        }
    }
}
