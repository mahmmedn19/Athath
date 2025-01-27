package com.project.asas.ui.on_boarding;

import android.content.Intent;
import android.os.Bundle;

import com.codemybrainsout.onboarder.AhoyOnboarderActivity;
import com.codemybrainsout.onboarder.AhoyOnboarderCard;
import com.project.asas.MainActivity;
import com.project.asas.R;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class OnboardingActivity extends AhoyOnboarderActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        List<AhoyOnboarderCard> pages = new ArrayList<>();

        // Page 1: Vendor Features
        AhoyOnboarderCard page1 = new AhoyOnboarderCard(
                "Manage Vendors",
                "Admins can approve, reject, or block vendors. Vendors can add, update, or remove furniture products.",
                R.drawable.logo
        );
        page1.setBackgroundColor(R.color.black_transparent);

        // Page 2: AI Room Suggestions
        AhoyOnboarderCard page2 = new AhoyOnboarderCard(
                "AI Room Suggestions",
                "Customers can get AI-based furniture recommendations, tailored to their preferences like colors, plants, and flooring.",
                R.drawable.logo
        );
        page2.setBackgroundColor(R.color.black_transparent);

        // Page 3: Favorite Furniture
        AhoyOnboarderCard page3 = new AhoyOnboarderCard(
                "Manage Favorites",
                "Easily save, view, or remove your favorite products and access detailed product descriptions.",
                R.drawable.logo
        );
        page3.setBackgroundColor(R.color.black_transparent);

        pages.add(page1);
        pages.add(page2);
        pages.add(page3);

        for (AhoyOnboarderCard page : pages) {
            page.setTitleColor(R.color.white);
            page.setDescriptionColor(R.color.white);
        }

        // Set onboarding pages
        setOnboardPages(pages);

        // Set the background image for the onboarding
        setImageBackground(R.drawable.furniture1);

        // Customize finish button
        setFinishButtonTitle("Get Started");

        // Customize navigation controls (optional)
        showNavigationControls(true);
        setInactiveIndicatorColor(R.color.black);
        setActiveIndicatorColor(R.color.white);
    }

    @Override
    public void onFinishButtonPressed() {
        // navigate to the main activity
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}