package com.sibnet.kent.sibnetnews;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.nostra13.universalimageloader.core.display.BitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.utils.StorageUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;


public class ViewNewActivity extends Activity {
    boolean running = false;
    TextView tvTitle;
    ImageView imageView;
    String HtmlArticleBody;
    String ImgArticle;
    ProgressDialog plsWait;
    MyTask mt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewnew);
        tvTitle = (TextView) findViewById(R.id.textViewTitle1);
        WebView webView = (WebView) findViewById(R.id.webView);
        imageView=(ImageView) findViewById(R.id.imageView);
        Intent intent = getIntent();
        String Title = intent.getStringExtra("title");
        tvTitle.setText(Title);
        tvTitle.setTypeface(Typeface.createFromAsset(
                getAssets(), "fonts/BazhanovC-Bold.otf"));
        showProgress("");
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setUseWideViewPort(false);
        ImageLoader imgLoad = ImageLoader.getInstance();

        File cacheDir = StorageUtils.getOwnCacheDirectory(this, "cache_folder");
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .showImageOnLoading(R.drawable.stublogosibnet)
                .resetViewBeforeLoading(true)
                .displayer(new SimpleBitmapDisplayer())
                .build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .diskCache(new UnlimitedDiskCache(cacheDir))
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator())
                .defaultDisplayImageOptions(options)
                .build();

        imgLoad.init(config);


        LoadArticle(intent.getStringExtra("url"));
        try {
            mt.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        hideProgress();

        imgLoad.displayImage(ImgArticle, imageView,options);
        webView.loadDataWithBaseURL(null, HtmlArticleBody, "text/html", "ru_RU", null);



    }




    private void showProgress(String text) {

        if (plsWait == null) {
            try {
                plsWait = ProgressDialog.show(this, "", text);
                plsWait.setCancelable(false);
            } catch (Exception e) {

            }

        }

    }


    public void hideProgress() {

        if (plsWait != null) {
            plsWait.dismiss();
            plsWait = null;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_new, menu);
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


    public void LoadArticle(String GetUrl) {
        running = true;
        mt = new MyTask();
        mt.execute(GetUrl);
    }


    class MyTask extends AsyncTask<String, String, Void> {

    Elements ArticleBody,ImgBody;

    @Override
    protected Void doInBackground(String... GetUrl) {

        Document doc = null;
        try {

            doc = Jsoup.connect(GetUrl[0]).get();
        } catch (IOException e) {

            e.printStackTrace();
        }
        assert doc!=null;
        ImgBody=doc.select("div.b-article-img-block-middle__td img");
        ImgArticle="http://info.sibnet.ru/"+ImgBody.get(0).attr("src");
        ArticleBody=doc.select("div.article-content-block");
        HtmlArticleBody=
                        "<html><head> "+
                        "<style type=\"text/css\">"+
                        "iframe {\n" +
                        "display: block;\n" +
                        "max-width:100%;\n" +
                        "margin-top:10px;\n" +
                        "margin-bottom:10px;\n" +
                        "}"+
                        "@font-face {\n" +
                        "font-family: BazhanovC;\n" +
                        "src: url(\"file:///android_asset/fonts/BazhanovC.otf\")\n" +
                        "}"+
                        "body {\n" +
                        "font-family: BazhanovC;\n" +
                        "font-size: medium;\n" +
                        "text-align: justify;\n" +
                        "}"+
                        "</style>"+
                        "<body>"
                        + ArticleBody.outerHtml()+
                        " </body></head></html>";



        return null;
    }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            running=false;

        }
}

}
