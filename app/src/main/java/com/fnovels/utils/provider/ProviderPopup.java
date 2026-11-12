package com.fnovels.utils.provider;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import androidx.appcompat.widget.ListPopupWindow;
import androidx.core.content.ContextCompat;
import com.fnovels.R;
import com.fnovels.adapter.provider.ProviderAdapter;
import com.fnovels.adapter.provider.ProviderMenuItem;
import com.fnovels.model.api.Provider;
import java.util.List;

public class ProviderPopup {

    public interface Listener {
        void onProviderSelected(String name);

        void onAddClicked();

        void onRemoveRequested(String name);
    }

    private ListPopupWindow popup;

    public void show(View anchor, List<Provider> providers, String selectedName,
            Listener listener) {
        Context ctx = anchor.getContext();
        List<ProviderMenuItem> items = ProviderMenuItem.build(providers, selectedName);

        popup = new ListPopupWindow(ctx);
        popup.setAnchorView(anchor);
        popup.setModal(true);
        popup.setDropDownGravity(Gravity.END);
        popup.setWidth((int) (240 * ctx.getResources().getDisplayMetrics().density));
        popup.setBackgroundDrawable(ContextCompat.getDrawable(ctx, R.drawable.bg_popup_menu));
        popup.setAdapter(new ProviderAdapter(ctx, items, item -> {
            popup.dismiss();
            listener.onRemoveRequested(item.title);
        }));
        popup.setOnItemClickListener((parent, v, position, id) -> {
            ProviderMenuItem item = items.get(position);
            popup.dismiss();
            if (item.type == ProviderMenuItem.PROVIDER) {
                listener.onProviderSelected(item.title);
            } else if (item.type == ProviderMenuItem.ADD) {
                listener.onAddClicked();
            }
        });
        popup.show();
    }

    public void dismiss() {
        if (popup != null && popup.isShowing())
            popup.dismiss();
        popup = null;
    }
}