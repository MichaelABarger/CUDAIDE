import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;


public class CUDAPaintListener implements PaintListener {
	Display display;
	CUDACode parent;
	ArrowCanvas canvas;
	Rectangle bounds;
	Rectangle client;
	
	public void paintControl( PaintEvent e ) {
		int y1, y2;
		
		if ( this.parent == null )
			this.parent = (CUDACode)e.getSource();
		if ( this.display == null )
			this.display = this.parent.getDisplay();
		if ( this.canvas == null )
			this.canvas = this.parent.getCanvas();
		if ( this.bounds == null ) {
			this.client = parent.getClientArea();
			this.bounds = parent.getBounds();
		}

		
		int caret_pos = parent.getCaretOffset();
		int cur_line = parent.getLineAtOffset( caret_pos );
		int line_pixel = parent.getLinePixel( cur_line );
		
		if ( line_pixel < 0 )
			y1 = y2 = 0;
		else if ( line_pixel > bounds.height ) {
			y1 = client.height;
			y2 = bounds.height;
		} else {
			y1 = line_pixel + 1;
			y2 = y1 + parent.getLineHeight( cur_line );
		}
		this.canvas.drawArrow( y1, y2 );
	}
	
	public void finalize () throws Throwable {
		try {
			// nothing
		} catch ( Throwable e ) {
		} finally {
			super.finalize();
		}
	}
}
