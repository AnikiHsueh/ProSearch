package net.anumbrella.lkshop.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.cookie.CookieJarImpl;
import com.lzy.okgo.cookie.store.SPCookieStore;
import com.umeng.message.PushAgent;

import net.anumbrella.lkshop.R;
import net.anumbrella.lkshop.api.Api;
import net.anumbrella.lkshop.api.entity.ProCate;
import net.anumbrella.lkshop.api.entity.ResultData;
import net.anumbrella.lkshop.model.bean.LocalUserDataModel;
import net.anumbrella.lkshop.utils.BaseUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;

/**
 * @author Anumbrella
 * @date 18-6-17 上午1:05
 */
public class UserHobbyActivity extends BaseThemeSettingActivity {

    @BindView(R.id.user_hobby_toolbar)
    Toolbar toolbar;

    @BindView(R.id.hobbies)
    LinearLayout hobbies;

    @BindView(R.id.save_hobby)
    TextView saveHobby;

    private int uid;

    private List<CheckBox> checkBoxs = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_hobby);
        ButterKnife.bind(this);
        PushAgent.getInstance(this).onAppStart();
        toolbar.setTitle("偏好");

        uid = BaseUtils.readLocalUser(UserHobbyActivity.this).getUid();
        setToolbar(toolbar);

        Map<String, String> map = new HashMap<>(4);
        map.put("parentId", "1000");
        JSONObject jsonObject = new JSONObject(map);

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.cookieJar(new CookieJarImpl(new SPCookieStore(this)));
        OkGo.getInstance().setOkHttpClient(builder.build());
        OkGo.<String>post(Api.contextPathPlus + "/proCate/list")
                .tag(this)
                .upJson(jsonObject)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<String> response) {

                        Log.i("mdzz", response.body());

                        try {

                            ResultData result = com.alibaba.fastjson.JSONObject.parseObject(response.body(), ResultData.class);

                            if ("10000".equals(result.getCode())) {
                                List<ProCate> proCates = com.alibaba.fastjson.JSONObject.parseArray(result.getData().toString(), ProCate.class);

                                for (ProCate l : proCates) {
                                    CheckBox checkBoxLayout = (CheckBox) getLayoutInflater().inflate(R.layout.checkbox, null);
                                    checkBoxLayout.setText(l.getId() + ":" + l.getName());
                                    checkBoxs.add(checkBoxLayout);

                                    hobbies.addView(checkBoxLayout);
                                }
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<String> response) {
                        Toast.makeText(UserHobbyActivity.this, "网络不给力", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @OnClick({R.id.save_hobby})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.save_hobby:

                String str = "";
                String[] desc;

                for (CheckBox l : checkBoxs) {
                    if(l.isChecked()){
                        desc = l.getText().toString().split(":");
                        str += desc[0] + ",";
                    }
                }

                Map<String, String> map = new HashMap<>(4);
                map.put("hobby", str);
                map.put("id", String.valueOf(uid));
                JSONObject jsonObject = new JSONObject(map);

                OkHttpClient.Builder builder = new OkHttpClient.Builder();
                builder.cookieJar(new CookieJarImpl(new SPCookieStore(this)));
                OkGo.getInstance().setOkHttpClient(builder.build());
                OkGo.<String>post(Api.contextPathPlus + "/cust/complete")
                        .tag(this)
                        .upJson(jsonObject)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(com.lzy.okgo.model.Response<String> response) {

                                Log.i("mdzz", response.body());

                                try {
                                    ResultData result = com.alibaba.fastjson.JSONObject.parseObject(response.body(), ResultData.class);
//                                    LocalUserDataModel data = BaseUtils.readLocalUser(UserHobbyActivity.this);
                                    if ("10000".equals(result.getCode())) {
                                        Toast.makeText(UserHobbyActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(UserHobbyActivity.this, "保存失败", Toast.LENGTH_SHORT).show();
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError(com.lzy.okgo.model.Response<String> response) {
                                Toast.makeText(UserHobbyActivity.this, "网络不给力", Toast.LENGTH_SHORT).show();
                            }
                        });

                break;
        }


    }

    /**
     * 建立toolbar
     *
     * @param toolbar
     */
    public void setToolbar(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
