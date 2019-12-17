package activityest.com.example.hsc.a2019mf.view.ui.activity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.DrawableRes;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import activityest.com.example.hsc.a2019mf.R;
import activityest.com.example.hsc.a2019mf.model.bean.User;
import activityest.com.example.hsc.a2019mf.model.bean.advertisement;
import activityest.com.example.hsc.a2019mf.utils.Uri2PathUtil;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;
import de.hdodenhof.circleimageview.CircleImageView;


public class RegisterActivity extends MainActivity {

    private EditText mAccount;                        //用户名编辑
    private EditText mPwd;                            //密码编辑
    private EditText mPwdCheck;                       //密码编辑
    private EditText mRealN;                          //姓名编辑
    private Button mSureButton;                       //确定按钮
    private Button mCancelButton;                     //取消按钮
    private ImageView iv_see_password;              //密码隐藏或显示
    private ImageView iv_see_password2;             //密码隐藏或显示
    private de.hdodenhof.circleimageview.CircleImageView CircleImageView;
    private String imageUri=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            //设置修改状态栏
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //设置状态栏的颜色，和你的app主题或者标题栏颜色设置一致就ok了
            window.setStatusBarColor(getResources().getColor(R.color.pink_main));
        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//        }
        setContentView(R.layout.activity_register_plus);
        mAccount = (EditText) findViewById(R.id.resetpwd_edit_name);
        mPwd = (EditText) findViewById(R.id.resetpwd_edit_pwd_old);
        mPwdCheck = (EditText) findViewById(R.id.resetpwd_edit_pwd_new);
        mRealN = (EditText)findViewById(R.id.resetpwd_edit_realName);
        iv_see_password = (ImageView) findViewById(R.id.iv_see_password);
        iv_see_password2 = (ImageView) findViewById(R.id.iv_see_password2);
        CircleImageView=(de.hdodenhof.circleimageview.CircleImageView)findViewById(R.id.CircleImageView);
//
//        Toast.makeText(RegisterActivity.this,"本地图片地址：" + imageUri,Toast.LENGTH_SHORT).show();

        mSureButton = (Button) findViewById(R.id.register_btn_sure);
        mCancelButton = (Button) findViewById(R.id.register_btn_cancel);

