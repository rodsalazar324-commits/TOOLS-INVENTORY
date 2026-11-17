
package com.example;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

@Route("delete-tool")
public class DeleteToolView extends ToolLayout {

    public DeleteToolView() {
        super("delete-tool", createContent());
    }

    private static VerticalLayout createContent() {

        VerticalLayout layout = new VerticalLayout();

        H2 title = new H2("Delete Tool");

        TextField toolId = new TextField("Tool ID");
        toolId.setPlaceholder("Enter tool ID");
        toolId.setWidth("350px");

        Button deleteButton = new Button("Delete Tool");

        deleteButton.getStyle()
            .set("background", "#DC2626")
            .set("color", "white");

        deleteButton.addClickListener(event -> {

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

            if (!ToolStore.deleteTool(id)) {
                Notification.show("Unable to delete tool.");
                return;
            }

            Notification.show("Tool deleted successfully!");

            toolId.clear();
        });

        layout.add(title, toolId, deleteButton);

        return layout;
    }
}
