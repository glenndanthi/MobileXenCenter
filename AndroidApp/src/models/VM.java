package models;

public class VM {
	private String name;
    private String group;

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public VM(String name,int group) {
        this.name = name;
        this.group = "VM Cluster "+String.valueOf(group);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
