//BANDARA H.G.T.D.
//2022e048
//Lab03


import java.util.ArrayList;
import java.util.Stack;

// Part 1: BookNode Class for BST
class BookNode {
    int id;
    String title;
    String author;
    boolean isAvailable;
    BookNode left, right; // left and right children for BST

    public BookNode(int id, String title, String author, boolean isAvailable) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isAvailable = isAvailable;
        this.left = this.right = null;
    }

    public void displayBookDetails() {
        System.out.println("Book ID: " + id);
        System.out.println("Title: " + title);
        System.out.println("Author: " + author);
        System.out.println("Available: " + (isAvailable ? "Yes" : "No"));
        System.out.println("------------------------------");
    }
}

// Part 1:I create a  BookCatalog Class using BST implementation
class BookCatalog {
    private BookNode root;

    public BookCatalog() {
        root = null;
    }

    // Insert book into BST
    public void insert(BookNode book) {
        root = insertRec(root, book);
    }

    private BookNode insertRec(BookNode root, BookNode book) {
        if (root == null) {
            root = book;
            return root;
        }
        if (book.title.compareTo(root.title) < 0)
            root.left = insertRec(root.left, book);
        else if (book.title.compareTo(root.title) > 0)
            root.right = insertRec(root.right, book);

        return root;
    }

    // Search for a book by title
    public BookNode search(String title) {
        return searchRec(root, title);
    }

    private BookNode searchRec(BookNode root, String title) {
        if (root == null || root.title.equals(title))
            return root;
        if (title.compareTo(root.title) < 0)
            return searchRec(root.left, title);

        return searchRec(root.right, title);
    }

    // In-order traversal to display books in sorted order
    public void inOrderTraversal() {
        inOrderRec(root);
    }

    private void inOrderRec(BookNode root) {
        if (root != null) {
            inOrderRec(root.left);
            root.displayBookDetails();
            inOrderRec(root.right);
        }
    }

    // Remove book by title
    public void remove(String title) {
        root = removeRec(root, title);
    }

    private BookNode removeRec(BookNode root, String title) {
        if (root == null) {
            return root;
        }

        if (title.compareTo(root.title) < 0) {
            root.left = removeRec(root.left, title);
        } else if (title.compareTo(root.title) > 0) {
            root.right = removeRec(root.right, title);
        } else {
            if (root.left == null) {
                return root.right;
            } else if (root.right == null) {
                return root.left;
            }

            root.title = minValue(root.right);
            root.right = removeRec(root.right, root.title);
        }

        return root;
    }

    private String minValue(BookNode root) {
        String minValue = root.title;
        while (root.left != null) {
            minValue = root.left.title;
            root = root.left;
        }
        return minValue;
    }
}

// Part 2; Graph Class,which i going to book Recommendations using adjacency list
class Graph {
    private ArrayList<ArrayList<BookNode>> adjList;

    public Graph() {
        adjList = new ArrayList<>();
    }

    public void addBook(BookNode book) {
        while (adjList.size() <= book.id) {
            adjList.add(new ArrayList<>());
        }
    }

    // Add a book recommendation
    public void addRecommendation(BookNode book1, BookNode book2) {
        adjList.get(book1.id).add(book2);
    }

    // Display all recommendations for a given book
    public void displayRecommendations(BookNode book) {
        System.out.println("Recommendations for: " + book.title);
        for (BookNode recommendedBook : adjList.get(book.id)) {
            System.out.println("- " + recommendedBook.title);
        }
    }

    // DFS traversal to find recommendations
    public void DFS(BookNode start) {
        boolean[] visited = new boolean[adjList.size()];
        Stack<BookNode> stack = new Stack<>();
        stack.push(start);

        while (!stack.isEmpty()) {
            BookNode current = stack.pop();
            if (!visited[current.id]) {
                visited[current.id] = true;
                System.out.println("Visited: " + current.title);
                for (BookNode neighbor : adjList.get(current.id)) {
                    if (!visited[neighbor.id]) {
                        stack.push(neighbor);
                    }
                }
            }
        }
    }

