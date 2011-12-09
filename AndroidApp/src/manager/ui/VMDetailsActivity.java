package manager.ui;
import java.util.Iterator;

import service.RestClient;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import models.ActionResponse;
import models.GeneralInfo;
import models.MemoryInfo;
import models.ServerCredentials;
import models.VM;
import models.VirtualMachine;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class VMDetailsActivity extends Activity{
	Button btnStart;
    Button btnStop;
    Button btnResume;
    Button btnSuspend;
    Button btnPause;
    Button btnUnPause;
    Button btnUpdateVmDetails;
    Button btnUpdateMemory;
    Button btnBack;
    EditText txtVmName;
    EditText txtVmDesc;
    EditText txtCpuCount;
    EditText txtMemory;
    EditText txtStaticMin;
    EditText txtStaticMax;
    EditText txtDynamicMin;
    EditText txtDymanicMax;
    
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.vmdetails);	
		
		
		// Initialize Buttons
		btnStart = (Button)this.findViewById(R.id.start);
	    btnStop  = (Button)this.findViewById(R.id.stop);
	    btnResume= (Button)this.findViewById(R.id.resume);
	    btnSuspend= (Button)this.findViewById(R.id.suspend);
	    btnPause  = (Button)this.findViewById(R.id.pause);
	    btnUnPause= (Button)this.findViewById(R.id.unpause);
	    btnUpdateVmDetails= (Button)this.findViewById(R.id.buttonUpdateGeneral);
	    btnUpdateMemory= (Button)this.findViewById(R.id.buttonUpdateMemory);
	    btnBack = (Button)this.findViewById(R.id.buttonBack);
	    
	    //Initialize EditText
	    txtVmName = (EditText)this.findViewById(R.id.editTextVmName);
	    txtVmDesc = (EditText)this.findViewById(R.id.editTextVmDesc);
	    txtCpuCount = (EditText)this.findViewById(R.id.editTextCpuCount);
	    txtMemory = (EditText)this.findViewById(R.id.editTextMemory);
	    txtStaticMin = (EditText)this.findViewById(R.id.editTextStaticMin);
	    txtStaticMax = (EditText)this.findViewById(R.id.editTextStaticMax);
	    txtDynamicMin = (EditText)this.findViewById(R.id.editTextDynamicMin);
	    txtDymanicMax = (EditText)this.findViewById(R.id.editTextDynamicMax);
	    	    
	    // Call Web Service and Initialize UI
	    GeneralInfo g = intializeGeneralUI();
	    MemoryInfo m = intializeMemoryUI();
	    
	    txtVmName.setText(g.getName());
	    txtVmDesc.setText(g.getDescription());
	    txtCpuCount.setText(g.getVcpuCount());
	    txtMemory.setText(g.getMemory());
	    txtStaticMin.setText(m.getStaticMin());
	    txtStaticMax.setText(m.getStaticMax());
	    txtDynamicMin.setText(m.getDynamicMin());
	    txtDymanicMax.setText(m.getDynamicMax());
	    
	    // Start VM action
	    btnStart.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String response="";
				if(ServerCredentials.getApplicationMode()==1){
		        	// Demo Mode
					response = "{\"status\": \"failure\", \"errorInfo\": \"VM_BAD_POWER_STATE\"}";
		        	        	
		        }else{
		        	// Web Service Mode
		        	String urlGeneral = "http://10.211.17.13:8000/mobilexencenter/virtualmachines/"+ServerCredentials.getSelectedVMId()+"/start";
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
		        }
				Log.v("st respnse",response);
				JsonElement json = new JsonParser().parse(response);
				Gson gson = new Gson();
				ActionResponse res = gson.fromJson(json, ActionResponse.class);
				Log.v("Start Action Response",""+res.getStatus()+"  "+res.getErrorInfo());
				Toast.makeText(getBaseContext(), "Status: "+res.getStatus()+" Error Info: "+res.getErrorInfo(), Toast.LENGTH_LONG).show();
			}
		});
	    
	 // Stop VM action
	    btnStop.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String response="";
				if(ServerCredentials.getApplicationMode()==1){
		        	// Demo Mode
					response = "{\"status\": \"failure\", \"errorInfo\": \"VM_BAD_POWER_STATE\"}";
		        	        	
		        }else{
		        	// Web Service Mode
		        	String urlGeneral = "http://10.211.17.13:8000/mobilexencenter/virtualmachines/"+ServerCredentials.getSelectedVMId()+"/stop";
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
		        }

				JsonElement json = new JsonParser().parse(response);
				Gson gson = new Gson();
				ActionResponse res = gson.fromJson(json, ActionResponse.class);
				Log.v("Start Action Response","Status: "+res.getStatus()+"  Info: "+res.getErrorInfo());
				Toast.makeText(getBaseContext(), "Status: "+res.getStatus()+" Error Info: "+res.getErrorInfo(), Toast.LENGTH_LONG).show();
			}
		});
	    
	 // Resume VM action
	    btnResume.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String response="";
				if(ServerCredentials.getApplicationMode()==1){
		        	// Demo Mode
					response = "{\"status\": \"failure\", \"errorInfo\": \"VM_BAD_POWER_STATE\"}";
		        	        	
		        }else{
		        	// Web Service Mode
		        	String urlGeneral = "http://10.211.17.13:8000/mobilexencenter/virtualmachines/"+ServerCredentials.getSelectedVMId()+"/resume";
		        	Log.v("URL", ""+urlGeneral);
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
		        }

				JsonElement json = new JsonParser().parse(response);
				Gson gson = new Gson();
				ActionResponse res = gson.fromJson(json, ActionResponse.class);
				Log.v("Start Action Response","Status: "+res.getStatus()+"  Info: "+res.getErrorInfo());
				Toast.makeText(getBaseContext(), "Status: "+res.getStatus()+" Error Info: "+res.getErrorInfo(), Toast.LENGTH_LONG).show();
			}
		});
	    
	 // Suspend VM action
	    btnSuspend.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String response="";
				if(ServerCredentials.getApplicationMode()==1){
		        	// Demo Mode
					response = "{\"status\": \"failure\", \"errorInfo\": \"VM_BAD_POWER_STATE\"}";
		        	        	
		        }else{
		        	// Web Service Mode
		        	String urlGeneral = "http://10.211.17.13:8000/mobilexencenter/virtualmachines/"+ServerCredentials.getSelectedVMId()+"/suspend";
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
		        }

				JsonElement json = new JsonParser().parse(response);
				Gson gson = new Gson();
				ActionResponse res = gson.fromJson(json, ActionResponse.class);
				Log.v("Start Action Response","Status: "+res.getStatus()+"  Info: "+res.getErrorInfo());
				Toast.makeText(getBaseContext(), "Status: "+res.getStatus()+" Error Info: "+res.getErrorInfo(), Toast.LENGTH_LONG).show();
			}
		});
	    
	 // Pause VM action
	    btnPause.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String response="";
				if(ServerCredentials.getApplicationMode()==1){
		        	// Demo Mode
					response = "{\"status\": \"failure\", \"errorInfo\": \"VM_BAD_POWER_STATE\"}";
		        	        	
		        }else{
		        	// Web Service Mode
		        	String urlGeneral = "http://10.211.17.13:8000/mobilexencenter/virtualmachines/"+ServerCredentials.getSelectedVMId()+"/pause";
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
		        }

				JsonElement json = new JsonParser().parse(response);
				Gson gson = new Gson();
				ActionResponse res = gson.fromJson(json, ActionResponse.class);
				Log.v("Start Action Response","Status: "+res.getStatus()+"  Info: "+res.getErrorInfo());
				Toast.makeText(getBaseContext(), "Status: "+res.getStatus()+" Error Info: "+res.getErrorInfo(), Toast.LENGTH_LONG).show();
			}
		});
	    
	 // UnPause VM action
	    btnUnPause.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String response="";
				if(ServerCredentials.getApplicationMode()==1){
		        	// Demo Mode
					response = "{\"status\": \"failure\", \"errorInfo\": \"VM_BAD_POWER_STATE\"}";
		        	        	
		        }else{
		        	// Web Service Mode
		        	String urlGeneral = "http://10.211.17.13:8000/mobilexencenter/virtualmachines/"+ServerCredentials.getSelectedVMId()+"/unpause";
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
		        }

				JsonElement json = new JsonParser().parse(response);
				Gson gson = new Gson();
				ActionResponse res = gson.fromJson(json, ActionResponse.class);
				Log.v("Start Action Response","Status: "+res.getStatus()+"  Info: "+res.getErrorInfo());
				Toast.makeText(getBaseContext(), "Status: "+res.getStatus()+" Error Info: "+res.getErrorInfo(), Toast.LENGTH_LONG).show();
			}
		});
	    
	 // Update VM Details action
	    btnUpdateVmDetails.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String response="";
				if(ServerCredentials.getApplicationMode()==1){
		        	// Demo Mode
					response = "{\"status\": \"success\"}";
		        	        	
		        }else{
		        	// Web Service Mode
		        	String urlGeneral = "http://10.211.17.13:8000/mobilexencenter/virtualmachines/"+ServerCredentials.getSelectedVMId()+"/generalInfo/update";
		        	RestClient client = new RestClient(urlGeneral);
		        	client.AddParam("address", ServerCredentials.getServerIp());
		    		client.AddParam("username", ServerCredentials.getUsername());
		    		client.AddParam("password", ServerCredentials.getPassword());
		    		client.AddParam("name", txtVmName.getText().toString());
		    		client.AddParam("description", txtVmDesc.getText().toString());
		    		
		    		try {
		    			client.Execute(2);
		    			response = client.getResponse();
		    			
		    		} catch (Exception e) {
		    			e.printStackTrace();
		    		}
		        }

				JsonElement json = new JsonParser().parse(response);
				Gson gson = new Gson();
				ActionResponse res = gson.fromJson(json, ActionResponse.class);
				Log.v("General Info Update Response","Status: "+res.getStatus()+"  Info: "+res.getErrorInfo());
				Toast.makeText(getBaseContext(), "Status: "+res.getStatus(), Toast.LENGTH_LONG).show();
			}
		});
	    
	 // Update Memory action
	    btnUpdateMemory.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String response="";
				if(ServerCredentials.getApplicationMode()==1){
		        	// Demo Mode
					response = "{\"status\": \"success\"}";
		        	        	
		        }else{
		        	// Web Service Mode
		        	String urlGeneral = "http://10.211.17.13:8000/mobilexencenter/virtualmachines/"+ServerCredentials.getSelectedVMId()+"/memory/update";
		        	RestClient client = new RestClient(urlGeneral);
		        	client.AddParam("address", ServerCredentials.getServerIp());
		    		client.AddParam("username", ServerCredentials.getUsername());
		    		client.AddParam("password", ServerCredentials.getPassword());
		    		client.AddParam("staticMin", txtStaticMin.getText().toString());
		    		client.AddParam("staticMax", txtStaticMax.getText().toString());
		    		client.AddParam("dynamicMin", txtDynamicMin.getText().toString());
		    		client.AddParam("dynamicMax", txtDymanicMax.getText().toString());
		    	
		    	  	
		    		try {
		    			client.Execute(2);
		    			response = client.getResponse();
		    			
		    		} catch (Exception e) {
		    			e.printStackTrace();
		    		}
		        }

				JsonElement json = new JsonParser().parse(response);
				Gson gson = new Gson();
				ActionResponse res = gson.fromJson(json, ActionResponse.class);
				Log.v("Start Action Response","Status: "+res.getStatus()+"  Info: "+res.getErrorInfo());
				Toast.makeText(getBaseContext(), "Status: "+res.getStatus(), Toast.LENGTH_LONG).show();
			}
		});
	    
	 // Update Memory action
	    btnBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent myIntent = new Intent(getApplicationContext(), ApplicationUI.class);
				myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				getApplicationContext().startActivity(myIntent);
				finish();
			}
		});
	    
	}
	
	
	private GeneralInfo intializeGeneralUI(){
		String general="";
		GeneralInfo gInfo = new GeneralInfo();
        // Get the List of Virtual Machines based on Application Mode
        if(ServerCredentials.getApplicationMode()==1){
        	// Demo Mode
        	general = "{\"vcpuCount\": \"1\", \"memory\": \"67108864\", \"name\": \"Debian 2\", \"description\": \"Test Desc\"}";
        	        	
        }else{
        	// Web Service Mode
        	String urlGeneral = "http://10.211.17.13:8000/mobilexencenter/virtualmachines/"+ServerCredentials.getSelectedVMId()+"/generalInfo";
        	Log.v("URL General",urlGeneral);
        	RestClient client = new RestClient(urlGeneral);
        	client.AddParam("address", "10.211.17.14");
    		client.AddParam("username", "root");
    		client.AddParam("password", "arizona");
    	
    	  	
    		try {
    			client.Execute(2);
    			general = client.getResponse();
    			Log.v("Str Return",general);
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
        }

        //Log.v("VM String",vmList);
        
		JsonElement json = new JsonParser().parse(general);
		Gson gson = new Gson();
		gInfo = gson.fromJson(json, GeneralInfo.class);
		Log.v("JSON1",""+gInfo.getDescription()+"  "+gInfo.getVcpuCount()+"   "+gInfo.getName());
		
		return gInfo;
	}
	
	private MemoryInfo intializeMemoryUI(){
		MemoryInfo mInfo = new MemoryInfo();
		String memory="";
        // Get the List of Virtual Machines based on Application Mode
        if(ServerCredentials.getApplicationMode()==1){
        	// Demo Mode
        	memory =  "{\"staticMax\": 65, \"dynamicMin\": 65, \"dynamicMax\": 65, \"staticMin\": 16}";
        	        	
        }else{
        	// Web Service Mode
        	String urlMemory = "http://10.211.17.13:8000/mobilexencenter/virtualmachines/"+ServerCredentials.getSelectedVMId()+"/memory";
        	//Log.v("URL", ""+urlGeneral);
        	RestClient client2 = new RestClient(urlMemory);
    		
    		client2.AddParam("address", ServerCredentials.getServerIp());
    		client2.AddParam("username", ServerCredentials.getUsername());
    		client2.AddParam("password", ServerCredentials.getPassword());
    	  	
    		try {
    			client2.Execute(2);
    			memory = client2.getResponse();
    			
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
        }
        
		JsonElement json = new JsonParser().parse(memory);
		Gson gson = new Gson();
		mInfo = gson.fromJson(json, MemoryInfo.class);
		Log.v("JSON2",mInfo.getStaticMin()+"   "+mInfo.getStaticMax());
		
		return mInfo;
	}
}