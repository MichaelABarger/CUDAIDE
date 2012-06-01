import org.eclipse.swt.custom.CaretEvent;
import org.eclipse.swt.custom.CaretListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;




public class CUDACaretListener implements CaretListener {
	int prev_line = 0;
	Display display;
	CUDACode parent;
	Color orange;
	
	public void caretMoved ( CaretEvent e ) {
		if ( this.parent == null )
			this.parent = (CUDACode)e.getSource();
		if ( this.display == null )
			this.display = this.parent.getDisplay();
		if ( this.orange == null )
			this.orange = new Color ( this.display, 255, 127, 0 );
		
		int cur_line = parent.getLineAtOffset( e.caretOffset );
		
		if ( cur_line != prev_line ) {
			parent.setLineBackground( prev_line, 1, null );
			parent.setLineBackground( cur_line, 1, this.orange );
			prev_line = cur_line;
		}
	}
	
	public void finalize () throws Throwable {
		try {
			this.orange.dispose();
		} catch ( Throwable e ) {
		} finally {
			super.finalize();
		}
	}
}
