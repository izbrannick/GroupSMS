package dk.glutter.groupsmsmanager.groupsms;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by u321424 on 03-01-2017.
 */

public class MyGroup {

    private String uniqueID;
    private String groupName;
    private List<MyContact> members;

    public MyGroup(String groupName)
    {
        this.uniqueID = String.valueOf( UUID.randomUUID() );
        this.groupName = groupName;
        this.members = new ArrayList<>();
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName( String groupName ) {
        this.groupName = groupName;
    }

    public List<MyContact> getMembers() {
        return members;
    }

    public void setMembers( List<MyContact> members ) {
        this.members = members;
    }

    public void addMembers( MyContact member ) {
        this.members.add( member );
    }

    public String getUniqueID() {
        return uniqueID;
    }

    public void setUniqueID( String uniqueID ) {
        this.uniqueID = uniqueID;
    }
}