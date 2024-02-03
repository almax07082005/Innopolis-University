package Lab7.Ex2;

public class Book {
    private final String name;
    private final Author author;
    private double price;
    private int qty;

    public Book(String name, Author author, double price) {
        this.name = name;
        this.author = author;
        this.price = price;
        this.qty = 0;
    }
    public Book(String name, Author author, double price, int qty) {
        this.name = name;
        this.author = author;
        this.price = price;
        this.qty = qty;
    }
    public String getName() { return this.name; }
    public Author getAuthor() { return this.author; }
    public double getPrice() { return this.price; }
    public void setPrice(double price) { this.price = price; }
    public int getQty() { return this.qty; }
    public void setQty(int qty) { this.qty = qty; }
    public String toString() { return "Book[name=" + this.name + ",Author[name=" + this.author.name + ",email=" + this.author.email + ",gender=" + this.author.gender + "],price=" + this.price + ",qty=" + this.qty + "]"; }
}
