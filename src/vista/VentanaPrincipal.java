package vista;

import modelo.Cliente;
import modelo.Libreria;
import modelo.LibroDigital;
import modelo.LibroImpreso;
import modelo.Papeleria;
import modelo.Producto;
import modelo.Venta;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.List;

public class VentanaPrincipal extends JFrame implements ActionListener {
    private static final Color COLOR_MARINO = new Color(44, 62, 80);
    private static final Color COLOR_AZUL = new Color(52, 120, 190);
    private static final Color COLOR_VERDE = new Color(39, 145, 95);
    private static final Color COLOR_GRIS = new Color(110, 120, 130);
    private static final Color COLOR_FONDO = new Color(245, 247, 250);

    private Libreria libreria;
    private Venta ventaActual;

    private JTextField campoBusqueda;
    private DefaultTableModel modeloTabla;
    private JTable tablaProductos;

    private JComboBox<String> tipoProducto;
    private JTextField codigoProducto;
    private JTextField nombreProducto;
    private JTextField precioProducto;
    private JLabel etiquetaDetalle;
    private JTextField campoDetalle;
    private JLabel etiquetaFormato;
    private JComboBox<String> formatoProducto;
    private JLabel etiquetaStock;
    private JTextField stockProducto;

    private JComboBox<Cliente> selectorCliente;
    private JTextField nuevoCliente;
    private JButton botonRegistrarCliente;
    private JComboBox<Producto> selectorProducto;
    private JTextField cantidadVenta;
    private JTextArea textoVenta;

    public VentanaPrincipal() {
        libreria = new Libreria();
        ventaActual = null;

        setTitle("Librería Páginas Andinas - Sistema de Caja");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(900, 620));
        setSize(1100, 740);
        getContentPane().setBackground(COLOR_FONDO);
        setLayout(new BorderLayout());

        add(crearEncabezado(), BorderLayout.NORTH);

        JTabbedPane pestanas = new JTabbedPane();
        pestanas.setFont(new Font("SansSerif", Font.BOLD, 14));
        pestanas.addTab("1. Catálogo y Búsqueda", crearPanelCatalogo());
        pestanas.addTab("2. Registrar Producto", crearPanelRegistro());
        pestanas.addTab("3. Punto de Venta", crearPanelVentas());
        pestanas.setBorder(BorderFactory.createEmptyBorder(8, 12, 12, 12));
        add(pestanas, BorderLayout.CENTER);

