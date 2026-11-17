
package com.example;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Tool {

    private String id;
    private String name;
    private String condition;
    private String status;
    private String location;
    private String description;

    private String currentBorrower;
    private LocalDateTime borrowedAt;

    private List<BorrowingRecord> borrowingHistory =
        new ArrayList<>();

    public Tool(
        String id,
        String name,
        String condition,
        String status,
        String location,
        String description
    ) {
        this.id = id;
        this.name = name;
        this.condition = condition;
        this.status = status;
        this.location = location;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCondition() {
        return condition;
    }

    public String getStatus() {
        return status;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    public String getCurrentBorrower() {
        return currentBorrower;
    }

    public LocalDateTime getBorrowedAt() {
        return borrowedAt;
    }

    public List<BorrowingRecord> getBorrowingHistory() {
        return Collections.unmodifiableList(borrowingHistory);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // BORROW TOOL

    public boolean borrow(String borrower) {

        if (!"Available".equalsIgnoreCase(status)) {
            return false;
        }

        if (borrower == null || borrower.trim().isEmpty()) {
            return false;
        }

        currentBorrower = borrower.trim();
        borrowedAt = LocalDateTime.now();

        borrowingHistory.add(
            new BorrowingRecord(currentBorrower)
        );

        status = "Borrowed";

        return true;
    }

    // RETURN TOOL

    public boolean returnTool(String returnedBy) {

        if (!"Borrowed".equalsIgnoreCase(status)) {
            return false;
        }

        if (returnedBy == null || returnedBy.trim().isEmpty()) {
            return false;
        }

        if (!borrowingHistory.isEmpty()) {

            BorrowingRecord latestRecord =
                borrowingHistory.get(borrowingHistory.size() - 1);

            latestRecord.completeReturn(returnedBy.trim());
        }

        currentBorrower = null;
        borrowedAt = null;

        status = "Available";

        return true;
    }

    public abstract String getCategory();
}
