package wallet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import enumerations.TipoOperacion;
import inputOutput.IniManager;
import inputOutput.Output;
import reporte.ExportadorReporteCSV;
import reporte.GeneradorReporte;
import reporte.Reporte;
import repositorioOperacion.RepositorioOperacion;

public class Wallet {

	private final static String K_PATH_CONFIG_WALLET = "src/config_walet.ini";
	private final static String K_CONFIG_WALLET = "configuracion";
	private final static String K_SALDO_WALLET = "Saldo";
	private final static String K_LIMITE_GASTOS_WALLET = "LimiteGastos";
	private final static String K_TOT_MES_GASTOS_WALLET = "TotMesGastos";
	private final static String K_FECHA_WALLET = "Fecha";
	private final static String K_DATOS_INVALIDOS = "Los datos ingresados son invalidos";
	private final static String K_OPERACION_NO_ENCONTRADA = "La operacion con el id solicitado es inexistente";
	private final static String K_SALDO_RESTANTE = "Saldo restante: $";
	private final static String K_LIMITE_SUPERADO = "Se ha superado el límite de gastos";
	private double saldo;
	private double limiteGastos;
	private double totMesGastos;
	private boolean limiteSuperado;
	private RepositorioOperacion repositorio;
	private IniManager iniManager;

	public Wallet(RepositorioOperacion repositorio) throws IllegalArgumentException, ParseException {
		iniManager = new IniManager(K_PATH_CONFIG_WALLET);
		cargarDatosWallet();
		this.repositorio = repositorio;
		validarWallet(this.saldo, this.limiteGastos, this.totMesGastos, this.repositorio);
	}
	
	public double getSaldo() {
		return saldo;
	}
	
	public void setSaldo(double saldo) {
		if(saldo > 0) {
			this.saldo = saldo;
		}
		else {
			Output.mostrarMensaje(K_DATOS_INVALIDOS);
		}
	}
	
	public void setLimiteGastos(double limiteGastos) {
		if(limiteGastos > 0) {
			this.limiteGastos = limiteGastos;
		}	
		else {
			Output.mostrarMensaje(K_DATOS_INVALIDOS);
		}
	}
	
	public double getTotMesGastos() {
		return totMesGastos;
	}
	
	public double getLimiteGastos() {
		return limiteGastos;
	}

	public void generarIngreso() {
		try {
			Operacion op = repositorio.crearOperacion(TipoOperacion.Ingreso, this.saldo);
			aumentarSaldo(op.getImporte());
		} catch (IllegalArgumentException e) {
			Output.mostrarMensaje(e.getMessage());
		}
	}

	/**
	 * Si el mes y año de la operacion ingresada es igual al mes y año
	 * actual y aumentara el total mensual de gastos con el importe de la
	 * operacion. Se disminuirá el saldo (tomando el importe de la
	 * operacion). Se verificará si se superó el limite mensual de gastos.
	 */
	public void generarGasto() {
		try {
			Operacion op = repositorio.crearOperacion(TipoOperacion.Gasto, this.saldo);
			if(op != null) {
				Date fechaAct = new Date();
				if (compararMesAnio(fechaAct, op.getFecha())) {
					aumentarTotMesGastos(op.getImporte());
				}
				disminuirSaldo(op.getImporte());
				limiteSuperado();
			}
		} catch (IllegalArgumentException e) {
			Output.mostrarMensaje(e.getMessage());
		}
	}

