package com.mike724.teamjug.timing;

/** Represents a "timer". For use with the
 *  TimeManager class.
 */
@SuppressWarnings("unused")
public class Timer {

    private long time;
    private boolean recurring;
    private String eventMethod;
    private Object methodObject;
    private boolean killFlag;

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
        killFlag = false;
    }

    /** Timer Object Constructor
     *
     * @param time Interval in seconds
     * @param recurring It's like the energizer bunny
     * @param methodObject The object where the method is located and should be invoked
     * @param eventMethod Method to call when time is up (TimeEvents)
     */
    public Timer(long time, boolean recurring, Object methodObject, String eventMethod) {
        this.time = time;
        this.recurring = recurring;
        this.methodObject = methodObject;
        this.eventMethod = eventMethod;
        killFlag = false;
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

    public Object getMethodObject() {
        return methodObject;
    }

    public void setMethodObject(Object methodObject) {
        this.methodObject = methodObject;
    }

    public String getEventMethod() {
        return eventMethod;
    }

    public void setEventMethod(String eventMethod) {
        this.eventMethod = eventMethod;
    }

    public boolean shouldKill() {
        return killFlag;
    }

    public void setKillFlag(boolean killFlag) {
        this.killFlag = killFlag;
    }
}
