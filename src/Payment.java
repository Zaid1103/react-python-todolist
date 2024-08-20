import java.time.LocalDate;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Scanner;

public class Payment {
    double amount;
    LocalDate datePayed;
    int houseNumber;
    String postcode;
    String city;

    // Method to process payment and update stock
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

            basket.clearBasket();  // Clear the basket after updating stock
            System.out.println("Payment processed and basket cleared.");
        } catch (FileNotFoundException e) {
            System.out.println("Stock file not found.");
        }
    }
}
