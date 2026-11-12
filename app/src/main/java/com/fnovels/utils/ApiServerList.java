package com.fnovels.utils;

import android.content.Context;
import android.content.SharedPreferences;
import com.fnovels.model.api.Provider;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class ApiServerList {

    private static final String PREF_NAME = "api_server_preferences";
    private static final String KEY_PROVIDERS = "api_providers";
    private static final String KEY_SELECTED = "api_selected_provider";
    private static final String KEY_SEEDED = "api_defaults_seeded";

    // name -> route
    private static final String[][] DEFAULT_PROVIDERS = {
            { "Default", "https://book-api-teal.vercel.app/" },
            { "Provider 2", "https://novel-api-liard.vercel.app/" }, };

    private final SharedPreferences sharedPref;
    private final Gson gson = new Gson();
    private final ArrayList<Provider> apiProviderList;

    public ApiServerList(Context context) {
        sharedPref = context.getApplicationContext().getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        apiProviderList = loadProviders();
        seedDefaultsIfNeeded();
    }

    private void seedDefaultsIfNeeded() {
        if (sharedPref.getBoolean(KEY_SEEDED, false))
            return;
        addDefaults();
        sharedPref.edit().putBoolean(KEY_SEEDED, true).apply();
    }

    private void addDefaults() {
        for (String[] d : DEFAULT_PROVIDERS) {
            if (!containsName(d[0]))
                apiProviderList.add(new Provider(d[0], d[1]));
        }
        savedProviders();
    }

    public boolean containsName(String name) {
        for (Provider p : apiProviderList) {
            if (p.getName().equalsIgnoreCase(name))
                return true;
        }
        return false;
    }

    public void addNewProvider(String name, String route) {
        apiProviderList.add(new Provider(name, route));
        savedProviders();
        // First provider becomes the default
        if (apiProviderList.size() == 1)
            setSelectedProvider(name);
    }

    public ArrayList<Provider> getListOfProviders() {
        return new ArrayList<>(apiProviderList);
    }

    /** Returns the selected provider, falling back to the first one (and saving it). */
    public Provider getSelectedProvider() {
        if (apiProviderList.isEmpty())
            return null;

        String selected = sharedPref.getString(KEY_SELECTED, null);
        if (selected != null) {
            for (Provider p : apiProviderList) {
                if (p.getName().equals(selected))
                    return p;
            }
        }
        Provider first = apiProviderList.get(0);
        setSelectedProvider(first.getName());
        return first;
    }

    public void setSelectedProvider(String name) {
        sharedPref.edit().putString(KEY_SELECTED, name).apply();
    }

    public boolean removeProviderByName(String name) {
        for (int i = 0; i < apiProviderList.size(); i++) {
            if (apiProviderList.get(i).getName().equals(name)) {
                removeProvider(i);
                return true;
            }
        }
        return false;
    }

    public void removeProvider(int position) {
        if (position < 0 || position >= apiProviderList.size())
            return;

        String removedName = apiProviderList.get(position).getName();
        apiProviderList.remove(position);
        savedProviders();

        // If the selected one was removed, reset so getSelectedProvider() falls back to first
        if (removedName.equals(sharedPref.getString(KEY_SELECTED, null))) {
            sharedPref.edit().remove(KEY_SELECTED).apply();
        }
    }

    public void resetToDefaults() {
        addDefaults();
    }

    public int getProviderCount() {
        return apiProviderList.size();
    }

    public void clearProviders() {
        apiProviderList.clear();
        savedProviders();
        sharedPref.edit().remove(KEY_SELECTED).apply();
    }

    private void savedProviders() {
        sharedPref.edit().putString(KEY_PROVIDERS, gson.toJson(apiProviderList)).apply();
    }

    private ArrayList<Provider> loadProviders() {
        String json = sharedPref.getString(KEY_PROVIDERS, null);
        if (json == null)
            return new ArrayList<>();

        Type type = new TypeToken<ArrayList<Provider>>() {
        }.getType();
        ArrayList<Provider> providers = gson.fromJson(json, type);
        return providers != null ? providers : new ArrayList<>();
    }
}