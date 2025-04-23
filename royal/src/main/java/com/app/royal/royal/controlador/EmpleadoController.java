package com.app.royal.royal.controlador;

import com.app.royal.royal.modelos.Empleado;
import com.app.royal.royal.servicios.EmpleadoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/empleados")
public class EmpleadoController {
    @Autowired
    private EmpleadoService empleadoService;

    @GetMapping
    public String listarEmpleados(Model model) {
        model.addAttribute("empleados", empleadoService.listarEmpleados());
        return "empleados/listar";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("empleado", new Empleado());
        return "empleados/formulario";
    }

    @PostMapping("/guardar")
    public String guardarEmpleado(
            @Valid @ModelAttribute Empleado empleado,
            BindingResult result,
            Model model
    ) {
        if (result.hasErrors()) {
            return "empleados/formulario";
        }

        // Obtener el empleado actual (si existe) para comparar
        Empleado empleadoExistente = null;
        if (empleado.getNoEmpleado() != null) {
            empleadoExistente = empleadoService.obtenerPorId(empleado.getNoEmpleado());
        }

        // Validar usuario solo si es nuevo o ha cambiado
        if (empleadoExistente == null || !empleado.getUsuario().equals(empleadoExistente.getUsuario())) {
            if (empleadoService.existeUsuario(empleado.getUsuario(), empleado.getNoEmpleado())) {
                model.addAttribute("errorUsuario", "El usuario ya está registrado");
                return "empleados/formulario";
            }
        }

        // Validar correo solo si es nuevo o ha cambiado
        if (empleadoExistente == null || !empleado.getCorreo().equals(empleadoExistente.getCorreo())) {
            if (empleadoService.existeCorreo(empleado.getCorreo(), empleado.getNoEmpleado())) {
                model.addAttribute("errorCorreo", "El correo ya está registrado");
                return "empleados/formulario";
            }
        }

        empleadoService.guardarEmpleado(empleado);
        return "redirect:/empleados";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        model.addAttribute("empleado", empleadoService.obtenerPorId(id));
        return "empleados/formulario";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarEmpleado(@PathVariable Long id) {
        empleadoService.eliminar(id);
        return "redirect:/empleados";
    }
}
