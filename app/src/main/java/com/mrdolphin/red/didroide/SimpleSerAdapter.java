package com.mrdolphin.red.didroide;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


public class SimpleSerAdapter extends
        RecyclerView.Adapter<SimpleSerAdapter.MyViewHolder2> {

    private List<String> list_item ;
    private List<String> series_list_item ;
    private List<String> list_mod ;
    public Context mcontext;






    public SimpleSerAdapter(List<String> series_list, List<String> list, List<String> mod, Context context) {

        list_item = list;
        series_list_item = series_list;
        list_mod = mod;
        mcontext = context;




        //SomeFunctions.openNetImage(myUrl, mcontext, );
    }



    // Called when RecyclerView needs a new RecyclerView.ViewHolder of the given type to represent an item.
    @Override
    public MyViewHolder2 onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
        // create a layout
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.list_i_series, null);

        MyViewHolder2 myViewHolder = new MyViewHolder2(view);
        return myViewHolder;
    }

    // Called by RecyclerView to display the data at the specified position.
    @Override
    public void onBindViewHolder(final SimpleSerAdapter.MyViewHolder2 viewHolder2, final int position ) {

        viewHolder2.series_card.setText(series_list_item.get(position));
        Log.d("SERIES_ID", series_list_item.get(position).toString());
        //viewHolder2.series_card.setText("1");
        viewHolder2.seriesname.setText(list_item.get(position));
        viewHolder2.seriesmod.setText(list_mod.get(position));

        viewHolder2.series_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(mcontext, series_list_item.get(position),
                //        Toast.LENGTH_LONG).show();
            }
        });

    }

    // initializes textview in this class
    public static class MyViewHolder2 extends RecyclerView.ViewHolder {

        public TextView series_card;
        public TextView seriesname;
        public TextView seriesmod;


        public MyViewHolder2(View itemLayoutView) {
            super(itemLayoutView);

            series_card = (TextView) itemLayoutView.findViewById(R.id.card_series);
            seriesname = (TextView) itemLayoutView.findViewById(R.id.card_seriesname);
            seriesmod = (TextView) itemLayoutView.findViewById(R.id.card_sermod);

        }
    }



    //Returns the total number of items in the data set hold by the adapter.
    @Override
    public int getItemCount() {
        return series_list_item.size();
    }



}
