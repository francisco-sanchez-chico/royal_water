package com.app.royal.royal.entidades;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Defecto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDefecto;

    @Enumerated(EnumType.STRING)
    private TipoDefecto tipo;

    private String descripcion;

    private LocalDate fechaRegistro;

    private Integer cantidadAfectada;

    @ManyToOne
    @JoinColumn(name = "id_producto", nullable = false)
    private Producto producto;

    public Long getIdDefecto() {
        return idDefecto;
    }

    public void setIdDefecto(Long idDefecto) {
        this.idDefecto = idDefecto;
    }

    public TipoDefecto getTipo() {
        return tipo;
    }

    public void setTipo(TipoDefecto tipo) {
        this.tipo = tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDate getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDate fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Integer getCantidadAfectada() {
        return cantidadAfectada;
    }

    public void setCantidadAfectada(Integer cantidadAfectada) {
        this.cantidadAfectada = cantidadAfectada;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }
}

