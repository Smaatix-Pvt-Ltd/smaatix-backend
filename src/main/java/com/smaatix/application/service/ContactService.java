package com.smaatix.application.service;

import com.smaatix.application.entity.ContactEntity;
import com.smaatix.application.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ContactService {

  @Autowired
  private ContactRepository contactRepository;

  public ContactEntity updateContact(int contactId, ContactEntity updatedContact) {
    Optional<ContactEntity> existingContactOptional = contactRepository.findById(contactId);

    if (existingContactOptional.isPresent()) {
      ContactEntity existingContact = existingContactOptional.get();
      existingContact.setName(updatedContact.getName());
      existingContact.setPhone(updatedContact.getPhone());
      existingContact.setEmail(updatedContact.getEmail());
      existingContact.setMessage(updatedContact.getMessage());

      return contactRepository.save(existingContact);
    } else {
      throw new RuntimeException("Contact not found with id: " + contactId);
    }
  }

  public ContactEntity createContact(ContactEntity contact) {
    return contactRepository.save(contact);
  }
  }