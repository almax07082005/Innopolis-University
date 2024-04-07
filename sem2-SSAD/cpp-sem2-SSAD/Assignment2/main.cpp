#include <iostream>
#include <unordered_map>
#include <vector>
#include <fstream>

std::ifstream fin;
std::ofstream fout;

/**
 * @class Character
 * @brief Represents a character in a game.
 * 
 * The Character class stores information about a character, such as its name and health points.
 * It provides methods to retrieve the character's name and health points, as well as to modify them.
 */
class Character {
private:
    int healthPoints; /**< The health points of the character. */
    std::string name; /**< The name of the character. */
public:
    /**
     * @brief Default constructor for the Character class.
     */
    Character() {}

    /**
     * @brief Constructor for the Character class.
     * @param name The name of the character.
     * @param healthPoints The health points of the character.
     */
    Character(std::string name, int healthPoints) {
        this->name = name;
        this->healthPoints = healthPoints;
    }

    /**
     * @brief Get the name of the character.
     * @return The name of the character.
     */
    std::string getName() const {
        return this->name;
    }

    /**
     * @brief Get the health points of the character.
     * @return The health points of the character.
     */
    int getHP() const {
        return this->healthPoints;
    }

    /**
     * @brief Reduce the character's health points by the specified damage.
     * @param damage The amount of damage to be taken.
     */
    void takeDamage(int damage) {
        this->healthPoints -= damage;
    }

    /**
     * @brief Increase the character's health points by the specified heal value.
     * @param healValue The amount of health points to be healed.
     */
    void heal(int healValue) {
        this->healthPoints += healValue;
    }
};

/**
 * @class PhysicalItem
 * @brief Represents a physical item in the game.
 * 
 * The PhysicalItem class provides functionality to represent a physical item that can be used by characters in the game.
 * It contains methods to use the item, get its name, and perform various actions related to the item.
 * 
 * @note This is an abstract base class and should not be instantiated directly.
 */
class PhysicalItem {
private:
    bool isUsableOnce;
    std::string name;
    Character owner;
public:
    void use(Character user, Character target);
    std::string getName() const;
protected:
    virtual Character getOwner() const {
        return this->owner;
    }
    virtual void useCondition(Character user, Character target);
    virtual void giveDamageTo(Character to, int damage);
    virtual void giveHealTo(Character to, int heal);
    virtual void afterUse();
    virtual void useLogic(Character user, Character target);
};

class Weapon : public PhysicalItem {
private:
    int damage;
    void useLogic(Character user, Character target) override;
public:
    int getDamage() const {
        return this->damage;
    }
};

class Potion : public PhysicalItem {
private:
    int healValue;
    void useLogic(Character user, Character target) override;
public:
    int getHealValue() const {
        return this->healValue;
    }
};

class Spell : public PhysicalItem {
private:
    std::vector<Character> allowedTargets;
    void useLogic(Character user, Character target) override;
public:
    int getNumAllowedTargets() const {
        return this->allowedTargets.size();
    }
};

template <typename T>
concept DerivedFromPhysicalItem = requires {
    { std::is_base_of<PhysicalItem, T>::value } -> std::same_as<bool>;
};

template <typename T>
class Container {
protected:
    std::unordered_map<std::string, T> elements;
public:
    virtual void addItem(T newItem) = 0;
};

/**
 * @brief A generic container class for storing items derived from PhysicalItem.
 * 
 * This class provides functionality to add, remove, and find items in the container.
 * The container is implemented using an unordered map.
 * 
 * @tparam T The type of items to be stored in the container. It must be derived from PhysicalItem.
 */
template <DerivedFromPhysicalItem T>
class Container<T> {
protected:
    std::unordered_map<std::string, T> elements; /**< The underlying unordered map to store the items. */
public:
    /**
     * @brief Adds a new item to the container.
     * 
     * If an item with the same name already exists in the container, the new item is not added.
     * 
     * @param newItem The item to be added.
     */
    virtual void addItem(T newItem) {
        try {
            elements.at(newItem.getName());
        } catch (const std::out_of_range& e) {
            elements[newItem.getName()] = newItem;
        }
    }

    /**
     * @brief Removes an item from the container.
     * 
     * If the item is not found in the container, nothing happens.
     * 
     * @param newItem The item to be removed.
     */
    void removeItem(T newItem) {
        try {
            elements.at(newItem.getName());
            elements.erase(newItem.getName());
        } catch (const std::out_of_range& e) {}
    }

