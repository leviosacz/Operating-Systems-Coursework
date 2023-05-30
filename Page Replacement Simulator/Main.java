import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        PrintStream ps = new PrintStream("G:\\SCU\\Courses\\COEN283\\Assignments\\Assignment3\\output.txt");
        System.setOut(ps);
        List<List<Process>> allProcessLists = new ArrayList<>();

        for (char c = 'A'; c <= 'E'; c++) {
            List<Process> processList = generateProcessList(String.valueOf(c), 150);
            allProcessLists.add(processList);
        }

        List<List<PageReference>> allPageReferenceLists = new ArrayList<>();

        for (int i = 0; i < allProcessLists.size(); i++) {
            List<PageReference> pageReferenceList = generatePageReferenceList(allProcessLists.get(i));
            allPageReferenceLists.add(pageReferenceList);
        }

        System.out.println("Generated Process Lists");
//        for (int i = 0; i < allProcessLists.size(); i++) {
//            System.out.println("Process List " + (i + 1) + ": ");
//            for (Process process : allProcessLists.get(i)) {
//                System.out.println("  Process: " + process.name + ", Size: " + process.size + ", Arrival Time: " + process.arrivalTime + " ms, Service Duration: " + process.serviceDuration + " s");
//            }
//        }

        System.out.println("\nGenerated Page Reference Lists");
