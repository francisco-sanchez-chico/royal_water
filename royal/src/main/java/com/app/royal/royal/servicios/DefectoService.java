package com.app.royal.royal.servicios;

import com.app.royal.royal.entidades.Defecto;
import com.app.royal.royal.entidades.Producto;
import com.app.royal.royal.repositorios.DefectoRepository;
import com.app.royal.royal.repositorios.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefectoService {

    private final DefectoRepository defectoRepository;
    private final ProductoRepository productoRepository;

    public DefectoService(DefectoRepository defectoRepository, ProductoRepository productoRepository) {
        this.defectoRepository = defectoRepository;
        this.productoRepository = productoRepository;
    }

    public Defecto registrar(Defecto defecto) {
        Producto producto = productoRepository.findById(defecto.getProducto().getIdProducto())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        if (producto.getCantidadActual() < defecto.getCantidadAfectada()) {
            throw new RuntimeException("La cantidad afectada supera el inventario disponible.");
        }

        producto.setCantidadActual(producto.getCantidadActual() - defecto.getCantidadAfectada());
        productoRepository.save(producto);

        return defectoRepository.save(defecto);
    }

    public List<Defecto> listarTodos() {
        return defectoRepository.findAll();
    }

    public List<Defecto> buscarPorProducto(Long idProducto) {
        return defectoRepository.findByProductoIdProducto(idProducto);
    }
}

