package com.example.ddmdemo.service.impl;

import co.elastic.clients.elasticsearch._types.GeoDistanceType;
import co.elastic.clients.elasticsearch._types.GeoLocation;
import co.elastic.clients.elasticsearch._types.LatLonGeoLocation;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.GeoDistanceQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.example.ddmdemo.dto.SearchDTO;
import com.example.ddmdemo.exceptionhandling.exception.MalformedQueryException;
import com.example.ddmdemo.indexmodel.IndexUnit;
import com.example.ddmdemo.service.interfaces.SearchService;

import java.util.List;

import com.example.ddmdemo.utils.GeoSearchAPI;
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
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {

    private final ElasticsearchOperations elasticsearchTemplate;

    private Page<IndexUnit> geoSearch(SearchDTO searchDTO, Pageable pageable){

        var geo = GeoSearchAPI.getGeoLocation(searchDTO.getAddress());

        var geoLocation = new GeoLocation.Builder().latlon(
                new LatLonGeoLocation.Builder().lon(geo.getLon()).lat(geo.getLat()).build()).build();

        var gs = GeoDistanceQuery.of(g -> {
            g.field("location")
                    .distance(+searchDTO.getRadius()+"km")
                    .distanceType(GeoDistanceType.Plane)
                    .location(geoLocation);
            return g;
        })._toQuery();

        var searchQueryBuilder = new NativeQueryBuilder()
                .withQuery(gs)
                .withPageable(pageable);
        return runQuery(searchQueryBuilder.build());
    }

    @Override
    public Page<IndexUnit> search(SearchDTO searchDTO, Pageable pageable) {

        if(searchDTO.getTypeOfSearch().equals("geo_search")){
            return geoSearch(searchDTO, pageable);
        }

        //Query query = BoolQuery.of(q -> q.must(mb -> mb.bool(b -> {
        Query query = BoolQuery.of(b -> {

            if(searchDTO.getTypeOfSearch().equals("standard_search")){


                if(searchDTO.isContractDoc() && searchDTO.isLawDoc()) {
                    // Oba dokumenta

                    b.should(xxx -> {

                        // LAW
                        xxx.bool(x -> {
                            if (!searchDTO.getFullText().isEmpty()) {
                                if (searchDTO.getFullText().contains("\"")) {
                                    // is necessary to remove quotes from string?
                                    x.must(sb -> sb.matchPhrase(m -> m.field("lawText").query(searchDTO.getFullText().replace("\"", ""))));
                                } else {
                                    x.must(sb -> sb.match(m -> m.field("lawText").query(searchDTO.getFullText())));
                                }
                            }

                            return x;
                        });
                        return xxx;
                    });

                    // CONTRACT
                    b.should(xxx -> {
                        xxx.bool(x -> {
                            if (!searchDTO.getEmployeeName().isEmpty()) {
                                if (searchDTO.getEmployeeName().contains("\"")) {
                                    // is necessary to remove quotes from string?
                                    searchDTO.setEmployeeName(searchDTO.getEmployeeName().replace("\"", ""));
                                    x.must(sb -> sb.matchPhrase(m -> m.field("employeeName").query(searchDTO.getEmployeeName())));
                                } else {
                                    x.must(sb -> sb.match(m -> m.field("employeeName").fuzziness(Fuzziness.ONE.asString()).query(searchDTO.getEmployeeName())));
                                }
                            }
                            if (!searchDTO.getEmployerSurname().isEmpty()) {
                                if (searchDTO.getEmployerSurname().contains("\"")) {
                                    // is necessary to remove quotes from string?
                                    searchDTO.setEmployerSurname(searchDTO.getEmployerSurname().replace("\"", ""));
                                    x.must(sb -> sb.matchPhrase(m -> m.field("employerSurname").query(searchDTO.getEmployerSurname())));
                                } else {
                                    x.must(sb -> sb.match(m -> m.field("employerSurname").fuzziness(Fuzziness.ONE.asString()).query(searchDTO.getEmployerSurname())));
                                }
                            }
                            if (!searchDTO.getGovernmentName().isEmpty()) {
                                if (searchDTO.getGovernmentName().contains("\"")) {
                                    // is necessary to remove quotes from string?
                                    searchDTO.setGovernmentName(searchDTO.getGovernmentName().replace("\"", ""));
                                    x.must(sb -> sb.matchPhrase(m -> m.field("governmentName").query(searchDTO.getGovernmentName())));
                                } else {
                                    x.must(sb -> sb.match(m -> m.field("governmentName").query(searchDTO.getGovernmentName())));
                                }
                            }
                            if (!searchDTO.getGovernmentLevel().isEmpty()) {
                                if (searchDTO.getGovernmentLevel().contains("\"")) {
                                    // is necessary to remove quotes from string?
                                    searchDTO.setGovernmentLevel(searchDTO.getGovernmentLevel().replace("\"", ""));
                                    x.must(sb -> sb.matchPhrase(m -> m.field("governmentLevel").query(searchDTO.getGovernmentLevel())));
                                } else {
                                    x.must(sb -> sb.match(m -> m.field("governmentLevel").query(searchDTO.getGovernmentLevel())));
                                }
                            }
                            if (!searchDTO.getFullText().isEmpty()) {
                                if (searchDTO.getFullText().contains("\"")) {
                                    // is necessary to remove quotes from string?
                                    x.must(sb -> sb.matchPhrase(m -> m.field("contractText").query(searchDTO.getFullText().replace("\"", ""))));
                                } else {
                                    x.must(sb -> sb.match(m -> m.field("contractText").query(searchDTO.getFullText())));
                                }
                            }

                            return x;
                        });

                        return xxx;
                    });



                }
                else if(searchDTO.isLawDoc()){
                    // Samo zakon
                    b.must(sb -> sb.match(m -> m.field("typeOfDoc").query("LAW")));
                    if(!searchDTO.getFullText().isEmpty()){
                        if(searchDTO.getFullText().contains("\"")){
                            // is necessary to remove quotes from string?
                            b.must(sb -> sb.matchPhrase(m -> m.field("lawText").query(searchDTO.getFullText().replace("\"", ""))));
                        }else{
                            b.must(sb -> sb.match(m -> m.field("lawText").query(searchDTO.getFullText())));
                        }
                    }

                }
                else if(searchDTO.isContractDoc()){
                    // Samo ugovor
                    b.must(sb -> sb.match(m -> m.field("typeOfDoc").query("CONTRACT")));
                    if(!searchDTO.getEmployeeName().isEmpty()){
                        if(searchDTO.getEmployeeName().contains("\"")){
                            // is necessary to remove quotes from string?
                            searchDTO.setEmployeeName(searchDTO.getEmployeeName().replace("\"", ""));
                            b.must(sb -> sb.matchPhrase(m -> m.field("employeeName").query(searchDTO.getEmployeeName())));
                        }else{
                            b.must(sb -> sb.match(m -> m.field("employeeName").fuzziness(Fuzziness.ONE.asString()).query(searchDTO.getEmployeeName())));
                        }
                    }
                    if(!searchDTO.getEmployerSurname().isEmpty()){
                        if(searchDTO.getEmployerSurname().contains("\"")){
                            // is necessary to remove quotes from string?
                            searchDTO.setEmployerSurname(searchDTO.getEmployerSurname().replace("\"", ""));
                            b.must(sb -> sb.matchPhrase(m -> m.field("employerSurname").query(searchDTO.getEmployerSurname())));
                        }else{
                            b.must(sb -> sb.match(m -> m.field("employerSurname").fuzziness(Fuzziness.ONE.asString()).query(searchDTO.getEmployerSurname())));
                        }
                    }
                    if(!searchDTO.getGovernmentName().isEmpty()){
                        if(searchDTO.getGovernmentName().contains("\"")){
                            // is necessary to remove quotes from string?
                            searchDTO.setGovernmentName(searchDTO.getGovernmentName().replace("\"", ""));
                            b.must(sb -> sb.matchPhrase(m -> m.field("governmentName").query(searchDTO.getGovernmentName())));
                        }else{
                            b.must(sb -> sb.match(m -> m.field("governmentName").query(searchDTO.getGovernmentName())));
                        }
                    }
                    if(!searchDTO.getGovernmentLevel().isEmpty()){
                        if(searchDTO.getGovernmentLevel().contains("\"")){
                            // is necessary to remove quotes from string?
                            searchDTO.setGovernmentLevel(searchDTO.getGovernmentLevel().replace("\"", ""));
                            b.must(sb -> sb.matchPhrase(m -> m.field("governmentLevel").query(searchDTO.getGovernmentLevel())));
                        }else{
                            b.must(sb -> sb.match(m -> m.field("governmentLevel").query(searchDTO.getGovernmentLevel())));
                        }
                    }
                    if(!searchDTO.getFullText().isEmpty()){
                        if(searchDTO.getFullText().contains("\"")){
                            // is necessary to remove quotes from string?
                            b.must(sb -> sb.matchPhrase(m -> m.field("contractText").query(searchDTO.getFullText().replace("\"", ""))));
                        }else{
                            b.must(sb -> sb.match(m -> m.field("contractText").query(searchDTO.getFullText())));
                        }
                    }
                }



            }else if(searchDTO.getTypeOfSearch().equals("boolean_query")){
                //b.must(sb -> sb.match(m -> m.field("lawText").query(searchDTO.getFullText())));
                //b.must(sb -> sb.match(m -> m.field("contractText").query(searchDTO.getFullText())));
                //TODO: implement complex boolean query

                // title: neki deo naslova AND content: neki deo sadrzaja OR name: Rastislav NOT surname: Pavlovic
                /*
                    title
                    :
                     neki deo naslova
                    AND
                     content
                    :
                     neki deo sadrzaja
                    OR
                     name
                    :
                     Rastislav
                    AND

                    NOT
                     surname
                    :
                     Pavlovic

                 */
                String[] tokens = searchDTO.getBooleanQuery().split("(?<=AND|OR|NOT|:)|(?=AND|OR|NOT|:)");

                boolean firstOperand = false;
                String generatedQuery = "";
                for(int i = 0; i < tokens.length; i++) {
                    tokens[i] = tokens[i].trim();
                }
                for(int i = 0; i < tokens.length; i++){
                    tokens[i] = tokens[i].trim();

                    switch (tokens[i]) {
                        case "AND":
                            if(i+2 >= tokens.length || tokens[i+2].equals("NOT")){
                                break;
                            }
                            if(!firstOperand){
                                int finalI = i;
                                if(finalI-3 >= 0){
                                    b.must(sb -> sb.match(m -> m.field(tokens[finalI-3]).query(tokens[finalI-1])));
                                    generatedQuery += tokens[finalI-3] + ": " + tokens[finalI-1];
                                }
                                if(finalI+3 < tokens.length){
                                    b.must(sb -> sb.match(m -> m.field(tokens[finalI+1]).query(tokens[finalI+3])));
                                    generatedQuery += " AND " + tokens[finalI+1] + ": " + tokens[finalI+3];
                                }
                                firstOperand = true;
                            }else{
                                int finalI = i;
                                if(finalI+3 < tokens.length){
                                    b.must(sb -> sb.match(m -> m.field(tokens[finalI+1]).query(tokens[finalI+3])));
                                    generatedQuery += " AND " + tokens[finalI+1] + ": " + tokens[finalI+3];
                                }
                            }
                            break;
                        case "OR":
                            if(i+2 >= tokens.length || tokens[i+2].equals("NOT")){
                                break;
                            }
                            if(!firstOperand){
                                int finalI = i;
                                if(finalI-3 >= 0) {
                                    b.should(sb -> sb.match(m -> m.field(tokens[finalI-3]).query(tokens[finalI-1])));
                                    generatedQuery += tokens[finalI-3] + ": " + tokens[finalI-1];
                                }
                                if(finalI+3 < tokens.length){
                                    b.should(sb -> sb.match(m -> m.field(tokens[finalI+1]).query(tokens[finalI+3])));
                                    generatedQuery += " OR " + tokens[finalI+1] + ": " + tokens[finalI+3];
                                }
                                firstOperand = true;
                            }else{
                                int finalI = i;
                                if(finalI+3 < tokens.length){
                                    b.should(sb -> sb.match(m -> m.field(tokens[finalI+1]).query(tokens[finalI+3])));
                                    generatedQuery += " OR " + tokens[finalI+1] + ": " + tokens[finalI+3];
                                }
                            }
                            break;
                        case "NOT":
                            if(!firstOperand){
                                firstOperand = true;
                            }
                            int finalI = i;
                            b.mustNot(sb -> sb.match(m -> m.field(tokens[finalI+1]).query(tokens[finalI+3])));
                            generatedQuery += " NOT " + tokens[finalI+1] + ": " + tokens[finalI+3];
                            break;
                        case ":":

                            break;
                        default:
                            break;
                    }
                }

                System.out.println("//////////////////// ORIGINAL query: " + searchDTO.getBooleanQuery());
                System.out.println("//////////////////// GENERATED query: " + generatedQuery);


            }

            return b;
        })._toQuery();

        System.out.println("//////////////////// QUERY BY ELASTIC: " + query.toString());


        var searchQueryBuilder = new NativeQueryBuilder()
                .withQuery(query)
                .withPageable(pageable);
        return runQuery(searchQueryBuilder.build());
    }

    private Page<IndexUnit> runQuery(NativeQuery searchQuery) {

        var searchHits = elasticsearchTemplate.search(searchQuery, IndexUnit.class,
                IndexCoordinates.of("contracts_and_laws_index"));

        var searchHitsPaged = SearchHitSupport.searchPageFor(searchHits, searchQuery.getPageable());

        return (Page<IndexUnit>) SearchHitSupport.unwrapSearchHits(searchHitsPaged);
    }

}
