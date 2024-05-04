package Assignment4;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

/**
 * The Main class is the entry point of the program.
 * It contains the main method that reads user input and interacts with the Proxy class.
 */
public class Main {

    /**
     * The main method reads user input and interacts with the Proxy class based on the input commands.
     * The program continues to read input until the user enters the "end" command.
     *
     * @param args The command-line arguments passed to the program.
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Proxy proxy = new Proxy();

        while (true) {
            String[] input = scanner.nextLine().split(" ");
            String command = input[0];
            if (command.equals("end")) {
                break;
            }
            switch (command) {
                case "createBook":
                    proxy.createBook(input[1], input[2], input[3]);
                    break;
                case "createUser":
                    proxy.createUser(input[1], input[2]);
                    break;
                case "subscribe":
                    proxy.subscribe(input[1]);
                    break;
                case "unsubscribe":
                    proxy.unsubscribe(input[1]);
                    break;
                case "updatePrice":
                    proxy.updatePrice(input[1], input[2]);
                    break;
                case "readBook":
                    proxy.readBook(input[1], input[2]);
                    break;
                case "listenBook":
                    proxy.listenBook(input[1], input[2]);
                    break;
            }
        }

        System.out.println();
        scanner.close();
    }
}

/**
 * The Proxy class acts as a proxy for accessing and manipulating books and users.
 */
class Proxy {

    private final List<Book> books = new ArrayList<>();
    private final List<User> users = new ArrayList<>();
    private final Publisher publisher = new Publisher();

    /**
     * Retrieves a book with the specified title.
     *
     * @param title the title of the book to retrieve
     * @return the book with the specified title, or null if not found
     */
    public Book getBook(String title) {
        for (Book book : books) {
            if (book.getTitle().equals(title)) {
                return book;
            }
        }
        return null;
    }

    /**
     * Retrieves a user with the specified username.
     *
     * @param username the username of the user to retrieve
     * @return the user with the specified username, or null if not found
     */
    public User getUser(String username) {
        for (User user : users) {
            if (user.username().equals(username)) {
                return user;
            }
        }
        return null;
    }

    /**
     * Creates a new book with the specified title, author, and price.
     *
     * @param title  the title of the book
     * @param author the author of the book
     * @param price  the price of the book
     */
    public void createBook(String title, String author, String price) {
        if (getBook(title) == null) {
            books.add(new Book(title, author, price));
        } else {
            System.out.println("Book already exists");
        }
    }

    /**
     * Creates a new user with the specified type and username.
     *
     * @param type     the type of the user
     * @param username the username of the user
     */
    public void createUser(String type, String username) {
        if (getUser(username) == null) {
            users.add(UserFactory.createUser(type, username));
        } else {
            System.out.println("User already exists");
        }
    }

    /**
     * Subscribes a user to the publisher's notifications.
     *
     * @param username the username of the user to subscribe
     */
    public void subscribe(String username) {
        publisher.subscribe(getUser(username));
    }

    /**
     * Unsubscribes a user from the publisher's notifications.
     *
     * @param username the username of the user to unsubscribe
     */
    public void unsubscribe(String username) {
        publisher.unsubscribe(getUser(username));
    }

    /**
     * Updates the price of a book with the specified title and notifies subscribers.
     *
     * @param title the title of the book to update
     * @param price the new price of the book
     */
    public void updatePrice(String title, String price) {
        Objects.requireNonNull(getBook(title)).updatePrice(price);
        publisher.notifySubscribers(title, price);
    }

    /**
     * Allows a user to read a book with the specified title.
     *
     * @param username the username of the user
     * @param title    the title of the book to read
     */
    public void readBook(String username, String title) {
        Objects.requireNonNull(getUser(username)).readBook(title, Objects.requireNonNull(getBook(title)).getAuthor());
    }

