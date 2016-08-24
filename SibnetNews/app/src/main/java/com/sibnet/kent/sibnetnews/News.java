package com.sibnet.kent.sibnetnews;

import java.util.ArrayList;

/**
 * Created by Acer on 02.07.2015.
 */
public class News {
     ArrayList<String> title;
     ArrayList<String> date;
     ArrayList<String> url;
     ArrayList<String>  image;
     ArrayList<Integer> id;
    int n=0;
    News() {
        title = new ArrayList<String>();
        date = new ArrayList<String>();
        url= new ArrayList<String>();
        image = new ArrayList<String>();



    }

    public ArrayList<String> getTitle(){
        return title;
    };
    public ArrayList<String> getDate(){
        return date;
    };
    public ArrayList<String> getUrl(){
        return url;
    };
    public ArrayList<String> getUrlImage(){
        return image;
    }

    public void Add (String input_title, String input_Date, String input_Url, String input_URLImage){
        title.add(input_title);
        date.add(input_Date);
        url.add(input_Url);
        image.add(input_URLImage);
        n++;

    };
}
//https://github.com/nostra13/Android-Universal-Image-Loader