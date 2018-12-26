package wallet;

import java.util.Date;

import enumerations.TipoOperacion;

public class Operacion implements CSVCompatible{
	private int id;
	private double importe;
	private String categoria;
	private TipoOperacion tipo;
	private String descripcion;
	private Date fecha;

	public Operacion(int id, double importe, String categoria, String descripcion, Date fecha, TipoOperacion tipo) {
		this.id = id;
		this.importe = importe;
		this.categoria = categoria;
		this.fecha = fecha;
		this.tipo = tipo;
		this.descripcion = descripcion;
	}

	public int getId(){
		return this.id;
	}
	public double getImporte() {
		return importe;
	}

	public void setImporte(double importe) {
		this.importe = importe;
	}

	public String getCategoria() {
		return this.categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public TipoOperacion getTipo() {
		return tipo;
	}

	public void setTipo(TipoOperacion tipo) {
		this.tipo = tipo;
	}
	
	

	@Override
	public String toString() {
		return "id: " + id + "\n" +  "importe: " + "$" + importe + "\n" + "categoria: " + categoria + "\n" + "tipo: " + tipo
				+ "\n" + "descripcion: " + descripcion + "\n" +"fecha: " + getFechaString() + "\n";
	}

	@Override
	public String toCSV() {
		return this.id + ";" + getFechaString() + ";" + this.tipo + ";" + this.importe + ";"  + this.categoria + ";" + this.descripcion;
	}
	public String getFechaString() {
		return (fecha.getYear() + 1900) + "-" + (fecha.getMonth() + 1) + "-" + fecha.getDate();
	}

}
