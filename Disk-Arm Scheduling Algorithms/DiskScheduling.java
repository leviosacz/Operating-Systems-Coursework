// SCU COEN 283 Operating Systems
// Assignment 4 - Disk Scheduling Algorithms
// Chen Zhang, W1631147

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DiskScheduling {
    private static final int START_POSITION = 50;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter 10 unique cylinder/track numbers (0-99):");
        List<Integer> requests = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            requests.add(scanner.nextInt());
        }
        scanner.close();

        System.out.println("\nFirst Come-First Serve Algorithm");
        fcfs(requests);

        System.out.println("\nShortest Seek First Algorithm");
        ssf(requests);

        System.out.println("\nElevator (SCAN) Algorithm");
        elevator(requests);
    }

    private static void fcfs(List<Integer> requests) {
        int currentPosition = START_POSITION;
        int totalDistance = 0;

        for (int request : requests) {
            int distance = Math.abs(request - currentPosition);
            System.out.printf("Reading track %02d      distance moved: %d%n", request, distance);
            totalDistance += distance;
            currentPosition = request;
        }

        System.out.printf("FCFS Total distance: %d%n", totalDistance);
    }

    private static void ssf(List<Integer> requests) {
        List<Integer> remainingRequests = new ArrayList<>(requests);
        int currentPosition = START_POSITION;
        int totalDistance = 0;

        while (!remainingRequests.isEmpty()) {
            int closestRequest = findClosestRequest(currentPosition, remainingRequests);
            int distance = Math.abs(closestRequest - currentPosition);
            System.out.printf("Reading track %02d      distance moved: %d%n", closestRequest, distance);
            totalDistance += distance;
            currentPosition = closestRequest;
            remainingRequests.remove(Integer.valueOf(closestRequest));
        }

        System.out.printf("SSF Total distance: %d%n", totalDistance);
    }

    private static int findClosestRequest(int currentPosition, List<Integer> remainingRequests) {
        int closestRequest = remainingRequests.get(0);
        int minDistance = Math.abs(currentPosition - closestRequest);

        for (int request : remainingRequests) {
            int distance = Math.abs(currentPosition - request);
            if (distance < minDistance) {
                minDistance = distance;
                closestRequest = request;
            }
        }

        return closestRequest;
    }

    private static void elevator(List<Integer> requests) {
        List<Integer> remainingRequests = new ArrayList<>(requests);
        int currentPosition = START_POSITION;
        int totalDistance = 0;
        boolean movingUp = true;

        while (!remainingRequests.isEmpty()) {
            int nextRequest = findNextElevatorRequest(currentPosition, remainingRequests, movingUp);
            if (nextRequest != -1) {
                int distance = Math.abs(nextRequest - currentPosition);
                System.out.printf("Reading track %02d      distance moved: %d%n", nextRequest, distance);
                totalDistance += distance;
                currentPosition = nextRequest;
                remainingRequests.remove(Integer.valueOf(nextRequest));
            } else {
                movingUp = !movingUp;
            }
        }

        System.out.printf("Elevator Total distance: %d%n", totalDistance);
    }

    private static int findNextElevatorRequest(int currentPosition, List<Integer> remainingRequests, boolean movingUp) {
        int nextRequest = -1;
        int minDistance = Integer.MAX_VALUE;

        for (int request : remainingRequests) {
            int distance = movingUp ? request - currentPosition : currentPosition - request;
            if (distance >= 0 && distance < minDistance) {
                minDistance = distance;
                nextRequest = request;
            }
        }

        return nextRequest;
    }
}
