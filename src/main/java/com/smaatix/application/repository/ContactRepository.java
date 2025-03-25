package com.smaatix.application.repository;

import com.smaatix.application.entity.ContactEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

  @Repository
  public interface ContactRepository extends JpaRepository<ContactEntity, Integer> {
  }
