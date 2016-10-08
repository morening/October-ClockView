package com.morening.project;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import com.morening.project.project_clockview.R;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
    }
}
