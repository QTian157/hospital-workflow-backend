package com.tq.hospitalequipmenttracking.validation;

import com.tq.hospitalequipmenttracking.dto.request.EquipmentSearchRequest;
import com.tq.hospitalequipmenttracking.exception.BadRequestException;

import java.util.List;

public class EquipmentSearchRequestValidator {

    public static void validate(EquipmentSearchRequest request) {

        if (request.getPage() < 0) {
            throw new BadRequestException("Page number cannot be negative.");
        }

        if (request.getSize() <= 0 || request.getSize() > 50) {
            throw new BadRequestException("Page size must be between 1 and 50.");
        }

        List<String> allowedSortFields = List.of(
                "id",
                "name",
                "status",
                "type",
                "category",
                "assetTag",
                "serialNumber"
        );

        if (!allowedSortFields.contains(request.getSortBy())) {
            throw new BadRequestException("Invalid sort field: " + request.getSortBy());
        }

        if (!request.getSortDir().equalsIgnoreCase("asc")
                && !request.getSortDir().equalsIgnoreCase("desc")) {
            throw new BadRequestException("Sort direction must be either asc or desc.");
        }
    }
}