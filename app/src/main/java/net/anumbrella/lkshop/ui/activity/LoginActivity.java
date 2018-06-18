package net.anumbrella.lkshop.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.cookie.CookieJarImpl;
import com.lzy.okgo.cookie.store.SPCookieStore;
import com.umeng.message.PushAgent;

import net.anumbrella.customedittext.FloatLabelView;
import net.anumbrella.lkshop.R;
import net.anumbrella.lkshop.api.Api;
import net.anumbrella.lkshop.api.ServiceApi;
import net.anumbrella.lkshop.api.entity.Customer;
import net.anumbrella.lkshop.api.entity.ResultData;
import net.anumbrella.lkshop.db.DBManager;
import net.anumbrella.lkshop.http.RetrofitHttp;
import net.anumbrella.lkshop.model.bean.LocalUserDataModel;
import net.anumbrella.lkshop.staticc.Env;
import net.anumbrella.lkshop.utils.BaseUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * author：Anumbrella
 * Date：18/5/24 下午7:02
 */
public class LoginActivity extends BaseThemeSettingActivity {

    private ProgressDialog mDialog;

    private String phone;

    private String password;

    private boolean prompt = false;

    private boolean checkUpResult = true;

    private String startUp;

    @BindView(R.id.btn_login)
    Button btn_login;

    @BindView(R.id.btn_register)
    Button btn_register;

    @BindView(R.id.forget_password)
    TextView forget_password;

    @BindView(R.id.btn_back)
    Button btn_back;

    @BindView(R.id.login_phone)
    FloatLabelView login_phone;

    @BindView(R.id.login_password)
    FloatLabelView login_password;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        PushAgent.getInstance(this).onAppStart();
        login_password.getEditText().setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        if (getIntent().getStringExtra("startUp") != null) {
            startUp = getIntent().getStringExtra("startUp");
        }
        mDialog = new ProgressDialog(this);
        mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mDialog.setMessage("请稍等");
        mDialog.setIndeterminate(false);
        // 设置ProgressDialog 是否可以按退回按键取消
        mDialog.setCancelable(false);
    }


    @OnClick({R.id.btn_register, R.id.btn_login, R.id.forget_password, R.id.btn_back})
    public void clickBtn(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                prompt = true;
                if (checkUpResult == false) {
                    checkUpResult = true;
                }
                getData();
                break;
            case R.id.btn_register:
                Intent intent_register = new Intent();
                intent_register.setClass(this, RegisterActivity.class);
                startActivity(intent_register);
                break;
            case R.id.forget_password:
                Intent intent_forget_password = new Intent();
                intent_forget_password.setClass(this, FindPasswordActivity.class);
                startActivity(intent_forget_password);
                break;
            case R.id.btn_back:
                if (startUp != null) {
                    if (startUp.equals("main")) {
                        finish();
                    } else {
                        Intent intent = new Intent();
                        intent.setClass(this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
                break;
            default:
                break;
        }
    }

    private void getData() {
        phone = login_phone.getEditText().getText().toString().trim();
        password = login_password.getEditText().getText().toString().trim();

        if (phone.equals("") && prompt) {
            Toast.makeText(this, "帐号不能为空", Toast.LENGTH_SHORT).show();
            checkUpResult = false;
            prompt = false;
        }

//        if (!checkPhoneNumber(phone) && prompt) {
//            Toast.makeText(this, "手机号格式不正确", Toast.LENGTH_SHORT).show();
//            checkUpResult = false;
//            prompt = false;
//        }


        if (password.equals("") && prompt) {
            Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
            checkUpResult = false;
            prompt = false;
        }

        doLogin();
    }

    private void doLogin() {
        if (checkUpResult) {
            mDialog.show();

            Map<String, String> map = new HashMap<>(4);
            map.put("loginName", phone);
            map.put("loginPwd", password);
            JSONObject jsonObject = new JSONObject(map);

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.cookieJar(new CookieJarImpl(new SPCookieStore(this)));
            OkGo.getInstance().setOkHttpClient(builder.build());
            OkGo.<String>post(Api.contextPathPlus + "/cust/login")
                    .tag(this)
                    .upJson(jsonObject)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(com.lzy.okgo.model.Response<String> response) {

                            Log.i("mdzz", response.body());

                            try {

                                ResultData result = com.alibaba.fastjson.JSONObject.parseObject(response.body(), ResultData.class);

                                mDialog.hide();
                                if (null != result.getMessage()) {
                                    Toast.makeText(LoginActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                                }

                                if ("10000".equals(result.getCode())) {

                                    Customer customer = com.alibaba.fastjson.JSONObject.parseObject(result.getData().toString(), Customer.class);

                                    String userName = customer.getNickName();
                                    String signName = customer.getLoginName();
                                    int uid = customer.getId();
                                    //保存用户数据
                                    LocalUserDataModel data = new LocalUserDataModel();
                                    data.setSignName(signName);
                                    data.setUid(uid);
                                    data.setUserImg(Env.iconImg);
                                    data.setUserName(userName);
                                    data.setHobby("null");
                                    data.setLogin(true);
                                    BaseUtils.saveLocalUser(LoginActivity.this, data);
                                    //添加用户
                                    DBManager.getManager(LoginActivity.this).addUser(userName, true, uid);
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            mDialog.dismiss();
                                            Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                                            if (startUp != null) {
                                                if (startUp.equals("main")) {
                                                    finish();
                                                } else if (startUp.equals("welcome")) {
                                                    Intent intent = new Intent();
                                                    intent.setClass(LoginActivity.this, MainActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            } else {
                                                Intent intent = new Intent();
                                                intent.setClass(LoginActivity.this, MainActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                            finish();
                                        }

                                    }, 1000);
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(com.lzy.okgo.model.Response<String> response) {
                            mDialog.hide();
                            Toast.makeText(LoginActivity.this, "网络不给力", Toast.LENGTH_SHORT).show();
                        }
                    });

        }

    }


    public static boolean checkPhoneNumber(String mobiles) {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        p = Pattern.compile("^[1][3,4,5,8][0-9]{9}$"); // 验证手机号
        m = p.matcher(mobiles);
        b = m.matches();
        return b;
    }

}
