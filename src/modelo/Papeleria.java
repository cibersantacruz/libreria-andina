package modelo;

public class Papeleria extends Producto {
    private String marca;
    private int existencias;

    public Papeleria(String codigo, String nombre, double precio,
                     String marca, int existencias) {
        super(codigo, nombre, precio);
        if (marca == null || marca.trim().isEmpty()) {
            throw new IllegalArgumentException("La marca es obligatoria.");
        }
        if (existencias < 0) {
            throw new IllegalArgumentException("Las existencias no pueden ser negativas.");
        }
        this.marca = marca.trim();
        this.existencias = existencias;
    }

    public String getMarca() {
        return marca;
    }

    @Override
    public String getTipo() {
        return "Papelería";
    }

    @Override
    public String getDetalle() {
        return "Marca: " + marca;
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
        return false;
    }

    @Override
    public double calcularIva(double subtotal) {
        return subtotal * 0.15;
    }
}
