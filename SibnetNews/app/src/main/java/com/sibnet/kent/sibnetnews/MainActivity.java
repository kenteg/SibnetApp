package com.sibnet.kent.sibnetnews;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.content.ContentValues;
import android.content.Context;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {
    private static final String TAG = "myLogs";
    ListView lvMain;
    News SibnetNews;
    InfoAdapter adapter;
    String Sibnet="http://info.sibnet.ru/";
    int page = 2;
    boolean userScrolled = false;
    boolean running = false;
    private String[] mBot_Menu;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBot_Menu = getResources().getStringArray(R.array.bot_menu);

        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // Set the adapter for the list view
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, mBot_Menu));
        // Set the list's click listener
       // mDrawerList.setOnItemClickListener(new DrawerItemClickListener());







        lvMain= (ListView) findViewById(R.id.lvMain);
        SibnetNews = new News();


        adapter = new InfoAdapter(this,SibnetNews);
        lvMain.setAdapter(adapter);
        LoadNews("http://info.sibnet.ru/articles/");


        lvMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //Log.d(TAG, "itemClick: position = " + position + ", id = "
               //         + id);
                Intent intent = new Intent(MainActivity.this, ViewNewActivity.class);
                intent.putExtra("title", SibnetNews.getTitle().get(position));
                intent.putExtra("url", SibnetNews.getUrl().get(position));
                startActivity(intent);
            }
        });

        lvMain.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();

                if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_UP) {
                    userScrolled = true;
                }

                return false;
            }
        });

        lvMain.setOnScrollListener(new AbsListView.OnScrollListener() {
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // Log.d(LOG_TAG, "scrollState = " + scrollState);
            }

            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                if (!userScrolled) return;
                if ( ((firstVisibleItem + visibleItemCount >= totalItemCount-3)&& visibleItemCount>0 && !running)) {
                    LoadNews(Sibnet + "articles/?page=" + page++);
                }

            }
        });


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


    public void LoadNews(String GetUrl) {
        running = true;
        MyTask mt = new MyTask();
        mt.execute(GetUrl);
    }
   class MyTask extends AsyncTask<String, Void, Void> {

        Elements NewTitle,ImageNew,NewUrl,NewDate;

        @Override
        protected Void doInBackground(String... GetUrl) {

            Document doc = null;
            try {

                doc = Jsoup.connect(GetUrl[0]).get();
            } catch (IOException e) {

                e.printStackTrace();
            }


            NewTitle = doc.select("span.article-item-block-right-td-header-text");
            ImageNew = doc.select("div.article-item-block-left-td-img img");
            NewUrl   = doc.select("a.article-item-block");
            NewDate  = doc.getElementsByClass("date-comments-views-date");
            for(int j=0;j<NewTitle.size();j++) {
                SibnetNews.Add(NewTitle.get(j).text(), NewDate.get(j).text() , Sibnet+NewUrl.get(j).attr("href"), Sibnet+ImageNew.get(j).attr("src"));
            };
/*
            for (Element el : ImageNew) {
             Log.w(TAG,"link img " + el.attr("src"));
            };

            for (Element el : NewUrl ) {
                Log.w(TAG,"url " + el.attr("href"));
            };

*/





            //    TitleList = new String[content.size()];
         //   for (int i = 0; i < content.size(); i++) {
         //           TitleList[i] = "some string " + i;
         //        };



            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            running=false;
            adapter.notifyDataSetChanged();
            lvMain.invalidateViews();


        }
    }

}
