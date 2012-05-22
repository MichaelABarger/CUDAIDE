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

import java.io.*;
import java.util.StringTokenizer;
class Line{
	String commentedSource;
	String PTXInstruction[];
	int numPTXInstructions;
	Line(){
		numPTXInstructions = 0;
		commentedSource = null;
	}
	Line(String f){
		commentedSource = f;
		numPTXInstructions = 0;
	}
	void declareOptimizationLevel(int level){
}
	void declarePTXInstructionCount(int count){
		PTXInstruction = new String[count];
	}
	void addPTXInstruction(String f){
		PTXInstruction[numPTXInstructions] = f;
		numPTXInstructions++;
	}
	void print(){
		if(commentedSource != null) System.out.println(commentedSource);
		for(int i = 0; i < numPTXInstructions; i++){
			System.out.println(PTXInstruction[i]);
		}
	}
}

class Scanner{
	Line opt3[];
	Scanner() {}
	int numberOfLines(String filename) throws FileNotFoundException, IOException{
		BufferedReader in = new BufferedReader(new FileReader(filename));
		int i = 0;
		while(in.ready()){
			in.readLine();
			i++;
		}
		return i;
	}
	void ReadIn(String filename) throws FileNotFoundException, IOException{
		BufferedReader in = new BufferedReader(new FileReader(filename));
		String buffer;
		String PTXBuffer[] = new String[1000];
		Line temp;
		boolean header = true;
		int i = 0; 
		int j = 0;
		while(in.ready()){
			buffer = in.readLine();
			if(header){
				if(buffer.contains("(Report advisories)")) {
					header = false;
					in.readLine();
				}
			} else {
				if(!buffer.contains("//")){
					if(opt3[i] == null) opt3[i] = new Line();
					PTXBuffer[j] = buffer;
					j++;
				} else {
					opt3[i].declarePTXInstructionCount(j);
					for(int k = 0; k < j; k++){
						opt3[i].addPTXInstruction(PTXBuffer[k]);
					}
					j = 0;
					i++;
					temp = new Line(buffer);
					opt3[i] = temp;
				}
			}
		}
	}
	void print(){
		for(int i = 0; i < opt3.length; i++)
			if(opt3[i] != null) opt3[i].print();
	}
	public static void main(String args[]){
		Scanner q = new Scanner();

		try{
			q.opt3 = new Line[q.numberOfLines(args[0])];
			q.ReadIn(args[0]);
		} catch (FileNotFoundException f) {
			System.out.println("No such file. Exiting.");
		} catch (IOException e) {
			System.out.println("IO error: "+e.getMessage()+". Exiting");
		}
		q.print();
	}
}
