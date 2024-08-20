import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class Admin extends User{
	
	public Admin(int userID, String username, String name, int houseNumber, String postcode, String city, String role) {
        // Directly initialise User properties
        this.userID = userID;
        this.username = username;
        this.name = name;
        this.houseNumber = houseNumber;
        this.postcode = postcode;
        this.city = city;
        this.role = role;

        // Admin-specific initialisation can be added here
    }
	
	public void getPeripheralInfo(String barcode) {
        String filePath = "Stock.txt"; // Update this to the actual path of Stock.txt on your system
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean found = false;
            while ((line = br.readLine()) != null) {
                String[] details = line.split(",");  // Ensure no space in split if not in original data format
                if (details.length > 0 && details[0].equals(barcode)) {
                    found = true;
                    // Determine the label for the tenth value based on the type of peripheral
                    String labelForTenthValue = details[1].equalsIgnoreCase("mouse") ? "Number of buttons: " + details[9]
                                                                                    : "Layout: " + details[9];
                    // Print the formatted string including original and retail prices
                    System.out.println("ID: " + details[0] +
                                       ", Type: " + details[1] +
                                       ", Category: " + details[2] +
                                       ", Brand: " + details[3] +
                                       ", Color: " + details[4] +
                                       ", Connection: " + details[5] +
                                       ", Stock: " + details[6] +
                                       ", Original Price: $" + details[7] +
                                       ", Retail Price: $" + details[8] +
                                       ", " + labelForTenthValue);
                }
            }
            if (!found) {
                System.out.println("No details found for barcode: " + barcode);
            }
        } catch (IOException e) {
            System.err.println("An error occurred while reading the file: " + e.getMessage());
        }
    }

	public void addPeripheral(int barcode, String productCategory, String deviceType, String brand, String colour, String connectivity,int quantityStocked, double originalCost, double retailPrice, String extra) {
	    String filePath = "Stock.txt"; // Update this to the actual path of Stock.txt on your system
	    String data = String.format("%d, %s, %s, %s, %s, %s, %d, %.2f, %.2f, %s%n", barcode, productCategory, deviceType, brand, colour, connectivity, quantityStocked, originalCost, retailPrice, extra);

	    try {
	        File file = new File(filePath);
	        Scanner scanner = new Scanner(file);

	        // Check for existing barcode
	        while (scanner.hasNextLine()) {
	            String line = scanner.nextLine();
	            if (line.startsWith(String.valueOf(barcode) + ",")) {
	                System.out.println("Error: A peripheral with barcode " + barcode + " already exists in the stock file.");
	                scanner.close();
	                return; // Exit if a matching barcode is found
	            }
	        }
	        scanner.close();

	        // Append new entry to the file
	        try (FileWriter fw = new FileWriter(file, true)) {
	            fw.write(data);
	            System.out.println("Peripheral added successfully.");
	        } catch (IOException e) {
	            System.err.println("An error occurred while writing to the file: " + e.getMessage());
	        }
	    } catch (IOException e) {
	        System.err.println("An error occurred while reading the file: " + e.getMessage());
	    }
	}

	
	public void changeStockQuantity(int barcode, int newQuantity) {
        String filePath = "Stock.txt"; // Update to the actual path
        List<String> fileContent = new ArrayList<>();
        boolean found = false;

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 6 && Integer.parseInt(parts[0]) == barcode) {
                    parts[6] = " " + String.valueOf(newQuantity); // Update the quantity
                    line = String.join(",", parts);
                    found = true;
                }
                fileContent.add(line);
            }
        } catch (IOException e) {
            System.err.println("An error occurred while reading the file: " + e.getMessage());
            return;
        }

        if (!found) {
            System.out.println("Item with barcode " + barcode + " not found.");
            return;
        }

        try (FileWriter fw = new FileWriter(filePath, false)) { // False to overwrite the file
            for (String line : fileContent) {
                fw.write(line + System.lineSeparator());
            }
            System.out.println("Stock quantity updated successfully.");
        } catch (IOException e) {
            System.err.println("An error occurred while writing to the file: " + e.getMessage());
        }
    }
    
    public void removeStockItem(int barcode) {
        String filePath = "Stock.txt"; // Update to the actual path
        List<String> fileContent = new ArrayList<>();
        boolean found = false;

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 0 && Integer.parseInt(parts[0]) == barcode) {
                    // This is the line to remove, so skip adding it to fileContent
                    found = true;
                } else {
                    fileContent.add(line); // Add other lines as they are
                }
            }
        } catch (IOException e) {
            System.err.println("An error occurred while reading the file: " + e.getMessage());
            return;
        }

        if (!found) {
            System.out.println("Item with barcode " + barcode + " not found.");
            return;
        }

        try (FileWriter fw = new FileWriter(filePath, false)) { // False to overwrite the file
            for (String line : fileContent) {
                fw.write(line + System.lineSeparator());
            }
            System.out.println("Item removed successfully.");
        } catch (IOException e) {
            System.err.println("An error occurred while writing to the file: " + e.getMessage());
        }
    }
    
    public void displayStock() {
        String filePath = "Stock.txt";
        ArrayList<String[]> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] details = line.split(", ");
                records.add(details);
            }
        } catch (IOException e) {
            System.err.println("An error occurred while reading the file: " + e.getMessage());
            return;
        }

        // Sorting records by price, which is at index 8
        Collections.sort(records, Comparator.comparingDouble(a -> Double.parseDouble(a[8])));

        // Displaying sorted records
        for (String[] details : records) {
            String labelForTenthValue = details[1].equalsIgnoreCase("mouse") ? "Number of buttons: " + details[9]
                                                                            : "Layout: " + details[9];
            System.out.println("ID: " + details[0] + 
                               ", Type: " + details[1] + 
                               ", Category: " + details[2] + 
                               ", Brand: " + details[3] + 
                               ", Color: " + details[4] + 
                               ", Connection: " + details[5] + 
                               ", Stock: " + details[6] + 
                               ", Original Price: $" + details[7] + 
                               ", Retail Price: $" + details[8] + 
                               ", " + labelForTenthValue);
        }
    }

}
