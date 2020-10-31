package dk.glutter.groupsmsmanager.groupsms.SMS;

import java.util.ArrayList;

import static dk.glutter.groupsmsmanager.groupsms.StaticDB.myContacts_;

/**
 * Created by luther on 03/11/2016.
 */

public class MyContact {
    private String name, numberPrimary, email, credit;
    private ArrayList<Object> groups;

    public MyContact() {}

    public MyContact(Object name, Object numberPrimary, Object email, Object credit, ArrayList<Object> groups) {

        this.setName(String.valueOf(name));
        this.setNumberPrimary(String.valueOf(numberPrimary));
        this.setMail(String.valueOf(email));

        this.setCredit(String.valueOf(credit));

        this.groups = new ArrayList<>();
        this.groups = groups;


    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumberPrimary() {
        return numberPrimary;
    }

    public void setNumberPrimary(String numberPrimary) {
        this.numberPrimary = numberPrimary;
    }

    public String getMail() {
        return email;
    }

    public void setMail(String mail) {
        this.email = mail;
    }


    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public ArrayList<Object> getGroups() {
        return groups;
    }

    public void setGroups(ArrayList<Object> groups) {
        //TODO: find out if this is needed
        this.groups = new ArrayList<>();
        this.groups = groups;
    }

    /**
     * Returns particular Contacts groups by number
     * @param numberPrimary
     * @return
     */
    public  MyContact getContatByNumber( String numberPrimary )
    {
        if (myContacts_ != null) {
            for (int i = 0; i < myContacts_.size(); i++) {
                MyContact contact = myContacts_.get(i);
                if (contact != null) {
                    String contactsNumber = contact.getNumberPrimary();
                    if (contactsNumber.equalsIgnoreCase(numberPrimary)) {
                        return contact;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return name + " " + groups;
    }
}