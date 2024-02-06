package com.example.ddmdemo.controller;

import com.example.ddmdemo.dto.SearchDTO;
import com.example.ddmdemo.dto.SearchQueryDTO;
import com.example.ddmdemo.indexmodel.IndexUnit;
import com.example.ddmdemo.service.interfaces.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @PostMapping
    public Page<IndexUnit> search(@RequestBody SearchDTO searchDTO, Pageable pageable) {
        return searchService.search(searchDTO, pageable);
    }

}
