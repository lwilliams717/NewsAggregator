package com.example.newsaggregator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private TextView textView;
    private ActionBarDrawerToggle mDrawerToggle;
    private String[] items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Make sample items for the drawer list
        items = new String[15];
        for (int i = 0; i < items.length; i++)
            items[i] = "Drawer Item #" + (i + 1);
        //

        mDrawerLayout = findViewById(R.id.drawer_layout); // <== Important!
        mDrawerList = findViewById(R.id.drawer_list); // <== Important!

        mDrawerList.setAdapter(new ArrayAdapter<>(this,   // <== Important!
                R.layout.drawer_layout_item, items));

        mDrawerToggle = new ActionBarDrawerToggle(   // <== Important!
                this,                /* host Activity */
                mDrawerLayout,             /* DrawerLayout object */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        );

        if (getSupportActionBar() != null) {  // <== Important!
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState(); // <== IMPORTANT
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig); // <== IMPORTANT
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        // Important!
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            Log.d(TAG, "onOptionsItemSelected: mDrawerToggle " + item);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void selectItem(int position) {
        textView.setText(String.format(Locale.getDefault(),
                "You picked %s", items[position]));
        mDrawerLayout.closeDrawer(mDrawerList);
    }
}