package autoswitch.config;

import autoswitch.AutoSwitch;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.aeonbits.owner.Accessible;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Environment(EnvType.CLIENT)
public class AutoSwitchMapsGenerator {

    public AutoSwitchMapsGenerator() {
        populateToolTargetMaps();
        populateToolListMap(AutoSwitch.data.toolLists);
    }

    private void populateToolTargetMaps() {
        populateMap(AutoSwitch.data.toolTargetLists, AutoSwitch.matCfg);
        populateMap(AutoSwitch.data.useMap, AutoSwitch.usableCfg);

    }

    private void populateMap(ConcurrentHashMap<Object, ArrayList<UUID>> map, Accessible cfg) {
        for (String key : cfg.propertyNames()) {
            String raw = cfg.getProperty(key);
            String[] split = raw.split(",");

            ArrayList<UUID> list = new ArrayList<>();
            for (String input : split) {
                //Handle normal operation where input is tool and enchantment
                UUID x = (new ToolHandler(input)).getId();
                if (x != null) {
                    list.add(x);
                }
            }

            //Populate target map with the list
            if (!list.isEmpty() && (new MaterialHandler(key)).getMat() != null) {
                map.put((new MaterialHandler(key)).getMat(), list);
            }

        }
    }

    private void populateToolListMap(Map<UUID, ArrayList<Integer>> toolLists) {

        if (AutoSwitch.cfg.toolPriorityOrder() == null) {
            return;
        }

        for (String type : AutoSwitch.cfg.toolPriorityOrder()) {
            toolLists.put((new ToolHandler(type).getId()), new ArrayList<>());
        }

    }
}
