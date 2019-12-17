package activityest.com.example.hsc.a2019mf.model.bean;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by 何盛昌 on 2019/2/25.
 */
public class User extends BmobUser {


    private String realname;                    //用户姓名
    private BmobFile Picture;//用户头像
    //获取用户姓名
    public String getRealname() {                   //获取用户姓名
        return realname;
    }
    //设置用户姓名
    public void setRealname(String realname) {       //输入用户姓名
        this.realname = realname;
    }
    public BmobFile getPicture() {
        return Picture;
    }

    public void setPicture(BmobFile icon) {
        Picture = icon;
    }

}