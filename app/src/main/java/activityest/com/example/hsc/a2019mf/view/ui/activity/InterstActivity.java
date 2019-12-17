package activityest.com.example.hsc.a2019mf.view.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.cjj.MaterialRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import activityest.com.example.hsc.a2019mf.R;
import activityest.com.example.hsc.a2019mf.model.bean.interst;
import activityest.com.example.hsc.a2019mf.view.adapter.InterstRecycleAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class InterstActivity extends AppCompatActivity {

    public RecyclerView mInterstRecyclerView;
    @BindView(R.id.interest_back)
    ImageView interestBack;
    private ArrayList<interst> InterstEntityList = new ArrayList<>();
    private InterstRecycleAdapter mInterstRecycleAdapter;
    private MaterialRefreshLayout materialRefreshLayout;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_interest);
        ButterKnife.bind(this);
        //获取从MyFragment_basicMessage中传过来的值
        Intent intent = getIntent();
        String objectId = intent.getStringExtra("username");
        iniData(objectId);
//        show_info(objectId);
//        ButterKnife.bind(this);
    }

    private void iniData(String objectId) {

        BmobQuery<interst> queryinterst = new BmobQuery<>();
        Log.e("username", objectId);
        queryinterst.addWhereEqualTo("username", objectId);
        queryinterst.findObjects(new FindListener<interst>() {
            @Override
            public void done(List<interst> list, BmobException e) {
                if (e == null) {
                    InterstEntityList.addAll(list);
                    initRecyclerView();
                } else {
                    Log.e("BMOB", e.toString());

                }
            }
        });


    }

    private void initRecyclerView() {
        mInterstRecyclerView = findViewById(R.id.interest_recyclerView);
        mInterstRecycleAdapter = new InterstRecycleAdapter(InterstActivity.this, InterstEntityList);
        mInterstRecycleAdapter.setOnItemClickLitener(new InterstRecycleAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, String objectId) {
            }

            @Override
            public void onItemLongClick(View view, String objectId) {
//                Toast.makeText(getActivity(),"长按了"+position,Toast.LENGTH_SHORT).show();
            }
        });
        mInterstRecyclerView.setAdapter(mInterstRecycleAdapter);
        mInterstRecyclerView.setLayoutManager(new LinearLayoutManager(InterstActivity.this, LinearLayoutManager.VERTICAL, false));
    }

    @OnClick(R.id.interest_back)
    public void onViewClicked() {
        finish();
    }
}
