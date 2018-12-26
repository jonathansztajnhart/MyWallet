package repositorioOperacion;

import java.util.ArrayList;
import java.util.Date;

import enumerations.TipoOperacion;
import wallet.Operacion;

public interface RepositorioOperacion {
	Operacion getOperacion(int id);
	public ArrayList<Operacion> getListaGastos();
	public void informarOperaciones();
	Operacion crearOperacion(TipoOperacion tipo, double topeImporte);
	Operacion eliminarOperacion(int id);
	double modificarImporteOp(int id, double topeImporte);
	void modificarCategoriaOp(int id);
	void modificarDescripcionOp(int id);
	Date modificarFechaOp(int id);
    void cerrar();
}
