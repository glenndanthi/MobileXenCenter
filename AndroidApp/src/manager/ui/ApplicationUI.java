package manager.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import service.RestClient;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import models.ActionResponse;
import models.ExpandableListAdapter;
import models.Hosts;
import models.ServerCredentials;
import models.VM;
import models.VirtualMachine;

public class ApplicationUI extends Activity implements Runnable
{
    private ExpandableListAdapter adapter;
    ExpandableListView listView;
    Button btnAddVm;
    Button btnDeleteVm;
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
        
        adapter = new ExpandableListAdapter(this, new ArrayList<String>(),
                new ArrayList<ArrayList<VirtualMachine>>(),this);

        // Set this blank adapter to the list view
        listView.setAdapter(adapter);
        //ServerCredentials.setApplicationMode(1);
        String vmList="";
        // Get the List of Virtual Machines based on Application Mode
        if(ServerCredentials.getApplicationMode()==1){
        	// Demo Mode
        	vmList = " [{\"state\": \"Halted\", \"name\": \"Debian 1\", \"uid\": \"db54aecc-d499-69f8-f194-9af8d297081c\"}, {\"state\": " +
    		"\"Suspended\", \"name\": \"Debian 2\", \"uid\": \"594c306d-4aff-41fa-095f-b038d45a661f\"},{\"state\": \"Running\", \"name\": \"SUSE 32 bit\", \"uid\": \"db54aecc-d499-69f8-f194-9af8d297081c\"}, {\"state\": " +
    		"\"Stopped\", \"name\": \"Ubuntu Linux\", \"uid\": \"594c306d-4aff-41fa-095f-b038d45a6sasd\"}]";
        	        	
        }else{
        	// Web Service Mode
        	String url = ServerCredentials.GETVMLIST_URL; 
        	RestClient client = new RestClient(url);
    		
    		client.AddParam("address", ServerCredentials.getServerIp());
    		client.AddParam("username", ServerCredentials.getUsername());
    		client.AddParam("password", ServerCredentials.getPassword());
    	  	
    		try {
    			client.Execute(2);
    			vmList = client.getResponse();
    			
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
        }

        Log.v("VM String",vmList);
        
		JsonElement json = new JsonParser().parse(vmList);
		JsonArray array= json.getAsJsonArray();
		Iterator iterator = array.iterator();
				
		while(iterator.hasNext()){
		    JsonElement json2 = (JsonElement)iterator.next();
		    Gson gson = new Gson();
		    VirtualMachine vm = gson.fromJson(json2, VirtualMachine.class);
		    adapter.addItem(vm);
		    Log.v("Added VM",vm.getName()+"  "+vm.getUid());
		    handler.sendEmptyMessage(1);		    
		}
        
        listView.expandGroup(0);
       
     // Attaching Listeners to button Events
        btnAddVm = (Button)this.findViewById(R.id.newVM);
        btnDeleteVm = (Button)this.findViewById(R.id.delete);        
        btnShutdownVm = (Button)this.findViewById(R.id.shutDown);
        btnRebootVm = (Button)this.findViewById(R.id.reboot);
        btnLogout = (Button)this.findViewById(R.id.logout);
        
        
     // New VM action
        btnAddVm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Toast.makeText(getBaseContext(), "Not supported at the moment", Toast.LENGTH_LONG).show();
				Intent myIntent = new Intent(getApplicationContext(), AddVirtualMachine.class);
				myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				
		    	getApplicationContext().startActivity(myIntent);
		    	//finish();
			}
		});
        
        
        // Delete Virtual Machine Handler
        btnDeleteVm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String response="";
				if(ServerCredentials.getApplicationMode()==1){
		        	// Demo Mode
					
					String status = "";int i;
		        	for(i=0;i<ServerCredentials.selectedUID.size();i++){
		        		adapter.removeItem(ServerCredentials.selectedUID.get(i));
		        		response = "{\"status\": \"success\"}";
		        		JsonElement json = new JsonParser().parse(response);
						Gson gson = new Gson();
						ActionResponse res = gson.fromJson(json, ActionResponse.class);
						status+="Status for VM ID "+ServerCredentials.selectedUID.get(i)+res.getStatus();
		        	}
		        	
		        	for(i=0;i<ServerCredentials.selectedUID.size();i++){
		        		ServerCredentials.selectedUID.remove(i);
		        	
		        	}
		        	
		        	handler.sendEmptyMessage(1);
		        	Log.v("General Info Update Response","Status: "+status);
					Toast.makeText(getBaseContext(), status, Toast.LENGTH_LONG).show();
		        	        	
		        }else{
		        	// Web Service Mode
		        	String status = "";int i;
		        	for(i=0;i<ServerCredentials.selectedUID.size();i++){
			        	String urlGeneral = "http://10.211.17.13:8000/mobilexencenter/virtualmachines/"+ServerCredentials.selectedUID.get(i)+"/delete";
			        	Log.v("del URL", urlGeneral);
			        	RestClient client = new RestClient(urlGeneral);
			        	client.AddParam("address", ServerCredentials.getServerIp());
			    		client.AddParam("username", ServerCredentials.getUsername());
			    		client.AddParam("password", ServerCredentials.getPassword());
			    		
			    		try {
			    			client.Execute(2);
			    			response = client.getResponse();
			    			
			    		} catch (Exception e) {
			    			e.printStackTrace();
			    		}
			    		JsonElement json = new JsonParser().parse(response);
						Gson gson = new Gson();
						ActionResponse res = gson.fromJson(json, ActionResponse.class);
						status+="Status for VM ID "+ServerCredentials.selectedUID.get(i)+res.getStatus();
			        }
		        	for(i=0;i<ServerCredentials.selectedUID.size();i++){
		        		ServerCredentials.selectedUID.remove(i);
		        	}
		        	startActivity(getIntent()); finish();
		        	Log.v("General Info Update Response","Status: "+status);
					Toast.makeText(getBaseContext(), status, Toast.LENGTH_LONG).show();	
		        }
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
				//Toast.makeText(getBaseContext(), "Logout Action", Toast.LENGTH_LONG).show();
				finish();				
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
           // adapter.addItem(new VM("Virtual Machine " + count,count%3));
              
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