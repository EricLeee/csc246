public class Interleaving {
  /** Thread to print ab. */

  // ...
  static class pAb implements Runnable {

    @Override
    public void run() {
        // TODO Auto-generated method stub
        System.out.print( "a" );
        System.out.print( "b" );
    }
  }

  /** Thread to print cd. */

  // ...
  static class pCd implements Runnable {

    @Override
    public void run() {
        // TODO Auto-generated method stub
        System.out.print( "c" );
        System.out.print( "d" );
    }
  }

  /** Thread to print ef. */

  // ...  
  static class pEf implements Runnable {

    @Override
    public void run() {
        System.out.print( "e" );
        System.out.print( "f" );
    }
  }

  public static void main( String[] args ) {
    // The three threads we make.
    Thread tab, tcd, tef;
  
    // A bunch of times.
    for ( int i = 0; i < 50000; i++ ) {
      // Make one of each type of threads.
      
      // ...
      tab = new Thread( new pAb() );
      tcd = new Thread( new pCd() );
      tef = new Thread( new pEf() );
      // Start them all.

      // ...
      tab.start();
      tcd.start();
      tef.start();

      // Join with our three threads.

      // ...
      try {
        tab.join();
        tcd.join();
        tef.join();
    } catch (InterruptedException e) {
        System.out.println("interruped during join" );
    }

      // Print a newline to end the line they just printed.
      System.out.println();
    }
  }
}
