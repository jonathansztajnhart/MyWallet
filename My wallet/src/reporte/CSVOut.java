package reporte;

import wallet.CSVCompatible;

public class CSVOut implements CSVCompatible{
	private String columna1;
	private double[] importes;
	
	
	public CSVOut(String columna1, double[] impteMensuales) {
		this.columna1 = columna1;
		this.importes = impteMensuales;
	}
	
	@Override
	public String toCSV() {
		String line = columna1;
		for (int i = 0; i < importes.length; i++) {
			line += String.format(";%2.2f", importes[i]);
		}
		return line;
	}
}
