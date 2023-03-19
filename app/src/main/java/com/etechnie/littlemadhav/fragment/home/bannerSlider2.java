package com.etechnie.littlemadhav.fragment.home;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.Transformers.BaseTransformer;
import com.etechnie.littlemadhav.R;
import com.etechnie.littlemadhav.fragment.Product_Description;
import com.etechnie.littlemadhav.fragment.Products;
import com.etechnie.littlemadhav.models.banner_model.BannerDetails;
import com.etechnie.littlemadhav.models.category_model.CategoryDetails;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;


public class bannerSlider2 extends Fragment implements BaseSliderView.OnSliderClickListener{

   View rootview;
    SliderLayout sliderLayout;
    List<BannerDetails> bannerImages = new ArrayList<>();
    List<CategoryDetails> allCategoriesList = new ArrayList<>();
    PagerIndicator pagerIndicator;
    FragmentManager fragmentManager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootview =inflater.inflate(R.layout.fragment_banner_slider2, container, false);
        sliderLayout = (SliderLayout) rootview.findViewById(R.id.slider_2);
        pagerIndicator = (PagerIndicator) rootview.findViewById(R.id.banner_slider_indicator_2);

        BannerDetails b2=new BannerDetails();
        b2.setImage("https://img.freepik.com/free-psd/fashion-store-banner-template_23-2148675200.jpg");

        b2.setTitle("Banner Image 2");
        bannerImages.add(b2);
        b2= new BannerDetails();
        b2.setImage("https://img.freepik.com/free-psd/fashion-banner-template_23-2148509060.jpg");

        b2.setTitle("Banner Image 3");
        bannerImages.add(b2);
        setupBannerSlider(bannerImages);



        return rootview;
    }

    private void setupBannerSlider(List<BannerDetails> bannerImages) {
        // Initialize new LinkedHashMap<ImageName, ImagePath>
        final LinkedHashMap<String, String> slider_covers = new LinkedHashMap<>();


        for (int i = 0; i < bannerImages.size(); i++) {
            // Get bannerDetails at given Position from bannerImages List
            BannerDetails bannerDetails = bannerImages.get(i);

            // Put Image's Name and URL to the HashMap slider_covers
            slider_covers.put
                    (
                            "Image" + bannerDetails.getTitle(),
                            bannerDetails.getImage()
                    );
        }


        for (String name : slider_covers.keySet()) {
            // Initialize DefaultSliderView
            final DefaultSliderView defaultSliderView = new DefaultSliderView(getContext());

            // Set Attributes(Name, Image, Type etc) to DefaultSliderView
            defaultSliderView
                    .description(name)
                    .image(slider_covers.get(name))
                    .setScaleType(BaseSliderView.ScaleType.CenterCrop)
                    .setOnSliderClickListener(this);


            // Add DefaultSliderView to the SliderLayout
            sliderLayout.addSlider(defaultSliderView);
        }

        // Set PresetTransformer type of the SliderLayout
        sliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);


        // Check if the size of Images in the Slider is less than 2
        if (slider_covers.size() < 2) {
            // Disable PagerTransformer
            sliderLayout.setPagerTransformer(false, new BaseTransformer() {
                @Override
                protected void onTransform(View view, float v) {
                }
            });

            // Hide Slider PagerIndicator
            sliderLayout.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Invisible);

        } else {
            // Set custom PagerIndicator to the SliderLayout
            sliderLayout.setCustomIndicator(pagerIndicator);
            // Make PagerIndicator Visible
            sliderLayout.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Visible);
        }


    }
    @Override
    public void onSliderClick(BaseSliderView slider_2) {
        int position = sliderLayout.getCurrentPosition();
        String url = bannerImages.get(position).getUrl();
        String type = bannerImages.get(position).getType();

        if (type.equalsIgnoreCase("product")) {
            if (!TextUtils.isEmpty(url)) {
                // Get Product Info
                Bundle bundle = new Bundle();
                bundle.putInt("itemID", Integer.parseInt(url));

                // Navigate to Product_Description of selected Product
                Fragment fragment = new Product_Description();
                fragment.setArguments(bundle);
                fragmentManager.beginTransaction()
                        .add(R.id.main_fragment, fragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .addToBackStack(getString(R.string.actionHome)).commit();
            }

        } else if (type.equalsIgnoreCase("category")) {
            if (!TextUtils.isEmpty(url)) {
                int categoryID = 0;
                String categoryName = "";

                for (int i = 0; i < allCategoriesList.size(); i++) {
                    if (url.equalsIgnoreCase(allCategoriesList.get(i).getId())) {
                        categoryName = allCategoriesList.get(i).getName();
                        categoryID = Integer.parseInt(allCategoriesList.get(i).getId());
                    }
                }

                // Get OrderProductCategory Info
                Bundle bundle = new Bundle();
                bundle.putInt("CategoryID", categoryID);
                bundle.putString("CategoryName", categoryName);

                // Navigate to Products Fragment
                Fragment fragment = new Products();
                fragment.setArguments(bundle);
                fragmentManager.beginTransaction()
                        .add(R.id.main_fragment, fragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .addToBackStack(getString(R.string.actionHome)).commit();


            } else if (type.equalsIgnoreCase("deals")) {
                Bundle bundle = new Bundle();
                bundle.putString("sortBy", "special");

                // Navigate to Products Fragment
                Fragment fragment = new Products();
                fragment.setArguments(bundle);
                fragmentManager.beginTransaction()
                        .add(R.id.main_fragment, fragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .addToBackStack(getString(R.string.actionHome)).commit();

            } else if (type.equalsIgnoreCase("top seller")) {
                Bundle bundle = new Bundle();
                bundle.putString("sortBy", "top seller");

                // Navigate to Products Fragment
                Fragment fragment = new Products();
                fragment.setArguments(bundle);
                fragmentManager.beginTransaction()
                        .add(R.id.main_fragment, fragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .addToBackStack(getString(R.string.actionHome)).commit();

            } else if (type.equalsIgnoreCase("most liked")) {
                Bundle bundle = new Bundle();
                bundle.putString("sortBy", "most liked");

                // Navigate to Products Fragment
                Fragment fragment = new Products();
                fragment.setArguments(bundle);
                fragmentManager.beginTransaction()
                        .add(R.id.main_fragment, fragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .addToBackStack(getString(R.string.actionHome)).commit();

            }


        }}
}