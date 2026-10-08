package com.tq.hospitalequipmenttracking.spec;

import com.tq.hospitalequipmenttracking.dto.request.EquipmentSearchRequest;
import com.tq.hospitalequipmenttracking.model.Equipment;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.criteria.Predicate;

// I separated query construction logic into a Specification layer,
// so that the repository remains clean and focused on data access.
// 查询条件构造逻辑（query building）
// 不要放在repository： 数据库访问接口（JpaRepository）
public class EquipmentSpecification {
    public static Specification<Equipment> search(EquipmentSearchRequest request) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (request.getKeyword() != null && !request.getKeyword().isBlank()) {
                String keyword = "%" + request.getKeyword().toLowerCase() + "%";

                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("name")), keyword),
                        cb.like(cb.lower(root.get("assetTag")), keyword),
                        cb.like(cb.lower(root.get("serialNumber")), keyword)
                ));
            }

            if (request.getStatus() != null) {
                predicates.add(cb.equal(root.get("status"), request.getStatus()));
            }

            if (request.getType() != null) {
                predicates.add(cb.equal(root.get("type"), request.getType()));
            }

            if (request.getCategory() != null) {
                predicates.add(cb.equal(root.get("category"), request.getCategory()));
            }

            if (request.getDepartmentId() != null) {
                predicates.add(cb.equal(root.get("department").get("id"), request.getDepartmentId()));
            }

            if (request.getRoomId() != null) {
                predicates.add(cb.equal(root.get("currentRoom").get("id"), request.getRoomId()));
            }

            if (request.getMobile() != null) {
                predicates.add(cb.equal(root.get("mobile"), request.getMobile()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
