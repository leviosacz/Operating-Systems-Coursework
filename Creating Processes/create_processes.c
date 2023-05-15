#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/types.h>

int main()
{
    int num_processes = 9;
    int i;
    pid_t pid;

    for (i = 1; i <= num_processes; i++)
    {
        pid = fork();
        if (pid < 0)
        {
            perror("fork failed");
            exit(1);
        }
        else if (pid == 0)
        {
            printf("Process %d: PID=%d, PPID=%d\n", i, getpid(), getppid());
        }
        else
        {
            wait(NULL);
            break;
        }
    }

    return 0;
}
