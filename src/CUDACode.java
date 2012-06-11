import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CaretEvent;
import org.eclipse.swt.custom.CaretListener;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import java.io.*;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.wb.swt.SWTResourceManager;

	public final class CUDACode extends StyledText {

		public static boolean arrow_visible = false;
		private GC gc;
		private Font font;
		private boolean ready = false;
	
		public CUDACode(Composite parent, int style) {
			super(parent, style);
			addCaretListener(new CUDACaretListener());
			addPaintListener(new CUDAPaintListener());
			this.gc = new GC( this );
			this.font = SWTResourceManager.getFont("Segoe UI", 8, SWT.NORMAL);
			
		}
		
		public void resetCaret() {
			this.setCaretOffset( 100 );
			this.setCaretOffset( 0 );
			this.ready = true;
			this.redraw();
		}
		
		public class CUDACaretListener implements CaretListener {
			int prev_line = 0;
			Color orange;

			public void caretMoved ( CaretEvent e ) {
				
				int cur_line = MainWindow.ppCUDACode.getLineAtOffset( e.caretOffset );
				
				if ( orange == null )
					orange = new Color ( MainWindow.ppCUDACode.getDisplay(), 255, 127, 0 );
				
				if ( cur_line != prev_line ) {
					MainWindow.ppCUDACode.setLineBackground( prev_line, 1, null );
					MainWindow.ppCUDACode.setLineBackground( cur_line, 1, this.orange );
					prev_line = cur_line;
				}
				MainWindow.table.removeAll();
				if(MainWindow.ppPTXScanner.inScope(cur_line + 1)) {
					CUDACode.this.arrow_visible = true;
					ChangeTable(cur_line + 1);
				} else
					CUDACode.arrow_visible = false;
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
					data = new String[5];
					TableItem tblInstructions = new TableItem(MainWindow.table, SWT.NONE);
					for(int i = 0; i < 5; i++){
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
		
			public void paintControl( PaintEvent e ) {
				
				if ( MainWindow.ppCUDACode != null && MainWindow.arrow_canvas != null ) {
				
					int y1, y2;
					int caret_pos = MainWindow.ppCUDACode.getCaretOffset();
					int cur_line = MainWindow.ppCUDACode.getLineAtOffset( caret_pos );
					int line_pixel = MainWindow.ppCUDACode.getLinePixel( cur_line );
					final int line_height = MainWindow.ppCUDACode.getLineHeight();
					Rectangle bounds = MainWindow.ppCUDACode.getBounds();
					Rectangle client = MainWindow.ppCUDACode.getClientArea();
					
					if ( line_pixel < 0 )
						y1 = y2 = 0;
					else if ( line_pixel > bounds.height ) {
						y1 = client.height;
						y2 = bounds.height;
					} else {
						y1 = line_pixel + 1;
						y2 = y1 + line_height;
					}
					MainWindow.arrow_canvas.drawArrow( y1, y2 );
					
					// clear text
					gc.setBackground( CUDACode.this.getDisplay().getSystemColor(SWT.COLOR_WIDGET_DARK_SHADOW) );
					gc.setForeground( CUDACode.this.getDisplay().getSystemColor(SWT.COLOR_WIDGET_DARK_SHADOW) );
					gc.fillRectangle( client.x, client.y, MainWindow.ppCUDACode.getLeftMargin(), client.height );
					gc.setFont( CUDACode.this.font );
          gc.setForeground(CUDACode.this.getDisplay().getSystemColor(SWT.COLOR_WHITE));
          gc.setAntialias( SWT.ON );
					int cur_x = 5;
					// and draw text
					for ( int cur_y = 0; cur_y < client.height; cur_y += line_height ) {
					    cur_line = MainWindow.ppCUDACode.getLineIndex( cur_y );
					    if(MainWindow.ppCUDACode.ready && MainWindow.ppPTXScanner.inScope(cur_line + 1)) {
					         if ( MainWindow.ppPTXScanner.instructions( cur_line + 1 ) > 0 ) continue;
					         String cycle_sum = MainWindow.ppPTXScanner.getArg(cur_line + 1, 0, 5);
					         if ( cycle_sum == null ) cycle_sum = "";
					         gc.drawString(cycle_sum, cur_x, cur_y );
					    }
					}
					gc.setAntialias( SWT.OFF );
				}
			}
			
			public void finalize () throws Throwable {
				try {
				    gc.dispose();
				} catch ( Throwable e ) {
				} finally {
					super.finalize();
				}
			}
		}

	}
