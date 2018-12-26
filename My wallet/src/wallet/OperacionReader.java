package wallet;

import java.util.Date;

import enumerations.TipoOperacion;
import readers.CSVReader;

public class OperacionReader extends CSVReader{
	
	private final static int K_CANT_ATRIBUTOS = 6;

	public OperacionReader(String header) {
		super(header);
	}

	@Override
	public CSVCompatible fromCSV(String line) {
		Operacion operacion = null;
		String[] atributos = line.split(";");
		if(atributos.length == K_CANT_ATRIBUTOS) {
			int id = Integer.parseInt(atributos[0]);
			Date fecha = cargarFecha(atributos[1]);
			double importe = Double.parseDouble(atributos[3]);
			String categoria = atributos[4];
			String descripcion = atributos[5];	
			TipoOperacion tipoOperacion = null;
			String tipo = String.valueOf(TipoOperacion.Gasto);
			if(atributos[2].equals(tipo)) {
				tipoOperacion = TipoOperacion.Gasto;
			}
			else {
				tipoOperacion = TipoOperacion.Ingreso;
			}
			operacion = new Operacion(id, importe, categoria, descripcion, fecha, tipoOperacion);
		}
		return operacion;
	}

	private Date cargarFecha(String fecha) {
		String[] datosFecha = fecha.split("-");
		int anio = Integer.parseInt(datosFecha[0]);
		int mes = Integer.parseInt(datosFecha[1]);
		int dia = Integer.parseInt(datosFecha[2]);
		return new Date(anio - 1900, mes - 1, dia);
	}

}
