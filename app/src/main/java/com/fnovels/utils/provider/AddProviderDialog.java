package com.fnovels.utils.provider;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.InputType;
import android.text.method.LinkMovementMethod;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.core.text.HtmlCompat;
import androidx.core.widget.TextViewCompat;
import com.fnovels.R;
import com.fnovels.utils.ApiServerList;
import com.google.android.material.color.MaterialColors;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public final class AddProviderDialog {

    private static final String GITHUB_URL = "https://github.com/denzkie18/book-api";

    private static final String INSTRUCTIONS = "Add an API provider. You can use the default provider or host your own.<br><br>"
            + "<h4><strong>How to add your own</strong></h4><br>"
            + "1. Tap <b>Github</b> and follow the hosting guide.<br>"
            + "2. Deploy the provider and copy its base URL.<br>"
            + "3. Enter a name and paste the URL below.<br><br>"
            + "<h4><strong>Importance</strong></h4><br>"
            + "<small>The defualt provider has it's own limitation and you might get unexpected errors, so it is recommended to host your own api provider and don't share to keep the limat access to minimum.</small><br><br>"
            + "<b>Notes</b><br>" + "• Use the full URL, including <i>https://</i>.<br>"
            + "• Names must be unique.<br>" + "• You can switch providers anytime from the menu.";

    private AddProviderDialog() {
    }

    public static void show(Context ctx, ApiServerList apiServerList, Runnable onAdded) {
        float d = ctx.getResources().getDisplayMetrics().density;

        // Instructions
        TextView instructions = new TextView(ctx);
        instructions.setText(HtmlCompat.fromHtml(INSTRUCTIONS, HtmlCompat.FROM_HTML_MODE_COMPACT));
        instructions.setMovementMethod(LinkMovementMethod.getInstance());
        TextViewCompat.setTextAppearance(instructions,
                com.google.android.material.R.style.TextAppearance_Material3_BodyMedium);
        instructions.setTextColor(MaterialColors.getColor(instructions,
                com.google.android.material.R.attr.colorOnSurfaceVariant));
        instructions.setLineSpacing(0, 1.15f);

        // Inputs
        TextInputLayout nameLayout = new TextInputLayout(ctx);
        nameLayout.setHint("Name");
        TextInputEditText nameInput = new TextInputEditText(nameLayout.getContext());
        nameInput.setSingleLine(true);
        nameLayout.addView(nameInput);

        TextInputLayout routeLayout = new TextInputLayout(ctx);
        routeLayout.setHint("Route / URL");
        routeLayout.setHelperText("Example: https://api.example.com");
        TextInputEditText routeInput = new TextInputEditText(routeLayout.getContext());
        routeInput.setSingleLine(true);
        routeInput.setInputType(InputType.TYPE_TEXT_VARIATION_URI);
        routeLayout.addView(routeInput);

        // Form (scrollable)
        LinearLayout form = new LinearLayout(ctx);
        form.setOrientation(LinearLayout.VERTICAL);
        int pad = (int) (24 * d);
        form.setPadding(pad, (int) (8 * d), pad, 0);
        form.addView(instructions);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.topMargin = (int) (16 * d);
        form.addView(nameLayout, lp);
        form.addView(routeLayout);

        ScrollView scroll = new ScrollView(ctx);
        scroll.addView(form);

        AlertDialog dialog = new MaterialAlertDialogBuilder(ctx).setTitle("Add provider")
                .setIcon(R.drawable.ic_api_filled).setView(scroll).setNegativeButton("Cancel", null)
                .setNeutralButton("Github", null) // overridden below so it doesn't dismiss
                .setPositiveButton("Add", null) // overridden below to prevent auto-dismiss
                .create();

        dialog.setOnShowListener(di -> {
            dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(v -> {
                try {
                    ctx.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(GITHUB_URL))
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                } catch (Exception e) {
                    Toast.makeText(ctx, "No browser found", Toast.LENGTH_SHORT).show();
                }
            });

            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
                String name = String.valueOf(nameInput.getText()).trim();
                String route = String.valueOf(routeInput.getText()).trim();

                nameLayout.setError(null);
                routeLayout.setError(null);

                if (name.isEmpty()) {
                    nameLayout.setError("Name is required");
                } else if (apiServerList.containsName(name)) {
                    nameLayout.setError("Name already exists");
                } else if (route.isEmpty()) {
                    routeLayout.setError("Route is required");
                } else if (!route.startsWith("http://") && !route.startsWith("https://")) {
                    routeLayout.setError("URL must start with http:// or https://");
                } else {
                    apiServerList.addNewProvider(name, route);
                    dialog.dismiss();
                    if (onAdded != null)
                        onAdded.run();
                }
            });
        });
        dialog.show();
    }
}