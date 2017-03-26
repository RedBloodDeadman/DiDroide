package com.mrdolphin.red.didroide;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


public class SimpleAdapter extends
        RecyclerView.Adapter<SimpleAdapter.MyViewHolder> {

    private List<String> list_item ;
    private List<String> id_list_item ;
    public Context mcontext;


    public SimpleAdapter(List<String> id_list, List<String> list, Context context) {

        list_item = list;
        id_list_item = id_list;
        mcontext = context;
    }

    // Called when RecyclerView needs a new RecyclerView.ViewHolder of the given type to represent an item.
    @Override
    public SimpleAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        // create a layout
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.list_item, null);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    // Called by RecyclerView to display the data at the specified position.
    @Override
    public void onBindViewHolder(final MyViewHolder viewHolder, final int position ) {


        viewHolder.id.setText(id_list_item.get(position));
        viewHolder.name.setText(list_item.get(position));

        viewHolder.id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(mcontext, id_list_item.get(position),
                //        Toast.LENGTH_LONG).show();
            }
        });

    }

    // initializes textview in this class
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView id;
        public TextView name;


        public MyViewHolder(View itemLayoutView) {
            super(itemLayoutView);

            id = (TextView) itemLayoutView.findViewById(R.id.id);
            name = (TextView) itemLayoutView.findViewById(R.id.name);

        }
    }



    //Returns the total number of items in the data set hold by the adapter.
    @Override
    public int getItemCount() {
        return id_list_item.size();
    }



}
