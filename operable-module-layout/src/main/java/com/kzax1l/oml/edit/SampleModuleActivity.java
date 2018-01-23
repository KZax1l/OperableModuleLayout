package com.kzax1l.oml.edit;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.kzax1l.oml.R;

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
        // 注意这里实例化一定要放在super.onCreate和setContentView之前
        mAgent = new ModuleActivityAgent(this, R.id.userGridView, R.id.otherGridView);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_modules);
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
