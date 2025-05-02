package com.app.royal.royal.controlador;

import com.app.royal.royal.entidades.Producto;
import com.app.royal.royal.entidades.Categoria;
import com.app.royal.royal.servicios.ProductoService;
import com.app.royal.royal.servicios.CategoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ProductoViewController {

    private final ProductoService productoService;
    private final CategoriaService categoriaService;

    public ProductoViewController(ProductoService productoService, CategoriaService categoriaService) {
        this.productoService = productoService;
        this.categoriaService = categoriaService;
    }

    @GetMapping("/productos")
    public String verProductos(Model model) {
        List<Producto> productos = productoService.listarTodos();
        List<Categoria> categorias = categoriaService.listarTodas();

        model.addAttribute("productos", productos);
        model.addAttribute("categorias", categorias);

        return "productos";
    }

    @PostMapping("/productos")
    public String guardarProducto(@ModelAttribute Producto producto) {
        productoService.guardar(producto);
        return "redirect:/productos";
    }
}

