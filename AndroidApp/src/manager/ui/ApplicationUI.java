package manager.ui;

import java.util.ArrayList;
import java.util.List;

import models.ExpandableListAdapter;
import models.Model;
import models.VM;
import android.app.ListActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;

public class ApplicationUI extends ListActivity implements Runnable{
	private ExpandableListAdapter adapter;
    ExpandableListView listView;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manager_ui);

        // Retrive the ExpandableListView from the layout
        listView = (ExpandableListView) findViewById(R.id.listView);
        
        adapter = new ExpandableListAdapter(this, new ArrayList<String>(),
                new ArrayList<ArrayList<VM>>());

        // Set this blank adapter to the list view
        listView.setAdapter(adapter);
        
        final int ITEMS = 15;
        int count = 0;
        while (count != ITEMS)
        {
            count++;
            adapter.addItem(new VM("Virtual Machine " + count,count%3));
            // Notify the adapter
            handler.sendEmptyMessage(1);            
        }
        listView.expandGroup(0);
        listView.expandGroup(1);
        listView.expandGroup(2);
        // This thread randomly generates some vehicle types
        // At an interval of every 2 seconds
        Thread thread = new Thread(this);
        thread.start();
        
        
    }

    @Override
    public void run()
    {
        final int ITEMS = 15;
        int count = 0;
        while (count != ITEMS)
        {
            count++;
            adapter.addItem(new VM("Virtual Machine " + count,count%3));
            
            // Notify the adapter
            handler.sendEmptyMessage(1);
            try
            {
                // Sleep for two milliseconds
                Thread.sleep(200);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        
    }

    private Handler handler = new Handler()
    {

        @Override
        public void handleMessage(Message msg)
        {
            adapter.notifyDataSetChanged();
            super.handleMessage(msg);
        }

    };	
}
