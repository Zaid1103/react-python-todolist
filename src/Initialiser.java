import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Initialiser {
	private static ShoppingBasket basket;
	private static Customer customer;
	private static Admin admin;
	private static String cardNumber;
	private static String securityCode;
	private static String email;
	
	private static int barcode;
	private static String productCategory;  
	private static String deviceType;
	private static String brand; 
	private static String colour;
	private static String connectivity;
	private static int quantityStocked;
	private static double originalCost;
	private static double retailPrice;
	private static String extra;

    public void listUserDetails() {
        String filePath = "UserAccounts.txt"; // Ensure this path is correctly pointed to your UserAccounts.txt file
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            System.out.println("User Details:");
            while ((line = br.readLine()) != null) {
                String[] userDetails = line.split(", ");
                // Print only selected details, excluding house number, postcode, and city
                System.out.println("User ID: " + userDetails[0] +
                                   ", Username: " + userDetails[1] +
                                   ", Name: " + userDetails[2] +
                                   ", Role: " + userDetails[6]);
            }
        } catch (IOException e) {
            System.err.println("An error occurred while reading the file: " + e.getMessage());
        }
    }

    public static void createUserObject(String userID) {
        String filePath = "UserAccounts.txt";
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] userDetails = line.split(", ");
                if (userDetails[0].equals(userID)) {
                    if (userDetails[6].trim().equals("customer")) {
                        customer = new Customer(Integer.parseInt(userDetails[0].trim()), userDetails[1].trim(), userDetails[2].trim(),
                                            Integer.parseInt(userDetails[3].trim()), userDetails[4].trim(), userDetails[5].trim(), userDetails[6].trim());
                    } else if (userDetails[6].trim().equals("admin")) {
                        admin = new Admin(Integer.parseInt(userDetails[0].trim()), userDetails[1].trim(), userDetails[2].trim(),
                                         Integer.parseInt(userDetails[3].trim()), userDetails[4].trim(), userDetails[5].trim(), userDetails[6].trim());
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("An error occurred while reading the file: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Initialiser init = new Initialiser();
        init.listUserDetails();
        customer = null;
        admin = null;
        Scanner scanner = new Scanner(System.in);

        while (customer == null && admin == null) {
            System.out.print("Please enter the User ID to select an account: ");
            String userId = scanner.nextLine();
            createUserObject(userId);
            if (customer == null && admin == null) {
                System.out.println("Invalid User ID, please try again");
            }
        }

        if (customer != null) {
            System.out.println("Selected User: " + customer.name + " (" + customer.role + ")");
            // Optional: display information or interact with the shopping basket
            basket = new ShoppingBasket(customer.name);
            userMainMenu();
        } else {
            System.out.println("Selected User: " + admin.name + " (" + admin.role + ")");
            adminMainMenu();
        }

        scanner.close();
        
    }
    
    public static void userMainMenu() {
        Scanner scanner = new Scanner(System.in);
        int userInput = 0;

        while (true) {
        	System.out.println("");
            System.out.println("Choose an option (input the number):");
            System.out.println("1 - Search for items");
            System.out.println("2 - View basket");
            System.out.println("3 - Log out");
            
            // Check if the next input is an integer
            if (scanner.hasNextInt()) {
                userInput = scanner.nextInt();
                
                // Check if the input is within the valid range
                if (userInput == 1) {
                    userSearchMenu();  // Exit the loop if valid input is entered
                    break;
                } else if (userInput == 2) {
                	userBasketMenu();
                	break;
                } else if (userInput == 3) {
                	System.out.println("Logging off");
                	System.exit(0);
                } else {
                    System.out.println("Invalid input, please choose a number between 1 and 3");
                }
            } else {
                System.out.println("Invalid input, please choose a number between 1 and 3");
                scanner.next();  // Consume the invalid input
            }
        }
        
        scanner.close();
    }
    
    public static void userSearchMenu() {
    	Scanner scanner = new Scanner(System.in);
        int userInput = 0;
    	
        while (true) {
        	System.out.println("");
	    	System.out.println("Choose an option:");
	    	System.out.println("1 - View all products");
	    	System.out.println("2 - Barcode search");
	    	System.out.println("3 - Mouse button filter");
	    	System.out.println("4 - Add product to basket");
	    	System.out.println("5 - Back");
	    	
	    	if (scanner.hasNextInt()) {
	            userInput = scanner.nextInt();
	            
	            // Check if the input is within the valid range
	            if (userInput == 1) {
	            	customer.displayStock();
	            	postDisplayMenu();
	                break;
	            } else if (userInput == 2) {
	            	barcodeSearchMenu();
	            	break;
	            } else if (userInput == 3) {
	            	buttonFilterMenu();
	            	break;
	            } else if (userInput == 4) {
	            	addProductMenu();  // Exit the loop if valid input is entered
	                break;
	            } else if (userInput == 5) {
	            	userMainMenu();  // Exit the loop if valid input is entered
	                break; 
	            } else {
	                System.out.println("Invalid input, please choose a number between 1 and 5");
	            }
	        } else {
	            System.out.println("Invalid input, please choose a number between 1 and 5");
	            scanner.next();  // Consume the invalid input
	        }
    }
    
    scanner.close();
    }
    
    public static void postDisplayMenu() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println();
            System.out.println("Input 0 to go back to the search menu");

            if (scanner.hasNextInt()) {
                int userInput = scanner.nextInt();
                if (userInput == 0) {
                	userSearchMenu();  // Assuming this method exists and handles going back to the main menu
                    break;
                }
            } else {
                scanner.next();  // Clear the non-integer input from the scanner buffer
            }
        }
        scanner.close();
    }
    
    public static void barcodeSearchMenu() {
        Scanner scanner = new Scanner(System.in);
        int userInput = 0;

        while (true) {
        	System.out.println("");
            System.out.println("Input the barcode of the product you want the details of (or input '0' to go back):");
            if (scanner.hasNextInt()) {
	            userInput = scanner.nextInt();
	            if (userInput == 0) {
	            	userSearchMenu();
	                break;
	            } else {
	            	String barcodeString = String.valueOf(userInput);
	            	customer.getPeripheralInfo(barcodeString);
	            }
	            
            }
        
        }
        scanner.close();
    }
    
    public static void buttonFilterMenu() {
        Scanner scanner = new Scanner(System.in);
        int userInput = 0;

        while (true) {
        	System.out.println("");
            System.out.println("Input the number of buttons you want on your mouse (or input '0' to go back):");
            if (scanner.hasNextInt()) {
	            userInput = scanner.nextInt();
	            if (userInput == 0) {
	            	userSearchMenu();
	                break;
	            } else {
	            	customer.mouseButtonFilter(userInput);
	            }
	            
            }
        
        }
        scanner.close();
    }
    
    public static void addProductMenu() {
        Scanner scanner = new Scanner(System.in);
        int userInput = 0;

        while (true) {
        	System.out.println("");
            System.out.println("Input the barcode of the product you want to add to basket (or input '0' to go back):");
            if (scanner.hasNextInt()) {
	            userInput = scanner.nextInt();
	            if (userInput == 0) {
	            	userSearchMenu();
	                break;
	            } else {
	            	basket.addToBasket(userInput);
	            }
	            
            }
        
        }
        scanner.close();
    }
    
    public static void userBasketMenu() {
    	Scanner scanner = new Scanner(System.in);
        int userInput = 0;
    	
        while (true) {
        	System.out.println("");
        	basket.displayBasket();
        	System.out.println("");
	    	System.out.println("Choose what you want to do with the basket:");
	    	System.out.println("1 - Clear Basket");
	    	System.out.println("2 - Proceed to payment");
	    	System.out.println("3 - Back");
	    	
	    	if (scanner.hasNextInt()) {
	            userInput = scanner.nextInt();
	            
	            // Check if the input is within the valid range
	            if (userInput == 1) {
	            	basket.clearBasket();
	            } else if (userInput == 2) {
	            	if (basket.productList.isEmpty()) {
	            		System.out.println("Error, basket empty");
	            	} else {
	            		proceedToPayment();
	            		break;
	            	}
	            } else if (userInput == 3) {
	            	userMainMenu();  // Exit the loop if valid input is entered
	                break;
	            } else {
	                System.out.println("Invalid input, please choose a number between 1 and 3");
	            }
	        } else {
	            System.out.println("Invalid input, please choose a number between 1 and 3");
	            scanner.next();  // Consume the invalid input
	        }
    }
    
    scanner.close();
    }
    
    public static void proceedToPayment() {
    	Scanner scanner = new Scanner(System.in);
        int userInput = 0;
    	
        while (true) {
        	System.out.println("");
	    	System.out.println("Choose a type of payment:");
	    	System.out.println("1 - Credit card payment");
	    	System.out.println("2 - PayPal payment");
	    	System.out.println("3 - Back");
	    	
	    	if (scanner.hasNextInt()) {
	            userInput = scanner.nextInt();
	            
	            // Check if the input is within the valid range
	            if (userInput == 1) {
	            	cardNumberMenu();
	            	break;
	            } else if (userInput == 2) {
	            	emailMenu();
	            	break;
	            } else if (userInput == 3) {
	            	userBasketMenu();  // Exit the loop if valid input is entered
	                break;
	            } else {
	                System.out.println("Invalid input, please choose a number between 1 and 3");
	            }
	        } else {
	            System.out.println("Invalid input, please choose a number between 1 and 3");
	            scanner.next();  // Consume the invalid input
	        }
    }
    
    scanner.close();
    }
    
    public static void cardNumberMenu() {
        Scanner scanner = new Scanner(System.in);
        String userInput;

        while (true) {
            System.out.println();
            System.out.println("Input 6-digit card number (or 0 to go back):");

            userInput = scanner.nextLine(); // Read the input as a string to preserve leading zeros

            if (userInput.equals("0")) {
                proceedToPayment();
                break;
            } else if (userInput.matches("\\d{6}")) { // Check if the input is exactly 6 digits
            	cardNumber = userInput;
            	securityCodeMenu();
                break;
            } else {
                System.out.println("Invalid input. Please enter a 6-digit card number.");
            }
        }
        scanner.close(); // Be careful with closing scanners that wrap System.in if you need to use them elsewhere.
    }
    
    public static void securityCodeMenu() {
    	Scanner scanner = new Scanner(System.in);
        String userInput;

        while (true) {
            System.out.println();
            System.out.println("Input 3-digit security code (or 0 to go back):");

            userInput = scanner.nextLine(); // Read the input as a string to preserve leading zeros

            if (userInput.equals("0")) {
                proceedToPayment();
                break;
            } else if (userInput.matches("\\d{3}")) { // Check if the input is exactly 3 digits
            	securityCode = userInput;
            	LocalDate today = LocalDate.now();
            	CreditCardPayment payment = new CreditCardPayment(basket.calculateTotal(), today, customer.houseNumber, customer.postcode, customer.city,cardNumber, securityCode);
            	payment.printReceipt(basket);
            	basket.basketPayed(basket);
            	postPaymentMenu();
                break;
            } else {
                System.out.println("Invalid input. Please enter a 6-digit card number.");
            }
        }
        scanner.close();
    }

    public static boolean isValidEmail(String email) {
    	String EMAIL_PATTERN = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
    	
        if (email == null) {
            return false;
        }
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    
    public static void emailMenu() {
    	Scanner scanner = new Scanner(System.in);
        String userInput;

        while (true) {
            System.out.println();
            System.out.println("Input email (or 0 to go back):");

            userInput = scanner.nextLine(); // Read the input as a string to preserve leading zeros

            if (userInput.equals("0")) {
                proceedToPayment();
                break;
            } else if (isValidEmail(userInput)) { // Check if the input is exactly 6 digits
            	email = userInput;
            	LocalDate today = LocalDate.now();
            	PayPalPayment payment = new PayPalPayment(basket.calculateTotal(), today, customer.houseNumber, customer.postcode, customer.city, email);
            	payment.printReceipt(basket);
            	basket.basketPayed(basket);
            	postPaymentMenu();
            	break;
            } else {
                System.out.println("Invalid input. Please enter a valid email.");
            }
        }
        scanner.close(); 
    }
    
    public static void postPaymentMenu() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println();
            System.out.println("Input 0 to go back to the main menu");

            if (scanner.hasNextInt()) {
                int userInput = scanner.nextInt();
                if (userInput == 0) {
                    userMainMenu();  // Assuming this method exists and handles going back to the main menu
                    break;
                }
            } else {
                scanner.next();  // Clear the non-integer input from the scanner buffer
            }
        }
        scanner.close();
    }
    
    
    public static void adminMainMenu() {
        Scanner scanner = new Scanner(System.in);
        int userInput = 0;

        while (true) {
        	System.out.println("");
            System.out.println("Choose an option (input the number):");
            System.out.println("1 - Display stock");
            System.out.println("2 - Add new product");
            System.out.println("3 - Log out");
            
            // Check if the next input is an integer
            if (scanner.hasNextInt()) {
                userInput = scanner.nextInt();
                
                // Check if the input is within the valid range
                if (userInput == 1) {
                    admin.displayStock();  // Exit the loop if valid input is entered
                    postStockMenu();
                    break;
                } else if (userInput == 2) {
                	addBarcodeMenu();
                	break;
                } else if (userInput == 3) {
                	System.out.println("Logging off");
                	System.exit(0);
                } else {
                    System.out.println("Invalid input, please choose a number between 1 and 3");
                }
            } else {
                System.out.println("Invalid input, please choose a number between 1 and 3");
                scanner.next();  // Consume the invalid input
            }
        }
        
        scanner.close();
    }
    
    public static void postStockMenu() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println();
            System.out.println("Input 0 to go back to the main menu");

            if (scanner.hasNextInt()) {
                int userInput = scanner.nextInt();
                if (userInput == 0) {
                	adminMainMenu();  // Assuming this method exists and handles going back to the main menu
                    break;
                }
            } else {
                scanner.next();  // Clear the non-integer input from the scanner buffer
            }
        }
        scanner.close();
    }
    
    private static HashSet<Integer> loadExistingBarcodes() {
        HashSet<Integer> existingBarcodes = new HashSet<>();
        try {
            File stockFile = new File("Stock.txt");
            Scanner fileScanner = new Scanner(stockFile);
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] details = line.split(",");
                int barcode = Integer.parseInt(details[0].trim()); // Convert the first value in each row to integer
                existingBarcodes.add(barcode);
            }
            fileScanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error: Stock file not found.");
        }
        return existingBarcodes;
    }
    
    public static void addBarcodeMenu() {
        Scanner scanner = new Scanner(System.in);
        String userInput;
        HashSet<Integer> existingBarcodes = loadExistingBarcodes(); // Load existing barcodes once

        while (true) {
            System.out.println();
            System.out.println("Input 6-digit barcode (or 0 to go back):");

            userInput = scanner.nextLine(); // Read the input as a string to preserve leading zeros

            if (userInput.equals("0")) {
            	adminMainMenu();
                break;
            } else if (userInput.matches("\\d{6}")) { // Check if the input is exactly 6 digits
                int userNumber = Integer.parseInt(userInput);
                if (existingBarcodes.contains(userNumber)) {
                    System.out.println("Error: Barcode already exists. Please enter a unique barcode.");
                } else {
                    barcode = userNumber;
                    addCategoryMenu();
                    break;
                }
            } else {
                System.out.println("Invalid input. Please enter a 6-digit barcode.");
            }
        }
        scanner.close(); // Be careful with closing scanners that wrap System.in
    }
    
    public static void addCategoryMenu() {
    	Scanner scanner = new Scanner(System.in);
        int userInput = 0;
    	
        while (true) {
        	System.out.println("");
	    	System.out.println("Choose a device type:");
	    	System.out.println("1 - Keyboard");
	    	System.out.println("2 - Mouse");
	    	System.out.println("3 - Back");
	    	
	    	if (scanner.hasNextInt()) {
	            userInput = scanner.nextInt();
	            
	            // Check if the input is within the valid range
	            if (userInput == 1) {
	            	productCategory = "keyboard";
	            	keyboardTypeMenu();
	            	break;
	            } else if (userInput == 2) {
	            	productCategory = "mouse";
	            	mouseTypeMenu();
	            	break;
	            } else if (userInput == 3) {
	            	addBarcodeMenu();  // Exit the loop if valid input is entered
	                break;
	            } else {
	                System.out.println("Invalid input, please choose a number between 1 and 3");
	            }
	        } else {
	            System.out.println("Invalid input, please choose a number between 1 and 3");
	            scanner.next();  // Consume the invalid input
	        }
    }
    
    scanner.close();
    }
    
    public static void keyboardTypeMenu() {
        Scanner scanner = new Scanner(System.in);
        int userInput = 0;

        while (true) {
        	System.out.println("");
            System.out.println("Choose a device type:");
            System.out.println("1 - Standard");
            System.out.println("2 - Gaming");
            System.out.println("3 - Flexible");
            System.out.println("4 - Back");
            
            // Check if the next input is an integer
            if (scanner.hasNextInt()) {
                userInput = scanner.nextInt();
                
                // Check if the input is within the valid range
                if (userInput == 1) {
                    deviceType = "standard";
                    addBrandMenu();
                    break;
                } else if (userInput == 2) {
                	deviceType = "gaming";
                	addBrandMenu();
                	break;
                } else if (userInput == 3) {
                	deviceType = "flexible";
                	addBrandMenu();
                	break;
                } else if (userInput == 4) {
                	addCategoryMenu();
                	break;
                } else {
                    System.out.println("Invalid input, please choose a number between 1 and 4");
                }
            } else {
                System.out.println("Invalid input, please choose a number between 1 and 4");
                scanner.next();  // Consume the invalid input
            }
        }
        
        scanner.close();
    }
    
    public static void mouseTypeMenu() {
        Scanner scanner = new Scanner(System.in);
        int userInput = 0;

        while (true) {
        	System.out.println("");
            System.out.println("Choose a device type:");
            System.out.println("1 - Standard");
            System.out.println("2 - Gaming");
            System.out.println("3 - Back");
            
            // Check if the next input is an integer
            if (scanner.hasNextInt()) {
                userInput = scanner.nextInt();
                
                // Check if the input is within the valid range
                if (userInput == 1) {
                    deviceType = "standard";
                    addBrandMenu();
                    break;
                } else if (userInput == 2) {
                	deviceType = "gaming";
                	addBrandMenu();
                	break;
                } else if (userInput == 3) {
                	addCategoryMenu();
                	break;
                } else {
                    System.out.println("Invalid input, please choose a number between 1 and 3");
                }
            } else {
                System.out.println("Invalid input, please choose a number between 1 and 3");
                scanner.next();  // Consume the invalid input
            }
        }
        
        scanner.close();
    }
    
    public static void addBrandMenu() {
    	Scanner scanner = new Scanner(System.in);
        String userInput;

        while (true) {
            System.out.println();
            System.out.println("Input brand (or 0 to go back):");

            userInput = scanner.nextLine(); // Read the input as a string to preserve leading zeros

            if (userInput.equals("0")) {
                if (productCategory == "keyboard") {
                	keyboardTypeMenu();
                } else if (productCategory == "mouse") {
                	mouseTypeMenu();
                }
                break;
            } else if (userInput.length() < 21) { // Check if the input is exactly 6 digits
            	brand = userInput;
            	addColourMenu();
            	break;
            } else {
                System.out.println("Input should be no more than 20 characters");
            }
        }
        scanner.close(); 
    }
    
    public static void addColourMenu() {
    	Scanner scanner = new Scanner(System.in);
        String userInput;

        while (true) {
            System.out.println();
            System.out.println("Input colour (or 0 to go back):");

            userInput = scanner.nextLine(); // Read the input as a string to preserve leading zeros

            if (userInput.equals("0")) {
                addBrandMenu();
                break;
            } else if (userInput.length() < 21) { // Check if the input is exactly 6 digits
            	colour = userInput;
            	addConnectivityMenu();
            	break;
            } else {
                System.out.println("Input should be no more than 20 characters");
            }
        }
        scanner.close(); 
    }
    
    public static void addConnectivityMenu() {
        Scanner scanner = new Scanner(System.in);
        int userInput = 0;

        while (true) {
        	System.out.println("");
            System.out.println("Choose a device type:");
            System.out.println("1 - Wired");
            System.out.println("2 - Wireless");
            System.out.println("3 - Back");
            
            // Check if the next input is an integer
            if (scanner.hasNextInt()) {
                userInput = scanner.nextInt();
                
                // Check if the input is within the valid range
                if (userInput == 1) {
                    connectivity = "wired";
                    addStockMenu();
                    break;
                } else if (userInput == 2) {
                	connectivity = "wireless";
                	addStockMenu();
                	break;
                } else if (userInput == 3) {
                	addColourMenu();
                	break;
                } else {
                    System.out.println("Invalid input, please choose a number between 1 and 3");
                }
            } else {
                System.out.println("Invalid input, please choose a number between 1 and 3");
                scanner.next();  // Consume the invalid input
            }
        }
        
        scanner.close();
    }
    
    public static void addStockMenu() {
    	Scanner scanner = new Scanner(System.in);
        int userInput = 0;

        while (true) {
        	System.out.println("");
            System.out.println("Input quantity stocked or 0 to go back");
            
            // Check if the next input is an integer
            if (scanner.hasNextInt()) {
                userInput = scanner.nextInt();
                
                // Check if the input is within the valid range
                if (userInput == 0) {
                	addConnectivityMenu();
                    break;
                } else if (userInput > 0) {
                	quantityStocked = userInput;
                	originalCostMenu();
                	break;
                } else {
                    System.out.println("Invalid input, please choose an integer");
                }
            } else {
                System.out.println("Invalid input, please choose an integer");
                scanner.next();  // Consume the invalid input
            }
        }
        
        scanner.close();
    }
    
    public static void originalCostMenu() {
        Scanner scanner = new Scanner(System.in);
        double userInput = 0.0;

        while (true) {
            System.out.println();
            System.out.println("Input original cost (decimal values allowed) or 0 to go back");

            // Check if the next input is a double
            if (scanner.hasNextDouble()) {
                userInput = scanner.nextDouble();

                // Check if the input is exactly 0 to go back or positive for valid stock input
                if (userInput == 0) {
                    addStockMenu();
                    break;
                } else if (userInput > 0) {
                    originalCost = userInput;
                    retailPriceMenu();
                    break;
                } else {
                    System.out.println("Invalid input, please enter a positive decimal number or 0 to exit.");
                }
            } else {
                System.out.println("Invalid input, please enter a valid decimal number.");
                scanner.next();  // Consume the invalid input
            }
        }

        scanner.close();  // Be careful with closing scanners that wrap System.in
    }
    
    public static void retailPriceMenu() {
        Scanner scanner = new Scanner(System.in);
        double userInput = 0.0;

        while (true) {
            System.out.println();
            System.out.println("Input retail price (decimal values allowed) or 0 to go back");

            // Check if the next input is a double
            if (scanner.hasNextDouble()) {
                userInput = scanner.nextDouble();

                // Check if the input is exactly 0 to go back or positive for valid stock input
                if (userInput == 0) {
                    originalCostMenu();
                    break;
                } else if (userInput > 0) {
                    retailPrice = userInput;
                    if (productCategory == "keyboard") {
                    	keyboardLayoutMenu();
                    } else if (productCategory == "mouse") {
                    	mouseButtonMenu();
                    }
                    break;
                } else {
                    System.out.println("Invalid input, please enter a positive decimal number or 0 to exit");
                }
            } else {
                System.out.println("Invalid input, please enter a valid decimal number or 0 to exit");
                scanner.next();  // Consume the invalid input
            }
        }

        scanner.close();  // Be careful with closing scanners that wrap System.in
    }
    
    public static void keyboardLayoutMenu() {
    	Scanner scanner = new Scanner(System.in);
        int userInput = 0;

        while (true) {
        	System.out.println("");
            System.out.println("Choose a keyboard layout:");
            System.out.println("1 - UK");
            System.out.println("2 - US");
            System.out.println("3 - Back");
            
            // Check if the next input is an integer
            if (scanner.hasNextInt()) {
                userInput = scanner.nextInt();
                
                // Check if the input is within the valid range
                if (userInput == 1) {
                    extra = "UK";
                    admin.addPeripheral(barcode, productCategory, deviceType, brand, colour, connectivity, quantityStocked, originalCost, retailPrice, extra);
                	adminMainMenu();
                    break;
                } else if (userInput == 2) {
                	extra = "US";
                	admin.addPeripheral(barcode, productCategory, deviceType, brand, colour, connectivity, quantityStocked, originalCost, retailPrice, extra);
                	adminMainMenu();
                	break;
                } else if (userInput == 3) {
                	retailPriceMenu();
                	break;
                } else {
                    System.out.println("Invalid input, please choose a number between 1 and 3");
                }
            } else {
                System.out.println("Invalid input, please choose a number between 1 and 3");
                scanner.next();  // Consume the invalid input
            }
        }
        
        scanner.close();
    }
    
    public static void mouseButtonMenu() {
    	Scanner scanner = new Scanner(System.in);
        int userInput = 0;

        while (true) {
        	System.out.println("");
            System.out.println("Input number of buttons (or 0 to go back):");
            
            // Check if the next input is an integer
            if (scanner.hasNextInt()) {
                userInput = scanner.nextInt();
                
                // Check if the input is within the valid range
                if (userInput == 0) {
                	retailPriceMenu();
                    break;
                } else if (userInput > 0) {
                	String strInput = String.valueOf(userInput);
                	extra = strInput;
                	admin.addPeripheral(barcode, productCategory, deviceType, brand, colour, connectivity, quantityStocked, originalCost, retailPrice, extra);
                	adminMainMenu();
                	break;
                } else {
                    System.out.println("Invalid input, please choose a positive integer or 0");
                }
            } else {
                System.out.println("Invalid input, please choose a positive integer or 0");
                scanner.next();  // Consume the invalid input
            }
        }
        
        scanner.close();
    }
    
}