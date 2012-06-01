import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;


public class CUDARecompileButton extends Button {
	
	private List< CUDAGauge > gauges;
	private MainWindow parent;
	static private File audio_file = new File( "res" + File.separator + "ding.wav" );
	
	
	public CUDARecompileButton( Composite parent, int flags ) {
		
		super( parent, flags );
		
		this.gauges = new ArrayList< CUDAGauge >();
		
		this.addMouseListener(new MouseAdapter() {
			
			CUDARecompileButton parent;
			
			public void mouseDown(MouseEvent e) {
				
				if ( this.parent == null )
					this.parent = (CUDARecompileButton)e.getSource();
				
				// set gauges to random values
				for ( CUDAGauge g : this.parent.gauges ) {
					g.moveNeedleTo( (int)(Math.random() * 100) );
				}
				
				// play sound
				try {
					AudioInputStream au = AudioSystem.getAudioInputStream( CUDARecompileButton.audio_file );
					Clip clip = AudioSystem.getClip();
					
					clip.open( au );
					clip.start();
					
				} catch ( Exception ex ) {
					// do nothing--no sound is okay, too
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
