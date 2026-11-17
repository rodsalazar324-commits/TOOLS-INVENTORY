
package com.example;

import java.util.Map;
import java.util.List;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.router.Route;

@Route("add-tool")
public class AddToolView extends ToolLayout {

    public AddToolView() {
        super("add-tool", createPage());
    }

    // Available tools grouped by category
    private static final Map<String, List<String>> TOOL_CATALOG = Map.of(

        "Hand Tool", List.of(
            "Hammer", "Screwdriver", "Pliers",
            "Adjustable Wrench", "Combination Wrench",
            "Socket Wrench", "Allen Key", "Chisel",
            "Handsaw", "Pipe Wrench", "Wire Stripper",
            "Utility Knife", "Crowbar", "Bolt Cutter",
            "Ratchet"
        ),

        "Power Tool", List.of(
            "Electric Drill", "Cordless Drill",
            "Angle Grinder", "Circular Saw", "Jigsaw",
            "Reciprocating Saw", "Power Sander",
            "Impact Driver", "Impact Wrench",
            "Rotary Tool", "Heat Gun", "Electric Planer",
            "Electric Screwdriver", "Power Cutter",
            "Welding Machine"
        ),

        "Measuring Tool", List.of(
            "Measuring Tape", "Steel Ruler", "Meter Stick",
            "Spirit Level", "Digital Caliper",
            "Vernier Caliper", "Micrometer",
            "Laser Distance Meter", "Protractor",
            "Combination Square", "Try Square",
            "Angle Finder", "Feeler Gauge",
            "Measuring Wheel", "Plumb Bob"
        ),

        "Gardening Tool", List.of(
            "Shovel", "Spade", "Garden Rake", "Hoe",
            "Garden Trowel", "Pruning Shears",
            "Hedge Shears", "Garden Fork",
            "Hand Cultivator", "Leaf Blower",
            "Lawn Mower", "Grass Trimmer", "Axe",
            "Pickaxe", "Wheelbarrow"
        ),

        "Safety Equipment", List.of(
            "Safety Helmet", "Safety Goggles",
            "Face Shield", "Ear Protection", "Dust Mask",
            "Respirator", "Work Gloves",
            "Cut-Resistant Gloves", "Safety Vest",
            "Safety Harness", "Steel-Toe Boots",
            "Knee Pads", "Welding Helmet",
            "Welding Gloves", "Protective Apron"
        ),

        "Welding Tool", List.of(
            "Welding Machine", "Welding Clamp",
            "Welding Torch", "Welding Chipping Hammer",
            "Wire Brush", "Metal File", "Bench Vise",
            "C-Clamp", "Metal Shears", "Plasma Cutter",
            "Soldering Iron", "Soldering Gun",
            "Deburring Tool", "Metal Punch", "Pipe Cutter"
        ),

        "Woodworking Tool", List.of(
            "Wood Chisel", "Hand Plane", "Coping Saw",
            "Wood Rasp", "Wood File", "Claw Hammer",
            "Mallet", "Circular Saw", "Jigsaw", "Router",
            "Wood Clamp", "Nail Set", "Miter Saw",
            "Table Saw", "Wood Carving Knife"
        )
    );

