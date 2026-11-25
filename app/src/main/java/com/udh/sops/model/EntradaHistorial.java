package com.udh.sops.model;

import java.util.Date;

public class EntradaHistorial implements Comparable<EntradaHistorial> {
    public final String tipo, titulo, fecha, detalle;
    public final long registroId;
    public EntradaHistorial(String tipo, long registroId, String titulo, String fecha, String detalle) {
        this.tipo = tipo; this.registroId = registroId; this.titulo = titulo;
        this.fecha = fecha; this.detalle = detalle;
    }
    @Override public int compareTo(EntradaHistorial otra) {
        Date a = CuidadoPendiente.leerFecha(fecha), b = CuidadoPendiente.leerFecha(otra.fecha);
        if (a == null && b != null) return 1;
        if (b == null && a != null) return -1;
        if (a != null && !a.equals(b)) return b.compareTo(a);
        int tipoOrden = tipo.compareTo(otra.tipo);
        return tipoOrden != 0 ? tipoOrden : Long.compare(otra.registroId, registroId);
    }
}
