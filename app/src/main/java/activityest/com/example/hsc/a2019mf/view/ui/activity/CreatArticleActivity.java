package activityest.com.example.hsc.a2019mf.view.ui.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import activityest.com.example.hsc.a2019mf.R;
import activityest.com.example.hsc.a2019mf.model.bean.advertisement;
import activityest.com.example.hsc.a2019mf.model.bean.article;
import activityest.com.example.hsc.a2019mf.utils.Uri2PathUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

public class CreatArticleActivity extends AppCompatActivity {
    @BindView(R.id.write_text_back)
    TextView writeTextBack;
    @BindView(R.id.write_send)
    TextView writeSend;
    @BindView(R.id.creation_write_title)
    TextInputEditText creationWriteTitle;
    @BindView(R.id.iv_take_pic)
    ImageView ivTakePic;
    @BindView(R.id.creation_write_content)
    TextInputEditText creationWriteContent;
    @BindView(R.id.imageView)
    ImageView imageView;
    private String imageUrl=null;
    private String username ;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.creation_take_pic);
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        ButterKnife.bind(this);

    }

    @OnClick({R.id.write_text_back, R.id.write_send, R.id.iv_take_pic,R.id.creation_write_content, R.id.imageView})
    public void onViewClicked(View view) {
        isHasPermission(view);
        switch (view.getId()) {
            case R.id.write_text_back:
                finish();
                break;
            case R.id.write_send:

//                upload(imageUrl,username);
                if(!creationWriteTitle.getText().toString().trim().equals("")&&!creationWriteContent.getText().toString().trim().equals("")&&imageUrl!=null)    //不为空
                {
                    Log.e("upload", "图片路径为"+imageUrl);
                    upload(imageUrl,username);
                }
                else{
                    Toast.makeText(CreatArticleActivity.this,"要发布的内容不完整",Toast.LENGTH_SHORT).show();
                }
                finish();
                break;
            case R.id.iv_take_pic:
                Intent intent = new Intent();
                /* 开启Pictures画面Type设定为image */
                intent.setType("image/*");
                /* 使用Intent.ACTION_GET_CONTENT这个Action */
                intent.setAction(Intent.ACTION_PICK);
                /* 取得相片后返回本画面 */
                startActivityForResult(intent, 1);
                break;
            case R.id.creation_write_content:

                break;
            case R.id.imageView:

                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Uri uri = data.getData();
            ContentResolver cr = this.getContentResolver();
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                /* 将Bitmap设定到ImageView */
                imageView.setImageBitmap(bitmap);
                /*路径转化为String*/
                Log.e("qwe", "图片URI为"+uri);
                imageUrl = Uri2PathUtil.getRealPathFromUri(CreatArticleActivity.this, uri);
                Log.e("qwe", "图片路径为"+imageUrl);
            } catch (FileNotFoundException e) {
                Log.e("qwe", e.getMessage(),e);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //上传图文到表中
    private void upload(String imageUri,String username){
            final BmobFile bmobFile = new BmobFile(new File(imageUri));
            bmobFile.uploadblock(new UploadFileListener() {
                @Override
                public void done(BmobException e) {
                    if(e==null){
                        article ac = new article();
                        ac.setArticleImage(bmobFile);//该文章图片
                        ac.setArticleTitle(creationWriteTitle.getText().toString());
                        ac.setArticleContent(creationWriteContent.getText().toString());
                        ac.setArticleAuthor(username);
                        ac.setArticleClass("");
                        ac.save(new SaveListener<String>() {
                            @Override
                            public void done(String objectId, BmobException e) {
                                if (e == null) {
                                    Log.e("用户图片列表添加成功","原因：",e);
                                    Toast.makeText(CreatArticleActivity.this,"发布成功！" ,Toast.LENGTH_SHORT).show();
                                } else {
                                    Log.e("用户图片列表添加失败","原因：",e);
                                    Toast.makeText(CreatArticleActivity.this,"发布失败：" + e.getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        //bmobFile.getFileUrl()--返回的上传文件的完整地址
                        Log.e("图片路径",bmobFile.getFileUrl());
                    }else{
                        Toast.makeText(CreatArticleActivity.this,"发布失败：" + e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onProgress(Integer value) {
                    // 返回的上传进度（百分比）
                }
            });
        }
    public void isHasPermission(View view) {
        if (XXPermissions.isHasPermission(CreatArticleActivity.this, Permission.Group.STORAGE)) {
//            Toast.makeText(CreatVideoActivity.this,"已经获取到权限，不需要再次申请了",Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(CreatArticleActivity.this,"还没有获取到权限或者部分权限未授予",Toast.LENGTH_SHORT).show();
            gotoPermissionSettings(view);
        }
    }

    public void gotoPermissionSettings(View view) {
        XXPermissions.gotoPermissionSettings(CreatArticleActivity.this);
    }

}
