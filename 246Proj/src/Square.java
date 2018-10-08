import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Square {
    
    private static int num;
    
    private static char grid[][];

    static class thread1 implements Runnable {

        private int startR;
        
        private int startC;
        
        private char alph[];
        
        private int count = 0;
        
        public thread1( int r, int c ) {
            this.startR = r;
            this.startC = c;
            this.alph = new char[ 26 ];
        }
        
        public void setStartLoc( int r ) {
        	this.startR = r;
        }
        
        @Override
        public void run() {
            for( int i = startR; i < startR + 6; i++ ) {
                for( int j = startC; j < startC + 6; j++ ) {
                    char c = grid[ i ][ j ];
                    int ch = (int)c - 97;
                    if ( alph[ ch ] == ' ' )
                        alph[ ch ] = c;
                }
            }
            if( alph.length == 26 )
            	this.count++;
            
        }
        
    }
    
    public static void main( String[] args ) {
        num = (int)args[ 0 ].charAt( 0 );
        int row, col;
        
        Scanner fd = null;
        try {
            fd = new Scanner( new File ( args[ 1 ] ) );
        } catch (FileNotFoundException e) {
            System.out.println("cannot open file");
        }
        
        row = fd.nextInt();
        col = fd.nextInt();
        grid = new char[ row ][ col ];
        
        String temp = null;
        int rowNum = 0;
        while( fd.hasNextLine() ){
            temp = fd.nextLine();
            for( int i = 0; i < col; i++ ) {
                grid[ rowNum ][ i ] = temp.charAt( i );
                
            }
            rowNum++;

        }
        
        thread1 worker[] = new thread1[ num ];
        
        //
        for( int i = 0; i < num; i++ ) {
        	worker[ i ] = new thread1(0, 0);
        }
    }
}
