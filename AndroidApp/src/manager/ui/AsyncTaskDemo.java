package manager.ui;

import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;

/**
 * 
 * @author http://www.android-codes-examples.blogspot.com/
 *
 */

public class AsyncTaskDemo extends ListActivity {
	private static String[] items = { "Joseph", "George", "Mary", "Antony", "Albert",
			"Michel", "John", "Abraham", "Mark", "Savior", "Kristopher",
			"Thomas", "Williams", "Assisi", "Sebastian", "Aloysius", "Alex", "Daniel",
			"Anto", "Alexandar", "Brito", "Robert", "Jose",
			"Paul", "Peter" };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.manager_ui);

		setListAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_checked, new ArrayList()));

		new AddStringTask().execute();
	}

	class AddStringTask extends AsyncTask<Void, String, Void> {
		@Override
		protected Void doInBackground(Void... unused) {
			for (String item : items) {
				publishProgress(item);
				SystemClock.sleep(200);
			}

			return (null);
		}

		@Override
		protected void onProgressUpdate(String... item) {
			((ArrayAdapter) getListAdapter()).add(item[0]);
		}

		@Override
		protected void onPostExecute(Void unused) {
			setSelection(3);
			Toast.makeText(AsyncTaskDemo.this, "Done!", Toast.LENGTH_SHORT).show();
		}
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
	    CheckedTextView check = (CheckedTextView)v;
	    check.setChecked(!check.isChecked());
	}

	
}