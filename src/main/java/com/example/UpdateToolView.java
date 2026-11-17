
package com.example;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.Route;

@Route("update-tool")
public class UpdateToolView extends ToolLayout {

    public UpdateToolView() {
        super("update-tool", createContent());
    }

    private static VerticalLayout createContent() {

        VerticalLayout layout = new VerticalLayout();

        H2 title = new H2("Update Tool");

        TextField toolId = new TextField("Tool ID");
        toolId.setPlaceholder("Enter tool ID");
        toolId.setWidth("350px");

        Button searchButton = new Button("Find Tool");

        TextField name = new TextField("Tool Name");
        name.setWidth("350px");

        TextField condition = new TextField("Condition");
        condition.setWidth("350px");

        ComboBox<String> status = new ComboBox<>("Status");
        status.setItems(
            "Available",
            "Borrowed",
            "Under Maintenance",
            "Lost"
        );
        status.setWidth("350px");

        TextField location = new TextField("Location");
        location.setWidth("350px");

        TextArea description = new TextArea("Description");
        description.setWidth("350px");

        Button updateButton = new Button("Save Changes");

        name.setVisible(false);
        condition.setVisible(false);
        status.setVisible(false);
        location.setVisible(false);
        description.setVisible(false);
        updateButton.setVisible(false);

        final Tool[] selectedTool = {null};

        searchButton.addClickListener(event -> {

            String id = toolId.getValue().trim();

            if (id.isEmpty()) {
                Notification.show("Please enter a Tool ID.");
                return;
            }

            Tool tool = ToolStore.findTool(id);

            if (tool == null) {
                Notification.show("Tool not found.");
                return;
            }

            selectedTool[0] = tool;

            name.setValue(tool.getName());
            condition.setValue(tool.getCondition());
            status.setValue(tool.getStatus());
            location.setValue(tool.getLocation());
            description.setValue(tool.getDescription());

            name.setVisible(true);
            condition.setVisible(true);
            status.setVisible(true);
            location.setVisible(true);
            description.setVisible(true);
            updateButton.setVisible(true);

            Notification.show("Tool found.");
        });

        updateButton.addClickListener(event -> {

            Tool tool = selectedTool[0];

            if (tool == null) {
                Notification.show("Find a tool first.");
                return;
            }

            if (name.getValue().trim().isEmpty()
                    || condition.getValue().trim().isEmpty()
                    || status.getValue() == null
                    || location.getValue().trim().isEmpty()) {

                Notification.show("Please complete the required fields.");
                return;
            }

            tool.setName(name.getValue().trim());
            tool.setCondition(condition.getValue().trim());
            tool.setStatus(status.getValue());
            tool.setLocation(location.getValue().trim());
            tool.setDescription(description.getValue().trim());

            Notification.show("Tool updated successfully!");

            toolId.clear();
            name.clear();
            condition.clear();
            status.clear();
            location.clear();
            description.clear();

            selectedTool[0] = null;

            name.setVisible(false);
            condition.setVisible(false);
            status.setVisible(false);
            location.setVisible(false);
            description.setVisible(false);
            updateButton.setVisible(false);
        });

        layout.add(
            title,
            toolId,
            searchButton,
            name,
            condition,
            status,
            location,
            description,
            updateButton
        );

        return layout;
    }
}
