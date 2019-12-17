package activityest.com.example.hsc.a2019mf.view.ui.activity;


import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.hjq.permissions.OnPermission;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;

import activityest.com.example.hsc.a2019mf.R;

import java.util.ArrayList;
import java.util.List;

import activityest.com.example.hsc.a2019mf.view.ui.fragment.Fragment1;
import activityest.com.example.hsc.a2019mf.view.ui.fragment.Fragment2;
import activityest.com.example.hsc.a2019mf.view.ui.fragment.Fragment3;
import activityest.com.example.hsc.a2019mf.view.ui.fragment.Fragment4;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.bmob.v3.Bmob;

public class BottomNavigationActivity1 extends AppCompatActivity {

    @BindView(R.id.fl_fragment)
    FrameLayout flFragment;
    @BindView(R.id.iv1)
    ImageView iv1;
    @BindView(R.id.tv1)
    TextView tv1;
    @BindView(R.id.ll_tab1)
    LinearLayout llTab1;
    @BindView(R.id.iv2)
    ImageView iv2;
    @BindView(R.id.tv2)
    TextView tv2;
    @BindView(R.id.ll_tab2)
    LinearLayout llTab2;
    @BindView(R.id.iv3)
    ImageView iv3;
    @BindView(R.id.tv3)
    TextView tv3;
    @BindView(R.id.ll_tab3)
    LinearLayout llTab3;
    @BindView(R.id.iv4)
    ImageView iv4;
    @BindView(R.id.tv4)
    TextView tv4;
    @BindView(R.id.ll_tab4)
    LinearLayout llTab4;


    Unbinder unbinder;
    Fragment1 fragment1;
    Fragment2 fragment2;
    Fragment3 fragment3;
    Fragment4 fragment4;

