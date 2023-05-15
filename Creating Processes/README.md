# Creating Processes

The program `create_processes.c` creates a chain of child processes using the `fork()` system call. Each child process prints its process ID (`PID`) and its parent process ID (`PPID`) using the `getpid()` and `getppid()` system calls.

## Running the Program

### Prerequisites

- A C compiler, such as `gcc`
- A Unix-like operating system, such as Linux or macOS

### Compiling the Program

To compile the program, navigate to the directory containing the `create_processes.c` file and run the following command:

```bash
gcc create_processes.c -o create_processes
```

This will compile the program and create an executable file named `create_processes`.

### Running the Program

To run the program, simply execute the following command:

```bash
./create_processes
```

This will create a chain of child processes and print the `PID` and `PPID` of each process. The output should look something like this:

```
Process 1: PID=624, PPID=623
Process 2: PID=625, PPID=624
Process 3: PID=626, PPID=625
Process 4: PID=627, PPID=626
Process 5: PID=628, PPID=627
Process 6: PID=629, PPID=628
Process 7: PID=630, PPID=629
Process 8: PID=631, PPID=630
Process 9: PID=632, PPID=631
```

Note that the actual `PID` values will be different each time the program is run.

## How the Program Works

In this program, we use a single `for` loop to create a chain of child processes, where each child has a different parent.

When the original process (Process 0) forks, it creates a copy of itself, which becomes the child process (Process 1). The child process (Process 1) then continues the `for` loop and creates a new child process (Process 2). This process continues until we create the last child process (Process 9).

The key here is that each child process, when it calls `fork()`, creates a new process that is a copy of itself, with the same code and data as the parent. However, the child process and its parent have different process IDs (PID), and the child's `PPID` (Parent Process ID) is set to the PID of its parent. So, when Process 1 creates Process 2, Process 2 is a child of Process 1 with a `PPID` of Process 1's `PID`, not Process 0's `PID`.

Thus, the chain of child processes is created by the `for` loop, with each child process having a different parent, but all starting from the same original process (Process 0). When each child process calls `getppid()`, it will return the `PID` of its parent, which is the `PID` of the previous process in the chain.
