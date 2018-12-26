package wallet;

import java.util.Date;

import enumerations.TipoOperacion;

public class Ingreso extends Operacion{
	
	public Ingreso(int id, double importe, String categoria, String descripcion, Date fecha) {
		super(id, importe, categoria, descripcion, fecha, TipoOperacion.Ingreso);
	}
}
