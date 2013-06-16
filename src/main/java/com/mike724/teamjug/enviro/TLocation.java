package com.mike724.teamjug.enviro;

import org.bukkit.Location;
import org.bukkit.World;

/** Bukkit Location alternative that is world agnostic */
@SuppressWarnings("unused")
public class TLocation {

    private double x;
    private double y;
    private double z;
    private float pitch;
    private float yaw;

    public TLocation(double x, double y, double z, float pitch, float yaw) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.pitch = pitch;
        this.yaw = yaw;
    }

    public TLocation(double x, double y, double z) {
        this(x, y, z, 0, 0);
    }

    /** Creates a Bukkit Location instance from this object
     *
     * @param world The world of the Location object
     * @return The Bukkit Location instance
     */
    public Location getBukkitLocation(World world) {
        return new Location(world, x, y, z, yaw, pitch);
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }
}
