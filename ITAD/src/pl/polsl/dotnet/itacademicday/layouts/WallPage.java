package pl.polsl.dotnet.itacademicday.layouts;

import java.util.ArrayList;

import pl.polsl.dotnet.itacademicday.R;
import pl.polsl.dotnet.itacademicday.core.signalr.SignalRClient;
import pl.polsl.dotnet.itacademicday.layouts.MainActivity.FontStyle;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

/**
 * A placeholder fragment containing a simple view.
 */
public class WallPage extends Page {

	public WallPage(Context c) {
		super(c);
	}

	private ListView mList;
	private ArrayAdapter<String> mAdapter;
	private final ArrayList<String> mMessages = new ArrayList<String>(); //TODO put messages from server here

	private String mCommentAwaitForSubmit, mServerError;

	private SignalRClient signalerClient = null;
	
	@Override
<<<<<<< HEAD:ITAD/src/pl/polsl/dotnet/itacademicday/layouts/WallPage.java
	protected void onCreate(){
		mCommentAwaitForSubmit = getResources().getString(R.string.await_for_comment_submit);
		mServerError = getResources().getString(R.string.server_error);
		final String connectionError = getResources().getString(R.string.connection_error);

		mList = (ListView) findViewById(R.id.wall_list);
		MainActivity.setFont(this, FontStyle.REGULAR);
		mMessages = new ArrayList<String>();
		mAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, mMessages);
=======
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		
		signalerClient = new SignalRClient(mMessages);
		
		mContext = container.getContext();
		mCommentAwaitForSubmit = getResources().getString(R.string.await_for_comment_submit);
		mServerError = getResources().getString(R.string.server_error);
		final String connectionError = getResources().getString(R.string.connection_error);
		
		View rootView = inflater.inflate(R.layout.wall_fragment, container, false);
		mList = (ListView) rootView.findViewById(R.id.wall_list);
		MainActivity.setFont(rootView, FontStyle.REGULAR);
		mAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, mMessages);
>>>>>>> 690375b671353b7345df928aea632d1c148fe81f:ITAD/src/pl/polsl/dotnet/itacademicday/layouts/WallFragment.java
		mList.setAdapter(mAdapter);
		final EditText commentEdit = (EditText) findViewById(R.id.commentEdit);
		final ImageButton commentSubmit = (ImageButton) findViewById(R.id.commentSubmit);
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
					Toast.makeText(getContext(), connectionError, Toast.LENGTH_LONG).show();
				}
			}
		});
<<<<<<< HEAD:ITAD/src/pl/polsl/dotnet/itacademicday/layouts/WallPage.java
=======
		
		return rootView;
>>>>>>> 690375b671353b7345df928aea632d1c148fe81f:ITAD/src/pl/polsl/dotnet/itacademicday/layouts/WallFragment.java
	}

	/** Return true if success - message sent to server correctly*/
	public boolean onSubmit(String comment){
		boolean success;

		//TODO replace "processing" with server communication code

		//"processing"
		
		signalerClient.sendMessage(comment);
		onRefresh();
		success = true;

		Toast.makeText(getContext(), success ? mCommentAwaitForSubmit : mServerError, Toast.LENGTH_LONG).show();
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
	protected int getLayoutResourceId(){
		return R.layout.wall_fragment;
	}

	@Override
	public boolean onBack(){
		((MainActivity) getContext()).setContentPage(new AboutPage(getContext()));
		return true;
	}
}