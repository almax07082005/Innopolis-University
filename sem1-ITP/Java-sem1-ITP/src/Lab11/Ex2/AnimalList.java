package Lab11.Ex2;

class AnimalList {
    Animal[] animals;
    int size;

    public AnimalList(int capacity) {
        this.animals = new Animal[capacity];
        this.size = 0;
    }

    public void addAnimal(Animal animal) {
        if (size < animals.length) {
            this.animals[size] = animal;
            size++;
        } else {
            System.out.println("No more space to add new animals");
        }
    }

    public void removeAnimal(int index) {
        if (index >= 0 && index < size) {
            for (int i = index; i < size - 1; i++) {
                animals[i] = animals[i + 1];
            }
            size--;
        } else {
            System.out.println("Invalid index");
        }
    }

    public void updateAnimal(int index, Animal animal) {
        if (index >= 0 && index < size) {
            this.animals[index] = animal;
        } else {
            System.out.println("Invalid index");
        }
    }

    public void displayAnimals() {
        for (int i = 0; i < size; i++) {
            System.out.println(animals[i]);
        }
    }
}