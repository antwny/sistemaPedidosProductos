
package model;

import java.util.ArrayList;

public class CabeceraPedido {
    private String codPedido;
    private String codCliente;
    private String fechaPedido;
    private double totalPagar;
    private ArrayList<DetallePedido> listaDetalles;

    public CabeceraPedido() {
        this.listaDetalles = new ArrayList<>();
    }

    public CabeceraPedido(String codPedido,String codCliente, String fechaPedido, double totalPagar) {
        this.codPedido = codPedido;
        this.codCliente = codCliente;
        this.fechaPedido = fechaPedido;
        this.totalPagar = totalPagar;
        this.listaDetalles = new ArrayList<>();
    }

    public String getCodCliente() {
        return codCliente;
    }

    public void setCodCliente(String codCliente) {
        this.codCliente = codCliente;
    }

    public String getCodPedido() {
        return codPedido;
    }

    public void setCodPedido(String codPedido) {
        this.codPedido = codPedido;
    }

    public String getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(String fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public double getTotalPagar() {
        return totalPagar;
    }

    public void setTotalPagar(double totalPagar) {
        this.totalPagar = totalPagar;
    }

    public void agregarDetalle(DetallePedido detalle) {
        this.listaDetalles.add(detalle);
    }

    // Getters
    public ArrayList<DetallePedido> getListaDetalles() {
        return listaDetalles;
    }
}