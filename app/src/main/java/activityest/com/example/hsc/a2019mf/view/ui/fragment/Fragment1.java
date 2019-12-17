package activityest.com.example.hsc.a2019mf.view.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
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
import java.util.Collections;
import java.util.Date;
import java.util.List;

import activityest.com.example.hsc.a2019mf.R;
import activityest.com.example.hsc.a2019mf.model.bean.article;
import activityest.com.example.hsc.a2019mf.view.adapter.ArticleRecycleAdapter;
import activityest.com.example.hsc.a2019mf.view.ui.activity.ArticleActivity;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class Fragment1 extends Fragment {
    private View rootview;
    public RecyclerView mArticleRecyclerView;
    private ArrayList<article> ArticleEntityList = new ArrayList<>();
    private ArticleRecycleAdapter mArticleRecycleAdapter;
    private MaterialRefreshLayout materialRefreshLayout;
    public Fragment1() {
    }


    public static Fragment1 newInstance() {
        Fragment1 fragment = new Fragment1();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview=inflater.inflate(R.layout.fragment_1, container, false);
        materialRefreshLayout=rootview.findViewById(R.id.news_refresh);
        iniData();
        iniMaterialRefresh();
        return rootview;
    }


    private void iniData(){
        BmobQuery<article> query=new BmobQuery<>();
        query.order("-createdAt");
        query.setLimit(10);
        query.findObjects(new FindListener<article>() {
            @Override
            public void done(List<article> list, BmobException e) {
                if(e == null){
                    ArticleEntityList.addAll(list);
                    initRecyclerView();
                }else{
                    Toast.makeText(getContext(),""+e.getMessage(),Toast.LENGTH_LONG).show();
                    Log.e("用户图片下载失败","原因：",e);
                }
            }
        });

    }
    private void initRecyclerView(){
        mArticleRecyclerView =rootview.findViewById(R.id.article_recyclerView);
        mArticleRecycleAdapter=new ArticleRecycleAdapter(getActivity(), ArticleEntityList);
        mArticleRecycleAdapter.setOnItemClickLitener(new  ArticleRecycleAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, String objectId) {
                Intent intent=new Intent(getActivity(),ArticleActivity.class); //参数1:Fragment所依存的Activity,参数2：要跳转的Activity
                Bundle bundle = new Bundle();
                bundle.putString("objectId",objectId); //放入所需要传递的值
                intent.putExtras(bundle);
                getActivity().startActivity(intent); //这里一定要获取到所在Activity再startActivity()；
            }
            @Override
            public void onItemLongClick(View view, String objectId) {
//                Toast.makeText(getActivity(),"长按了"+position,Toast.LENGTH_SHORT).show();
            }
        });
        mArticleRecyclerView.setAdapter(mArticleRecycleAdapter);
        mArticleRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
    }

    private void iniMaterialRefresh(){
        materialRefreshLayout.setLoadMore(true);
        materialRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(final MaterialRefreshLayout materialRefreshLayout) {
                materialRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        article article= ArticleEntityList.get(0);
                        Log.e("列表最新时间",article.getCreatedAt());

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date createdAtDate = null;
                        try {
                            createdAtDate = sdf.parse(article.getCreatedAt());
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
                                        ArticleEntityList.clear();
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
            }

            @Override
            public void onRefreshLoadMore(final MaterialRefreshLayout materialRefreshLayout) {

                materialRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //ArticleEntityList.add(0, null);
                        article article= ArticleEntityList.get(ArticleEntityList.size()-1);
                        Log.e("列表最新时间",article.getCreatedAt());

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date createdAtDate = null;
                        try {
                            createdAtDate = sdf.parse(article.getCreatedAt());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        final BmobDate bmobCreatedAtDate = new BmobDate(createdAtDate);
                        Log.e("内容最新时间", createdAtDate.toString());
                        BmobQuery<article> categoryBmobQuery = new BmobQuery<>();
                        categoryBmobQuery.order("-createdAt");
                        categoryBmobQuery.setLimit(10);
                        categoryBmobQuery.addWhereLessThan("createdAt", bmobCreatedAtDate);
                        categoryBmobQuery.findObjects(new FindListener<article>() {
                            @Override
                            public void done(List<article> object, BmobException e) {
                                if (e == null) {
                                    if (object.size()!=0) {
                                        int Index=ArticleEntityList.size();
                                        Log.e("上拉加载", "加载"+object.size()+"条内容");
                                        ArticleEntityList.addAll(object);
                                        mArticleRecycleAdapter.notifyItemRangeInserted(Index,Index+object.size());

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

    }


}
