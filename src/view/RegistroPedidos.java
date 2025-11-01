package view;

import com.formdev.flatlaf.FlatLightLaf;
import controller.ArregloPedidos;
import controller.ArregloProductos;
import model.Calendario;
import model.Producto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class RegistroPedidos extends JDialog {
    private JPanel contentPane;
    private JButton procesarPedidoButton;
    private JButton buttonCancel;
    private JTextField txtCodPedido;
    private JTextField txtCodCliente;
    private JLabel jLfechaHora;
    private JComboBox cboProductos;
    private JTextField txtCantidad;
    private JButton agregarItemButton;
    private JTable table;
    private JTextField txtBuscarItem;
    private JButton buscarButton;
    private JButton eliminarItemButton;
    private JButton editarCantidadButton;
    private JButton nuevoItemButton;
    private JLabel jLstock;
    private JLabel jLtotalPedido;
    private DefaultTableModel modelo;


    ArregloProductos arregloProductos = new ArregloProductos();
    ArregloPedidos arregloPedidos = new ArregloPedidos();

    public RegistroPedidos() {
        setContentPane(contentPane);
        setBounds(100, 100, 750, 400);
        setTitle("Registrar Pedidos");
        setLocationRelativeTo(null);
        setModal(true);
        Timer timer = new Timer(1000, e -> updateFecha());
        timer.start();
        Timer timer2 = new Timer(500, e -> stockItem());
        timer2.start();
        llenarcboProductos();
        llenarCodPedido();
        stockItem();
        Object[] columnas = {"Codigo", "Descripcion", "Cantidad", "Precio Unitario"};

        // Crea una subclase anónima de DefaultTableModel
        modelo = new DefaultTableModel(columnas, 0) {

            // Sobreescribe el método isCellEditable para que siempre devuelva FALSE
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table.setModel(modelo);
        limpiarDeshabilitarCampos();

        buscarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cboProductos.setSelectedIndex(arregloProductos.getindiceProducto(arregloProductos.buscarProducto(txtBuscarItem.getText())));
                txtBuscarItem.setText("");
                txtCantidad.setText("");
            }
        });

        agregarItemButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {

                    Producto producto = arregloProductos.getProducto(cboProductos.getSelectedIndex());
                    if (arregloProductos.verificarStock(producto.getCodProducto(), Integer.parseInt(txtCantidad.getText()))) {
                        modelo.addRow(new Object[]{
                                producto.getCodProducto(),
                                producto.getDescripcion(),
                                txtCantidad.getText(),
                                producto.getPrecio()
                        });
                        updateTotalPedido();
                        txtCantidad.setText("");
                        cboProductos.setSelectedIndex(-1);
                        nuevoItemButton.setEnabled(true);
                        agregarItemButton.setEnabled(false);
                        limpiarDeshabilitarCampos();
                    } else mensaje("Insuficiente stock");


                } catch (Exception ex) {
                    mensaje("Por favor llenar todos los campos");
                }
            }
        });
        eliminarItemButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    modelo.removeRow(table.getSelectedRow());
                    limpiarDeshabilitarCampos();
                    nuevoItemButton.setEnabled(true);
                    updateTotalPedido();
                } catch (Exception ex) {
                    mensaje("Error al eliminar el producto");
                }

            }
        });
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cboProductos.setSelectedIndex(arregloProductos.getindiceProducto(arregloProductos.buscarProducto(modelo.getValueAt(table.getSelectedRow(), 0).toString())));
                txtCantidad.setText(modelo.getValueAt(table.getSelectedRow(), 2).toString());
                agregarItemButton.setEnabled(false);
                editarCantidadButton.setEnabled(true);
                eliminarItemButton.setEnabled(true);
                txtCantidad.requestFocus();
                txtCantidad.setEnabled(true);


            }
        });
        nuevoItemButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                txtCantidad.setEnabled(true);
                txtBuscarItem.setEnabled(true);
                agregarItemButton.setEnabled(true);
                cboProductos.setEnabled(true);
                buscarButton.setEnabled(true);
                nuevoItemButton.setEnabled(false);
                cboProductos.setSelectedIndex(-1);
                txtCantidad.setText("");
                txtBuscarItem.setText("");
                eliminarItemButton.setEnabled(false);
                editarCantidadButton.setEnabled(false);

            }
        });
        editarCantidadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Producto producto = arregloProductos.getProducto(cboProductos.getSelectedIndex());
                if (arregloProductos.verificarStock(producto.getCodProducto(), Integer.parseInt(txtCantidad.getText()))) {
                    modelo.setValueAt(txtCantidad.getText(), table.getSelectedRow(), 2);
                    limpiarDeshabilitarCampos();
                    updateTotalPedido();
                }else mensaje("Insuficiente stock");
            }
        });

        procesarPedidoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    for (int i = 0; i < modelo.getRowCount(); i++) {
                        String codProducto = (String) modelo.getValueAt(i, 0);
                        int cantidad = Integer.parseInt(modelo.getValueAt(i, 2).toString());
                        arregloProductos.modificarStock(codProducto, cantidad);

                        limpiarDeshabilitarCampos();

                        modelo.setRowCount(0);
                        updateTotalPedido();
                        txtCodPedido.setText(arregloPedidos.codCorrelativo());
                    }
                    mensaje("Pedido registrado con exito");
                }catch (Exception ex) { mensaje("Error al registrar el pedido"); }

            }
        });
    }


    private void stockItem() {
        if (cboProductos.getSelectedIndex() == -1) {
            jLstock.setText("Seleccione un producto");
        } else {
            jLstock.setText(String.valueOf(arregloProductos.getProducto(cboProductos.getSelectedIndex()).getStock()));
        }
    }

    void updateTotalPedido() {
        double sumaTotal = 0.0; // Variable local
        // Iterar sobre las filas de la tabla
        for (int i = 0; i < modelo.getRowCount(); i++) {

            // Columna 2 es la Cantidad (String)
            int cantidad = Integer.parseInt(modelo.getValueAt(i, 2).toString());

            // Columna 3 es el Precio Unitario (Double)
            double precioUnitario = (double) modelo.getValueAt(i, 3);

            sumaTotal += cantidad * precioUnitario;
        }
        // Actualizamos la variable de instancia totalPedido y la etiqueta
        jLtotalPedido.setText("S/. " + String.format("%.2f", sumaTotal));
    }

    private void limpiarDeshabilitarCampos() {
        txtCantidad.setEnabled(false);
        txtBuscarItem.setEnabled(false);
        agregarItemButton.setEnabled(false);
        eliminarItemButton.setEnabled(false);
        editarCantidadButton.setEnabled(false);
        cboProductos.setEnabled(false);
        buscarButton.setEnabled(false);
        txtCantidad.setText("");
        cboProductos.setSelectedIndex(-1);
    }

    private void mensaje(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje);
    }

    private void updateFecha() {
        jLfechaHora.setText(Calendario.getFechaActual());

    }

    private void llenarCodPedido() {
        txtCodPedido.setText(arregloPedidos.codCorrelativo());
    }

    private void llenarcboProductos() {
        for (int i = 0; i < arregloProductos.tamanioProductos(); i++) {
            cboProductos.setSelectedIndex(-1);
            cboProductos.addItem(arregloProductos.getProducto(i).getDescripcion());
        }
    }

    public static void main(String[] args) {
        try {
            FlatLightLaf.setup();
        } catch (Exception e) {

        }
        RegistroPedidos dialog = new RegistroPedidos();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
