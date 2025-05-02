package com.app.royal.royal.entidades;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProducto;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "La descripción es obligatoria")
    private String descripcion;

    @NotNull(message = "La cantidad actual es obligatoria")
    @Min(value = 0, message = "La cantidad no puede ser negativa")
    private Integer cantidadActual;

    @NotNull(message = "La fecha de ingreso es obligatoria")
    private LocalDate fechaIngreso;

    @NotNull(message = "Debe seleccionar una categoría")
    @ManyToOne
    @JoinColumn(name = "id_categoria", nullable = false)
    private Categoria categoria;

    @NotNull(message = "Debe especificar el stock mínimo")
    @Min(value = 0, message = "El stock mínimo no puede ser negativo")
    private Integer stockMinimo;

    public Producto() {
        
    }

    public Producto(Object o, String nombre, String descripcion, Integer cantidadActual, LocalDate fechaIngreso, Categoria cat) {
    }

    public Long getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Long idProducto) {
        this.idProducto = idProducto;
    }

    public @NotBlank(message = "El nombre es obligatorio") String getNombre() {
        return nombre;
    }

    public void setNombre(@NotBlank(message = "El nombre es obligatorio") String nombre) {
        this.nombre = nombre;
    }

    public @NotBlank(message = "La descripción es obligatoria") String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(@NotBlank(message = "La descripción es obligatoria") String descripcion) {
        this.descripcion = descripcion;
    }

    public @NotNull(message = "La cantidad actual es obligatoria") @Min(value = 0, message = "La cantidad no puede ser negativa") Integer getCantidadActual() {
        return cantidadActual;
    }

    public void setCantidadActual(@NotNull(message = "La cantidad actual es obligatoria") @Min(value = 0, message = "La cantidad no puede ser negativa") Integer cantidadActual) {
        this.cantidadActual = cantidadActual;
    }

    public @NotNull(message = "La fecha de ingreso es obligatoria") LocalDate getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(@NotNull(message = "La fecha de ingreso es obligatoria") LocalDate fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public @NotNull(message = "Debe seleccionar una categoría") Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(@NotNull(message = "Debe seleccionar una categoría") Categoria categoria) {
        this.categoria = categoria;
    }

    public @NotNull(message = "Debe especificar el stock mínimo") @Min(value = 0, message = "El stock mínimo no puede ser negativo") Integer getStockMinimo() {
        return stockMinimo;
    }

    public void setStockMinimo(@NotNull(message = "Debe especificar el stock mínimo") @Min(value = 0, message = "El stock mínimo no puede ser negativo") Integer stockMinimo) {
        this.stockMinimo = stockMinimo;
    }
}


