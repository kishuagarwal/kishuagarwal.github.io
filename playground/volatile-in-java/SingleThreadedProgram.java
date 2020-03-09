public class SingleThreadedProgram implements Runnable {
    private boolean running = true;

    public boolean isRunning() {
        return this.running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    @Override
    public void run() {
        running = true;
        while (running) {
            //System.out.println("Still running");
        }
        System.out.println(Thread.currentThread().getName() + ": Executed");
        System.out.println(System.nanoTime());
    }

    public static void main(String args[]) throws Exception {
        // Main thread
        SingleThreadedProgram stp = new SingleThreadedProgram();
        new Thread(stp).start();

        Thread.sleep(500);

        System.out.println(Thread.currentThread().getName() + ": Setting child thread to stop");
        stp.setRunning(false);
    }
}