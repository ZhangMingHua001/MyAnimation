package cn.zmh.animation.activity;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import cn.zmh.animation.R;
import cn.zmh.animation.util.ZLog;
import cn.zmh.animation.view.IStickLayout;
import cn.zmh.animation.view.StickLayout;

public class MainActivity extends Activity implements IStickLayout {
    private final static String TAG = "MainActivity";
    private StickLayout mStickLayout;
    private TextView mHead;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mStickLayout = (StickLayout) findViewById(R.id.activity_main);
        mStickLayout.setIStickLayout(this);
        mHead = (TextView) findViewById(R.id.text_head);
        mHead.setTextSize(30);
    }

    @Override
    public void update(float value) {
        ZLog.v(TAG, "update value:" + value);
        mHead.setTextSize(30 * (1f - value));
    }

    @Override
    public boolean getIntercept() {
        return true;
    }
}
