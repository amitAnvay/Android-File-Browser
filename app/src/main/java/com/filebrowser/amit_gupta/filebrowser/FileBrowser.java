package com.filebrowser.amit_gupta.filebrowser;

import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileBrowser extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ListView.OnItemClickListener{

    private File file;
    private String TAG = FileBrowser.class.getSimpleName();
    private List<String> myList = new ArrayList<String>();
    private int numFiles;
    private int totalSize;
    ListView listView;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_browser);
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        listView = (ListView)findViewById(R.id.listview);
        listView.setOnItemClickListener(this);
        textView = (TextView)findViewById(R.id.text1);

        String root_sd = Environment.getExternalStorageDirectory().toString();
        //file = new File(root_sd + "/external_sd");
        textView.setText(root_sd);
        file = new File(root_sd);
        File list[] = file.listFiles();

        for (int i = 0; i < list.length; i++) {
            myList.add(list[i].getName());
        }

        listView.setAdapter(new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, myList));

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            String parent = file.getParent() != null ? file.getParent().toString() : null;
            if(parent != null) {
                file = new File(parent);
                File list[] = file.listFiles();

                myList.clear();

                for (int i = 0; i < list.length; i++) {
                    myList.add(list[i].getName());
                }
                //Toast.makeText(getApplicationContext(), parent, Toast.LENGTH_SHORT).show();
                textView.setText(parent);
                listView.setAdapter(new ArrayAdapter(this,
                        android.R.layout.simple_list_item_1, myList));
            }else {
                Toast.makeText(getApplicationContext(),"AT THE ROOT - no more back", Toast.LENGTH_SHORT).show();
            }
           // super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.file_browser, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int j, long l) {

        File temp_file = new File(file, myList.get(j));

        if (!temp_file.isFile()) {
            file = new File(file, myList.get(j));
            File list[] = file.listFiles();

            if(list != null) {
                for (int i = 0; i < list.length; i++) {
                    myList.add(list[i].getName());
                }
                //Toast.makeText(getApplicationContext(), file.toString(), Toast.LENGTH_SHORT).show();
                textView.setText(file.toString());
                listView.setAdapter(new ArrayAdapter(this,
                        android.R.layout.simple_list_item_1, myList));
            }else{
                Toast.makeText(getApplicationContext(),"No permissions to access this folder", Toast.LENGTH_SHORT).show();
            }

        }
    }
}
