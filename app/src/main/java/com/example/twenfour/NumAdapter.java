package com.example.twenfour;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import static com.baidu.tts.f.c.p;

public class NumAdapter extends BaseAdapter {
    String TAG="hh";

    private List<ProvinceBean> provinceBeanList;
    private LayoutInflater layoutInflater;

    public NumAdapter(Context context, List<ProvinceBean> provinceBeanList) {
        this.provinceBeanList = provinceBeanList;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return provinceBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return provinceBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {

            convertView = layoutInflater.inflate(R.layout.num_item, null);
            holder = new ViewHolder();
            holder.text = (TextView) convertView.findViewById(R.id.text);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ProvinceBean provinceBean = provinceBeanList.get(position);
        if (provinceBean != null) {
            String x=provinceBean.getName();
            Log.e(TAG, "getView: "+x );
            holder.text.setText(provinceBean.getName());
            holder.text.setBackgroundResource(provinceBean.getColor());
        }
        return convertView;
    }




    class ViewHolder {
        TextView text;
    }


}

