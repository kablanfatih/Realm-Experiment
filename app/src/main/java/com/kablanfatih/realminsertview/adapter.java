package com.kablanfatih.realminsertview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class adapter extends BaseAdapter {

    List<PersonInfo> list;
    Context context;

    public adapter(List<PersonInfo> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = LayoutInflater.from(context).inflate(R.layout.layout,parent,false);
        TextView name = convertView.findViewById(R.id.named);
        TextView  pass = convertView.findViewById(R.id.password);
        TextView userName = convertView.findViewById(R.id.userNameText);
        TextView gender = convertView.findViewById(R.id.gender);

        name.setText(list.get(position).getName());
        pass.setText(list.get(position).getPassword());
        userName.setText(list.get(position).getUsername());
        gender.setText(list.get(position).getGender());

        return convertView;
    }
}
