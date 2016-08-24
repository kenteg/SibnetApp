package com.sibnet.kent.sibnetnews;

import android.widget.BaseAdapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;


/**
 * Created by Acer on 19.07.2015.
 */
public class InfoAdapter extends BaseAdapter {
    Context ctx;
    LayoutInflater lInflater;
    News object;
    ImageLoader imageLoader;
    InfoAdapter(Context context, News sibnetnews) {
        ctx = context;
        object = sibnetnews;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        File cacheDir = StorageUtils.getOwnCacheDirectory(context, "cache_folder");
        imageLoader = ImageLoader.getInstance();

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .showImageOnLoading(R.drawable.stublogosibnet)
                .resetViewBeforeLoading(true)
                .displayer(new RoundedBitmapDisplayer(15))
                .build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .diskCache(new UnlimitedDiskCache(cacheDir))
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator())
                .defaultDisplayImageOptions(options)
                .build();

        imageLoader.init(config);

    }


    static class ViewHolder {           //паттерн ViewHolder для ускорения
        public ImageView imageView1;
        public TextView textViewTitle;
        public TextView textViewDate;
    }

    // кол-во элементов
    @Override
    public int getCount() {
        return object.title.size();
    }

    // элемент по позиции
    @Override
    public Object getItem(int position) {
        return object;
    }

    // id по позиции
    @Override
    public long getItemId(int position) {
        return position;
    }

    // пункт списка
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // используем созданные, но не используемые view
        ViewHolder holder;
        if (convertView == null) {
            convertView = lInflater.inflate(R.layout.item, parent, false);
            holder=new ViewHolder();
            holder.textViewTitle = ((TextView) convertView.findViewById(R.id.tvTitle));
            holder.textViewDate = ((TextView) convertView.findViewById(R.id.tvDate));
            holder.imageView1 = (ImageView) convertView.findViewById(R.id.ivImage);
            convertView.setTag(holder);
        }
        else holder = (ViewHolder) convertView.getTag();


        // заполняем View в пункте списка данными : заголовок, дата
        // и картинка


        holder.textViewTitle.setText(object.title.get(position));
        holder.textViewDate.setText(object.date.get(position));
        imageLoader.displayImage(object.image.get(position), holder.imageView1);




        return convertView;
    }



    }

