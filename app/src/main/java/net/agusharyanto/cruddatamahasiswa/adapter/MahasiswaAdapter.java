package net.agusharyanto.cruddatamahasiswa.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.agusharyanto.cruddatamahasiswa.R;
import net.agusharyanto.cruddatamahasiswa.model.Mahasiswa;

import java.util.List;

/**
 * Created by agus on 12/22/16.
 */

public class MahasiswaAdapter extends RecyclerView.Adapter<MahasiswaAdapter.MahasiswaViewHolder> {
    List<Mahasiswa> mahasiswas;

    public MahasiswaAdapter(List<Mahasiswa> mahasiswas) {
        this.mahasiswas = mahasiswas;
    }
    @Override
    public MahasiswaViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_mahasiswa, viewGroup, false);
        MahasiswaViewHolder mahasiswaViewHolder = new MahasiswaViewHolder(v);
        return mahasiswaViewHolder;
    }
    @Override
    public void onBindViewHolder(MahasiswaViewHolder mahasiswaViewHolder, int i) {
        mahasiswaViewHolder.mahasiswaName.setText(mahasiswas.get(i).getNama());
        mahasiswaViewHolder.mahasiswaJurusan.setText(mahasiswas.get(i).getJurusan());
        mahasiswaViewHolder.mahasiswaNim.setText(mahasiswas.get(i).getNim());
    }
    @Override
    public int getItemCount() {
        return mahasiswas.size();
    }
    public Mahasiswa getItem(int position) {
        return mahasiswas.get(position);
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class MahasiswaViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView mahasiswaName;
        TextView mahasiswaJurusan;
        TextView mahasiswaNim;
        MahasiswaViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv);
            mahasiswaName = (TextView) itemView.findViewById(R.id.textViewRowNama);
            mahasiswaJurusan = (TextView) itemView.findViewById(R.id.textViewRowJurusan);
            mahasiswaNim = (TextView) itemView.findViewById(R.id.textViewRowNim);
        }
    }
}