package activityest.com.example.hsc.a2019mf.view.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import activityest.com.example.hsc.a2019mf.R;
import activityest.com.example.hsc.a2019mf.model.bean.article;
import activityest.com.example.hsc.a2019mf.model.bean.video;
import activityest.com.example.hsc.a2019mf.view.adapter.VideoRecycleAdapter;
import activityest.com.example.hsc.a2019mf.view.ui.activity.VideoActivity;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.jzvd.JzvdStd;

public class Fragment2 extends Fragment {
    private View rootview;
    public static final int TAKE_PHOTO=1;
    public RecyclerView mVideoRecyclerView;
    private ArrayList<video> videoList = new ArrayList<>();
    private VideoRecycleAdapter mVideoRecycleAdapter;
    private MaterialRefreshLayout materialRefreshLayout;
    private JzvdStd player;

    public Fragment2() {

    }


    public static Fragment2 newInstance() {
        Fragment2 fragment = new Fragment2();
        return fragment;
    }


    @Override
    public void  onActivityResult(int requesCode, int resultCode, Intent data){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview=inflater.inflate(R.layout.fragment_2, container, false);
        materialRefreshLayout=rootview.findViewById(R.id.video_refresh);
        iniData();
        iniMaterialRefresh();
        return rootview;
    }

    private void iniData(){
        BmobQuery<video> query=new BmobQuery<>();
        query.setLimit(10);
        query.order("-createdAt");
        query.findObjects(new FindListener<video>() {
            @Override
            public void done(List<video> list, BmobException e) {
                if(e == null){
                    videoList.addAll(list);
                    initRecyclerView();
                }else{
                    Toast.makeText(getContext(),""+e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });

    }
    private void initRecyclerView(){
        mVideoRecyclerView =rootview.findViewById(R.id.video_recyclerView);
        mVideoRecycleAdapter=new VideoRecycleAdapter(getActivity(), videoList);
        mVideoRecycleAdapter.setOnItemClickLitener((view, objectId, path) -> {
            Intent intent=new Intent(getActivity(), VideoActivity.class); //参数1:Fragment所依存的Activity,参数2：要跳转的Activity
            Bundle bundle = new Bundle();
            bundle.putString("objectId",objectId); //放入所需要传递的值
            Log.e("onItemClick视频地址为", path);
            bundle.putString("path",path); //放入所需要传递的值
            intent.putExtras(bundle);
            getActivity().startActivity(intent); //这里一定要获取到所在Activity再startActivity()；
        });
        mVideoRecyclerView.setAdapter(mVideoRecycleAdapter);
        mVideoRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    private void iniMaterialRefresh(){

        materialRefreshLayout.setLoadMore(true);
        materialRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(final MaterialRefreshLayout materialRefreshLayout) {
                materialRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        video vi= videoList.get(0);

                        Log.e("列表最新时间",vi.getCreatedAt());

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date createdAtDate = null;
                        try {
                            createdAtDate = sdf.parse(vi.getCreatedAt());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        final BmobDate bmobCreatedAtDate = new BmobDate(createdAtDate);
                        Log.e("内容最新时间", createdAtDate.toString());
                        BmobQuery<article> categoryBmobQuery = new BmobQuery<>();
                        categoryBmobQuery.addWhereGreaterThanOrEqualTo("createdAt", bmobCreatedAtDate);
                        categoryBmobQuery.order("-createdAt");
//                        categoryBmobQuery.setLimit(5);
                        categoryBmobQuery.findObjects(new FindListener<article>() {
                            @Override
                            public void done(List<article> object, BmobException e) {
                                if (e == null) {
                                    if (object.size()==1||object.size()==0) {
                                        Log.e("下拉刷新", "没有新的内容");
                                        Toast.makeText(getActivity(),"没有新的内容",Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        Log.e("下拉刷新", ""+object.size()+"条新的内容");
                                        Toast.makeText(getActivity(),"刷新成功，"+(object.size()-1)+"条新的内容",Toast.LENGTH_SHORT).show();
                                        videoList.clear();
                                        iniData();
                                    }
                                } else {
                                    Log.e("下拉刷新BMOB", e.toString());
                                }
                            }
                        });
                        materialRefreshLayout.finishRefresh();

                    }
                }, 500);


            }

            @Override
            public void onfinish() {
//                Toast.makeText(getActivity(), "刷新成功", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onRefreshLoadMore(final MaterialRefreshLayout materialRefreshLayout) {
                materialRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //ArticleEntityList.add(0, null);
                        video vi= videoList.get(videoList.size()-1);
                        Log.e("列表最新时间",vi.getCreatedAt());

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date createdAtDate = null;
                        try {
                            createdAtDate = sdf.parse(vi.getCreatedAt());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        final BmobDate bmobCreatedAtDate = new BmobDate(createdAtDate);
                        Log.e("内容最新时间", createdAtDate.toString());
                        BmobQuery<video> categoryBmobQuery = new BmobQuery<>();
                        categoryBmobQuery.order("-createdAt");
                        categoryBmobQuery.setLimit(10);
                        categoryBmobQuery.addWhereLessThan("createdAt", bmobCreatedAtDate);
                        categoryBmobQuery.findObjects(new FindListener<video>() {
                            @Override
                            public void done(List<video> object, BmobException e) {
                                if (e == null) {
                                    if (object.size()!=0) {
                                        int Index=videoList.size();
                                        Log.e("上拉加载", "加载"+object.size()+"条内容");
                                        videoList.addAll(object);
                                        mVideoRecycleAdapter.notifyItemRangeInserted(Index,Index+object.size());

                                    }
                                    else{
                                        Log.e("上拉加载", "加载"+object.size()+"条内容");
                                        Toast.makeText(getActivity(),"加载成功，没有更多内容了",Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Log.e("上拉加载BMOB", e.toString());
                                }
                            }
                        });
                        materialRefreshLayout.finishRefreshLoadMore();
                    }

                }, 500);
//                Toast.makeText(getContext(), "加载更多", Toast.LENGTH_LONG).show();
            }


        });


        //materialRefreshLayout.autoRefresh();

    }

}
