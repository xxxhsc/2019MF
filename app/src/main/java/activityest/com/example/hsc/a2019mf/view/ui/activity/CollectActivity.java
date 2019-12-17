package activityest.com.example.hsc.a2019mf.view.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import activityest.com.example.hsc.a2019mf.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CollectActivity extends AppCompatActivity {
    @BindView(R.id.collect_back)
    ImageView collectBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_collect);
        ButterKnife.bind(this);
        //获取从MyFragment_basicMessage中传过来的值
        Intent intent = getIntent();
        String objectId = intent.getStringExtra("username");
//        show_info(objectId);
//        ButterKnife.bind(this);
    }

    @OnClick(R.id.collect_back)
    public void onViewClicked() {
        finish();
    }
}
