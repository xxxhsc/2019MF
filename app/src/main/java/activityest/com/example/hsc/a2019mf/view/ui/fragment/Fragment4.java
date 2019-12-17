package activityest.com.example.hsc.a2019mf.view.ui.fragment;


import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import activityest.com.example.hsc.a2019mf.R;
import activityest.com.example.hsc.a2019mf.model.bean.User;
import activityest.com.example.hsc.a2019mf.model.bean.advertisement;
import activityest.com.example.hsc.a2019mf.model.bean.interst;
import activityest.com.example.hsc.a2019mf.view.ui.activity.BottomNavigationActivity1;
import activityest.com.example.hsc.a2019mf.view.ui.activity.CollectActivity;
import activityest.com.example.hsc.a2019mf.view.ui.activity.FansActivity;
import activityest.com.example.hsc.a2019mf.view.ui.activity.HistoryActivity;
import activityest.com.example.hsc.a2019mf.view.ui.activity.InterstActivity;
import activityest.com.example.hsc.a2019mf.view.ui.activity.LoginActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.FindListener;
import de.hdodenhof.circleimageview.CircleImageView;


public class Fragment4 extends Fragment {


    RelativeLayout exit;
    @BindView(R.id.line_interst)
    LinearLayout lineInterst;
    @BindView(R.id.line_fans)
    LinearLayout lineFans;
    @BindView(R.id.bt_collect)
    TextView btCollect;
    @BindView(R.id.bt_history)
    TextView btHistory;
    Unbinder unbinder;
    @BindView(R.id.tv_interst)
    TextView tvInterst;
    @BindView(R.id.tv_fans)
    TextView tvFans;
    private View rootview;
    private User customer;
    private TextView info1, info2, textView_tt,tv_history;
    private String cus_realname;
    private String username;
    private CircleImageView headImageView;
    private TextView bt_collect;


    public Fragment4() {
    }


    public static Fragment4 newInstance() {
        Fragment4 fragment = new Fragment4();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_4, container, false);
        //line1=rootview.findViewById(R.id.bt_collect);
        //line1.setOnClickListener(ButtonListener);

        unbinder = ButterKnife.bind(this, rootview);
        return rootview;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        exit = getActivity().findViewById(R.id.btn_exit2);
        textView_tt = getActivity().findViewById(R.id.textView_tt);
        tvInterst=getActivity().findViewById(R.id.tv_interst);
        tv_history=getActivity().findViewById(R.id.tv_history);

