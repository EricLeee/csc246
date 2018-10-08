/** An example showing one way to pass values to a new thread and how
    to get values back, analogous to the argPassing.c example.  */
public class ThreadArguments {

  /** Instead of making a Runnable, you can just subclass
      Thread to tell the thread what to do. */
  static class MyThread extends Thread {
    /** Effectively, a parameter we're passing the thread, which fibonacci
        number it's supposed to compute.  Here, we can just store this in
        a field of the thread itself. */
    private int x;

    /** Effectively, the return value from the thread.  We can just leave it
        in a field. */
    public long fib;
    
    /** Make a new Thread, giving it a parameter value to store. */
    public MyThread( int x ) {
      this.x = x;
    }

    /** When run, I report in and compute a fibonacci number. */
    public void run() {
      System.out.println( "Hi there, I'm computing fib_" + x );

      // Compute our requested fibonacci number.
      long fa = 1;
      long fb = 1;

      for ( int i = 2; i < x; i++ ) {
        // Compute the next fibonacci number and put the previous one in fb.
        long c = fa + fb;
        fa = fb;
        fb = c;
      }

      // Store the final value in our fib field, where the main thread can get it.
      fib = fb;
    }
  }

  /** Make a thread and wait for it to do something. */
  public static void main( String[] args ) {

    // Make 10 threads and let them start running.
    MyThread[] thread = new MyThread [ 10 ];
    for ( int i = 0; i < thread.length; i++ ) {
      thread[ i ] = new MyThread( i + 1 );
      thread[ i ].start();
    }

    // Wait for each of the threads to terminate.
    try {
      for ( int i = 0; i < thread.length; i++ ) {
        thread[ i ].join();
        System.out.println( "Thread terminated: " + thread[ i ].x +
                            " " + thread[ i ].fib );
      }
    } catch ( InterruptedException e ) {
      System.out.println( "Interrupted during join!" );
    }
  }
}
