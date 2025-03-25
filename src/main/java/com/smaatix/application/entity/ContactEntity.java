package com.smaatix.application.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "contactentity")
public class ContactEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int ContactId;
  @Column(name = "Name", nullable = false)
  private String Name;
  @Column(name = "Phone", nullable = false)
  private String Phone;
  @Column(name = "Email", nullable = false)
  private String Email;
  @Column(name = "Message", nullable = false)
  private String Message;


  public ContactEntity() {
  }

  public ContactEntity(int contactId, String name, String phone, String email, String message) {
    ContactId = contactId;
    Name = name;
    Phone = phone;
    Email = email;
    Message = message;
  }

  public ContactEntity(int contactId) {
    ContactId = contactId;
  }

  public int getContactId() {
    return ContactId;
  }

  public void setContactId(int contactId) {
    ContactId = contactId;
  }

  public String getName() {
    return Name;
  }

  public void setName(String name) {
    Name = name;
  }

  public String getPhone() {
    return Phone;
  }

  public void setPhone(String phone) {
    Phone = phone;
  }

  public String getEmail() {
    return Email;
  }

  public void setEmail(String email) {
    Email = email;
  }

  public String getMessage() {
    return Message;
  }

  public void setMessage(String message) {
    Message = message;
  }
}
