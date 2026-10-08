package com.tq.hospitalequipmenttracking.model;

// state machine
// I included additional statuses like RESERVED and IN_TRANSIT for future extensibility,
// but my current state machine focuses only on the core lifecycle:
// usage, cleaning, and maintenance.
public enum EquipmentStatus {
    // Core lifecycle
    AVAILABLE,
    IN_USE,
    DIRTY,
    IN_CLEANING,
    STERILE,

    // Maintenance
    UNDER_MAINTENANCE,

    // Extended states (not in current state machine)
    RESERVED,
    IN_TRANSIT,
    OUT_OF_SERVICE,
    LOST,
}