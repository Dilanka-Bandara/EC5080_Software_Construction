// BANDARA H.G.T.D.
//2022/E/048
// lab 02
//EC5080: Software Construction

import java.util.ArrayList;
//Book class
class Book {
    // for creating Attributes
    int id;
    String title;
    String author;
    boolean isAvailable;

    // Constructor to initialize book details
    public Book(int id, String title, String author, boolean isAvailable) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isAvailable = isAvailable;
    }

    // A Method to display book details
    public void displayBookDetails() {
        System.out.println("Book ID: " + id);
        System.out.println("Title: " + title);
        System.out.println("Author: " + author);
        System.out.println("Available: " + (isAvailable ? "Yes" : "No"));
        System.out.println("------------------------------");
    }
}
// Library class
class Library {
    // I use this List to store books
    private ArrayList<Book> books;

    // Constructor to initialize the library in library class
    public Library() {
        books = new ArrayList<>();
    }

    // Method to add a book to the library
    public void addBook(Book book) {
        books.add(book);
    }

    // Method to remove a book from the library
    public void removeBook(Book book) {
        books.remove(book);
    }

    // Method to display all books in the library using loops
    public void displayAllBooks() {
        System.out.println("Library Books:");
        for (Book book : books) {
            book.displayBookDetails();
        }
    }
}

public class main {
    public static void main(String[] args) {
        // Part 1: Book Class
        System.out.println("Part 1: Book Class");
        Book book1 = new Book(1, "Madolduwa", "Martin Wikramasinghe", true); // I am going to declare Madolduwa is Available
        Book book2 = new Book(2, "HeenSaraya", "Kumarathunga Munidasa", false); // I am going to declare Heensaraya is unavailable
        Book book3 = new Book(3, "Hathpana", "kumarathunga Munidasa", true);    // I am going to declare hathpana is Available

        // in prt 01 : Displaying book details
        book1.displayBookDetails();
        book2.displayBookDetails();
        book3.displayBookDetails();

        // Part 2: Aliasing and References
        System.out.println("\nPart 2: Aliasing and References");
        // Creating a second reference pointing to the same object
        Book bookAlias = book1;
        bookAlias.title = "Magul kama Modified"; // Modify the title through bookAlias

        // Desplaying book details through both references
        System.out.println("Book 1 Details (after aliasing):");
        book1.displayBookDetails();
        System.out.println("Book Alias Details: (after aliasing):");
        bookAlias.displayBookDetails();

        // Part 3: Library Class
        System.out.println("\nPart 3: Library Class");

        // Creating Library object
        Library library = new Library();

        // Adding books to library
        // I am going to add previous books in to the library
        library.addBook(book1);
        library.addBook(book2);
        library.addBook(book3);

        // Displaying all books in the library
        library.displayAllBooks();

        //Display all books in the library before remove
        System.out.println("\n #### Before removing a book: ####");
        library.displayAllBooks();

        // Removing a book and displaying again
        library.removeBook(book2);
        System.out.println("\n #### After removing a book: ####");
        library.displayAllBooks();
    }
}
