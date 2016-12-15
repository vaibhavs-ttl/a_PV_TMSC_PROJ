package com.ttl.customersocialapp;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class WebsiteBrowser extends Fragment implements OnClickListener{

	
	
	private WebView browser;
	private String url;
	private ProgressBar progress_bar;
	private View view;
	private ImageView back_btn;
	private ImageView forward_btn;
	private TextView productNameWebView;
	@Override
	public void onStart() {

		super.onStart();
	}
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

			view=inflater.inflate(R.layout.url_browser, container, false);
		
			productNameWebView= (TextView) view.findViewById(R.id.webViewProductName);
					
			getReferences();
			setHandlers();
			Bundle bundle=getArguments();
		
			Toast.makeText(getActivity(),"You’ll be browsing the data in a new page. Do note, the app session will expire if you do not navigate back to the home page within 30 mins.", Toast.LENGTH_SHORT).show();
			
			url=bundle.getString("website");	
			productNameWebView.setText(bundle.getString("productNameKey"));
	
			browser.setWebViewClient(new ProgressBarClient());
			browser.loadUrl(url);
			browser.getSettings().setJavaScriptEnabled(true);			
			browser.canGoBack();
			browser.canGoForward();
			
			
			view.getRootView().setFocusableInTouchMode(true);
			view.getRootView().requestFocus();
			
			view.getRootView().setOnKeyListener(new OnKeyListener() {
			    @Override
			    public boolean onKey(View v, int keyCode, KeyEvent event) {
			     if (event.getAction() == KeyEvent.ACTION_DOWN) {
			      if (keyCode == KeyEvent.KEYCODE_BACK) {      
			    	  FragmentManager fm = getActivity().getFragmentManager();
			    	    fm.popBackStack();
			       return true;
			      }
			     }
			     return false;
			    }
			   });
			
			
			
			
			
			
			
			
			
			
			
			return view;
	}

	
	
	private void setHandlers()
	{
		back_btn.setOnClickListener(this);
		forward_btn.setOnClickListener(this);
		
	}
	
	private void getReferences()
	{
		browser=(WebView)view.findViewById(R.id.browser);
		progress_bar=(ProgressBar)view.findViewById(R.id.progress);
		back_btn=(ImageView)view.findViewById(R.id.backward_btn);
		forward_btn=(ImageView)view.findViewById(R.id.forward_btn);
		
	}
	
	
	class ProgressBarClient extends WebViewClient
	{
		
		
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
	
			
			view.loadUrl(url);
			
			return true;
		}
		
	
		@Override
		public void onPageFinished(WebView view, String url) {
		
			progress_bar.setVisibility(View.INVISIBLE);
			
		}
		
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
		
			progress_bar.setVisibility(View.VISIBLE);
		}
		
	}


	@Override
	public void onClick(View v) {
		
		
		if (v.getId()==R.id.forward_btn) {
	
			
			if (browser.canGoForward()) {
				
				browser.goForward();
				
			}
			
			
			
		}
		else if(v.getId()==R.id.backward_btn)
		{

			if (browser.canGoBack()) {
				
				browser.goBack();
				
			}
		}
	
	
	
	
	
	
	
	
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