    /**
     * @brief Finds an item in the container.
     * 
     * @param item The item to be found.
     * @return true if the item is found, false otherwise.
     */
    bool find(T item) {
        try {
            elements.at(item.getName());
            return true;
        } catch (const std::out_of_range& e) {
            return false;
        }
    }

    /**
     * @brief Finds an item in the container by its name.
     * 
     * @param itemName The name of the item to be found.
     * @return true if the item is found, false otherwise.
     */
    bool find(std::string itemName) {
        try {
            elements.at(itemName);
            return true;
        } catch (const std::out_of_range& e) {
            return false;
        }
    }

    /**
     * @brief Removes an item from the container by its name.
     * 
     * If the item is not found in the container, nothing happens.
     * 
     * @param itemName The name of the item to be removed.
     */
    void removeItem(std::string itemName) {
        try {
            elements.at(itemName);
            elements.erase(itemName);
        } catch (const std::out_of_range& e) {}
    }
};

/**
 * @brief A class template for a container with a maximum capacity.
 * 
 * This class extends the Container class and adds a maximum capacity constraint to the container.
 * It allows adding items to the container only if the current number of elements is less than the maximum capacity.
 * The container can store elements of any type.
 */
template <typename T>
class ContainerWithMaxCapacity : public Container<T> {
private:
    int maxCapacity; /**< The maximum capacity of the container. */
public:
    /**
     * @brief Constructs a new ContainerWithMaxCapacity object.
     * 
     * @param maxCapacity The maximum capacity of the container. Defaults to 0.
     */
    ContainerWithMaxCapacity(int maxCapacity = 0) {
        this->maxCapacity = maxCapacity;
    }

    /**
     * @brief Adds an item to the container.
     * 
     * This function overrides the addItem function from the base Container class.
     * It adds the item to the container only if the current number of elements is less than the maximum capacity.
     * 
     * @param item The item to be added to the container.
     */
    void addItem(T item) override {
        if (this->elements.size() < maxCapacity) {
            Container<T>::addItem(item);
        }
    }

    /**
     * @brief Displays the elements in the container.
     * 
     * This function prints the elements in the container to the standard output.
     */
    void show() {
        for (auto& item : this->elements) {
            std::cout << item.first << " ";
        }
    }
};

using Arsenal = ContainerWithMaxCapacity<Weapon>;
using MedicalBag = ContainerWithMaxCapacity<Potion>;
using SpellBook = ContainerWithMaxCapacity<Spell>;

class WeaponUser : virtual public Character {
protected:
    Arsenal arsenal;
public:
    void attack(Character target, std::string weaponName);
    void showWeapons();
};

class PotionUser : virtual public Character {
protected:
    MedicalBag medicalBag;
public:
    void drink(Character target, std::string potionName);
    void showPotions();
};

class SpellUser : virtual public Character {
protected:
    SpellBook spellBook;
public:
    void cast(Character target, std::string spellName);
    void showSpells();
};

/**
 * @class Fighter
 * @brief Represents a fighter character in the game.
 * 
 * The Fighter class inherits from WeaponUser and PotionUser classes and represents a character
 * that can use weapons and potions. It has a maximum limit on the number of weapons and potions
 * that can be used.
 */
class Fighter : public WeaponUser, public PotionUser {
public:
    static const int maxAllowedWeapons = 3; /**< The maximum number of weapons allowed for the fighter. */
    static const int maxAllowedPotions = 5; /**< The maximum number of potions allowed for the fighter. */

    /**
     * @brief Constructs a Fighter object with the given name and health points.
     * 
     * @param name The name of the fighter.
     * @param healthPoints The initial health points of the fighter.
     */
    Fighter(std::string name, int healthPoints) : Character(name, healthPoints) {}
};

/**
 * @class Archer
 * @brief Represents an Archer character in the game.
 * 
 * The Archer class inherits from the WeaponUser, PotionUser, and SpellUser classes.
 * It defines the maximum number of allowed weapons, potions, and spells for an Archer character.
 * 
 * @see WeaponUser
 * @see PotionUser
 * @see SpellUser
 */
class Archer : public WeaponUser, public PotionUser, public SpellUser {
public:
    static const int maxAllowedWeapons = 2; /**< The maximum number of allowed weapons for an Archer. */
    static const int maxAllowedPotions = 3; /**< The maximum number of allowed potions for an Archer. */
    static const int maxAllowedSpells = 2; /**< The maximum number of allowed spells for an Archer. */

