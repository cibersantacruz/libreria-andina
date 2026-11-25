package modelo;

public class ClienteSocio extends Cliente {

    public ClienteSocio(String codigo, String nombre) {
        super(codigo, nombre);
    }

    @Override
    public boolean esSocio() {
        return true;
    }

    @Override
    public double calcularDescuento(Producto producto, double subtotal) {
        if (producto.permiteDescuento()) {
            return subtotal * 0.10;
        }
        return 0;
    }
}
