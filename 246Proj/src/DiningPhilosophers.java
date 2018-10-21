import java.util.concurrent.Semaphore;
//import java.util.Random;

/** Simulation of the dining philosophers problem, the standard, flawed
    solution using semaphores. */
class DiningPhilosophers {
  /** Number of philosophers. */
  public static final int N = 5;

  /** Semaphore representing each of the chopsticks. */
  private Semaphore[] chopstick = new Semaphore[ N ];

  /** Runnable representing the actions of a philosopher. */
  private class Philosopher implements Runnable {
    /** Rank of this particular philosopher, set at construction time. */
    private int rank;
    
    /** Constructor, just to give each philosopher a unique rank. */
    public Philosopher( int r ) {
      rank = r;
    }

    /** Main function for each philosopher. */
    public void run() {
      for ( int i = 0; i < 10000; i++ ) {
        // Think fast
        System.out.println( rank + " is thinking" );

        // Get both chopsticks
        chopstick[ rank ].acquireUninterruptibly();
        chopstick[ ( rank + 1 ) % N ].acquireUninterruptibly();

        // Eat
        System.out.println( rank + " is eating" );

        // Release both chopsticks.
        chopstick[ rank ].release();
        chopstick[ ( rank + 1 ) % N ].release();
      }
    }
  }


  /** Instance method to actually run the dining philosophers
      simulation. */
  private void doSomething() {
    // Make all the semaphores for the chopsticks, each initialized to 1.
    for ( int i = 0; i < N; i++ )
      chopstick[ i ] = new Semaphore( 1 );

    // Make a thread for each philosopher.
    Thread[] plist = new Thread[ N ];
    for ( int i = 0; i < N; i++ ) {
      plist[ i ] = new Thread( new Philosopher( i ) );
      plist[ i ].start();
    }

    // Wait for all our philosophers to finish
    for ( int i = 0; i < N; i++ ) {
      try {
        plist[ i ].join();
      } catch ( InterruptedException e ) {
        System.err.println( "Interrupted while joining with " + i );
      }
    }
  }

  /** Make an instance of DiningPhilosophers and let it run the
      simulation. */
  public static void main( String[] args ) {
    DiningPhilosophers dp = new DiningPhilosophers();
    dp.doSomething();
  }
}
