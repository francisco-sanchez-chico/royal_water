package com.app.royal.royal.servicios;

import com.app.royal.royal.entidades.Movimiento;
import com.app.royal.royal.entidades.Producto;
import com.app.royal.royal.entidades.TipoMovimiento;
import com.app.royal.royal.repositorios.MovimientoRepository;
import com.app.royal.royal.repositorios.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovimientoService {

    private final MovimientoRepository movimientoRepository;
    private final ProductoRepository productoRepository;

    public MovimientoService(MovimientoRepository movimientoRepository, ProductoRepository productoRepository) {
        this.movimientoRepository = movimientoRepository;
        this.productoRepository = productoRepository;
    }

    public Movimiento guardar(Movimiento movimiento) {
        Producto producto = productoRepository.findById(movimiento.getProducto().getIdProducto())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        Integer cantidad = movimiento.getCantidad();

        if (movimiento.getTipo() == TipoMovimiento.ENTRADA) {
            producto.setCantidadActual(producto.getCantidadActual() + cantidad);
        } else if (movimiento.getTipo() == TipoMovimiento.SALIDA) {
            if (producto.getCantidadActual() < cantidad) {
                throw new RuntimeException("No hay suficiente inventario para la salida.");
            }
            producto.setCantidadActual(producto.getCantidadActual() - cantidad);
        }

        productoRepository.save(producto);
        return movimientoRepository.save(movimiento);
    }

    public List<Movimiento> listarTodos() {
        return movimientoRepository.findAll();
    }

    public Optional<Movimiento> buscarPorId(Long id) {
        return movimientoRepository.findById(id);
    }

    public List<Movimiento> buscarPorProducto(Long idProducto) {
        return movimientoRepository.findByProductoIdProducto(idProducto);
    }
}
