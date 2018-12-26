package reporte;

import java.util.ArrayList;

import wallet.Operacion;

public class GeneradorReporte {
	private int mesDesde;
	private int mesHasta;
	private int anioDesde;
	private int anioHasta;
	private Reporte reporte;

	public GeneradorReporte(int mesDesde, int mesHasta, int anioDesde, int anioHasta) {
		this.mesDesde = mesDesde;
		this.mesHasta = mesHasta;
		this.anioDesde = anioDesde;
		this.anioHasta = anioHasta;
		this.reporte = new Reporte(mesDesde, mesHasta, anioDesde, anioHasta);
	}

	public Reporte generarReporte(ArrayList<Operacion> listaOperacion) {
		int totGen = 0;
		int i = 0;
		Operacion op;
		while(i < listaOperacion.size()) {
			op = listaOperacion.get(i);
			int anioLeido = obtenerAnio(op);
			int mesLeido = obtenerMes(op);
			int anioAnt = anioLeido;
			double[] importesMensuales = new double[12];
			while(i < listaOperacion.size() && anioAnt == anioLeido) {
				int totMes = 0;
				int mesAnt = mesLeido;
				while(i < listaOperacion.size() && anioAnt == anioLeido && mesAnt == mesLeido){
					if(anioLeido >= anioDesde && anioLeido <= anioHasta && !((anioAnt == anioDesde && mesAnt < mesDesde - 1) || (anioAnt == anioHasta && mesAnt > mesHasta - 1))) {
						/*si el año esta dentro del rango y:
						 * 1) es igual al desde solo me sirven los meses mayores (o el mes igual) al desde
						 * 2) es igual al hasta solo me sirven los meses menores (o el mes igual) al hasta
						 */
						totMes += op.getImporte();
					}
					i++;
					if(i < listaOperacion.size()) {
						op = listaOperacion.get(i);
						anioLeido = obtenerAnio(op);
						mesLeido = obtenerMes(op);
					}
				}
				importesMensuales[mesAnt] = totMes;

				totGen += totMes;
			}
			if(anioAnt >= anioDesde && anioAnt <= anioHasta) {
				reporte.cargarTotalesAnio(anioAnt, importesMensuales);
			}
		}
		reporte.setTotGen(totGen);
		reporte.cargarTotalGeneral();
		return reporte;
	}


	private int obtenerAnio(Operacion op) {
		return op.getFecha().getYear() + 1900;	
	}
	private int obtenerMes(Operacion op) {
		return op.getFecha().getMonth();	
	}

}

