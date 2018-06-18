package net.anumbrella.lkshop.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
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
import net.anumbrella.lkshop.adapter.CollectAdapter;
import net.anumbrella.lkshop.api.Api;
import net.anumbrella.lkshop.api.entity.Pro;
import net.anumbrella.lkshop.api.entity.ResultData;
import net.anumbrella.lkshop.db.DBManager;
import net.anumbrella.lkshop.model.bean.ListProductContentModel;
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
 * Date：18/6/10 下午11:09
 */
public class CollectActivity extends BaseThemeSettingActivity {

    private static CollectAdapter adapter;

    private static EasyRecyclerView recyclerView;

    private GridLayoutManager girdLayoutManager;

    private Handler handler = new Handler();

    private static int uid;


    @BindView(R.id.collect_all_toolbar)
    Toolbar toolbar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);
        recyclerView = (EasyRecyclerView) findViewById(R.id.collect_all_data);
        ButterKnife.bind(this);
        PushAgent.getInstance(this).onAppStart();
        uid = BaseUtils.readLocalUser(CollectActivity.this).getUid();
        toolbar.setTitle("我的收藏");
        setToolbar(toolbar);
        adapter = new CollectAdapter(this);
        girdLayoutManager = new GridLayoutManager(this, 2);
        girdLayoutManager.setSpanSizeLookup(adapter.obtainTipSpanSizeLookUp());
        recyclerView.setLayoutManager(girdLayoutManager);
        recyclerView.setErrorView(R.layout.collect_no_data_error);
        recyclerView.setAdapterWithProgress(adapter);
//        recyclerView.setRefreshListener(this);
        recyclerView.setRefreshing(false);
//        onRefresh();

        Map<String, String> map = new HashMap<>(4);
        map.put("custId", String.valueOf(uid));
        JSONObject jsonObject = new JSONObject(map);

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.cookieJar(new CookieJarImpl(new SPCookieStore(this)));
        OkGo.getInstance().setOkHttpClient(builder.build());
        OkGo.<String>post(Api.contextPathPlus + "/proFavor/list")
                .tag(this)
                .upJson(jsonObject)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<String> response) {

                        Log.i("mdzz", response.body());

                        try {

                            ResultData result = com.alibaba.fastjson.JSONObject.parseObject(response.body(), ResultData.class);

                            if (null != result.getMessage()) {
                                Toast.makeText(CollectActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(CollectActivity.this, "网络不给力", Toast.LENGTH_SHORT).show();
                    }
                });

    }


    private ArrayList<ListProductContentModel> setData() {
        ArrayList<ListProductContentModel> list = new ArrayList<ListProductContentModel>();
        list = (ArrayList<ListProductContentModel>) DBManager.getManager(this).getCollectListData(uid);
        return list;
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


//    @Override
//    public void onRefresh() {
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                adapter.clear();
//                adapter.addAll(setData());
//                if (adapter.getCount() == 0) {
//                    recyclerView.showError();
//                }
//            }
//        }, 1000);
//
//    }

    public void deleteCollect(ListProductContentModel data) {
        int pid = data.getPid();
        if (pid > 0 && uid > 0) {
            DBManager.getManager(this).deleteCollect(pid, uid);
        }
        adapter.clear();
        adapter.addAll(setData());
        adapter.notifyDataSetChanged();
        if (adapter.getCount() == 0) {
            recyclerView.showError();
        }
    }

//    @Override
//    protected void onResume() {
//        adapter.clear();
//        adapter.addAll(setData());
//        if (adapter.getCount() == 0) {
//            recyclerView.showError();
//        }
//        super.onResume();
//    }
}
