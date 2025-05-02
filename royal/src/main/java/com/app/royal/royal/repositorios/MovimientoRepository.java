package com.app.royal.royal.repositorios;

import com.app.royal.royal.entidades.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {
    List<Movimiento> findByProductoIdProducto(Long idProducto);
    List<Movimiento> findByFechaBetween(LocalDateTime desde, LocalDateTime hasta);

}