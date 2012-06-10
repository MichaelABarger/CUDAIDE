import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;



public class ArrowCanvas extends Canvas {

	private Color orange;
	private Color background;
	private GC gc;
	private int cur_y1, cur_y2;
	private Rectangle leftside, rightside;
	private boolean needs_clear = true;
	
	// constructor
	public ArrowCanvas( Composite parent, int style ) {
		
		super(parent, style);
		orange = new Color( this.getDisplay(), 255, 127, 0 );
		background = this.getBackground();
		gc = new GC( parent );
		cur_y1 = cur_y2 = -1;
		
	}
	
	public void setDimensions( Rectangle leftside, Rectangle rightside ) {
	
		this.leftside = leftside;
		this.rightside = rightside;
		
	}
	
	public void drawArrow( int y1, int y2 ) {
		
		if ( this.rightside == null || this.leftside == null )
			return;
		
		y1 += this.leftside.y;
		y2 += this.leftside.y;
		int x1 = this.leftside.x + this.leftside.width;
		
		if ( this.cur_y1 != y1 && this.cur_y2 != y2 ) {
			
			if ( this.cur_y1 != -1 && this.cur_y2 != -1 && needs_clear ) {
				
				this.gc.setAntialias( SWT.ON );
				this.gc.setBackground( background );
				this.gc.fillPolygon( new int[] { x1, this.cur_y2 + 1, x1, this.cur_y1 - 1, rightside.x, rightside.y - 1, rightside.x, rightside.y + rightside.height + 1 } );
				this.gc.setAntialias( SWT.OFF );
				
				needs_clear = false;
			}
		
			if ( CUDACode.arrow_visible ) {
				
				this.gc.setAntialias( SWT.ON );
				this.gc.setBackground( orange );
				this.gc.fillPolygon( new int[] { x1, y2, x1, y1, rightside.x, rightside.y, rightside.x, rightside.y + rightside.height } );
				this.gc.setAntialias( SWT.OFF );
				
				this.cur_y1 = y1;
				this.cur_y2 = y2;
				
				needs_clear = true;
			}
		}
		
	}
	
	public void finalize() throws Throwable {
		
		try {
			this.orange.dispose();
			this.gc.dispose();
		} catch ( Throwable e ) {
		} finally {
			super.finalize();
		}
		
	}

}
