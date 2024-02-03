package Lab11.Ex2;

class Animal {
    String name;
    int length;

    public Animal(String name, int length) {
        this.name = name;
        this.length = length;
    }

    @Override
    public String toString() {
        return "Animal{" +
                "name='" + name + '\'' +
                ", length=" + length +
                '}';
    }
}
