package com.app.royal.royal.dto;
import lombok.*;

import java.time.LocalDateTime;

@Data
public class ReporteMovimientoDTO {
    public ReporteMovimientoDTO(LocalDateTime fecha, String producto, String tipo, Integer cantidad, String motivo, String usuario) {
        this.fecha = fecha;
        this.producto = producto;
        this.tipo = tipo;
        this.cantidad = cantidad;
        this.motivo = motivo;
        this.usuario = usuario;
    }

    private LocalDateTime fecha;
    private String producto;
    private String tipo;
    private Integer cantidad;
    private String motivo;
    private String usuario;
}

