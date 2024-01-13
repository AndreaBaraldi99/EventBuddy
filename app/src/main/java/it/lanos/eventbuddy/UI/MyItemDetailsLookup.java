package it.lanos.eventbuddy.UI;

import android.view.MotionEvent;

import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.widget.RecyclerView;

import it.lanos.eventbuddy.adapter.AddGuestsRecyclerViewAdapter;

public class MyItemDetailsLookup extends ItemDetailsLookup<Long> {

    private final RecyclerView recyclerView;

    public MyItemDetailsLookup(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    @Override
    public ItemDetails<Long> getItemDetails(MotionEvent event) {
        android.view.View view = recyclerView.findChildViewUnder(event.getX(), event.getY());

        if (view != null) {
            RecyclerView.ViewHolder viewHolder = recyclerView.getChildViewHolder(view);

            if (viewHolder instanceof AddGuestsRecyclerViewAdapter.GuestViewHolder) {
                return ((AddGuestsRecyclerViewAdapter.GuestViewHolder) viewHolder).getItemDetails();
            }
        }

        return null;
    }
}

