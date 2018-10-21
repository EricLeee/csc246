import java.util.Scanner;

/**
 * scan square and count numbers of complete squares
 * @author Jialang Li
 *
 */
public class Square {
    
    private static int num;
    
    private static char grid[][];
    
    private static int rows, cols;
    
    private static boolean report = false;

    static class thread1 extends Thread {

    private int startC;
        
    private int startR;
        
    private char alph[];
  
    private int count = 0;
        
    private int size;
    
    public thread1( int startRow ) {
        	
        this.startR = startRow;
        this.startC = 0;
        this.size = 0;
    	this.alph = new char[ 26 ];
    }
        
    @Override
    public void run() {
      	while( startR + 6 <= rows ) {
       	    for( int k = startC; k <= cols - 6; k++ ) {
       	        for( int i = startR; i < startR + 6; i++ ) {
                    for( int j = k; j < k + 6; j++ ) {
                        char c = grid[ i ][ j ];
                        int ch = (int)c - 97;
                        if ( alph[ ch ] == 0 ) {
                            alph[ ch ] = c;
                            size++;
                        }
                    }
                }
                if( size == 26 ) {
                    this.count++;
                    if( report )
                        System.out.println( startR + " " + k );
                }
                        
                    alph = new char[ 26 ];
                    size = 0;
        	    }
        	    
                startR += num;
                startC = 0;
            }
            
        }
        
    }
    
    public static void main( String[] args ) {

    	try {
    		num = Integer.parseInt(args[ 0 ]);
    	} catch ( NumberFormatException e ) {
    		System.out.println( "first argument must be int\n" );
    	}
        
		Scanner fd = new Scanner( System.in );
        
        if( args.length == 2 && args[ 1 ].equals("report") )
        	report = true;
        
        rows = fd.nextInt();
        cols = fd.nextInt();
        grid = new char[ rows ][ cols ];
        fd.nextLine();
        
        String temp = null;
        for( int j = 0; j < rows; j++ ){
            temp = fd.nextLine();
            for( int i = 0; i < cols; i++ )
                grid[ j ][ i ] = temp.charAt( i );
            
        }
        fd.close();
        
        thread1[] worker = new thread1[ num ];
        
        for( int i = 0; i < num; i++ ) {
        	worker[ i ] = new thread1( i );
        	worker[ i ].start();
        }
        
        try {
        	for( int i = 0; i < num; i++ ) {
        		worker[ i ].join();
        	}
        } catch ( InterruptedException e ) {
        	System.out.println( "Interrupted\n" );
        }
        
        
        int total = 0;
    	for( int i = 0; i < num; i++ )
    		total += worker[ i ].count;
    	
    	System.out.println( "Squares: " + total );
    	
    }
}
