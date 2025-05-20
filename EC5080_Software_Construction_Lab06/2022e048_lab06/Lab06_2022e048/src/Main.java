import java.io.*;
import java.util.regex.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
//import com.google.gson.*;

// Custom extxceptions 
class MalformedHTMLBlockException extends Exception {
    public MalformedHTMLBlockException(String message) {
        super(message);
    }
}

class InvalidBookEntryException extends Exception {
    public InvalidBookEntryException(String message) {
        super(message);
    }
}

class InvalidEmailFormatException extends Exception {
    public InvalidEmailFormatException(String message) {
        super(message);
    }
}

public class Main {

    public static void processHTML() {
        System.out.println("\n--- Task 1: HTML Processing ---\n");
        try {
            BufferedReader reader = new BufferedReader(new FileReader("sample.html"));
            StringBuilder content = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
            reader.close();

            String regex = "<div class=\"post\">\\s*<h2>(.*?)</h2>\\s*<span class=\"author\">Author: (.*?)</span>\\s*<span class=\"date\">Published: (.*?)</span>\\s*</div>";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(content.toString());

            System.out.printf("%-30s %-20s %-15s%n", "Title", "Author", "Date");
            System.out.println("-------------------------------------------------------------");

            while (matcher.find()) {
                String title = matcher.group(1);
                String author = matcher.group(2);
                String date = matcher.group(3);

                if (title.isEmpty() || author.isEmpty() || date.isEmpty()) {
                    throw new MalformedHTMLBlockException("Missing fields in post block");
                }

                if (!date.matches("\\d{4}-\\d{2}-\\d{2}")) {
                    throw new MalformedHTMLBlockException("Invalid date format: " + date);
                }

                System.out.printf("%-30s %-20s %-15s%n", title, author, date);
            }

        } catch (MalformedHTMLBlockException e) {
            System.err.println("Malformed HTML: " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void processXML() {
        System.out.println("\n--- Task 2: XML Processing ---\n");
        try {
            File inputFile = new File("catalog.xml");
            PrintWriter validWriter = new PrintWriter("parsed_books.txt");
            PrintWriter errorWriter = new PrintWriter("xml_errors.log");

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(inputFile);
            doc.getDocumentElement().normalize();

            NodeList books = doc.getElementsByTagName("book");

            for (int i = 0; i < books.getLength(); i++) {
                try {
                    Element book = (Element) books.item(i);
                    String id = book.getAttribute("id");
                    String title = book.getElementsByTagName("title").item(0).getTextContent();
                    String author = book.getElementsByTagName("author").item(0).getTextContent();
                    String priceStr = book.getElementsByTagName("price").item(0).getTextContent();

                    if (title.isEmpty() || author.isEmpty()) {
                        throw new InvalidBookEntryException("Title or author is empty");
                    }

                    double price = Double.parseDouble(priceStr);
                    if (price <= 0) {
                        throw new InvalidBookEntryException("Invalid price: " + priceStr);
                    }

                    validWriter.println(id + " - " + title + " by " + author + " ($" + price + ")");
                    System.out.println(id + " - " + title + " by " + author + " ($" + price + ")");

                } catch (InvalidBookEntryException | NumberFormatException e) {
                    errorWriter.println("Error in book entry: " + e.getMessage());
                }
            }

            validWriter.close();
            errorWriter.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void processJSON() {
        System.out.println("\n--- Task 3: JSON Processing ---\n");
        try {
            BufferedReader reader = new BufferedReader(new FileReader("users.json"));
            PrintWriter errorWriter = new PrintWriter("json_errors.log");

            JsonElement jsonElement = JsonParser.parseReader(reader);
            JsonArray users = jsonElement.getAsJsonArray();

            System.out.printf("%-5s %-20s %-30s%n", "ID", "Name", "Email");
            System.out.println("-----------------------------------------------------------");

            for (JsonElement userElement : users) {
                try {
                    JsonObject user = userElement.getAsJsonObject();

                    int id = user.get("id").getAsInt();
                    String name = user.get("name").getAsString();
                    String email = user.get("email").getAsString();
                    boolean active = user.get("active").getAsBoolean();

                    if (active) {
                        if (!email.matches("^[\\w.-]+@[\\w.-]+\\.\\w{2,}$")) {
                            throw new InvalidEmailFormatException(email);
                        }

                        System.out.printf("%-5d %-20s %-30s%n", id, name, email);
                    }

                } catch (InvalidEmailFormatException e) {
                    errorWriter.println("Invalid email: " + e.getMessage());
                }
            }

            errorWriter.close();
            reader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        processHTML();
        processXML();
        processJSON();
    }
}
