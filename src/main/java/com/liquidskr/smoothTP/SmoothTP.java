package com.liquidskr.smoothTP;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class SmoothTP extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("smtp")) {
            if (args.length == 11) {

                Player player = (Player) sender;

                double x1 = Double.parseDouble(args[0]);
                double y1 = Double.parseDouble(args[1]);
                double z1 = Double.parseDouble(args[2]);
                double yaw1 = Double.parseDouble(args[3]);
                double pitch1 = Double.parseDouble(args[4]);
                double x2 = Double.parseDouble(args[5]);
                double y2 = Double.parseDouble(args[6]);
                double z2 = Double.parseDouble(args[7]);
                double yaw2 = Double.parseDouble(args[8]);
                double pitch2 = Double.parseDouble(args[9]);
                double speed = Double.parseDouble(args[10]);

                smoothTeleport(player, x1, y1, z1, yaw1, pitch1, x2, y2, z2, yaw2, pitch2, speed);

                return true;
            }

        }
        return false;
    }

    private void smoothTeleport(Player player, double x1, double y1, double z1, double yaw1, double pitch1, double x2, double y2, double z2, double yaw2, double pitch2, double speed) {
        double dx = x2 - x1;
        double dy = y2 - y1;
        double dz = z2 - z1;
        double dyaw = yaw2 - yaw1;
        double dpitch = pitch2 - pitch1;

        double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);
        double yawDistance = Math.abs(dyaw);
        double pitchDistance = Math.abs(dpitch);

        double lerp = speed / 1000;
        double lerpYaw = lerp * yawDistance;
        double lerpPitch = lerp * pitchDistance;

        double lerpSpeed = speed / 1000;

        double lerpX = dx * lerp;
        double lerpY = dy * lerp;
        double lerpZ = dz * lerp;

        double lerpYawSpeed = dyaw * lerpSpeed;
        double lerpPitchSpeed = dpitch * lerpSpeed;

        double currentX = x1;
        double currentY = y1;
        double currentZ = z1;
        double currentYaw = yaw1;
        double currentPitch = pitch1;

        while (distance > 0) {
            if (distance < lerp) {
                currentX = x2;
                currentY = y2;
                currentZ = z2;
                currentYaw = yaw2;
                currentPitch = pitch2;
                break;
            }

            if (yawDistance < lerpYaw) {
                currentYaw = yaw2;
            } else {
                currentYaw += lerpYawSpeed;
            }

            if (pitchDistance < lerpPitch) {
                currentPitch = pitch2;
            } else {
                currentPitch += lerpPitchSpeed;
            }

            currentX += lerpX;
            currentY += lerpY;
            currentZ += lerpZ;

            distance = Math.sqrt((x2 - currentX) * (x2 - currentX) + (y2 - currentY) * (y2 - currentY) + (z2 - currentZ) * (z2 - currentZ));
            yawDistance = Math.abs(yaw2 - currentYaw);
            pitchDistance = Math.abs(pitch2 - currentPitch);

            try {
                Thread.sleep(1);
                player.teleport(new Location(player.getWorld(), currentX, currentY, currentZ, (float) currentYaw, (float) currentPitch));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
