package it.lanos.eventbuddy.UI;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemKeyProvider;
import androidx.recyclerview.widget.RecyclerView;

public class MyItemKeyProvider extends ItemKeyProvider<Long>{
    private final RecyclerView recyclerView;
    protected MyItemKeyProvider(int scope, RecyclerView recyclerView) {
        super(scope);
        this.recyclerView = recyclerView;
    }

    @Nullable
    @Override
    public Long getKey(int position) {
        return (long) position;
    }

    @Override
    public int getPosition(@NonNull Long key) {
        return key.intValue();
    }
}