	/**
	 * Si el mes y año de la operacion ingresada es igual al mes y año
	 * actual y aumentara el total mensual de gastos con el importe de la
	 * operacion. Se disminuirá el saldo (tomando el importe de la
	 * operacion). Se verificará si se superó el limite mensual de gastos.
	 */
	public void modificarImporteOperacion(int id){
		try {
			Operacion op = repositorio.getOperacion(id);
			if (op != null) {
				double importeAnt = op.getImporte();
				double nuevoImporte = repositorio.modificarImporteOp(id, op.getImporte() + this.saldo);
				boolean esMesAnioIgual = compararMesAnio(op.getFecha(), new Date());
				if (op.getTipo().equals(TipoOperacion.Gasto)) {
					if (nuevoImporte > importeAnt) {
						disminuirSaldo(nuevoImporte - importeAnt);
						if (esMesAnioIgual) {
							aumentarTotMesGastos(nuevoImporte - importeAnt);
						}
					}
					else if (nuevoImporte < importeAnt){
						aumentarSaldo(importeAnt - nuevoImporte);
						if (esMesAnioIgual) {
							disminuirTotMesGastos(importeAnt - nuevoImporte); 
						}
					} else {
						if (nuevoImporte > importeAnt) {
							aumentarSaldo(nuevoImporte - importeAnt);
						}
						else if (nuevoImporte < importeAnt){
							disminuirSaldo(importeAnt - nuevoImporte);
						}
					}
				}
			}
		}
		catch (IllegalArgumentException e) {
			Output.mostrarMensaje(e.getMessage());
		}
	}

	public void modificarCategoriaOperacion(int id) {
		try {
			repositorio.modificarCategoriaOp(id);
		} catch (IllegalArgumentException e) {
			Output.mostrarMensaje(e.getMessage());
		}
	}

	public void modificarDescOperacion(int id) {
		try {
			repositorio.modificarDescripcionOp(id);
		} catch (IllegalArgumentException e) {
			Output.mostrarMensaje(e.getMessage());
		}	
	}

	public void modificarFechaOperacion(int id) {
		/*
		 * si la operacion es un gasto se debe verificar la posibilidad de que
		 * que se deba modificar el total mensual de gastos comparando la la
		 * fecha original del gasto, la fecha actual y la fecha solicitada para
		 * modificar
		 */
		try {
			Operacion op = repositorio.getOperacion(id);
			if (op != null) {
				Date fechaAnt = op.getFecha();
				Date fechaNueva = repositorio.modificarFechaOp(id);
				if (op.getTipo().equals(TipoOperacion.Gasto)) {
					actualizarTotalMenGastos(fechaAnt, fechaNueva, op.getImporte());
				}
			}
		} catch (IllegalArgumentException e) {
			Output.mostrarMensaje(e.getMessage());
		}
	}

	/**
	 * Elimina la operacion correspondiente al id del parametro.
	 * Si la operacion es un gasto se aumentará el saldo por el
	 * importe del gasto y si el mes y año de la fecha del gasto es
	 * igual al mes y año de la fecha actual se disminiurá el total
	 * mensual de gastos. Si la operacion es un ingreso se disminuirá
	 * el saldo por el importe del ingreso.
	 * @param id el id de la operacion a eliminar
	 */
	public void eliminarOperacion(int id) {
		Operacion op = repositorio.eliminarOperacion(id);
		if(op != null) {
			double importe = op.getImporte();
			if (op.getTipo().equals(TipoOperacion.Gasto)) {
				aumentarSaldo(importe);
				Date fechaAct = new Date();
				if (compararMesAnio(op.getFecha(), fechaAct)) {
					disminuirTotMesGastos(importe);
				}
			} else {
				disminuirSaldo(importe);
			}
		}
		else {
			Output.mostrarMensaje(K_OPERACION_NO_ENCONTRADA);
		}
	}

	public void generarReporteGastos(int mesDesde, int mesHasta, int anioDesde, int anioHasta) {
		GeneradorReporte genRep = new GeneradorReporte(mesDesde, mesHasta, anioDesde, anioHasta);
		ArrayList<Operacion> listaGastos = repositorio.getListaGastos();
		if (!listaGastos.isEmpty()) {
			Reporte rep = genRep.generarReporte(listaGastos);
			new ExportadorReporteCSV(rep);
		}
	}

	public void guardaDatosWallet() {
		Date fechaActual = new Date();
		String fechaString = (fechaActual.getYear()+1900) + "-" + (fechaActual.getMonth()+1) + "-" + fechaActual.getDate();
		iniManager.setItem(K_CONFIG_WALLET, K_SALDO_WALLET, String.valueOf(this.saldo));
		iniManager.setItem(K_CONFIG_WALLET, K_LIMITE_GASTOS_WALLET, String.valueOf(this.limiteGastos));
		iniManager.setItem(K_CONFIG_WALLET, K_TOT_MES_GASTOS_WALLET, String.valueOf(this.totMesGastos));
		iniManager.setItem(K_CONFIG_WALLET, K_FECHA_WALLET, fechaString);
		iniManager.save();
	}

