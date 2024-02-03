package Lab8.Ex1;

public class Dog extends Animal {
    @Override
    public void eat() {
        super.eat();
        System.out.println("That animal is dog");
    }

    @Override
    public void sleep() {
        super.sleep();
        System.out.println("That animal is dog");
    }

    @Override
    public void makeSound() {
        super.makeSound();
        System.out.println("That animal is dog");
    }
}
