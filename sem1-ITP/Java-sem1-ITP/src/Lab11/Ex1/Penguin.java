package Lab11.Ex1;

public class Penguin implements Swimmable, Living {
    private boolean isSwimming = false;

    @Override
    public void swim() {
        isSwimming = true;
        System.out.println("Penguin is swimming");
    }

    @Override
    public void stopSwimming() {
        if (isSwimming) {
            isSwimming = false;
            System.out.println("Penguin stopped swimming");
        } else {
            System.out.println("Penguin is not swimming");
        }
    }
}
