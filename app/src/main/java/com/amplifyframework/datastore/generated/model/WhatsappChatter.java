package com.amplifyframework.datastore.generated.model;

import com.amplifyframework.core.model.annotations.HasMany;
import com.amplifyframework.core.model.temporal.Temporal;

import java.util.List;
import java.util.UUID;
import java.util.Objects;

import androidx.core.util.ObjectsCompat;

import com.amplifyframework.core.model.Model;
import com.amplifyframework.core.model.annotations.Index;
import com.amplifyframework.core.model.annotations.ModelConfig;
import com.amplifyframework.core.model.annotations.ModelField;
import com.amplifyframework.core.model.query.predicate.QueryField;

import static com.amplifyframework.core.model.query.predicate.QueryField.field;

/** This is an auto generated class representing the WhatsappChatter type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "WhatsappChatters")
@Index(name = "undefined", fields = {"CreatedByUserAndNumber"})
public final class WhatsappChatter implements Model {
  public static final QueryField ID = field("WhatsappChatter", "id");
  public static final QueryField NAME = field("WhatsappChatter", "Name");
  public static final QueryField CHATID = field("WhatsappChatter", "Chatid");
  public static final QueryField CREATED_BY_USER_AND_NUMBER = field("WhatsappChatter", "CreatedByUserAndNumber");
  public static final QueryField PHOTO = field("WhatsappChatter", "Photo");
  public static final QueryField NUMBER = field("WhatsappChatter", "Number");
  public static final QueryField CREATED_BY_USER = field("WhatsappChatter", "CreatedByUser");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="String") String Name;
  private final @ModelField(targetType="String", isRequired = true) String Chatid;
  private final @ModelField(targetType="ID", isRequired = true) String CreatedByUserAndNumber;
  private final @ModelField(targetType="AWSURL") String Photo;
  private final @ModelField(targetType="String") String Number;
  private final @ModelField(targetType="WhatsappMessages") @HasMany(associatedWith = "chatter", type = WhatsappMessages.class) List<WhatsappMessages> messages = null;
  private final @ModelField(targetType="String", isRequired = true) String CreatedByUser;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  public String getId() {
      return id;
  }
  
  public String getName() {
      return Name;
  }
  
  public String getChatid() {
      return Chatid;
  }
  
  public String getCreatedByUserAndNumber() {
      return CreatedByUserAndNumber;
  }
  
  public String getPhoto() {
      return Photo;
  }
  
  public String getNumber() {
      return Number;
  }
  
  public List<WhatsappMessages> getMessages() {
      return messages;
  }
  
  public String getCreatedByUser() {
      return CreatedByUser;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private WhatsappChatter(String id, String Name, String Chatid, String CreatedByUserAndNumber, String Photo, String Number, String CreatedByUser) {
    this.id = id;
    this.Name = Name;
    this.Chatid = Chatid;
    this.CreatedByUserAndNumber = CreatedByUserAndNumber;
    this.Photo = Photo;
    this.Number = Number;
    this.CreatedByUser = CreatedByUser;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      WhatsappChatter whatsappChatter = (WhatsappChatter) obj;
      return ObjectsCompat.equals(getId(), whatsappChatter.getId()) &&
              ObjectsCompat.equals(getName(), whatsappChatter.getName()) &&
              ObjectsCompat.equals(getChatid(), whatsappChatter.getChatid()) &&
              ObjectsCompat.equals(getCreatedByUserAndNumber(), whatsappChatter.getCreatedByUserAndNumber()) &&
              ObjectsCompat.equals(getPhoto(), whatsappChatter.getPhoto()) &&
              ObjectsCompat.equals(getNumber(), whatsappChatter.getNumber()) &&
              ObjectsCompat.equals(getCreatedByUser(), whatsappChatter.getCreatedByUser()) &&
              ObjectsCompat.equals(getCreatedAt(), whatsappChatter.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), whatsappChatter.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getName())
      .append(getChatid())
      .append(getCreatedByUserAndNumber())
      .append(getPhoto())
      .append(getNumber())
      .append(getCreatedByUser())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("WhatsappChatter {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("Name=" + String.valueOf(getName()) + ", ")
      .append("Chatid=" + String.valueOf(getChatid()) + ", ")
      .append("CreatedByUserAndNumber=" + String.valueOf(getCreatedByUserAndNumber()) + ", ")
      .append("Photo=" + String.valueOf(getPhoto()) + ", ")
      .append("Number=" + String.valueOf(getNumber()) + ", ")
      .append("CreatedByUser=" + String.valueOf(getCreatedByUser()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()))
      .append("}")
      .toString();
  }
  
  public static ChatidStep builder() {
      return new Builder();
  }
  
  /** 
   * WARNING: This method should not be used to build an instance of this object for a CREATE mutation.
   * This is a convenience method to return an instance of the object with only its ID populated
   * to be used in the context of a parameter in a delete mutation or referencing a foreign key
   * in a relationship.
   * @param id the id of the existing item this instance will represent
   * @return an instance of this model with only ID populated
   */
  public static WhatsappChatter justId(String id) {
    return new WhatsappChatter(
      id,
      null,
      null,
      null,
      null,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      Name,
      Chatid,
      CreatedByUserAndNumber,
      Photo,
      Number,
      CreatedByUser);
  }
  public interface ChatidStep {
    CreatedByUserAndNumberStep chatid(String chatid);
  }
  

  public interface CreatedByUserAndNumberStep {
    CreatedByUserStep createdByUserAndNumber(String createdByUserAndNumber);
  }
  

  public interface CreatedByUserStep {
    BuildStep createdByUser(String createdByUser);
  }
  

  public interface BuildStep {
    WhatsappChatter build();
    BuildStep id(String id);
    BuildStep name(String name);
    BuildStep photo(String photo);
    BuildStep number(String number);
  }
  

  public static class Builder implements ChatidStep, CreatedByUserAndNumberStep, CreatedByUserStep, BuildStep {
    private String id;
    private String Chatid;
    private String CreatedByUserAndNumber;
    private String CreatedByUser;
    private String Name;
    private String Photo;
    private String Number;
    @Override
     public WhatsappChatter build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new WhatsappChatter(
          id,
          Name,
          Chatid,
          CreatedByUserAndNumber,
          Photo,
          Number,
          CreatedByUser);
    }
    
    @Override
     public CreatedByUserAndNumberStep chatid(String chatid) {
        Objects.requireNonNull(chatid);
        this.Chatid = chatid;
        return this;
    }
    
    @Override
     public CreatedByUserStep createdByUserAndNumber(String createdByUserAndNumber) {
        Objects.requireNonNull(createdByUserAndNumber);
        this.CreatedByUserAndNumber = createdByUserAndNumber;
        return this;
    }
    
    @Override
     public BuildStep createdByUser(String createdByUser) {
        Objects.requireNonNull(createdByUser);
        this.CreatedByUser = createdByUser;
        return this;
    }
    
    @Override
     public BuildStep name(String name) {
        this.Name = name;
        return this;
    }
    
    @Override
     public BuildStep photo(String photo) {
        this.Photo = photo;
        return this;
    }
    
    @Override
     public BuildStep number(String number) {
        this.Number = number;
        return this;
    }
    
    /** 
     * @param id id
     * @return Current Builder instance, for fluent method chaining
     */
    public BuildStep id(String id) {
        this.id = id;
        return this;
    }
  }
  

  public final class CopyOfBuilder extends Builder {
    private CopyOfBuilder(String id, String name, String chatid, String createdByUserAndNumber, String photo, String number, String createdByUser) {
      super.id(id);
      super.chatid(chatid)
        .createdByUserAndNumber(createdByUserAndNumber)
        .createdByUser(createdByUser)
        .name(name)
        .photo(photo)
        .number(number);
    }
    
    @Override
     public CopyOfBuilder chatid(String chatid) {
      return (CopyOfBuilder) super.chatid(chatid);
    }
    
    @Override
     public CopyOfBuilder createdByUserAndNumber(String createdByUserAndNumber) {
      return (CopyOfBuilder) super.createdByUserAndNumber(createdByUserAndNumber);
    }
    
    @Override
     public CopyOfBuilder createdByUser(String createdByUser) {
      return (CopyOfBuilder) super.createdByUser(createdByUser);
    }
    
    @Override
     public CopyOfBuilder name(String name) {
      return (CopyOfBuilder) super.name(name);
    }
    
    @Override
     public CopyOfBuilder photo(String photo) {
      return (CopyOfBuilder) super.photo(photo);
    }
    
    @Override
     public CopyOfBuilder number(String number) {
      return (CopyOfBuilder) super.number(number);
    }
  }
  
}
