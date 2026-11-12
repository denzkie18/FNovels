package com.fnovels.adapter.provider;

import com.fnovels.model.api.Provider;
import java.util.ArrayList;
import java.util.List;

public class ProviderMenuItem {
    public static final int PROVIDER = 0, EMPTY = 1, ADD = 2;

    public final int type;
    public final String title;
    public final boolean selected;

    public ProviderMenuItem(int type, String title, boolean selected) {
        this.type = type;
        this.title = title;
        this.selected = selected;
    }

    /** Builds the popup rows: providers (or an empty message) + "Add new provider". */
    public static List<ProviderMenuItem> build(List<Provider> providers, String selectedName) {
        List<ProviderMenuItem> items = new ArrayList<>();
        if (providers.isEmpty()) {
            items.add(new ProviderMenuItem(EMPTY, "No providers available", false));
        } else {
            for (Provider p : providers) {
                items.add(new ProviderMenuItem(PROVIDER, p.getName(),
                        p.getName().equals(selectedName)));
            }
        }
        items.add(new ProviderMenuItem(ADD, "Add new provider", false));
        return items;
    }
}