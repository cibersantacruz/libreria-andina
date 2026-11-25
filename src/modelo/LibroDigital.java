package modelo;

public class LibroDigital extends Producto {
    private String autor;
    private String formato;

    public LibroDigital(String codigo, String nombre, double precio,
                        String autor, String formato) {
        super(codigo, nombre, precio);
        if (autor == null || autor.trim().isEmpty()) {
            throw new IllegalArgumentException("El autor del libro es obligatorio.");
        }
        if (formato == null
                || (!formato.equalsIgnoreCase("PDF") && !formato.equalsIgnoreCase("EPUB"))) {
            throw new IllegalArgumentException("El formato debe ser PDF o EPUB.");
        }
        this.autor = autor.trim();
        this.formato = formato.toUpperCase();
    }

    public String getAutor() {
        return autor;
    }

    public String getFormato() {
        return formato;
    }

    @Override
    public String getTipo() {
        return "Libro digital";
    }

    @Override
    public String getDetalle() {
        return "Autor: " + autor + " | Formato: " + formato;
    }

    @Override
    public String getExistencias() {
        return "Ilimitadas";
    }

    @Override
    public boolean tieneExistencias(int cantidad) {
        return cantidad > 0;
    }

    @Override
    public void reducirExistencias(int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero.");
        }
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
