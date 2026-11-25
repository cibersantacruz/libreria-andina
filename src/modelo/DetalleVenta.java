package modelo;

public class DetalleVenta {
    private Producto producto;
    private int cantidad;
    private double subtotal;
    private double descuento;
    private double iva;

    public DetalleVenta(Producto producto, int cantidad, Cliente cliente) {
        if (producto == null || cliente == null) {
            throw new IllegalArgumentException("El producto y el cliente son obligatorios.");
        }
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero.");
        }
        this.producto = producto;
        this.cantidad = cantidad;
        this.subtotal = redondear(producto.getPrecio() * cantidad);
        this.descuento = redondear(cliente.calcularDescuento(producto, subtotal));
        this.iva = redondear(producto.calcularIva(subtotal - descuento));
    }

    public Producto getProducto() {
        return producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public double getDescuento() {
        return descuento;
    }

    public double getIva() {
        return iva;
    }

    public double getTotal() {
        return subtotal - descuento + iva;
    }

    private double redondear(double cantidad) {
        return Math.round(cantidad * 100) / 100.0;
    }
}
