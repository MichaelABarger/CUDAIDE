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
	String InstructionToken[];
	String Arg1[];
	String Arg2[];
	String Arg3[];
	int lineNumber;
	int numPTXInstructions;

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
		case 1:
			return Arg1[q];
		case 2:
			return Arg2[q];
		case 3:
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
				int offset = 0;
				for(int j = 0; j < Token.length; j++){
					if(Token[j].length() > 0){
						if(InstructionToken[i] == null)	InstructionToken[i] = Token[j];
						else if(Arg1[i] == null) Arg1[i] = Token[j];
						else if(Arg2[i] == null) Arg2[i] = Token[j];
						else if(Arg3[i] == null) Arg3[i] = Token[j];
					} 
				}
			}
		}	
	}
	
int getLineNumber(){
		return lineNumber;
	}
}