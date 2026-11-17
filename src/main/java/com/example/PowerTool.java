
package com.example;

public class PowerTool extends Tool {

    public PowerTool(
        String id,
        String name,
        String condition,
        String status,
        String location,
        String description
    ) {
        super(id, name, condition, status, location, description);
    }

    @Override
    public String getCategory() {
        return "Power Tool";
    }
}
