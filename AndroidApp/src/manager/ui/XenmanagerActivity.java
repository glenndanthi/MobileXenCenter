package manager.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class XenmanagerActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
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
		        
				 if((txtUserName.getText().toString()).compareTo("testuser")==0 && txtPassword.getText().toString().compareTo("testpass")==0){
			           //Handle Successful login
					 _displayManagerScreen(v);
					 
					 
			          } else{
			           // Handle Incorrect Login
			        	  Toast msg = Toast.makeText(XenmanagerActivity.this, "Invalid Login",Toast.LENGTH_LONG);
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
    	Intent myIntent = new Intent(this, AsyncTaskDemo.class);
    	startActivity(myIntent);
    }
    
    
    // Variable Declarations
    EditText txtUserName;
    EditText txtPassword;
    Button btnLogin;
    Button btnCancel;
   
}