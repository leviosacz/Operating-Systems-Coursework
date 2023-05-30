import java.util.*;

public class OptimalAlgorithm implements PageReplacementAlgorithm {
    private Memory memory;

    public OptimalAlgorithm(int memoryCapacity) {
        this.memory = new Memory(memoryCapacity);
    }

    @Override
    public void referencePage(PageReference pageReference) {
        Page page = pageReference.page;
        boolean pageInMemory = memory.contains(page);
        String pageToEvict = null;

        if (!pageInMemory) {
            if (!memory.isFull()) {
                memory.add(page);
            } else {
                Page evictedPage = findOptimalPageToEvict(pageReference.timestamp, pageReference.futureReferences);
                memory.replace(evictedPage, page);
                pageToEvict = evictedPage.name;
            }
        }

        // Check if process is entering
        if (page.process.remainTime == page.process.serviceDuration * 1000) {
            String memoryMap = memory.generateMemoryMap();
            pageReference.printMemoryStatistics(
                    "Enter",
                    memoryMap,
                    pageInMemory,
                    pageToEvict
            );
        }

        // Update process remaining time
        page.process.remainTime -= 100;
        String memoryMap = memory.generateMemoryMap();

        pageReference.printPageStatistics(
                pageInMemory,
                pageToEvict
        );

        if (page.process.remainTime == 0) {
            memory.removeAllPagesOfProcess(page.process);
            memoryMap = memory.generateMemoryMap();
            pageReference.printMemoryStatistics(
                    "Exit",
                    memoryMap,
                    false,
                    null
            );
        }
    }

    private Page findOptimalPageToEvict(int currentTimestamp, List<PageReference> futureReferences) {
        List<Page> pagesInMemory = Arrays.asList(memory.getMemory());
        Page farthestPage = null;
        int maxTimestamp = -1;

        for (Page page : pagesInMemory) {
            int nextTimestamp = getNextTimestamp(currentTimestamp, page, futureReferences);
            if (nextTimestamp > maxTimestamp) {
                maxTimestamp = nextTimestamp;
                farthestPage = page;
            }
        }

        return farthestPage;
    }

    private int getNextTimestamp(int currentTimestamp, Page page, List<PageReference> futureReferences) {
        int nextTimestamp = Integer.MAX_VALUE;
        for (PageReference reference : futureReferences) {
            if (reference.timestamp < nextTimestamp && reference.page.equals(page)) {
                nextTimestamp = reference.timestamp;
            }
        }
        return nextTimestamp;
    }
}
