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
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
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
    static volatile ArrayList<NewsSource> all_items = new ArrayList<>();
    static volatile ArrayList<NewsSource> current_items = new ArrayList<>();
    private Menu menu;
    private ArticleAdapter artadap;
    static final ArrayList<String> topics = new ArrayList<>();
    static private volatile ArrayList<Article> article = new ArrayList<>();
    private String [] colors = {"#ad152e", "#d68c45", "#8c8307", "#60785c", "#1f5a61", "#6c596e", "#ff8c9f"};
    static HashMap<String, String> topic_color = new HashMap<String, String>();
    String currentTitle;
    static volatile String loadedTitle;
    boolean network;
    int currentPage = 0;
    int newsSelected = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrawerLayout = findViewById(R.id.drawer_layout); // <== Important!
        mDrawerList = findViewById(R.id.drawer_list); // <== Important!
        network = hasInternet();

        ArrayAdapter<NewsSource> adapter=new ArrayAdapter<NewsSource>(this, R.layout.drawer_layout_item, current_items);
        mDrawerList.setAdapter(adapter);

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
        if (hasInternet()){
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
        createHashMap();
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
        if(hasInternet()){
            newsSelected = position;
            viewPager.setBackground(null);
            //grab the title
            NewsDownloaderArticle news2 = new NewsDownloaderArticle(this, current_items.get(position).getId());
            new Thread(news2).start();

            currentTitle = current_items.get(position).getName();
            changeTitle(currentTitle);
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
        readaptDrawer(all);
    }

    //changes the title of the activity once the information is loaded in
    //this method is specifically for when the number of topics have been created
     public void changeTitle(int num){
            currentTitle = new StringBuilder()
                    .append(getString(R.string.app_name))
                    .append(" (").append(num)
                    .append(")").toString();
        setTitle(currentTitle);
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
            //this just makes sure the app is not trying to load a saved page
            if(loadedTitle == null){
                viewPager.setCurrentItem(0);
            }else{
                //this is if the user needs to load a saved page
                viewPager.setCurrentItem(currentPage);
                //loadedTitle = null;
            }
    }

    public void createHashMap(){
        for (int i = 1; i < menu.size(); i++){
            topic_color.put(menu.getItem(i).getTitle().toString(), colors[i-1]);
        }
    }

    public void readaptDrawer(boolean all){
        while(all_items.size() > 128){
            all_items.remove( all_items.size()-1 );
        }
        if(all){
            current_items.clear();
            current_items.addAll(all_items);
        }
        ArrayAdapter<NewsSource> adapter = null;
            adapter = new ArrayAdapter<NewsSource>(this, R.layout.drawer_layout_item, current_items){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                textView=(TextView) view.findViewById(android.R.id.text1);
                    String category = current_items.get(position).getCategory();
                    textView.setTextColor( Color.parseColor( topic_color.get(category) ) );
                return view;
            }
        };
        mDrawerList.setAdapter(adapter);
    }
    //need to make sure the activity doesnt lose the information when it's destroyed by rotating
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if(article.size() > 0) {
            outState.putInt("currentPage", viewPager.getCurrentItem());
            outState.putString("newsSource", article.get(viewPager.getCurrentItem()).getNewsId());
            outState.putBoolean("load", true);
        }else{
            outState.putBoolean("load", false);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        // Call super first
        if( savedInstanceState.getBoolean("load") ){
            currentPage = savedInstanceState.getInt("currentPage");
            String news = savedInstanceState.getString("newsSource");
            int i;
            for (i = 0; i < current_items.size(); i++) {
                if (current_items.get(i).getId().equals(news)) {
                    newsSelected = i;
                    Log.d(TAG, "onRestoreInstanceState: ");
                    break;
                }
            }
            loadedTitle = current_items.get(newsSelected).getName();
            currentTitle = loadedTitle;
            selectItem(newsSelected);
        }

        super.onRestoreInstanceState(savedInstanceState);
    }
}