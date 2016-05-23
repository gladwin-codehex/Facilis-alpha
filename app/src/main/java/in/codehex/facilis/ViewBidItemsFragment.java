package in.codehex.facilis;


import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.codehex.facilis.app.AppController;
import in.codehex.facilis.app.Config;
import in.codehex.facilis.model.ViewBidItem;


/**
 * A fragment that is used to display bid item list for the {@link ActiveBidsFragment},
 * {@link PreviousBidsFragment}, {@link SuccessfulBidsFragment} class.
 */
public class ViewBidItemsFragment extends Fragment {

    SwipeRefreshLayout mRefreshLayout;
    RecyclerView mRecyclerView;
    List<ViewBidItem> mViewBidItemList;
    ViewBidItemsAdapter mAdapter;
    SharedPreferences userPreferences;
    LinearLayoutManager mLayoutManager;
    int mOrderId, mBidId;

    public ViewBidItemsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_bid_items, container, false);

        initObjects(view);
        prepareObjects();

        return view;
    }

    /**
     * Initialize the objects.
     *
     * @param view the root view of the layout.
     */
    private void initObjects(View view) {
        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.view_bid_list);

        mViewBidItemList = new ArrayList<>();
        mLayoutManager = new LinearLayoutManager(getContext());
        mAdapter = new ViewBidItemsAdapter(getContext(), mViewBidItemList);
        userPreferences = getActivity().getSharedPreferences(Config.PREF_USER,
                Context.MODE_PRIVATE);
    }

    /**
     * Implement and manipulate the objects.
     */
    private void prepareObjects() {
        Bundle bundle = getArguments();
        // TODO: handle null value of arguments
        if (bundle != null) {
            mOrderId = bundle.getInt(Config.KEY_BUNDLE_ORDER_ID);
            mBidId = bundle.getInt(Config.KEY_BUNDLE_BID_ID);
        }

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mRefreshLayout.setColorSchemeColors(R.color.primary, R.color.primary_dark, R.color.accent);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRefreshLayout.setRefreshing(true);
                processBidItems();
            }
        });
        mRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.setRefreshing(true);
                processBidItems();
            }
        });
    }

    /**
     * Fetch the bid item list from the server.
     */
    private void processBidItems() {
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Config.KEY_API_USER, userPreferences.getInt(Config.KEY_PREF_USER_ID, 0));
            jsonObject.put(Config.KEY_API_ORDER, mOrderId);
            jsonObject.put(Config.KEY_API_BID, mBidId);
        } catch (JSONException e) {
            // TODO: remove toast
            Toast.makeText(getContext(),
                    "Error occurred while generating data - "
                            + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Config.API_VIEW_BID_ITEMS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mViewBidItemList.clear();
                mAdapter.notifyDataSetChanged();
                mRefreshLayout.setRefreshing(false);
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        int id = object.getInt(Config.KEY_API_ID);
                        String name = object.getString(Config.KEY_API_NAME);
                        int order = object.getInt(Config.KEY_API_ORDER);
                        String quantity = object.getString(Config.KEY_API_QUANTITY);
                        String brand = object.getString(Config.KEY_API_BRAND);
                        String description = object.getString(Config.KEY_API_DESC);
                        if (TextUtils.isEmpty(description))
                            description = "-";
                        int delCharge = object.getInt(Config.KEY_API_DEL_CHARGE);
                        double percentage = object.getDouble(Config.KEY_API_PERCENTAGE);
                        int itemAmount = object.getInt(Config.KEY_API_ITEM_AMOUNT);
                        mViewBidItemList.add(new ViewBidItem(percentage, id, order,
                                delCharge, itemAmount, name, quantity, brand, description));
                    }
                } catch (JSONException e) {
                    // TODO: remove toast
                    Toast.makeText(getContext(),
                            "Error occurred while parsing data - "
                                    + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse response = error.networkResponse;
                try {
                    byte[] bytes = response.data;
                    String data = new String(bytes);
                    if (response.statusCode == 400) {
                        JSONObject errorObject = new JSONObject(data);
                        // TODO: login again in case of invalid user
                        int mError = errorObject.getInt(Config.KEY_API_ERROR);
                        String mMessage = errorObject.getString(Config.KEY_API_MESSAGE);
                        // TODO: remove toast
                        Toast.makeText(getContext(), mMessage, Toast.LENGTH_SHORT).show();
                    } else if (response.statusCode == 401) {
                        JSONObject errorObject = new JSONObject(data);
                        String errorData = errorObject.getString(Config.KEY_API_DETAIL);
                        // TODO: remove toast
                        Toast.makeText(getContext(), errorData, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    // TODO: remove toast
                    Toast.makeText(getContext(),
                            "Error occurred while parsing data - "
                                    + e.getMessage(), Toast.LENGTH_SHORT).show();
                } catch (NullPointerException e) {
                    // TODO: remove toast
                    Toast.makeText(getContext(), "Network error - "
                            + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Token " +
                        userPreferences.getString(Config.KEY_PREF_TOKEN, null));
                return headers;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                return jsonObject.toString().getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest, "view_bid_items");
    }

    /**
     * View adapter for the recycler view of the view bid item list.
     */
    private class ViewBidItemsAdapter
            extends RecyclerView.Adapter<ViewBidItemsAdapter.ViewBidItemsHolder> {

        Context context;
        List<ViewBidItem> mViewBidItemList;

        public ViewBidItemsAdapter(Context context, List<ViewBidItem> mViewBidItemList) {
            this.context = context;
            this.mViewBidItemList = mViewBidItemList;
        }

        @Override
        public ViewBidItemsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_view_bid, parent, false);
            return new ViewBidItemsHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewBidItemsHolder holder, int position) {
            ViewBidItem viewBidItem = mViewBidItemList.get(position);
            String name = viewBidItem.getName() + " - " + viewBidItem.getBrand();
            String quantity = "Quantity: " + viewBidItem.getQuantity();
            String description = "Description: " + viewBidItem.getDescription();
            String amount = "\u20B9 " + String.valueOf(viewBidItem.getItemAmount());

            holder.textName.setText(name);
            holder.textDescription.setText(description);
            holder.textQuantity.setText(quantity);
            holder.textAmount.setText(amount);
        }

        @Override
        public int getItemCount() {
            return mViewBidItemList.size();
        }

        protected class ViewBidItemsHolder extends RecyclerView.ViewHolder {

            private TextView textName, textDescription, textQuantity, textAmount;

            public ViewBidItemsHolder(View view) {
                super(view);
                textName = (TextView) view.findViewById(R.id.text_name);
                textDescription = (TextView) view.findViewById(R.id.text_description);
                textQuantity = (TextView) view.findViewById(R.id.text_quantity);
                textAmount = (TextView) view.findViewById(R.id.text_amount);
            }
        }
    }
}
