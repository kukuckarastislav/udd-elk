package com.example.ddmdemo.service.interfaces;

import com.example.ddmdemo.dto.SearchDTO;
import com.example.ddmdemo.indexmodel.DummyIndex;
import java.util.List;

import com.example.ddmdemo.indexmodel.IndexUnit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface SearchService {

    Page<DummyIndex> simpleSearch(List<String> keywords, Pageable pageable);

    Page<DummyIndex> advancedSearch(List<String> expression, Pageable pageable);

    Page<IndexUnit> search(SearchDTO searchDTO, Pageable pageable);
}
