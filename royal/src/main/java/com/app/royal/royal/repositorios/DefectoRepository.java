package com.app.royal.royal.repositorios;

import com.app.royal.royal.entidades.Defecto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DefectoRepository extends JpaRepository<Defecto, Long> {
    List<Defecto> findByProductoIdProducto(Long idProducto);
}