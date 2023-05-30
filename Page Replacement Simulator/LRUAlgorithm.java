import java.util.HashMap;
import java.util.Map;

public class LRUAlgorithm implements PageReplacementAlgorithm {
    private Memory memory;
    private Map<Page, Integer> lruCounter;
    private int counter;

    public LRUAlgorithm(int memoryCapacity) {
        this.memory = new Memory(memoryCapacity);
        this.lruCounter = new HashMap<>();
        this.counter = 0;
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
                Page evictedPage = findLRUPage();
                memory.replace(evictedPage, page);
                lruCounter.remove(evictedPage);
                pageToEvict = evictedPage.name;
            }
        }
        page.inMemory = true;
        lruCounter.put(page, counter++);

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
            lruCounter.entrySet().removeIf(entry -> entry.getKey().process.equals(page.process));
            memoryMap = memory.generateMemoryMap();
            pageReference.printMemoryStatistics(
                    "Exit",
                    memoryMap,
                    false,
                    null
            );
        }
    }

    private Page findLRUPage() {
        int minCounter = Integer.MAX_VALUE;
        Page lruPage = null;

        for (Map.Entry<Page, Integer> entry : lruCounter.entrySet()) {
            if (entry.getValue() < minCounter) {
                minCounter = entry.getValue();
                lruPage = entry.getKey();
            }
        }

        return lruPage;
    }
}
