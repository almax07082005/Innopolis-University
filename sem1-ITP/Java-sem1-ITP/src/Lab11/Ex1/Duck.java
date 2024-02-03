package Lab11.Ex1;

public class Duck implements Swimmable, Flyable, Living {
    private boolean isSwimming = false;
    private boolean isFlying = false;

    @Override
    public void swim() {
        isSwimming = true;
        System.out.println("Duck is swimming");
    }

    @Override
    public void stopSwimming() {
        if (isSwimming) {
            isSwimming = false;
            System.out.println("Duck stopped swimming");
        } else {
            System.out.println("Duck is not swimming");
        }
    }

    @Override
    public void fly() {
        isFlying = true;
        System.out.println("Duck is flying");
    }

    @Override
    public void stopFlying() {
        if (isFlying) {
            isFlying = false;
            System.out.println("Duck stopped flying");
        } else {
            System.out.println("Duck is not flying");
        }
    }
}
