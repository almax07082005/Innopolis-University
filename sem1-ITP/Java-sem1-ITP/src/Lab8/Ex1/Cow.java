package Lab8.Ex1;

public class Cow extends Animal {
    @Override
    public void eat() {
        super.eat();
        System.out.println("That animal is cow");
    }

    @Override
    public void sleep() {
        super.sleep();
        System.out.println("That animal is cow");
    }

    @Override
    public void makeSound() {
        super.makeSound();
        System.out.println("That animal is cow");
    }
}
