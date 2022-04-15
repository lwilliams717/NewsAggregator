package com.example.newsaggregator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private TextView textView;
    private ActionBarDrawerToggle mDrawerToggle;
    static final ArrayList<NewsSource> items = new ArrayList<>();
    private Menu menu;
    static final ArrayList<String> topics = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



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

        //makeMenu();

    }

    //dynamic menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        //im putting it here to see if this fixes the error
        //downloads the topics from the news api source
        NewsDownloaderSource.NewsDownloaderTopics(this);

        return super.onCreateOptionsMenu(menu);
    }

    //when menu option is selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //this is for the drawer only
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            Log.d(TAG, "onOptionsItemSelected: mDrawerToggle " + item);
            return true;
        }

        //for dynamic menu with submenus
        if (item.hasSubMenu())
            return true;

        //int parentSubmenu = item.getGroupId();
        //int menuItem = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    //this method makes the menu
    //menu should load the topics
    public void makeMenu() {
        menu.clear();
        // Add the elements to set
        topics.size();

        Set<String> set = new LinkedHashSet<String>();

        set.addAll(topics);
        // Clear the list
        topics.clear();
        // add the elements of set
        // with no duplicates to the list
        topics.addAll(set);
        //adds the topics to the menu
        for(int i = 0; i < topics.size(); i++){
            menu.add( topics.get(i) );
        }
        hideKeyboard();
    }
    //helper method to hide keyboard
    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (imm == null) return;

        //Find the currently focused view
        View view = getCurrentFocus();

        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null)
            view = new View(this);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    ///////////////////////////////////////////////////////////////////
    //this is for drawer
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


    private void selectItem(int position) {
        //textView.setText(String.format(Locale.getDefault(),"You picked %s", items[position]));
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    public void updateData(Object o) {
    }

    //method for when the downloader throws an error
    public void ErrorDownload() {
        Toast.makeText(MainActivity.this, R.string.error, Toast.LENGTH_SHORT).show();
    }

    public static void addTopic(String t){
        topics.add(t);
    }

    public void loadDrawer(){
        mDrawerList.setAdapter(new ArrayAdapter<>(this,   // <== Important!
                R.layout.drawer_layout_item, items));
    }
}