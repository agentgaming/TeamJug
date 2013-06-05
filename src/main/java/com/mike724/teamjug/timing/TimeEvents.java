package com.mike724.teamjug.timing;

import com.mike724.teamjug.TeamJug;

/** This class holds the methods that TimeManager
 *  will call when a Timer is up if another Object is not specified.
 */
@SuppressWarnings("unused")
public class TimeEvents {

    public void testMethod() {
        TeamJug.getInstance().getLogger().info("invoked");
    }



}
