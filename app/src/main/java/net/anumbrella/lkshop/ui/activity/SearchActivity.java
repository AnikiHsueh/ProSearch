package net.anumbrella.lkshop.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.cookie.CookieJarImpl;
import com.lzy.okgo.cookie.store.SPCookieStore;
import com.umeng.message.PushAgent;

import net.anumbrella.lkshop.R;
import net.anumbrella.lkshop.adapter.CategorizeDetailProductAdapter;
import net.anumbrella.lkshop.api.Api;
import net.anumbrella.lkshop.api.entity.Customer;
import net.anumbrella.lkshop.api.entity.Pro;
import net.anumbrella.lkshop.api.entity.ResultData;
import net.anumbrella.lkshop.db.DBManager;
import net.anumbrella.lkshop.model.bean.ListProductContentModel;
import net.anumbrella.lkshop.model.bean.LocalUserDataModel;
import net.anumbrella.lkshop.staticc.Env;
import net.anumbrella.lkshop.utils.BaseUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.OkHttpClient;

/**
 * author：Anumbrella
 * Date：18/5/25 下午8:08
 */
public class SearchActivity extends BaseThemeSettingActivity {

    private CategorizeDetailProductAdapter adapter;

    private GridLayoutManager girdLayoutManager;

    private String searchkeyWord;


    @BindView(R.id.search_all_toolbar)
    Toolbar toolbar;

    @BindView(R.id.search_all_data)
    EasyRecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        PushAgent.getInstance(this).onAppStart();
        toolbar.setTitle("搜索商品");
        setToolbar(toolbar);
        if (getIntent().getBundleExtra("search") != null) {
            searchkeyWord = getIntent().getBundleExtra("search").getString("search");
        }
        adapter = new CategorizeDetailProductAdapter(this);
        girdLayoutManager = new GridLayoutManager(this, 2);
        girdLayoutManager.setSpanSizeLookup(adapter.obtainTipSpanSizeLookUp());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setLayoutManager(girdLayoutManager);
        recyclerView.setAdapterWithProgress(adapter);
        recyclerView.setErrorView(R.layout.search_no_data);

        Map<String, String> map = new HashMap<>(4);
        map.put("name", searchkeyWord);
        JSONObject jsonObject = new JSONObject(map);

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.cookieJar(new CookieJarImpl(new SPCookieStore(this)));
        OkGo.getInstance().setOkHttpClient(builder.build());
        OkGo.<String>post(Api.contextPathPlus + "/pro/list")
                .tag(this)
                .upJson(jsonObject)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<String> response) {

                        Log.i("mdzz", response.body());

                        try {

                            ResultData result = com.alibaba.fastjson.JSONObject.parseObject(response.body(), ResultData.class);

                            if (null != result.getMessage()) {
                                Toast.makeText(SearchActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                            if ("10000".equals(result.getCode())) {

                                List<Pro> pros = com.alibaba.fastjson.JSONObject.parseArray(result.getData().toString(), Pro.class);

                                ArrayList<ListProductContentModel> data = new ArrayList<>();
                                ListProductContentModel one;
                                for (Pro l : pros) {
                                    one = new ListProductContentModel();
                                    one.setPid(l.getId());
                                    one.setTitle(l.getName());
//                                    one.setType(3);
                                    one.setImageUrl(Api.picUrl + l.getPicture());
                                    one.setPrice(Float.parseFloat(l.getPrice()));
//                                    one.setColor(5);
//                                    one.setStorage();
                                    one.setCarrieroperator(l.getCreator());
                                    one.setSum(l.getQuantity());
                                    one.setDetail(l.getDetail());
                                    one.setSerialNo(l.getSerialNo());
//                                    one.setUid(999);

                                    data.add(one);
                                }

                                adapter.addAll(data);

                                if (adapter.getCount() == 0) {
                                    recyclerView.showError();
                                }

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<String> response) {
                        Toast.makeText(SearchActivity.this, "网络不给力", Toast.LENGTH_SHORT).show();
                    }
                });


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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
