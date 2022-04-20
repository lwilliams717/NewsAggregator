package com.example.newsaggregator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
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
    private static TextView drawerText;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private TextView textView;
    private ViewPager2 viewPager;
    private ActionBarDrawerToggle mDrawerToggle;
    static final ArrayList<NewsSource> all_items = new ArrayList<>();
    static final ArrayList<NewsSource> current_items = new ArrayList<>();
    private Menu menu;
    private ArticleAdapter artadap;
    static final ArrayList<String> topics = new ArrayList<>();
    private  ArrayList<Article> article = new ArrayList<>();
    private String [] colors = {"#ad152e", "#d68c45", "#f4d35e", "#60785c", "#77a0a9", "#6c596e", "#ff8c9f"};
    String currentTitle;
    boolean network;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrawerLayout = findViewById(R.id.drawer_layout); // <== Important!
        mDrawerList = findViewById(R.id.drawer_list); // <== Important!
        network = hasInternet();


        mDrawerList.setAdapter(new ArrayAdapter<>(this,   // <== Important!
                R.layout.drawer_layout_item, all_items));

        mDrawerList.setOnItemClickListener(
                (parent, view, position, id) -> {
                    selectItem(position);
                    mDrawerLayout.closeDrawer(mDrawerList);
                }
        );

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
        artadap = new ArticleAdapter(this,article);
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(artadap);

    }

    //dynamic menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        if (network){
            NewsDownloaderSource.NewsDownloaderTopics(this);
        }
        else{
            ErrorNetwork();
        }

        return super.onCreateOptionsMenu(menu);
    }

    //when menu option is selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //this is for the drawer only
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        //this hopefully will loop thru the menu items on the list and select the item that matches
        //the item that was clicked
        for (int i = 0; i < menu.size(); i++){
            if(menu.getItem(i).getTitle().equals(item.getTitle())){
                reloadSources(topics.get(i));
                break;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void reloadSources(String s) {
        current_items.clear();
        if(s.equals("All")){
            current_items.addAll(all_items);
        }
        else{
            for(int i = 0; i < all_items.size(); i++){
                if(all_items.get(i).getCategory().equals(s)){
                    current_items.add(all_items.get(i));
                }
            }
        }
        changeTitle(current_items.size());
        loadDrawer(false);
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
            if(i > 0){
                int newind = i-1;
                MenuItem item = menu.getItem(i);
                SpannableString spanString = new SpannableString(item.getTitle().toString());
                spanString.setSpan(new ForegroundColorSpan(Color.parseColor(colors[newind])), 0, spanString.length(), 0); // fix the color to white
                item.setTitle(spanString);
            }
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

    @SuppressLint("NotifyDataSetChanged")
    private void selectItem(int position) {
        if(network){
            viewPager.setBackground(null);
            //grab the title
            currentTitle = current_items.get(position).getName();
            NewsDownloaderArticle news2 = new NewsDownloaderArticle(this, current_items.get(position).getId());
            new Thread(news2).start();
        }
        else{
            ErrorNetwork();
        }

        mDrawerLayout.closeDrawer(mDrawerList);
    }

    //method for when the downloader throws an error
    public void ErrorDownload() {
        Toast.makeText(MainActivity.this, R.string.error, Toast.LENGTH_SHORT).show();
    }
    //error method for internet
    public void ErrorNetwork(){
        Toast.makeText(MainActivity.this, R.string.no_network, Toast.LENGTH_SHORT).show();
    }

    //adds topics to the topics array when called from the downloader
    public static void addTopic(String t){
        topics.add(t);
    }

    //reloads the drawer when the information is pulled from the API
    public void loadDrawer(boolean all){
        //will just load all the items if the user wants to load all the items
        if(all){
            current_items.addAll(all_items);
        }
        mDrawerList.setAdapter(new ArrayAdapter<>(this,   // <== Important!
                R.layout.drawer_layout_item, current_items));
        mDrawerLayout.setScrimColor(Color.WHITE);
    }

    //changes the title of the activity once the information is loaded in
    //this method is specifically for when the number of topics have been created
    public void changeTitle(int num){
        String temp = new StringBuilder()
                .append(getString(R.string.app_name))
                .append(" (").append(num)
                .append(")").toString();
        setTitle(temp);
    }
    //changes the title when the user pulls up a view page
    //this changes the name to the publication in use
    public void changeTitle(String title){
        setTitle(title);
    }

    private boolean hasInternet() {
        ConnectivityManager connectivityManager = getSystemService(ConnectivityManager.class);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnectedOrConnecting());
    }

    public void addArticle(ArrayList<Article> w) {
            article.clear();
            article.addAll(w);
            artadap.notifyDataSetChanged();
            setTitle(currentTitle);
            viewPager.setCurrentItem(0);
    }
}