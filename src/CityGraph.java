/**
 * delivery-route-optimizer/src/CityGraph.java
 * Implementation of a data structure representing a city as a directed weighted graph. Includes Dijkstra's Algorithm
 * to find the shortest route from the central warehouse to all delivery locations
 * @author Anna Khoriakova
 * @date April 11, 2025
 * @email khoriakova.anna@gmail.com
 * @version 1.0
 */

import java.util.*;

public class CityGraph {
    private Map<Integer, List<Road>> adjList; // Adjacency list represented as a map

    /* Represents a road (edge) between two locations */
    static class Road {
        int destination;
        int distance;

        // Create a new Road
        public Road(int destination, int distance) {
            this.destination = destination;
            this.distance = distance;
        }
    }

    /* Stores location-distance pairs */
    static class LocationDistance {
        int location; // Delivery location
        int distance; // Distance from the warehouse

        // Create a new LocationDistance
        public LocationDistance(int location, int distance) {
            this.location = location;
            this.distance = distance;
        }
    }

    /* Specific class for storing route information (route sequence and total distance) */
    static class Route {
        // May be more than one route sequence to a delivery location with the shortest distance
        List<String> routeSequence; // Each String entry represents a route sequence to the delivery location
        int totalDistance; // Total distance

        // Create a new Route
        public Route(List<String> routeSequence, int totalDistance) {
            this.routeSequence = routeSequence;
            this.totalDistance = totalDistance;
        }
    }

    // Create a new CityGraph with an adjacency list
    public CityGraph() {
        adjList = new HashMap<>();
    }

    /**
     * Adds a location to the CityGraph
     * @param location The int representation of the location
     */
    public void addLocation(int location) {
        // If the location doesn't exist in the adjacency list, add it with an empty ArrayList of roads
        adjList.putIfAbsent(location, new ArrayList<>());
    }

    /**
     * Adds a road to the CityGraph
     * @param source The source location of the road
     * @param destination The destination location of the road
     * @param distance The distance between the two connected locations
     */
    public void addRoad(int source, int destination, int distance) {
        addLocation(source); // Adds the source location to the CityGraph
        addLocation(destination); // Adds the destination location to the CityGraph
        // Adds a new road from the source location to the destination location with the given distance
        adjList.get(source).add(new Road(destination, distance));
    }

    /**
     * Retrieves the locations in a CityGraph
     * @return A set of integer locations in the CityGraph
     */
    public Set<Integer> getLocations() {
        return adjList.keySet();
    }

    /**
     * Retrieves the roads corresponding to a location in a CityGraph
     * @param location The location to find the roads for
     * @return A list of roads from the given location
     */
    public List<Road> getRoads(int location) {
        return adjList.getOrDefault(location, new ArrayList<>());
    }

    /**
     * Uses Dijkstra's Algorithm to find the shortest route from the central warehouse to all delivery locations
     * @param warehouse The starting location warehouse
     * @return A map of the shortest reachable locations
     */
    private Map<Integer, Route> dijkstraShortestRoute(int warehouse) {
        // Map of distances; tracks the shortest known distance from the warehouse to each delivery location
        Map<Integer, Integer> distances = new HashMap<>();
        /* Map of path history (predecessors); tracks path history from the warehouse and maintains all
        possible paths that have the same shortest distance
         */
        Map<Integer, List<Integer>> pathHistory = new HashMap<>();
        // Generate a priority queue that prioritizes shorter distances
        PriorityQueue<LocationDistance> pq = new PriorityQueue<>(Comparator.comparingInt(ld -> ld.distance));

        // Cycles through each delivery location in the adjacency list
        for (int location : adjList.keySet()) {
            // Adds the location to the distances HashMap with an initial distance of infinity
            distances.put(location, Integer.MAX_VALUE);
            // Adds the location to the path history HashMap, creating a new ArrayList for the path history
            pathHistory.put(location, new ArrayList<>());
        }
        // Distance from the warehouse to the warehouse is 0
        distances.put(warehouse, 0);
        // Add to the priority queue a new LocationDistance. Distance from the warehouse to the warehouse is 0
        pq.add(new LocationDistance(warehouse, 0));

        // Cycles until the priority queue is empty
        while (!pq.isEmpty()) {
            /* Retrieve the next LocationDistance at the head of the priority queue
            (location with the shortest distance from the warehouse)
             */
            LocationDistance currentLoc = pq.poll();
            int location = currentLoc.location; // Obtain the current location

            /* If the distance of the current location is greater than a shorter path previously found,
            skip this entry in the priority queue
             */
            if (currentLoc.distance > distances.get(location))
                continue;

            // Cycles through each road in the adjacency list. If a location doesn't have roads, create a new ArrayList for the roads
            for (Road road : adjList.getOrDefault(location, new ArrayList<>())) {
                int destination = road.destination; // Retrieve the destination from the road
                /* Calculates the new distance by adding the distance to the location from the warehouse
                with the distance of the new road
                 */
                int newDistance = distances.get(location) + road.distance;

                // Check if the new distance is shorter than the current shortest distance to the destination
                if (newDistance < distances.get(destination)) {
                    // Add to the distances HashMap the destination with the new distance
                    distances.put(destination, newDistance);
                    // Reset the path history list for the location when a shorter path was found
                    pathHistory.get(destination).clear();
                    /* Set the location as the only previous location to the destination in the shortest path
                    (the best path to the destination comes through the location)
                     */
                    pathHistory.get(destination).add(location);
                    /* Add to the priority queue a new LocationDistance with the destination as the delivery location
                    and the new distance as the distance from the warehouse to the destination
                     */
                    pq.add(new LocationDistance(destination, newDistance));
                // Check if the new distance is equal to the current shortest distance to the destination
                } else if (newDistance == distances.get(destination)) {
                    // Add the location as a node to the shortest path to the destination
                    pathHistory.get(destination).add(location);
                }
            }
        }

        // Create a HashMap of Integer and Route to track the reachable locations
        Map<Integer, Route> reachableLoc = new HashMap<>();
        // Cycle through the locations in the adjacency list
        for (int location : adjList.keySet()) {
            // If the location is the warehouse, skip to the next cycle
            if (location == warehouse)
                continue;
            // If the distance to the location is infinity, the location is unreachable
            if (distances.get(location) == Integer.MAX_VALUE)
                /* Add to the reachable locations the delivery location and the label "Unreachable" for the route,
                represented as the sole object of a singletonList
                 */
                reachableLoc.put(location, new Route(Collections.singletonList("Unreachable"), -1));
            else {
                // Create an ArrayList to represent all the routes to the location
                List<String> allRoutes = new ArrayList<>();
                // Generate route information from the warehouse to the delivery location, using the path history HashMap
                createRouteInfo(warehouse, location, pathHistory, new LinkedList<>(), allRoutes);
                /* Add to the reachable locations the delivery location as the key, and a new Route. allRoutes represents
                the route sequence, and the total distance is represented by the distance to the location from the distances HashMap
                 */
                reachableLoc.put(location, new Route(allRoutes, distances.get(location)));
            }
        }
        return reachableLoc; // Return the reachable locations
    }

