package com.fnovels.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.fnovels.R;
import com.fnovels.model.api.Provider;
import com.fnovels.utils.ApiServerList;
import com.fnovels.utils.provider.AddProviderDialog;
import com.fnovels.utils.provider.ProviderPopup;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class ExploreFragmentActivity extends Fragment {

    private ApiServerList apiServerList;
    private final ProviderPopup providerPopup = new ProviderPopup();

    private AppBarLayout appbar;
    private MaterialToolbar toolbar;

    private LinearLayout errorLayout;

    public ExploreFragmentActivity() {
        super(R.layout.activity_explore_fragment);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        appbar = view.findViewById(R.id.appbar);
        toolbar = view.findViewById(R.id.toolbar);

        errorLayout = view.findViewById(R.id.errorLayout);

        toolbar.setTitle("Explore");

        apiServerList = new ApiServerList(requireContext());
        apiServerList.getSelectedProvider(); // ensures first item is the default

        view.findViewById(R.id.apiServerListBtn).setOnClickListener(anchor -> {
            Provider selected = apiServerList.getSelectedProvider();
            providerPopup.show(anchor, apiServerList.getListOfProviders(),
                    selected != null ? selected.getName() : null, new ProviderPopup.Listener() {
                        @Override
                        public void onProviderSelected(String name) {
                            apiServerList.setSelectedProvider(name);
                            // TODO: reload explore content for the new provider
                        }

                        @Override
                        public void onAddClicked() {
                            AddProviderDialog.show(requireContext(), apiServerList, () -> {
                                // TODO: reload explore content if needed
                            });
                        }

                        @Override
                        public void onRemoveRequested(String name) {
                            confirmRemoveProvider(name);
                        }
                    });
        });

        initializeLogic();
    }

    private void initializeLogic() {
        if (!isProviderNotEmpty()) {
            errorLayout.setVisibility(View.VISIBLE);
        } else {
            errorLayout.setVisibility(View.GONE);
        }
    }

    // Check if provider is empty
    private Boolean isProviderNotEmpty() {
        int providerCount = apiServerList.getProviderCount();

        return providerCount != 0;
    }

    private void confirmRemoveProvider(String name) {
        new MaterialAlertDialogBuilder(requireContext()).setTitle("Remove provider")
                .setMessage("Remove \"" + name + "\" from your providers?")
                .setNegativeButton("Cancel", null).setPositiveButton("Remove", (d, w) -> {
                    apiServerList.removeProviderByName(name);
                    // If the removed one was selected, the first remaining one is now selected
                    // TODO: reload explore content for the (possibly new) provider
                }).show();
    }

    @Override
    public void onDestroyView() {
        providerPopup.dismiss();
        super.onDestroyView();
    }
}