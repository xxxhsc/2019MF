package activityest.com.example.hsc.a2019mf.view.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.hjq.permissions.OnPermission;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;

import java.util.List;

import activityest.com.example.hsc.a2019mf.R;
import activityest.com.example.hsc.a2019mf.model.bean.User;
import activityest.com.example.hsc.a2019mf.view.ui.activity.CreatArticleActivity;
import activityest.com.example.hsc.a2019mf.view.ui.activity.CreatTextActivity;
import activityest.com.example.hsc.a2019mf.view.ui.activity.CreatVideoActivity;
import activityest.com.example.hsc.a2019mf.view.ui.activity.LoginActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.bmob.v3.BmobUser;


public class Fragment3 extends Fragment {
    @BindView(R.id.line1)
    LinearLayout line1;
    @BindView(R.id.line2)
    LinearLayout line2;
    @BindView(R.id.line3)
    LinearLayout line3;
    @BindView(R.id.creation_recyclerView)
    RecyclerView creationRecyclerView;
    @BindView(R.id.news_refresh)
    MaterialRefreshLayout newsRefresh;
    Unbinder unbinder;
    private View rootview;


    public static Fragment3 newInstance() {
        Fragment3 fragment = new Fragment3();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_3, container, false);

        unbinder = ButterKnife.bind(this, rootview);
        return rootview;


    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.line1, R.id.line2, R.id.line3})
    public void onViewClicked(View view) {
        Bundle bundle = new Bundle();
        Intent intent = new Intent();
        BmobUser currentUser = BmobUser.getCurrentUser(User.class);
        if (BmobUser.getCurrentUser(User.class) != null) {
        switch (view.getId()) {
            case R.id.line1:
                intent = new Intent(getActivity(), CreatTextActivity.class); //参数1:Fragment所依存的Activity,参数2：要跳转的Activity
                bundle.putString("username", currentUser.getUsername()); //放入所需要传递的值
                intent.putExtras(bundle);
                getActivity().startActivity(intent); //这里一定要获取到所在Activity再startActivity()；
                break;
            case R.id.line2:
                intent = new Intent(getActivity(), CreatArticleActivity.class); //参数1:Fragment所依存的Activity,参数2：要跳转的Activity
                bundle.putString("username", currentUser.getUsername()); //放入所需要传递的值
                intent.putExtras(bundle);
                getActivity().startActivity(intent); //这里一定要获取到所在Activity再startActivity()；
                break;
            case R.id.line3:
                intent = new Intent(getActivity(), CreatVideoActivity.class); //参数1:Fragment所依存的Activity,参数2：要跳转的Activity
                bundle.putString("username", currentUser.getUsername()); //放入所需要传递的值
                intent.putExtras(bundle);
                getActivity().startActivity(intent); //这里一定要获取到所在Activity再startActivity()；
                break;
        }
        } else {
            Toast.makeText(getActivity(), "请登录", Toast.LENGTH_SHORT).show();
            intent = new Intent(getActivity(), LoginActivity.class); //参数1:Fragment所依存的Activity,参数2：要跳转的Activity
            getActivity().startActivity(intent); //这里一定要获取到所在Activity再startActivity()；
        }
    }

    public void requestPermission(View view) {
        XXPermissions.with(getActivity())
                // 可设置被拒绝后继续申请，直到用户授权或者永久拒绝
//                .constantRequest()
                // 支持请求6.0悬浮窗权限8.0请求安装权限
//                .permission(Permission.REQUEST_INSTALL_PACKAGES)
                // 不指定权限则自动获取清单中的危险权限android.permission.WRITE_EXTERNAL_STORAGE
                .permission(Permission.Group.STORAGE,Permission.Group.CALENDAR)
                .request(new OnPermission() {

                    @Override
                    public void hasPermission(List<String> granted, boolean isAll) {
                        if (isAll) {
                            Toast.makeText(getActivity(),"获取权限成功",Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(getActivity(),"获取权限成功，部分权限未正常授予",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void noPermission(List<String> denied, boolean quick) {
                        if(quick) {
                            Toast.makeText(getActivity(),"被永久拒绝授权，请手动授予权限",Toast.LENGTH_SHORT).show();
                            //如果是被永久拒绝就跳转到应用权限系统设置页面
                            XXPermissions.gotoPermissionSettings(getActivity());
                        }else {
                            Toast.makeText(getActivity(),"获取权限失败",Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    public void isHasPermission(View view) {
        if (XXPermissions.isHasPermission(getActivity(), Permission.Group.STORAGE)) {
            Toast.makeText(getActivity(),"已经获取到权限，不需要再次申请了",Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(getActivity(),"还没有获取到权限或者部分权限未授予",Toast.LENGTH_SHORT).show();
        }
    }

    public void gotoPermissionSettings(View view) {
        XXPermissions.gotoPermissionSettings(getActivity());
    }



}
