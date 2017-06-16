package com.example.shukri.fixed;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class CustomListAdapter extends ArrayAdapter<Players> {
    ArrayList<Players> filterList;
    ArrayList<Players> players;
    CustomFilter filter;
    Context context;
    int resource;

    public CustomListAdapter( Context context, int resource, ArrayList<Players> players) {
        super(context, resource, players);
        this.players = players;
        this.context = context;
        this.resource = resource;
        this.filterList = players;

    }

    @Override
    public int getCount() {
        return players.size();
    }

    @Nullable
    @Override
    public Players getItem(int position) {
        return players.get(position);
    }

    @Override
    public long getItemId(int position) {
        return players.indexOf(getItem(position));

    }
    public Filter getFilter(){
        if (filter == null){
            filter = new CustomFilter();
        }
        return filter;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater)getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.listview_item,null,true);

        }
        Players players = getItem(position);
        ImageView imageView = (ImageView)convertView.findViewById(R.id.imageView2);
        Picasso.with(context).load(players.getImage()).resize(60,60).into(imageView);

        TextView textView = (TextView)convertView.findViewById(R.id.textView2);
        textView.setText(players.getName());


        return convertView;
    }
    class CustomFilter extends Filter
    {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            // TODO Auto-generated method stub
            FilterResults results=new FilterResults();
            if(constraint != null && constraint.length()>0)
            {
                //CONSTARINT TO UPPER
                constraint=constraint.toString().toUpperCase();
                ArrayList<Players> filters=new ArrayList<Players>();
                //get specific items
                for(int i=0;i<filterList.size();i++)
                {
                    if(filterList.get(i).getName().toUpperCase().startsWith(constraint.toString()))
                    {
                        Players p=new Players(filterList.get(i).getImage(),filterList.get(i).getName() );
                        filters.add(p);
                    }
                }
                results.count=filters.size();
                results.values=filters;
            }else
            {
                results.count=filterList.size();
                results.values=filterList;
            }
            return results;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            // TODO Auto-generated method stub
            players=(ArrayList<Players>) results.values;
            notifyDataSetChanged();
        }
    }
}
