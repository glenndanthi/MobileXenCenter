package manager.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import service.RestClient;
import models.Hosts;
import models.ServerCredentials;
import models.VirtualMachine;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class XenmanagerActivity extends Activity {
	
	final int DEMO = 1;
	final int LIVE = 2;
	
	int applicationMode = LIVE; // current mode
	String errorMsg = "";
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
    	// Setting Application Mode
    	ServerCredentials.setApplicationMode(applicationMode);
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);	
        
        // Getting button objects
        btnLogin = (Button)this.findViewById(R.id.login);
        btnCancel = (Button)this.findViewById(R.id.cancel);
        
        // Setting Listener on LoginButton
        btnLogin.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Reading Text Field Values
				
		        txtUserName = (EditText)findViewById(R.id.entryUser);
		        txtPassword = (EditText)findViewById(R.id.entryPass);
		        txtServerAddress = (EditText)findViewById(R.id.entryIP);
		        String userName = txtUserName.getText().toString();
		        String pass = txtPassword.getText().toString();
		        String serverAdd = txtServerAddress.getText().toString();
		        
		        if(authenticate(userName, pass, serverAdd)){
		        	// Set the Credentials in the Authenticator Model
					 ServerCredentials.setUsername(userName);
					 ServerCredentials.setPassword(pass);
					 ServerCredentials.setServerIp(serverAdd);
					 
		        	_displayManagerScreen(v);
		        	finish();
		        	
		        }else{ 
		        	  	Toast msg = Toast.makeText(XenmanagerActivity.this, "Invalid Login "+errorMsg,Toast.LENGTH_LONG);
		        	  	msg.setGravity(Gravity.CENTER, msg.getXOffset() / 2, msg.getYOffset() / 2);
		        	  	msg.show();
		        }
		        
		    
			}
		});
        
        // Setting Listener on Cancel Button
        btnCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// Handling Cancel Button
				finish();
			}
		});
    }
    
    public void _displayManagerScreen(View v){
    	//setContentView(R.layout.manager_ui);
    	Intent myIntent = new Intent(this, ApplicationUI.class);
    	startActivity(myIntent);
    }
    
    
    private boolean authenticate(String user, String  pass, String ip){
    	
    	if(ServerCredentials.getApplicationMode()== DEMO){
        	// Use HardCoded Data
        	if(user.compareTo("root")==0 && 
					 pass.compareTo("ramayan")==0 &&
					 ip.compareTo("192.168.1.15")==0){
		        		 ServerCredentials.setServerName("Demo Server");
						 ServerCredentials.setServerUuid(null);
						 return true;
		    }else{
		    			return false;
		    }
        }else{
        	// Call Web Service and Validate Credentails, if Valid set the Server name
        	String url = ServerCredentials.GETHOST_URL; 
        	RestClient client = new RestClient(url);
    		
    		client.AddParam("address", ip);
    		client.AddParam("username", user);
    		client.AddParam("password", pass);
    	  	
    		try {
    			client.Execute(2);
    			if(client.getResponseCode()==400){
    				errorMsg = client.getErrorMessage();
    				return false;
    			}else{
    				String response = client.getResponse();
    				JsonElement json = new JsonParser().parse(response);
    				JsonArray array= json.getAsJsonArray();
    				Iterator iterator = array.iterator();
    				
    				while(iterator.hasNext()){
    				    JsonElement json2 = (JsonElement)iterator.next();
    				    Gson gson = new Gson();
    				    Hosts host = gson.fromJson(json2, Hosts.class);
    				    ServerCredentials.setServerName(host.getHostName());
    				    ServerCredentials.setServerUuid(host.getUuid());
    				}
    			    
    				Log.v("Host Detials", "Name "+ServerCredentials.getServerName()+"  UUID: "+ServerCredentials.getServerUuid());
    				return true;
    			}
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
        }
        

    	return false;
    }
    
    
    // Variable Declarations
    EditText txtUserName;
    EditText txtPassword;
    EditText txtServerAddress;
    Button btnLogin;
    Button btnCancel;
   
}