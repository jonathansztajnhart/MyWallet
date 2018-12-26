package reporte;

import java.util.ArrayList;

public class Reporte {
	private ArrayList<CSVOut> listaCsvOut;
	private double totGen;
	private int mesDesde;
	private int mesHasta;
	private int anioDesde;
	private int anioHasta;
	private int cantAnio;
	
	public Reporte(int mesDesde, int mesHasta, int anioDesde, int anioHasta) {
		cantAnio = anioHasta - anioDesde + 1;
		this.listaCsvOut = new ArrayList<>();
		this.mesDesde = mesDesde;
		this.mesHasta = mesHasta;
		this.anioDesde = anioDesde;
		this.anioHasta = anioHasta;
	}
	
	public void cargarTotalesAnio(int anio, double[] importesMensuales) {
		CSVOut csvOut = new CSVOut(String.valueOf(anio), importesMensuales);
		this.listaCsvOut.add(csvOut);
	}
	
	public void cargarTotalGeneral(){
		double[] importeTotal = new double[1];
		importeTotal[0] = this.totGen;
		CSVOut csvOut = new CSVOut(String.valueOf("Total: "), importeTotal);
		this.listaCsvOut.add(csvOut);
	}
	public void setTotGen(double totGen) {
		this.totGen = totGen;
	}

	public ArrayList<CSVOut> getListaCsvOut() {
		return listaCsvOut;
	}

	public void setListaCsvOut(ArrayList<CSVOut> listaCsvOut) {
		this.listaCsvOut = listaCsvOut;
	}

	public double getTotGen() {
		return totGen;
	}

	public int getMesDesde() {
		return mesDesde;
	}

	public int getMesHasta() {
		return mesHasta;
	}
	
	public int getAnioDesde() {
		return anioDesde;
	}
	
	public int getAnioHasta() {
		return anioHasta;
	}

	public int getCantAnio() {
		return cantAnio;
	}
	
}
