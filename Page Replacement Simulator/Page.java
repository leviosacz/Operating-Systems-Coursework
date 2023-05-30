public class Page {
    public String name;
    public boolean inMemory;
    public Process process;
    public String processName;

    public Page(String name, boolean inMemory, Process process) {
        this.name = name;
        this.inMemory = inMemory;
        this.process = process;
        this.processName = process.name;
    }
}