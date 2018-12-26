package readers;

import java.io.IOException;
import java.util.ArrayList;

import enumerations.ControlLevelError;
import wallet.CSVCompatible;

public abstract class CSVReader<ClassType extends CSVCompatible> {
	private final static String CSV_EXTENSION = ".csv";
	private String header;

	public CSVReader(String header) {
		this.header = header;
	}

	public void readAll(String ruta, ArrayList<CSVCompatible> items, ControlLevelError error) {
		TextReader textReader = null;
		try {
			textReader = new BufferedTextReader(ruta + CSV_EXTENSION);
			readHeader(textReader);
			readItems(textReader, items, error);

		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		finally {
			if(textReader != null) {
				textReader.close();
			}
		}
	}
	public abstract CSVCompatible fromCSV(String line);

	private void readHeader(TextReader textReader) {
		String line = textReader.readLine();
		if(!line.equals(header)) {
			throw new IllegalArgumentException();
		}
	}
	private void readItems(TextReader textReader, ArrayList<CSVCompatible> items, ControlLevelError error) {
		String line = textReader.readLine();
		Exception exception = null;
		while(line != null && exception == null) {
			try {
				CSVCompatible csvItem = fromCSV(line);
				items.add(csvItem);
			}
			catch (Exception e) {
				System.out.println(e.getMessage());
				if(error.equals(ControlLevelError.LOAD_UNTIL_ERROR)) {
					exception = e;
				}
				else if(error.equals(ControlLevelError.NO_LOAD_WITH_ERRORS)){
					exception = e;
					items.clear();
				}
			}
			finally {
				line = textReader.readLine();
			}
		}
	}
}