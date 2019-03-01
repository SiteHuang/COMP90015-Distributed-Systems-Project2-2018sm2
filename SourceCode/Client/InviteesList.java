package CLIENT;

import java.util.ArrayList;
import java.util.List;

public class InviteesList {
    private static InviteesList inviteesManager;
    private List<Invitee> invitees;
    public InviteesList(){
        invitees = new ArrayList<>();
    }

    public static InviteesList getInstance(){
        if (inviteesManager==null){
            inviteesManager=new InviteesList();
        }
        return inviteesManager;
    }

    public List<Invitee> getInvitees() {
        return invitees;
    }

    public void addInvitee(Invitee newInvitee){
        invitees.add(newInvitee);
    }

    public void removeInvitee(){ invitees.clear();}
}
