package pl.polsl.dotnet.itacademicday.layouts;

import java.util.ArrayList;

import pl.polsl.dotnet.itacademicday.R;
import pl.polsl.dotnet.itacademicday.core.signalr.SignalRClient;
import pl.polsl.dotnet.itacademicday.core.signalr.SignalRClient.OnMessage;
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
	private ArrayList<String> mMessages = new ArrayList<String>();

	private String mCommentAwaitForSubmit, mServerError;

	private SignalRClient mSignalerClient;

	@Override
	protected void onCreate(){

		mCommentAwaitForSubmit = getResources().getString(R.string.await_for_comment_submit);
		mServerError = getResources().getString(R.string.server_error);
		final String connectionError = getResources().getString(R.string.connection_error);

		mList = (ListView) findViewById(R.id.wall_list);
		MainActivity.setFont(this, FontStyle.SEMILIGHT);
		MainActivity.setFont(findViewById(R.id.subtitle), FontStyle.LIGHT);

		mMessages = new ArrayList<String>();
		mAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, mMessages);

		mSignalerClient = new SignalRClient(new OnMessage() {

			@Override
			public void onMessage(String message){
				mMessages.add(message);
				mAdapter.notifyDataSetChanged();
			}
		});

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

		//TODO dodawanie do listy
		mMessages.add("dupa");
		mAdapter.notifyDataSetChanged();
	}

	/** Return true if success - message sent to server correctly*/
	public boolean onSubmit(String comment){
		boolean success;
		try {
			mSignalerClient.sendMessage(comment);
			success = true;
		} catch (Exception e) {
			success = false;
		}
		onRefresh();

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