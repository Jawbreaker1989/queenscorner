package com.uptc.queenscorner.services.impl;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.uptc.queenscorner.models.entities.FacturaEntity;
import com.uptc.queenscorner.models.entities.LineaFacturaEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;

@Service
public class FacturaPdfGeneratorService {

    @Value("${app.factura.pdf.path:/tmp/facturas/}")
    private String pdfPath;

    private final DecimalFormat df = new DecimalFormat("$#,##0.00");
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public String generarPdfFactura(FacturaEntity factura) throws Exception {
        File dir = new File(pdfPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String nombreArchivo = "FAC_" + factura.getNumeroFactura().replace("-", "_") + ".pdf";
        String rutaCompleta = pdfPath + nombreArchivo;

        PdfWriter writer = new PdfWriter(rutaCompleta);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc, PageSize.A4);
        document.setMargins(20, 20, 20, 20);

        agregarEncabezado(document, factura);
        agregarInfoFactura(document, factura);
        agregarDatosCliente(document, factura);
        agregarTablaLineas(document, factura);
        agregarTotales(document, factura);
        agregarPiePagina(document, factura);

        document.close();

        return rutaCompleta;
    }

    private void agregarEncabezado(Document document, FacturaEntity factura) throws Exception {
        Table encabezado = new Table(UnitValue.createPercentArray(2)).useAllAvailableWidth();

        Cell leftCell = new Cell();
        leftCell.setBorder(null);
        leftCell.add(new Paragraph("QUEEN'S CORNER").setFontSize(20).setBold());
        leftCell.add(new Paragraph("NIT: 123.456.789-0").setFontSize(10));
        leftCell.add(new Paragraph("Dirección: Calle 123 #45-67, Bogotá D.C.").setFontSize(10));
        leftCell.add(new Paragraph("Teléfono: +57 1 2345678").setFontSize(10));
        leftCell.add(new Paragraph("Email: info@queenscorner.com").setFontSize(10));

        Cell rightCell = new Cell();
        rightCell.setBorder(null);
        rightCell.setTextAlignment(TextAlignment.RIGHT);
        rightCell.add(new Paragraph("FACTURA").setFontSize(18).setBold());
        rightCell.add(new Paragraph("No: " + factura.getNumeroFactura()).setFontSize(12).setBold());
        rightCell.add(new Paragraph("Fecha: " + factura.getFechaEmision().format(dateFormatter)).setFontSize(10));
        rightCell.add(new Paragraph("Vencimiento: " + factura.getFechaVencimiento()).setFontSize(10));

        encabezado.addCell(leftCell);
        encabezado.addCell(rightCell);

        document.add(encabezado);
        document.add(new Paragraph("\n"));
    }

    private void agregarInfoFactura(Document document, FacturaEntity factura) {
        Table infoTable = new Table(UnitValue.createPercentArray(2)).useAllAvailableWidth();
        infoTable.setMarginBottom(10);

        Cell cell1 = new Cell();
        cell1.add(new Paragraph("Factura emitida por: " + (factura.getUsuarioEmision() != null ? factura.getUsuarioEmision() : "N/A")).setFontSize(9));
        cell1.add(new Paragraph("Estado: " + factura.getEstado().toString()).setFontSize(9).setBold());
        infoTable.addCell(cell1);

        Cell cell2 = new Cell();
        cell2.setTextAlignment(TextAlignment.RIGHT);
        cell2.add(new Paragraph("Medio de Pago: " + factura.getMedioPago().getDescripcion()).setFontSize(9));
        if (factura.getReferenciaPago() != null) {
            cell2.add(new Paragraph("Referencia: " + factura.getReferenciaPago()).setFontSize(9));
        }
        infoTable.addCell(cell2);

        document.add(infoTable);
    }

    private void agregarDatosCliente(Document document, FacturaEntity factura) {
        document.add(new Paragraph("FACTURADO A:").setBold().setMarginTop(10).setMarginBottom(5));

        Table clienteTable = new Table(UnitValue.createPercentArray(2)).useAllAvailableWidth();
        clienteTable.setMarginBottom(15);

        Cell labelCell = new Cell();
        labelCell.add(new Paragraph("Razón Social: ").setBold().setFontSize(10));
        labelCell.add(new Paragraph("NIT/Cédula: ").setBold().setFontSize(10));
        labelCell.add(new Paragraph("Contacto: ").setBold().setFontSize(10));
        labelCell.add(new Paragraph("Email: ").setBold().setFontSize(10));
        labelCell.add(new Paragraph("Teléfono: ").setBold().setFontSize(10));

        Cell dataCell = new Cell();
        dataCell.add(new Paragraph(factura.getNegocio().getNombreCliente()).setFontSize(10));
        dataCell.add(new Paragraph(factura.getNegocio().getRutCliente()).setFontSize(10));
        dataCell.add(new Paragraph(factura.getNegocio().getNombreCliente()).setFontSize(10));
        dataCell.add(new Paragraph(factura.getNegocio().getEmailCliente()).setFontSize(10));
        dataCell.add(new Paragraph(factura.getNegocio().getTelefonoCliente()).setFontSize(10));

        clienteTable.addCell(labelCell);
        clienteTable.addCell(dataCell);

        document.add(clienteTable);
    }

