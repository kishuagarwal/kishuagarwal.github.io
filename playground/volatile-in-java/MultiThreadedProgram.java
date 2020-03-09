public class MultiThreadedProgram {
    private int count = 0;

    public static void main(String args[]) throws Exception {
    	MultiThreadedProgram instance = new MultiThreadedProgram();
        Thread readerThread = new Thread(new Runnable() {
        	public void run() {
        		instance.count = 1;
        		try {
	        		while (true) {
	        			Thread.sleep(100);
	        			System.out.println(Thread.currentThread().getName()+ ": " + instance.count);
	        		}
	        	} catch (Exception e) {}
        	}
        });
        readerThread.start();

        Thread writerThread = new Thread(new Runnable() {
        	public void run() {
        		MultiThreadedProgram localCache = instance;
        		try {
        			while (true) {
        				Thread.sleep(10);
	        			localCache.count += 1;
	        			System.out.println(Thread.currentThread().getName()+ ": " + localCache.count);
        			}
        		} catch (Exception e) {}
        		
        	}
        });
        writerThread.start();
    }
}