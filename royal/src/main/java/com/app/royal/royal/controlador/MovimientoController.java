package com.app.royal.royal.controlador;

import com.app.royal.royal.entidades.Movimiento;
import com.app.royal.royal.servicios.MovimientoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movimientos")
public class MovimientoController {

    private final MovimientoService movimientoService;

    public MovimientoController(MovimientoService movimientoService) {
        this.movimientoService = movimientoService;
    }

    @PostMapping
    public ResponseEntity<Movimiento> guardar(@Valid @RequestBody Movimiento movimiento) {
        Movimiento creado = movimientoService.guardar(movimiento);
        return ResponseEntity.ok(creado);
    }


    @GetMapping
    public ResponseEntity<List<Movimiento>> listar() {
        return ResponseEntity.ok(movimientoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Movimiento> buscarPorId(@PathVariable Long id) {
        return movimientoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/producto/{idProducto}")
    public ResponseEntity<List<Movimiento>> buscarPorProducto(@PathVariable Long idProducto) {
        return ResponseEntity.ok(movimientoService.buscarPorProducto(idProducto));
    }
}

