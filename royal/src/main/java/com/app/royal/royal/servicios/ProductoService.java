package com.app.royal.royal.servicios;

import com.app.royal.royal.entidades.Producto;
import com.app.royal.royal.repositorios.ProductoRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    // Guardar producto
    public Producto guardar(Producto producto) {
        return productoRepository.save(producto);
    }

    // Listar todos los productos
    public List<Producto> listarTodos() {
        return productoRepository.findAll();
    }

    // Buscar por ID
    public Optional<Producto> buscarPorId(Long id) {
        return productoRepository.findById(id);
    }

    // Buscar por nombre parcial
    public List<Producto> buscarPorNombre(String nombre) {
        return productoRepository.findByNombreContainingIgnoreCase(nombre);
    }

    // Eliminar producto por ID
    public void eliminar(Long id) {
        productoRepository.deleteById(id);
    }

    public ByteArrayInputStream generarReportePDF() {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // Logo (esquina superior izquierda)
            String logoPath = "src/main/resources/static/logo.png";
            Image logo = Image.getInstance(logoPath);
            logo.scaleToFit(60, 60);
            logo.setAlignment(Image.ALIGN_LEFT);

            // Fecha (esquina superior derecha)
            Paragraph fecha = new Paragraph("Fecha: " + LocalDate.now(),
                    FontFactory.getFont(FontFactory.HELVETICA, 10));
            fecha.setAlignment(Element.ALIGN_RIGHT);

            // Cabecera
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
            Paragraph titulo = new Paragraph("Reporte de Inventario", headerFont);
            titulo.setAlignment(Element.ALIGN_CENTER);

            // Agregar elementos
            document.add(logo);
            document.add(fecha);
            document.add(titulo);
            document.add(new Paragraph(" ")); // espacio

            // Tabla
            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            Stream.of("Producto", "Categoría", "Cantidad").forEach(headerTitle -> {
                PdfPCell header = new PdfPCell();
                header.setPhrase(new Phrase(headerTitle));
                header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                table.addCell(header);
            });

            for (Producto p : productoRepository.findAll()) {
                table.addCell(p.getNombre());
                table.addCell(p.getCategoria().getNombre());
                table.addCell(String.valueOf(p.getCantidadActual()));
            }

            document.add(table);
            document.close();

        } catch (Exception e) {
            throw new RuntimeException("Error generando PDF", e);
        }

        return new ByteArrayInputStream(out.toByteArray());
    }


    public ByteArrayInputStream generarReporteExcel() {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Inventario");

            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("Producto");
            header.createCell(1).setCellValue("Categoría");
            header.createCell(2).setCellValue("Cantidad");

            List<Producto> productos = productoRepository.findAll();
            int rowIdx = 1;
            for (Producto p : productos) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(p.getNombre());
                row.createCell(1).setCellValue(p.getCategoria().getNombre());
                row.createCell(2).setCellValue(p.getCantidadActual());
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());

        } catch (IOException e) {
            throw new RuntimeException("Error generando Excel", e);
        }
    }

    public List<Producto> obtenerProductosConStockBajo() {
        return productoRepository.findAll().stream()
                .filter(p -> p.getCantidadActual() < p.getStockMinimo())
                .toList();
    }

}

