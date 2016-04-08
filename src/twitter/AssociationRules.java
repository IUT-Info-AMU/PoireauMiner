package twitter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class AssociationRules {
	private double minConf;
	private String fileName;
	
	public AssociationRules(String fileName,double minConf){
		this.minConf = minConf;
		this.fileName = fileName;
	}
	
	public void generateRules(){
		try {
			BufferedReader outFile =  new BufferedReader (new InputStreamReader (new FileInputStream(this.fileName + ".out")));
			BufferedWriter writer = new BufferedWriter (new OutputStreamWriter (new FileOutputStream (this.fileName+".associationRules.out")));
			String line;
			while ((line = outFile.readLine()) != null) {
				BufferedReader outFile2 = new BufferedReader (new InputStreamReader (new FileInputStream(this.fileName + ".out")));
				String line2;
				while ((line2 = outFile2.readLine()) != null) {
					double freq = 0.0;
					double freq2 = 0.0;
					if(line.contains(line2) && (!line.equals(line2))){
						freq = Double.parseDouble(line.substring(line.indexOf("(") + 1, line.indexOf(")")));
						freq2 = Double.parseDouble(line2.substring(line.indexOf("(")+1, line.indexOf(")")));
						double conf = freq/freq2;
						if(conf <= this.minConf){
							System.out.println(line + " " + line2 + " line contient line2");
							writer.write(line + " -> " + line2);
							writer.newLine();
						}
					}
				}
				
			}
			writer.close();
			outFile.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		AssociationRules associationRules = new AssociationRules("pokemon",10.5);
		associationRules.generateRules();
	}
}
