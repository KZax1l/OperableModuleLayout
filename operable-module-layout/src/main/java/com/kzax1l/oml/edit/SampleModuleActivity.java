package com.kzax1l.oml.edit;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.kzax1l.oml.R;
import com.kzax1l.oml.view.CheckedGridView;
import com.kzax1l.oml.view.UncheckedGridView;

/**
 * Created by Administrator on 2018-01-11.
 *
 * @author kzaxil
 * @since 1.0.1
 */
public class SampleModuleActivity extends AppCompatActivity {
    private ModuleActivityAgent mAgent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_modules);
        mAgent = new ModuleActivityAgent(this,
                (CheckedGridView) findViewById(R.id.userGridView),
                (UncheckedGridView) findViewById(R.id.otherGridView));
        mAgent.onCreate();
    }

    @Override
    public void onBackPressed() {
        if (mAgent != null && mAgent.isDataSetChanged()) {
            mAgent.onBackPressed();
            return;
        }
        super.onBackPressed();
    }
}
