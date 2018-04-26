package com.wt.piaoliuping.servlet;

import android.os.AsyncTask;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.wt.piaoliuping.Const;
import com.wt.piaoliuping.entity.About_Entity;
import com.wt.piaoliuping.utils.HttpUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: jiangyao
 * @date: 2018/4/8
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO: 关于我们
 */
public class About_Servlet extends AsyncTask<String, Integer, About_Entity> {
    private static final String TAG = "About_Servlet";

    @Override
    protected About_Entity doInBackground(String... strings) {
        Map map = new HashMap();
        map.put("", "");
        map.put("", "");
        map.put("", "");

        String res = HttpUtil.doPost(Const.URL + "", map);
        About_Entity entity;
        if (TextUtils.isEmpty(res)) {
            entity = new About_Entity();

        } else {
            try {
                entity = new Gson().fromJson(res, About_Entity.class);
            } catch (Exception e) {
                entity = new About_Entity();
            }
        }

        return entity;
    }

    @Override
    protected void onPostExecute(About_Entity about_entity) {
        super.onPostExecute(about_entity);

    }
}
