import java.util.Scanner;

public class Square {
    
    private static int num;
    
    private static char grid[][];
    
    private static int rows, cols;
    
    private static boolean report = false;

    static class thread1 extends Thread {

        private int startC;
        
        private int workerNum;
        
        private char alph[];
        
        protected int count = 0;
        
        public thread1( int workerNum ) {
        	
        	this.workerNum = workerNum;
            this.startC = 0;
        	this.alph = new char[ 26 ];
        	
        }
        
        @Override
        public void run() {
        	int i = workerNum;
        	while( i + 6 < rows ) {
                for( int j = startC; j < startC + 6; j++ ) {
                    char c = grid[ i ][ j ];
                    int ch = (int)c - 97;
                    if ( alph[ ch ] == ' ' )
                        alph[ ch ] = c;
                }
                i += num;
            }
            if( alph.length == 26 )
            	this.count++;
            
        }
        
    }
    
    public static void main( String[] args ) {
        num = (int)args[ 0 ].charAt( 0 );
        
        @SuppressWarnings("resource")
		Scanner fd = new Scanner( System.in );
        
        if( args.length == 2 && args[ 1 ].equals("report") )
        	report = true;
        
        rows = fd.nextInt();
        cols = fd.nextInt();
        grid = new char[ rows ][ cols ];
        
        String temp = null;
        int rowNum = 0;
        while( fd.hasNextLine() ){
            temp = fd.nextLine();
            for( int i = 0; i < cols; i++ ) {
                grid[ rowNum ][ i ] = temp.charAt( i );
                
            }
            rowNum++;

        }
        
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
    	
    	System.out.println( total + "\n" );
    	
        if( report ) {
        	
        }
    }
}
