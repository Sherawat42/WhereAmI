package com.example.sherawat42.whereami;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by sherawat42 on 5/5/17.
 */
@Table(name="Reminders")
public class LocationReminderObject extends Model {

    @Column(name="location_type")
    String loc_type;

    @Column(name="description")
    String description;

    @Column(name="done")
    Boolean done = false;


    public LocationReminderObject(){super();}

    public String getLoc_type() {
        return loc_type;
    }

    public Boolean getDone() {
        return done;
    }

    public void setDone(Boolean done) {
        this.done = done;
    }

    public void setLoc_type(String loc_type) {
        this.loc_type = loc_type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
