package com.wt.piaoliuping.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by wangtao on 2017/11/12.
 */

@Entity(nameInDb = "user")
public class UserDao {
    @Id
    private Long id;
    private String userName;
    private String nick;
    private String avatar;

    @Generated(hash = 1267848447)
    public UserDao(Long id, String userName, String nick, String avatar) {
        this.id = id;
        this.userName = userName;
        this.nick = nick;
        this.avatar = avatar;
    }

    @Generated(hash = 917059161)
    public UserDao() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNick() {
        return this.nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getAvatar() {
        return this.avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
