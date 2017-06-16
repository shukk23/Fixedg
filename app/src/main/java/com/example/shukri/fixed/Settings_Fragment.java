package com.example.shukri.fixed;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;


public class Settings_Fragment extends Fragment {

    Switch darkmode;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.settings_fragment,container,false);

        darkmode=(Switch) v.findViewById(R.id.darkMode);


        if (darkmode.isChecked()){
            getActivity().getApplication().setTheme(R.style.Material);

        }

        return v;

    }
}
