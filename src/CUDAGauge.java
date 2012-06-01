import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;




public class CUDAGauge extends Canvas {
	Image gauge_img;
	Image shiny_img;
	Color red;
	Color gray;
	Display display;
	GC graphics;
	int cur_needle_pos;
	
	static int origin = 80;
	static int needle_length = 60;
	static double max_angle = Math.PI / 3.2;
	
	public CUDAGauge( Composite parent, int flags ) {
		super( parent, SWT.NO_BACKGROUND );
		this.display = this.getDisplay();
		this.shiny_img = new Image( this.display, "res/shiny.png" );
		this.red = new Color( this.display, 220, 0, 0 );
		this.gray = new Color( this.display, 160, 160, 160 );
		
		this.graphics = new GC( this );
		this.cur_needle_pos = 50;
		
		this.addPaintListener( new PaintListener() {
			CUDAGauge parent;
			public void paintControl(PaintEvent e) {
				if ( this.parent == null )
					this.parent = (CUDAGauge)e.getSource();
				setNeedle( this.parent.cur_needle_pos );
			}
			
		});
		
	}
	
	public void setGaugeType( String gauge_type ) {
		
		this.gauge_img = new Image( this.display, "res/gauge-" + gauge_type + ".png" );
		
	}
	
	public void setNeedle( int value /* 1~100 */ ) {

		double angle = CUDAGauge.max_angle - value / 50.0 * CUDAGauge.max_angle;
		
		Point needle[] = new Point[] { new Point( 0, -CUDAGauge.needle_length ), new Point( -5, 0 ), new Point( 0, 10 ), new Point( 5, 0 ) };
		double transform[][] = new double[][] { { Math.cos(angle), -Math.sin(angle) }, { Math.sin(angle), Math.cos(angle) } };
		Point new_needle[] = new Point[4];
		Point pivot = adjustCoords( new Point( -3, -3 ) ); 
		
		for ( int i = 0; i < 4; i++ )
			new_needle[i] = adjustCoords( rotate( needle[i], transform ) );
		
		this.graphics.drawImage( this.gauge_img, 0, 0 );
		this.graphics.setAntialias( SWT.ON );
		this.graphics.setBackground( this.red );
		this.graphics.fillPolygon( new int[] { new_needle[0].x, new_needle[0].y, new_needle[1].x, new_needle[1].y, new_needle[2].x, new_needle[2].y, new_needle[3].x, new_needle[3].y } );
		this.graphics.setBackground( this.gray );
		this.graphics.fillOval( pivot.x, pivot.y, 6, 6 );
		this.graphics.setAntialias( SWT.OFF );
		this.graphics.drawImage( this.shiny_img, 0, 0 );
		
		this.cur_needle_pos = value;
		
	}
	
	private Point adjustCoords( Point p ) {
		
		p.x += CUDAGauge.origin;
		p.y += CUDAGauge.origin;
		return p;
		
	}
	
	private Point rotate( Point p, double[][] m2 ) {
		
		return new Point( (int)(m2[0][0] * p.x + m2[1][0] * p.y), (int)(m2[0][1] * p.x + m2[1][1] * p.y) );
		
	}
	
	public void finalize() throws Throwable {
		try {
			this.gauge_img.dispose();
			this.shiny_img.dispose();
			this.red.dispose();
			this.gray.dispose();
			this.graphics.dispose();
		} catch ( Throwable e ) {
		} finally {
			super.dispose();
		}
	}

}
