import java.util.List;

public class PageReference {
    public Page page;
    public int timestamp;
    public static int hitCount = 0;
    public static int missCount = 0;
    public static int pageReferenceCount = 0;
    public List<PageReference> futureReferences = null;

    public PageReference(Page page, int timestamp) {
        this.page = page;
        this.timestamp = timestamp;
    }

    public void printMemoryStatistics(String action, String memoryMap, boolean pageInMemory, String pageToEvict) {
        System.out.printf(
                "%.3fs %-5s %-5s %-4d %-5d %s\n",
                timestamp/1000.0,
                page.processName,
                action,
                page.process.size,
                page.process.serviceDuration,
                memoryMap
        );
    }

    public void printPageStatistics(boolean pageInMemory, String pageToEvict) {
        if (pageReferenceCount < 100) {
            pageReferenceCount++;
            System.out.printf(
                    "%.3fs %-5s %-5s %-5d\n",
                    timestamp / 1000.0,
                    page.name,
                    pageInMemory ? "Hit" : "Miss",
                    page.process.serviceDuration
            );
        }
        if (pageInMemory) {
            hitCount++;
        } else {
            missCount++;
        }
    }
}