import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Composite;




public final class CUDACode extends StyledText {

	ArrowCanvas c;
	
	public CUDACode(Composite parent, int style) {
		super(parent, style);
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

}
