import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
// Book Class
class Book {
    String title, author, isbn;
    int copies;

    public Book(String title, String author, String isbn, int copies) {
        this.title = title.trim();
        this.author = author.trim();
        this.isbn = isbn.trim();
        this.copies = copies;
    }

    @Override
    public String toString() {
        return String.format("%-30s %-20s %-15s %-5d", title, author, isbn, copies);
    }
}

// Exception hierarchy-I inherit the exceptions to exception class
class BookCatalogException extends Exception {
    public BookCatalogException(String message) {
        super(message);
    }
}

class InvalidISBNException extends BookCatalogException {
    public InvalidISBNException(String isbn) {
        super("Invalid ISBN: " + isbn);
    }
}

class DuplicateISBNException extends BookCatalogException {
    public DuplicateISBNException(String isbn) {
        super("Duplicate ISBN found: " + isbn);
    }
}

class MalformedBookEntryException extends BookCatalogException {
    public MalformedBookEntryException(String line) {
        super("Malformed book entry: " + line);
    }
}

class InsufficientArgumentsException extends BookCatalogException {
    public InsufficientArgumentsException() {
        super("Insufficient arguments. Usage: java LibraryBookTracker <catalog.txt> <command>");
    }
}

class InvalidFileNameException extends BookCatalogException {
    public InvalidFileNameException(String filename) {
        super("Invalid file name. Expected a .txt file: " + filename);
    }
}

public class Main {
    private static final String ERROR_LOG = "errors.log";
    private static List<Book> catalog = new ArrayList<>();

    //Set initial state to the zero
    private static int validRecords = 0, searchResults = 0, booksAdded = 0, errors = 0;
    // the main function
    public static void main(String[] args) {


        try {
            //String filename = args[0];
            if (args.length < 2)
                throw new InsufficientArgumentsException();

            String filename = args[0];
            //File filenames = new File("record.txt");

            // this for if the file is another type rhrow an error
            if (!filename.endsWith(".txt"))
                throw new InvalidFileNameException(filename);

            File file = new File(filename);
            file.getParentFile().mkdirs();
            file.createNewFile(); // no effect if file exists

            readCatalog(filename);

            StringBuilder commandBuilder = new StringBuilder();
            for (int i = 1; i < args.length; i++) {
                commandBuilder.append(args[i]);
                if (i != args.length - 1) commandBuilder.append(" ");
            }
            String command = commandBuilder.toString();

            if (command.matches("\\d{13}")) {
                searchByISBN(command);
            } else if (command.contains(":")) {
                addBook(command, filename);
            } else {
                searchByTitle(command);
            }

        } catch (BookCatalogException e) {
            logError(e.getMessage(), "");
            System.err.println(e.getMessage());
            errors++;
        } catch (IOException e) {
            System.err.println("IO Error: " + e.getMessage());
        } finally {
            System.out.println("\nSummary:");
            System.out.printf("Valid records processed: %d\n", validRecords);
            System.out.printf("Search results: %d\n", searchResults);
            System.out.printf("Books added: %d\n", booksAdded);
            System.out.printf("Errors encountered: %d\n", errors);
            System.out.println("Thank you for using the Library Book Tracker.");
        }
    }

    private static void readCatalog(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    Book book = parseBook(line);
                    catalog.add(book);
                    validRecords++;
                } catch (BookCatalogException e) {
                    logError(e.getMessage(), line);
                    errors++;
                }
            }
        } catch (IOException e) {
            logError("Error reading file: " + e.getMessage(), "");
        }
    }

    private static Book parseBook(String line) throws BookCatalogException {
        String[] parts = line.split(":");
        if (parts.length != 4 || Arrays.stream(parts).anyMatch(String::isEmpty)) {
            throw new MalformedBookEntryException(line);
        }

        String title = parts[0];
        String author = parts[1];
        String isbn = parts[2];
        String copiesStr = parts[3];

        if (!isbn.matches("\\d{13}")) {
            throw new InvalidISBNException(isbn);
        }

        int copies;
        try {
            copies = Integer.parseInt(copiesStr.trim());
        } catch (NumberFormatException e) {
            throw new MalformedBookEntryException(line);
        }

        return new Book(title, author, isbn, copies);
    }

    private static void searchByTitle(String keyword) {
        System.out.printf("\nSearching for title containing: \"%s\"\n", keyword);
        boolean found = false;
        for (Book book : catalog) {
            if (book.title.toLowerCase().contains(keyword.toLowerCase())) {
                System.out.println(book);
                searchResults++;
                found = true;
            }
        }
        if (!found) {
            System.out.println("No matching titles found.");
        }
    }

    private static void searchByISBN(String isbn) throws BookCatalogException {
        System.out.printf("\nSearching for ISBN: %s\n", isbn);
        if (!isbn.matches("\\d{13}")) {
            throw new InvalidISBNException(isbn);
        }

        List<Book> matches = new ArrayList<>();
        for (Book book : catalog) {
            if (book.isbn.equals(isbn)) {
                matches.add(book);
            }
        }

        if (matches.size() > 1) {
            throw new DuplicateISBNException(isbn);
        } else if (matches.size() == 1) {
            System.out.println(matches.get(0));
            searchResults++;
        } else {
            System.out.println("No book found with ISBN " + isbn);
        }
    }

    private static void addBook(String entry, String filename) {
        try {
            Book book = parseBook(entry);
            for (Book b : catalog) {
                if (b.isbn.equals(book.isbn)) {
                    throw new DuplicateISBNException(book.isbn);
                }
            }
            catalog.add(book);
            catalog.sort(Comparator.comparing(b -> b.title.toLowerCase()));
            booksAdded++;

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
                for (Book b : catalog) {
                    writer.write(String.format("%s:%s:%s:%d\n", b.title, b.author, b.isbn, b.copies));
                }
            }
            System.out.println("Book added successfully:");
            System.out.println(book);
        } catch (BookCatalogException | IOException e) {
            logError(e.getMessage(), entry);
            System.err.println(e.getMessage());
            errors++;
        }
    }

    private static void logError(String message, String line) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ERROR_LOG, true))) {
            writer.write("[" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "]");
            writer.write(" " + message);
            if (!line.isEmpty()) {
                writer.write(" | Offending line: " + line);
            }
            writer.newLine();
        } catch (IOException ignored) {}
    }
}

