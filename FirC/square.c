#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <stdbool.h>
#include <pthread.h>
#include <semaphore.h>

// Size of the square we're looking for.
#define SQUARE_WIDTH 6
#define SQUARE_HEIGHT 6
#define ASCIICONVERT 97

// Print out an error message and exit.
static void fail( char const *message ) {
  fprintf( stderr, "%s\n", message );
  exit( 1 );
}

// Print out a usage message, then exit.
static void usage() {
  printf( "usage: square <workers>\n" );
  printf( "       square <workers> report\n" );
  exit( 1 );
}

// True if we're supposed to report what we find.
bool report = false;

// Size of the grid of characters.
int rows, cols;

// Number of rows we've filled in so far.
int rowCount = 0;

// Maximum width of a row.  Makes it easier to allocate the whole
// grid contiguously.
#define MAX_WIDTH 16384

// Type used for a row of the grid.  Makes it easier to declare the
// grid as a pointer.
typedef char Row[ MAX_WIDTH ];

// Grid of letters.
Row *grid;

sem_t lock;

sem_t idxLock;

sem_t totalLock;

int idx = 0;

// Total number of complete squares we've found.
int total = 0;

// Read the grid of characters.
void readGrid() {
  // Read grdi dimensions.
  scanf( "%d%d", &rows, &cols );
  if ( cols > MAX_WIDTH ) {
    fprintf( stderr, "Input grid is too wide.\n" );
    exit( EXIT_FAILURE );
  }

  // Make space to store the grid as a big, contiguous array.
  grid = (Row *) malloc( rows * sizeof( Row ) );
  
  // Read each row of the grid as a string, then copy everything
  // but the null terminator into the grid array.
  char buffer[ MAX_WIDTH + 1 ];
  while ( rowCount < rows ) {
    scanf( "%s", buffer );
    
    memcpy( grid[ rowCount ], buffer, cols );
    rowCount += 1;
  }
}

/** Start routine for each worker. */
void *workerRoutine( void *arg ) {
  // ...
  int startR;
  sem_wait( &idxLock );
  startR = idx;
  idx++;
  sem_post( &idxLock );

  char alph[ 26 ];
  memset( alph, '\0', 26);

  int size = 0;
  int ch;

  while ( startR <= rows - SQUARE_HEIGHT || rows == 0 )
  {

	  while ( rowCount - startR < SQUARE_HEIGHT )
	  {
		  if ( rowCount == rows && rows != 0 )
			  return NULL;
		  sem_wait( &lock );
	  }

	  for ( int i = 0; i <= cols - SQUARE_WIDTH; i++ )
	  {
	    for ( int j = startR; j < startR + SQUARE_HEIGHT; j++ )
	    {
	    	for ( int k = i; k < i + SQUARE_WIDTH; k++ )
	    	{
	    		ch = ( int ) grid[ j ][ k ] - ASCIICONVERT;
	    		if ( alph[ ch ] == '\0' )
	    		{
	    			alph[ ch ] = grid[ j ][ k ];
	    			size++;
	    		}

	    	}

	  	}
	    if ( size == 26 )
	    {
	    	sem_wait( &totalLock );
	    	total++;
	    	sem_post( &totalLock );

	    	if ( report )
	    		printf( "%d %d\n", startR, i );
	    }
	  //  printf( "%s\n", alph );
	    memset( alph, '\0', 26 );
	    size = 0;
	  }

	  sem_post( &lock );


	  sem_wait( &idxLock );
	  startR = idx;
	  idx++;
	  sem_post( &idxLock );


  }

  return NULL;
}
  
int main( int argc, char *argv[] ) {
  int workers = 4;
  
  // Parse command-line arguments.
  if ( argc < 2 || argc > 3 )
    usage();
  
  if ( sscanf( argv[ 1 ], "%d", &workers ) != 1 ||
       workers < 1 )
    usage();

  // If there's a second argument, it better be "report"
  if ( argc == 3 ) {
    if ( strcmp( argv[ 2 ], "report" ) != 0 )
      usage();
    report = true;
  }


  sem_init( &lock, 0, 1 );
  sem_init( &idxLock, 0, 1 );
  sem_init( &totalLock, 0, 1 );
  // Make each of the workers.
  pthread_t worker[ workers ];
  for ( int i = 0; i < workers; i++ )
    // ...
	if ( pthread_create( worker + i, NULL, workerRoutine, &i ) != 0 )
		fail( "can't create thread\n" );

  // Then, start getting work for them to do.
  readGrid();

  // Wait until all the workers finish.
  for ( int i = 0; i < workers; i++ )
    // ...
	pthread_join( worker[ i ], NULL );

  sem_destroy( &lock );
  sem_destroy( &idxLock );
  sem_destroy( &totalLock );
  // Report the total and release the semaphores.
  printf( "Squares: %d\n", total );
  
  return EXIT_SUCCESS;
}
