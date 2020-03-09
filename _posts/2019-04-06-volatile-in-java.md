---
layout: post
title:  "Volatile keyword in Java"
date:   2019-04-06 07:00:00 +0530
tags: threads posix-threads c posix multicore
comments: true
meta:
    keywords: "threads, thread, posix-thread, posix-threads,posix multicore, threading process, operating-system threads, concurrency, scheduler"
---

If you have just started multithreaded programming in Java, or if you are confused about this
**volatile** keyword in Java, then this post is for you.

Need for volatile

Outline
1. Introduction to the problem. Need for volatile - Cache Coherence
2. How volatile solves the problem
3. Should volatiles be always used?
3. Conclusion 


Single threaded model -

When we are working only in a single thread environment, life is so simple. There are so much things that we don't have to worry about. You can very(strikethrough) easily reason about your program correctness, performance and determinism. You know that your program starts in the main function and will follow your function body line by line, following all the method calls in between and stopping only when either you have reached the last line in your program or your program has thrown some exception.

Let's take the below example which is going to be the long running example for this post - 

public class SingleThreadedProgram {
    private boolean running;

    public boolean isRunning() {
        return this.running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public static void main(String args[]) {
        SingleThreadedProgram stp = new SingleThreadedProgram();
        System.out.println("Value of running before setting");
        System.out.println(stp.isRunning());
        System.out.println("Setting running to true");
        stp.setRunning(true);
        System.out.println("Value of running after setting");
        System.out.println(stp.isRunning);
    }
}

It's a very simple program. You have a main method which initializes an instance of the class and calls some operations on it. Our class has an count instance variable which stores the state of the class. Possible actions on the class is to either get the current count or increase the value of the count by 1.

If you run the program above, you would get the following output which is quite obvious - 
Value of count before increasing 0
Increasing the count by 1
Value of count after increasing 1

Till now it's pretty obvious. Now let's turn our attention to when this class is used in a multithreaded context.

Here we have a main Thread which runs the main method body which contructs an instance of the Class. We wrap the instance in an Thread and start it. Now we have two threads running. One is the main thread and other is the SingleThreadedProgram thread which is doing nothing, just doing conditional check every time.

To stop the second thread from wasting cycles, we have to set the value of the variable running to false. To set the value, we call the function setRunning from the main thread and passing in false. Ideally the thread should have checked that the value of the variable running is set to false now in it's next iteration but that won't happen and it would keep on spinning and wasting cycles.

Now why did that happen. Answer is cache incoherence.

Cache coherence - 

The problem is the running variable. Two threads have access to the value of the variable. What we want here is that when we update the value of the running variable, the second thread should see the updated variable and come out of the while loop.






