package controller;

import model.Producto;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Objects;

public class ArregloProductos {
    public ArrayList<Producto> productos;

    public ArregloProductos() {
        productos = new ArrayList<>();
        cargarProductos();
    }

    public int tamanioProductos() {
        return productos.size();
    }

    public int getindiceProducto(Producto producto) {
        return productos.indexOf(producto);
    }

    public Producto getProducto(int pos) {
        return productos.get(pos);
    }

    public void agregarProducto(Producto producto) {
        productos.add(producto);
        grabarProductos();
    }

    public void setProductos(int pos, Producto producto) {
        productos.set(pos, producto);
        grabarProductos();
    }

    public void eliminarProducto(int pos){
        productos.remove(pos);
        grabarProductos();
    }

    public Producto buscarProducto(String codProducto) {
        for (int i = 0; i < productos.size(); i++) {
            if (Objects.equals(productos.get(i).getCodProducto(), codProducto)) {
                return productos.get(i);
            }

        }
        return null;
    }

    public boolean verificarStock(String codProducto, int cantidad) {
        if(buscarProducto(codProducto).getStock()>=cantidad) {
            return true;
        }
        return false;
    }

    public String codCorrelativo(){
        if(productos.isEmpty()){
            return "1001";
        }else{
            return String.valueOf(Integer.parseInt(getProducto(tamanioProductos()-1).getCodProducto())+1);
        }
    }

    public void modificarStock(String codProducto, int cantidadComprada){
        buscarProducto(codProducto).setStock(buscarProducto(codProducto).getStock()-cantidadComprada);
        grabarProductos();
    }

    public void cargarProductos() {
        try {
            BufferedReader br;
            String linea, s[];
            String codproducto;
            String descripcion;
            double precio;
            int stock;
            br = new BufferedReader(new FileReader("productos.txt"));
            while ((linea = br.readLine()) != null) {
                s = linea.split(";");
                codproducto = s[0];
                descripcion = s[1];
                precio = Double.parseDouble(s[2]);
                stock = Integer.parseInt(s[3]);
                productos.add(new Producto(codproducto, descripcion, precio, stock));

            }
            br.close();
        } catch (Exception e) {
            System.out.println("Error al cargar los productos");
        }
    }

    public void grabarProductos() {
        try {
            PrintWriter pw;
            Producto producto;
            String linea;
            pw = new PrintWriter(new FileWriter("productos.txt"));
            for (int i = 0; i < tamanioProductos() ; i++) {
                producto = getProducto(i);
                linea = producto.getCodProducto() + ";" +
                        producto.getDescripcion() + ";" +
                        producto.getPrecio() + ";" +
                        producto.getStock();
                pw.println(linea);
            }
            pw.close();
        } catch (Exception e) {
            System.out.println("Error al grabar los productos");
        }
    }
}
