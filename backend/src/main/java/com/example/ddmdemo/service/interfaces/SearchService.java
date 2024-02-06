package com.example.ddmdemo.service.interfaces;

import com.example.ddmdemo.dto.SearchDTO;

import java.util.List;

import com.example.ddmdemo.indexmodel.IndexUnit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface SearchService {

    Page<IndexUnit> search(SearchDTO searchDTO, Pageable pageable);
}
