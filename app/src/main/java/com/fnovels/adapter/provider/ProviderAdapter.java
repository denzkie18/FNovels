package com.fnovels.adapter.provider;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.fnovels.R;
import com.google.android.material.color.MaterialColors;
import java.util.List;
import androidx.appcompat.R.attr;

public class ProviderAdapter extends BaseAdapter {

    public interface OnRemoveClickListener {
        void onRemove(ProviderMenuItem item);
    }

    private final LayoutInflater inflater;
    private final List<ProviderMenuItem> items;
    private final OnRemoveClickListener removeListener;

    public ProviderAdapter(Context ctx, List<ProviderMenuItem> items,
            OnRemoveClickListener removeListener) {
        this.inflater = LayoutInflater.from(ctx);
        this.items = items;
        this.removeListener = removeListener;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public ProviderMenuItem getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return items.get(position).type != ProviderMenuItem.EMPTY;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView != null ? convertView
                : inflater.inflate(R.layout.api_server_list_provider, parent, false);

        ProviderMenuItem item = items.get(position);
        TextView name = v.findViewById(R.id.itemName);
        ImageView check = v.findViewById(R.id.activeIndicator);
        ImageView remove = v.findViewById(R.id.removeBtn);

        name.setText(item.title);
        check.setVisibility(item.selected ? View.VISIBLE : View.INVISIBLE);
        name.setAlpha(item.type == ProviderMenuItem.EMPTY ? 0.6f : 1f);

        int colorAttr = item.type == ProviderMenuItem.ADD ? R.attr.colorPrimary
                : com.google.android.material.R.attr.colorOnSurface;
        name.setTextColor(MaterialColors.getColor(v, colorAttr));

        boolean removable = item.type == ProviderMenuItem.PROVIDER;
        remove.setVisibility(removable ? View.VISIBLE : View.GONE);
        remove.setOnClickListener(removable ? x -> removeListener.onRemove(item) : null);

        return v;
    }
}