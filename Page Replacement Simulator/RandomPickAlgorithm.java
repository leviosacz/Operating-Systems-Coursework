import java.util.*;

public class RandomPickAlgorithm implements PageReplacementAlgorithm {
    private Memory memory;
    private Random random;

    public RandomPickAlgorithm(int memoryCapacity) {
        this.memory = new Memory(memoryCapacity);
        this.random = new Random();
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
                Page evictedPage = pickRandomPageToEvict();
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

    private Page pickRandomPageToEvict() {
        Page[] pagesInMemory = memory.getMemory();
        int randomIndex = random.nextInt(pagesInMemory.length);
        return pagesInMemory[randomIndex];
    }
}
