package com.udh.sops.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.udh.sops.R;
import com.udh.sops.model.Mascota;
import java.util.ArrayList;
import java.util.List;

public class MascotaAdapter extends RecyclerView.Adapter<MascotaAdapter.MascotaViewHolder> {
    public interface AlPulsarMascota {
        void abrir(Mascota mascota);
    }

    private final List<Mascota> mascotas;
    private final AlPulsarMascota alPulsar;

    public MascotaAdapter(List<Mascota> mascotas, AlPulsarMascota alPulsar) {
        this.mascotas = new ArrayList<>(mascotas);
        this.alPulsar = alPulsar;
    }

    @NonNull
    @Override
    public MascotaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MascotaViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_mascota, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MascotaViewHolder holder, int position) {
        Mascota mascota = mascotas.get(position);
        holder.nombre.setText(mascota.getNombre());
        holder.raza.setText(mascota.getRaza());
        holder.resumen.setText(holder.itemView.getContext().getString(
                R.string.mascota_resumen, mascota.getTipo(), mascota.getEdad()));
        holder.itemView.setOnClickListener(view -> alPulsar.abrir(mascota));
    }

    @Override
    public int getItemCount() {
        return mascotas.size();
    }

    public static class MascotaViewHolder extends RecyclerView.ViewHolder {
        private final TextView nombre;
        private final TextView raza;
        private final TextView resumen;

        public MascotaViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.tvNombreMascota);
            raza = itemView.findViewById(R.id.tvRazaMascota);
            resumen = itemView.findViewById(R.id.tvResumenMascota);
        }
    }
}
