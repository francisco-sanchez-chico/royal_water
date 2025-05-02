package com.app.royal.royal.servicios;

import com.app.royal.royal.entidades.Defecto;
import com.app.royal.royal.entidades.Producto;
import com.app.royal.royal.repositorios.DefectoRepository;
import com.app.royal.royal.repositorios.ProductoRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

@Service
public class DefectoService {

    private final DefectoRepository defectoRepository;
    private final ProductoRepository productoRepository;

    public DefectoService(DefectoRepository defectoRepository, ProductoRepository productoRepository) {
        this.defectoRepository = defectoRepository;
        this.productoRepository = productoRepository;
    }

    public Defecto registrar(Defecto defecto) {
        Producto producto = productoRepository.findById(defecto.getProducto().getIdProducto())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        if (producto.getCantidadActual() < defecto.getCantidadAfectada()) {
            throw new RuntimeException("La cantidad afectada supera el inventario disponible.");
        }

        producto.setCantidadActual(producto.getCantidadActual() - defecto.getCantidadAfectada());
        productoRepository.save(producto);

        return defectoRepository.save(defecto);
    }

    public List<Defecto> listarTodos() {
        return defectoRepository.findAll();
    }

    public List<Defecto> buscarPorProducto(Long idProducto) {
        return defectoRepository.findByProductoIdProducto(idProducto);
    }

    public List<Defecto> buscarPorRangoFechas(LocalDate desde, LocalDate hasta) {
        LocalDateTime desdeInicio = desde.atStartOfDay();
        LocalDateTime hastaFin = hasta.atTime(23, 59, 59);
        return defectoRepository.findAll().stream()
                .filter(d -> d.getFechaRegistro().atStartOfDay().isAfter(desdeInicio.minusSeconds(1)) &&
                        d.getFechaRegistro().atStartOfDay().isBefore(hastaFin.plusSeconds(1)))
                .toList();
    }

    public ByteArrayInputStream generarReporteDefectosPDF(LocalDate desde, LocalDate hasta) {
        List<Defecto> defectos = buscarPorRangoFechas(desde, hasta);

        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // Logo
            String logoPath = "src/main/resources/static/logo.png";
            Image logo = Image.getInstance(logoPath);
            logo.scaleToFit(60, 60);
            logo.setAlignment(Image.ALIGN_LEFT);

            // Fecha del reporte
            Paragraph fecha = new Paragraph("Reporte del " + desde + " al " + hasta,
                    FontFactory.getFont(FontFactory.HELVETICA, 10));
            fecha.setAlignment(Element.ALIGN_RIGHT);

            // Título
            Paragraph titulo = new Paragraph("Reporte de Defectos (Filtrado)",
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16));
            titulo.setAlignment(Element.ALIGN_CENTER);

            document.add(logo);
            document.add(fecha);
            document.add(titulo);
            document.add(new Paragraph(" "));

            // Tabla
            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            Stream.of("Fecha", "Producto", "Tipo", "Cantidad", "Descripción").forEach(h -> {
                PdfPCell cell = new PdfPCell(new Phrase(h));
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                table.addCell(cell);
            });

            for (Defecto d : defectos) {
                table.addCell(d.getFechaRegistro().toString());
                table.addCell(d.getProducto().getNombre());
                table.addCell(d.getTipo().name());
                table.addCell(String.valueOf(d.getCantidadAfectada()));
                table.addCell(d.getDescripcion());
            }

            document.add(table);
            document.close();

        } catch (Exception e) {
            throw new RuntimeException("Error generando PDF de defectos", e);
        }

        return new ByteArrayInputStream(out.toByteArray());
    }


}

