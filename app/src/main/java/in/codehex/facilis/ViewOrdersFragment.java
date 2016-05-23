package in.codehex.facilis;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.codehex.facilis.app.Config;


/**
 * A fragment that is used to display orders list in the {@link SellerActivity} class.
 */
public class ViewOrdersFragment extends Fragment {

    TabLayout mTabLayout;

    public ViewOrdersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_orders, container, false);

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
        mTabLayout = (TabLayout) view.findViewById(R.id.layout_tab);
    }

    /**
     * Implement and manipulate the objects.
     */
    private void prepareObjects() {
        for (int i = 0; i < Config.TAB_TITLE.length; i++)
            mTabLayout.addTab(mTabLayout.newTab().setText(Config.TAB_TITLE[i]));

        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                setCurrentTab(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        setCurrentTab(0);
    }

    /**
     * Set the layout to the selected tab.
     *
     * @param position the position of the tab.
     */
    private void setCurrentTab(int position) {
        Fragment fragment;
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        switch (position) {
            case 0:
                fragment = new AllDealsFragment();
                fragmentTransaction.replace(R.id.layout_container, fragment);
                fragmentTransaction.commit();
                break;
            case 1:
                fragment = new HotDealsFragment();
                fragmentTransaction.replace(R.id.layout_container, fragment);
                fragmentTransaction.commit();
                break;
            case 2:
                fragment = new LastMinuteFragment();
                fragmentTransaction.replace(R.id.layout_container, fragment);
                fragmentTransaction.commit();
                break;
        }
    }
}
