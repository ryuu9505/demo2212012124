package com.example.demo5new.service.impl;

import com.example.demo5new.domain.Resources;
import com.example.demo5new.repository.ResourcesRepository;
import com.example.demo5new.service.ResourcesService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("resourcesService")
@Transactional
@RequiredArgsConstructor
public class ResourcesServiceImpl implements ResourcesService {

    private final ResourcesRepository ResourcesRepository;

    @Override
    public Resources getResources(long id) {
        return ResourcesRepository.findById(id).orElse(new Resources());
    }

    @Override
    public List<Resources> getResources() {
        return ResourcesRepository.findAll(Sort.by(Sort.Order.asc("orderNum")));
    }

    @Override
    public void createResources(Resources resources){
        ResourcesRepository.save(resources);
    }

    @Override
    public void deleteResources(long id) {
        ResourcesRepository.deleteById(id);
    }

}