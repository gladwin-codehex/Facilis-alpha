package in.codehex.facilis;


import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
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
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import in.codehex.facilis.helper.DividerItemDecoration;
import in.codehex.facilis.model.OrderedItem;


/**
 * A fragment that is used to display ordered item list for the {@link CurrentOrdersFragment},
 * {@link SelectedOrdersFragment}, {@link ClosedOrdersFragment} class.
 */
public class OrderedItemsFragment extends Fragment {

    RecyclerView mRecyclerView;
    List<OrderedItem> mOrderedItemList;
    OrderedItemsAdapter mAdapter;
    SharedPreferences userPreferences;
    LinearLayoutManager mLayoutManager;
    int mOrderId;

    public OrderedItemsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ordered_items, container, false);

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
        mRecyclerView = (RecyclerView) view.findViewById(R.id.ordered_item_list);

        mOrderedItemList = new ArrayList<>();
        mLayoutManager = new LinearLayoutManager(getContext());
        mAdapter = new OrderedItemsAdapter(getContext(), mOrderedItemList);
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
        }

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL_LIST));

        processOrderedItems();
    }

    /**
     * Fetch the ordered items list from the server.
     */
    private void processOrderedItems() {
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Config.KEY_API_USER, userPreferences.getInt(Config.KEY_PREF_USER_ID, 0));
            jsonObject.put(Config.KEY_API_ORDER, mOrderId);
        } catch (JSONException e) {
            // TODO: remove toast
            Toast.makeText(getContext(),
                    "Error occurred while generating data - "
                            + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Config.API_VIEW_ORDERED_ITEMS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mOrderedItemList.clear();
                mAdapter.notifyDataSetChanged();
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        int id = object.getInt(Config.KEY_API_ID);
                        String name = object.getString(Config.KEY_API_NAME);
                        String quantity = object.getString(Config.KEY_API_QUANTITY);
                        String brand = object.getString(Config.KEY_API_BRAND);
                        String desc = object.getString(Config.KEY_API_DESC);
                        mOrderedItemList.add(new OrderedItem(id, name, quantity, brand, desc));
                        mAdapter.notifyItemInserted(i);
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

        AppController.getInstance().addToRequestQueue(stringRequest, "view_ordered_items");
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    /**
     * View adapter for the recycler view of the ordered item list.
     */
    private class OrderedItemsAdapter
            extends RecyclerView.Adapter<OrderedItemsAdapter.OrderedItemsHolder> {

        Context context;
        List<OrderedItem> mOrderedItemList;

        public OrderedItemsAdapter(Context context, List<OrderedItem> mOrderedItemList) {
            this.context = context;
            this.mOrderedItemList = mOrderedItemList;
        }

        @Override
        public OrderedItemsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_ordered, parent, false);
            return new OrderedItemsHolder(view);
        }

        @Override
        public void onBindViewHolder(OrderedItemsHolder holder, int position) {
            OrderedItem orderedItem = mOrderedItemList.get(position);
            holder.textName.setText(orderedItem.getName());
            holder.textBrand.setText(orderedItem.getBrand());
            holder.textDesc.setText(orderedItem.getDescription());
            holder.textQuantity.setText(orderedItem.getQuantity());
        }

        @Override
        public int getItemCount() {
            return mOrderedItemList.size();
        }

        protected class OrderedItemsHolder extends RecyclerView.ViewHolder {

            private TextView textName, textBrand, textDesc, textQuantity;

            public OrderedItemsHolder(View view) {
                super(view);
                textName = (TextView) view.findViewById(R.id.text_name);
                textBrand = (TextView) view.findViewById(R.id.text_brand);
                textDesc = (TextView) view.findViewById(R.id.text_desc);
                textQuantity = (TextView) view.findViewById(R.id.text_quantity);
            }
        }
    }
}
