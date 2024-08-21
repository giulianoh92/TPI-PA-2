package com.tpi.tpi;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.Date;
import java.util.List;

@SpringBootApplication
public class Main implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // Crear instancias de ejemplo
        Cliente cliente = new Cliente("calle 1", 1, "pass123", "user@gmail.com", "usuario1", new Date(System.currentTimeMillis()));
        Producto producto1 = new Producto(1, "Redmi Note 11 Pro 128GB", "6.7' 5000mAh", 200, 2, new CategoriaDeProducto(1, "Smartphone"));
        Producto producto2 = new Producto(2, "Samsung Galaxy S21", "6.2' 4000mAh", 300, 1, new CategoriaDeProducto(2, "Smartphone"));

        // Agregar productos al carrito del cliente
        Item item1 = new Item(1, producto1);
        Item item2 = new Item(2, producto2);
        cliente.addToCarrito(item1);
        cliente.addToCarrito(item2);

        // Crear un pedido
        Estado estado = new Estado(1, "Pendiente");
        double montoTotal = calcularMontoTotal(cliente.getCarrito().getItems());
        Pago pago = new Pago(1, new Date(System.currentTimeMillis()), "Tarjeta", montoTotal);
        Pedido pedido = new Pedido(1, estado, pago);

        // Cargar el pedido con los items del carrito
        for (Item item : cliente.getCarrito().getItems()) {
            pedido.addItem(item);
        }

        // Agregar el pedido al cliente
        cliente.addPedido(pedido);

        // Vaciar el carrito del cliente
        cliente.getCarrito().getItems().clear();

        // Imprimir información del cliente
        System.out.println("Cliente:");
        System.out.println("Nombre: " + cliente.getNombreUsuario());
        System.out.println("Contraseña: " + cliente.getPassword());
        System.out.println("Dirección: " + cliente.getDireccion());
        System.out.println("Email: " + cliente.getEmail());
        System.out.println("Fecha de Registro: " + cliente.getfechaRegistro());

        // Imprimir contenido del carrito (debería estar vacío)
        System.out.println("\nCarrito del Cliente:");
        if (cliente.getCarrito().getItems().isEmpty()) {
            System.out.println("El carrito está vacío.");
        } else {
            for (Item item : cliente.getCarrito().getItems()) {
                System.out.println("Producto: " + item.getProducto().getNombre());
            }
        }

        // Imprimir información del pedido
        System.out.println("\nPedido:");
        System.out.println("ID del Pedido: " + pedido.getId_pedido());
        System.out.println("Estado: " + pedido.getEstado().getEstado()); // Suponiendo que Estado tiene un método getNombre()
        System.out.println("Pago: " + pedido.getPago().getMonto()); // Suponiendo que Pago tiene un método getMonto()
        System.out.println("Items en el Pedido:");
        for (Item item : pedido.getItems()) {
            System.out.println("Producto: " + item.getProducto().getNombre());
        }
    }

    private double calcularMontoTotal(List<Item> items) {
        double total = 0;
        for (Item item : items) {
            total += item.getProducto().getPrecioUnitario() * item.getCantidad();
        }
        return total;
    }
}
