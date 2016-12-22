package net.agusharyanto.cruddatamahasiswa;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import net.agusharyanto.cruddatamahasiswa.model.Mahasiswa;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MahasiswaActivityServer extends AppCompatActivity {

    private EditText editTextNIM, editTextNama, editTextJurusan;
    private Button buttonSave, buttonHapus;
    private Mahasiswa mahasiswa;

    private String action_flag="add";
    private String refreshFlag="0";
    private static final String TAG="MahsiswaActivityServer";
    private ProgressDialog pDialog;

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
                saveDataVolley();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
        pDialog = new ProgressDialog(MahasiswaActivityServer.this);

        editTextNIM = (EditText) findViewById(R.id.editTextNim);
        editTextNama = (EditText) findViewById(R.id.editTextNama);
        editTextJurusan = (EditText) findViewById(R.id.editTextJurusan);
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
            saveDataVolley();
            return true;
        }else  if (id == R.id.action_delete) {
            hapusData();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void hapusData() {
        new AlertDialog.Builder(this)
                .setTitle("Data Mahasiswa")
                .setMessage("Hapus Data " + mahasiswa.getNama() + " ?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //databaseHelper.deleteMahasiswa(mahasiswa, db);
                        hapusDataServer();
                        refreshFlag = "1";
                        finish();
                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
    }




    private void saveDataVolley(){
        refreshFlag="1";
        final String nama = editTextNama.getText().toString();
        final String nim = editTextNIM.getText().toString();
        final String jurusan = editTextJurusan.getText().toString();


        String url = GlobalVar.IP_SERVER+"/mahasiswa/savedata.php";
        pDialog.setMessage("Save Data Mahasiswa...");
        showDialog();

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideDialog();
                        // Log.d("AddEditActivity", "response :" + response);
                        // Toast.makeText(getBaseContext(),"response: "+response, Toast.LENGTH_SHORT).show();
                        processResponse(response);
                        finish();


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hideDialog();
                        error.printStackTrace();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<>();
                // the POST parameters:

                params.put("nama",nama);
                params.put("nim",nim);
                params.put("jurusan",jurusan);
                if (action_flag.equals("add")){
                    params.put("id","0");
                }else{
                    params.put("id",mahasiswa.getId());
                }
                return params;
            }
        };
        Volley.newRequestQueue(this).add(postRequest);

    }
    private void processResponse(String response){

        try {
            JSONObject jsonObj = new JSONObject(response);
            String errormsg = jsonObj.getString("errormsg");
            Toast.makeText(getBaseContext(),"Save Data "+errormsg,Toast.LENGTH_SHORT).show();

        } catch (JSONException e) {
            Log.d("ViewActivity", "errorJSON");
        }

    }


    private void hapusDataServer(){
        refreshFlag="1";
        String url = GlobalVar.IP_SERVER+"/mahasiswa/deletedata.php";
        pDialog.setMessage("Hapus Data Mahaiswa...");
        showDialog();

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideDialog();
                        Log.d("ViewActivity", "response :" + response);
                        Toast.makeText(getBaseContext(),"Hapus Data "+response, Toast.LENGTH_SHORT).show();
                        //processResponse(response);
                        finish();


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hideDialog();
                        error.printStackTrace();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<>();
                // the POST parameters:
                params.put("id",mahasiswa.getId());

                return params;
            }
        };
        Volley.newRequestQueue(this).add(postRequest);

    }
    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
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

}