        info1 = getActivity().findViewById(R.id.info1);
        info2 = getActivity().findViewById(R.id.info2);
        headImageView = getActivity().findViewById(R.id.headImageView);
        lineInterst.setOnClickListener(ButtonListener);
        lineFans.setOnClickListener(ButtonListener);
        btCollect.setOnClickListener(ButtonListener);
        btHistory.setOnClickListener(ButtonListener);
        exit.setOnClickListener(ButtonListener);
        if (BmobUser.getCurrentUser(User.class) != null) {
            //显示个人信息
            textView_tt.setText("退出登录");
            showInfo();
        } else {
            textView_tt.setText("登录");
            show_youke();
        }

    }

    private void show_youke() {
        info1.setText("请登录");

//        startActivity(new Intent(getActivity(), LoginActivity.class));
    }

    //显示个人信息
    private void showInfo() {

        customer = BmobUser.getCurrentUser(User.class);//获取当前用户用户名
        cus_realname = customer.getRealname();
        username = customer.getUsername();
        info1.setText("用 户 名：" + username);
        info2.setText("真实姓名：" + cus_realname);
        //下载图片
        BmobQuery<advertisement> query = new BmobQuery<>();
        query.findObjects(new FindListener<advertisement>() {
            @Override
            public void done(List<advertisement> list, BmobException e) {
                if (e == null) {

                    show_ad(list);
                } else {
                    Toast.makeText(getContext(), "" + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
        BmobQuery<interst> queryinterst = new BmobQuery<>();
        Log.e("username", customer.getUsername());
        queryinterst.addWhereEqualTo("username", customer.getUsername());
        queryinterst.findObjects(new FindListener<interst>() {
            @Override
            public void done(List<interst> list, BmobException e) {
                if (e == null) {
                        tvInterst.setText(""+list.size());
                } else {
                    Log.e("BMOB", e.toString());

                }
            }
        });
        BmobQuery<interst> queryfans = new BmobQuery<>();
        queryfans.addWhereEqualTo("fatherusername", customer.getUsername());
        queryfans.findObjects(new FindListener<interst>() {
            @Override
            public void done(List<interst> list, BmobException e) {
                if (e == null) {
                    tvFans.setText(""+list.size());
                } else {
                    Log.e("BMOB", e.toString());
                }
            }
        });







    }

    public void show_ad(List<advertisement> list) {

        for (advertisement ad : list) {
            if (ad.getUsername() != null && customer.getUsername().equals(ad.getUsername())) {
                BmobFile icon = ad.getPicture();
                icon.download(new DownloadFileListener() {
                    @Override
                    public void onProgress(Integer integer, long l) {

                    }

                    @Override
                    public void done(String s, BmobException e) {
                        if (e == null) {
                            //设置圆形头像并显示

                            headImageView.setImageBitmap(BitmapFactory.decodeFile(s)); //根据地址解码并显示图片
                        } else {
                            Log.e("用户图片下载失败", "原因：", e);
                        }
                    }
                });
                break;
            }
        }

    }

    View.OnClickListener ButtonListener = new View.OnClickListener() {

        Bundle bundle = new Bundle();
        Intent intent = new Intent();
        BmobUser currentUser = BmobUser.getCurrentUser(User.class);

        @Override
        public void onClick(View v) {
            if (BmobUser.getCurrentUser(User.class) != null) {

                switch (v.getId()) {
                    case R.id.btn_exit2:
                        BmobUser.logOut();
                        Toast.makeText(getActivity(), "退出登录成功！", Toast.LENGTH_SHORT).show();
                        /*模拟提交成功后  重新加载布局*/

                        BottomNavigationActivity1 activity = (BottomNavigationActivity1) getActivity();
                        activity.reLoadFragView();
                        break;
//                关闭当前activity，并重新打开login
//                    startActivity(intent);      //跳转登录界面
                    case R.id.bt_collect:
                        intent = new Intent(getActivity(), CollectActivity.class); //参数1:Fragment所依存的Activity,参数2：要跳转的Activity
                        bundle.putString("username", currentUser.getUsername()); //放入所需要传递的值
                        intent.putExtras(bundle);
                        getActivity().startActivity(intent); //这里一定要获取到所在Activity再startActivity()；
                        break;
                    case R.id.line_fans:
                        intent = new Intent(getActivity(), FansActivity.class); //参数1:Fragment所依存的Activity,参数2：要跳转的Activity
                        bundle = new Bundle();
                        bundle.putString("username", currentUser.getUsername()); //放入所需要传递的值
                        intent.putExtras(bundle);
                        getActivity().startActivity(intent); //这里一定要获取到所在Activity再startActivity()；
                        break;
                    case R.id.line_interst:
                        intent = new Intent(getActivity(), InterstActivity.class); //参数1:Fragment所依存的Activity,参数2：要跳转的Activity
                        bundle = new Bundle();
                        bundle.putString("username", currentUser.getUsername()); //放入所需要传递的值
                        intent.putExtras(bundle);
                        getActivity().startActivity(intent); //这里一定要获取到所在Activity再startActivity()；
                        break;
                    case R.id.bt_history:
                        Intent intent = new Intent(getActivity(), HistoryActivity.class); //参数1:Fragment所依存的Activity,参数2：要跳转的Activity
                        Bundle bundle = new Bundle();
                        bundle.putString("username", currentUser.getUsername()); //放入所需要传递的值
                        intent.putExtras(bundle);
                        getActivity().startActivity(intent); //这里一定要获取到所在Activity再startActivity()；
                        break;

                }
            } else {
                Toast.makeText(getActivity(), "请登录", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), LoginActivity.class); //参数1:Fragment所依存的Activity,参数2：要跳转的Activity
                getActivity().startActivity(intent); //这里一定要获取到所在Activity再startActivity()；
            }

        }

        ;
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}