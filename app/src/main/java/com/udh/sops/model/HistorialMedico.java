package com.udh.sops.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HistorialMedico {
    private static String dato(String texto) { return texto == null || texto.trim().isEmpty() ? "sin registrar" : texto; }
    public static List<EntradaHistorial> cargar(SopsDatabase base, long mascotaId) {
        List<EntradaHistorial> lista = new ArrayList<>();
        base.runInTransaction(() -> {
            for (Vacuna v : base.vacunaDao().obtenerPorMascota(mascotaId)) {
                lista.add(new EntradaHistorial("vacuna", v.getId(), dato(v.getNombre()), v.getFecha(),
                        "estado: " + (v.isAplicada() ? "aplicada" : "pendiente") + "\ndosis: " + v.getDosis()
                        + "\nproxima fecha: " + dato(v.getProximaFecha()) + "\nveterinario: " + dato(v.getVeterinario())
                        + "\nobservaciones: " + dato(v.getObservaciones())));
            }
            for (VisitaVeterinaria v : base.visitaVeterinariaDao().obtenerPorMascota(mascotaId)) {
                lista.add(new EntradaHistorial("visita", v.getId(), dato(v.getMotivo()), v.getFecha(),
                        "diagnostico: " + dato(v.getDiagnostico()) + "\ntratamiento: " + dato(v.getTratamiento())
                        + "\nveterinario: " + dato(v.getVeterinario()) + "\nclinica: " + dato(v.getClinica())
                        + "\npeso (kg): " + dato(v.getPeso()) + "\nproxima revision: " + dato(v.getProximaRevision())
                        + "\nobservaciones: " + dato(v.getObservaciones())));
            }
            for (Medicamento m : base.medicamentoDao().obtenerPorMascota(mascotaId)) {
                lista.add(new EntradaHistorial("medicamento", m.getId(), dato(m.getNombre()), m.getFechaInicio(),
                        "dosis: " + dato(m.getDosis()) + "\ncada " + m.getIntervaloHoras() + " horas"
                        + "\nfecha de fin: " + dato(m.getFechaFin()) + "\nindicaciones: " + dato(m.getIndicaciones())
                        + "\nestado: " + (m.isActivo() ? "activo" : "inactivo")));
            }
        });
        Collections.sort(lista);
        return lista;
    }
}
