
package com.example;

import java.time.LocalDateTime;

public class BorrowingRecord {

    private String borrower;
    private String returnedBy;

    private LocalDateTime borrowedAt;
    private LocalDateTime returnedAt;

    public BorrowingRecord(String borrower) {

        this.borrower = borrower;
        this.borrowedAt = LocalDateTime.now();
    }

    public String getBorrower() {
        return borrower;
    }

    public String getReturnedBy() {
        return returnedBy;
    }

    public LocalDateTime getBorrowedAt() {
        return borrowedAt;
    }

    public LocalDateTime getReturnedAt() {
        return returnedAt;
    }

    public void completeReturn(String returnedBy) {

        this.returnedBy = returnedBy;
        this.returnedAt = LocalDateTime.now();
    }
}
