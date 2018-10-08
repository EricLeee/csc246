/** A simple class to demonstrate threads in Java. */
public class HelloThreads {
  /** Shared variable both threads can see. */
  private static volatile int sharedVariable = 0;

  /** Something for my new tread to do. */
  static class MyRunnable implements Runnable {
    public void run() {
      // Periodically increment the shared variable.
      for ( int i = 0; i < 15; i++ ) {
        try {
          Thread.sleep( 500 );
        } catch ( InterruptedException e ) {
        }

        sharedVariable += 1;
      }
    }
  }

  /** Make a thread and wait for it to do something. */
  public static void main( String[] args ) {
    // make the new thread and start it up.
    Thread myThread = new Thread( new MyRunnable() );
    myThread.start();

    // Print a message over and over, so the user can see two threads running.
    for ( int i = 0; i < 20; i++ ) {
      System.out.println( "Original thread: " + sharedVariable );
      
      try {
        Thread.sleep( 250 );
      } catch ( InterruptedException e ) {
      }
    }

    // Wait for the new thread to exit.
    try {
      myThread.join();
    } catch ( InterruptedException e ) {
      System.out.println( "Interrupted during join!" );
    }

    System.out.println( "New thread is done" );
  }
}
