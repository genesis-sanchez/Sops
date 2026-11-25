package com.udh.sops.model;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CuidadoPendiente implements Comparable<CuidadoPendiente> {
    public final Mascota mascota;
    public final long registroId;
    public final String titulo, fecha;
    public final boolean vacuna;
    public final Date fechaOrden;

    public CuidadoPendiente(Mascota mascota, long registroId, String titulo, String fecha, boolean vacuna) {
        this.mascota = mascota;
        this.registroId = registroId;
        this.titulo = titulo;
        this.fecha = fecha;
        this.vacuna = vacuna;
        fechaOrden = leerFecha(fecha);
    }

    public static Date leerFecha(String texto) {
        if (texto == null) return null;
        texto = texto.trim();
        String patron;
        if (texto.matches("\\d{2}/\\d{2}/\\d{4}")) patron = "dd/MM/yyyy";
        else if (texto.matches("\\d{4}-\\d{2}-\\d{2}")) patron = "yyyy-MM-dd";
        else return null;
        SimpleDateFormat formato = new SimpleDateFormat(patron, Locale.ROOT);
        formato.setLenient(false);
        ParsePosition posicion = new ParsePosition(0);
        Date fecha = formato.parse(texto, posicion);
        return posicion.getIndex() == texto.length() ? fecha : null;
    }

    @Override public int compareTo(CuidadoPendiente otro) {
        if (fechaOrden == null) return otro.fechaOrden == null ? 0 : 1;
        return otro.fechaOrden == null ? -1 : fechaOrden.compareTo(otro.fechaOrden);
    }
}
