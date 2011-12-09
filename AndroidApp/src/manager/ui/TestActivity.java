package manager.ui;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import models.VirtualMachine;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import service.RestClient;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class TestActivity extends Activity{

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.vmdetails);
		
		/*String LOGIN_URL = "http://192.168.1.9:8008/mobilexencenter/virtualmachines";
		//String LOGIN_URL = "http://192.168.1.9:8008/";
		RestClient client = new RestClient(LOGIN_URL);
		
		client.AddParam("address", "192.168.1.15");
		client.AddParam("username", "root");
		client.AddParam("password", "ramayan");
	  	


		try {
			client.Execute(2);
			String response = client.getResponse();
			//JSONObject newobj = new JSONObject(response);
			Log.v("res", response);
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		
		String json1 = " [{\"state\": \"Halted\", \"name\": \"Debian 1\", \"uid\": \"db54aecc-d499-69f8-f194-9af8d297081c\"}, {\"state\": " +
				"\"Halted\", \"name\": \"Debian 2\", \"uid\": \"594c306d-4aff-41fa-095f-b038d45a661f\"}]";

		JsonElement json = new JsonParser().parse(json1);

		JsonArray array= json.getAsJsonArray();

		Iterator iterator = array.iterator();

		List<VirtualMachine> vmList = new ArrayList<VirtualMachine>();

		while(iterator.hasNext()){
		    JsonElement json2 = (JsonElement)iterator.next();
		    Gson gson = new Gson();
		    VirtualMachine vm = gson.fromJson(json2, VirtualMachine.class);
		    //can set some values in contact, if required 
		    vmList.add(vm);
		}
		
		for(int i=0;i<vmList.size();i++){
			Log.v("Details "+i," VM Name:"+vmList.get(i).getName()+" VM State:"+vmList.get(i).getState()+" VM UID:"+vmList.get(i).getUid());
		}
	}
}
