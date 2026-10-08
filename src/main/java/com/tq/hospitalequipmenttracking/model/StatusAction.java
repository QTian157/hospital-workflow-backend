package com.tq.hospitalequipmenttracking.model;

// Each status transition is tied to a domain action, which makes the system more traceable and easier to debug.
//I designed two workflows:
//1. A normal usage lifecycle (use → dirty → clean → sterile → available)
//2. A maintenance workflow that can interrupt from multiple states
public enum StatusAction {
    // operation process
    START_USE,              // AVAILABLE → IN_USE
    MARK_DIRTY,             // IN_USE → DIRTY
    START_CLEANING,         // DIRTY → IN_CLEANING
    MARK_STERILE,           // IN_CLEANING → STERILE
    RETURN_TO_AVAILABLE,    // STERILE → AVAILABLE

    // maintainess process
    SEND_TO_MAINTENANCE,    // 多状态 → UNDER_MAINTENANCE
    COMPLETE_MAINTENANCE    // UNDER_MAINTENANCE → AVAILABLE
}