    private static VerticalLayout createPage() {

        VerticalLayout page = new VerticalLayout();

        page.setPadding(true);
        page.setSpacing(true);
        page.setWidthFull();

        H2 title = new H2("Add New Tool");
        title.getStyle().set("color", "white");

        TextField id = new TextField("Tool ID");

        ComboBox<String> category =
            new ComboBox<>("Category");

        category.setItems(
            "Hand Tool",
            "Power Tool",
            "Measuring Tool",
            "Gardening Tool",
            "Safety Equipment",
            "Welding Tool",
            "Woodworking Tool"
        );

        category.setPlaceholder("Select Category");

        ComboBox<String> name =
            new ComboBox<>("Tool Name");

        name.setPlaceholder("Select Category First");
        name.setEnabled(false);
        name.setAllowCustomValue(false);

        // Change available tool names when category changes
        category.addValueChangeListener(event -> {

            name.clear();

            String selectedCategory = event.getValue();

            if (selectedCategory != null) {

                name.setItems(
                    TOOL_CATALOG.get(selectedCategory)
                );

                name.setEnabled(true);
                name.setPlaceholder("Select Tool");

            } else {

                name.setEnabled(false);
                name.setPlaceholder("Select Category First");
            }
        });

        ComboBox<String> condition =
            new ComboBox<>("Condition");

        condition.setItems("Good", "Fair", "Poor");
        condition.setPlaceholder("Select Condition");

        ComboBox<String> status =
            new ComboBox<>("Status");

        status.setItems(
            "Available",
            "Borrowed",
            "Under Maintenance",
            "Lost"
        );

        status.setValue("Available");

        TextField location = new TextField("Location");

        TextArea description =
            new TextArea("Description (Optional)");

        description.setPlaceholder("Enter additional notes...");
        description.setWidthFull();
        description.setHeight("90px");

        id.setPlaceholder("e.g. T001");
        location.setPlaceholder("e.g. Tool Room A");

        HorizontalLayout row1 =
            new HorizontalLayout(id, name);

        HorizontalLayout row2 =
            new HorizontalLayout(category, condition);

        HorizontalLayout row3 =
            new HorizontalLayout(status, location);

        row1.setWidthFull();
        row2.setWidthFull();
        row3.setWidthFull();

        row1.setFlexGrow(1, id, name);
        row2.setFlexGrow(1, category, condition);
        row3.setFlexGrow(1, status, location);

        Button save = new Button("Save Tool");
        Button clear = new Button("Clear");
        Button cancel = new Button("Cancel");

        save.setIcon(VaadinIcon.CHECK.create());

        save.getStyle()
            .set("background", "#087BFA")
            .set("color", "white");

        clear.getStyle()
            .set("background", "#102744")
            .set("color", "white");

        cancel.getStyle()
            .set("background", "#102744")
            .set("color", "white");

        // Save tool
        save.addClickListener(e -> {

            if (id.isEmpty()
                || name.isEmpty()
                || category.isEmpty()
                || condition.isEmpty()
                || status.isEmpty()
                || location.isEmpty()) {

                Notification.show(
                    "Please complete all required fields."
                );

                return;
            }

            String toolId = id.getValue().trim();

            if (toolId.isEmpty()) {
                Notification.show("Please enter a Tool ID.");
                return;
            }

            Tool tool = new CatalogTool(
                toolId,
                name.getValue(),
                condition.getValue(),
                status.getValue(),
                location.getValue().trim(),
                description.getValue(),
                category.getValue()
            );

            if (!ToolStore.addTool(tool)) {

                Notification.show("Tool ID already exists.");
                return;
            }

            Notification.show("Tool saved successfully!");

            id.clear();
            category.clear();
            name.clear();
            condition.clear();
            status.setValue("Available");
            location.clear();
            description.clear();
        });

        // Clear fields
        clear.addClickListener(e -> {

            id.clear();
            category.clear();
            name.clear();
            condition.clear();
            status.setValue("Available");
            location.clear();
            description.clear();
        });

        // Cancel
        cancel.addClickListener(e ->
            e.getSource().getUI().ifPresent(
                ui -> ui.navigate("dashboard")
            )
        );

        HorizontalLayout buttons =
            new HorizontalLayout(save, clear, cancel);

        buttons.setWidthFull();

        buttons.setJustifyContentMode(
            FlexComponent.JustifyContentMode.BETWEEN
        );

        page.getStyle()
            .set("box-sizing", "border-box");

        page.add(
            title,
            row1,
            row2,
            row3,
            description,
            buttons
        );

        return page;
    }

    // General tool class for the seven catalog categories
    private static class CatalogTool extends Tool {

        private String category;

        public CatalogTool(
            String id,
            String name,
            String condition,
            String status,
            String location,
            String description,
            String category
        ) {

            super(
                id,
                name,
                condition,
                status,
                location,
                description
            );

            this.category = category;
        }

        @Override
        public String getCategory() {
            return category;
        }
    }
}
