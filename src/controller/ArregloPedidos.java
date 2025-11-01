
package controller;

import model.CabeceraPedido;
import model.DetallePedido;

import java.io.*;
import java.util.ArrayList;

public class ArregloPedidos {
    private ArrayList<CabeceraPedido> pedidos;

    public ArregloPedidos() {
        pedidos = new ArrayList<>();
    }

    public int tamanioPedidos() {
        return pedidos.size();
    }

    public void agregarPedido(CabeceraPedido pedido) {
        pedidos.add(pedido);
        guardarCabeceras();
        guardarDetalles();
    }

    public String codCorrelativo(){
        if(pedidos.isEmpty()){
            return "P001";
        } else {
            String last = getPedido(tamanioPedidos()-1).getCodPedido();
            String numStr = last.replaceAll("\\D+", "");
            int n = 0;
            try {
                n = Integer.parseInt(numStr);
            } catch (NumberFormatException e) {
                n = 0;
            }
            return String.format("P%03d", n + 1);
        }
    }

    public CabeceraPedido getPedido(int pos) {
        return pedidos.get(pos);
    }

    public CabeceraPedido buscarPedido(String codPedido) {
        for (int i = 0; i < tamanioPedidos(); i++) {
            if (pedidos.get(i).getCodPedido().equals(codPedido)) {
                return pedidos.get(i);
            }
        }
        return null;
    }

    public void cargarPedidos() {
        // Limpiar la lista antes de cargar
        pedidos.clear();

        // 1. Cargar Cabeceras (en cabeceras.txt)
        try (BufferedReader br = new BufferedReader(new FileReader("cabeceras.txt"))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split("\\|");
                if (partes.length == 4) {
                    CabeceraPedido cp = new CabeceraPedido(
                            partes[0],
                            partes[1],
                            partes[2],
                            Double.parseDouble(partes[3])
                    );
                    pedidos.add(cp);
                }
            }
        } catch (IOException e) {
            System.err.println("Archivo cabeceras.txt no encontrado o error de lectura: " + e.getMessage());
        }

        // 2. Cargar Detalles (en detalles.txt) y vincularlos a la Cabecera
        try (BufferedReader br = new BufferedReader(new FileReader("detalles.txt"))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split("\\|");
                if (partes.length == 4) {
                    String codPedido = partes[0];
                    String codProducto = partes[1];
                    int cantidad = Integer.parseInt(partes[2]);
                    double precioVenta = Double.parseDouble(partes[3]);

                    DetallePedido dp = new DetallePedido(
                            codProducto,
                            cantidad,
                            precioVenta,
                            codPedido
                    );

                    CabeceraPedido cabecera = buscarPedido(codPedido);
                    if (cabecera != null) {
                        cabecera.agregarDetalle(dp);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Archivo detalles.txt no encontrado o error de lectura: " + e.getMessage());
        }
    }

    private void guardarCabeceras() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("cabeceras.txt"))) {
            for (CabeceraPedido cp : pedidos) {
                bw.write(cp.getCodPedido() + "|" + cp.getCodCliente() + "|" + cp.getFechaPedido() + "|" + cp.getTotalPagar());
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error al escribir cabeceras.txt: " + e.getMessage());
        }
    }

    private void guardarDetalles() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("detalles.txt"))) {
            for (CabeceraPedido cp : pedidos) {
                for (DetallePedido dp : cp.getListaDetalles()) {
                    bw.write(dp.getCodPedido() + "|" + dp.getCodProducto() + "|" + dp.getCantidad() + "|" + dp.getPrecioVenta());
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            System.err.println("Error al escribir detalles.txt: " + e.getMessage());
        }
    }

    // Método público para forzar guardado manual si se desea
    public void guardarTodos() {
        guardarCabeceras();
        guardarDetalles();
    }
}