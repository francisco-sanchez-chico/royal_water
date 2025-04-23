package com.app.royal.royal.servicios;

import com.app.royal.royal.modelos.Empleado;
import com.app.royal.royal.repositorios.EmpleadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmpleadoService {
    @Autowired
    private EmpleadoRepository empleadoRepository;

    public List<Empleado> listarEmpleados(){
        return empleadoRepository.findAll();
    }

    public void guardarEmpleado(Empleado empleado){
        empleadoRepository.save(empleado);
    }

    public Empleado obtenerPorId(Long id) {
        return empleadoRepository.findById(id).orElse(null);
    }

    public void eliminar(Long id) {
        empleadoRepository.deleteById(id);
    }

    public boolean existeUsuario(String usuario) {
        return empleadoRepository.existsByUsuario(usuario);
    }

    public boolean existeCorreo(String correo) {
        return empleadoRepository.existsByCorreo(correo);
    }

    public boolean existeUsuario(String usuario, Long idEmpleado) {
        return empleadoRepository.existsByUsuarioAndNoEmpleadoNot(usuario, idEmpleado);
    }

    public boolean existeCorreo(String correo, Long idEmpleado) {
        return empleadoRepository.existsByCorreoAndNoEmpleadoNot(correo, idEmpleado);
    }
}
