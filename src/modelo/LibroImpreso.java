package modelo;

public class LibroImpreso extends Producto {
    private String autor;
    private int existencias;

    public LibroImpreso(String codigo, String nombre, double precio,
                        String autor, int existencias) {
        super(codigo, nombre, precio);
        if (autor == null || autor.trim().isEmpty()) {
            throw new IllegalArgumentException("El autor del libro es obligatorio.");
        }
        if (existencias < 0) {
            throw new IllegalArgumentException("Las existencias no pueden ser negativas.");
        }
        this.autor = autor.trim();
        this.existencias = existencias;
    }

    public String getAutor() {
        return autor;
    }

    @Override
    public String getTipo() {
        return "Libro impreso";
    }

    @Override
    public String getDetalle() {
        return "Autor: " + autor;
    }

    @Override
    public String getExistencias() {
        return String.valueOf(existencias);
    }

    @Override
    public boolean tieneExistencias(int cantidad) {
        return cantidad > 0 && existencias >= cantidad;
    }

    @Override
    public void reducirExistencias(int cantidad) {
        if (!tieneExistencias(cantidad)) {
            throw new IllegalArgumentException("No hay suficientes unidades de " + getNombre() + ".");
        }
        existencias = existencias - cantidad;
    }

    @Override
    public boolean permiteDescuento() {
        return true;
    }

    @Override
    public double calcularIva(double subtotal) {
        return 0;
    }
}
