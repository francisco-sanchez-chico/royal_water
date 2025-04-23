package com.app.royal.royal.repositorios;

import com.app.royal.royal.modelos.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmpleadoRepository extends JpaRepository<Empleado,Long> {
    boolean existsByUsuario(String usuario);

    boolean existsByCorreo(String correo);

    boolean existsByUsuarioAndNoEmpleadoNot(String usuario, Long noEmpleado);

    boolean existsByCorreoAndNoEmpleadoNot(String correo, Long noEmpleado);
}
