package modelo;

import java.util.ArrayList;
import java.util.List;

public class Venta {
    private Cliente cliente;
    private List<DetalleVenta> detalles;
    private boolean confirmada;

    public Venta(Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("Debe seleccionar un cliente.");
        }
        this.cliente = cliente;
        this.detalles = new ArrayList<DetalleVenta>();
        this.confirmada = false;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public List<DetalleVenta> getDetalles() {
        return new ArrayList<DetalleVenta>(detalles);
    }

    public boolean estaConfirmada() {
        return confirmada;
    }

    public void agregarProducto(Producto producto, int cantidad) {
        if (confirmada) {
            throw new IllegalArgumentException("La venta ya fue confirmada.");
        }
        if (producto == null) {
            throw new IllegalArgumentException("Debe seleccionar un producto.");
        }
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero.");
        }

        for (int i = 0; i < detalles.size(); i++) {
            DetalleVenta detalle = detalles.get(i);
            if (detalle.getProducto().getCodigo().equalsIgnoreCase(producto.getCodigo())) {
                int nuevaCantidad = detalle.getCantidad() + cantidad;
                if (!producto.tieneExistencias(nuevaCantidad)) {
                    throw new IllegalArgumentException(
                            "No hay suficientes unidades de " + producto.getNombre() + ".");
                }
                detalles.set(i, new DetalleVenta(producto, nuevaCantidad, cliente));
                return;
            }
        }

        if (!producto.tieneExistencias(cantidad)) {
            throw new IllegalArgumentException(
                    "No hay suficientes unidades de " + producto.getNombre() + ".");
        }
        detalles.add(new DetalleVenta(producto, cantidad, cliente));
    }

    public double getSubtotal() {
        double total = 0;
        for (DetalleVenta detalle : detalles) {
            total = total + detalle.getSubtotal();
        }
        return total;
    }

    public double getDescuento() {
        double total = 0;
        for (DetalleVenta detalle : detalles) {
            total = total + detalle.getDescuento();
        }
        return total;
    }

    public double getIva() {
        double total = 0;
        for (DetalleVenta detalle : detalles) {
            total = total + detalle.getIva();
        }
        return total;
    }

    public double getTotal() {
        double total = 0;
        for (DetalleVenta detalle : detalles) {
            total = total + detalle.getTotal();
        }
        return total;
    }

    public void confirmar() {
        if (confirmada) {
            throw new IllegalArgumentException("La venta ya fue confirmada.");
        }
        if (detalles.isEmpty()) {
            throw new IllegalArgumentException("Agregue al menos un producto a la venta.");
        }

        for (DetalleVenta detalle : detalles) {
            if (!detalle.getProducto().tieneExistencias(detalle.getCantidad())) {
                throw new IllegalArgumentException(
                        "Ya no hay suficientes unidades de "
                                + detalle.getProducto().getNombre() + ".");
            }
        }
        for (DetalleVenta detalle : detalles) {
            detalle.getProducto().reducirExistencias(detalle.getCantidad());
        }
        confirmada = true;
    }

    public String generarComprobante() {
        String comprobante = "VENTA - Librería Páginas Andinas\n";
        comprobante = comprobante + "Cliente: " + cliente.getNombre();
        if (cliente.esSocio()) {
            comprobante = comprobante + " (socio)";
        }
        comprobante = comprobante + "\n\n";

        for (DetalleVenta detalle : detalles) {
            comprobante = comprobante + detalle.getProducto().getNombre()
                    + " x " + detalle.getCantidad()
                    + " | Subtotal: $" + String.format("%.2f", detalle.getSubtotal())
                    + " | Descuento: $" + String.format("%.2f", detalle.getDescuento())
                    + " | IVA: $" + String.format("%.2f", detalle.getIva())
                    + " | Total: $" + String.format("%.2f", detalle.getTotal()) + "\n";
        }

        comprobante = comprobante + "\nSubtotal: $" + String.format("%.2f", getSubtotal());
        comprobante = comprobante + "\nDescuento: $" + String.format("%.2f", getDescuento());
        comprobante = comprobante + "\nIVA: $" + String.format("%.2f", getIva());
        comprobante = comprobante + "\nTOTAL A PAGAR: $" + String.format("%.2f", getTotal());
        return comprobante;
    }
}
