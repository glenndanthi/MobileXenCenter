package models;

import java.util.ArrayList;

import manager.ui.ApplicationUI;
import manager.ui.AsyncTaskDemo;
import manager.ui.R;
import manager.ui.SampleActivity;
import manager.ui.VMDetailsActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class ExpandableListAdapter extends BaseExpandableListAdapter {
	VirtualMachine vmSaved;
    @Override
    public boolean areAllItemsEnabled()
    {
        return true;
    }

    private Context context;

    private ArrayList<String> groups;
    private ApplicationUI storedUi;
    private ArrayList<ArrayList<VirtualMachine>> children;

    public ExpandableListAdapter(Context context, ArrayList<String> groups,
            ArrayList<ArrayList<VirtualMachine>> children, ApplicationUI ui) {
        this.context = context;
        this.groups = groups;
        this.children = children;
        this.storedUi = ui;
    }

    
    public void removeItem(String uid){
    	//Log.v("test", "UID:"+uid+"  child size= "+children.size()+" child 1st size "+children.get(0).size());
    	
    	for(int i=0;i<children.size();i++){
    		for(int j=0;i<children.get(i).size();j++){
    			if(children.get(i).get(j).getUid().compareTo(uid)==0){
    				children.get(i).remove(j);
    				break;
    			}
    		}
    	}
    }
   
    public void addItem(VirtualMachine virtualMachine) {
    	
        if (!groups.contains(ServerCredentials.getServerName())) {
            groups.add(ServerCredentials.getServerName());
        }
        int index = groups.indexOf(ServerCredentials.getServerName());
        if (children.size() < index + 1) {
            children.add(new ArrayList<VirtualMachine>());
        }
        children.get(index).add(virtualMachine);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return children.get(groupPosition).get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }
    
    // Return a child view. You can load your custom layout here.
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
            View convertView, ViewGroup parent) {
    	
    	VirtualMachine vm = (VirtualMachine) getChild(groupPosition, childPosition);
    	vmSaved = vm;
    	Log.v("getChildView","Name: "+vm.getName()+"  UID: "+vm.getUid()+"  GP "+groupPosition+"  CP"+childPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.child_layout, null);
        }
        final String s = vm.getUid();
        
        CheckBox check = (CheckBox) convertView.findViewById(R.id.check);
        check.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					ServerCredentials.selectedUID.add(s);
				}else{
					for(int i=0;i<ServerCredentials.selectedUID.size();i++){
						if(ServerCredentials.selectedUID.get(i).compareTo(s)==0){
							ServerCredentials.selectedUID.remove(i);
							break;
						}
					}
				}
				//Toast.makeText(context,"Added: "+s+"    All="+all, Toast.LENGTH_LONG).show();
				
			}
		});
        
        
        
        TextView tv = (TextView) convertView.findViewById(R.id.tvChild);
        tv.setText("   " + vm.getName());
        
        // Adding Lister on Text View
        tv.setOnClickListener(new OnClickListener() {
        	
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startAct(s);
				//Toast.makeText(context,"  Name:"+s, Toast.LENGTH_LONG).show();
			}
		});
        
        
        
        // Depending upon the child type, set the imageTextView01
        tv.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        if (vm.getState().compareTo("Halted")==0 ) {
            tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.tree_halted_16, 0, 0, 0);            
        } else if (vm.getState().compareTo("Suspended")==0 ) {
            tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.tree_suspended_16, 0, 0, 0);
        } else if (vm.getState().compareTo("Running")==0 ) {
            tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.tree_running_16, 0, 0, 0);
        }else if (vm.getState().compareTo("Starting")==0 ) {
            tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.tree_starting_16, 0, 0, 0);
        }else if (vm.getState().compareTo("Stopped")==0 ) {
            tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.tree_stopped_16, 0, 0, 0);
        }else if (vm.getState().compareTo("Paused")==0 ) {
            tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.tree_paused_16, 0, 0, 0);
        }
        
        
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return children.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return groups.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    // Return a group view. You can load your custom layout here.
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
            ViewGroup parent) {
        String group = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.group_layout, null);
        }
        TextView tv = (TextView) convertView.findViewById(R.id.tvGroup);
        tv.setText(group);
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int arg0, int arg1) {
        return true;
    }
    void startAct(String uid){
    	// Need to attach the selected VM
    	ServerCredentials.setSelectedVMId(uid);
    	Intent myIntent = new Intent(context, VMDetailsActivity.class);
    	context.startActivity(myIntent);
    	this.storedUi.finish();
    }
}