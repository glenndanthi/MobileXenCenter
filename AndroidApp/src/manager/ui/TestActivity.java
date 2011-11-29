package manager.ui;

import org.json.JSONObject;

import service.RestClient;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class TestActivity extends Activity{

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		String LOGIN_URL = "http://192.168.1.9:8008/mobilexencenter/virtualmachines/";
		//String LOGIN_URL = "http://192.168.1.9:8008/";
		RestClient client = new RestClient(LOGIN_URL);
		
		client.AddParam("address", "192.168.100.100");
		client.AddParam("username", "ramayan");
		client.AddParam("password", "heyram");
		


		try {
			client.Execute(2);
			String response = client.getResponse();
			//JSONObject newobj = new JSONObject(response);
			Log.v("res", response);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}