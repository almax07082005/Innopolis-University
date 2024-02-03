package Lab8.Ex1;

public class Cat extends Animal {
    @Override
    public void eat() {
        super.eat();
        System.out.println("That animal is cat");
    }

    @Override
    public void sleep() {
        super.sleep();
        System.out.println("That animal is cat");
    }

    @Override
    public void makeSound() {
        super.makeSound();
        System.out.println("That animal is cat");
    }
}
