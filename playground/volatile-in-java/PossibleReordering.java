public class PossibleReordering {
	static int x = 0, y = 0;
	static int a = 0, b = 0;


	public static void main(String args[]) throws Exception {
		Thread first = new Thread(new Runnable() {
			public void run() {
				a = 1;
				x = b;
			}
		});

		Thread second = new Thread(new Runnable() {
			public void run() {
				b = 1;
				y = a;
			}
		});
		
		first.start();
		second.start();
		first.join();
		second.join();
		System.out.println("x = " + x);
		System.out.println("y = " + y);
	}

}
