package modelo;

public class Cliente {
    private String codigo;
    private String nombre;

    public Cliente(String codigo, String nombre) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("El código del cliente es obligatorio.");
        }
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del cliente es obligatorio.");
        }
        if (!nombre.trim().matches("[\\p{L}\\p{M}]+\\.?(?:[ '-][\\p{L}\\p{M}]+\\.?)*")) {
            throw new IllegalArgumentException(
                    "El nombre solo puede contener letras, espacios, puntos, guiones o apóstrofes.");
        }
        this.codigo = codigo.trim();
        this.nombre = nombre.trim();
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public boolean esSocio() {
        return false;
    }

    public double calcularDescuento(Producto producto, double subtotal) {
        return 0;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
