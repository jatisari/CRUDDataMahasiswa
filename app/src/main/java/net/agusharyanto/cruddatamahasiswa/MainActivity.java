package net.agusharyanto.cruddatamahasiswa;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import net.agusharyanto.cruddatamahasiswa.adapter.MahasiswaAdapter;
import net.agusharyanto.cruddatamahasiswa.model.Mahasiswa;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private MahasiswaAdapter rvAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Context context = MainActivity.this;
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase db;
    private static  final int REQUEST_CODE_ADD =1;
    private static  final int REQUEST_CODE_EDIT =2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        // specify an adapter (see also next example)
        initializeData();
        gambarDatakeRecyclerView();

    }

    private void gambarDatakeRecyclerView(){
        rvAdapter = new MahasiswaAdapter(mahasiswaList);
        mRecyclerView.setAdapter(rvAdapter);
        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Mahasiswa mahasiswa = rvAdapter.getItem(position);
                        Toast.makeText(context, "Name :" + mahasiswa.getNama(), Toast.LENGTH_SHORT).show();
                        // selectedPosition = position;
                        //Intent intent = new Intent(MainActivity.this, MahasiswaActivity.class);
                        //intent.putExtra("mahasiswa", mahasiswa);
                        //startActivityForResult(intent, REQUEST_CODE_EDIT);
                    }
                })
        );
    }
    private List<Mahasiswa> mahasiswaList;
    // This method creates an ArrayList that has three Fruit objects
    private void initializeData(){
        databaseHelper = new DatabaseHelper(context);
        db = databaseHelper.getWritableDatabase();
        mahasiswaList = databaseHelper.getDataMahasiswa(db);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
