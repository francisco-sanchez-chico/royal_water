package com.app.royal.royal.controlador;

import com.app.royal.royal.entidades.Defecto;
import com.app.royal.royal.servicios.DefectoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/defectos")
public class DefectoController {

    private final DefectoService defectoService;

    public DefectoController(DefectoService defectoService) {
        this.defectoService = defectoService;
    }

    @PostMapping
    public ResponseEntity<Defecto> registrar(@Valid @RequestBody Defecto defecto) {
        return ResponseEntity.ok(defectoService.registrar(defecto));
    }

    @GetMapping
    public ResponseEntity<List<Defecto>> listar() {
        return ResponseEntity.ok(defectoService.listarTodos());
    }

    @GetMapping("/producto/{idProducto}")
    public ResponseEntity<List<Defecto>> buscarPorProducto(@PathVariable Long idProducto) {
        return ResponseEntity.ok(defectoService.buscarPorProducto(idProducto));
    }
}
