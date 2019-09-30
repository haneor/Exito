package kr.co.exito_com.www.exito.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import kr.co.exito_com.www.exito.HistoryActivity;
import kr.co.exito_com.www.exito.List_ItemActivity;
import kr.co.exito_com.www.exito.LoginActivity;
import kr.co.exito_com.www.exito.MainActivity;
import kr.co.exito_com.www.exito.R;
import kr.co.exito_com.www.exito.recycleview_item.HLVAdapter;
import kr.co.exito_com.www.exito.recycleview_item.HistoryAdapter;

public class HeadFragment extends Fragment {

    Context context;

    private ViewGroup rootView;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;

    private ArrayList<String> aTime = new ArrayList<>();
    private ArrayList<String> aPhone = new ArrayList<>();
    private ArrayList<String> aStart = new ArrayList<>();
    private ArrayList<String> aGet = new ArrayList<>();
    private ArrayList<String> aSpace = new ArrayList<>();
    private ArrayList<String> aPrice = new ArrayList<>();
    private ArrayList<String> aIndex = new ArrayList<>();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_head, container, false);
        recyclerView = rootView.findViewById(R.id.head_frag_recycler);

        List_ItemActivity.checkActivity = "B";

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                onLoadDateItem();
            }
        };
        Handler handler = new Handler();
        handler.postDelayed(runnable, 500);
        return rootView;
    }

    private void onLoadDateItem() {

        aTime.clear();
        aPhone.clear();
        aStart.clear();
        aGet.clear();
        aSpace.clear();
        aPrice.clear();
        aIndex.clear();

        RequestQueue postRequestQueue = Volley.newRequestQueue(context);
        // 서버에 php파일 올리고 서버 주소 변경하기.
        StringRequest postStringRequest = new StringRequest(Request.Method.POST, "http://eor0601.dothome.co.kr/exito/exito_head_load.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject_response = new JSONObject(response);
                    JSONArray jsonArray = (JSONArray) jsonObject_response.get("response");
                    for(int i = 0; i<jsonArray.length(); i++) {
                        JSONObject result = (JSONObject) jsonArray.get(i);

                        aTime.add(result.getString("create_time"));
                        aPhone.add(result.getString("phone_number"));
                        aStart.add(result.getString("start"));
                        aGet.add(result.getString("get"));
                        aSpace.add(result.getString("space"));
                        aPrice.add(result.getString("price"));
                        aIndex.add(result.getString("auto_index"));

                        mAdapter = new HistoryAdapter(context, aTime, aPhone, aStart, aGet, aSpace, aPrice, aIndex);
                        recyclerView.setAdapter(mAdapter);
                    }

                    if(aStart.size() != 0) {
                        recyclerView.setHasFixedSize(true);
                        mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                        recyclerView.setLayoutManager(mLayoutManager);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("check_id", LoginActivity.LOGIN_ID);
                params.put("item_state", "ing");
                return params;
            }
        };
        postRequestQueue.add(postStringRequest);
    }


    private void clearList() {
        aTime.clear();
        aPhone.clear();
        aStart.clear();
        aGet.clear();
        aSpace.clear();
        aPrice.clear();
        aIndex.clear();
    }

}
