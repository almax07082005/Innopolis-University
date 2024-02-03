package Lab11.Ex2;

public class Main {
    public static void main(String[] args) {
        AnimalList animalList = new AnimalList(10);
        Animal dog = new Animal("Dog", 50);
        Animal cat = new Animal("Cat", 30);

        animalList.addAnimal(dog);
        animalList.addAnimal(cat);
        animalList.displayAnimals();

        Animal elephant = new Animal("Elephant", 300);
        animalList.updateAnimal(0, elephant);
        animalList.displayAnimals();

        animalList.removeAnimal(1);
        animalList.displayAnimals();
    }
}
