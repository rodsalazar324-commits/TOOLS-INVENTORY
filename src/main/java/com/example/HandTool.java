
package com.example;

public class HandTool extends Tool {

    public HandTool(
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
        return "Hand Tool";
    }
}
