package com.asktun.mg.activity;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalActivity;
import uk.co.senab.photoview.PhotoView;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

import com.androidquery.AQuery;
import com.asktun.mg.BaseActivity;
import com.asktun.mg.R;
import com.asktun.mg.view.HackyViewPager;

public class ImageBrowser2Activity extends BaseActivity {

	private SamplePagerAdapter pagerAdapter;
	private HackyViewPager mViewPager;
	private AQuery aq;
	private List<String> list = new ArrayList<String>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initActionBar();
		setAbContentView(R.layout.activity_image_browser);
		FinalActivity.initInjectedView(this);
		setLogo(R.drawable.button_back_selector);
		// setTitleText("Ìí¼Ó");
		init();
	}

	private void init() {
		Intent intent = getIntent();
		int position = intent.getIntExtra("position", 0);
		List<String> temlist = intent.getStringArrayListExtra("list");
		if (temlist != null) {
			list.addAll(temlist);
		}
		aq = new AQuery(this);
		mViewPager = new HackyViewPager(this);
		LinearLayout view = (LinearLayout) findViewById(R.id.vp_contain);
		view.addView(mViewPager);
		if (list != null) {
			int num = position + 1;
			final int size = list.size();
			setTitleText(num + "/" + size);
			pagerAdapter = new SamplePagerAdapter(list);
			mViewPager.setAdapter(pagerAdapter);
			mViewPager.setCurrentItem(position);
			mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
				
				@Override
				public void onPageSelected(int arg0) {
					// TODO Auto-generated method stub
					setTitleText((arg0+1) + "/" + size);
				}
				
				@Override
				public void onPageScrolled(int arg0, float arg1, int arg2) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onPageScrollStateChanged(int arg0) {
					// TODO Auto-generated method stub
					
				}
			});
		}
	}

	class SamplePagerAdapter extends PagerAdapter {
		private List<String> list = new ArrayList<String>();

		public SamplePagerAdapter(List<String> list) {
			super();
			this.list = list;
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public View instantiateItem(ViewGroup container, int position) {
			String photo = list.get(position);
			PhotoView photoView = new PhotoView(container.getContext());
			// photoView.setImageResource(sDrawables[position]);
			aq.id(photoView).progress(R.id.progress)
					.image(photo, false, true, 0, R.drawable.image_no);

			// Now just add PhotoView to ViewPager and return it
			container.addView(photoView, LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT);

			return photoView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

	}
}
