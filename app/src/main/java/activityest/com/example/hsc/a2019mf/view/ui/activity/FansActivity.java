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
import activityest.com.example.hsc.a2019mf.view.adapter.FansRecycleAdapter;
import activityest.com.example.hsc.a2019mf.view.adapter.InterstRecycleAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class FansActivity extends AppCompatActivity {

    @BindView(R.id.fans_back)
    ImageView fansback;
    @BindView(R.id.fans_recyclerView)
    RecyclerView fansRecyclerView;

    public RecyclerView mFansRecyclerView;

    private ArrayList<interst> InterstEntityList = new ArrayList<>();
    private FansRecycleAdapter mFansRecycleAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_fans);
        ButterKnife.bind(this);
        //获取从MyFragment_basicMessage中传过来的值
        Intent intent = getIntent();
        String objectId = intent.getStringExtra("username");
        iniData(objectId);
//        show_info(objectId);
//        ButterKnife.bind(this);
    }

    private void iniData(String username) {
        BmobQuery<interst> queryinterst = new BmobQuery<>();
        Log.e("username", username);
        queryinterst.addWhereEqualTo("fatherusername", username);
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
        mFansRecyclerView = findViewById(R.id.fans_recyclerView);
        mFansRecycleAdapter = new FansRecycleAdapter(FansActivity.this, InterstEntityList);
        mFansRecycleAdapter.setOnItemClickLitener(new FansRecycleAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, String objectId) {
            }

            @Override
            public void onItemLongClick(View view, String objectId) {
//                Toast.makeText(getActivity(),"长按了"+position,Toast.LENGTH_SHORT).show();
            }
        });
        mFansRecyclerView.setAdapter(mFansRecycleAdapter);
        mFansRecyclerView.setLayoutManager(new LinearLayoutManager(FansActivity.this, LinearLayoutManager.VERTICAL, false));
    }


    @OnClick(R.id.fans_back)
    public void onViewClicked() {
        finish();
    }
}
