import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ShoppingBasket {
    ArrayList<String> productList;
    String basketOwner;

    // Constructor
    public ShoppingBasket(String owner) {
        this.basketOwner = owner;
        this.productList = new ArrayList<>();
    }

    // Method to add a product to the basket by its barcode
    public void addToBasket(int barcode) {
        try {
            File stockFile = new File("Stock.txt");
            @SuppressWarnings("resource")
			Scanner scanner = new Scanner(stockFile);
            boolean found = false;

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] details = line.split(",");
                int stockBarcode = Integer.parseInt(details[0].trim());
                int stockQuantity = Integer.parseInt(details[6].trim());

                if (stockBarcode == barcode) {
                    // Count the current quantity in the basket for this barcode
                    int currentQuantity = 0;
                    for (String product : productList) {
                        if (product.equals(details[0].trim())) {
                            currentQuantity++;
                        }
                    }

                    if (currentQuantity >= stockQuantity) {
                        System.out.println("Error: No more stock of product with barcode " + barcode + " can be ordered.");
                        return; // Prevent adding the product if stock limit is reached
                    }

                    productList.add(details[0]); // Add barcode to productList
                    found = true;
                }
            }
            scanner.close();

            if (!found) {
                System.out.println("Product with barcode " + barcode + " not found in stock.");
            } else {
                System.out.println("Product with barcode " + barcode + " added to basket.");
            }
        } catch (FileNotFoundException e) {
            System.out.println("Stock file not found.");
        }
    }

    // Method to display the products in the basket with counts
    public void displayBasket() {
        System.out.println("Products in " + basketOwner + "'s basket:");
        if (productList.isEmpty()) {
            System.out.println("No items");
        } else {
            HashMap<String, Double> prices = new HashMap<>();
            HashMap<String, String> productNames = new HashMap<>();
            loadProductDetails(prices, productNames);

            // Counting occurrences of each barcode in productList
            HashMap<String, Integer> productCounts = new HashMap<>();
            for (String barcode : productList) {
                productCounts.put(barcode, productCounts.getOrDefault(barcode, 0) + 1);
            }

            double total = 0.0;
            for (Map.Entry<String, Integer> entry : productCounts.entrySet()) {
                String barcode = entry.getKey();
                String productName = productNames.getOrDefault(barcode, "Unknown Product");
                int quantity = entry.getValue();
                double unitPrice = prices.getOrDefault(barcode, 0.0);
                double cost = unitPrice * quantity;
                System.out.println(productName + " (" + barcode + ") x" + quantity + " - Unit Price: $" + unitPrice + ", Cost: $" + cost);
                total += cost;
            }
            System.out.println("Total Cost: $" + total);
        }
    }

    private void loadProductDetails(HashMap<String, Double> prices, HashMap<String, String> productNames) {
        try {
            File stockFile = new File("Stock.txt");
            Scanner scanner = new Scanner(stockFile);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] details = line.split(",");
                if (details.length > 8) {
                    String barcode = details[0].trim();
                    double price = Double.parseDouble(details[8].trim());
                    productNames.put(barcode, details[1].trim());
                    prices.put(barcode, price);
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error: Stock file not found.");
        }
    }

    // Method to clear all items from the basket
    public void clearBasket() {
        if (productList.isEmpty()) {
            System.out.println("Basket is already empty.");
        } else {
            productList.clear();
            System.out.println("Basket has been cleared.");
        }
    }
    
    public double calculateTotal() {
        try {
            File stockFile = new File("Stock.txt");
            Scanner scanner = new Scanner(stockFile);
            HashMap<Integer, Double> prices = new HashMap<>();
            
            // Read the stock file and store prices of products by barcode
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] details = line.split(",");
                int barcode = Integer.parseInt(details[0].trim());
                double price = Double.parseDouble(details[8].trim());  // Assuming price is the 9th element
                prices.put(barcode, price);
            }
            scanner.close();

            // Calculate total price of products in the basket
            double total = 0.0;
            HashMap<Integer, Integer> count = new HashMap<>();
            
            // Count the occurrences of each barcode in the productList
            for (String barcode : productList) {
                int bcode = Integer.parseInt(barcode.trim());
                count.put(bcode, count.getOrDefault(bcode, 0) + 1);
            }
            
            // Compute the total cost by multiplying the count of each product by its price
            for (Map.Entry<Integer, Integer> entry : count.entrySet()) {
                int barcode = entry.getKey();
                int quantity = entry.getValue();
                if (prices.containsKey(barcode)) {
                    total += prices.get(barcode) * quantity;
                }
            }

            return total;
        } catch (FileNotFoundException e) {
            System.out.println("Stock file not found.");
            return 0;
        } catch (NumberFormatException e) {
            System.out.println("Error parsing number from file.");
            return 0;
        }
    }
    
    public void basketPayed(ShoppingBasket basket) {
        File stockFile = new File("Stock.txt");
        File tempFile = new File("Stock_temp.txt");
        HashMap<String, Integer> productCount = new HashMap<>();

        for (String barcode : basket.productList) {
            productCount.put(barcode, productCount.getOrDefault(barcode, 0) + 1);
        }

        try {
            Scanner scanner = new Scanner(stockFile);
            PrintWriter writer = new PrintWriter(tempFile);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] details = line.split(",");
                if (productCount.containsKey(details[0].trim())) {
                    int currentStock = Integer.parseInt(details[6].trim());
                    int soldQuantity = productCount.get(details[0].trim());
                    currentStock -= soldQuantity;  // Subtract the quantity bought
                    details[6] = " " + String.valueOf(currentStock);
                }
                writer.println(String.join(",", details));  // Write updated or original line
            }

            scanner.close();
            writer.close();

            // Replace old stock file with updated one
            if (!stockFile.delete() || !tempFile.renameTo(stockFile)) {
                System.out.println("Error updating stock file.");
            }

            productList.clear();  // Clear the basket after updating stock
        } catch (FileNotFoundException e) {
            System.out.println("Stock file not found.");
        }
    }


}