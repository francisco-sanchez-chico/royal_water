package com.app.royal.royal.controlador;

import com.app.royal.royal.entidades.Defecto;
import com.app.royal.royal.servicios.DefectoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
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

    @GetMapping("/reporte/pdf")
    public ResponseEntity<InputStreamResource> reporteDefectosPDF(
            @RequestParam("desde") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam("hasta") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {

        ByteArrayInputStream pdf = defectoService.generarReporteDefectosPDF(desde, hasta);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=reporte_defectos.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(pdf));
    }

}
