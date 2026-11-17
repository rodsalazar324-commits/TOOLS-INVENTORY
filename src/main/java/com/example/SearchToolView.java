
package com.example;

import java.util.List;
import java.util.stream.Collectors;
import java.time.format.DateTimeFormatter;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.Route;

@Route("search-tool")
public class SearchToolView extends ToolLayout {

    public SearchToolView() {
        super("search-tool", createPage());
    }

    private static VerticalLayout createPage() {

        VerticalLayout page = new VerticalLayout();

        page.setPadding(true);
        page.setSpacing(true);
        page.setWidthFull();

        page.getStyle()
            .set("box-sizing", "border-box");

        // TITLE

        H2 title = new H2("Search Tool");

        title.getStyle()
            .set("color", "white")
            .set("margin", "0");

        // SEARCH FIELD

        TextField search = new TextField();

        search.setPlaceholder("Search by Tool ID or Name...");
        search.setPrefixComponent(VaadinIcon.SEARCH.create());
        search.setWidth("100%");

        // CATEGORY FILTER

        ComboBox<String> category =
            new ComboBox<>("Category");

        category.setItems(
            "All Categories",
            "Hand Tool",
            "Power Tool",
            "Measuring Tool",
            "Gardening Tool",
            "Safety Equipment",
            "Welding Tool",
            "Woodworking Tool"
        );

        category.setValue("All Categories");

        // STATUS FILTER

        ComboBox<String> status =
            new ComboBox<>("Status");

        status.setItems(
            "All Status",
            "Available",
            "Borrowed",
            "Under Maintenance",
            "Lost"
        );

        status.setValue("All Status");

        // FILTER LAYOUT

        HorizontalLayout filters =
            new HorizontalLayout(search, category, status);

        filters.setWidthFull();

        filters.setAlignItems(
            FlexComponent.Alignment.END
        );

        filters.setFlexGrow(1, search);

        // RESULTS TITLE

        H2 resultsTitle = new H2("Search Results");

        resultsTitle.getStyle()
            .set("color", "white")
            .set("font-size", "17px");

        // TOOL GRID

        Grid<Tool> grid = new Grid<>(Tool.class, false);

        // ID COLUMN

        grid.addColumn(new ComponentRenderer<>(tool ->
            createCell(tool.getId())
        ))
            .setHeader(createHeader("ID"));

        // NAME COLUMN

        grid.addColumn(new ComponentRenderer<>(tool ->
            createCell(tool.getName())
        ))
            .setHeader(createHeader("Name"));

        // CATEGORY COLUMN

        grid.addColumn(new ComponentRenderer<>(tool ->
            createCell(tool.getCategory())
        ))
            .setHeader(createHeader("Category"));

        // CONDITION COLUMN

        grid.addColumn(new ComponentRenderer<>(tool ->
            createCell(tool.getCondition())
        ))
            .setHeader(createHeader("Condition"));

        // STATUS BADGE

        grid.addComponentColumn(tool -> {

            Span badge = new Span(tool.getStatus());

            badge.getStyle()
                .set("background", colorFor(tool.getStatus()))
                .set("color", "white")
                .set("border-radius", "20px")
                .set("padding", "4px 10px")
                .set("font-size", "11px");

            return badge;

        }).setHeader(createHeader("Status"));

        // VIEW BUTTON

        grid.addComponentColumn(tool -> {

            Button viewButton = new Button(
                "View",
                VaadinIcon.EYE.create()
            );

            viewButton.getStyle()
                .set("background", "#087BFA")
                .set("color", "white");

            viewButton.addClickListener(e ->
                showTool(tool)
            );

            return viewButton;

        }).setHeader(createHeader("Action"));

        // TABLE APPEARANCE

        grid.setWidthFull();
        grid.setHeight("400px");

        grid.addThemeVariants(
            GridVariant.LUMO_ROW_STRIPES,
            GridVariant.LUMO_COLUMN_BORDERS
        );

        grid.getStyle()
            .set("--lumo-base-color", "#F1F5F9")
            .set("--lumo-body-text-color", "#102744")
            .set("--lumo-secondary-text-color", "#315477")
            .set("background", "#F1F5F9");

        // REFRESH SEARCH RESULTS

        Runnable refresh = () -> {

            String query = search.getValue() == null
                ? ""
                : search.getValue().trim().toLowerCase();

            String selectedCategory = category.getValue();
            String selectedStatus = status.getValue();

            List<Tool> filtered = ToolStore.getTools().stream()

                .filter(tool -> {

                    String toolId = tool.getId() == null
                        ? ""
                        : tool.getId().toLowerCase();

                    String toolName = tool.getName() == null
                        ? ""
                        : tool.getName().toLowerCase();

                    return toolId.contains(query)
                        || toolName.contains(query);
                })

                .filter(tool ->
                    selectedCategory == null
                    || selectedCategory.equals("All Categories")
                    || selectedCategory.equalsIgnoreCase(
                        tool.getCategory()
                    )
                )

                .filter(tool ->
                    selectedStatus == null
                    || selectedStatus.equals("All Status")
                    || selectedStatus.equalsIgnoreCase(
                        tool.getStatus()
                    )
                )

                .collect(Collectors.toList());

            grid.setItems(filtered);

            resultsTitle.setText(
                "Search Results (" + filtered.size() + ")"
            );
        };

        // SEARCH LISTENERS

        search.addValueChangeListener(e ->
            refresh.run()
        );

        category.addValueChangeListener(e ->
            refresh.run()
        );

        status.addValueChangeListener(e ->
            refresh.run()
        );

        // ADD COMPONENTS

        page.add(
            title,
            filters,
            resultsTitle,
            grid
        );

        refresh.run();

        return page;
    }

