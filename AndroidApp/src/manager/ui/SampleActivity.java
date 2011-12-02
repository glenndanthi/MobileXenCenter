package manager.ui;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import models.ExpandableListAdapter;
import models.VM;

public class SampleActivity extends Activity implements Runnable
{
    private ExpandableListAdapter adapter;
    ExpandableListView listView;
    Button btnAddVm;
    Button btnStartVm;
    Button btnShutdownVm;
    Button btnRebootVm;
    Button btnLogout;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manager_ui);

        // Retrive the ExpandableListView from the layout
        listView = (ExpandableListView) findViewById(R.id.listView);
        
        listView.setOnChildClickListener(new OnChildClickListener()
        {
            
            @Override
            public boolean onChildClick(ExpandableListView arg0, View arg1, int arg2, int arg3, long arg4)
            {
                Toast.makeText(getBaseContext(), "Child clicked", Toast.LENGTH_LONG).show();
                return false;
            }
        });
        
        listView.setOnGroupClickListener(new OnGroupClickListener()
        {
            
            @Override
            public boolean onGroupClick(ExpandableListView arg0, View arg1, int arg2, long arg3)
            {
                Toast.makeText(getBaseContext(), "Group clicked", Toast.LENGTH_LONG).show();
            	return false;
            }
        });

        // Initialize the adapter with blank groups and children
        // We will be adding children on a thread, and then update the ListView
       /* adapter = new ExpandableListAdapter(this, new ArrayList<String>(),
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
*/        listView.expandGroup(0);
        listView.expandGroup(1);
        listView.expandGroup(2);
        // This thread randomly generates some vehicle types
        // At an interval of every 2 seconds
        Thread thread = new Thread(this);
        thread.start();
     // Attaching Listeners to button Events
        btnAddVm = (Button)this.findViewById(R.id.newVM);
        btnStartVm = (Button)this.findViewById(R.id.start);        
        btnShutdownVm = (Button)this.findViewById(R.id.shutDown);
        btnRebootVm = (Button)this.findViewById(R.id.reboot);
        btnLogout = (Button)this.findViewById(R.id.logout);
        
        
     // New VM action
        btnAddVm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(getBaseContext(), "New VM Action", Toast.LENGTH_LONG).show();
			}
		});
        
        
        // Start Virtual Machine Handler
        btnStartVm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(getBaseContext(), "Start VM Action", Toast.LENGTH_LONG).show();
			}
		});
        
        
       // Shutdown Virtual Machine Handler
        btnShutdownVm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(getBaseContext(), "Shutdown VM Action", Toast.LENGTH_LONG).show();
			}
		});
        
        
       // Reboot Virtual Machine Handler
        btnRebootVm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(getBaseContext(), "Reboot VM Action", Toast.LENGTH_LONG).show();
			}
		});
        
        
     // Logout Virtual Machine Handler
        btnLogout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(getBaseContext(), "Logout Action", Toast.LENGTH_LONG).show();
			}
		});
        
        
    }

    @Override
    public void run()
    {
        final int ITEMS = 15;
        int count = 0;
        while (count != ITEMS)
        {
            count++;
       //     adapter.addItem(new VM("Virtual Machine " + count,count%3));
            
            // Notify the adapter
            handler.sendEmptyMessage(1);
            try
            {
                // Sleep for two seconds
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