package com.example.twenfour;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class MyAdapter extends ArrayAdapter<Fruit> {
    int resourceid;
    public MyAdapter(Context context, int textViewResourceid, List<Fruit> objects){
        super(context,textViewResourceid,objects);
        resourceid=textViewResourceid;

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Fruit fruit=getItem(position);
        View view;
        if(convertView==null) {
            view = LayoutInflater.from((getContext())).inflate(resourceid, parent, false);
        }
        else{
            view=convertView;
        }
        ImageView fruitImage=(ImageView) view.findViewById(R.id.fruitimage);
        TextView fruitName=(TextView) view.findViewById(R.id.textname);
        fruitImage.setImageResource(fruit.getImageid());
        fruitName.setText(fruit.getName());
        return view;
    }
}