    /**
     * Generates shortest route information from the warehouse to the current delivery location by working
     * backwards through the path history
     * @param startLoc The starting location
     * @param currentLoc The current delivery location
     * @param previousLoc The path history HashMap
     * @param route The route to create the information in
     * @param allRoutes The list of all routes to the location
     */
    private void createRouteInfo(int startLoc, int currentLoc, Map<Integer, List<Integer>> previousLoc,
                                 LinkedList<Integer> route, List<String> allRoutes) {
        /* Dijkstra's algorithm will find the shortest from the target location to the source location using the
        path history (or predecessors). Will add to the head of the list to maintain proper order when the
        route is constructed
         */
        route.addFirst(currentLoc);
        // Check if the delivery location equals the starting location. If yes, convert the reversed route to a string
        if (currentLoc == startLoc) {
            StringBuilder path = new StringBuilder(); // Create a new StringBuilder for the path
            for (int i = 0; i < route.size(); i++) {
                // If i > 0, add an arrow
                if (i > 0)
                    path.append(" -> ");
                path.append(route.get(i)); // Append the location in the route at index i to the path
            }
            allRoutes.add(path.toString()); // Add to allRoutes the string representation of the route
        } else {
            for (int prev : previousLoc.get(currentLoc)) {
                createRouteInfo(startLoc, prev, previousLoc, route, allRoutes);
            }
        }
        /* Removes the current location before to backtrack (allows the list to be reused by removing last moves);
        allows alternatives to be explored without creating a new list for each path
         */
        route.removeFirst();
    }

    /**
     * Prints the routes from the warehouse
     * @param warehouse The int value of the warehouse
     */
    protected void printRoutes(int warehouse) {
        // Set routes to equal the resulting reachable locations HashMap returned by Dijkstra's algorithm
        Map<Integer, Route> routes = dijkstraShortestRoute(warehouse); // Pass in the int value of the warehouse
        System.out.println(); // Print empty line

        // Cycles through each entry in the routes entry set
        for (Map.Entry<Integer, Route> entry : routes.entrySet()) {
            int location = entry.getKey(); // Retrieve the int location (key in the entry)
            Route route = entry.getValue(); // Retrieve the Route (value of the location entry)

            /* If the total distance of the route is -1, then no route exists to the delivery location (it is
            unreachable from the central warehouse, and has distance infinity)
             */
            if (route.totalDistance == -1) {
                System.out.printf("Delivery Location %d - Shortest Route: No route exists, Distance: Infinity (Location %d" +
                        " is unreachable from the central warehouse)\n", location, location);
            } else {
                System.out.printf("Delivery Location %d - Shortest Route: ", location); // Print the delivery location
                // Cycle to print all route sequences to the delivery location
                for (int k = 0; k < route.routeSequence.size(); k++) {
                    // If the size of the route sequence is greater than 1, include printing "or" between paths
                    if (route.routeSequence.size() > 1)
                        // If k is not the last route sequence, print the route sequence and "or"
                        if (k != route.routeSequence.size() - 1)
                            System.out.printf("%s or ", route.routeSequence.get(k));
                        // If k will represent the last route sequence to the delivery location, do not include "or"
                        else
                            System.out.print(route.routeSequence.get(k));
                    // Otherwise print the route sequence as-is
                    else
                        System.out.print(route.routeSequence.get(k));
                }
                System.out.printf(", Distance: %d\n", route.totalDistance); // Print the total distance for the route
            }
        }
    }
}
