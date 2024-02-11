#include <iostream>

class Animal {
protected:
    std::string name;
    int age;
public:
    Animal(): age(0) {}
    virtual void makeSound() = 0;
};

class LandAnimal : virtual public Animal {
public:
    void makeSound() override {
        std::cout << "I am on land!\n";
    }
    virtual void walk() {
        std::cout << "I am walking!\n";
    }
};

class WaterAnimal : virtual public Animal {
public:
    void makeSound() override {
        std::cout << "I am in water!\n";
    }
    virtual void swim() {
        std::cout << "I am swimming!\n";
    }
};

class Lion : public LandAnimal {
public:
    void makeSound() override {
        std::cout << "Lion is on land!\n";
    }
    void walk() override {
        std::cout << "Lion is walking!\n";
    }
};

class Dolphin : public WaterAnimal {
public:
    void makeSound() override {
        std::cout << "Dolphin is in water!\n";
    }
    void swim() override {
        std::cout << "Dolphin is swimming!\n";
    }
};

class Frog : public LandAnimal, public WaterAnimal {
public:
    void makeSound() override {
        LandAnimal::makeSound();
        WaterAnimal::makeSound();
        std::cout << std::endl;
    }
    void walk() override {
        LandAnimal::walk();
        std::cout << "But I am frog!\n";
    }
    void swim() override {
        WaterAnimal::swim();
        std::cout << "But I am frog!\n";
    }
};

int main() {
    Animal* animals[3];
    animals[0] = new Frog();
    animals[1] = new Lion();
    animals[2] = new Dolphin();

    for (Animal* animal : animals) {
        animal->makeSound();
    }

    return EXIT_SUCCESS;
}
