package com.dqt.hotel.elk;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface EmployeeRepositoryELK extends ElasticsearchRepository<Employee, Integer> {
}