        mSureButton.setOnClickListener(m_register_Listener);      //注册界面按钮的监听事件
        mCancelButton.setOnClickListener(m_register_Listener);
        iv_see_password.setOnClickListener(m_register_Listener);
        iv_see_password2.setOnClickListener(m_register_Listener);
        CircleImageView.setOnClickListener(m_register_Listener);
    }

    View.OnClickListener m_register_Listener = new View.OnClickListener() {    //不同按钮按下的监听事件选择
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.register_btn_sure:                       //确认按钮的监听事件
                    signUp();
                    break;
                case R.id.register_btn_cancel:                     //取消按钮的监听事件,由注册界面返回登录界面
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    finish();//关闭页面
                    break;
                case R.id.iv_see_password:
                    setPasswordVisibility();    //改变图片并设置输入框的文本可见或不可见
                    break;
                case R.id.iv_see_password2:
                    setPasswordVisibility2();    //改变图片并设置输入框的文本可见或不可见
                    break;
                case R.id.CircleImageView:
                    Intent intent = new Intent();
                    /* 开启Pictures画面Type设定为image */
                    intent.setType("image/*");
                    /* 使用Intent.ACTION_GET_CONTENT这个Action */
                    intent.setAction(Intent.ACTION_PICK);
                    /* 取得相片后返回本画面 */
                    startActivityForResult(intent, 1);
                    break;
            }
        }
    };

    public void signUp(){
        if (isMessageValid()){
            String userName = mAccount.getText().toString().trim();
            String userPwd = mPwd.getText().toString().trim();
            String userRealN = mRealN.getText().toString().trim();




            final User user = new User();
            user.setUsername(userName);
            user.setPassword(userPwd);
            user.setRealname(userRealN);

            user.signUp(new SaveListener<BmobUser>() {
                @Override
                public void done(BmobUser bmobUser, BmobException e){
                    if (e==null){
                            uploadHeadImage(imageUri);
                            showToast("注册成功！");
                            startActivity(new Intent(RegisterActivity.this, BottomNavigationActivity1.class));
                            finish();//关闭页面

                    }else {
                        showToast("注册失败！");
                        Log.e("注册失败","原因：",e);
                    }

                }
            });


        }

    }

    //上传图片到表中
    private void uploadHeadImage(String imageUri){
        if(imageUri==null)
        {
            BitmapDrawable d = (BitmapDrawable) getResources().getDrawable(R.drawable.zhuzhu);
            Bitmap img = d.getBitmap();

            String fn = "image_test.png";
            String path = getFilesDir() + File.separator + fn;
            try{
                OutputStream os = new FileOutputStream(path);
                img.compress(Bitmap.CompressFormat.PNG, 100, os);
                os.close();
            }catch(Exception e){
                Log.e("TAG", "", e);
            }
            Log.w("图片路径",path);
            final BmobFile bmobFile = new BmobFile(new File(path));
            bmobFile.uploadblock(new UploadFileListener() {
                @Override
                public void done(BmobException e) {
                    if(e==null){
                        advertisement ad = new advertisement();
                        ad.setUsername(mAccount.getText().toString().trim());//当前的用户名
                        ad.setPicture(bmobFile);//该用户的头像图片
                        ad.save(new SaveListener<String>() {
                            @Override
                            public void done(String objectId, BmobException e) {
                                if (e == null) {
                                    Log.e("用户图片列表添加成功","原因：",e);
                                } else {
                                    Log.e("用户图片列表添加失败","原因：",e);
                                }
                            }
                        });

                        //bmobFile.getFileUrl()--返回的上传文件的完整地址
                        Log.w("图片路径",bmobFile.getFileUrl());
                    }else{
                        Toast.makeText(RegisterActivity.this,"上传头像失败：" + e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }

            });
        }
        else{
        final BmobFile bmobFile = new BmobFile(new File(imageUri));
        bmobFile.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    advertisement ad = new advertisement();
                    ad.setUsername(mAccount.getText().toString().trim());//当前的用户名
                    ad.setPicture(bmobFile);//该用户的头像图片
                    ad.save(new SaveListener<String>() {
                        @Override
                        public void done(String objectId, BmobException e) {
                            if (e == null) {
                                Log.e("用户图片列表添加成功","原因：",e);
                            } else {
                                Log.e("用户图片列表添加失败","原因：",e);
                            }
                        }
                    });

                    //bmobFile.getFileUrl()--返回的上传文件的完整地址
                    Log.w("图片路径",bmobFile.getFileUrl());
                }else{
                    Toast.makeText(RegisterActivity.this,"上传头像失败：" + e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }

        });
     }
    }

    public boolean isMessageValid() {
        if (mAccount.getText().toString().trim().equals("")) {
            showToast(getString(R.string.account_empty));
            return false;
        } else if (mPwd.getText().toString().trim().equals("")) {
            showToast(getString(R.string.pwd_empty));
            return false;
        }else if(mPwdCheck.getText().toString().trim().equals("")) {
            showToast(getString(R.string.pwd_check_empty));
            return false;
        }else if(mRealN.getText().toString().trim().equals("")){
            showToast(getString(R.string.realname_check_empty));
            return false;
        }else if (!(mPwd.getText().toString().trim().equals(mPwdCheck.getText().toString().trim()))){
            showToast(getString(R.string.pwd_not_the_same));
            return false;
        }
        return true;
    }

    /**
     * 设置密码可见和不可见的相互转换
     */
    private void setPasswordVisibility() {
        if (iv_see_password.isSelected()) {
            iv_see_password.setSelected(false);
            //密码不可见
            mPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        } else {
            iv_see_password.setSelected(true);
            //密码可见
            mPwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        }
    }

    private void setPasswordVisibility2(){
        if (iv_see_password2.isSelected()) {
            iv_see_password2.setSelected(false);
            //密码不可见
            mPwdCheck.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        } else {
            iv_see_password2.setSelected(true);
            //密码可见
            mPwdCheck.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        }
    }




    public void showToast(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(RegisterActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Uri uri = data.getData();

            ContentResolver cr = this.getContentResolver();
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                /* 将Bitmap设定到ImageView */
                CircleImageView.setImageBitmap(bitmap);
                /*路径转化为String*/
                Log.e("qwe", "图片URI为"+uri);
                imageUri= Uri2PathUtil.getRealPathFromUri(RegisterActivity.this,uri);
                Log.e("qwe", "图片路径为"+imageUri);
            } catch (FileNotFoundException e) {
                Log.e("qwe", e.getMessage(),e);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


}
