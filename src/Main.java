/**
 * delivery-route-optimizer/src/Main.java
 * A Java program for delivery routes optimization using Dijkstra's Algorithm
 * @author Anna Khoriakova
 * @date April 11, 2025
 * @email khoriakova.anna@gmail.com
 * @version 1.0
 */

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in); // Create scanner for user input
        int numWarehouses;
        int numDeliveryLoc;
        int numRoads;
        String userInput;
        CityGraph cityGraph = new CityGraph();

        // Obtain the number of warehouses from the user
        System.out.print("Number of Warehouses (Always 1): ");
        try {
            String input = scanner.nextLine(); // Obtain user input
            input = input.replaceAll("\\s+",""); // Remove whitespaces
            if (input.isBlank()) { // If the user inputs an empty line, return
                System.out.println("Error: Cannot have an empty input");
                return;
            }
            try {
                numWarehouses = Integer.parseInt(input); // Attempt to parse the user's input to an int
                if (numWarehouses != 1) { // If the number of warehouses isn't 1, return
                    System.out.println("Error: Number of warehouses must be 1");
                    return;
                }
            } catch (NumberFormatException e) { // If the number of warehouses isn't a number, return
                System.out.println("Error: Number of warehouses must be a number");
                return;
            }
        } catch (Exception e) { // If an unexpected error occurred, return
            System.out.println("An unexpected error occurred");
            return;
        }

        // Obtain the number of delivery locations from the user
        System.out.print("Number of Delivery Locations: ");
        try {
            String input = scanner.nextLine(); // Obtain user input
            input = input.replaceAll("\\s+",""); // Remove whitespaces
            if (input.isBlank()) { // If the user inputs an empty line, return
                System.out.println("Error: Cannot have an empty input");
                return;
            }
            try {
                numDeliveryLoc = Integer.parseInt(input); // Attempt to parse the user's input to an int
                if (numDeliveryLoc < 0) { // If the number of delivery locations is less than 0, return
                    System.out.println("Error: Number of delivery locations cannot be negative");
                    return;
                }
            } catch (NumberFormatException e) { // If the number of warehouses isn't a number, return
                System.out.println("Error: Number of delivery locations must be a number");
                return;
            }
        } catch (Exception e) { // If an unexpected error occurred, return
            System.out.println("An unexpected error occurred");
            return;
        }

        // Obtain the number of roads connecting locations from the user
        System.out.print("Number of Roads Connecting Locations: ");
        try {
            String input = scanner.nextLine(); // Obtain user input
            input = input.replaceAll("\\s+",""); // Remove whitespaces
            if (input.isBlank()) { // If the user inputs an empty line, return
                System.out.println("Error: Cannot have an empty input");
                return;
            }
            try {
                numRoads = Integer.parseInt(input); // Attempt to parse the user's input to an int
                if (numRoads < 0) { // If the number of roads connecting locations is less than 0, return
                    System.out.println("Error: Number of roads connecting locations cannot be negative");
                    return;
                }
                if (numDeliveryLoc < 2 && numRoads > 1) {
                    System.out.println("Error: Less than 2 delivery locations, but greater than 1 road");
                    return;
                }
            } catch (NumberFormatException e) { // If the number of warehouses isn't a number, return
                System.out.println("Error: Number of roads connecting locations must be a number");
                return;
            }
        } catch (Exception e) { // If an unexpected error occurred, return
            System.out.println("An unexpected error occurred");
            return;
        }

        // Obtain the roads connecting locations from the user
        System.out.printf("Enter the Starting Location, Destination Location, and Distance. " +
                "Include a space between each input. Press ENTER to start the next entry. Will repeat %d times:\n", numRoads);

        // Repeat for the total number of roads
        for (int i = 0; i < numRoads; i++) {
            userInput = scanner.nextLine(); // Retrieve the user input
            userInput = userInput.trim(); // Trims leading and trailing whitespaces

            String[] splitInput = userInput.split("\\s+"); // Split the user input by whitespace regex
            // If the user did not provide 3 values separated by spaces, return
            if (splitInput.length != 3) {
                System.out.println("Error: Must provide 3 numbers");
                return;
            }

            int[] intInput = new int[3]; // Create an int array of size 3

            // Cycle through all 3 values in the splitInput String array and attempt to parse the String into an int
            try {
                for (int j = 0; j < splitInput.length; j++) {
                    int intChar = Integer.parseInt(splitInput[j]); // Attempt to convert the input into an integer

                    // If the current value is less than 0 and not describing the distance between two locations, return
                    if (intChar < 0 && j != 2) {
                        System.out.println("Error: Locations cannot be negative");
                        return;
                    }

                    // If the current value is less than or equal to 0 and describes the distance between two locations, return
                    if (intChar <= 0 && j == 2) {
                        System.out.println("Error: Distance cannot be negative or zero");
                        return;
                    }

                    // If the current value is describing a location that's greater than the total number of delivery locations, return 0
                    if (intChar > numDeliveryLoc && j != 2) {
                        System.out.println("Error: Starting or destination location does not exist");
                        return;
                    }
                    intInput[j] = intChar; // Add the value to the input
                }

            } catch (NumberFormatException e) { // Catch if an invalid input was entered and return immediately
                System.out.println("Error: Non-integer value(s) provided");
                return;
            }

            // Add the source and destination location, along with the road and weight of it
            cityGraph.addLocation(intInput[0]);
            cityGraph.addLocation(intInput[1]);
            cityGraph.addRoad(intInput[0], intInput[1], intInput[2]);
        }

        /* If a delivery location had no roads to or from it, it won't be in the city graph. Cycles through the delivery
        locations to check which location was not added to the city graph
         */
        if (cityGraph.getLocations().size() < numDeliveryLoc + 1) {
            for (int i = 0; i <= numDeliveryLoc; i++) {
                // If the city graph does not contain the delivery location, add it
                if (!cityGraph.getLocations().contains(i)) {
                    cityGraph.addLocation(i);
                }
            }
        }
        cityGraph.printRoutes(0); // Print the routes from the warehouse in the city graph
        scanner.close(); // Close the scanner to prevent resource leak
    }
}
