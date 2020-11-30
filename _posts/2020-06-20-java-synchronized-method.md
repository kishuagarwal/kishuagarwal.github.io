---
layout: post
title:  "Caveat with Java synchronized methods"
date:   2020-06-20 10:56:08 +0530
tags: java concurrency multithreading synchronized-method
comments: true
meta:
    keywords: "java concurrency multithreading synchronized-method"
---

### Contents
- Need for synchronized methods
- How synchronized methods work
- Problem with synchronized methods
- Better ways


Most of you who have been working with Java language, would be aware of the synchronized method. If not, here is a small detour.

### Need for Synchronization

When working with multiple threads, you sometimes have the need to ensure controlled access to shared data. You want only one thread to be able to access shared data at one time. Here is an example

{% highlight java %}
public class VisitsHandler {
    int visitsCounter = 0;
    
    public static void main(String[] args) throws InterruptedException {
        VisitsHandler visitHandler = new VisitsHandler();
        int numUsers = 10000;
        Thread[] userThreads = new Thread[numUsers];
        for (int i = 0; i < numUsers; i += 1) {
            userThreads[i] = new Thread(new Runnable() {
                public void run() {
                    // System.out.println("Starting thread " + Thread.currentThread().getName());
                    visitHandler.visit();
                }
            }, "User " + i);
        }

        // Start all the user threads
        for (int i = 0; i < numUsers; i += 1) {
            userThreads[i].start();
        }

        // Join 
        for (int i = 0; i < numUsers; i += 1) {
            userThreads[i].join();
        }

        System.out.println("Total number of visits " + visitHandler.visitsCounter);
    }


    public void visit() {
        visitsCounter += 1;
    }
}
{% endhighlight %}

Here the shared data is the `visitsCounter` which tracks the number of visits that have happened to our website. We are modeling the users by independent threads running on the system with each thread calling the function `visit` to simulate user visiting our website. The code in turn increase the website `visitsCounter` variable.

Simple enough, right? If you run this program, and use sufficiently large value for the number of user threads (for small values also it would work, but it is easy to reproduce the behaviour for large values), you will notice that at the end of the program, `visitsCounter` variable value is sometimes not equal to the number of threads you chose.

This behaviour is explained by the following image - 

### TODO: Add image from the ipad showing two threads affecting the shared variable state

Thus, because of the multiple threads running at the same time and the thread scheduler switching between different threads at the runtime, you can never be sure at which instruction the thread switch can happen. By instruction, I mean the Java bytecode level instruction and not the Java language instructions which we type in our favourite code editor.

Now to solve this problem, Java provides many synchronization solutions. Today we are going to see how the Java `synchronized` keyword solves this problem for us. 

### Synchronized keyword

Java `synchronized` keyword can be used in two forms. One is where we add this to the method prototype and other is to use it as a code block. 

For the above site visitor program, here are the two ways to implement it using this two ways - 

### TODO: Add two ways code



### How does it work

Every Java object has an associated monitor with it. This monitor handles the mutual exclusion for that object.

When we use the synchronized code block, we provide the object whose monitor we want to use and for synchronized method, the object whose instance method is called, that object is implicitly used as the object whose monitor is to be used.

There is one point to add here. For static methods, it the class object itself whose monitor is used.

From this point, let's call the object whose monitor simply the monitor.

When one thread tries to access a method or a code block, which is protected by synchronized keyword, that thread first tries to acquire a lock on the monitor and if successful, then proceeds forward with rest of the code. If the lock acquire fails because some other thread has already acquired the lock, then the thread waits for the lock to be released.

This ensures that only one thread is able to execute the code which is protected inside the synchronized keyword. Other threads would be in the BLOCKED state.

BLOCKED is one of the [six](https://docs.oracle.com/javase/7/docs/api/java/lang/Thread.State.html) states a Java thread could be in.

Here is a nice demonstration of the linearization effect caused by the synchronized keywod. For this, I have used the number of threads to be 5 and added a sleep time of 5 seconds inside the `visits` function and used the JVisualVM to track the threads states over time.

### Add thread synchronization image captured from JVisualVM


### Caveat
Favour code blocks instead of methods.
Fine grain locks instead of coarse locks