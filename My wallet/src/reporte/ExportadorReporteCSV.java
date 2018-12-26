package reporte;

import writers.CSVWriter;

public class ExportadorReporteCSV {
	private final static String K_PATH_REPORTE = "src/";
	private final static String K_FILENAME = "%sReporte %s - %s al %s - %s";
	private final static String K_MES = "AÑO; ENERO; FBERERO; MARZO; ABRIL; MAYO; JUNIO; JULIO; AGOSTO; SEPTIEMRE; OCTUBRE; NOVIEMBRE; DICIEMBRE";
	private CSVWriter<CSVOut> csvWriter;
	private Reporte reporte;
	
	public ExportadorReporteCSV(Reporte reporte) {
		csvWriter = new CSVWriter<>(K_MES);
		this.reporte = reporte;
		guardarReporte();
	}
	
	private String crearPath() {
		return String.format(K_FILENAME, K_PATH_REPORTE, reporte.getMesDesde(), reporte.getAnioDesde(), reporte.getMesHasta(), reporte.getAnioHasta());
	}

	private void guardarReporte() {
		String path = crearPath();
		csvWriter.writeAll(path, reporte.getListaCsvOut());
	}
}
