package com.dqt.hotel.elk;

import com.dqt.hotel.dto.response.ResponseMessage;
import com.dqt.hotel.utils.ObjectMapperUtil;
import com.dqt.hotel.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepositoryELK employeeRepositoryELK;
    private final ObjectMapperUtil objectMapperUtil;
    private final ElasticsearchOperations elasticsearchOperations;

    public Object findEmployeesByBoolQuery(String key) {
        List<Employee> collect;
//        Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "lastName"));
        Pageable pageable = PageRequest.of(0, 1000);

        Query query;
        Query query1 = null;
        if (Strings.isNotBlank(key)) {
            query1 = new NativeQueryBuilder().withQuery(q ->
                    q.multiMatch(m -> m.fields(List.of("firstName", "lastName")).query(key))).build();

            query = NativeQuery.builder()
                    .withQuery(q -> q
                                    .matchBoolPrefix(m -> m
                                            .field("firstName")
                                            .query(key)
                                    )
//                    ).withQuery(q -> q
//                            .matchBoolPrefix(m -> m
//                                    .field("lastName")
//                                    .query(key)
//                            )
                    ).withPageable(pageable)
//                .build().addSort(Sort.by("title").descending());
                    .build();
        } else {
            query = Query.findAll().setPageable(pageable);
        }

        SearchHits<Employee> searchHits = elasticsearchOperations.search(query, Employee.class);
        SearchHits<Employee> searchHits1 = elasticsearchOperations.search(query1, Employee.class);
        collect = searchHits.stream().parallel().map(SearchHit::getContent).collect(Collectors.toList());
//            collect = bookELKRepository.findAll(pageable).getContent();

        long count = elasticsearchOperations.count(query, Employee.class);

        Map<String, Object> map = Utils.putData(1, 100000, count, collect);

        return map;
    }


//    public List<Employee> findEmployeesByBoolQuery(String key) {
//        //Bool Query
//        QueryBuilder boolQuery = QueryBuilders.boolQuery()
//                .must(QueryBuilders.termQuery("category", category))
//                .should(QueryBuilders.rangeQuery("price").lt(minPrice))
//                .should(QueryBuilders.matchQuery("inStock", inStock));
//
//        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
//                .withQuery(boolQuery).build();
//        SearchHits<Employee> searchHits = elasticsearchRestTemplate
//                .search(nativeSearchQuery, Employee.class);
//        return searchHits.stream().map(SearchHit::getContent).collect(Collectors.toList());
//    }


    public ResponseMessage add(EmployeeRequest request) {
        Employee book = objectMapperUtil.mapObject(request, Employee.class);
        Employee save = employeeRepositoryELK.save(book);
        return ResponseMessage.ok("Add Oke", save);
    }

    public ResponseMessage getById(Integer id) {
        Optional<Employee> optional = employeeRepositoryELK.findById(id);
        if (optional.isPresent()) {
            return ResponseMessage.ok("Get Oke", optional.get());
        }
        return ResponseMessage.error("Employee Not Found");
    }


    public ResponseMessage getAll() {
        Iterable<Employee> all = employeeRepositoryELK.findAll();
        return ResponseMessage.ok("Get Oke", all);
    }

    public ResponseMessage addList(List<EmployeeRequest> list) {
        List<Employee> listToAdd = new ArrayList<>();
        list.forEach(request -> {
            Employee book = objectMapperUtil.mapObject(request, Employee.class);
            listToAdd.add(book);
        });
        employeeRepositoryELK.saveAll(listToAdd);
        return ResponseMessage.ok("Add Oke");
    }


//    public ResponseMessage searchBooks(String key) {
////        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
////        if (key != null) {
////            boolQuery.must(matchQuery("name", key));
////        }
////
////        TermQueryBuilder name = QueryBuilders.termQuery("name", key);
////
////
////        Query query = NativeQuery.builder()
////                .withQuery(QueryBuilders.termQuery("name", key))
////                .build();
//
//        Query query = new NativeSearchQueryBuilder()
//                .withQuery(QueryBuilders.boolQuery()
//                        .filter(QueryBuilders.termsQuery("name", key)))
////                .addAggregation(AggregationBuilders
////                        .terms(Constants.ELSConstant.GROUP_BY_CAMPAIGN_ID).field(Constants.ELSConstant.CAMPAIGN_ID_FIELD)
////                        .size(campaignIds.size())
////                        .subAggregation(AggregationBuilders.terms(Constants.ELSConstant.GROUP_BY_STATUS)
////                                .field(Constants.ELSConstant.STATUS_FIELD)
////                                .size(statuses.size())))
//                .build();
//
////        NativeQuery query = new NativeQueryBuilder()
////                .withQuery(QueryBuilders.boolQuery(termQuery("name", key)))
////                .build();
////
////        NativeSearchQuery query = new NativeSearchQueryBuilder()
////                .withQuery(matchQuery("name", key))
////                .build();
//
//        SearchHits<Employee> search = elasticsearchOperations.search(query, Employee.class, IndexCoordinates.of("employees"));
//
////        Aggregation aggregation = AggregationBuilders.terms(ta -> ta.field("name").size(10000));
////
////        Query query = NativeQuery.builder()
////                .withQuery(q -> q.matchAll(ma -> ma))
////                .withAggregation(key, aggregation)
////                .build();
//
////        SearchHits<Employee> search = searchOperations.search(query, Employee.class);
//
//        List<Employee> employees = search.getSearchHits().stream().map(SearchHit::getContent).toList();
//
//        return ResponseMessage.ok("OK", employees);
//    }

}
