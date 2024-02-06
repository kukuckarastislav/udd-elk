package com.example.ddmdemo.indexrepository;

import com.example.ddmdemo.indexmodel.IndexUnit;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IndexRepository extends ElasticsearchRepository<IndexUnit, String> {
}
