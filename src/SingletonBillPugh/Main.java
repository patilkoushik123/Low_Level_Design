package SingletonBillPugh;

public class Main {

    public static void main(String[] args) {
        System.out.println("1. Application started. Notice the constructor has NOT run yet.\n");

        // Define a task where threads call getInstance()
        Runnable task = () -> {
            TenantRegistry registry = TenantRegistry.getInstance();
            System.out.println(Thread.currentThread().getName() + " got instance -> HashCode: " + registry.hashCode());
        };

        System.out.println("2. Spawning multiple concurrent threads to test thread safety...\n");

        // Creating separate threads
        Thread t1 = new Thread(task, "Thread-A");
        Thread t2 = new Thread(task, "Thread-B");
        Thread t3 = new Thread(task, "Thread-C");

        // Starting them simultaneously
        t1.start();
        t2.start();
        t3.start();
        
        /*
         * 1. Application started. Notice the constructor has NOT run yet.

2. Spawning multiple concurrent threads to test thread safety...

--> [JVM] TenantRegistry constructor executed! (Object created NOW)
Thread-A got instance -> HashCode: 1765771427
Thread-B got instance -> HashCode: 1765771427
Thread-C got instance -> HashCode: 1765771427

here this is the output because only one time object is called (constructor prints one time so) and remaining threads utilize the same object(hashcode is same it means the object is same) 
NOTE: while creating object ,calling of constructor is mandatory,if we mention it will call that constructor or else it will internally default constructor.
Daily life example: suppose lets take an building as an object,If we want to occupy that building ,fst that building should be built by constructor,same in java while creating an object or while we need to use that object ,fst it get called by constructor.
         */
    }
}


// uses of bill pugh: 1.only creates object when it calls as it is in the seperate class
//2.when multiple threads hits the same instance as the object is in class level  ,jvm creates the insrance for that fst thread and remaining threads get skips and remaining threads get utiilize the same object
// 3. no need of synchronized word,as jvm helps to manage the threads,fst thread will gets in object creation remaining threads by pass.