	public void informarOperaciones() {
		repositorio.informarOperaciones();
	}

	public void cerrarRepositorio(){
		repositorio.cerrar();
	}

	/**
	 * si exite limite de gastos verifica si este fue alcanzado (solo en caso de aun no haberlo hecho). 
	 * Si fue alcanzad lo informara, e informará el saldo restante
	 */
	private void limiteSuperado() {
		if (this.limiteGastos > 0 && this.totMesGastos >= this.limiteGastos && this.limiteSuperado == false) {
			this.limiteSuperado = true;
			Output.mostrarMensaje(K_LIMITE_SUPERADO);
			Output.mostrarMensaje(K_SALDO_RESTANTE + this.saldo);
		}
	}
	
	private void cargarDatosWallet() throws ParseException {
		String saldo = iniManager.getValueOf(K_CONFIG_WALLET, K_SALDO_WALLET);
		String fecha = iniManager.getValueOf(K_CONFIG_WALLET, K_FECHA_WALLET);
		String limiteGastos = iniManager.getValueOf(K_CONFIG_WALLET, K_LIMITE_GASTOS_WALLET);
		String totMesGastos = iniManager.getValueOf(K_CONFIG_WALLET, K_TOT_MES_GASTOS_WALLET);
		SimpleDateFormat sdfFecha = new SimpleDateFormat("yyyy-MM-dd"); 
		if(saldo != null && fecha != null && limiteGastos != null && totMesGastos != null) {
			Date fechaActual = new Date();
			Date fechaGuardada = sdfFecha.parse(fecha);
			this.saldo = Double.parseDouble(saldo);
			this.limiteGastos = Double.parseDouble(limiteGastos);
			if(compararMesAnio(fechaActual, fechaGuardada)) {
				this.totMesGastos = Double.parseDouble(totMesGastos);
			}
			limiteSuperado();
		}	
	}

	private void validarWallet(double saldo, double limiteGastos, double totMesGastos, RepositorioOperacion repositorio) throws IllegalArgumentException {
		if (saldo < 0 || limiteGastos < 0 || totMesGastos < 0 || repositorio == null) {
			throw new IllegalArgumentException(K_DATOS_INVALIDOS);
		}
	}

	/**
	 * actualiza el total de gastos mensuales para gastos cuya mes y año original o 
	 * el nuevo mes y año ingresado concidan con el mes y año actual  
	 * @param fechaAnt la fecha original del operacion
	 * @param fechaNueva la fecha a modificar ingresada
	 * @param importe de la operacion
	 */
	private void actualizarTotalMenGastos(Date fechaAnt, Date fechaNueva, double importe) {
		Date fechaAct = new Date();
		if(!compararMesAnio(fechaAnt, fechaNueva)) {
			if(!compararMesAnio(fechaAnt, fechaAct)) {
				if(compararMesAnio(fechaNueva, fechaAct)) {
					aumentarTotMesGastos(importe);
				}
			}
			else {
				if(!compararMesAnio(fechaNueva, fechaAct)) {
					disminuirTotMesGastos(importe);
				}
			}
		}
	}

	private boolean compararMesAnio(Date fecha1, Date fecha2) {
		boolean esIgual = false;
		int mesfecha1 = fecha1.getMonth();
		int aniofecha1 = fecha1.getYear();
		int mesfecha2 = fecha2.getMonth();
		int aniofecha2 = fecha2.getYear();
		if(aniofecha1 == aniofecha2 && mesfecha1 == mesfecha2) {
			esIgual = true;
		}
		return esIgual;
	}

	private void aumentarTotMesGastos(double importe) {
		this.totMesGastos += importe;
	}

	private void disminuirTotMesGastos(double importe) {
		this.totMesGastos -= importe;
	}

	private void aumentarSaldo(double importe) {
		this.saldo += importe;
	}

	private void disminuirSaldo(double importe) {
		this.saldo -= importe;
	}
}
