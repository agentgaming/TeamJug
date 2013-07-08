package com.mike724.teamjug.timing;

import com.mike724.teamjug.TeamJug;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/** Object that manages all the Timers/events.
 *  This object operates on ticks.
 */
@SuppressWarnings("unused")
public class TimeManager {

    // The Long is for the current time on the Timer
    private HashMap<Timer, Long> timers;
    //TimeEvents instance where Timer methods are stored
    private TimeEvents timeEvents;

    public TimeManager() {
        timers = new HashMap<>();
        timeEvents = new TimeEvents();
    }

    public void addTimer(Timer timer) {
        long time = timer.getTime();
        timers.put(timer, time);
    }

    public long getTimeLeft(Timer t) {
        if(timers.containsKey(t)) {
            return timers.get(t);
        }
        return -1;
    }

    public void onTick(long ticks) {
        //Every second
        if(ticks % 20 == 0) {
            for(Map.Entry<Timer, Long> entry : timers.entrySet()) {
                if(entry.getKey().shouldKill()) {
                    timers.remove(entry.getKey());
                    continue;
                }
                long entryTime = entry.getValue();
                if(entryTime > 1) {
                    //Decrement current time
                    entry.setValue(--entryTime);
                } else {
                    Timer timer = entry.getKey();

                    //Run the timer method
                    try {
                        if(timer.getMethodObject() == null) {
                            Method m = TimeEvents.class.getMethod(timer.getEventMethod());
                            m.invoke(timeEvents);
                        } else {
                            Method m = timer.getMethodObject().getClass().getMethod(timer.getEventMethod());
                            m.invoke(timer.getMethodObject());
                        }
                    } catch (NoSuchMethodException e) {
                        TeamJug.errorMessage("The method "+timer.getEventMethod()+" does not exist!");
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }

                    if(timer.isRecurring()) {
                        //Reset time
                        entry.setValue(timer.getTime());
                    } else {
                        timers.remove(timer);
                    }
                }
            }
        }
    }

}
