package view;

import controller.ArregloProductos;
import model.Producto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ProductosPane extends JDialog {
    private JPanel contentPane;
    private JButton nuevoButtom;
    private JButton agregarProductoButtom;
    private JTextField txtCodProducto;
    private JTextField txtDescripcion;
    private JTextField txtPrecio;
    private JTextField txtStock;
    private JScrollPane scrollpane;
    private JTable table;
    private JButton modificarProductoButton;
    private JButton eliminarProductoButton;
    private DefaultTableModel modelo;

    ArregloProductos ap = new ArregloProductos();

    public ProductosPane() {
        Object[] columnas = {"Codigo","Descripcion","Precio","Stock"};
        modelo = new DefaultTableModel(columnas,0){
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table.setModel(modelo);
        updateTable();
        setContentPane(contentPane);
        setBounds(100, 100, 650, 400);
        setTitle("Gestion de Productos");
        setLocationRelativeTo(null);
        setModal(true);
        deshabilitarTodo();
        nuevoButtom.requestFocus();

        agregarProductoButtom.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Producto nuevoProducto = new Producto();
                nuevoProducto.setCodProducto(String.valueOf(ap.codCorrelativo()));
                nuevoProducto.setDescripcion(leerDescripcion());
                nuevoProducto.setPrecio(leerPrecio());
                nuevoProducto.setStock(leerStock());
                ap.agregarProducto(nuevoProducto);
                updateTable();
                limpiarCampos();
                mensaje("Producto agregado correctamente");
                deshabilitarTodo();
                nuevoButtom.setEnabled(true);
            }
        });

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try{
                    Producto p = ap.getProducto(table.getSelectedRow());
                    txtCodProducto.setText(String.valueOf(p.getCodProducto()));
                    txtDescripcion.setText(p.getDescripcion());
                    txtPrecio.setText(String.valueOf(p.getPrecio()));
                    txtStock.setText(String.valueOf(p.getStock()));
                    habilitarCampos();
                    modificarProductoButton.setEnabled(true);
                    eliminarProductoButton.setEnabled(true);
                    nuevoButtom.setEnabled(true);
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }
        });

        eliminarProductoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ap.eliminarProducto(table.getSelectedRow());
                updateTable();
                limpiarCampos();
                mensaje("Producto eliminado correctamente");
                deshabilitarTodo();
            }
        });

        nuevoButtom.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limpiarCampos();
                txtCodProducto.setText(String.valueOf(ap.codCorrelativo()));
                table.clearSelection();
                txtDescripcion.requestFocus();
                nuevoButtom.setEnabled(false);
                agregarProductoButtom.setEnabled(true);
                habilitarCampos();
            }
        });

        modificarProductoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Producto p = ap.getProducto(table.getSelectedRow());
                p.setCodProducto(txtCodProducto.getText());
                p.setDescripcion(leerDescripcion());
                p.setPrecio(leerPrecio());
                p.setStock(leerStock());
                ap.setProductos(table.getSelectedRow(), p);
                updateTable();
                limpiarCampos();
                mensaje("Producto modificado exitosamente");
                deshabilitarTodo();
            }
        });
    }

    void deshabilitarTodo(){
        txtDescripcion.setEditable(false);
        txtPrecio.setEditable(false);
        txtStock.setEditable(false);
        agregarProductoButtom.setEnabled(false);
        modificarProductoButton.setEnabled(false);
        eliminarProductoButton.setEnabled(false);
    }

    void habilitarCampos() {
        txtDescripcion.setEditable(true);
        txtPrecio.setEditable(true);
        txtStock.setEditable(true);
    }

    void updateTable() {
        modelo.setRowCount(0);
        for (int i = 0; i<ap.tamanioProductos() ; i++) {
            Producto p = ap.getProducto(i);
            modelo.addRow(new Object[]{p.getCodProducto(),p.getDescripcion(),p.getPrecio(),p.getStock()});
        }
    }

    void mensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }

    void limpiarCampos() {
        txtCodProducto.setText("");
        txtDescripcion.setText("");
        txtPrecio.setText("");
        txtStock.setText("");
    }

    String leerCodigo() {
        return txtCodProducto.getText();
    }

    String leerDescripcion() {
        return txtDescripcion.getText();
    }
    double leerPrecio() {
        return Double.parseDouble(txtPrecio.getText());
    }
    int leerStock() {
        return Integer.parseInt(txtStock.getText());
    }

    public static void main(String[] args) {
        ProductosPane dialog = new ProductosPane();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