    private void agregarTablaLineas(Document document, FacturaEntity factura) {
        Table lineasTable = new Table(UnitValue.createPercentArray(new float[]{5, 45, 15, 15, 20}))
                .useAllAvailableWidth();

        lineasTable.addHeaderCell(crearCeldaEncabezado("Item"));
        lineasTable.addHeaderCell(crearCeldaEncabezado("Descripción"));
        lineasTable.addHeaderCell(crearCeldaEncabezado("Cantidad"));
        lineasTable.addHeaderCell(crearCeldaEncabezado("V. Unitario"));
        lineasTable.addHeaderCell(crearCeldaEncabezado("V. Total"));

        for (LineaFacturaEntity linea : factura.getLineas()) {
            lineasTable.addCell(new Cell().add(new Paragraph(String.valueOf(linea.getNumeroLinea())).setFontSize(9)));
            lineasTable.addCell(new Cell().add(new Paragraph(linea.getDescripcion()).setFontSize(9)));
            lineasTable.addCell(new Cell().add(new Paragraph(String.valueOf(linea.getCantidad())).setFontSize(9)));
            lineasTable.addCell(new Cell().add(new Paragraph(df.format(linea.getValorUnitario())).setFontSize(9)));
            lineasTable.addCell(new Cell().add(new Paragraph(df.format(linea.getValorLinea())).setFontSize(9)));
        }

        document.add(lineasTable);
        document.add(new Paragraph("\n"));
    }

    private void agregarTotales(Document document, FacturaEntity factura) {
        Table totalesTable = new Table(UnitValue.createPercentArray(new float[]{70, 30}))
                .useAllAvailableWidth();
        totalesTable.setMarginBottom(20);

        Cell anticipoLabel = new Cell();
        anticipoLabel.setTextAlignment(TextAlignment.RIGHT);
        anticipoLabel.add(new Paragraph("Anticipo pagado:").setBold());
        Cell anticipoValue = new Cell();
        anticipoValue.add(new Paragraph(df.format(factura.getAnticipo())));
        totalesTable.addCell(anticipoLabel);
        totalesTable.addCell(anticipoValue);

        Cell subtotalLabel = new Cell();
        subtotalLabel.setTextAlignment(TextAlignment.RIGHT);
        subtotalLabel.add(new Paragraph("Subtotal items:").setBold());
        Cell subtotalValue = new Cell();
        subtotalValue.add(new Paragraph(df.format(factura.getSubtotalItems())));
        totalesTable.addCell(subtotalLabel);
        totalesTable.addCell(subtotalValue);

        Cell baseLabel = new Cell();
        baseLabel.setTextAlignment(TextAlignment.RIGHT);
        baseLabel.add(new Paragraph("Base gravable:").setBold());
        Cell baseValue = new Cell();
        baseValue.add(new Paragraph(df.format(factura.calcularBaseGravable())));
        totalesTable.addCell(baseLabel);
        totalesTable.addCell(baseValue);

        Cell ivaLabel = new Cell();
        ivaLabel.setTextAlignment(TextAlignment.RIGHT);
        ivaLabel.add(new Paragraph("IVA (19%):").setBold());
        Cell ivaValue = new Cell();
        ivaValue.add(new Paragraph(df.format(factura.calcularIva())));
        totalesTable.addCell(ivaLabel);
        totalesTable.addCell(ivaValue);

        Cell totalLabel = new Cell();
        totalLabel.setTextAlignment(TextAlignment.RIGHT);
        totalLabel.add(new Paragraph("TOTAL A PAGAR:").setBold().setFontSize(12));
        totalLabel.setBackgroundColor(com.itextpdf.kernel.colors.ColorConstants.LIGHT_GRAY);
        Cell totalValue = new Cell();
        totalValue.add(new Paragraph(df.format(factura.calcularTotal())).setBold().setFontSize(12));
        totalValue.setBackgroundColor(com.itextpdf.kernel.colors.ColorConstants.LIGHT_GRAY);
        totalesTable.addCell(totalLabel);
        totalesTable.addCell(totalValue);

        document.add(totalesTable);
    }

    private void agregarPiePagina(Document document, FacturaEntity factura) {
        document.add(new Paragraph("\n"));
        if (factura.getCondicionesPago() != null && !factura.getCondicionesPago().isEmpty()) {
            document.add(new Paragraph("Condiciones de Pago:").setBold().setFontSize(10));
            document.add(new Paragraph(factura.getCondicionesPago()).setFontSize(9));
        }

        if (factura.getNotas() != null && !factura.getNotas().isEmpty()) {
            document.add(new Paragraph("\nNotas:").setBold().setFontSize(10));
            document.add(new Paragraph(factura.getNotas()).setFontSize(9));
        }

        document.add(new Paragraph("\n\nGracias por su compra").setTextAlignment(TextAlignment.CENTER).setFontSize(10));
    }

    private Cell crearCeldaEncabezado(String texto) {
        Cell cell = new Cell();
        cell.setBackgroundColor(com.itextpdf.kernel.colors.ColorConstants.LIGHT_GRAY);
        cell.add(new Paragraph(texto).setBold().setFontSize(10));
        return cell;
    }
}