//        for (int i = 0; i < allPageReferenceLists.size(); i++) {
//            System.out.println("Page Reference List " + (i + 1) + ": ");
//            for (PageReference pageReference : allPageReferenceLists.get(i)) {
//                System.out.println("  Page: " + pageReference.page.name + ", Timestamp: " + pageReference.timestamp + " ms");
//            }
//        }

        int memoryCapacity = 100;
        List<Class<? extends PageReplacementAlgorithm>> algorithms = List.of(
                FIFOAlgorithm.class,
                LRUAlgorithm.class,
                OptimalAlgorithm.class,
                RandomPickAlgorithm.class
        );

        for (Class<? extends PageReplacementAlgorithm> algorithmClass : algorithms) {
            runSimulation(algorithmClass, memoryCapacity, allPageReferenceLists, allProcessLists);
        }
        ps.close();
    }

    public static void runSimulation(Class<? extends PageReplacementAlgorithm> algorithmClass,
                                     int memoryCapacity,
                                     List<List<PageReference>> allPageReferenceLists,
                                     List<List<Process>> allProcessLists) {
        double totalHitMissRatio = 0;
        double totalSwappedInProcesses = 0;
        PageReference.pageReferenceCount = 0;

        System.out.printf("\nSimulating Page Replacement Algorithm (%s):\n----------------------------------------\n", algorithmClass.getSimpleName());

        for (int i = 0; i < allPageReferenceLists.size(); i++) {
            System.out.println("\nPage Reference List " + (i + 1) + ":");
            PageReplacementAlgorithm algorithm;
            try {
                algorithm = algorithmClass.getConstructor(int.class).newInstance(memoryCapacity);
            } catch (Exception e) {
                throw new RuntimeException("Error creating algorithm instance", e);
            }

            int swappedInProcesses = 0;
            resetProcesses(allProcessLists.get(i));

            for (PageReference pageReference : allPageReferenceLists.get(i)) {
                if (pageReference.page.process.remainTime == pageReference.page.process.serviceDuration * 1000) {
                    swappedInProcesses++;
                }
                if (algorithmClass.getSimpleName() == "OptimalAlgorithm") {
                    pageReference.futureReferences = allPageReferenceLists.get(i).subList(allPageReferenceLists.get(i).indexOf(pageReference) + 1, allPageReferenceLists.get(i).size());
                }
                algorithm.referencePage(pageReference);
            }

            double hitMissRatio = (double) PageReference.hitCount / (PageReference.hitCount + PageReference.missCount);
            System.out.printf("Hit Ratio (hit/all) for Page Reference List %d: %.5f\n", i + 1, hitMissRatio);
            System.out.printf("Number of Processes Swapped In for Page Reference List %d: %d\n", i + 1, swappedInProcesses);

            totalHitMissRatio += hitMissRatio;
            totalSwappedInProcesses += swappedInProcesses;

            PageReference.hitCount = 0;
            PageReference.missCount = 0;
        }

        double averageHitMissRatio = totalHitMissRatio / allPageReferenceLists.size();
        double averageSwappedInProcesses = totalSwappedInProcesses / allPageReferenceLists.size();

        System.out.printf("\nAverage Hit Ratio (hit/all): %.5f\n", averageHitMissRatio);
        System.out.printf("Average Number of Processes Swapped In: %.0f\n", averageSwappedInProcesses);
    }

    public static List<Process> generateProcessList(String processGroupName, int numOfProcesses) {
        List<Process> processList = new ArrayList<>();
        Random random = new Random();
        int[] sizes = {5, 11, 17, 31};
        int[] serviceDurations = {1, 2, 3, 4, 5};

        for (int i = 0; i < numOfProcesses; i++) {
            String name = processGroupName + (i + 1);
            int size = sizes[random.nextInt(sizes.length)];
            int arrivalTime = random.nextInt(100) * 100;
            int serviceDuration = serviceDurations[random.nextInt(serviceDurations.length)];
            processList.add(new Process(name, size, arrivalTime, serviceDuration));
        }

        return processList;
    }

    public static List<PageReference> generatePageReferenceList(List<Process> processList) {
        int memoryCapacity = 100;
        int freePages = memoryCapacity;
        JobQueue jobQueue = new JobQueue();
        jobQueue.addAll(processList);

        List<Process> assignedProcesses = new ArrayList<>();
        while (!jobQueue.isEmpty()) {
            Process process = jobQueue.peek();
            int processPages = process.size;
            if (freePages >= processPages) {
                jobQueue.poll();
                assignedProcesses.add(process);
                freePages -= processPages;
            } else {
                break;
            }
        }

        assignedProcesses.sort(Comparator.comparingInt(process -> process.arrivalTime));

        int maxArrivalTime = assignedProcesses.stream().mapToInt(process -> process.arrivalTime).max().orElse(0);
        int totalTime = maxArrivalTime + assignedProcesses.stream().mapToInt(process -> process.serviceDuration).sum() * 1000;

        List<Process> activeProcesses = new ArrayList<>();
        int processIndex = 0;
        Random random = new Random();
        List<PageReference> pageReferences = new ArrayList<>();

        for (int currentTime = 0; currentTime < totalTime; currentTime += 10) {
            while (processIndex < assignedProcesses.size() && assignedProcesses.get(processIndex).arrivalTime <= currentTime) {
                activeProcesses.add(assignedProcesses.get(processIndex));
                processIndex++;
            }

            List<Process> completedProcesses = new ArrayList<>();
            for (Process process : activeProcesses) {
                for (int i = 0; i < process.size; i++) {
                    if (process.remainTime <= 0) {
                        completedProcesses.add(process);
                        int processPages = process.size;
                        freePages += processPages;

                        // Set inMemory flag to false for all pages of the completed process
                        for (Page page : process.pageList) {
                            page.inMemory = false;
                        }

                        continue;
                    }

                    int pageIndex;
                    if (random.nextDouble() < 0.7) {
                        pageIndex = (process.currentPageIndex + random.nextInt(3) - 1 + process.size) % process.size;
                    } else {
                        do {
                            pageIndex = random.nextInt(process.size);
                        } while (Math.abs(pageIndex - process.currentPageIndex) <= 1);
                    }
                    process.currentPageIndex = pageIndex;
                    // Add the timestamp to the referenced page
                    if (currentTime < 60000) {
                        PageReference pageReference = new PageReference(process.pageList.get(pageIndex), currentTime);
                        pageReferences.add(pageReference);
                    }
                }


                process.remainTime = 0;
            }

            activeProcesses.removeAll(completedProcesses);

            // Assign memory to new processes if there are free pages
            while (!jobQueue.isEmpty()) {
                Process process = jobQueue.peek();
                int processPages = process.size;
                if (freePages >= processPages) {
                    jobQueue.poll();
                    assignedProcesses.add(process);
                    freePages -= processPages;
                } else {
                    break;
                }
            }
        }

        return pageReferences;
    }

    public static void resetProcesses(List<Process> processList) {
        for (Process process : processList) {
            // Reset remainTime to serviceDuration * 1000
            process.remainTime = process.serviceDuration * 1000;

            // Reset currentPageIndex to 0
            process.currentPageIndex = 0;

            // Set inMemory flag of all pages to false
            for (Page page : process.pageList) {
                page.inMemory = false;
            }
        }
    }
}