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
	
	private MainWindow parent;
	
	public CUDARecompileButton( Composite parent, int flags ) {
		
		super( parent, flags );
		
	}
	
	protected void checkSubclass() {
		// **override** this is here so SWT lets us override the button -- do nothing
	}
	
}
