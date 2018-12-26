package wallet;

import java.util.Date;

import enumerations.TipoOperacion;

public class Gasto extends Operacion{
	
	public Gasto(int id, double importe, String categoria, String descripcion, Date fecha) {
		super(id, importe, categoria, descripcion, fecha, TipoOperacion.Gasto);
	}
}
