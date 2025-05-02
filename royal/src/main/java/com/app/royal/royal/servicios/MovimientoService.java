package com.app.royal.royal.servicios;

import com.app.royal.royal.entidades.MotivoMovimiento;
import com.app.royal.royal.entidades.Movimiento;
import com.app.royal.royal.entidades.Producto;
import com.app.royal.royal.entidades.TipoMovimiento;
import com.app.royal.royal.repositorios.MovimientoRepository;
import com.app.royal.royal.repositorios.ProductoRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class MovimientoService {

    private final MovimientoRepository movimientoRepository;
    private final ProductoRepository productoRepository;

    public MovimientoService(MovimientoRepository movimientoRepository, ProductoRepository productoRepository) {
        this.movimientoRepository = movimientoRepository;
        this.productoRepository = productoRepository;
    }

    public List<Movimiento> buscarPorRangoFechas(LocalDate desde, LocalDate hasta) {
        LocalDateTime desdeInicio = desde.atStartOfDay();
        LocalDateTime hastaFin = hasta.atTime(23, 59, 59);
        return movimientoRepository.findByFechaBetween(desdeInicio, hastaFin);
    }


    public Movimiento guardar(Movimiento movimiento) {
        Producto producto = productoRepository.findById(movimiento.getProducto().getIdProducto())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        Integer cantidad = movimiento.getCantidad();

        if (movimiento.getTipo() == TipoMovimiento.ENTRADA) {
            producto.setCantidadActual(producto.getCantidadActual() + cantidad);
        } else if (movimiento.getTipo() == TipoMovimiento.SALIDA) {
            if (producto.getCantidadActual() < cantidad) {
                throw new RuntimeException("No hay suficiente inventario para la salida.");
            }
            producto.setCantidadActual(producto.getCantidadActual() - cantidad);
        }

        productoRepository.save(producto);
        return movimientoRepository.save(movimiento);
    }

    public List<Movimiento> listarTodos() {
        return movimientoRepository.findAll();
    }

    public Optional<Movimiento> buscarPorId(Long id) {
        return movimientoRepository.findById(id);
    }

    public List<Movimiento> buscarPorProducto(Long idProducto) {
        return movimientoRepository.findByProductoIdProducto(idProducto);
    }

    public ByteArrayInputStream generarReportePDF() {
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

            // Fecha actual
            Paragraph fecha = new Paragraph("Fecha: " + LocalDate.now(),
                    FontFactory.getFont(FontFactory.HELVETICA, 10));
            fecha.setAlignment(Element.ALIGN_RIGHT);

            // Título
            Font tituloFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
            Paragraph titulo = new Paragraph("Reporte de Movimientos", tituloFont);
            titulo.setAlignment(Element.ALIGN_CENTER);

            // Crear una tabla de 2 columnas para colocar logo y fecha en la misma fila
            PdfPTable cabecera = new PdfPTable(2);
            cabecera.setWidthPercentage(100);
            cabecera.setWidths(new float[]{1, 3}); // Logo pequeño, fecha más espacio

            PdfPCell celdaLogo = new PdfPCell(logo, false);
            celdaLogo.setBorder(Rectangle.NO_BORDER);
            PdfPCell celdaFecha = new PdfPCell(fecha);
            celdaFecha.setHorizontalAlignment(Element.ALIGN_RIGHT);
            celdaFecha.setVerticalAlignment(Element.ALIGN_TOP);
            celdaFecha.setBorder(Rectangle.NO_BORDER);

            cabecera.addCell(celdaLogo);
            cabecera.addCell(celdaFecha);
            document.add(cabecera);

            // Espacio y título
            document.add(new Paragraph(" "));
            document.add(titulo);
            document.add(new Paragraph(" "));

            // Tabla de datos
            PdfPTable tabla = new PdfPTable(6);
            tabla.setWidthPercentage(100);
            tabla.setSpacingBefore(10f);

            Stream.of("Fecha", "Producto", "Tipo", "Cantidad", "Motivo", "Usuario").forEach(header -> {
                PdfPCell th = new PdfPCell(new Phrase(header));
                th.setBackgroundColor(BaseColor.LIGHT_GRAY);
                tabla.addCell(th);
            });

            for (Movimiento m : movimientoRepository.findAll()) {
                tabla.addCell(m.getFecha().toString());
                tabla.addCell(m.getProducto().getNombre());
                tabla.addCell(m.getTipo().name());
                tabla.addCell(m.getCantidad().toString());
                tabla.addCell(m.getMotivo() == null ? "-" : m.getMotivo().name());
                tabla.addCell(m.getUsuario().getUsername());
            }

            document.add(tabla);
            document.close();

        } catch (Exception e) {
            throw new RuntimeException("Error generando PDF", e);
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    public ByteArrayInputStream generarReportePDFPorFechas(LocalDate desde, LocalDate hasta) {
        List<Movimiento> movimientos = buscarPorRangoFechas(desde, hasta);

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

            // Fecha
            Paragraph fecha = new Paragraph("Reporte del " + desde + " al " + hasta,
                    FontFactory.getFont(FontFactory.HELVETICA, 10));
            fecha.setAlignment(Element.ALIGN_RIGHT);

            // Título
            Paragraph titulo = new Paragraph("Reporte de Movimientos (Filtrado)",
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16));
            titulo.setAlignment(Element.ALIGN_CENTER);

            // Agregar elementos
            document.add(logo);
            document.add(fecha);
            document.add(titulo);
            document.add(new Paragraph(" ")); // espacio

            // Tabla
            PdfPTable table = new PdfPTable(6);
            table.setWidthPercentage(100);
            Stream.of("Fecha", "Producto", "Tipo", "Cantidad", "Motivo", "Usuario").forEach(h -> {
                PdfPCell cell = new PdfPCell(new Phrase(h));
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                table.addCell(cell);
            });

            for (Movimiento m : movimientos) {
                table.addCell(m.getFecha().toString());
                table.addCell(m.getProducto().getNombre());
                table.addCell(m.getTipo().name());
                table.addCell(String.valueOf(m.getCantidad()));
                table.addCell(m.getMotivo() == null ? "-" : m.getMotivo().name());
                table.addCell(m.getUsuario().getUsername());
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
            Sheet sheet = workbook.createSheet("Movimientos");

            // === Insertar logo ===
            InputStream logoStream = new FileInputStream("src/main/resources/static/logo.png");
            byte[] bytes = IOUtils.toByteArray(logoStream);
            int pictureIdx = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);
            logoStream.close();

            CreationHelper helper = workbook.getCreationHelper();
            Drawing<?> drawing = sheet.createDrawingPatriarch();
            ClientAnchor anchor = helper.createClientAnchor();
            anchor.setCol1(0);
            anchor.setRow1(0);
            Picture pict = drawing.createPicture(anchor, pictureIdx);
            pict.resize(1, 3); // ajustar tamaño

            // === Fecha del reporte ===
            Row fechaRow = sheet.createRow(0);
            Cell fechaCell = fechaRow.createCell(4);
            fechaCell.setCellValue("Fecha del reporte: " + LocalDate.now());

            // === Encabezados (desde la fila 2 en adelante) ===
            Row header = sheet.createRow(2);
            header.createCell(0).setCellValue("Fecha");
            header.createCell(1).setCellValue("Producto");
            header.createCell(2).setCellValue("Tipo");
            header.createCell(3).setCellValue("Cantidad");
            header.createCell(4).setCellValue("Motivo");
            header.createCell(5).setCellValue("Usuario");

            // === Cuerpo del reporte ===
            List<Movimiento> movimientos = movimientoRepository.findAll();
            int rowIdx = 3;
            for (Movimiento m : movimientos) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(m.getFecha().toString());
                row.createCell(1).setCellValue(m.getProducto().getNombre());
                row.createCell(2).setCellValue(m.getTipo().name());
                row.createCell(3).setCellValue(m.getCantidad());
                row.createCell(4).setCellValue(m.getMotivo() == null ? "-" : m.getMotivo().name());
                row.createCell(5).setCellValue(m.getUsuario().getUsername());
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());

        } catch (IOException e) {
            throw new RuntimeException("Error generando Excel", e);
        }
    }

    public ByteArrayInputStream generarReporteExcelPorFechas(LocalDate desde, LocalDate hasta) {
        List<Movimiento> movimientos = buscarPorRangoFechas(desde, hasta);

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Movimientos Filtrados");

            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("Fecha");
            header.createCell(1).setCellValue("Producto");
            header.createCell(2).setCellValue("Tipo");
            header.createCell(3).setCellValue("Cantidad");
            header.createCell(4).setCellValue("Motivo");
            header.createCell(5).setCellValue("Usuario");

            int rowIdx = 1;
            for (Movimiento m : movimientos) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(m.getFecha().toString());
                row.createCell(1).setCellValue(m.getProducto().getNombre());
                row.createCell(2).setCellValue(m.getTipo().name());
                row.createCell(3).setCellValue(m.getCantidad());
                row.createCell(4).setCellValue(m.getMotivo() == null ? "-" : m.getMotivo().name());
                row.createCell(5).setCellValue(m.getUsuario().getUsername());
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());

        } catch (IOException e) {
            throw new RuntimeException("Error generando Excel", e);
        }
    }

    public List<Movimiento> buscarVentas() {
        return movimientoRepository.findAll().stream()
                .filter(m -> m.getTipo() == TipoMovimiento.SALIDA && m.getMotivo() == MotivoMovimiento.VENTA)
                .toList();
    }

    public ByteArrayInputStream generarReporteVentasPDF() {
        List<Movimiento> ventas = buscarVentas();

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

            // Fecha
            Paragraph fecha = new Paragraph("Fecha de generación: " + LocalDate.now(),
                    FontFactory.getFont(FontFactory.HELVETICA, 10));
            fecha.setAlignment(Element.ALIGN_RIGHT);

            // Título
            Paragraph titulo = new Paragraph("Reporte de Ventas (Movimientos de salida)",
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16));
            titulo.setAlignment(Element.ALIGN_CENTER);

            document.add(logo);
            document.add(fecha);
            document.add(titulo);
            document.add(new Paragraph(" "));

            // Tabla
            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            Stream.of("Fecha", "Producto", "Cantidad", "Motivo", "Usuario").forEach(h -> {
                PdfPCell cell = new PdfPCell(new Phrase(h));
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                table.addCell(cell);
            });

            for (Movimiento m : ventas) {
                table.addCell(m.getFecha().toString());
                table.addCell(m.getProducto().getNombre());
                table.addCell(String.valueOf(m.getCantidad()));
                table.addCell(m.getMotivo().name());
                table.addCell(m.getUsuario().getUsername());
            }

            document.add(table);
            document.close();

        } catch (Exception e) {
            throw new RuntimeException("Error generando PDF de ventas", e);
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    public ByteArrayInputStream generarReporteVentasExcel() {
        List<Movimiento> ventas = buscarVentas();

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Ventas");

            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("Fecha");
            header.createCell(1).setCellValue("Producto");
            header.createCell(2).setCellValue("Cantidad");
            header.createCell(3).setCellValue("Motivo");
            header.createCell(4).setCellValue("Usuario");

            int rowIdx = 1;
            for (Movimiento m : ventas) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(m.getFecha().toString());
                row.createCell(1).setCellValue(m.getProducto().getNombre());
                row.createCell(2).setCellValue(m.getCantidad());
                row.createCell(3).setCellValue(m.getMotivo().name());
                row.createCell(4).setCellValue(m.getUsuario().getUsername());
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());

        } catch (IOException e) {
            throw new RuntimeException("Error generando Excel de ventas", e);
        }
    }

}