    // CREATE READABLE TABLE CELLS

    private static Span createCell(String value) {

        Span cell = new Span(
            value == null ? "" : value
        );

        cell.getStyle()
            .set("color", "#102744")
            .set("font-weight", "500");

        return cell;
    }

    // CREATE READABLE TABLE HEADERS

    private static Span createHeader(String value) {

        Span header = new Span(value);

        header.getStyle()
            .set("color", "#102744")
            .set("font-weight", "bold");

        return header;
    }

    // STATUS COLORS

    private static String colorFor(String status) {

        if (status == null) {
            return "#64748B";
        }

        switch (status) {

            case "Available":
                return "#059669";

            case "Borrowed":
                return "#DC2626";

            case "Under Maintenance":
                return "#D99B00";

            case "Lost":
                return "#64748B";

            default:
                return "#64748B";
        }
    }

    // VIEW TOOL DETAILS AND BORROWING HISTORY

    private static void showTool(Tool tool) {

        Dialog dialog = new Dialog();

        dialog.setHeaderTitle("Tool Details");
        dialog.setWidth("550px");

        DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("MMM dd, yyyy hh:mm a");

        VerticalLayout details = new VerticalLayout();

        details.setPadding(true);
        details.setSpacing(true);
        details.setWidthFull();

        // BASIC INFORMATION

        H2 basicTitle = new H2("Basic Information");

        basicTitle.getStyle()
            .set("font-size", "18px")
            .set("color", "#102744");

        details.add(
            basicTitle,
            createDetail("Tool ID", tool.getId()),
            createDetail("Tool Name", tool.getName()),
            createDetail("Category", tool.getCategory()),
            createDetail("Condition", tool.getCondition()),
            createDetail("Status", tool.getStatus()),
            createDetail("Location", tool.getLocation()),
            createDetail("Description", tool.getDescription())
        );

        // CURRENT BORROWER

        H2 borrowerTitle = new H2("Current Borrower");

        borrowerTitle.getStyle()
            .set("font-size", "18px")
            .set("color", "#102744");

        details.add(borrowerTitle);

        if (tool.getCurrentBorrower() != null) {

            details.add(
                createDetail(
                    "Borrower",
                    tool.getCurrentBorrower()
                ),
                createDetail(
                    "Date Borrowed",
                    tool.getBorrowedAt().format(formatter)
                )
            );

        } else {

            details.add(
                createDetail(
                    "Borrower",
                    "No current borrower"
                )
            );
        }

        // BORROWING HISTORY

        H2 historyTitle = new H2("Borrowing History");

        historyTitle.getStyle()
            .set("font-size", "18px")
            .set("color", "#102744");

        details.add(historyTitle);

        if (tool.getBorrowingHistory().isEmpty()) {

            details.add(
                createDetail(
                    "History",
                    "No borrowing records yet."
                )
            );

        } else {

            int number = 1;

            for (BorrowingRecord record :
                    tool.getBorrowingHistory()) {

                H2 recordTitle = new H2(
                    "Record " + number
                );

                recordTitle.getStyle()
                    .set("font-size", "15px")
                    .set("color", "#087BFA");

                details.add(
                    recordTitle,

                    createDetail(
                        "Borrowed By",
                        record.getBorrower()
                    ),

                    createDetail(
                        "Date Borrowed",
                        record.getBorrowedAt().format(formatter)
                    )
                );

                if (record.getReturnedAt() != null) {

                    details.add(
                        createDetail(
                            "Returned By",
                            record.getReturnedBy()
                        ),

                        createDetail(
                            "Date Returned",
                            record.getReturnedAt().format(formatter)
                        )
                    );

                } else {

                    details.add(
                        createDetail(
                            "Return Status",
                            "Not yet returned"
                        )
                    );
                }

                details.add(
                    new Span("------------------------------")
                );

                number++;
            }
        }

        // CLOSE BUTTON

        Button close = new Button(
            "Close",
            e -> dialog.close()
        );

        close.getStyle()
            .set("background", "#087BFA")
            .set("color", "white");

        dialog.add(details);

        dialog.getFooter().add(close);

        dialog.open();
    }

    // DETAIL ROW

    private static Span createDetail(
        String label,
        String value
    ) {

        Span detail = new Span(
            label + ": " + (value == null ? "" : value)
        );

        detail.getStyle()
            .set("color", "#102744")
            .set("font-size", "14px");

        return detail;
    }
}
