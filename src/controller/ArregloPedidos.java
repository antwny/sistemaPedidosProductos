package controller;

import model.CabeceraPedido;
import model.DetallePedido;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ArregloPedidos {
    private ArrayList<CabeceraPedido> pedidos;
    private ArrayList<DetallePedido> detalles;

    public ArregloPedidos() {
        pedidos = new ArrayList<>();
    }

    public int tamanioPedidos() {
        return pedidos.size();
    }

    public void agregarPedido(CabeceraPedido pedido) {
        pedidos.add(pedido);
    }

    public String codCorrelativo(){
        if(pedidos.isEmpty()){
            return "P001";
        }else {
            return "P"+(Integer.parseInt(getPedido(tamanioPedidos()-1).getCodPedido())+1);
        }
    }

    public CabeceraPedido getPedido(int pos) {
        return pedidos.get(pos);
    }

    public CabeceraPedido buscarPedido(String codPedido) {
        for (int i = 0; i < tamanioPedidos(); i++) {
            if (pedidos.get(i).getCodPedido() == codPedido) {
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
                if (partes.length == 5) {
                    CabeceraPedido cp = new CabeceraPedido(
                            partes[0],
                            partes[1],
                            partes[2],
                            Double.parseDouble(partes[3]) // totalPagar (double)
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
                if (partes.length == 5) {
                    DetallePedido dp = new DetallePedido(
                            partes[0], // codPedido (String)
                            Integer.parseInt(partes[1]),
                            Double.parseDouble(partes[2]), // cantidad (int)
                            partes[3]

                    );

                    // Buscar la CabeceraPedido a la que pertenece este detalle
                    CabeceraPedido cabecera = buscarPedido(dp.getCodPedido());
                    if (cabecera != null) {
                        cabecera.agregarDetalle(dp);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Archivo detalles.txt no encontrado o error de lectura: " + e.getMessage());
        }
    }
}
