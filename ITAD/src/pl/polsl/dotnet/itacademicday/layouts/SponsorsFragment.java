package pl.polsl.dotnet.itacademicday.layouts;

import java.util.ArrayList;

import pl.polsl.dotnet.itacademicday.R;
import pl.polsl.dotnet.itacademicday.layouts.MainActivity.FontStyle;
import pl.polsl.dotnet.itacademicday.models.Sponsor;
import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class SponsorsFragment extends Fragment {
	/**
	 * The fragment argument representing the section number for this
	 * fragment.
	 */
	private static final String ARG_SECTION_NUMBER = "section_number";

	/**
	 * Returns a new instance of this fragment for the given section
	 * number.
	 */
	public static SponsorsFragment newInstance(int sectionNumber){
		SponsorsFragment fragment = new SponsorsFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}

	public SponsorsFragment() {
	}

	private LayoutInflater mInflater;
	private ListView mList;
	private SponsorAdapter mAdapter;

	public class SponsorAdapter extends BaseAdapter {
		private ArrayList<Sponsor> mSponsorList;

		private class SponsorViewTag {
			ImageView iconView;
			TextView nameView, descriptionView;
		}

		public SponsorAdapter() {
			mSponsorList = new ArrayList<Sponsor>();
		}

		@Override
		public int getCount(){
			return mSponsorList.size();
		}

		@Override
		public Sponsor getItem(int position){
			return mSponsorList.get(position);
		}

		@Override
		public long getItemId(int position){
			return position;
		}

		public void add(Sponsor s){
			mSponsorList.add(s);
			notifyDataSetChanged();
		}

		public void clear(){
			mSponsorList.clear();
			notifyDataSetChanged();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent){
			SponsorViewTag t;
			if (convertView == null) {
				ViewGroup v = (ViewGroup) mInflater.inflate(R.layout.sponsor, mList, false);
				MainActivity.setFont(v, FontStyle.REGULAR);
				t = new SponsorViewTag();
				t.iconView = (ImageView) v.findViewById(R.id.icon);
				t.nameView = (TextView) v.findViewById(R.id.name);
				t.descriptionView = (TextView) v.findViewById(R.id.description);
				v.setTag(t);
				convertView = v;
			} else {
				t = (SponsorViewTag) convertView.getTag();
			}
			Sponsor l = getItem(position);
			t.iconView.setImageBitmap(l.getImage());
			t.nameView.setText(l.getName());
			t.descriptionView.setText(l.getDescription());

			return convertView;
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		mInflater = inflater;
		View rootView = inflater.inflate(R.layout.sponsors_fragment, container, false);
		MainActivity.setFont(rootView, FontStyle.REGULAR);
		mList = (ListView) rootView.findViewById(R.id.sponsors_list);
		mAdapter = new SponsorAdapter();
		mList.setAdapter(mAdapter);

		//TODO load in separate thread
		mAdapter.add(new Sponsor("abc def", "descr", BitmapFactory.decodeResource(getResources(),
				R.drawable.ic_launcher)));
		return rootView;
	}

	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
	}

	@Override
	public void onDetach(){
		super.onDetach();
	}
}