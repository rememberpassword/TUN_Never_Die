package com.example.mycompute.lichhoc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapterList extends BaseAdapter {
    Context context;
    ArrayList<itemSQL> data;

    public CustomAdapterList(Context context, ArrayList<itemSQL> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null)
            convertView=LayoutInflater.from(context).inflate(R.layout.item_list,parent,false);
        //
        TextView monHoc=convertView.findViewById(R.id.monHoc);
        TextView tietHoc=convertView.findViewById(R.id.tietHoc);
        TextView diaDiem=convertView.findViewById(R.id.diaDiem);
        TextView giangVien=convertView.findViewById(R.id.giangVien);
        TextView sbd=convertView.findViewById(R.id.sbd);
        ///
        itemSQL item=data.get(position);
        monHoc.setText(item.monHoc);
        tietHoc.setText(item.tiet);
        diaDiem.setText(item.diaDiem);
        if(item.sbd==null){
            sbd.setText("");
            giangVien.setText(item.giangVien);
        }else
        {
            sbd.setText("SBD:"+item.sbd);
            giangVien.setText(item.hinhThuc);
        }

        return convertView;
    }
}
