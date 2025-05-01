package com.app.royal.royal.entidades;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotNull(message = "El tipo de defecto es obligatorio")
    @Enumerated(EnumType.STRING)
    private TipoDefecto tipo;

    @NotBlank(message = "La descripción del defecto es obligatoria")
    private String descripcion;

    @NotNull(message = "La fecha de registro es obligatoria")
    private LocalDate fechaRegistro;

    @NotNull(message = "Debe indicar la cantidad afectada")
    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    private Integer cantidadAfectada;

    @NotNull(message = "Debe seleccionar el producto afectado")
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

