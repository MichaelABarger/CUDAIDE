import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;


public class ArrowCanvas extends Canvas {

	Color orange;
	Color background;
	GC gc;
	int cur_y1, cur_y2;
	Rectangle leftside, rightside;
	
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
		
		y1 += leftside.y;
		y2 += leftside.y;
		int x1 = this.leftside.x + this.leftside.width;
		
		if ( this.cur_y1 != -1 && this.cur_y2 != -1 ) {
			gc.setBackground( background );
			gc.fillPolygon( new int[] { x1, this.cur_y2, x1, this.cur_y1, rightside.x, rightside.y, rightside.x, rightside.y + rightside.height } );
		}
		gc.setBackground( orange );
		gc.fillPolygon( new int[] { x1, y2, x1, y1, rightside.x, rightside.y, rightside.x, rightside.y + rightside.height } );
		
		this.cur_y1 = y1;
		this.cur_y2 = y2;
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
