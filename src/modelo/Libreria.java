package modelo;

import java.util.ArrayList;
import java.util.List;

public class Libreria {
    private List<Producto> productos;
    private List<Cliente> clientes;

    public Libreria() {
        productos = new ArrayList<Producto>();
        clientes = new ArrayList<Cliente>();
        cargarDatosDePrueba();
    }

    private void cargarDatosDePrueba() {
        productos.add(new LibroImpreso(
                "LI-001", "Cien años de soledad", 25.00, "Gabriel García Márquez", 10));
        productos.add(new LibroImpreso(
                "LI-002", "El túnel", 15.50, "Ernesto Sabato", 5));
        productos.add(new LibroDigital(
                "LD-001", "Rayuela", 9.99, "Julio Cortázar", "EPUB"));
        productos.add(new LibroDigital(
                "LD-002", "La ciudad y los perros", 11.00, "Mario Vargas Llosa", "PDF"));
        productos.add(new Papeleria(
                "PA-001", "Cuaderno universitario", 3.50, "Norma", 50));
        productos.add(new Papeleria(
                "PA-002", "Esfero azul", 0.60, "Bic", 100));

        clientes.add(new ClienteSocio("CL-001", "Ana Torres"));
        clientes.add(new Cliente("CL-002", "Luis Pérez"));
    }

    public List<Producto> getProductos() {
        return new ArrayList<Producto>(productos);
    }

    public List<Cliente> getClientes() {
        return new ArrayList<Cliente>(clientes);
    }

    public Cliente registrarCliente(String nombre) {
        Cliente nuevo = new Cliente(
                "CL-" + String.format("%03d", clientes.size() + 1), nombre);
        for (Cliente cliente : clientes) {
            if (cliente.getNombre().equalsIgnoreCase(nuevo.getNombre())) {
                throw new IllegalArgumentException(
                        "Ya existe un cliente con el nombre " + nuevo.getNombre() + ".");
            }
        }
        clientes.add(nuevo);
        return nuevo;
    }

    public void registrarProducto(Producto nuevo) {
        if (nuevo == null) {
            throw new IllegalArgumentException("El producto es obligatorio.");
        }
        for (Producto producto : productos) {
            if (producto.getCodigo().equalsIgnoreCase(nuevo.getCodigo())) {
                throw new IllegalArgumentException(
                        "Ya existe un producto con el código " + nuevo.getCodigo() + ".");
            }
        }
        productos.add(nuevo);
    }

    public List<Producto> buscarPorNombre(String texto) {
        List<Producto> resultado = new ArrayList<Producto>();
        if (texto == null || texto.trim().isEmpty()) {
            return getProductos();
        }

        String buscado = texto.trim().toLowerCase();
        for (Producto producto : productos) {
            if (producto.getNombre().toLowerCase().contains(buscado)
                    || producto.getDetalle().toLowerCase().contains(buscado)) {
                resultado.add(producto);
            }
        }
        return resultado;
    }

    public Venta crearVenta(Cliente cliente) {
        return new Venta(cliente);
    }
}
