import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;

/*Scanner for Compiled PTX with commented source
 *For group 4B: Visual Profiling of CUDA Code:
 *
 *This is a simple backend scanner to read compiled PTX with commented source
 *and put it into a data object that contains PTX instructions and associated
 *CUDA source. This will be used for an IDE-like interface wherein source code
 *is shown alongside the compiled PTX form when completed. It takes a file in
 *the appropriate format as an argument.
 *
 *Copyright 2012 Alex Kelly
 */

class CUDAPTXLine {
	String commentedSource;
	String uncommentedSource;
	String PTXInstruction[];
	int Cycles[];
	Integer Cycle_Sum = 0;
	String InstructionToken[];
	String Arg1[];
	String Arg2[];
	String Arg3[];
	int lineNumber;
	int numPTXInstructions;
	
	private static Hashtable<String, Integer> inst_cycles = new Hashtable<String, Integer>();

	static {
		
		inst_cycles.put( "add.u", new Integer(24) );
		inst_cycles.put( "sub.u", new Integer(24) );
		inst_cycles.put( "max.u", new Integer(24) );
		inst_cycles.put( "min.u", new Integer(24) );
		inst_cycles.put( "add.s", new Integer(24) );
		inst_cycles.put( "sub.s", new Integer(24) );
		inst_cycles.put( "max.s", new Integer(24) );
		inst_cycles.put( "min.s", new Integer(24) );
		inst_cycles.put( "mad.u", new Integer(120) );
		inst_cycles.put( "mad.s", new Integer(120) );
		inst_cycles.put( "mul.wide.u", new Integer(96) );
		inst_cycles.put( "mul.wide.s", new Integer(96) );
		inst_cycles.put( "mul.lo.u", new Integer(96) );
		inst_cycles.put( "mul.lo.s", new Integer(96) );
		inst_cycles.put( "div.u", new Integer(608) );
		inst_cycles.put( "div.s", new Integer(684) );
		inst_cycles.put( "rem.u", new Integer(728) );
		inst_cycles.put( "rem.s", new Integer(784) );
		inst_cycles.put( "and.u", new Integer(24) );
		inst_cycles.put( "or.u", new Integer(24) );
		inst_cycles.put( "xor.u", new Integer(24) );
		inst_cycles.put( "shl.u", new Integer(24) );
		inst_cycles.put( "shr.u", new Integer(24) );
		inst_cycles.put( "add.f", new Integer(24) );
		inst_cycles.put( "sub.f", new Integer(24) );
		inst_cycles.put( "max.f", new Integer(24) );
		inst_cycles.put( "min.f", new Integer(24) );
		inst_cycles.put( "mad.f", new Integer(24) );
		inst_cycles.put( "mul.wide.f", new Integer(24) );
		inst_cycles.put( "mul.lo.f", new Integer(24) );
		inst_cycles.put( "div.f", new Integer(137) );
		inst_cycles.put( "add.d", new Integer(48) );
		inst_cycles.put( "sub.d", new Integer(48) );
		inst_cycles.put( "max.d", new Integer(48) );
		inst_cycles.put( "min.d", new Integer(48) );
		inst_cycles.put( "mad.d", new Integer(48) );
		inst_cycles.put( "mul.wide.d", new Integer(48) );
		inst_cycles.put( "mul.lo.d", new Integer(48) );
		inst_cycles.put( "div.d", new Integer(1366) );
		inst_cycles.put( "rsqrt.f", new Integer(28) );
		inst_cycles.put( "sqrt.f", new Integer(56) );
		inst_cycles.put( "ld.global", new Integer(443) );
	}
	
	CUDAPTXLine() {
		numPTXInstructions = 0;
		commentedSource = null;
	}

	CUDAPTXLine(String f) {
		commentedSource = f;
		numPTXInstructions = 0;
	}

	void declarePTXInstructionCount(int count) {
		PTXInstruction = new String[count];
		Cycles = new int[count];
		InstructionToken = new String[count];
		Arg1 = new String[count];
		Arg2 = new String[count];
		Arg3 = new String[count];
	}

	void addPTXInstruction(String f) {
		PTXInstruction[numPTXInstructions] = f;
		numPTXInstructions++;
	}

	boolean locateSource(int f) {
		if (commentedSource != null
				&& commentedSource.contains("//  " + Integer.toString(f))) {
			lineNumber = f;
			return true;
		} else
			return false;
	}

	void normalizeSource(String f) {
		uncommentedSource = f;
	}

	public void printSource() {
		if (uncommentedSource != null)
			System.out.println(uncommentedSource);
		else if (commentedSource != null)
			System.out.println(commentedSource);
	}

	public void printPTX() {

		for (int i = 0; i < numPTXInstructions; i++) {
			System.out.println(PTXInstruction[i]);
			}
	}

	public void printInline() {
		if (commentedSource != null)
			System.out.println(commentedSource);
		printPTX();
	}
	
	boolean checkLineNumber(int i){
		if(i == lineNumber) return true;
		else return false;
	}
	
	public String getInstruction(int q){
		return InstructionToken[q];
	}
	
	public String getArg(int q, int argNum){
		switch(argNum){
		case 0:
			return Integer.toString(Cycles[q]);
		case 1:
			return InstructionToken[q];
		case 2:
			return Arg1[q];
		case 3:
			return Arg2[q];
		case 4:
			return Arg3[q];
		default:
			return null;
		}
	}
	
	public int instructions()
	{
		if(numPTXInstructions == 0) return 0;
		else return PTXInstruction.length;
	}
	
	void splitInstructions(){
		String[] Token;
		if(numPTXInstructions == 0);
		else{
			for(int i = 0; i < PTXInstruction.length; i++){
				Token = PTXInstruction[i].split("\\s");
				for(int j = 0; j < Token.length; j++){
					if(Token[j].length() > 0){
						if(InstructionToken[i] == null)	InstructionToken[i] = Token[j];
						else if(Arg1[i] == null) Arg1[i] = Token[j];
						else if(Arg2[i] == null) Arg2[i] = Token[j];
						else if(Arg3[i] == null) Arg3[i] = Token[j];
					}
					
				}
				if ( InstructionToken[i] != null) {
					Enumeration<String> keys = CUDAPTXLine.inst_cycles.keys();
					while ( keys.hasMoreElements() ) {
						String this_element = keys.nextElement();
						if ( InstructionToken[i].contains( this_element ) ) {
							Cycles[i] = CUDAPTXLine.inst_cycles.get( this_element );
							System.out.println(InstructionToken[i] + ": " + Cycles[i]);
						}
					} 
					if ( Cycles[i] == 0 )
						Cycles[i] = 24;
					Cycle_Sum += Cycles[i];
				}
			}
		}	
	}
	
	int getLineNumber(){
		return lineNumber;
	}
}