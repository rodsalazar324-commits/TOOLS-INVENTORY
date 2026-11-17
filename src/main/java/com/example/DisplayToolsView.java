
package com.example;

import java.util.ArrayList;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.router.Route;

@Route("display-tools")
public class DisplayToolsView extends ToolLayout {

    public DisplayToolsView() {
        super("display-tools", createPage());
    }

    private static VerticalLayout createPage() {

        VerticalLayout layout = new VerticalLayout();

        layout.setPadding(true);
        layout.setSpacing(true);
        layout.setWidthFull();

        H2 title = new H2("Display Tools");

        title.getStyle()
                .set("color", "#DCEBFF")
                .set("font-size", "26px")
                .set("font-weight", "bold");

        // Tool count
        Span toolCount = new Span();

        toolCount.getStyle()
                .set("color", "#DCEBFF")
                .set("font-size", "14px");

        // Create table
        Grid<Tool> grid = new Grid<>(Tool.class, false);

        // ID column
        grid.addColumn(new ComponentRenderer<>(tool ->
                createCell(tool.getId())
        ))
                .setHeader(createHeader("ID"))
                .setAutoWidth(true);

        // Name column
        grid.addColumn(new ComponentRenderer<>(tool ->
                createCell(tool.getName())
        ))
                .setHeader(createHeader("Name"))
                .setAutoWidth(true);

        // Category column
        grid.addColumn(new ComponentRenderer<>(tool ->
                createCell(tool.getCategory())
        ))
                .setHeader(createHeader("Category"))
                .setAutoWidth(true);

        // Condition column
        grid.addColumn(new ComponentRenderer<>(tool ->
                createCell(tool.getCondition())
        ))
                .setHeader(createHeader("Condition"))
                .setAutoWidth(true);

        // Status column
        grid.addColumn(new ComponentRenderer<>(tool ->
                createCell(tool.getStatus())
        ))
                .setHeader(createHeader("Status"))
                .setAutoWidth(true);

        // Location column
        grid.addColumn(new ComponentRenderer<>(tool ->
                createCell(tool.getLocation())
        ))
                .setHeader(createHeader("Location"))
                .setAutoWidth(true);

        // Table appearance
        grid.setWidthFull();
        grid.setHeight("450px");

        grid.addThemeVariants(
                GridVariant.LUMO_ROW_STRIPES,
                GridVariant.LUMO_COLUMN_BORDERS
        );

        grid.getStyle()
                .set("--lumo-base-color", "#F1F5F9")
                .set("--lumo-body-text-color", "#102744")
                .set("--lumo-secondary-text-color", "#315477")
                .set("background", "#F1F5F9");

        // Refresh tools
        Runnable refreshTools = () -> {

            ArrayList<Tool> tools =
                    new ArrayList<>(ToolStore.getTools());

            grid.setItems(tools);

            toolCount.setText(
                    "Total Tools: " + tools.size()
            );
        };

        // Refresh button
        Button refresh = new Button("Refresh");

        refresh.getStyle()
                .set("background", "#087BFA")
                .set("color", "white");

        refresh.addClickListener(e -> {
            refreshTools.run();
        });

        // Toolbar
        HorizontalLayout toolbar =
                new HorizontalLayout(toolCount, refresh);

        toolbar.setWidthFull();

        toolbar.setAlignItems(
                FlexComponent.Alignment.CENTER
        );

        toolbar.setJustifyContentMode(
                FlexComponent.JustifyContentMode.BETWEEN
        );

        // Initial loading
        refreshTools.run();

        layout.add(
                title,
                toolbar,
                grid
        );

        return layout;
    }

    // Create readable table cells
    private static Span createCell(String value) {

        Span cell = new Span(
                value == null ? "" : value
        );

        cell.getStyle()
                .set("color", "#102744")
                .set("font-weight", "500");

        return cell;
    }

    // Create readable column headers
    private static Span createHeader(String value) {

        Span header = new Span(value);

        header.getStyle()
                .set("color", "#102744")
                .set("font-weight", "bold");

        return header;
    }
}
