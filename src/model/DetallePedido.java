package model;

public class DetallePedido {
    private String codProducto;
    private int cantidad;
    private double precioVenta;
    private String codPedido;

    public DetallePedido() {
    }

    public DetallePedido(String codProducto, int cantidad, double precioVenta, String codPedido) {
        this.codProducto = codProducto;
        this.cantidad = cantidad;
        this.precioVenta = precioVenta;
        this.codPedido = codPedido;
    }

    public String getCodPedido() {
        return codPedido;
    }

    public void setCodPedido(String codPedido) {
        this.codPedido = codPedido;
    }

    public String getCodProducto() {
        return codProducto;
    }

    public void setCodProducto(String codProducto) {
        this.codProducto = codProducto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(double precioVenta) {
        this.precioVenta = precioVenta;
    }
}
