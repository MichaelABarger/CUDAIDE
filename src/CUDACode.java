import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CaretEvent;
import org.eclipse.swt.custom.CaretListener;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import java.io.*;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TableItem;

	public final class CUDACode extends StyledText {

		ArrowCanvas c;
	
		public CUDACode(Composite parent, int style) {
			super(parent, style);
			addCaretListener(new CUDACaretListener());
			addPaintListener(new CUDAPaintListener());
		}
		
		public void setCanvas( ArrowCanvas c ) {
			this.c = c;
		}
		
		public void resetCaret() {
			this.setCaretOffset( 100 );
			this.setCaretOffset( 0 );
		}
		
		public ArrowCanvas getCanvas() {
			return this.c;
		}

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
				MainWindow.table.removeAll();
				if(cur_line < MainWindow.ppPTXScanner.getScope())
					ChangeTable(cur_line + 1);
		//		else System.out.println(Integer.toString(cur_line) + " is out of CUDA scope (" + Integer.toString(MainWindow.ppPTXScanner.getScope()) + ")");
			}
			
			public void finalize () throws Throwable {
				try {
					this.orange.dispose();
				} catch ( Throwable e ) {
				} finally {
					super.finalize();
				}
			}
			
			public void ChangeTable(int n/*TableColumn a, TableColumn b, TableColumn c, TableColumn d*/)
			{
				String data [];
				int w = 0;
				w = MainWindow.ppPTXScanner.instructions(n);
				for(int j = 0; j < w; j++){
					data = new String[4];
					TableItem tblInstructions = new TableItem(MainWindow.table, SWT.NONE); 
					for(int i = 0; i < 4; i++){
						data[i] = MainWindow.ppPTXScanner.getArg(n, j, i);
						if(data[i] == null) data[i] = " ";
					}
					tblInstructions.setText(data);	
				}
//				a.pack();
//				b.pack();
//				c.pack();
//				d.pack();
			}
		}
		
		public void save(String filename) throws IOException {
			BufferedWriter s = new BufferedWriter(new FileWriter(filename));
			s.write(this.getText());
			s.close();
		}
		
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

	}