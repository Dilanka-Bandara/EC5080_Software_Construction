import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

class SaleEntry {
    private String productId;
    private int quantity;
    private double pricePerUnit;

    // I create this constructor for initialize the object
    public SaleEntry(String productId, int quantity, double pricePerUnit) {
        this.productId = productId;
        this.quantity = quantity;
        this.pricePerUnit = pricePerUnit;
    }
    // get method to get the revenue value
    public double getRevenue() {
        return quantity * pricePerUnit;
    }
    //this also get method
    public String getProductId() {
        return productId;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof SaleEntry)) return false;
        SaleEntry other = (SaleEntry) obj;
        return Objects.equals(productId, other.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId);
    }
}

class Product implements Comparable<Product> {
    private String productId;
    private String name;
    private Set<SaleEntry> sales;

    public Product(String productId, String name) {
        this.productId = productId;
        this.name = name;
        this.sales = new HashSet<>();
    }
    //This is a get method I an going to get product id
    public String getProductId() {
        return productId;
    }
    //this also get method
    public String getName() {
        return name;
    }
    // this is a add method
    public void addSale(SaleEntry entry) {
        sales.add(entry); // add method to enter data to entry
    }
    //this also get method
    public double getTotalRevenue() {
        return sales.stream().mapToDouble(SaleEntry::getRevenue).sum();
    }

    public boolean isActive() {
        return !sales.isEmpty() && getTotalRevenue() >= 100;
    }

    @Override
    public int compareTo(Product other) {
        return Double.compare(other.getTotalRevenue(), this.getTotalRevenue());
    }

    @Override
    public String toString() {
        return "Product[ID=" + productId + ", Name=" + name + ", Revenue=" + getTotalRevenue() + "]";
    }
}

