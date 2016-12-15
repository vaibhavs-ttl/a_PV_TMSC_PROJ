package com.ttl.adapter;

import com.ttl.customersocialapp.R;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ViewPagerAdapter  extends PagerAdapter {
 
    private Context mContext;
    private int[] mResources;
    private String[] mTitle;
    private String[] mDesc;
 
    public ViewPagerAdapter(Context mContext, int[] mResources, String[] mTitle,String[] mDesc) {
        this.mContext = mContext;
        this.mResources = mResources;
        this.mTitle=mTitle;
        this.mDesc=mDesc;
    }

    
	@Override
	public int getCount() {
		
		return mResources.length;
	}

	
	@Override
    public Object instantiateItem(ViewGroup container, int position) {
		
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.guide_pageritem, container, false);
 
        ImageView imageView = (ImageView) itemView.findViewById(R.id.logo);
        imageView.setImageResource(mResources[position]);
        TextView txttitle=(TextView)itemView.findViewById(R.id.titletxt);
        txttitle.setText(mTitle[position]);
        TextView txtDesc=(TextView)itemView.findViewById(R.id.dectxt);
        txtDesc.setText(mDesc[position]);
        container.addView(itemView);
 
        return itemView;
    }
	
	 @Override
	    public void destroyItem(ViewGroup container, int position, Object object) {
	        container.removeView((LinearLayout) object);
	    }
	    
		@Override
		public boolean isViewFromObject(View view, Object object) {
			  return view == ((LinearLayout) object);
		}
}
