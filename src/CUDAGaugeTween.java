import java.util.TimerTask;




public class CUDAGaugeTween extends TimerTask {

	private CUDAGauge parent;
	private int cur_pos;
	private int tick;
	private int final_tick;
	private int direction;
	
	public CUDAGaugeTween(	CUDAGauge parent,			// the CUDAGauge parent of this tween
							int src_value,				// where we're starting
							int ticks,					// number of ticks
							int direction		 ) {	// direction of ticks (-1 or 1)
		
		this.parent = parent;
		this.cur_pos = src_value;
		this.tick = 0;
		this.final_tick = ticks;
		this.direction = direction;
		
		this.parent.tweening = true;
		
	}
	
	public void run () {
		
		this.cur_pos += this.direction;
		this.parent.setNeedle( this.cur_pos );
		
		if ( ++this.tick > this.final_tick )
			this.cancel();
		
	}

	public boolean cancel () {
		
		super.cancel();
		this.parent.tweening = false;
		return true;
		
	}
}
