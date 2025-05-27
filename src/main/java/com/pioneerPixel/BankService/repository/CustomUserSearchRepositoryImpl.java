package com.pioneerPixel.BankService.repository;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.DateRangeQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import com.pioneerPixel.BankService.entity.UserData;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CustomUserSearchRepositoryImpl implements CustomUserSearchRepository {

    private final ElasticsearchOperations elasticsearchOperations;

    @Override
    public Page<UserData> searchUsers(String name,
                                      String email,
                                      String phone,
                                      LocalDate dateOfBirth,
                                      Pageable pageable) {

        NativeQueryBuilder queryBuilder = new NativeQueryBuilder()
                .withPageable(pageable);
        BoolQuery.Builder boolQueryBuilder = new BoolQuery.Builder();

        if (StringUtils.hasText(name)) {
            boolQueryBuilder.should(QueryBuilders.match(m -> m.field("name").query(name)));
        }

        if (StringUtils.hasText(email)) {
            boolQueryBuilder.should(QueryBuilders.match(t -> t.field("emails").query(email)));
        }

        if (StringUtils.hasText(phone)) {
            boolQueryBuilder.should(QueryBuilders.match(t -> t.field("phones").query(phone)));
        }

        if (dateOfBirth != null) {
            boolQueryBuilder.should(QueryBuilders.range(r -> r
                    .date(new DateRangeQuery.Builder().field("dateOfBirth")
                            .gt(dateOfBirth.format(DateTimeFormatter.ISO_DATE)).build())));
        }

        queryBuilder.withQuery(new Query(boolQueryBuilder.build()));

        SearchHits<UserData> searchHits = elasticsearchOperations.search(
                queryBuilder.build(),
                UserData.class
        );

        List<UserData> content = searchHits.stream()
                .map(SearchHit::getContent)
                .map(this::convertToUserData)
                .toList();

        return new PageImpl<>(
                content,
                pageable,
                searchHits.getTotalHits()
        );
    }

    private UserData convertToUserData(UserData data) {
        return UserData.builder()
                .id(data.getId())
                .name(data.getName())
                .emails(data.getEmails())
                .phones(data.getPhones())
                .dateOfBirth(data.getDateOfBirth())
                .build();
    }
}
