
package com.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ToolStore {

    private static final List<Tool> tools = new ArrayList<>();

    public static List<Tool> getTools() {
        return Collections.unmodifiableList(tools);
    }

    public static boolean addTool(Tool tool) {

        if (tool == null || tool.getId() == null) {
            return false;
        }

        if (findTool(tool.getId()) != null) {
            return false;
        }

        tools.add(tool);

        return true;
    }

    public static Tool findTool(String id) {

        if (id == null || id.trim().isEmpty()) {
            return null;
        }

        for (Tool tool : tools) {

            if (tool.getId().equalsIgnoreCase(id.trim())) {
                return tool;
            }
        }

        return null;
    }

    public static boolean deleteTool(String id) {

        Tool tool = findTool(id);

        if (tool == null) {
            return false;
        }

        tools.remove(tool);

        return true;
    }
}
