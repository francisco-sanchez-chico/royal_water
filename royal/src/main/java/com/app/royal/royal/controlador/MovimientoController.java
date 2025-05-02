package com.app.royal.royal.controlador;

import com.app.royal.royal.entidades.Movimiento;
import com.app.royal.royal.servicios.MovimientoService;
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

    @GetMapping("/reporte/pdf")
    public ResponseEntity<InputStreamResource> descargarPDF() {
        ByteArrayInputStream pdf = movimientoService.generarReportePDF();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=reporte_movimientos.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(pdf));
    }

    @GetMapping("/reporte/excel")
    public ResponseEntity<InputStreamResource> descargarExcel() {
        ByteArrayInputStream excel = movimientoService.generarReporteExcel();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=reporte_movimientos.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(new InputStreamResource(excel));
    }

    @GetMapping("/filtrar")
    public ResponseEntity<List<Movimiento>> filtrarPorFechas(
            @RequestParam("desde") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam("hasta") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {

        List<Movimiento> resultado = movimientoService.buscarPorRangoFechas(desde, hasta);
        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/reporte/pdfFecha")
    public ResponseEntity<InputStreamResource> reporteMovimientosPDF(
            @RequestParam("desde") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam("hasta") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {

        ByteArrayInputStream pdf = movimientoService.generarReportePDFPorFechas(desde, hasta);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=reporte_movimientos_filtrado.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(pdf));
    }

    @GetMapping("/reporte/excelFecha")
    public ResponseEntity<InputStreamResource> reporteMovimientosExcel(
            @RequestParam("desde") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam("hasta") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {

        ByteArrayInputStream excel = movimientoService.generarReporteExcelPorFechas(desde, hasta);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=reporte_movimientos_filtrado.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(new InputStreamResource(excel));
    }

    @GetMapping("/reporte/ventas/pdf")
    public ResponseEntity<InputStreamResource> reporteVentasPDF() {
        ByteArrayInputStream pdf = movimientoService.generarReporteVentasPDF();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=reporte_ventas.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(pdf));
    }

    @GetMapping("/reporte/ventas/excel")
    public ResponseEntity<InputStreamResource> reporteVentasExcel() {
        ByteArrayInputStream excel = movimientoService.generarReporteVentasExcel();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=reporte_ventas.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(new InputStreamResource(excel));
    }


}

