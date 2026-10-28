package main.java.com.prestaya.model;

public sealed  interface ApplicationStatus permits ApprovedStatus, RejectedStatus {
    
}
