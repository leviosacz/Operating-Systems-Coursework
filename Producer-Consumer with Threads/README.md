# Producer-Consumer with Threads

## Introduction
One thread, the producer, will generate a sequence of integers and write them in order to successive buffer cells. Several consumer threads will then read these integers from the buffer and print them out in the order read. The is a circular buffer shared between the producer and consumers.

## Running the Program

I included source code and a JAR file. To run the program, you can use the following command:

`java -jar prodcon.jar [numConsumers]`

where `numConsumers` is the number of consumer threads you want to create (default is 2 if not specified).

## Results

```
Thread-0 produced 0
Thread-0 produced 1
Thread-0 produced 2
Thread-0 produced 3
Thread-0 produced 4
Thread-0 producer is waiting
Thread-1 consumed 0
Thread-1 consumed 1
Thread-1 consumed 2
Thread-1 consumed 3
Thread-1 consumed 4
Thread-1 consumer is waiting
Thread-2 consumer is waiting
Thread-0 produced 5
Thread-0 produced 6
Thread-0 produced 7
Thread-0 produced 8
Thread-0 produced 9
Thread-0 producer is waiting
Thread-1 consumed 5
Thread-1 consumed 6
Thread-1 consumed 7
Thread-1 consumed 8
Thread-1 consumed 9
Thread-1 consumer is waiting
Thread-2 consumer is waiting
Thread-1 consumer is waiting
```

It's worth noting that this order of printing may vary each time you run the program, as it depends on the timing and scheduling of the threads.
