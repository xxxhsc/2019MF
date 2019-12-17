package activityest.com.example.hsc.a2019mf.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.lang.ref.WeakReference;

import activityest.com.example.hsc.a2019mf.R;

public class FollowStateButton extends RelativeLayout {
    private Context mContext;
    private TextView mTextView;
    private ProgressBar mProgressBar;
    private MyHand mMyHand;
    private onFollowLister mOnFollowLister;
    private int type =0; // 0为加关注 1 关注
    public interface onFollowLister  {
        void  onFollowClickLister();
    }
    public  static  class  MyHand extends Handler {
        WeakReference<FollowStateButton> weakReference;
        public MyHand(FollowStateButton followStateButton){
            weakReference =new WeakReference(followStateButton);
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            weakReference.get().updataUI(msg.what,msg.obj.toString());
        }
    }
    private  void updataUI(int what , String text){
        Log.d("okhttp","updataUI="+what);
        switch (what) {

            case 0: //添加关注
                mProgressBar.clearAnimation();
                mProgressBar.setVisibility(View.GONE);
                mTextView.setTextColor(getResources().getColor(R.color.white));
                mTextView.setText(text);
                mTextView.setBackgroundResource(R.drawable.shape_rectangle_red);
                Log.d("okhttp","0="+what);
                mTextView.setVisibility(View.VISIBLE);
                break;
            case 1: //关注成功
                mProgressBar.setVisibility(View.GONE);
                mTextView.setTextColor(getResources().getColor(R.color.gray));
                mTextView.setText(text);
                mTextView.setBackgroundResource(R.drawable.border_cicle_color_gray);
                Log.d("okhttp","1="+what);
                mTextView.setVisibility(View.VISIBLE);
                break;
            case 2:
                //设定按钮颜色以及进度条可见性
                mProgressBar.setVisibility(View.VISIBLE);
                mTextView.setText(text);
                mTextView.setVisibility(View.GONE);
                Log.d("okhttp","2="+what);

                break;
        }

    }

    public void setmOnFollowLister(onFollowLister mOnFollowLister) {
        this.mOnFollowLister = mOnFollowLister;
    }

    public FollowStateButton(Context context) {
        super(context);
        this.mContext=context;
        mMyHand = new MyHand(this);
    }

    public FollowStateButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext=context;
        mMyHand = new MyHand(this);
    }

    public FollowStateButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext=context;
        mMyHand = new MyHand(this);
    }

    private void initView(){

        for (int i=0 ; i < getChildCount() ;i++){
            View view =getChildAt(i);
            if(view instanceof TextView ){
                mTextView = (TextView) view;
                mTextView.setTag(0);
            }else  if(view instanceof ProgressBar){
                mProgressBar = (ProgressBar) view;
            }
        }
        mTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                String obj ="";
                Log.d("okhttp",""+mTextView.getTag());
                if(type == 0) {
                    type = 1; //未关注 点击表示关注
                    obj ="已关注";
                }else  if(type == 1){
                    type = 0; //关注 点击表示取消关注
                    obj ="关注";
                }
                Log.d("okhttp","obj="+obj + ",tag="+type );
                Message message =Message.obtain();
                message.what = 2 ;
                message.obj  = "" ;
                mMyHand.sendMessage(message);

                Message message2 =Message.obtain();
                message2.what = type;
                message2.obj  = obj ;
                mMyHand.sendMessageDelayed(message2,1500);
                if(mOnFollowLister != null) {
                    mOnFollowLister.onFollowClickLister();
                }
            }
        });
    }
    //已经关注
    public void alreadyFollow(String text){
        type =1;
        Message message =Message.obtain();
        message.what = type ;
        message.obj  = text ;
        mMyHand.sendMessage(message);
    }
    //没有关注
    public void unFollow(String text){
        type =0;
        Message message =Message.obtain();
        message.what = type ;
        message.obj  = text ;
        mMyHand.sendMessage(message);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        initView();
    }
}
