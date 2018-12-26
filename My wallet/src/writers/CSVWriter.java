package writers;

import java.io.IOException;
import java.util.ArrayList;

import wallet.CSVCompatible;


public class CSVWriter<ClassType extends CSVCompatible> {
	private final static String CSV_EXTENSION = ".csv";
	private String header;
	
	public CSVWriter(String header){
		this.header = header;
	}
	public void writeAll(String ruta, ArrayList<ClassType> items) {
		TextWriter textWriter = null;
		try {
			textWriter = new BufferedTextWriter(ruta + CSV_EXTENSION);
			writeHeader(textWriter);
			writeItems(textWriter, items);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		finally {
			if(textWriter!= null) {
				textWriter.close();
			}
		}
	}
	private void writeHeader(TextWriter textWriter) {
		textWriter.writeLine(header);
	}
	private void writeItems(TextWriter textWriter, ArrayList<ClassType> items) {
		for (CSVCompatible item : items) {
			textWriter.writeLine(item.toCSV());
		}
		
	}
}
