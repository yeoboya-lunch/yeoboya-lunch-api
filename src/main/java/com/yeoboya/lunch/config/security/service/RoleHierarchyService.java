package com.yeoboya.lunch.config.security.service;

import com.yeoboya.lunch.config.security.domain.RoleHierarchy;
import com.yeoboya.lunch.config.security.repository.RoleHierarchyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleHierarchyService {

    private final RoleHierarchyRepository roleHierarchyRepository;

    @Transactional
    public String findAllHierarchy() {

        List<RoleHierarchy> rolesHierarchy = roleHierarchyRepository.findAll();

        Iterator<RoleHierarchy> itr = rolesHierarchy.iterator();
        StringBuilder concatenatedRoles = new StringBuilder();
        while (itr.hasNext()) {
            RoleHierarchy model = itr.next();
            if (model.getParentName() != null) {
                concatenatedRoles.append(model.getParentName().getChildName());
                concatenatedRoles.append(" > ");
                concatenatedRoles.append(model.getChildName());
                concatenatedRoles.append("\n");
            }
        }
        return concatenatedRoles.toString();

    }
}