    /**
     * @brief Constructs an Archer object with the given name and health points.
     * 
     * @param name The name of the Archer.
     * @param healthPoints The initial health points of the Archer.
     */
    Archer(std::string name, int healthPoints) : Character(name, healthPoints) {}
};

/**
 * @class Wizard
 * @brief Represents a wizard character in the game.
 * 
 * The Wizard class inherits from SpellUser and PotionUser classes and represents a character
 * that can use spells and potions. It has a maximum limit for the number of spells and potions
 * that can be used.
 */
class Wizard : public SpellUser, public PotionUser {
public:
    static const int maxAllowedSpells = 10; /**< The maximum number of spells allowed for the wizard. */
    static const int maxAllowedPotions = 10; /**< The maximum number of potions allowed for the wizard. */

    /**
     * @brief Constructs a new Wizard object.
     * 
     * @param name The name of the wizard.
     * @param healthPoints The initial health points of the wizard.
     */
    Wizard(std::string name, int healthPoints) : Character(name, healthPoints) {}
};

/**
 * @brief The main function of the program.
 * 
 * This function reads commands from an input file and performs corresponding actions based on the command.
 * The input file should be formatted correctly with each command on a new line.
 * 
 * Supported commands:
 * - Create character: Creates a new character of a specific type with a given name and initial HP.
 * - Create item weapon: Creates a new weapon item owned by a character with a given name and damage value.
 * - Create item potion: Creates a new potion item owned by a character with a given name and heal value.
 * - Create item spell: Creates a new spell item owned by a character with a given name and a list of character names it can be cast on.
 * - Attack: Performs an attack action between two characters using a specific weapon.
 * - Cast: Performs a spell casting action between two characters using a specific spell.
 * - Drink: Performs a potion drinking action by a character using a specific potion.
 * - Dialogue: Prints a dialogue by a speaker with a given speech.
 * - Show characters: Displays all created characters.
 * - Show weapons: Displays all weapons owned by a specific character.
 * - Show potions: Displays all potions owned by a specific character.
 * - Show spells: Displays all spells owned by a specific character.
 * 
 * @return The exit status of the program.
 */
int main() {
    fin.open("input.txt");

    int n;
    fin >> n;

    for (int i = 0; i < n; i++) {
        std::string command;
        std::cin >> command;

        if (command == "Create character") {
            std::string type, name;
            int initHP;
            fin >> type >> name >> initHP;

            if (type == "fighter") {
                Fighter fighter(name, initHP);
            } else if (type == "wizard") {
                Wizard wizard(name, initHP);
            } else if (type == "archer") {
                Archer archer(name, initHP);
            }
        }
        else if (command == "Create item weapon") {
            std::string ownerName, weaponName;
            int damageValue;
            fin >> ownerName >> weaponName >> damageValue;
        }
        else if (command == "Create item potion") {
            std::string ownerName, potionName;
            int healValue;
            fin >> ownerName >> potionName >> healValue;
        }
        else if (command == "Create item spell") {
            std::string ownerName, spellName;
            int m;
            std::vector<std::string> characterNames;
            fin >> ownerName >> spellName >> m;
            for (int j = 0; j < m; j++) {
                std::string characterName;
                fin >> characterName;
                characterNames.push_back(characterName);
            }
        }
        else if (command == "Attack") {
            std::string attackerName, targetName, weaponName;
            fin >> attackerName >> targetName >> weaponName;
        }
        else if (command == "Cast") {
            std::string casterName, targetName, spellName;
            fin >> casterName >> targetName >> spellName;
        }
        else if (command == "Drink") {
            std::string supplierName, drinkerName, potionName;
            fin >> supplierName >> drinkerName >> potionName;
        }
        else if (command == "Dialogue") {
            std::string speaker;
            int sp_len;
            std::vector<std::string> speech;
            fin >> speaker >> sp_len;
            for (int j = 0; j < sp_len; j++) {
                std::string speechPart;
                fin >> speechPart;
                speech.push_back(speechPart);
            }
        }
        else if (command == "Show characters") {
        }
        else if (command == "Show weapons") {
            std::string characterName;
            fin >> characterName;
        }
        else if (command == "Show potions") {
            std::string characterName;
            fin >> characterName;
        }
        else if (command == "Show spells") {
            std::string characterName;
            fin >> characterName;
        }
    }

    return EXIT_SUCCESS;
}
