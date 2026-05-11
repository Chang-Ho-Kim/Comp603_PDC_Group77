package smarthome.factory;

import smarthome.model.*;

public class DeviceFactory {

    public static Device create(String type, String name) {

        switch (type) {

            case "LIGHT":
                return new Light(name);

            case "DOOR":
                return new Door(name);

            case "AIR_CON":
                return new AirCon(name);

            case "HEATER":
                return new Heater(name);

            case "ALARM_CLOCK":
                return new AlarmClock(name);

            case "ROBOT_CLEANER":
                return new RobotCleaner(name);

            case "MUSIC_PLAYER":
                return new MusicPlayer(name);

            case "TELEVISION":
                return new TV(name);

            default:
                throw new IllegalArgumentException("Unknown type: " + type);
        }
    }
}