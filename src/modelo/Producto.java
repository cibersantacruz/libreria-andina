package modelo;

public abstract class Producto {
    private String codigo;
    private String nombre;
    private double precio;

    public Producto(String codigo, String nombre, double precio) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("El código del producto es obligatorio.");
        }
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto es obligatorio.");
        }
        if (precio <= 0 || Double.isNaN(precio) || Double.isInfinite(precio)) {
            throw new IllegalArgumentException("El precio debe ser mayor que cero.");
        }
        this.codigo = codigo.trim();
        this.nombre = nombre.trim();
        this.precio = precio;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public abstract String getTipo();

    public abstract String getDetalle();

    public abstract String getExistencias();

    public abstract boolean tieneExistencias(int cantidad);

    public abstract void reducirExistencias(int cantidad);

    public abstract boolean permiteDescuento();

    public abstract double calcularIva(double subtotal);

    @Override
    public String toString() {
        return codigo + " - " + nombre;
    }
}
