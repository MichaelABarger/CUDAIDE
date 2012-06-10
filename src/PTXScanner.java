import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class PTXScanner {
	public CUDAPTXLine line[];
	String source[];
	int sourceLine[];
	private int upperBound;
	private int lowerBound;

	PTXScanner() {
	}
	
	public int lines()
	{
		return line.length;
	}
	
	public String getArg(int ln, int in, int an)
	{
		if(lnLookup(ln) != null){
			if(an == 0) return lnLookup(ln).getInstruction(in);
			else return lnLookup(ln).getArg(in, an);
		}
		return null;
	}
	
	private void setScope()
	{
		int b = 0, c = 0, d = 0, e;
		for(int i = 0; i < line.length; i++){
			if(line[i] != null) b = line[i].getLineNumber();
			if (b > c) c = b;
		}
		upperBound = c;
		e = upperBound;
		for(int i = 0; i < line.length; i++){
			if(line[i] != null)	d = line[i].getLineNumber();
			if (d < e && d != 0) e = d;
		}
		lowerBound = e;
	}
	
	public boolean inScope(int a)
	{
		if(a < lowerBound) return false; 
		else if(a > upperBound) return false;
		else return true;
	}
	
	public int instructions (int ln)
	{
		if(ln > line.length) return 0;
		if(lnLookup(ln) == null)
			return 0;
		else
			return lnLookup(ln).instructions();
	}
	
	public CUDAPTXLine lnLookup(int ln)
	{
		if(ln > line.length) return null;
		for(int i = 0; i < ln; i++){
			if(line[i].checkLineNumber(ln)){
				return line[i];
			}
		}
		return null;
	}
	
	int numberOfLines(String filename) throws FileNotFoundException,
			IOException {
		BufferedReader in = new BufferedReader(new FileReader(filename));
		int i = 0;
		while (in.ready()) {
			in.readLine();
			i++;
		}
		return i;
	}

	void ReadInCommentedPTX(String filename) throws FileNotFoundException,
			IOException {
//		System.out.println("Reading in commented PTX file" + filename);
		BufferedReader in = new BufferedReader(new FileReader(filename));
		String buffer;
		String PTXBuffer[] = new String[1000];
		CUDAPTXLine temp;
		boolean header = true;
		int i = 0;
		int j = 0;
		while (in.ready()) {
			buffer = in.readLine();
			if (header) {
				if (buffer.contains("(Report advisories)")) {
					header = false;
					in.readLine();
				}
			} else {
				if (!buffer.contains("//")) {
					if (line[i] == null)
						line[i] = new CUDAPTXLine();
					PTXBuffer[j] = buffer;
					j++;
				} else {
					line[i].declarePTXInstructionCount(j);
					for (int k = 0; k < j; k++) {
						line[i].addPTXInstruction(PTXBuffer[k]);
					}
					j = 0;
					i++;
					temp = new CUDAPTXLine(buffer);
					line[i] = temp;
				}
			}
		}
	}

	void ReadInCu(String filename) throws FileNotFoundException, IOException {
		BufferedReader in = new BufferedReader(new FileReader(filename));
		String buffer;
		int i = 0;
		while (in.ready()) {
			buffer = in.readLine();
			source[i] = buffer;
			connectSource(i, source[i]);
			i++;
		}
	}

	void connectSource(int f, String g) {
		int i;
		for (i = 0; i < line.length; i++) {
			if (line[i] != null)
				if (line[i].locateSource(f))
					line[i].normalizeSource(g);
		}
	}

	public void print() {
		System.out.println("Printing:");
		for (int i = 0; i < line.length; i++){
			if (line[i] != null) {
				line[i].printSource();
			}
		}
		for (int i = 0; i < line.length; i++){
			if (line[i] != null) {
				line[i].printPTX();
			}
		}
	}
	
	public void readIn(String CUFilename, String PTXFilename) {
		try {
			line = new CUDAPTXLine[numberOfLines(PTXFilename)];
			ReadInCommentedPTX(PTXFilename);
			source = new String[numberOfLines(CUFilename)];
			ReadInCu(CUFilename);	
		} catch (FileNotFoundException f) {
			System.out.println("No such file. Exiting.");
		} catch (IOException e) {
			System.out.println("IO error: " + e.getMessage() + ". Exiting");
		}
		for(int i = 0; i < line.length; i++){
		if(line[i] != null)
			line[i].splitInstructions();
		}
		setScope();
	}
	
	public static void main(String args[]) {
		PTXScanner q = new PTXScanner();

		q.readIn(args[0], args[1]);
		
		q.print();
	}
}