public class Main {
    public static void main(String[] args) {
        // Task 001
        System.out.println("----- Task 1: Product and Sales ---------");
        List<Product> productList = new ArrayList<>();

        //I add some items into saleEntry class

        Product p1 = new Product("P001", "Laptop");
        p1.addSale(new SaleEntry("P001", 2, 1000));
        p1.addSale(new SaleEntry("P001", 1, 1200));
        productList.add(p1);

        Product p2 = new Product("P002", "Mouse");
        p2.addSale(new SaleEntry("P002", 10, 25));
        p2.addSale(new SaleEntry("P002", 5, 30));
        productList.add(p2);

        Product p3 = new Product("P003", "KeyBoard");
        p3.addSale(new SaleEntry("P003", 3, 50));
        productList.add(p3);

        Product p4 = new Product("P004", "Monitar");
        p4.addSale(new SaleEntry("P004", 1, 150));
        p4.addSale(new SaleEntry("P004", 1, 200));
        productList.add(p4);

        Product p5 = new Product("P005", "Webcam");
        p5.addSale(new SaleEntry("P005", 4, 80));
        productList.add(p5);

        //I am going to use Enhanced for each loop for print the list.
        for (Product p : productList) {
            System.out.println(p);
        }

        // Task 02
        System.out.println("------ Task 2: Ranking Products  by Revenue ------");
        TreeSet<Product> sortedProducts = new TreeSet<>(productList);
        for (Product p : sortedProducts) {
            System.out.println(p);
        }

        HashMap<String, Product> productMap = new HashMap<>();
        for (Product p : productList) {
            productMap.put(p.getProductId(), p);
        }

        System.out.println("\nTop 3 revenue-generating products:");
        sortedProducts.stream().limit(3).forEach(p ->
                System.out.println("Name: " + p.getName() + ", ID: " + p.getProductId() + ", Revenue: " + p.getTotalRevenue()));

        // Task 003
        System.out.println("\n-------- Task 3: Removing Inactive Products --------");

        // Using Iterator create a iterator
        Iterator<Product> iter = productList.iterator();
        while (iter.hasNext()) {
            if (!iter.next().isActive()) {
                iter.remove();
            }
        }
        System.out.println("After removing inactive (Iterator): " + productList.size());

        // Re-adding one inactive to show ListIterator
        Product inactive = new Product("P006", "Empty");
        productList.add(inactive);

        ListIterator<Product> listIter = productList.listIterator();
        while (listIter.hasNext()) {
            if (!listIter.next().isActive()) {
                listIter.remove();
            }
        }
        System.out.println("After removing inactive (ListIterator): " + productList.size());

        // Try with for-each
        try {
            for (Product p : productList) {
                if (!p.isActive()) {
                    productList.remove(p); // I found this throws ConcurrentModificationException
                }
            }
        } catch (ConcurrentModificationException e) {
            System.out.println("Cannot modify collection during for-each loop.");
        }

        System.out.println("Explanation:");
        System.out.println("- for-each loop uses an internal iterator that throws an exception if modified.");
        System.out.println("- Iterator and ListIterator provide safe removal mechanisms.");

        // Task 4
        System.out.println("\n---- Task 4: Performance Comparison ----");
        List<Product> arrayList = new ArrayList<>();
        HashSet<String> hashSet = new HashSet<>();
        TreeMap<String, List<SaleEntry>> treeMap = new TreeMap<>();

        //to simulate 10000 products
        int n = 10000;
        //to store starttime and end time
        long startTime, endTime;

        // Insert
        startTime = System.nanoTime();
        for (int i = 0; i < n; i++) {
            String id = "PX" + i;
            Product prod = new Product(id, "Product" + i);
            for (int j = 0; j < 3; j++) {
                prod.addSale(new SaleEntry(id, ThreadLocalRandom.current().nextInt(1, 10),
                        ThreadLocalRandom.current().nextDouble(10, 100)));
            }
            arrayList.add(prod);
        }
        endTime = System.nanoTime();
        //calculate the time for insert time
        long arrayInsert = endTime - startTime;

        startTime = System.nanoTime();
        for (Product p : arrayList) {
            hashSet.add(p.getProductId());
        }
        endTime = System.nanoTime();
        //calculate the inserting time
        long hashInsert = endTime - startTime;

        //startTime = System.nanoTime();
        //for (Product p : arrayList) {
         //   treeMap.put(p.getName(), new ArrayList<>(p.sales));
       // }
        endTime = System.nanoTime();
        long treeInsert = endTime - startTime;

        // Search
        List<String> searchIds = arrayList.subList(0, 100).stream().map(Product::getProductId).toList();

        startTime = System.nanoTime();
        for (String id : searchIds) {
            for (Product p : arrayList) {
                if (p.getProductId().equals(id)) break;
            }
        }
        endTime = System.nanoTime();
        long arraySearch = endTime - startTime;

        startTime = System.nanoTime();
        for (String id : searchIds) {
            hashSet.contains(id);
        }
        endTime = System.nanoTime();
        long hashSearch = endTime - startTime;

        startTime = System.nanoTime();
        for (String name : treeMap.keySet().stream().limit(100).toList()) {
            treeMap.get(name);
        }
        endTime = System.nanoTime();
        long treeSearch = endTime - startTime;

        // Remove
        startTime = System.nanoTime();
        arrayList.removeIf(p -> searchIds.contains(p.getProductId()));
        endTime = System.nanoTime();
        long arrayRemove = endTime - startTime;

        startTime = System.nanoTime();
        hashSet.removeAll(searchIds);
        endTime = System.nanoTime();
        long hashRemove = endTime - startTime;

        startTime = System.nanoTime();
        for (String name : treeMap.keySet().stream().limit(100).toList()) {
            treeMap.remove(name);
        }
        endTime = System.nanoTime();
        long treeRemove = endTime - startTime;

        System.out.printf("\n%-10s %-15s %-15s %-15s\n", "Collection", "Insert Time(ns)", "Search Time(ns)", "Remove Time(ns)");
        System.out.printf("%-10s %-15d %-15d %-15d\n", "ArrayList", arrayInsert, arraySearch, arrayRemove);
        System.out.printf("%-10s %-15d %-15d %-15d\n", "HashSet", hashInsert, hashSearch, hashRemove);
        System.out.printf("%-10s %-15d %-15d %-15d\n", "TreeMap", treeInsert, treeSearch, treeRemove);

        // Task 5


    }
}