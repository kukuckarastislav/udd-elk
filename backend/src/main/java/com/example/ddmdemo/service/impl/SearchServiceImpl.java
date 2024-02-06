package com.example.ddmdemo.service.impl;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.example.ddmdemo.dto.SearchDTO;
import com.example.ddmdemo.exceptionhandling.exception.MalformedQueryException;
import com.example.ddmdemo.indexmodel.DummyIndex;
import com.example.ddmdemo.indexmodel.IndexUnit;
import com.example.ddmdemo.service.interfaces.SearchService;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.highlight.HighlightBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHitSupport;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
@RequiredArgsConstructor
@ComponentScan
public class SearchServiceImpl implements SearchService {

    private final ElasticsearchOperations elasticsearchTemplate;
    private final RestHighLevelClient elasticsearchClient;

    @Override
    public Page<DummyIndex> simpleSearch(List<String> keywords, Pageable pageable) {
        var searchQueryBuilder =
            new NativeQueryBuilder().withQuery(buildSimpleSearchQuery(keywords))
                .withPageable(pageable);

        return runQuery(searchQueryBuilder.build());
    }

    @Override
    public Page<DummyIndex> advancedSearch(List<String> expression, Pageable pageable) {
        if (expression.size() != 3) {
            throw new MalformedQueryException("Search query malformed.");
        }

        String operation = expression.get(1);
        expression.remove(1);
        var searchQueryBuilder =
            new NativeQueryBuilder().withQuery(buildAdvancedSearchQuery(expression, operation))
                .withPageable(pageable);

        return runQuery(searchQueryBuilder.build());
    }

    @Override
    public Page<IndexUnit> search(SearchDTO searchDTO, Pageable pageable) {

        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        if (searchDTO.getTypeOfSearch().equals("standard_search")) {
            if (searchDTO.isContractDoc()) {
                // Add specific conditions for contract documents
                if (!searchDTO.getEmployeeName().isEmpty()) {
                    queryBuilder.must(QueryBuilders.matchQuery("employeeName", searchDTO.getEmployeeName())
                            .fuzziness(Fuzziness.ONE));
                }
                if (!searchDTO.getEmployeeSurname().isEmpty()) {
                    queryBuilder.must(QueryBuilders.matchQuery("employerSurname", searchDTO.getEmployeeSurname())
                            .fuzziness(Fuzziness.ONE));
                }
                if (!searchDTO.getGovernmentName().isEmpty()) {
                    queryBuilder.must(QueryBuilders.matchQuery("governmentName", searchDTO.getGovernmentName()));
                }
                if (!searchDTO.getGovernmentLevel().isEmpty() && !searchDTO.getGovernmentLevel().equals("ALL")) {
                    queryBuilder.must(QueryBuilders.matchQuery("governmentLevel", searchDTO.getGovernmentLevel()));
                }
                if (!searchDTO.getFullText().isEmpty()) {
                    queryBuilder.must(QueryBuilders.matchQuery("contractText", searchDTO.getFullText()));
                }
                // Add other conditions as needed
            } else {
                // Add conditions for non-contract documents
                if (!searchDTO.getFullText().isEmpty()) {
                    queryBuilder.must(QueryBuilders.matchQuery("lawText", searchDTO.getFullText()));
                }
                // Add other conditions as needed
            }

            if(searchDTO.isContractDoc() && !searchDTO.isLawDoc()){
                queryBuilder.must(QueryBuilders.matchQuery("typeOfDoc", "CONTRACT"));
            }
            if(!searchDTO.isContractDoc() && searchDTO.isLawDoc()){
                queryBuilder.must(QueryBuilders.matchQuery("typeOfDoc", "LAW"));
            }

        }else if(searchDTO.getTypeOfSearch().equals("boolean_query")) {
            queryBuilder.must(QueryBuilders.matchQuery("lawText", searchDTO.getFullText()));
            queryBuilder.must(QueryBuilders.matchQuery("contractText", searchDTO.getFullText()));
        }

        // Create the highlighter configuration
        HighlightBuilder highlightBuilder = new HighlightBuilder()
                .preTags("<highlight>")
                .postTags("</highlight>")
                .field("contractText") // Adjust the field name as needed
                .field("lawText");

        // Create the search request
        SearchRequest searchRequest = new SearchRequest(IndexUnit.INDEX_NAME);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder()
                .query(queryBuilder)
                .highlight(highlightBuilder)
                .from((int) pageable.getOffset())
                .size(pageable.getPageSize());

        searchRequest.source(searchSourceBuilder);

        // Execute the search
        //SearchResponse searchResponse = elasticsearchClient.search(searchRequest, RequestOptions.DEFAULT);

        var searchHits = elasticsearchTemplate.search((org.springframework.data.elasticsearch.core.query.Query) searchRequest, IndexUnit.class, IndexCoordinates.of(IndexUnit.INDEX_NAME));
        var searchHitsPaged = SearchHitSupport.searchPageFor(searchHits, pageable);
        return (Page<IndexUnit>) SearchHitSupport.unwrapSearchHits(searchHitsPaged);


        /*
        SearchResponse searchResponse = null;
        try {
            searchResponse = elasticsearchClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // Process the search hits and retrieve highlighted fragments
        List<IndexUnit> results = new ArrayList<>();
        for (SearchHit hit : searchResponse.getHits().getHits()) {
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            HighlightField highlightContract = highlightFields.get("contractText"); // Adjust the field name
            HighlightField highlightLaw = highlightFields.get("lawText"); // Adjust the field name
            String highlightedContractText = highlightContract.fragments()[0].string();
            String highlightedLawText = highlightLaw.fragments()[0].string();
            // Create an IndexUnit object with the highlighted text and other relevant data

            IndexUnit indexUnit = new IndexUnit();
            indexUnit.setId(hit.getId());
            if(hit.getSource().get("employeeName") != null){
                indexUnit.setEmployeeName((String) hit.getSource().get("employeeName"));
            }
            if(hit.getSource().get("employerSurname") != null){
                indexUnit.setEmployerSurname((String) hit.getSource().get("employerSurname"));
            }
            if(hit.getSource().get("governmentName") != null){
                indexUnit.setGovernmentName((String) hit.getSource().get("governmentName"));
            }
            if(hit.getSource().get("governmentLevel") != null){
                indexUnit.setGovernmentLevel((String) hit.getSource().get("governmentLevel"));
            }
            if(hit.getSource().get("contractText") != null){
                indexUnit.setContractText((String) hit.getSource().get("contractText"));
            }
            if(hit.getSource().get("lawText") != null){
                indexUnit.setLawText((String) hit.getSource().get("lawText"));
            }
            if(hit.getSource().get("fileId") != null){
                indexUnit.setFileId((String) hit.getSource().get("fileId"));
            }
            if(hit.getSource().get("typeOfDoc") != null){
                var typeOfDocStr = (String) hit.getSource().get("typeOfDoc");
                if(typeOfDocStr.equals("CONTRACT")) {
                    indexUnit.setTypeOfDoc(TypeOfDoc.CONTRACT);
                }
                if(typeOfDocStr.equals("LAW")){
                    indexUnit.setTypeOfDoc(TypeOfDoc.LAW);
                }
            }
            if(hit.getSource().get("title") != null){
                indexUnit.setTitle((String) hit.getSource().get("title"));
            }
            if(hit.getSource().get("address") != null){
                indexUnit.setAddress((String) hit.getSource().get("address"));
            }

            results.add(indexUnit);
            // Add it to the results list
        }

        // Return the paginated results
        return new PageImpl<>(results, pageable, searchResponse.getHits().getTotalHits());

         */
    }


