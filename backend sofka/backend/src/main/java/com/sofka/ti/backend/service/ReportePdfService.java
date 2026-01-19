package com.sofka.ti.backend.service;

import com.sofka.ti.backend.dto.reporte.CuentaReporteItem;
import com.sofka.ti.backend.dto.reporte.MovimientoReporteItem;
import com.sofka.ti.backend.dto.reporte.ReporteResponse;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

@Service
public class ReportePdfService {
    public byte[] generarPdf(ReporteResponse r) {
        try (PDDocument doc = new PDDocument(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            PDPage page = new PDPage(PDRectangle.LETTER);
            doc.addPage(page);

            try (PDPageContentStream cs = new PDPageContentStream(doc, page)) {
                float y = 740;

                cs.setFont(PDType1Font.HELVETICA_BOLD, 16);
                y = writeLine(cs, 60, y, "Estado de Cuenta");

                cs.setFont(PDType1Font.HELVETICA, 11);
                y = writeLine(cs, 60, y - 10, "Cliente: " + r.clienteNombre() + "  (" + r.clienteIdentificacion() + ")");
                y = writeLine(cs, 60, y - 4, "Rango: " + r.desde() + "  a  " + r.hasta());

                y = writeLine(cs, 60, y - 12, "Totales:");
                y = writeLine(cs, 80, y - 4, "Total creditos: " + r.totalCreditos());
                y = writeLine(cs, 80, y - 4, "Total debitos:  " + r.totalDebitos());

                y = writeLine(cs, 60, y - 14, "Cuentas:");
                cs.setFont(PDType1Font.HELVETICA_BOLD, 10);
                y = writeLine(cs, 60, y - 6, "Numero | Tipo | Saldo Inicial | Creditos | Debitos | Saldo Final");
                cs.setFont(PDType1Font.HELVETICA, 10);

                for (CuentaReporteItem c : r.cuentas()) {
                    String line = c.numeroCuenta() + " | " + c.tipoCuenta()
                            + " | " + c.saldoInicial()
                            + " | " + c.totalCreditos()
                            + " | " + c.totalDebitos()
                            + " | " + c.saldoFinal();
                    y = writeLine(cs, 60, y - 4, line);
                    if (y < 120) break;
                }

                y = writeLine(cs, 60, y - 14, "Movimientos:");
                cs.setFont(PDType1Font.HELVETICA_BOLD, 10);
                y = writeLine(cs, 60, y - 6, "Fecha | Tipo | Valor | Saldo | Cuenta");
                cs.setFont(PDType1Font.HELVETICA, 10);

                for (MovimientoReporteItem m : r.movimientos()) {
                    String line = m.fecha() + " | " + m.tipoMovimiento()
                            + " | " + m.valor()
                            + " | " + m.saldoDisponible()
                            + " | " + m.numeroCuenta();
                    y = writeLine(cs, 60, y - 4, line);
                    if (y < 80) break;
                }
            }

            doc.save(out);
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("No se pudo generar PDF", e);
        }
    }

    private float writeLine(PDPageContentStream cs, float x, float y, String text) throws Exception {
        cs.beginText();
        cs.newLineAtOffset(x, y);
        cs.showText(safe(text));
        cs.endText();
        return y - 14;
    }

    private String safe(String s) {
        if (s == null) return "";
        // PDFBox con Helvetica no soporta algunos caracteres raros; dejamos ASCII seguro.
        return s.replace("\t", " ").replace("\n", " ").replace("\r", " ");
    }
}
