package com.mike724.teamjug.timing;

/** Represents a "timer". For use with the
 *  TimeManager class.
 */
@SuppressWarnings("unused")
public class Timer {

    private long time;
    private boolean recurring;
    private String eventMethod;

    /** Timer Object Constructor
     *
     * @param time Interval in seconds
     * @param recurring It's like the energizer bunny
     * @param eventMethod Method to call when time is up (TimeEvents)
     */
    public Timer(long time, boolean recurring, String eventMethod) {
        this.time = time;
        this.recurring = recurring;
        this.eventMethod = eventMethod;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean isRecurring() {
        return recurring;
    }

    public void setRecurring(boolean recurring) {
        this.recurring = recurring;
    }

    public String getEventMethod() {
        return eventMethod;
    }

    public void setEventMethod(String eventMethod) {
        this.eventMethod = eventMethod;
    }

}