    private Fragment fragment_now = null;
//    private List<View> textViews;
    private List<ImageView> iv_list;
    private List<TextView> tv_list;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
        //设置修改状态栏
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS); //透明导航栏
//            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        //设置状态栏的颜色，和你的app主题或者标题栏颜色设置一致就ok了
            window.setStatusBarColor(getResources().getColor(R.color.colorLightRed));
        }
        setContentView(R.layout.activity_bottom_navigation1);
        requestPermission();
        Bmob.initialize(this,"88eff1f4f7a1efe4fe3d28efeb65fd05");
        //绑定初始化ButterKnife
        unbinder = ButterKnife.bind(this);
        inint();

    }


    private void inint() {
        iv_list = new ArrayList<>();
        tv_list = new ArrayList<>();

        iv_list.add(iv1);
        iv_list.add(iv2);
        iv_list.add(iv3);
        iv_list.add(iv4);

        tv_list.add(tv1);
        tv_list.add(tv2);
        tv_list.add(tv3);
        tv_list.add(tv4);

//        textViews[0] = tv1;
//        textViews[1] = tv2;
//        textViews[2] = tv3;

        changePageSelect(0);
        changePageFragment(R.id.ll_tab1);


    }

    @OnClick({R.id.iv1, R.id.ll_tab1, R.id.iv2, R.id.ll_tab2, R.id.iv3, R.id.ll_tab3, R.id.iv4, R.id.ll_tab4})
    public void onViewClicked(View view) {
        changePageFragment(view.getId());

    }

    /**
     * 选中的tab 和 没有选中的tab 的图标和字体颜色
     *
     * @param index
     */
    public void changePageSelect(int index) {
        for (int i = 0; i < iv_list.size(); i++) {
            if (index == i) {
                iv_list.get(i).setEnabled(false);
                tv_list.get(i).setTextColor(getResources().getColor(R.color.colorLightRed));
            } else {
                iv_list.get(i).setEnabled(true);
                tv_list.get(i).setTextColor(getResources().getColor(R.color.colorTextGrey));
            }
        }
    }

    /**
     * 当点击导航栏时改变 fragment
     *
     * @param id
     */
    public void changePageFragment(int id) {
        switch (id) {
            case R.id.ll_tab1:
            case R.id.iv1:
                if (fragment1 == null) {//减少new fragmnet,避免不必要的内存消耗
                    fragment1 = Fragment1.newInstance();
                }
                changePageSelect(0);
                switchFragment(fragment_now, fragment1);
                break;
            case R.id.ll_tab2:
            case R.id.iv2:
                if (fragment2 == null) {
                    fragment2 = Fragment2.newInstance();
                }
                changePageSelect(1);
                switchFragment(fragment_now, fragment2);

                break;
            case R.id.ll_tab3:
            case R.id.iv3:
                if (fragment3 == null) {
                    fragment3 = Fragment3.newInstance();
                }
                changePageSelect(2);
                switchFragment(fragment_now, fragment3);
                break;
            case R.id.ll_tab4:
            case R.id.iv4:
                if (fragment4 == null) {
                    fragment4 = Fragment4.newInstance();
                }
                changePageSelect(3);
                switchFragment(fragment_now, fragment4);
                break;

        }
    }

    /**
     * 隐藏显示fragment
     *
     * @param from 需要隐藏的fragment
     * @param to   需要显示的fragment
     */
    public void switchFragment(Fragment from, Fragment to) {
        if (to == null)
            return;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (!to.isAdded()) {
            if (from == null) {
                transaction.add(R.id.fl_fragment, to).show(to).commit();
            } else {
                // 隐藏当前的fragment，add下一个fragment到Activity中并显示
                transaction.hide(from).add(R.id.fl_fragment, to).show(to).commitAllowingStateLoss();
            }
        } else {
            // 隐藏当前的fragment，显示下一个
            transaction.hide(from).show(to).commit();
        }
        fragment_now = to;

    }






    @Override
    protected void onDestroy() {
        super.onDestroy();
        //解除绑定
        unbinder.unbind();
    }

    public void reLoadFragView() {
        /*从FragmentManager中移除*/
        getSupportFragmentManager().beginTransaction().remove(fragment4).commit();
        /*重新创建*/
        fragment4=new Fragment4();
        changePageSelect(3);
        switchFragment(fragment_now, fragment4);

    }

    public void requestPermission() {
        XXPermissions.with(this)
                // 可设置被拒绝后继续申请，直到用户授权或者永久拒绝
                //.constantRequest()
                // 支持请求6.0悬浮窗权限8.0请求安装权限
                //.permission(Permission.REQUEST_INSTALL_PACKAGES)
                // 不指定权限则自动获取清单中的危险权限
                .permission(Permission.Group.STORAGE)
                .request(new OnPermission() {

                    @Override
                    public void hasPermission(List<String> granted, boolean isAll) {
                        if (isAll) {
                            Toast.makeText(BottomNavigationActivity1.this,"获取权限成功",Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(BottomNavigationActivity1.this,"获取权限成功，部分权限未正常授予",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void noPermission(List<String> denied, boolean quick) {
                        if(quick) {
                            Toast.makeText(BottomNavigationActivity1.this,"被永久拒绝授权，请手动授予权限",Toast.LENGTH_SHORT).show();
                            //如果是被永久拒绝就跳转到应用权限系统设置页面
                            XXPermissions.gotoPermissionSettings(BottomNavigationActivity1.this);
                        }else {
                            Toast.makeText(BottomNavigationActivity1.this,"获取权限失败",Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    public void isHasPermission(View view) {
        if (XXPermissions.isHasPermission(BottomNavigationActivity1.this, Permission.Group.STORAGE)) {
            Toast.makeText(BottomNavigationActivity1.this,"已经获取到权限，不需要再次申请了",Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(BottomNavigationActivity1.this,"还没有获取到权限或者部分权限未授予",Toast.LENGTH_SHORT).show();
        }
    }

    public void gotoPermissionSettings(View view) {
        XXPermissions.gotoPermissionSettings(BottomNavigationActivity1.this);
    }



}
