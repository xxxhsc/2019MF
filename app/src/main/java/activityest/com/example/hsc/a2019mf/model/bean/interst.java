package activityest.com.example.hsc.a2019mf.model.bean;


import cn.bmob.v3.BmobObject;

public class interst extends BmobObject {
    private String username;//用户名
    private String fatherusername;//关注的用户名


    public String getFatherusername() {
        return fatherusername;
    }

    public String getUsername() {
        return username;
    }

    public void setFatherusername(String fatherusername) {
        this.fatherusername = fatherusername;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
