package com.yeoboya.lunch.config.security.service;

import com.yeoboya.lunch.config.security.domain.RoleHierarchy;
import com.yeoboya.lunch.config.security.repository.RoleHierarchyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleHierarchyService {

    private final RoleHierarchyRepository roleHierarchyRepository;

//    @Transactional
//    public String findAllHierarchy() {
//        List<RoleHierarchy> rolesHierarchy = roleHierarchyRepository.findAll();
//        StringBuilder concatenatedRoles = new StringBuilder();
//        for (RoleHierarchy roleHierarchy : rolesHierarchy) {
//            if (roleHierarchy.getParentName() != null) {
//                concatenatedRoles.append(roleHierarchy.getParentName().getChildName());
//                concatenatedRoles.append(" > ");
//                concatenatedRoles.append(roleHierarchy.getChildName());
//                concatenatedRoles.append(System.lineSeparator());
//            }
//        }
//        return concatenatedRoles.toString();
//    }

    @Transactional
    public String findAllHierarchy() {
        return roleHierarchyRepository.findAll().stream()
                .filter(roleHierarchy -> roleHierarchy.getParentName() != null)
                .map(this::convertToHierarchyString)
                .collect(Collectors.joining(System.lineSeparator()));
    }

    private String convertToHierarchyString(RoleHierarchy roleHierarchy) {
        return roleHierarchy.getParentName().getChildName()
                + " > "
                + roleHierarchy.getChildName();
    }
}