    // Remove a book from the graph
    public void remove(BookNode book) {
        adjList.set(book.id, new ArrayList<>());
    }
}

// Main class to  Execute all implemented methods
public class LibrarySystem {
    public static void main(String[] args) {
        // Part 1;- Create some books and insert them into the BookCatalog
        BookCatalog catalog = new BookCatalog();

        BookNode book1 = new BookNode(1, "Madolduwa", "Martin Wickramasinghe", true);
        BookNode book2 = new BookNode(2, "HeenSaraya", "Kumarathunga Munidasa", false);
        BookNode book3 = new BookNode(3, "Hathpana", "Kumarathunga Munidasa", true);

        catalog.insert(book1);
        catalog.insert(book2);
        catalog.insert(book3);

        // Display all books in sorted order using in-order traversal
        System.out.println("Books in catalog (sorted by title):");
        catalog.inOrderTraversal();

        // Search for a book by title
        BookNode foundBook = catalog.search("HeenSaraya");
        if (foundBook != null) {
            System.out.println("\nFound book: ");
            foundBook.displayBookDetails();
        } else {
            System.out.println("Book not found.");
        }

        //Part 2- Graph for book recommendations
        Graph graph = new Graph();

        graph.addBook(book1);
        graph.addBook(book2);
        graph.addBook(book3);

        //  Adding book recommendations;
        graph.addRecommendation(book1, book2);
        graph.addRecommendation(book2, book3);

        // Display  recommendations for a specific book
        graph.displayRecommendations(book1);

        //DFS traversal from a book to see all connected recommendations
        System.out.println("\nDFS Traversal from 'Madolduwa':");
        graph.DFS(book1);

        //Part 3:= Dynamic update to BST and Graph
        System.out.println("\nAdding a new book dynamically:");
        BookNode book4 = new BookNode(4, "New Book", "New Author", true);
        catalog.insert(book4); // Insert into BST
        graph.addBook(book4); // Add to Graph

        // Adding recommendations for the new book
        graph.addRecommendation(book4, book1);
        graph.addRecommendation(book4, book3);

        // Displaying recommendations for the new book
        graph.displayRecommendations(book4);

        //Removing a book from BST and updating the graph
        System.out.println("\nRemoving 'HeenSaraya' from BST and Graph:");
        catalog.remove("HeenSaraya"); // Removing from BST
        graph.remove(book2); // Remove from Graph

        // Final display after removal
        System.out.println("\nBooks in catalog after removal:");
        catalog.inOrderTraversal();
        graph.displayRecommendations(book1);

        // Part 3: 3. Observe and comment on memory allocation and deallocation
        // answers:
        // WHEN ADDING Inserting a new book involves allocating memory for a new BookNode object.
        // The BST structure grows dynamically as new nodes are added, and each node is linked
        // based on the book's title. The graph also dynamically allocates space to store
        // recommendations for the new book, growing the adjacency list as necessary.
        // REMOVING PART When a book is removed from the BST, memory occupied by the BookNode
        // is deallocated. The node is removed from the tree structure, and references to
        // the book are severed. In the graph, when a book is removed, its recommendations
        // are cleared from the adjacency list. Both operations demonstrate dynamic memory
        // management as nodes (books) are added and removed from the data structures.





        // Part 4: 2. Compare BST and graph performance for searching and retrieving books
        // answers:
        // Searching for a book in the BST involves comparing the title to the nodes' titles
        // in the tree. In a balanced tree, this search operation runs in O(log N) time,
        // making it efficient for a large catalog. However, in the worst-case scenario,
        // if the tree is unbalanced, the search time can degrade to O(N) time, where N
        // is the total number of books. Insertion into the BST also follows this pattern,
        // with O(log N) average time complexity for adding new books.
        // This makes the BST efficient for both searching and inserting books in most cases.

    }
}

