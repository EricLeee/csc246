#include "pairMonitor.h"

#include <pthread.h>     // For mutex and condtion variables.
#include <stdlib.h>      // For malloc/free/exit
#include <stdio.h>       // For printf (but not using it)
#include <string.h>
#include <math.h>
#include <sys/time.h>

// Record for a pair of threads who have entered.
// We have one of these for every pair of threads in the room.
typedef struct {
  // Names for the two threads who entered.
  char name[ 2 ][ NAME_MAX + 1 ];

  // Add other fields that you need.
  // ...
} Pair;

// True if we're still running.
bool running = true;

// Capacity for the room we're entering.
int cap;

// List of all pairs of threads that have entered.
// An array of Pair structs with a length determined by capacity.
Pair *pairList;

// Time when we first create the monitor.
struct timeval startTime;

void initPairMonitor( int capacity ) {
  // Remember when the program started running.
  gettimeofday( &startTime, NULL );

  // Create and initialize the list of Pair structs.
  cap = capacity;
  pairList = (Pair *) malloc( sizeof( Pair ) * cap );
  for ( int i = 0; i < cap; i++ ) {
    // No threads are part of this pair yet.
    strcpy( pairList[ i ].name[ 0 ], "" );
    strcpy( pairList[ i ].name[ 1 ], "" );
  }
}

// Return the current execution time in milliseconds.
static long elapsedTime() {
  struct timeval endTime;
  gettimeofday( &endTime, NULL );

  long delta = 1000L * ( endTime.tv_sec - startTime.tv_sec ) +
    (long) round( ( endTime.tv_usec - startTime.tv_usec ) / 1000.0 );

  return delta;
}

void destroyPairMonitor() {

  // ...

  free( pairList );
}

bool enter( const char *name ) {

  // Find the index of an available Pair on the pairList.
  int idx = /* ... */;

  // ...

    // The second thread to join the pair can report when the two threads
    // enter.
    long delta = elapsedTime();
    printf( "Enter: %s %s (%ld.%03ld)\n",
            pairList[ idx ].name[ 0 ], pairList[ idx ].name[ 1 ],
            delta / 1000, delta % 1000 );

  // ...

  return true;
}

void leave( const char *name ) {

  // Find the index of the Pair object we're part of.
  int idx = /* ... */;

  // ...

    // The second thread to leave can report when the two threads enter.
    long delta = elapsedTime();
    printf( "Leave: %s %s (%ld.%03ld)\n",
            pairList[ idx ].name[ 0 ], pairList[ idx ].name[ 1 ],
            delta / 1000, delta % 1000 );

  // ...

}

void terminate() {
  // ...
}
