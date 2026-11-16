package com.udh.sops.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.udh.sops.R;
import com.udh.sops.model.Vacuna;
import java.util.ArrayList;
import java.util.List;

public class VacunaAdapter extends RecyclerView.Adapter<VacunaAdapter.VacunaViewHolder> {
    public interface AlPulsarVacuna {
        void abrir(Vacuna vacuna);
    }

    private final List<Vacuna> vacunas;
    private final AlPulsarVacuna alPulsar;

    public VacunaAdapter(List<Vacuna> vacunas, AlPulsarVacuna alPulsar) {
        this.vacunas = new ArrayList<>(vacunas);
        this.alPulsar = alPulsar;
    }

    @NonNull @Override
    public VacunaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VacunaViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_vacuna, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VacunaViewHolder holder, int position) {
        Vacuna vacuna = vacunas.get(position);
        holder.nombre.setText(vacuna.getNombre());
        holder.estado.setText(vacuna.isAplicada() ? R.string.vacuna_aplicada : R.string.vacuna_pendiente);
        holder.fechas.setText(holder.itemView.getContext().getString(R.string.vacuna_fechas,
                vacuna.getDosis(), holder.itemView.getContext().getString(vacuna.isAplicada()
                        ? R.string.fecha_aplicada : R.string.fecha_programada),
                vacuna.getFecha(), vacuna.getProximaFecha()));
        holder.itemView.setOnClickListener(view -> alPulsar.abrir(vacuna));
    }

    @Override public int getItemCount() { return vacunas.size(); }

    public static class VacunaViewHolder extends RecyclerView.ViewHolder {
        private final TextView nombre, estado, fechas;
        public VacunaViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.tvNombreVacuna);
            estado = itemView.findViewById(R.id.tvEstadoVacuna);
            fechas = itemView.findViewById(R.id.tvFechasVacuna);
        }
    }
}
