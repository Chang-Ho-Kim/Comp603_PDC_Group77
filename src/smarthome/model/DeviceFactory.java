package smarthome.model;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class DeviceFactory {
    private static final Map<String, Function<String, Device>> deviceRegistry = new HashMap<>();

    static {
        deviceRegistry.put("1", Heater::new);
        deviceRegistry.put("2", Light::new);
        deviceRegistry.put("3", AirCon::new);
        deviceRegistry.put("4", AlarmClock::new);
        deviceRegistry.put("5", Door::new);
        deviceRegistry.put("6", MusicPlayer::new);
        deviceRegistry.put("7", TV::new);
        deviceRegistry.put("8", RobotCleaner::new);
    }

    public static Device createDevice(String typeId, String name) {
        Function<String, Device> constructor = deviceRegistry.get(typeId);
        if (constructor != null) {
            return constructor.apply(name);
        }
        return null;
    }
}
