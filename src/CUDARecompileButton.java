import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;


public class CUDARecompileButton extends Button {
	
	public List< CUDAGauge > gauges;
	
	public CUDARecompileButton( Composite parent, int flags ) {
		
		super( parent, flags );
		
		this.gauges = new ArrayList< CUDAGauge >();
		
		this.addMouseListener(new MouseAdapter() {
			
			CUDARecompileButton parent;
			
			public void mouseDown(MouseEvent e) {
				
				if ( this.parent == null )
					this.parent = (CUDARecompileButton)e.getSource();
				
				for ( CUDAGauge g : this.parent.gauges ) {
					g.moveNeedleTo( (int)(Math.random() * 100) );
				}
				
			}
			
		});
		
	}
	
	public void addGauge( CUDAGauge new_gauge ) {
		
		this.gauges.add( new_gauge );
		
	}
	
	protected void checkSubclass() {
		// **override** this is here so SWT lets us override the button -- do nothing
	}
	
}
