package models;

import java.util.ArrayList;

import manager.ui.AsyncTaskDemo;
import manager.ui.R;
import manager.ui.SampleActivity;
import manager.ui.VMDetailsActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    @Override
    public boolean areAllItemsEnabled()
    {
        return true;
    }

    private Context context;

    private ArrayList<String> groups;

    private ArrayList<ArrayList<VM>> children;

    public ExpandableListAdapter(Context context, ArrayList<String> groups,
            ArrayList<ArrayList<VM>> children) {
        this.context = context;
        this.groups = groups;
        this.children = children;
    }

   
    public void addItem(VM virtualMachine) {
        if (!groups.contains(virtualMachine.getGroup())) {
            groups.add(virtualMachine.getGroup());
        }
        int index = groups.indexOf(virtualMachine.getGroup());
        if (children.size() < index + 1) {
            children.add(new ArrayList<VM>());
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
    	
    	VM virtualMachine = (VM) getChild(groupPosition, childPosition);
        
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.child_layout, null);
        }
        CheckBox check = (CheckBox) convertView.findViewById(R.id.check);
        TextView tv = (TextView) convertView.findViewById(R.id.tvChild);
        tv.setText("   " + virtualMachine.getName());
        // Adding Lister on Text View
        tv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Toast.makeText(context.getApplicationContext(), "Child clicked", Toast.LENGTH_LONG).show();
				//SampleActivity sa = new SampleActivity();
				//sa.startVMdetailsActivity();
				startAct();
			}
		});
        
        
        
        // Depending upon the child type, set the imageTextView01
        tv.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        if (virtualMachine.getGroup().compareTo("VM Cluster 0")==0 ) {
            tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.poweron, 0, 0, 0);
            check.setChecked(true);
        } else if (virtualMachine.getGroup().compareTo("VM Cluster 1")==0 ) {
            tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.shutdown, 0, 0, 0);
        } else if (virtualMachine.getGroup().compareTo("VM Cluster 2")==0 ) {
            tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.suspend, 0, 0, 0);
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
    void startAct(){
    	Intent myIntent = new Intent(context, VMDetailsActivity.class);
    	context.startActivity(myIntent);
    }
}