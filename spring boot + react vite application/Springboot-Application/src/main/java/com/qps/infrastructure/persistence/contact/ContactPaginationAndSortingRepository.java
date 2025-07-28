package com.qps.infrastructure.persistence.contact;

import com.qps.domain.contact.model.Contact;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactPaginationAndSortingRepository extends PagingAndSortingRepository<Contact, String> {
}
