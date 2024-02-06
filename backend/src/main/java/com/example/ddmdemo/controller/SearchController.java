package com.example.ddmdemo.controller;

import com.example.ddmdemo.dto.SearchDTO;
import com.example.ddmdemo.dto.SearchQueryDTO;
import com.example.ddmdemo.dto.TestClassDTO;
import com.example.ddmdemo.indexmodel.IndexUnit;
import com.example.ddmdemo.service.interfaces.SearchService;
import com.example.ddmdemo.utils.GeoSearchAPI;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @PutMapping
    public String test(@RequestBody TestClassDTO testclass){
        var geo = GeoSearchAPI.getGeoLocation(testclass.getAddress());
        return geo.getLat() + " " + geo.getLon();
    }

    @PostMapping
    public Page<IndexUnit> search(@RequestBody SearchDTO searchDTO, Pageable pageable) {
        return searchService.search(searchDTO, pageable);
    }

}
