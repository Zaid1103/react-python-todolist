import java.time.LocalDate;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class PayPalPayment extends Payment {
    String email;

    // Constructor
    public PayPalPayment(double amount, LocalDate datePayed, int houseNumber, String postcode, String city, String email) {
        this.amount = amount;
        this.datePayed = datePayed;
        this.houseNumber = houseNumber;
        this.postcode = postcode;
        this.city = city;
        this.email = email;
    }
    
    public void printReceipt(ShoppingBasket basket) {
        // Load product prices and names from the stock file
        HashMap<String, Double> prices = new HashMap<>();
        HashMap<String, String> productNames = new HashMap<>();
        try {
            File stockFile = new File("Stock.txt");
            Scanner scanner = new Scanner(stockFile);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] details = line.split(",");
                if (details.length > 8) {
                    String barcode = details[0].trim();
                    double price = Double.parseDouble(details[8].trim());  // Assuming price is the 9th value
                    productNames.put(barcode, details[1].trim());  // Assuming product name is the 2nd value
                    prices.put(barcode, price);
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error: Stock file not found.");
            return;
        }

        System.out.println("Receipt for " + basket.basketOwner);
        System.out.println("Date: " + datePayed);
        System.out.println("House Number: " + houseNumber);
        System.out.println("Postcode: " + postcode);
        System.out.println("City: " + city);
        System.out.println("Email: " + email);

        double total = 0.0;
        HashMap<String, Integer> productCounts = new HashMap<>();
        for (String barcode : basket.productList) {
            productCounts.put(barcode, productCounts.getOrDefault(barcode, 0) + 1);
        }

        for (Map.Entry<String, Integer> entry : productCounts.entrySet()) {
            String barcode = entry.getKey();
            int quantity = entry.getValue();
            double price = prices.getOrDefault(barcode, 0.0);
            double cost = price * quantity;
            System.out.println("Product: " + productNames.get(barcode) + " (" + barcode + "), Quantity: " + quantity + ", Unit Price: $" + price + ", Cost: $" + cost);
            total += cost;
        }

        System.out.println("Total Amount: $" + total);
    }
}
