package com.yeoboya.lunch.config.security.service;

import com.yeoboya.lunch.config.security.domain.Resources;
import com.yeoboya.lunch.config.security.repository.ResourcesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class ResourcesService {

    private final ResourcesRepository resourcesRepository;

    @Transactional
    public Resources selectResources(long id) {
        return resourcesRepository.findById(id).orElse(new Resources());
    }

    @Transactional
    public List<Resources> selectResources() {
        return resourcesRepository.findAll();
    }

    @Transactional
    public void insertResources(Resources resources){
        resourcesRepository.save(resources);
    }

    @Transactional
    public void deleteResources(long id) {
        resourcesRepository.deleteById(id);
    }
}
