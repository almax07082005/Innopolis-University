package Lab9.Ex2;

public enum Drinks {
    CokeCola(100),
    Sprite(60),
    Fanta(115);
    private final int price;
    Drinks(int price) {
        this.price = price;
    }
}
