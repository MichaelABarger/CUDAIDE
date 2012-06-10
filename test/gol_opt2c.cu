/* 
   Original Author of gol_textual.c:
	Christopher Mitchell <chrism@lclark.edu>
   CUDAfied version by Michael Barger <mbarger@pdx.edu>
    for Homework 2 for CS510[GPU] (Prof Karavanic)
 */	

#include "gol_common2.h"


// The two boards -- host only needs one
unsigned char h_current[WIDTH * HEIGHT];
unsigned char *d_current;
size_t current_pitch;
unsigned char *d_next;
size_t next_pitch;

// set the grid/block dimensions for kernel execution
const dim3 gridDim(16, 16, 1);
const dim3 blocksDim(8+2, 8+2, 1); // 256 threads per block
const size_t sharedmem_size = (8+2) * (8+2);

extern __shared__ unsigned char local_current [];

__global__ void step	(const unsigned char *current,	// previous 2D field
						 size_t c_pitch,		// pitch of "current"
						 unsigned char *next,				// result 2D field
						 size_t n_pitch) {		// pitch of "next"
    // register constants for efficiency & readability
    const int local_x = threadIdx.x;
    const int local_y = threadIdx.y;
    const int block_w = blockDim.x;
    const int block_h = blockDim.y;
    const int abs_x = blockIdx.x * (block_w - 2) + local_x - 1;
    const int abs_y = blockIdx.y * (block_h - 2) + local_y - 1;
    const int maxx = block_w - 1;
    const int maxy = block_h - 1;
    const int local_pitch = block_w;
    const int offsets[8][2] = {{-1, 1},{0, 1},{1, 1},
                           {-1, 0},       {1, 0},
                           {-1,-1},{0,-1},{1,-1}};
    
    // normalize 2d array pitches for use with array notation
    c_pitch /= sizeof(int);
    n_pitch /= sizeof(int);
    
    int num_neighbors = 0;	// to numerate the number of neighbors
    int nx, ny;				// to improve readability for temp array index calculations
    
    // populate the shared memory
    nx = (abs_x + WIDTH) % WIDTH;
    ny = (abs_y + HEIGHT) % HEIGHT;
    local_current[local_y * local_pitch + local_x] = current[ny * c_pitch + nx];
    __syncthreads();

	// if a shared-memory builder thread, don't do anything more
    if (local_x > 0 && local_x <= maxx && local_y > 0 && local_y <= maxy) {
        // count this cell's alive neighbors
	    num_neighbors = 0;
	    for (int i = 0; i < 8; i++) {
	        nx = local_x + offsets[i][0];
	        ny = local_y + offsets[i][1];
	        num_neighbors += local_current[ny * local_pitch + nx]==1;
	    }
	
	    // apply the Game of Life rules to this cell
	    next[abs_y * n_pitch + abs_x] = ((local_current[local_y * local_pitch + local_x] && num_neighbors==2) || num_neighbors==3);
	}
}


void loop_func() {
    step<<<gridDim, blocksDim, sharedmem_size>>>(d_current, current_pitch, d_next, next_pitch);
    cudaCheckError("kernel execution");

    cudaMemcpy2D(h_current, row_bytes, d_next, next_pitch, row_bytes, HEIGHT, cudaMemcpyDeviceToHost);
    cudaCheckError("Device->Host memcpy");

    cudaMemcpy2D(d_current, current_pitch, d_next, next_pitch, row_bytes, HEIGHT, cudaMemcpyDeviceToDevice);
    cudaCheckError("Device->Device memcpy");
}


int main(void) {
	// allocate the device-side field arrays
	cudaMallocPitch((void **)&d_current, &current_pitch, row_bytes, HEIGHT);
	cudaMallocPitch((void **)&d_next, &next_pitch, row_bytes, HEIGHT);
	cudaCheckError("device memory allocation");

    // Initialize the host-side "current".
    fill_board(h_current);
    
    // copy host memory to device
    cudaMemcpy2D(d_current, current_pitch, h_current, row_bytes, row_bytes, HEIGHT, cudaMemcpyHostToDevice);
    cudaCheckError("init array host->device copy");
    
    // run the simulation!
    animate(loop_func, h_current);

	// free device memory
	cudaFree(d_current);
	cudaFree(d_next);
    return 0;
}
