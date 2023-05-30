import java.util.LinkedList;
import java.util.Queue;

public class FIFOAlgorithm implements PageReplacementAlgorithm {
    private Memory memory;
    private Queue<Page> fifoQueue;

    public FIFOAlgorithm(int memoryCapacity) {
        this.memory = new Memory(memoryCapacity);
        this.fifoQueue = new LinkedList<>();
    }

    @Override
    public void referencePage(PageReference pageReference) {
        Page page = pageReference.page;
        boolean pageInMemory = memory.contains(page);
        String pageToEvict = null;

        if (!pageInMemory) {
            fifoQueue.add(page);
            if (!memory.isFull()) {
                memory.add(page);
            } else {
                Page evictedPage = fifoQueue.poll();
                memory.replace(evictedPage, page);
                pageToEvict = evictedPage.name;
            }
        }
        page.inMemory = true;

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
            fifoQueue.removeIf(p -> p.process.equals(page.process));
            memoryMap = memory.generateMemoryMap();
            pageReference.printMemoryStatistics(
                    "Exit",
                    memoryMap,
                    false,
                    null
            );
        }
    }
}