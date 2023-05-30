public class Memory {
    private int capacity;
    private Page[] memory;

    public Memory(int capacity) {
        this.capacity = capacity;
        this.memory = new Page[capacity];
    }

    public Page[] getMemory() {
        return memory;
    }

    public boolean contains(Page page) {
        for (Page p : memory) {
            if (p != null && p.equals(page)) {
                return true;
            }
        }
        return false;
    }

    public boolean isFull() {
        for (Page p : memory) {
            if (p == null) {
                return false;
            }
        }
        return true;
    }

    public void add(Page page) {
        for (int i = 0; i < capacity; i++) {
            if (memory[i] == null) {
                memory[i] = page;
                break;
            }
        }
    }

    public void replace(Page oldPage, Page newPage) {
        for (int i = 0; i < capacity; i++) {
            if (memory[i] != null && memory[i].equals(oldPage)) {
                memory[i] = newPage;
                break;
            }
        }
    }

    public String generateMemoryMap() {
        StringBuilder memoryMap = new StringBuilder();
        for (Page page : memory) {
            if (page != null) {
                memoryMap.append(page.processName);
            } else {
                memoryMap.append(".");
            }
        }
        return memoryMap.toString();
    }

    public void removeAllPagesOfProcess(Process process) {
        for (int i = 0; i < capacity; i++) {
            if (memory[i] != null && memory[i].process == process) {
                memory[i] = null;
            }
        }
    }
}
