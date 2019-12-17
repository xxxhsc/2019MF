package activityest.com.example.hsc.a2019mf.view.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import activityest.com.example.hsc.a2019mf.R;
import activityest.com.example.hsc.a2019mf.model.bean.advertisement;
import activityest.com.example.hsc.a2019mf.model.bean.interst;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.FindListener;

public class FansRecycleAdapter extends RecyclerView.Adapter<FansRecycleAdapter.myViewHodler> {
    private Context context;
    private ArrayList<interst> insterstEntityArrayList;
    private FansRecycleAdapter.OnItemClickLitener mOnItemClickLitener;






    // 设置点击事件的接口，利用接口回调，来完成点击事件
    public interface OnItemClickLitener {
        void onItemClick(View view, String objectId);

        void onItemLongClick(View view, String objectId);
    }



    public void setOnItemClickLitener(FansRecycleAdapter.OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }


    public FansRecycleAdapter(Context context, ArrayList<interst> insterstEntityArrayList){
//        将传递过来的数据，赋值给本地变量
        this.context=context;
        this.insterstEntityArrayList=insterstEntityArrayList;

    }



    @Override
    public FansRecycleAdapter.myViewHodler onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView=View.inflate(context, R.layout.item_fans,null);
        return  new FansRecycleAdapter.myViewHodler(itemView);
    }
    class  myViewHodler extends RecyclerView.ViewHolder{
        private ImageView fansHead;
        private TextView fansAuthor;



        public myViewHodler(View itemView) {
            super(itemView);
            fansHead=itemView.findViewById(R.id.fans_head);
            fansAuthor=itemView.findViewById(R.id.fans_author);

        }
    }
    @Override
    public void onBindViewHolder(@NonNull final FansRecycleAdapter.myViewHodler holder, int i) {
//        final article data=insterstEntityArrayList.get(insterstEntityArrayList.size()-i-1);
        final interst data=insterstEntityArrayList.get(i);
        BmobQuery<advertisement> query=new BmobQuery<>();
        query.addWhereEqualTo("username", data.getUsername());
        query.findObjects(new FindListener<advertisement>() {
            @Override
            public void done(List<advertisement> list, BmobException e) {
                if(e == null){
                    for (advertisement ad : list){
                        if(ad.getUsername() != null && data.getUsername().equals(ad.getUsername())){
                            BmobFile icon= ad.getPicture();
                            icon.download(new DownloadFileListener() {
                                @Override
                                public void onProgress(Integer integer, long l) {

                                }
                                @Override
                                public void done(String s, BmobException e) {
                                    if(e == null){
                                        //设置圆形头像并显示
                                        holder.fansHead.setImageBitmap(BitmapFactory.decodeFile(s)); //根据地址解码并显示图片
                                        Log.e("头像图片为",s,e);
                                    }
                                    else{
                                        Log.e("用户图片下载失败","原因：",e);
                                    }
                                }
                            });
                            break;
                        }
                    }

                }else{
                    Log.e("用户图片下载失败","原因：",e);
                }
            }
        });
        holder.fansAuthor.setText(data.getUsername());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getLayoutPosition();
                String objectId =data.getObjectId();
                mOnItemClickLitener.onItemClick(holder.itemView, objectId);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int pos = holder.getLayoutPosition();
                String objectId =data.getObjectId();
                mOnItemClickLitener.onItemLongClick(holder.itemView, objectId);
                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return insterstEntityArrayList.size();
    }





}
