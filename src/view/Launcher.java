package view;

import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static model.Calendario.getFechaActual;
import static model.Calendario.getHoraActual;

public class Launcher {
    private JPanel contentPane;
    private JButton productosButton;
    private JLabel fechaActualLabel;
    private JLabel horaActualLabel;
    private JButton registrarPedidosButton;
    private JButton configuraciónButton;

    public Launcher() {
        Timer timer = new Timer(1000, e -> actualizaHora());
        timer.start();

        productosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ProductosPane productosPane = new ProductosPane();
                productosPane.setVisible(true);
            }
        });
        registrarPedidosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RegistroPedidos registroPedidos = new RegistroPedidos();
                registroPedidos.setVisible(true);
            }
        });
    }

    private void actualizaHora() {
        fechaActualLabel.setText("Fecha Actual : " + getFechaActual());
        horaActualLabel.setText("Hora Actual : " + getHoraActual());
    }

    public static void main(String[] args) {
        try {
            FlatLightLaf.setup();
        } catch (Exception e) {
            System.out.println("Error al aplicar flatlaf" + e.getMessage());
        }
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setTitle("Sistema de Pedidos");
            frame.setBounds(100, 100, 550, 400);
            frame.setContentPane(new Launcher().contentPane);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

        });
    }
}
