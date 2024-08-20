import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Customer extends User{

    // Updated constructor to initialise a shopping basket along with customer details
	public Customer(int userID, String username, String name, int houseNumber, String postcode, String city, String role) {
        // Initialize User properties directly
        this.userID = userID;
        this.username = username;
        this.name = name;
        this.houseNumber = houseNumber;
        this.postcode = postcode;
        this.city = city;
        this.role = role;

    }
	
	public void getPeripheralInfo(String barcode) {
        String filePath = "Stock.txt"; // Update this to the actual path of Stock.txt on your system
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean found = false;
            while ((line = br.readLine()) != null) {
                String[] details = line.split(", ");
                if (details.length > 0 && details[0].equals(barcode)) {
                    found = true;
                    String labelForTenthValue = details[1].equalsIgnoreCase("mouse") ? "Number of buttons: " + details[9]
                                                                                    : "Layout: " + details[9];
                    System.out.println("ID: " + details[0] +
                                       ", Type: " + details[1] +
                                       ", Category: " + details[2] +
                                       ", Brand: " + details[3] +
                                       ", Color: " + details[4] +
                                       ", Connection: " + details[5] +
                                       ", Stock: " + details[6] +
                                       ", Price: " + details[8] +
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
	
	
	public void mouseButtonFilter(int buttons) {
        String path = "Stock.txt"; // Replace with the actual file path
        boolean found = false;  // Flag to check if any mouse was found
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] details = line.split(", ");
                if (details[1].trim().equals("mouse") && Integer.parseInt(details[9].trim()) == buttons) {
                    found = true;  // Set found to true if a mouse is found
                    System.out.println("ID: " + details[0] + ", Type: " + details[1] + ", Category: " + details[2] + 
                                       ", Brand: " + details[3] + ", Color: " + details[4] + ", Connection: " + details[5] + 
                                       ", Stock: " + details[6] + ", Price: " + details[8] + ", Number of buttons: " + details[9]);
                }
            }
            if (!found) {
                System.out.println("No mouse found with " + buttons + " buttons.");
            }
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
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
	                               ", Price: " + details[8] + 
	                               ", " + labelForTenthValue);
	        }
	    }
    
}
