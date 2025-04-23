package com.app.royal.royal.modelos;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "empleados", uniqueConstraints = {
        @UniqueConstraint(columnNames = "usuario"),
        @UniqueConstraint(columnNames = "correo")
})
public class Empleado {
    @Id
    @Column(name = "no_empleado")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long noEmpleado;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El primer apellido es obligatorio")
    @Column(name = "apellido1")
    private String apellido1;

    @NotBlank(message = "El segundo apellido es obligatorio")
    @Column(name = "apellido2")
    private String apellido2;

    @Setter
    @Getter
    @NotBlank(message = "El usuario es obligatorio")
    private String usuario;

    @NotBlank(message = "El correo es obligatorio")
    private String correo;

    @NotBlank(message = "La contraseña es obligatoria")
    private String contrasenia;

    public Long getNoEmpleado() {
        return noEmpleado;
    }

    public void setNoEmpleado(Long noEmpleado) {
        this.noEmpleado = noEmpleado;
    }

    public @NotBlank(message = "El nombre es obligatorio") String getNombre() {
        return nombre;
    }

    public void setNombre(@NotBlank(message = "El nombre es obligatorio") String nombre) {
        this.nombre = nombre;
    }

    public @NotBlank(message = "El primer apellido es obligatorio") String getApellido1() {
        return apellido1;
    }

    public void setApellido1(@NotBlank(message = "El primer apellido es obligatorio") String apellido1) {
        this.apellido1 = apellido1;
    }

    public @NotBlank(message = "El segundo apellido es obligatorio") String getApellido2() {
        return apellido2;
    }

    public void setApellido2(@NotBlank(message = "El segundo apellido es obligatorio") String apellido2) {
        this.apellido2 = apellido2;
    }

    public @NotBlank(message = "El usuario es obligatorio") String getUsuario() {
        return usuario;
    }

    public void setUsuario(@NotBlank(message = "El usuario es obligatorio") String usuario) {
        this.usuario = usuario;
    }

    public @NotBlank(message = "El correo es obligatorio") String getCorreo() {
        return correo;
    }

    public void setCorreo(@NotBlank(message = "El correo es obligatorio") String correo) {
        this.correo = correo;
    }

    public @NotBlank(message = "La contraseña es obligatoria") String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(@NotBlank(message = "La contraseña es obligatoria") String contrasenia) {
        this.contrasenia = contrasenia;
    }
}
