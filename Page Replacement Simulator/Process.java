import java.util.ArrayList;
import java.util.List;

public class Process {
    public String name;
    public int size;
    public int arrivalTime;
    public int serviceDuration;
    public int remainTime;
    public int currentPageIndex;
    public List<Page> pageList;

    public Process(String name, int size, int arrivalTime, int serviceDuration) {
        this.name = name;
        this.size = size;
        this.arrivalTime = arrivalTime;
        this.serviceDuration = serviceDuration;
        this.remainTime = serviceDuration;
        this.currentPageIndex = 0;
        this.pageList = generatePageList(this.size, this.name);
    }

    private List<Page> generatePageList(int size, String processName) {
        List<Page> pageList = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            String pageName = processName + "-" + (i + 1);
            boolean inMemory = false;
            Page page = new Page(pageName, inMemory, this);
            pageList.add(page);
        }

        return pageList;
    }
}