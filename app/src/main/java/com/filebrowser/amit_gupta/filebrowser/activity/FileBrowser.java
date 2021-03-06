package com.filebrowser.amit_gupta.filebrowser.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.NavigationView;
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

import com.filebrowser.amit_gupta.filebrowser.R;
import com.filebrowser.amit_gupta.filebrowser.activity.fileUtil.MediaFile;
import com.filebrowser.amit_gupta.filebrowser.activity.fileUtil.MimeUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileBrowser extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ListView.OnItemClickListener {

    private final String TAG = FileBrowser.class.getSimpleName();
    private final int PICK_FILE_RESULT_CODE = 999;

    private List<String> myList = new ArrayList<>();
    private File file;
    private int numFiles;
    private int totalSize;
    ListView listView;
    TextView textView;
    private long timeStamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_browser);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        listView = (ListView) findViewById(R.id.listview);
        listView.setOnItemClickListener(this);
        textView = (TextView) findViewById(R.id.text1);

        String root_sd = Environment.getExternalStorageDirectory().toString();
        //file = new File(root_sd + "/external_sd");
        textView.setText(root_sd);
        file = new File(root_sd);
        File list[] = file.listFiles();

        if (list != null && list.length > 0) {
            for (File aList : list) {
                myList.add(aList.getName());
            }
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
            String parent = file.getParent() != null ? file.getParent() : null;
            if (parent != null) {
                file = new File(parent);
                File list[] = file.listFiles();
                if (list != null && list.length > 0) {
                    myList.clear();

                    for (File aList : list) {
                        myList.add(aList.getName());
                    }
                    //Toast.makeText(getApplicationContext(), parent, Toast.LENGTH_SHORT).show();
                    textView.setText(parent);
                    listView.setAdapter(new ArrayAdapter(this,
                            android.R.layout.simple_list_item_1, myList));
                }
            } else {
                Toast.makeText(getApplicationContext(), "AT THE ROOT Folder, press one more back to Exit App", Toast.LENGTH_SHORT).show();
                if (System.currentTimeMillis() - timeStamp < 200) {
                    super.onBackPressed();
                }
                timeStamp = System.currentTimeMillis();
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
        int id = item.getItemId();
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
            //file = temp_file;
            File list[] = temp_file.listFiles();

            if (list != null && list.length > 0) {
                file = temp_file;
                myList.clear();
                for (File f : list) {
                    myList.add(f.getName());
                }
                //Toast.makeText(getApplicationContext(), file.toString(), Toast.LENGTH_SHORT).show();
                textView.setText(file.toString());
                listView.setAdapter(new ArrayAdapter(this,
                        android.R.layout.simple_list_item_1, myList));
            } else {
                Toast.makeText(getApplicationContext(), "Folder is empty.", Toast.LENGTH_SHORT).show();
            }
        } else {
            String mimeType = MediaFile.getMimeTypeForFile(temp_file.toString()); //getContentResolver().getType(Uri.parse("file://" + temp_file));

            String fileName = temp_file.getName();
            int dotposition = fileName.lastIndexOf(".");
            String file_Extension = "";
            if (dotposition != -1) {
                String filename_Without_Ext = fileName.substring(0, dotposition);
                file_Extension = fileName.substring(dotposition + 1, fileName.length());
            }
            if (mimeType == null) {
                mimeType = MimeUtils.guessMimeTypeFromExtension(file_Extension);
            }

            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse("file://" + temp_file), mimeType);
            ResolveInfo info = getPackageManager().resolveActivity(intent,
                    PackageManager.MATCH_DEFAULT_ONLY);
            if (info != null) {
                startActivity(Intent.createChooser(intent, "Complete action using"));
                // startActivity(intent);
            }
        }
    }
//    void pickFile(File aFile) {
//        Intent theIntent = new Intent(Intent.ACTION_PICK);
//        theIntent.setData(Uri.fromFile(aFile));  //default file / jump directly to this file/folder
//        theIntent.putExtra(Intent.EXTRA_TITLE,"A Custom Title"); //optional
//        theIntent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS); //optional
//        try {
//            startActivityForResult(theIntent,PICK_FILE_RESULT_CODE);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PICK_FILE_RESULT_CODE: {
                if (resultCode == RESULT_OK && data != null && data.getData() != null) {
                    String theFilePath = data.getData().getPath();
                    //handle code
                }
                break;
            }
        }
    }
}
