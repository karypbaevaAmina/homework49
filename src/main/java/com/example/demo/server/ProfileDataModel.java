package com.example.homework50.server;
import com.example.homework50.service.Profile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProfileDataModel {
    private List<Profile> profiles = new ArrayList<>();
    private boolean isAccess = false;
    private Profile profile = new Profile(0,null, null);

    public ProfileDataModel() {

        try {
            profiles = Utils.getProfilesFromFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public void setProfileById(int id) {
        profiles.forEach(profile1 -> {
            if (profile1.getId() == id) {
                profile = profile1;
            }
        });
    }

    public List<Profile> getProfiles() {
        return profiles;
    }

    public void setProfiles(List<Profile> profiles) {
        this.profiles = profiles;
    }

    public boolean isAccess() {
        return isAccess;
    }

    public void setAccess(boolean access) {
        isAccess = access;
    }

    public int getLastId() {
        int maxIndex = 0;
        for (Profile profile1 : profiles) {
            if (profile1.getId() > maxIndex) {
                maxIndex = profile1.getId();
            }
        }
        return maxIndex;
    }
}