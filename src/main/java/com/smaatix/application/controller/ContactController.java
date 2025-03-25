package com.smaatix.application.controller;


import com.smaatix.application.entity.ContactEntity;
import com.smaatix.application.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contacts")
public class ContactController {

  @Autowired
  private ContactService contactService;
  @PostMapping("/add")
  public ResponseEntity<ContactEntity> createContact(@RequestBody ContactEntity contact) {
    ContactEntity savedContact = contactService.createContact(contact);
    return ResponseEntity.ok(savedContact);
  }

  @PutMapping("/{contactId}")
  public ResponseEntity<ContactEntity> updateContact(
    @PathVariable int contactId,
    @RequestBody ContactEntity updatedContact) {
    ContactEntity updatedEntity = contactService.updateContact(contactId, updatedContact);
    return ResponseEntity.ok(updatedEntity);
  }
}
