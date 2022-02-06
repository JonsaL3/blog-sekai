package sekai;

import java.util.Objects;

public class Entrada {

	private Integer id;
	private String titulo;
	private String texto;
	private Integer fecha; // TODO localdate
	
	public Entrada(Integer id, String titulo, String texto, Integer fecha) 
	{
		this.id = id;
		this.titulo = titulo;
		this.texto = texto;
		this.fecha = fecha;
	}
	
	public Entrada()
	{
		this(0, "","",0);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public Integer getFecha() {
		return fecha;
	}

	public void setFecha(Integer fecha) {
		this.fecha = fecha;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Entrada other = (Entrada) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "Entrada [id=" + id + ", titulo=" + titulo + ", texto=" + texto + ", fecha=" + fecha + "]";
	}
	
}
