# Delivery Routes Optimization with Dijkstra's Algorithm

A program that optimizes delivery routes by finding the shortest paths from a central warehouse to various delivery locations within a city.

## Description

Implements data structures to represent a city as a directed weighted graph, where:
- Nodes represent warehouses and delivery locations
- Edges represent roads connecting locations
- Edge weights represent distances between locations in kilometers

Utilizes Dijkstra's algorithm to compute the shortest delivery routes from a central warehouse to all delivery locations based on distance, enabling efficient logistics planning and resource optimization.

### Repository Structure
```
delivery-route-optimizer/
│
├── src/                          # Source code directory
│   ├── Main.java                 # Main application entry point
│   ├── CityGraph.java            # Graph data structure implementation and Dijkstra's algorithm
│
├── test-cases/                   # Test cases
│   ├── input1.txt                # Sample input test case 1
│   ├── output1.txt               # Expected output for test case 1
│   ├── input2.txt                # Sample input test case 2
│   ├── output2.txt               # Expected output for test case 2
│   ├── input3.txt                # Sample input test case 3
│   └── output3.txt               # Expected output for test case 3
│
├── DeliveryRouteOptimizer.jar    # Executable JAR file
└── README.md                     # Current file
```
### Dependencies

* Java Development Kit (JDK) 8 or higher
* No external libraries are required (uses only standard Java libraries)

### Executing the Program

#### Option 1: Running the Jar File:
1. Navigate to the location of the jar file and run the application
```
java -jar DeliveryRouteOptimizer.jar
```

#### Option 2: Command Line Compilation and Execution:

1. Navigate to the delivery-route-optimizer/src folder
```
cd delivery-route-optimizer/src
```

2. Compile the Java files
```
javac *.java
```

3. Run the program
```
java Main
```

### Input Format
The program expects input in the following format:
```
Number of Warehouses (Always 1): 
Number of Delivery Locations: 
Number of Roads Connecting Locations: 
Enter the Starting Location, Destination Location, and Distance. Include a space between each input. Press ENTER to start the next entry. Will repeat [the number of roads connecting locations] times:
```

#### Example Input:
```
1
3
5
0 1 10
0 2 15
1 2 5
1 3 20
2 3 12
```

#### Corresponding Output:
```
Delivery Location 1 - Shortest Route: 0 -> 1, Distance: 10
Delivery Location 2 - Shortest Route: 0 -> 2 or 0 -> 1 -> 2, Distance: 15
Delivery Location 3 - Shortest Route: 0 -> 2 -> 3 or 0 -> 1 -> 2 -> 3, Distance: 27
```

### Test Cases
Sample input test cases with expected output are provided in `delivery-route-optimizer/test-cases`. The implementation can be tested using the following in Powershell:
```
Get-Content test-cases/input1.txt | java -jar DeliveryRouteOptimizer.jar
```

## Author

Anna Khoriakova: [@annakhoriakova](https://github.com/annakhoriakova)