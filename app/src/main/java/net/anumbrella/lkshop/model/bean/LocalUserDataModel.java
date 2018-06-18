package net.anumbrella.lkshop.model.bean;

/**
 * author：Anumbrella
 * Date：18/6/21 下午6:00
 */
public class LocalUserDataModel {

    private int uid;
    private String userName;
    private String signName;
    private String userImg;
    private String hobby;
    private boolean isLogin;

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSignName() {
        return signName;
    }

    public void setSignName(String signName) {
        this.signName = signName;
    }

    public String getUserImg() {
        return userImg;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    @Override
    public String toString() {
        return "LocalUserDataModel{" +
                "uid=" + uid +
                ", userName='" + userName + '\'' +
                ", signName='" + signName + '\'' +
                ", userImg='" + userImg + '\'' +
                ", hobby='" + hobby + '\'' +
                ", isLogin=" + isLogin +
                '}';
    }
}
