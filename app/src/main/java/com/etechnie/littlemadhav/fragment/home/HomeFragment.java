package com.etechnie.littlemadhav.fragment.home;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.etechnie.littlemadhav.R;
import com.etechnie.littlemadhav.app.App;
import com.etechnie.littlemadhav.constant.ConstantValues;
import com.etechnie.littlemadhav.fragment.Categories_1;
import com.etechnie.littlemadhav.fragment.HomePage_9;
import com.etechnie.littlemadhav.models.banner_model.BannerDetails;
import com.etechnie.littlemadhav.models.category_model.CategoryDetails;
import com.etechnie.littlemadhav.network.StartAppRequests;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import am.appwise.components.ni.NoInternetDialog;


public class HomeFragment extends Fragment {
    HorizontalCategoryFragment _horizontalCategory;
    MenAndWomenFragment _mandw;
    Brand_rv _brandFragment;
    deals_home_rv _dealFragment;
    topPicks_home_rv _topPicksFragment;
    topOfferCategoriesFragment _topOfferCategoriesFragment;
    home_gif __gif;
    FragmentManager fragmentManager;
    BannerSlider bannerSlider;
    bannerSlider2 bannerSlider2_;


    View rootView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.homepage, container, false);
        fragmentManager = getFragmentManager();
        _mandw = new MenAndWomenFragment();
        _horizontalCategory = new HorizontalCategoryFragment();
        bannerSlider = new BannerSlider();
        bannerSlider2_= new bannerSlider2();
        _brandFragment = new Brand_rv();
        _topOfferCategoriesFragment = new topOfferCategoriesFragment();
        _dealFragment = new deals_home_rv();
        _topPicksFragment = new topPicks_home_rv();
        __gif = new home_gif();



        fragmentManager.beginTransaction().replace(R.id.category_fragment, _horizontalCategory).commit();
        fragmentManager.beginTransaction().replace(R.id.bannerslider1_fragment, bannerSlider).commit();
        fragmentManager.beginTransaction().replace(R.id.bannerslider1_fragment_2, bannerSlider2_).commit();
        fragmentManager.beginTransaction().replace(R.id.menandwo, _mandw).commit();

        fragmentManager.beginTransaction().replace(R.id.brand_rvfrag, _brandFragment).commit();
        fragmentManager.beginTransaction().replace(R.id.top_offer_category_rvfrag, _topOfferCategoriesFragment).commit();
        fragmentManager.beginTransaction().replace(R.id.deals_rvfrag, _dealFragment).commit();
        fragmentManager.beginTransaction().replace(R.id.topPicks_rvfrag, _topPicksFragment).commit();
        fragmentManager.beginTransaction().replace(R.id.gifButton,__gif).commit();


        // Inflate the layout for this fragment
        return rootView;
    }
    

}