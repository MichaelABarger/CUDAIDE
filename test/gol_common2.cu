#include "gol_common2.h"

const int field_dim = HEIGHT * WIDTH;
const size_t field_size = field_dim;
const size_t row_bytes = WIDTH;


void fill_board(unsigned char *board) {
    for (int i=0; i<field_dim; i++)
        board[i] = rand() % 2;
}

void print_board(const unsigned char *board) {
    int x, y;
    for (y=0; y<P_HEIGHT; y++) {
        for (x=0; x<P_WIDTH; x++) {
            char c = board[y * WIDTH + x] ? '#':' ';
            //printf("%c", c);
        }
        //printf("\n");
    }
    //printf("-----\n");
}

/* error check routine */
void cudaCheckError (const char *msg) {
	cudaError_t err = cudaGetLastError();
	if (err != cudaSuccess) {
		fprintf(stderr, "Cuda error: %s: %s.\n", msg, cudaGetErrorString(err));
		exit(EXIT_FAILURE);
	}
}


void animate(void (*func)(void), const unsigned char *board) {
	int i;
    /*struct timespec delay = {0, 300000000}; // 0.300 seconds*/
    /*struct timespec remaining;*/
/*
    while (1)
    	print_board(board);
    	(*func)();
        nanosleep(&delay, &remaining);
    }
*/   
    for (i = 1; i < 1024; i++) {
    	print_board(board);
    	(*func)();
    }
	printf( "hi!\n" );
}