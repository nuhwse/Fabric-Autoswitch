package autoswitch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.UUID;

@SuppressWarnings("WeakerAccess")
public class AutoSwitchLists {

    //Lists of Material/Entity the tool targets
    private HashMap<Object, ArrayList<UUID>> materialTargetLists = new HashMap<>();

    //Lists of tool slots
    private LinkedHashMap<UUID, ArrayList<Integer>> toolLists = new LinkedHashMap<>();

    public HashMap<Object, ArrayList<UUID>> getToolTargetLists() {

        for (String key : AutoSwitch.matCfg.propertyNames()) {
            String raw = AutoSwitch.matCfg.getProperty(key);
            String[] split = raw.split(",");

            ArrayList<UUID> list = new ArrayList<>();
            for (String input : split) {

                //Handle special case of useTool that takes in targets and tool to use
                if (key.equals("useTool")) {
                    ToolHandler v = (new ToolHandler(input, 1));
                    MaterialHandler c = (new MaterialHandler(v.getTag()));
                    AutoSwitch.data.useMap.put(c.getMat(), v.getEnchTag());

                    continue;
                }

                //Handle normal operation where input is tool and enchantment
                UUID x = (new ToolHandler(input, 0)).getId();
                if (x != null) {
                    list.add(x);
                }
            }

            //Populate target map with the list
            if (!list.isEmpty()) {
                this.materialTargetLists.put((new MaterialHandler(key)).getMat(), list);
            }

        }

        return this.materialTargetLists;

    }

    public LinkedHashMap<UUID, ArrayList<Integer>> getToolLists() {

        if (AutoSwitch.cfg.toolPriorityOrder() == null) {
            return toolLists;
        }

        for (String type : AutoSwitch.cfg.toolPriorityOrder()) {
            toolLists.put((new ToolHandler(type, 0).getId()), new ArrayList<>());
        }

        return toolLists;
    }
}