        actualizarTabla(libreria.getProductos());
        actualizarSelectorProductos();
        setLocationRelativeTo(null);
    }

    private JPanel crearEncabezado() {
        JPanel encabezado = new JPanel(new BorderLayout());
        encabezado.setBackground(COLOR_MARINO);
        encabezado.setBorder(BorderFactory.createEmptyBorder(20, 26, 20, 20));

        JLabel titulo = new JLabel("LIBRERÍA PÁGINAS ANDINAS - SISTEMA DE CAJA");
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 21));
        encabezado.add(titulo, BorderLayout.CENTER);
        return encabezado;
    }

    private JPanel crearPanelCatalogo() {
        JPanel panel = crearPanelBase();

        JPanel busqueda = new JPanel(new BorderLayout(10, 0));
        busqueda.setOpaque(false);
        busqueda.setBorder(BorderFactory.createEmptyBorder(4, 4, 14, 4));
        campoBusqueda = new JTextField();
        campoBusqueda.setFont(new Font("SansSerif", Font.PLAIN, 14));
        campoBusqueda.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(190, 198, 208)),
                BorderFactory.createEmptyBorder(7, 9, 7, 9)));
        campoBusqueda.setToolTipText("Busque por nombre del producto o del autor.");
        campoBusqueda.addActionListener(this);
        busqueda.add(campoBusqueda, BorderLayout.CENTER);

        JPanel botones = new JPanel();
        botones.setOpaque(false);
        JButton botonBuscar = crearBoton("Buscar", COLOR_AZUL, "buscar");
        JButton botonVerTodos = crearBoton("Ver Todos", COLOR_GRIS, "verTodos");
        botones.add(botonBuscar);
        botones.add(botonVerTodos);
        busqueda.add(botones, BorderLayout.EAST);
        panel.add(busqueda, BorderLayout.NORTH);

        modeloTabla = new DefaultTableModel(
                new Object[]{"Código", "Tipo", "Nombre", "Precio ($)", "Detalle", "Stock"}, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };
        tablaProductos = new JTable(modeloTabla);
        tablaProductos.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tablaProductos.setRowHeight(28);
        tablaProductos.setFillsViewportHeight(true);
        tablaProductos.setSelectionBackground(new Color(218, 232, 247));
        tablaProductos.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        tablaProductos.getTableHeader().setReorderingAllowed(false);

        JScrollPane desplazamiento = new JScrollPane(tablaProductos);
        desplazamiento.setBorder(BorderFactory.createLineBorder(new Color(220, 225, 232)));
        panel.add(desplazamiento, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelRegistro() {
        JPanel panel = crearPanelBase();
        JPanel centrador = new JPanel(new GridBagLayout());
        centrador.setOpaque(false);

        JPanel formulario = new JPanel(new GridBagLayout());
        formulario.setBackground(Color.WHITE);
        formulario.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(225, 229, 235)),
                BorderFactory.createEmptyBorder(24, 30, 24, 30)));

        tipoProducto = new JComboBox<String>();
        tipoProducto.addItem("Libro de Papel");
        tipoProducto.addItem("Libro Digital");
        tipoProducto.addItem("Papelería");
        tipoProducto.setFont(new Font("SansSerif", Font.PLAIN, 14));
        tipoProducto.addActionListener(this);

        codigoProducto = crearCampo();
        nombreProducto = crearCampo();
        precioProducto = crearCampo();
        campoDetalle = crearCampo();
        formatoProducto = new JComboBox<String>();
        formatoProducto.addItem("PDF");
        formatoProducto.addItem("EPUB");
        formatoProducto.setFont(new Font("SansSerif", Font.PLAIN, 14));
        stockProducto = crearCampo();

        etiquetaDetalle = crearEtiqueta("Autor:");
        etiquetaFormato = crearEtiqueta("Formato:");
        etiquetaStock = crearEtiqueta("Stock en bodega:");
        configurarValidacion(codigoProducto, "[A-Za-z0-9-]*",
                "Use letras, números y guiones en el código.");
        configurarValidacion(precioProducto, "[0-9]*(\\.[0-9]{0,2})?",
                "Ingrese un precio positivo con hasta dos decimales.");
        configurarValidacion(campoDetalle, "[\\p{L}\\p{M}]*[.]?(?:[ '-][\\p{L}\\p{M}]+\\.?)*",
                "Use letras, espacios, puntos, guiones o apóstrofes.");
        configurarValidacion(stockProducto, "[0-9]*",
                "El stock solo puede contener números enteros.");

        int fila = 0;
        agregarFila(formulario, fila++, crearEtiqueta("Tipo de producto:"), tipoProducto);
        agregarFila(formulario, fila++, crearEtiqueta("Código:"), codigoProducto);
        agregarFila(formulario, fila++, crearEtiqueta("Nombre:"), nombreProducto);
        agregarFila(formulario, fila++, crearEtiqueta("Precio (use punto decimal):"), precioProducto);
        agregarFila(formulario, fila++, etiquetaDetalle, campoDetalle);
        agregarFila(formulario, fila++, etiquetaFormato, formatoProducto);
        agregarFila(formulario, fila++, etiquetaStock, stockProducto);

        JButton botonGuardar = crearBoton("GUARDAR PRODUCTO", COLOR_VERDE, "guardarProducto");
        GridBagConstraints restriccionesBoton = new GridBagConstraints();
        restriccionesBoton.gridx = 0;
        restriccionesBoton.gridy = fila;
        restriccionesBoton.gridwidth = 2;
        restriccionesBoton.fill = GridBagConstraints.HORIZONTAL;
        restriccionesBoton.insets = new Insets(18, 8, 4, 8);
        formulario.add(botonGuardar, restriccionesBoton);

        GridBagConstraints restriccionesFormulario = new GridBagConstraints();
        restriccionesFormulario.gridx = 0;
        restriccionesFormulario.gridy = 0;
        restriccionesFormulario.fill = GridBagConstraints.HORIZONTAL;
        restriccionesFormulario.weightx = 1;
        centrador.add(formulario, restriccionesFormulario);
        panel.add(centrador, BorderLayout.CENTER);

        actualizarCamposRegistro();
        return panel;
    }

    private JPanel crearPanelVentas() {
        JPanel panel = crearPanelBase();
        JSplitPane division = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT, crearControlesVenta(), crearPanelComprobante());
        division.setResizeWeight(0.40);
        division.setDividerLocation(360);
        division.setBorder(null);
        panel.add(division, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearControlesVenta() {
        JPanel controles = new JPanel(new GridBagLayout());
        controles.setBackground(Color.WHITE);
        controles.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(225, 229, 235)),
                BorderFactory.createEmptyBorder(20, 18, 20, 18)));

        selectorCliente = new JComboBox<Cliente>();
        List<Cliente> clientes = libreria.getClientes();
        for (Cliente cliente : clientes) {
            selectorCliente.addItem(cliente);
        }
        nuevoCliente = crearCampo();
        nuevoCliente.setPreferredSize(new Dimension(140, 34));
        nuevoCliente.setToolTipText(
                "Use letras, espacios, puntos, guiones o apóstrofes en el nombre.");
        configurarValidacion(nuevoCliente, "[\\p{L}\\p{M}]*\\.?(?:[ '-][\\p{L}\\p{M}]+\\.?)*",
                "El nombre solo puede contener letras, espacios, guiones o apóstrofes.");
        botonRegistrarCliente = crearBoton("Agregar cliente", COLOR_AZUL, "registrarCliente");
        botonRegistrarCliente.setFont(new Font("SansSerif", Font.BOLD, 12));
        botonRegistrarCliente.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        nuevoCliente.setActionCommand("registrarCliente");
        nuevoCliente.addActionListener(this);

        JPanel panelNuevoCliente = new JPanel(new BorderLayout(6, 0));
        panelNuevoCliente.setOpaque(false);
        panelNuevoCliente.add(nuevoCliente, BorderLayout.CENTER);
        panelNuevoCliente.add(botonRegistrarCliente, BorderLayout.EAST);

        selectorProducto = new JComboBox<Producto>();
        cantidadVenta = crearCampo();
        cantidadVenta.setText("1");
        configurarValidacion(cantidadVenta, "[0-9]*",
                "La cantidad solo puede contener números enteros.");

        agregarCampoVenta(controles, 0, "Cliente:", selectorCliente);
        agregarCampoVenta(controles, 2, "Agregar cliente nuevo:", panelNuevoCliente);
        agregarCampoVenta(controles, 4, "Producto:", selectorProducto);
        agregarCampoVenta(controles, 6, "Cantidad:", cantidadVenta);

        JButton botonAgregar = crearBoton("AGREGAR AL CARRITO", COLOR_AZUL, "agregarVenta");
        JButton botonCobrar = crearBoton("FINALIZAR Y COBRAR", COLOR_VERDE, "confirmarVenta");
        JButton botonNueva = crearBoton("NUEVA VENTA", COLOR_GRIS, "nuevaVenta");
        agregarBotonVenta(controles, 8, botonAgregar);
        agregarBotonVenta(controles, 9, botonCobrar);
        agregarBotonVenta(controles, 10, botonNueva);
        return controles;
    }

    private JPanel crearPanelComprobante() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COLOR_FONDO);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 0));

        textoVenta = new JTextArea();
        textoVenta.setEditable(false);
        textoVenta.setLineWrap(true);
        textoVenta.setWrapStyleWord(true);
        textoVenta.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        textoVenta.setBackground(new Color(252, 252, 250));
        textoVenta.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JScrollPane desplazamiento = new JScrollPane(textoVenta);
        desplazamiento.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(210, 216, 224)),
                "Desglose de Factura / Comprobante"));
        panel.add(desplazamiento, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelBase() {
        JPanel panel = new JPanel(new BorderLayout(12, 12));
        panel.setBackground(COLOR_FONDO);
        panel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        return panel;
    }

    private JTextField crearCampo() {
        JTextField campo = new JTextField(22);
        campo.setFont(new Font("SansSerif", Font.PLAIN, 14));
        campo.setPreferredSize(new Dimension(250, 34));
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(190, 198, 208)),
                BorderFactory.createEmptyBorder(7, 9, 7, 9)));
        campo.setBackground(Color.WHITE);
        return campo;
    }

    private JLabel crearEtiqueta(String texto) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setFont(new Font("SansSerif", Font.BOLD, 14));
        return etiqueta;
    }

    private JButton crearBoton(String texto, Color color, String comando) {
        JButton boton = new JButton(texto);
        boton.setActionCommand(comando);
        boton.addActionListener(this);
        boton.setFont(new Font("SansSerif", Font.BOLD, 14));
        boton.setForeground(Color.WHITE);
        boton.setBackground(color);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        boton.setOpaque(true);
        return boton;
    }

    private void agregarFila(JPanel panel, int fila, JLabel etiqueta, java.awt.Component campo) {
        GridBagConstraints restriccionesEtiqueta = new GridBagConstraints();
        restriccionesEtiqueta.gridx = 0;
        restriccionesEtiqueta.gridy = fila;
        restriccionesEtiqueta.anchor = GridBagConstraints.LINE_START;
        restriccionesEtiqueta.insets = new Insets(8, 8, 8, 18);
        panel.add(etiqueta, restriccionesEtiqueta);

        GridBagConstraints restriccionesCampo = new GridBagConstraints();
        restriccionesCampo.gridx = 1;
        restriccionesCampo.gridy = fila;
        restriccionesCampo.weightx = 1;
        restriccionesCampo.fill = GridBagConstraints.HORIZONTAL;
        restriccionesCampo.insets = new Insets(8, 8, 8, 8);
        panel.add(campo, restriccionesCampo);
    }

    private void agregarCampoVenta(JPanel panel, int fila, String texto, java.awt.Component campo) {
        GridBagConstraints restriccionesEtiqueta = new GridBagConstraints();
        restriccionesEtiqueta.gridx = 0;
        restriccionesEtiqueta.gridy = fila;
        restriccionesEtiqueta.gridwidth = 2;
        restriccionesEtiqueta.anchor = GridBagConstraints.LINE_START;
        restriccionesEtiqueta.insets = new Insets(10, 4, 5, 4);
        panel.add(crearEtiqueta(texto), restriccionesEtiqueta);

        GridBagConstraints restriccionesCampo = new GridBagConstraints();
        restriccionesCampo.gridx = 0;
        restriccionesCampo.gridy = fila + 1;
        restriccionesCampo.gridwidth = 2;
        restriccionesCampo.weightx = 1;
        restriccionesCampo.fill = GridBagConstraints.HORIZONTAL;
        restriccionesCampo.insets = new Insets(0, 4, 8, 4);
        panel.add(campo, restriccionesCampo);
    }

    private void agregarBotonVenta(JPanel panel, int fila, JButton boton) {
        GridBagConstraints restricciones = new GridBagConstraints();
        restricciones.gridx = 0;
        restricciones.gridy = fila;
        restricciones.weightx = 1;
        restricciones.fill = GridBagConstraints.HORIZONTAL;
        restricciones.insets = new Insets(8, 4, 4, 4);
        panel.add(boton, restricciones);
    }

    private void actualizarCamposRegistro() {
        String tipo = (String) tipoProducto.getSelectedItem();
        boolean esDigital = "Libro Digital".equals(tipo);
        boolean esPapeleria = "Papelería".equals(tipo);

        etiquetaDetalle.setText(esPapeleria ? "Marca:" : "Autor:");
        etiquetaFormato.setVisible(esDigital);
        formatoProducto.setVisible(esDigital);
        formatoProducto.setEnabled(esDigital);
        etiquetaStock.setEnabled(!esDigital);
        stockProducto.setEnabled(!esDigital);
        revalidate();
        repaint();
    }

    private void guardarProducto() {
        String tipo = (String) tipoProducto.getSelectedItem();
        validarCampo(codigoProducto, "Código", "[A-Za-z0-9-]+",
                "El código es obligatorio y solo puede contener letras, números y guiones.");
        validarCampo(nombreProducto, "Nombre", ".*\\S.*",
                "El nombre del producto es obligatorio.");
        validarCampo(precioProducto, "Precio", "[0-9]+(\\.[0-9]{1,2})?",
                "Ingrese un precio positivo con hasta dos decimales.");
        if (!"Libro Digital".equals(tipo)) {
            validarCampo(stockProducto, "Stock", "[0-9]+",
                    "Ingrese el stock como un número entero.");
        }
        validarCampo(campoDetalle, "Autor o marca",
                "[\\p{L}\\p{M}]+\\.?(?:[ '-][\\p{L}\\p{M}]+\\.?)*",
                "Use letras, espacios, puntos, guiones o apóstrofes en el autor o la marca.");
        double precio = Double.parseDouble(precioProducto.getText().trim());
        Producto producto;

        if ("Libro de Papel".equals(tipo)) {
            int stock = Integer.parseInt(stockProducto.getText().trim());
            producto = new LibroImpreso(
                    codigoProducto.getText(), nombreProducto.getText(), precio,
                    campoDetalle.getText(), stock);
        } else if ("Libro Digital".equals(tipo)) {
            producto = new LibroDigital(
                    codigoProducto.getText(), nombreProducto.getText(), precio,
                    campoDetalle.getText(), (String) formatoProducto.getSelectedItem());
        } else {
            int stock = Integer.parseInt(stockProducto.getText().trim());
            producto = new Papeleria(
                    codigoProducto.getText(), nombreProducto.getText(), precio,
                    campoDetalle.getText(), stock);
        }

        libreria.registrarProducto(producto);
        actualizarTabla(libreria.getProductos());
        actualizarSelectorProductos();
        limpiarFormularioProducto();
        JOptionPane.showMessageDialog(
                this, "El producto se registró correctamente.",
                "Producto registrado", JOptionPane.INFORMATION_MESSAGE);
    }

    private void buscarProductos() {
        actualizarTabla(libreria.buscarPorNombre(campoBusqueda.getText()));
    }

    private void actualizarTabla(List<Producto> productos) {
        if (modeloTabla == null) {
            return;
        }
        modeloTabla.setRowCount(0);
        for (Producto producto : productos) {
            Object[] fila = new Object[6];
            fila[0] = producto.getCodigo();
            fila[1] = producto.getTipo();
            fila[2] = producto.getNombre();
            fila[3] = String.format("%.2f", producto.getPrecio());
            fila[4] = producto.getDetalle();
            fila[5] = producto.getExistencias();
            modeloTabla.addRow(fila);
        }
    }

    private void actualizarSelectorProductos() {
        if (selectorProducto == null) {
            return;
        }
        selectorProducto.removeAllItems();
        List<Producto> productos = libreria.getProductos();
        for (Producto producto : productos) {
            selectorProducto.addItem(producto);
        }
    }

    private void agregarProductoAVenta() {
        Cliente clienteSeleccionado = (Cliente) selectorCliente.getSelectedItem();
        Producto productoSeleccionado = (Producto) selectorProducto.getSelectedItem();
        validarCampo(cantidadVenta, "Cantidad", "[0-9]+",
                "Ingrese la cantidad como un número entero.");
        int cantidad = Integer.parseInt(cantidadVenta.getText().trim());

        if (ventaActual == null) {
            Venta nuevaVenta = libreria.crearVenta(clienteSeleccionado);
            nuevaVenta.agregarProducto(productoSeleccionado, cantidad);
            ventaActual = nuevaVenta;
        } else {
            ventaActual.agregarProducto(productoSeleccionado, cantidad);
        }

        selectorCliente.setEnabled(false);
        nuevoCliente.setEnabled(false);
        botonRegistrarCliente.setEnabled(false);
        textoVenta.setText(ventaActual.generarComprobante());
        textoVenta.setCaretPosition(0);
    }

    private void confirmarVenta() {
        if (ventaActual == null) {
            throw new IllegalArgumentException("Agregue productos antes de finalizar la venta.");
        }

        ventaActual.confirmar();
        textoVenta.setText(ventaActual.generarComprobante() + "\n\nVENTA CONFIRMADA");
        textoVenta.setCaretPosition(0);
        actualizarTabla(libreria.getProductos());
        actualizarSelectorProductos();

        ventaActual = null;
        habilitarSeleccionCliente(true);
        cantidadVenta.setText("1");
        JOptionPane.showMessageDialog(
                this, "La venta se registró correctamente.",
                "Venta finalizada", JOptionPane.INFORMATION_MESSAGE);
    }

    private void nuevaVenta() {
        ventaActual = null;
        habilitarSeleccionCliente(true);
        cantidadVenta.setText("1");
        textoVenta.setText("");
    }

    private void registrarCliente() {
        validarCampo(nuevoCliente, "Nombre del cliente",
                "[\\p{L}\\p{M}]+\\.?(?:[ '-][\\p{L}\\p{M}]+\\.?)*",
                "Ingrese un nombre con letras, espacios, puntos, guiones o apóstrofes.");
        Cliente cliente = libreria.registrarCliente(nuevoCliente.getText().trim());
        selectorCliente.addItem(cliente);
        selectorCliente.setSelectedItem(cliente);
        nuevoCliente.setText("");
        JOptionPane.showMessageDialog(
                this, "El cliente se agregó correctamente.",
                "Cliente agregado", JOptionPane.INFORMATION_MESSAGE);
    }

    private void habilitarSeleccionCliente(boolean habilitado) {
        selectorCliente.setEnabled(habilitado);
        nuevoCliente.setEnabled(habilitado);
        botonRegistrarCliente.setEnabled(habilitado);
    }

    private void configurarValidacion(final JTextField campo, final String patron,
                                      final String mensaje) {
        campo.setToolTipText(mensaje);
        campo.putClientProperty("bordeNormal", campo.getBorder());
        campo.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent evento) {
                actualizarBordeValidacion(campo, patron);
            }
        });
        campo.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent evento) {
                actualizarBordeValidacion(campo, patron);
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent evento) {
                actualizarBordeValidacion(campo, patron);
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent evento) {
                actualizarBordeValidacion(campo, patron);
            }
        });
    }

    private void actualizarBordeValidacion(JTextField campo, String patron) {
        String texto = campo.getText().trim();
        if (texto.isEmpty() || texto.matches(patron)) {
            campo.setBorder((javax.swing.border.Border) campo.getClientProperty("bordeNormal"));
            campo.setBackground(Color.WHITE);
        } else {
            campo.setBorder(BorderFactory.createLineBorder(new Color(190, 45, 45), 2));
            campo.setBackground(new Color(255, 238, 238));
        }
    }

    private void validarCampo(JTextField campo, String nombre, String patron, String mensaje) {
        String texto = campo.getText().trim();
        if (!texto.matches(patron)) {
            campo.setBorder(BorderFactory.createLineBorder(new Color(190, 45, 45), 2));
            campo.setBackground(new Color(255, 238, 238));
            campo.requestFocusInWindow();
            throw new IllegalArgumentException(nombre + ": " + mensaje);
        }
        actualizarBordeValidacion(campo, patron);
    }

    private void limpiarFormularioProducto() {
        codigoProducto.setText("");
        nombreProducto.setText("");
        precioProducto.setText("");
        campoDetalle.setText("");
        stockProducto.setText("");
        formatoProducto.setSelectedIndex(0);
    }

    @Override
    public void actionPerformed(ActionEvent evento) {
        try {
            if (evento.getSource() == tipoProducto) {
                actualizarCamposRegistro();
            } else if (evento.getSource() == campoBusqueda) {
                buscarProductos();
            } else if ("buscar".equals(evento.getActionCommand())) {
                buscarProductos();
            } else if ("verTodos".equals(evento.getActionCommand())) {
                campoBusqueda.setText("");
                actualizarTabla(libreria.getProductos());
            } else if ("guardarProducto".equals(evento.getActionCommand())) {
                guardarProducto();
            } else if ("registrarCliente".equals(evento.getActionCommand())) {
                registrarCliente();
            } else if ("agregarVenta".equals(evento.getActionCommand())) {
                agregarProductoAVenta();
            } else if ("confirmarVenta".equals(evento.getActionCommand())) {
                confirmarVenta();
            } else if ("nuevaVenta".equals(evento.getActionCommand())) {
                nuevaVenta();
            }
        } catch (NumberFormatException error) {
            JOptionPane.showMessageDialog(
                    this,
                    "Revise el precio y la cantidad. Ingrese números válidos; para decimales, use punto.",
                    "Dato incorrecto",
                    JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException error) {
            JOptionPane.showMessageDialog(
                    this, error.getMessage(), "No se pudo completar",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        UIManager.put("TabbedPane.selected", Color.WHITE);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                VentanaPrincipal ventana = new VentanaPrincipal();
                ventana.setLocationRelativeTo(null);
                ventana.setVisible(true);
            }
        });
    }
}
