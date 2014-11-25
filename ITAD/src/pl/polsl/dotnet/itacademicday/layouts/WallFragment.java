package pl.polsl.dotnet.itacademicday.layouts;

import java.util.ArrayList;

import pl.polsl.dotnet.itacademicday.R;
import pl.polsl.dotnet.itacademicday.layouts.MainActivity.FontStyle;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

/**
 * A placeholder fragment containing a simple view.
 */
public class WallFragment extends Fragment {
	/**
	 * The fragment argument representing the section number for this
	 * fragment.
	 */
	private static final String ARG_SECTION_NUMBER = "section_number";

	/**
	 * Returns a new instance of this fragment for the given section
	 * number.
	 */
	public static WallFragment newInstance(int sectionNumber){
		WallFragment fragment = new WallFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}

	public WallFragment() {
	}

	private ListView mList;
	private ArrayAdapter<String> mAdapter;
	private ArrayList<String> mMessages; //TODO put messages from server here

	private Context mContext;
	private String mCommentAwaitForSubmit, mServerError;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		mContext = container.getContext();
		mCommentAwaitForSubmit = getResources().getString(R.string.await_for_comment_submit);
		mServerError = getResources().getString(R.string.server_error);
		final String connectionError = getResources().getString(R.string.connection_error);

		View rootView = inflater.inflate(R.layout.wall_fragment, container, false);
		mList = (ListView) rootView.findViewById(R.id.wall_list);
		MainActivity.setFont(rootView, FontStyle.REGULAR);
		mMessages = new ArrayList<String>();
		mAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, mMessages);
		mList.setAdapter(mAdapter);
		final EditText commentEdit = (EditText) rootView.findViewById(R.id.commentEdit);
		final ImageButton commentSubmit = (ImageButton) rootView.findViewById(R.id.commentSubmit);
		commentEdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count){
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after){
			}

			@Override
			public void afterTextChanged(Editable s){
				commentSubmit.setEnabled(s.length() > 2);
			}
		});
		commentSubmit.setEnabled(false);
		commentSubmit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v){
				if (MainActivity.hasAccessToNetwork()) {
					String t = commentEdit.getText().toString();
					if (onSubmit(t)) {
						commentEdit.setText("");
					}
				} else {
					Toast.makeText(mContext, connectionError, Toast.LENGTH_LONG).show();
				}
			}
		});
		return rootView;
	}

	/** Return true if success - message sent to server correctly*/
	public boolean onSubmit(String comment){
		boolean success;

		//TODO replace "processing" with server communication code

		//"processing"
		mMessages.add(comment);
		onRefresh();
		success = true;

		Toast.makeText(mContext, success ? mCommentAwaitForSubmit : mServerError, Toast.LENGTH_LONG).show();
		return success;
	}

	/**Called after new messages were received. Scrolls the list to bottom*/
	public void onRefresh(){
		mList.post(new Runnable() {
			@Override
			public void run(){
				mList.setSelection(mAdapter.getCount() - 1);
			}
		});
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