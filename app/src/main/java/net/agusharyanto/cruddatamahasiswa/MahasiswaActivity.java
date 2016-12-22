package net.agusharyanto.cruddatamahasiswa;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import net.agusharyanto.cruddatamahasiswa.model.Mahasiswa;

public class MahasiswaActivity extends AppCompatActivity {

    private EditText editTextNIM, editTextNama, editTextJurusan;
    private Button buttonSave, buttonHapus;
    private Mahasiswa mahasiswa;
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase db;
    private String action_flag="add";
    private String refreshFlag="0";
    private static final String TAG="AddEditActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mahasiswa);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        databaseHelper = new DatabaseHelper(MahasiswaActivity.this);
        db= databaseHelper.getWritableDatabase();
        mahasiswa = new Mahasiswa();
        initUI();
        //initEvent();
        Intent intent = getIntent();
        if (intent.hasExtra("mahasiswa")) {
            mahasiswa = (Mahasiswa) intent.getSerializableExtra("mahasiswa");
            Log.d(TAG, "Mahasiswa : " + mahasiswa.toString());
            setData(mahasiswa);
            action_flag = "edit";
            editTextNIM.setEnabled(false);
        }else{
            mahasiswa = new Mahasiswa();
        }
    }
    private void setData(Mahasiswa mahasiswa) {
        editTextNIM.setText(mahasiswa.getNim());
        editTextNama.setText(mahasiswa.getNama());
        editTextJurusan.setText(mahasiswa.getJurusan());
    }
    private void initUI() {
        editTextNIM = (EditText) findViewById(R.id.editTextNim);
        editTextNama = (EditText) findViewById(R.id.editTextNama);
        editTextJurusan = (EditText) findViewById(R.id.editTextJurusan);
    }
    private void initEvent() {
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });
        buttonHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hapusData();
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_mahasiswa, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_save) {
            saveData();
            return true;
        }else  if (id == R.id.action_delete) {
            hapusData();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void saveData() {
        String nama = editTextNama.getText().toString();
        String nim = editTextNIM.getText().toString();
        String jurusan = editTextJurusan.getText().toString();
        mahasiswa.setNim(nim);
        mahasiswa.setNama(nama);
        mahasiswa.setJurusan(jurusan);
        long rowaffect=0;
        if (action_flag.equals("add")) {
            rowaffect = databaseHelper.insertMahasiswa(mahasiswa, db);
        }else if (action_flag.equals("edit")){
            rowaffect = databaseHelper.updateMahasiswa(mahasiswa, db);
        }
        if (rowaffect > 0){
            Toast.makeText(getBaseContext(), "Save Data Sukses", Toast.LENGTH_SHORT).show();
            refreshFlag="1";
            finish();
        }else{
            Toast.makeText(getBaseContext(), "Save Data Gagal", Toast.LENGTH_SHORT).show();
        }
    }
    private void hapusData() {
        new AlertDialog.Builder(this)
                .setTitle("Data Mahasiswa")
                .setMessage("Hapus Data " + mahasiswa.getNama() + " ?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        databaseHelper.deleteMahasiswa(mahasiswa, db);
                        // hapusDataServer();
                        refreshFlag = "1";
                        finish();
                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
    }
    @Override
    public void finish() {
        System.gc();
        Intent data = new Intent();
        data.putExtra("refreshflag", refreshFlag);
        //  data.putExtra("mahasiswa", mahasiswa);
        setResult(RESULT_OK, data);
        super.finish();
    }
    @Override
    public void onDestroy(){
        db.close();
        databaseHelper.close();
        super.onDestroy();
    }
}
