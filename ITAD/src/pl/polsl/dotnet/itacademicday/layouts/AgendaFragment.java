package pl.polsl.dotnet.itacademicday.layouts;

import pl.polsl.dotnet.itacademicday.R;
import pl.polsl.dotnet.itacademicday.models.Lecture;
import android.app.Activity;
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
public class AgendaFragment extends Fragment {
	/**
	 * The fragment argument representing the section number for this
	 * fragment.
	 */
	private static final String ARG_SECTION_NUMBER = "section_number";

	/**
	 * Returns a new instance of this fragment for the given section
	 * number.
	 */
	public static AgendaFragment newInstance(int sectionNumber){
		AgendaFragment fragment = new AgendaFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}

	public AgendaFragment() {
	}

	private LayoutInflater mInflater;
	private ListView mList;

	private class LectureViewTag {
		ImageView iconView;
		TextView nameView, lecturerView;
	}

	public class AgendaAdapter extends BaseAdapter {

		@Override
		public int getCount(){
			return 4;
		}

		@Override
		public Lecture getItem(int position){
			switch (position) {
				case 0:
					return new Lecture("Hyper-V", "Janko Walski", "8:00", "12:00",
							"Postawie wam maszyne linucha na windowsie");
			}
			return new Lecture("Wyklad" + position, "Henryk " + position, position + ":00", (position + 1) + ":00",
					"azzz");
		}

		@Override
		public long getItemId(int position){
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent){
			LectureViewTag t;
			if (convertView == null) {
				ViewGroup v = (ViewGroup) mInflater.inflate(R.layout.lecture, mList, false);
				t = new LectureViewTag();
				t.iconView = (ImageView) v.findViewById(R.id.icon);
				t.nameView = (TextView) v.findViewById(R.id.name);
				t.lecturerView = (TextView) v.findViewById(R.id.lecturer);
				v.setTag(t);
				convertView = v;
			} else {
				t = (LectureViewTag) convertView.getTag();
			}
			Lecture l = getItem(position);
			t.iconView.setImageBitmap(l.getIcon());
			t.nameView.setText(l.getName());
			t.lecturerView.setText(l.getLecturer());

			return convertView;
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		mInflater = inflater;
		View rootView = inflater.inflate(R.layout.agenda_fragment, container, false);
		mList = (ListView) rootView.findViewById(R.id.agenda_list);
		mList.setAdapter(new AgendaAdapter());
		return rootView;
	}

	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
		((MainActivity) activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
	}

	@Override
	public void onDetach(){
		super.onDetach();
	}
}