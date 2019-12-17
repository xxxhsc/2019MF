package activityest.com.example.hsc.a2019mf.view.ui.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hjq.permissions.OnPermission;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;

import activityest.com.example.hsc.a2019mf.R;
import activityest.com.example.hsc.a2019mf.model.bean.article;
import activityest.com.example.hsc.a2019mf.model.bean.video;
import activityest.com.example.hsc.a2019mf.utils.Uri2PathUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadBatchListener;
import cn.bmob.v3.listener.UploadFileListener;

public class CreatVideoActivity extends AppCompatActivity {


    @BindView(R.id.write_text_back)
    TextView writeTextBack;
    @BindView(R.id.write_send)
    TextView writeSend;
    @BindView(R.id.creation_write_title)
    TextInputEditText creationWriteTitle;
    @BindView(R.id.videoimageView)
    ImageView videoimageView;
    @BindView(R.id.iv_take_pic)
    ImageView ivTakePic;
    private String imageUrl=null;
    private String videoUrl=null;
    private String username;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.creation_take_video);

        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        ButterKnife.bind(this);
    }

    @OnClick({R.id.write_text_back, R.id.write_send,R.id.videoimageView, R.id.iv_take_pic})
    public void onViewClicked(View view) {
        isHasPermission(view);
        switch (view.getId()) {
            case R.id.write_text_back:
                finish();
                break;
            case R.id.write_send:
                System.out.println(videoUrl);
                System.out.println(imageUrl);
                if(!creationWriteTitle.getText().toString().trim().equals("")&&videoUrl!=null&&imageUrl!=null)    //不为空
                {
                    Log.e("图片路径",creationWriteTitle.getText().toString().trim());
                    Log.e("图片路径",imageUrl);
                    Log.e("视频路径",videoUrl);
                    Log.e("upload", "图片路径为"+imageUrl);
                    upload();
                    finish();
                }
                else{
                    Toast.makeText(CreatVideoActivity.this,"要发布的内容不完整",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.videoimageView:
                break;
            case R.id.iv_take_pic:
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                /* 开启Pictures画面Type设定为image */
                intent.setType("video/*");
                /* 使用Intent.ACTION_GET_CONTENT这个Action */

                /* 取得相片后返回本画面 */
                startActivityForResult(intent, 1);
                break;
        }
    }

    private void upload() {
        try {
            final String[] filePaths = new String[2];
            filePaths[0] = imageUrl;
            filePaths[1] = videoUrl;
            BmobFile.uploadBatch(filePaths, new UploadBatchListener() {
                @Override
                public void onSuccess(List<BmobFile> files, List<String> urls) {
                    //1、files-上传完成后的BmobFile集合，是为了方便大家对其上传后的数据进行操作，例如你可以将该文件保存到表中
                    //2、urls-上传文件的完整url地址
                    if(urls.size()==filePaths.length){//如果数量相等，则代表文件全部上传完成
                        video ac = new video();
                        ac.setVideoImage(files.get(0));
                        ac.setVideoFile(files.get(1));//该文章图片
                        ac.setVideoTitle(creationWriteTitle.getText().toString());
                        ac.setUsername(username);
                        try {
                            ac.setVideoTime(generateTime(getVideotime(videoUrl)));
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        ac.save(new SaveListener<String>() {
                            @Override
                            public void done(String objectId, BmobException e) {
                                if (e == null) {
                                    Log.e("视频列表添加成功","原因：",e);
                                    Toast.makeText(CreatVideoActivity.this,"发布成功！" ,Toast.LENGTH_SHORT).show();
                                } else {
                                    Log.e("视频列表添加失败","原因：",e);
                                    Toast.makeText(CreatVideoActivity.this,"发布失败：" + e.getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        //bmobFile.getFileUrl()--返回的上传文件的完整地址
                        Log.e("图片路径",files.get(0).getFileUrl());
                        Log.e("视频路径",files.get(1).getFileUrl());
                    }
                }

                @Override
                public void onError(int statuscode, String errormsg) {
                    Toast.makeText(CreatVideoActivity.this,"错误码"+statuscode +",错误描述："+errormsg,Toast.LENGTH_SHORT);
                }

                @Override
                public void onProgress(int curIndex, int curPercent, int total,int totalPercent) {
                    //1、curIndex--表示当前第几个文件正在上传
                    //2、curPercent--表示当前上传文件的进度值（百分比）
                    //3、total--表示总的上传文件数
                    //4、totalPercent--表示总的上传进度（百分比）
                }
            });
        } catch (Exception e) {
            Log.e("qwe", e.getMessage(), e);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Uri uri = data.getData();
                /*路径转化为String*/
                Log.e("qwe", "视频URI为" + uri);
                videoUrl = Uri2PathUtil.getRealPathFromUri(CreatVideoActivity.this, uri);
                /* 将Bitmap设定到ImageView */
                Log.e("qwe", "视频URL为" + videoUrl);
            try {
                MediaMetadataRetriever media = new MediaMetadataRetriever();
                media.setDataSource(CreatVideoActivity.this,uri);
                Bitmap bitmap  = media.getFrameAtTime(1, MediaMetadataRetriever.OPTION_CLOSEST_SYNC );
                videoimageView.setImageBitmap(bitmap);//对应的ImageView
                String fn = "video_image.png";
                imageUrl = getFilesDir() + File.separator + fn;
                try{
                    OutputStream os = new FileOutputStream(imageUrl);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
                    os.close();
                }catch(Exception e){
                    Log.e("TAG", "", e);
                }
                Log.w("图片路径",imageUrl);
            }catch (IllegalArgumentException ex){
                ex.printStackTrace();
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    /**
     * 获取视频文件截图
     *
     * @param path 视频文件的路径
     * @return Bitmap 返回获取的Bitmap
     */
    public  static Bitmap getVideoThumb(String path) {
        try {
            MediaMetadataRetriever media = new MediaMetadataRetriever();
            media.setDataSource("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4",new HashMap<String, String>());
            Bitmap bitmap  = media.getFrameAtTime(1, MediaMetadataRetriever.OPTION_CLOSEST_SYNC );

        }catch (IllegalArgumentException ex){
            ex.printStackTrace();
        }


        MediaMetadataRetriever media = new MediaMetadataRetriever();
        media.setDataSource(path);
        return  media.getFrameAtTime();
    }

    public long getVideotime(String path) throws IOException {

        MediaPlayer meidaPlayer = new MediaPlayer();

        meidaPlayer.setDataSource(path);

        meidaPlayer.prepare();

        long time = meidaPlayer.getDuration();//获得了视频的时长（以毫秒为单位）
        return time;
    }

    /**
     * 将毫秒转时分秒
     *
     * @param time
     * @return
     */
    public static String generateTime(long time) {
        int totalSeconds = (int) (time / 1000);
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        return hours > 0 ? String.format("%02d:%02d:%02d", hours, minutes, seconds) : String.format("%02d:%02d", minutes, seconds);
    }

    public void requestPermission(View view) {
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
                            Toast.makeText(CreatVideoActivity.this,"获取权限成功",Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(CreatVideoActivity.this,"获取权限成功，部分权限未正常授予",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void noPermission(List<String> denied, boolean quick) {
                        if(quick) {
                            Toast.makeText(CreatVideoActivity.this,"被永久拒绝授权，请手动授予权限",Toast.LENGTH_SHORT).show();
                            //如果是被永久拒绝就跳转到应用权限系统设置页面
                            XXPermissions.gotoPermissionSettings(CreatVideoActivity.this);
                        }else {
                            Toast.makeText(CreatVideoActivity.this,"获取权限失败",Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    public void isHasPermission(View view) {
        if (XXPermissions.isHasPermission(CreatVideoActivity.this, Permission.Group.STORAGE)) {
//            Toast.makeText(CreatVideoActivity.this,"已经获取到权限，不需要再次申请了",Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(CreatVideoActivity.this,"还没有获取到权限或者部分权限未授予",Toast.LENGTH_SHORT).show();
            gotoPermissionSettings(view);
        }
    }

    public void gotoPermissionSettings(View view) {
        XXPermissions.gotoPermissionSettings(CreatVideoActivity.this);
    }




}