    private Query buildSimpleSearchQuery(List<String> tokens) {
        return BoolQuery.of(q -> q.must(mb -> mb.bool(b -> {
            tokens.forEach(token -> {
                b.should(sb -> sb.match(m -> m.field("title").fuzziness(Fuzziness.ONE.asString()).query(token)));
                b.should(sb -> sb.match(m -> m.field("content_sr").query(token)));
                b.should(sb -> sb.match(m -> m.field("content_en").query(token)));
                // ok sada radi :D
            });
            return b;
        })))._toQuery();
    }

    private Query buildAdvancedSearchQuery(List<String> operands, String operation) {
        return BoolQuery.of(q -> q.must(mb -> mb.bool(b -> {
            var field1 = operands.get(0).split(":")[0];
            var value1 = operands.get(0).split(":")[1];
            var field2 = operands.get(1).split(":")[0];
            var value2 = operands.get(1).split(":")[1];

            switch (operation) {
                case "AND":
                    b.must(sb -> sb.match(
                        m -> m.field(field1).fuzziness(Fuzziness.ONE.asString()).query(value1)));
                    b.must(sb -> sb.match(m -> m.field(field2).query(value2)));
                    break;
                case "OR":
                    b.should(sb -> sb.match(
                        m -> m.field(field1).fuzziness(Fuzziness.ONE.asString()).query(value1)));
                    b.should(sb -> sb.match(m -> m.field(field2).query(value2)));
                    break;
                case "NOT":
                    b.must(sb -> sb.match(
                        m -> m.field(field1).fuzziness(Fuzziness.ONE.asString()).query(value1)));
                    b.mustNot(sb -> sb.match(m -> m.field(field2).query(value2)));
                    break;
            }

            return b;
        })))._toQuery();
    }

    private Page<IndexUnit> runQuery1(NativeQuery searchQuery) {

        var searchHits = elasticsearchTemplate.search(searchQuery, IndexUnit.class,
                IndexCoordinates.of("contracts_and_laws_index"));

        var searchHitsPaged = SearchHitSupport.searchPageFor(searchHits, searchQuery.getPageable());

        return (Page<IndexUnit>) SearchHitSupport.unwrapSearchHits(searchHitsPaged);
    }

    private Page<DummyIndex> runQuery(NativeQuery searchQuery) {

        var searchHits = elasticsearchTemplate.search(searchQuery, DummyIndex.class,
            IndexCoordinates.of("dummy_index"));

        var searchHitsPaged = SearchHitSupport.searchPageFor(searchHits, searchQuery.getPageable());

        return (Page<DummyIndex>) SearchHitSupport.unwrapSearchHits(searchHitsPaged);
    }
}
