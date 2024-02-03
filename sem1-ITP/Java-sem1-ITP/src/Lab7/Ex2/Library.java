package Lab7.Ex2;

public class Library {
    public static void main(String[] args) {
        Book[] books = new Book[]{ new Book("In the garden of Beasts", new Author("Erik Larson", "erik@mail.ru", 'm'), 399.99, 2) };
        books[0].setQty(100);
        books[0].setPrice(599.99);
        System.out.println(books[0]);
    }
}
