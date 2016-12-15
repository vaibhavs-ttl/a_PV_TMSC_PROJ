package com.ttl.customersocialapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ttl.adapter.ViewPagerAdapter;

public class GuideActivity extends Activity implements OnPageChangeListener {

	protected View view;
	
	private ViewPager info;
	private LinearLayout pager_indicator;
	private int dotsCount;
	private ImageView[] dots;
	private ViewPagerAdapter mAdapter;
	
	
	private Button btnstart;

	private int[] mImageResources = { R.drawable.wcalculator,
			R.drawable.wcomplaint, R.drawable.wdealer, R.drawable.wemergency,
			R.drawable.wfeedback };
	private String[] mTitle = {"Cost Calculator","Complaints","Dealer Locator","Emergency location","Feedback"};
	private String[] mDesc = {"Know your approximate service cost (Labour, Spares and Consumables) for your vehicle's scheduled services and book a service online using the app.",
			"You can register a complaint easily & conveniently with Tata Motors directly using the app and keep track of the same in the app.",
			"All our service network details are now available in the app with contact details of the dealerships. Use the map module to locate the Dealers/ Service Centers vis a vis your location.",
			"Share your location in case of emergency with our call center with the press of a button. All-important contactability details will be auto shared with the support team.",
			"Share your feedback instantly using the app. You can share your generic feedback (Free text) or Post Service Feedback with TML directly. Your feedback will be acted upon on priority." };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide);
		
		btnstart = (Button) findViewById(R.id.btnstart);
		btnstart.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent i = new Intent(GuideActivity.this, LoginActivity.class);
				startActivity(i);
				finish();
			}
		});
		
		Intent ii = new Intent(GuideActivity.this, TransActivity.class);
		startActivity(ii);	

	
		info = (ViewPager) findViewById(R.id.pager_introduction);
		pager_indicator = (LinearLayout) findViewById(R.id.viewPagerCountDots);

		mAdapter = new ViewPagerAdapter(GuideActivity.this, mImageResources,mTitle,mDesc);
		info.setAdapter(mAdapter);
		info.setCurrentItem(0);
		info.setOnPageChangeListener(this);

		/*
		 * Update = new Runnable() { public void run() {
		 * 
		 * // intro_images.setCurrentItem((intro_images.getCurrentItem() < //
		 * dotsCount) ? intro_images.getCurrentItem() + 1 : 0,true);
		 * 
		 * if (info.getCurrentItem() == dotsCount - 1) { info.setCurrentItem(0);
		 * } else info.setCurrentItem(info.getCurrentItem() + 1);
		 * 
		 * } };
		 * 
		 * swipeTimer = new Timer(); swipeTimer.schedule(new TimerTask() {
		 * 
		 * @Override public void run() { handler.post(Update); } }, 1000, 1000);
		 */
		setUiPageViewController();

	
	}

	private void setUiPageViewController() {

		dotsCount = mAdapter.getCount();
		dots = new ImageView[dotsCount];

		for (int i = 0; i < dotsCount; i++) {
			dots[i] = new ImageView(this);
			dots[i].setImageDrawable(getResources().getDrawable(
					R.drawable.nonselecteditem_dot));

			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);

			params.setMargins(4, 0, 4, 0);

			pager_indicator.addView(dots[i], params);
		}

		dots[0].setImageDrawable(getResources().getDrawable(
				R.drawable.selecteditem_dot));

	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		

	}

	@Override
	public void onPageSelected(int position) {
		for (int i = 0; i < dotsCount; i++) {
			dots[i].setImageDrawable(getResources().getDrawable(
					R.drawable.nonselecteditem_dot));
		}

		dots[position].setImageDrawable(getResources().getDrawable(
				R.drawable.selecteditem_dot));

	}

}