    /**
     * Allows a user to listen to an audiobook with the specified title.
     *
     * @param username the username of the user
     * @param title    the title of the audiobook to listen to
     */
    public void listenBook(String username, String title) {
        Objects.requireNonNull(getUser(username)).listenBook(title, Objects.requireNonNull(getBook(title)).getAuthor());
    }
}

/**
 * The Publisher class represents a publisher that manages a list of subscribers.
 * It provides methods to subscribe, unsubscribe, and notify subscribers about price updates.
 */
class Publisher {
    private final List<User> subscribers = new ArrayList<>();

    /**
     * Subscribes a user to the publisher's list of subscribers.
     * If the user is already subscribed, a message is printed and no action is taken.
     * @param user the user to subscribe
     */
    public void subscribe(User user) {
        if (subscribers.contains(user)) {
            System.out.println("User already subscribed");
            return;
        }
        subscribers.add(user);
    }

    /**
     * Unsubscribes a user from the publisher's list of subscribers.
     * If the user is not subscribed, a message is printed and no action is taken.
     * @param user the user to unsubscribe
     */
    public void unsubscribe(User user) {
        if (!subscribers.contains(user)) {
            System.out.println("User is not subscribed");
            return;
        }
        subscribers.remove(user);
    }

    /**
     * Notifies all subscribers about a price update for a given title.
     * @param title the title of the item
     * @param price the updated price
     */
    public void notifySubscribers(String title, String price) {
        for (User user : subscribers) {
            System.out.println(user.username() + " notified about price update for " + title + " to " + price);
        }
    }
}

/**
 * Represents a book with a title, author, and price.
 */
class Book {
    private final String title;
    private final String author;
    private String price;

    /**
     * Constructs a book with the specified title, author, and price.
     *
     * @param title  the title of the book
     * @param author the author of the book
     * @param price  the price of the book
     */
    public Book(String title, String author, String price) {
        this.title = title;
        this.author = author;
        this.price = price;
    }

    /**
     * Returns the title of the book.
     *
     * @return the title of the book
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns the author of the book.
     *
     * @return the author of the book
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Updates the price of the book.
     *
     * @param price the new price of the book
     */
    public void updatePrice(String price) {
        this.price = price;
    }
}

/**
 * The User interface represents a user in the system.
 */
interface User {
    /**
     * Gets the username of the user.
     *
     * @return the username of the user
     */
    String username();

    /**
     * Reads a book with the specified title and author.
     *
     * @param title  the title of the book
     * @param author the author of the book
     */
    void readBook(String title, String author);

    /**
     * Listens to a book with the specified title and author.
     *
     * @param title  the title of the book
     * @param author the author of the book
     */
    void listenBook(String title, String author);
}

/**
 * Represents a standard user.
 * Implements the User interface.
 */
record StandardUser(String username) implements User {

    @Override
    public void readBook(String title, String author) {
        System.out.println(username + " reading " + title + " by " + author);
    }

    @Override
    public void listenBook(String title, String author) {
        System.out.println("No access");
    }
}

/**
 * Represents a premium user.
 * A premium user is a type of user that has additional privileges.
 */
record PremiumUser(String username) implements User {

    @Override
    public void readBook(String title, String author) {
        System.out.println(username + " reading " + title + " by " + author);
    }

    @Override
    public void listenBook(String title, String author) {
        System.out.println(username + " listening " + title + " by " + author);
    }
}

/**
 * The UserFactory class is responsible for creating instances of User objects based on the given type.
 */
abstract class UserFactory {
    /**
     * Creates a new User object based on the given type and username.
     *
     * @param type     the type of the user (e.g., "standard", "premium")
     * @param username the username of the user
     * @return a new User object of the specified type, or null if the type is invalid
     */
    public static User createUser(String type, String username) {
        if (type.equals("standard")) {
            return new StandardUser(username);
        } else if (type.equals("premium")) {
            return new PremiumUser(username);
        }
        return null;
    }
}
