package com.example.ddmdemo.service.impl;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.example.ddmdemo.dto.SearchDTO;
import com.example.ddmdemo.exceptionhandling.exception.MalformedQueryException;
import com.example.ddmdemo.indexmodel.DummyIndex;
import com.example.ddmdemo.indexmodel.IndexUnit;
import com.example.ddmdemo.service.interfaces.SearchService;
import org.elasticsearch.search.highlight.HighlightBuilder;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.common.unit.Fuzziness;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHitSupport;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {

    private final ElasticsearchOperations elasticsearchTemplate;

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

        Query query = BoolQuery.of(q -> q.must(mb -> mb.bool(b -> {

            if(searchDTO.getTypeOfSearch().equals("standard_search")){
                if(searchDTO.isContractDoc()){
                    if(!searchDTO.getEmployeeName().isEmpty()){
                        b.must(sb -> sb.match(m -> m.field("employeeName").fuzziness(Fuzziness.ONE.asString()).query(searchDTO.getEmployeeName())));
                    }
                    if(!searchDTO.getEmployeeSurname().isEmpty()){
                        b.must(sb -> sb.match(m -> m.field("employerSurname").fuzziness(Fuzziness.ONE.asString()).query(searchDTO.getEmployeeSurname())));
                    }
                    if(!searchDTO.getGovernmentName().isEmpty()){
                        b.must(sb -> sb.match(m -> m.field("governmentName").query(searchDTO.getGovernmentName())));
                    }
                    if(!searchDTO.getGovernmentLevel().isEmpty()){
                        b.must(sb -> sb.match(m -> m.field("governmentLevel").query(searchDTO.getGovernmentLevel())));
                    }
                    if(!searchDTO.getFullText().isEmpty()){
                        b.must(sb -> sb.match(m -> m.field("contractText").query(searchDTO.getFullText())));
                    }
                }else{
                    if(!searchDTO.getFullText().isEmpty()){
                        b.must(sb -> sb.match(m -> m.field("lawText").query(searchDTO.getFullText())));
                    }
                }

                if(searchDTO.isContractDoc() && !searchDTO.isLawDoc()){
                    b.must(sb -> sb.match(m -> m.field("typeOfDoc").query("CONTRACT")));
                }
                if(!searchDTO.isContractDoc() && searchDTO.isLawDoc()){
                    b.must(sb -> sb.match(m -> m.field("typeOfDoc").query("LAW")));
                }

            }else if(searchDTO.getTypeOfSearch().equals("boolean_query")){
                b.must(sb -> sb.match(m -> m.field("lawText").query(searchDTO.getFullText())));
                b.must(sb -> sb.match(m -> m.field("contractText").query(searchDTO.getFullText())));
                //TODO: implement complex boolean query
                /*
                // title: neki deo naslova AND content: neki deo sadrzaja OR name: Rastislav NOT surname: Pavlovic
                String[] tokens = searchDTO.getBooleanQuery().split("(?<=AND|OR|NOT|:)|(?=AND|OR|NOT|:)");

                for(int i = 0; i < tokens.length; i++){
                    tokens[i] = tokens[i].trim();

                    switch (tokens[i]) {
                        case "AND":
                            b.must(sb -> sb.match(
                                    m -> m.field(field1).query(value1)));
                            b.must(sb -> sb.match(m -> m.field(field2).query(value2)));
                            break;
                        case "OR":

                            break;
                        case "NOT":

                            break;
                        case ":":

                            break;
                        default:
                            break;
                    }
                }

                */
            }

            return b;
        })))._toQuery();


        var searchQueryBuilder = new NativeQueryBuilder()
                .withQuery(query)
                .withPageable(pageable);
        return runQuery1(searchQueryBuilder.build());
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
