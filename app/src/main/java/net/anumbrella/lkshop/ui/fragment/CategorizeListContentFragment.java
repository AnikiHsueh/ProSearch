package net.anumbrella.lkshop.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.cookie.CookieJarImpl;
import com.lzy.okgo.cookie.store.SPCookieStore;

import net.anumbrella.lkshop.R;
import net.anumbrella.lkshop.adapter.GridViewAdapter;
import net.anumbrella.lkshop.api.Api;
import net.anumbrella.lkshop.api.entity.Customer;
import net.anumbrella.lkshop.api.entity.Pro;
import net.anumbrella.lkshop.api.entity.ResultData;
import net.anumbrella.lkshop.config.Config;
import net.anumbrella.lkshop.db.DBManager;
import net.anumbrella.lkshop.model.bean.LocalUserDataModel;
import net.anumbrella.lkshop.model.bean.ProductTypeModel;
import net.anumbrella.lkshop.staticc.Env;
import net.anumbrella.lkshop.ui.activity.CategorizeDetailProductActivity;
import net.anumbrella.lkshop.ui.activity.LoginActivity;
import net.anumbrella.lkshop.ui.activity.MainActivity;
import net.anumbrella.lkshop.utils.BaseUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;

/**
 * author：Anumbrella
 * Date：18/5/26 下午7:13
 */
public class CategorizeListContentFragment extends Fragment {

    public ArrayList<ProductTypeModel> productListType;

    /**
     * widget网格view
     */
    private GridView gridView;


    private GridViewAdapter adapter;


    private ProductTypeModel productType;

    private String productName;

    private int icon;

    private Context mContext;

    private int uid;

    private int i = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
        uid = BaseUtils.readLocalUser(mContext).getUid();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.productlist_layout, null);
        gridView = (GridView) view.findViewById(R.id.GridViewList);
        int index = getArguments().getInt("index");
        productName = Config.categorizeTools[index];
        icon = Config.categorizeToolsImg[index];
        ((TextView) view.findViewById(R.id.productName)).setText(productName);

        Map<String, String> map = new HashMap<>(4);
        map.put("id", String.valueOf(uid));
        JSONObject jsonObject = new JSONObject(map);

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.cookieJar(new CookieJarImpl(new SPCookieStore(getActivity())));
        OkGo.getInstance().setOkHttpClient(builder.build());
        OkGo.<String>post(Api.contextPathPlus + "/pro/hobby")
                .tag(this)
                .upJson(jsonObject)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<String> response) {

                        Log.i("mdzz", response.body());

                        try {

                            ResultData result = com.alibaba.fastjson.JSONObject.parseObject(response.body(), ResultData.class);

                            if (null != result.getMessage()) {
                                Toast.makeText(getActivity(), result.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                            if ("10000".equals(result.getCode())) {

                                List<Pro> pros = com.alibaba.fastjson.JSONObject.parseArray(result.getData().toString(), Pro.class);

                                // 这里可以根据数据设定要填充的资源
                                productListType = new ArrayList<>();
                                for (Pro l : pros) {
                                    if (productName.equals(l.getIndirectCateName())) {
                                        productType = new ProductTypeModel(i++, Api.picUrl + l.getPicture(), productName, l.getName(), i++);
                                        productListType.add(productType);
                                    }
                                }

                                adapter = new GridViewAdapter(getActivity(), productListType);
                                gridView.setAdapter(adapter);
//                                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//                                    @Override
//                                    public void onItemClick(AdapterView<?> parent, View view,
//                                                            int position, long id) {
//                                        ProductTypeModel data = productListType.get(position);
//                                        Intent intent = new Intent();
//                                        intent.putExtra(CategorizeDetailProductActivity.INTENT_PRODUCT_ITEM_INFO, data);
//                                        intent.setClass(getContext(), CategorizeDetailProductActivity.class);
//                                        startActivity(intent);
//                                    }
//                                });

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<String> response) {
                        Toast.makeText(getActivity(), "网络不给力", Toast.LENGTH_SHORT).show();
                    }
                });


        //临时数据
//        productType = new ProductTypeModel(0, icon, productName, "全新 " + productName, Integer.parseInt(Config.productTypeArrays[1][0]));


        return view;
    }
}
