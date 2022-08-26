package com.amplifyframework.datastore.generated.model;

import com.amplifyframework.core.model.temporal.Temporal;
import com.amplifyframework.core.model.annotations.HasMany;

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

/** This is an auto generated class representing the User type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "Users")
@Index(name = "undefined", fields = {"Userid"})
public final class User implements Model {
  public static final QueryField ID = field("User", "id");
  public static final QueryField USERID = field("User", "Userid");
  public static final QueryField NAME = field("User", "Name");
  public static final QueryField EMAIL = field("User", "Email");
  public static final QueryField WHATSAPPNO = field("User", "Whatsappno");
  public static final QueryField PHOTO = field("User", "Photo");
  public static final QueryField LAST_SYNC = field("User", "LastSync");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="ID", isRequired = true) String Userid;
  private final @ModelField(targetType="String") String Name;
  private final @ModelField(targetType="String", isRequired = true) String Email;
  private final @ModelField(targetType="Int") Integer Whatsappno;
  private final @ModelField(targetType="AWSURL") String Photo;
  private final @ModelField(targetType="AWSDateTime", isRequired = true) Temporal.DateTime LastSync;
  private final @ModelField(targetType="CallLogs") @HasMany(associatedWith = "Userid", type = CallLogs.class) List<CallLogs> CallLogs = null;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  public String getId() {
      return id;
  }
  
  public String getUserid() {
      return Userid;
  }
  
  public String getName() {
      return Name;
  }
  
  public String getEmail() {
      return Email;
  }
  
  public Integer getWhatsappno() {
      return Whatsappno;
  }
  
  public String getPhoto() {
      return Photo;
  }
  
  public Temporal.DateTime getLastSync() {
      return LastSync;
  }
  
  public List<CallLogs> getCallLogs() {
      return CallLogs;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private User(String id, String Userid, String Name, String Email, Integer Whatsappno, String Photo, Temporal.DateTime LastSync) {
    this.id = id;
    this.Userid = Userid;
    this.Name = Name;
    this.Email = Email;
    this.Whatsappno = Whatsappno;
    this.Photo = Photo;
    this.LastSync = LastSync;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      User user = (User) obj;
      return ObjectsCompat.equals(getId(), user.getId()) &&
              ObjectsCompat.equals(getUserid(), user.getUserid()) &&
              ObjectsCompat.equals(getName(), user.getName()) &&
              ObjectsCompat.equals(getEmail(), user.getEmail()) &&
              ObjectsCompat.equals(getWhatsappno(), user.getWhatsappno()) &&
              ObjectsCompat.equals(getPhoto(), user.getPhoto()) &&
              ObjectsCompat.equals(getLastSync(), user.getLastSync()) &&
              ObjectsCompat.equals(getCreatedAt(), user.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), user.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getUserid())
      .append(getName())
      .append(getEmail())
      .append(getWhatsappno())
      .append(getPhoto())
      .append(getLastSync())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("User {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("Userid=" + String.valueOf(getUserid()) + ", ")
      .append("Name=" + String.valueOf(getName()) + ", ")
      .append("Email=" + String.valueOf(getEmail()) + ", ")
      .append("Whatsappno=" + String.valueOf(getWhatsappno()) + ", ")
      .append("Photo=" + String.valueOf(getPhoto()) + ", ")
      .append("LastSync=" + String.valueOf(getLastSync()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()))
      .append("}")
      .toString();
  }
  
  public static UseridStep builder() {
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
  public static User justId(String id) {
    return new User(
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
      Userid,
      Name,
      Email,
      Whatsappno,
      Photo,
      LastSync);
  }
  public interface UseridStep {
    EmailStep userid(String userid);
  }
  

  public interface EmailStep {
    LastSyncStep email(String email);
  }
  

  public interface LastSyncStep {
    BuildStep lastSync(Temporal.DateTime lastSync);
  }
  

  public interface BuildStep {
    User build();
    BuildStep id(String id);
    BuildStep name(String name);
    BuildStep whatsappno(Integer whatsappno);
    BuildStep photo(String photo);
  }
  

  public static class Builder implements UseridStep, EmailStep, LastSyncStep, BuildStep {
    private String id;
    private String Userid;
    private String Email;
    private Temporal.DateTime LastSync;
    private String Name;
    private Integer Whatsappno;
    private String Photo;
    @Override
     public User build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new User(
          id,
          Userid,
          Name,
          Email,
          Whatsappno,
          Photo,
          LastSync);
    }
    
    @Override
     public EmailStep userid(String userid) {
        Objects.requireNonNull(userid);
        this.Userid = userid;
        return this;
    }
    
    @Override
     public LastSyncStep email(String email) {
        Objects.requireNonNull(email);
        this.Email = email;
        return this;
    }
    
    @Override
     public BuildStep lastSync(Temporal.DateTime lastSync) {
        Objects.requireNonNull(lastSync);
        this.LastSync = lastSync;
        return this;
    }
    
    @Override
     public BuildStep name(String name) {
        this.Name = name;
        return this;
    }
    
    @Override
     public BuildStep whatsappno(Integer whatsappno) {
        this.Whatsappno = whatsappno;
        return this;
    }
    
    @Override
     public BuildStep photo(String photo) {
        this.Photo = photo;
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
    private CopyOfBuilder(String id, String userid, String name, String email, Integer whatsappno, String photo, Temporal.DateTime lastSync) {
      super.id(id);
      super.userid(userid)
        .email(email)
        .lastSync(lastSync)
        .name(name)
        .whatsappno(whatsappno)
        .photo(photo);
    }
    
    @Override
     public CopyOfBuilder userid(String userid) {
      return (CopyOfBuilder) super.userid(userid);
    }
    
    @Override
     public CopyOfBuilder email(String email) {
      return (CopyOfBuilder) super.email(email);
    }
    
    @Override
     public CopyOfBuilder lastSync(Temporal.DateTime lastSync) {
      return (CopyOfBuilder) super.lastSync(lastSync);
    }
    
    @Override
     public CopyOfBuilder name(String name) {
      return (CopyOfBuilder) super.name(name);
    }
    
    @Override
     public CopyOfBuilder whatsappno(Integer whatsappno) {
      return (CopyOfBuilder) super.whatsappno(whatsappno);
    }
    
    @Override
     public CopyOfBuilder photo(String photo) {
      return (CopyOfBuilder) super.photo(photo);
    }
  }
  
}
