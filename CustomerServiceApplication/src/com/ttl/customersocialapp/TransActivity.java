package com.ttl.customersocialapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TransActivity extends Activity {

	RelativeLayout mainrel;
	TextView txt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trans);

		mainrel = (RelativeLayout) findViewById(R.id.mainrel);

		mainrel.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub

				if (event.getAction() == MotionEvent.ACTION_DOWN)

					TransActivity.this.finish();

				return true;
			}

		});
		
		/*txt = (TextView)findViewById(R.id.txt);
		Animation animSlide = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide);
		txt.startAnimation(animSlide);*/
		
	}

}
