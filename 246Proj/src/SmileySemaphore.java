import java.util.Random;
import java.util.concurrent.Semaphore;

public class SmileySemaphore {
  /** Number of times for each thread to print its character. */
  static final public int LIMIT = 1000;

  /** Semaphores you declare and initialize here will be visible
      to all your threads (they'll need to be static. */
  static public Semaphore EYE = new Semaphore( 1 );
  
  static public Semaphore NOSE = new Semaphore( 0 );
  
  static public Semaphore MOUTH = new Semaphore( 0 );
  
  static public Semaphore LOCK = new Semaphore( 1 );

  /**
     A thread with a function to let it wait periodically.  In
     production, you wouldn't really want or need something like this
     as part of a multi-threaded program.  Here it varies thread
     execution timing so we see more of the possible execution orders
     for our threads.  This can help give you confidence that your
     solution is working correctly. */
  static class PokyThread extends Thread {
    /** Source of randomness for sometimes waiting. */
    private Random rand = new Random();

    /** Make the caller wait between 0 and t milliseconds. */
    protected void hesitate( int t ) {
      try {
        Thread.sleep( rand.nextInt( t ) );
      } catch ( InterruptedException e ) {
      }
    }
  }

  static class EyePrinter extends PokyThread {
    /** Symbol this thread is supposed to print for the eyes. */
    private char sym;

    /** Make an instance of this thread that prints the given mouth symbol. */
    public EyePrinter( char sym ) {
      this.sym = sym;
    }

    /** Print my face part over and over.  You'll need to add acquire()
        and release() calls to this and the other run methods to get
        them to only print smiley faces. */    
    public void run() {
      for ( int i = 0; i < LIMIT; i++ ) {

        hesitate( 10 );
        try {
            EYE.acquire();
            LOCK.acquire();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        

        // Print my symbol for the nose.
        System.out.print( sym );
        
        NOSE.release();
        LOCK.release();

      }
    }
  }
  
  static class NosePrinter extends PokyThread {
    /** Symbol this thread is supposed to print for the nose. */
    private char sym;

    /** Make an instance of this thread that prints the given mouth symbol. */
    public NosePrinter( char sym ) {
      this.sym = sym;
    }
    
    public void run() {
      for ( int i = 0; i < LIMIT; i++ ) {
        hesitate( 10 );

        NOSE.acquireUninterruptibly();
        LOCK.acquireUninterruptibly();

        // Print my symbol for the nose.
        System.out.print( sym );
        
        MOUTH.release();
        LOCK.release();
      }
    }
  }
  
  static class MouthPrinter extends PokyThread {
    /** Symbol this thread is supposed to print for the mouth. */
    private char sym;

    /** Make an instance of this thread that prints the given mouth symbol. */
    public MouthPrinter( char sym ) {
      this.sym = sym;
    }
    
    public void run() {
      for ( int i = 0; i < LIMIT; i++ ) {

        hesitate( 10 );
        MOUTH.acquireUninterruptibly();
        LOCK.acquireUninterruptibly();

        // I print the mouth symbol and the newline.
        System.out.println( sym );

        EYE.release();
        LOCK.release();
      }
    }
  }

  public static void main( String[] args ) {
    // Make our threads, in an array so it's easy to iterate over them.
    Thread[] threads = {
      new EyePrinter( ':' ),
      new EyePrinter( '8' ),
      new EyePrinter( ';' ),
      new NosePrinter( '-' ),
      new NosePrinter( '*' ),
      new NosePrinter( '+' ),
      new MouthPrinter( ')' ),
      new MouthPrinter( '|' ),
      new MouthPrinter( '(' ),
    };

    // Start them all.
    for ( int i = 0; i < threads.length; i++ )
      threads[ i ].start();

    // Wait for them to finish.
    try {
    for ( int i = 0; i < threads.length; i++ )
      threads[ i ].join();
    } catch ( InterruptedException e ) {
    }
  }
}
