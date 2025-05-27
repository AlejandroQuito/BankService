package com.pioneerPixel.BankService.repository;

import com.pioneerPixel.BankService.entity.UserData;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface UserSearchRepository extends ElasticsearchRepository<UserData, String>, CustomUserSearchRepository {
}

