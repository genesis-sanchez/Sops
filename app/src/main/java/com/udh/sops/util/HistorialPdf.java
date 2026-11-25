package com.udh.sops.util;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import com.udh.sops.model.EntradaHistorial;
import com.udh.sops.model.Mascota;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class HistorialPdf {
    private final PdfDocument documento = new PdfDocument();
    private final Paint tinta = new Paint(Paint.ANTI_ALIAS_FLAG);
    private PdfDocument.Page pagina;
    private Canvas lienzo;
    private int numero;
    private float y;
    private void nuevaPagina() {
        if (pagina != null) cerrarPagina();
        pagina = documento.startPage(new PdfDocument.PageInfo.Builder(595, 842, ++numero).create());
        lienzo = pagina.getCanvas();
        tinta.setColor(Color.rgb(37, 109, 93)); tinta.setTextSize(16); tinta.setTypeface(Typeface.DEFAULT_BOLD);
        lienzo.drawText("Sops | Historial medico", 40, 42, tinta);
        y = 72;
    }
    private void cerrarPagina() {
        tinta.setColor(Color.DKGRAY); tinta.setTextSize(9); tinta.setTypeface(Typeface.DEFAULT);
        lienzo.drawText("Pagina " + numero, 40, 818, tinta);
        documento.finishPage(pagina);
        pagina = null;
    }
    private void texto(String texto, boolean titulo) {
        for (String parrafo : texto.split("\n", -1)) {
            String resto = parrafo;
            do {
                if (y > 780) nuevaPagina();
                tinta.setColor(Color.rgb(32, 50, 44)); tinta.setTextSize(titulo ? 13 : 11);
                tinta.setTypeface(titulo ? Typeface.DEFAULT_BOLD : Typeface.DEFAULT);
                int largo = resto.isEmpty() ? 0 : tinta.breakText(resto, true, 515, null);
                if (!resto.isEmpty() && largo == 0) largo = 1;
                if (largo < resto.length()) {
                    int espacio = resto.lastIndexOf(' ', largo);
                    if (espacio > 0) largo = espacio;
                }
                if (largo < resto.length() && largo > 0 && Character.isHighSurrogate(resto.charAt(largo - 1))) largo--;
                if (largo == 0 && !resto.isEmpty()) largo = Character.charCount(resto.codePointAt(0));
                lienzo.drawText(resto.substring(0, largo), 40, y, tinta);
                y += 17;
                resto = resto.substring(largo).replaceFirst("^ +", "");
            } while (!resto.isEmpty());
        }
    }
    private static String dato(String texto) { return texto == null || texto.trim().isEmpty() ? "sin registrar" : texto; }

    public static void escribir(Mascota mascota, List<EntradaHistorial> entradas, OutputStream salida) throws IOException {
        HistorialPdf pdf = new HistorialPdf();
        try {
            pdf.nuevaPagina();
            pdf.texto(mascota.getNombre(), true);
            pdf.texto("Especie: " + dato(mascota.getTipo()) + " | Raza: " + dato(mascota.getRaza()), false);
            pdf.texto("Nacimiento: " + dato(mascota.getFechaNacimiento()) + " | Peso (kg): " + dato(mascota.getPeso()), false);
            pdf.y += 12;
            if (entradas.isEmpty()) pdf.texto("No hay registros medicos guardados.", false);
            for (EntradaHistorial entrada : entradas) {
                if (pdf.y > 720) pdf.nuevaPagina();
                pdf.texto(entrada.tipo + " | " + entrada.titulo, true);
                pdf.texto("Fecha: " + (entrada.fecha == null ? "sin registrar" : entrada.fecha), false);
                pdf.texto(entrada.detalle, false);
                pdf.y += 14;
            }
            pdf.cerrarPagina();
            pdf.documento.writeTo(salida);
        } finally { pdf.documento.close(); }
    }
}
