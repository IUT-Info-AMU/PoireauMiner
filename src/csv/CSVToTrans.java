package csv;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class CSVToTrans {
	private String fileName;
	private CSVFileWriter csvWriter;
	private CSVFileReader csvReader;
	private ArrayList<String> dictionnary;

	public CSVToTrans(String fileName) {
		this.fileName = fileName;
		this.csvReader = new CSVFileReader(";");
		this.csvWriter = new CSVFileWriter(";");
		this.dictionnary = new ArrayList<String> ();
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public void translateCSVToTrans (){	
		try {
				
			BufferedWriter writer = new BufferedWriter (new OutputStreamWriter (new FileOutputStream (this.fileName+".trans")));
			this.csvReader.open(this.fileName);
			List<String> lineRead = new ArrayList<String>();
			
			while ((lineRead = this.csvReader.readLine()) != null) {
				String lineWrite = "";
				for (String word : lineRead) {
					System.out.println(word);
					int index = IsInDictionnary(word);
					lineWrite += (new Integer (index)).toString() + " ";
				}
				writer.write(lineWrite);
				writer.newLine();
			}
			writer.close();
		
		}		
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void translateFileToCSV(){
		try {
			BufferedReader reader =  new BufferedReader (new InputStreamReader (new FileInputStream(this.fileName)));
			String line;
			
			while ((line = reader.readLine()) != null) {
				List<String> lineWrite = new ArrayList<String>();
				for (int i = 0; i < line.length(); ++i) {						
					
					if (line.charAt(i) != ' ' && line.charAt(i) != '(') {
						int indexEnd = i;
						for (; line.charAt(indexEnd) != ' ';++indexEnd) {
							
						}
						int index = Integer.parseInt(line.substring(i, indexEnd));
						i = indexEnd;
						lineWrite.add(this.dictionnary.get(index));
						System.out.print(index + " : ");
						System.out.println(this.dictionnary.get(index));
					}				
					if (line.charAt(i) == '(') {
						++i;
						int indexEnd = i;
						for (; line.charAt(indexEnd) != ')';++indexEnd) {
							
						}
						lineWrite.add("(" + new Integer(Integer.parseInt(line.substring(i, indexEnd))).toString() + ")");						
						i = line.length();
					}
				}
				System.out.println(lineWrite);
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public int IsInDictionnary(String wordToSearch){
		int index = -1;
		boolean find = false;
		for(int i = 0; i < this.dictionnary.size(); ++i){
			if(wordToSearch.equals(this.dictionnary.get(i))){
				index = i;
				find = true;
			}	
		}
		if(!find){
			this.dictionnary.add(wordToSearch);
			index = this.dictionnary.size()-1;
		}
		return index;
		
	}
	public static void main(String[] args) {
		CSVToTrans translate = new CSVToTrans("pokemon.csv");
		translate.translateCSVToTrans();
		try {
			Process proc = Runtime.getRuntime().exec("/home/p13006381/Bureau/twitMiner/apriori pokemon.csv.trans 200 pokemon.out");
		} catch (IOException e) {
			e.printStackTrace();
		}
		translate.setFileName("pokemon.out");
		translate.translateFileToCSV();
	}
}
