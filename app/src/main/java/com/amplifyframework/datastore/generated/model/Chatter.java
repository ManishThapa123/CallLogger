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

/** This is an auto generated class representing the Chatter type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "Chatters")
@Index(name = "undefined", fields = {"ChatterId"})
public final class Chatter implements Model {
  public static final QueryField ID = field("Chatter", "id");
  public static final QueryField CHATTER_ID = field("Chatter", "ChatterId");
  public static final QueryField NAME = field("Chatter", "Name");
  public static final QueryField CREATED_BY_USER = field("Chatter", "CreatedByUser");
  public static final QueryField PHOTO = field("Chatter", "Photo");
  public static final QueryField NUMBER = field("Chatter", "Number");
  public static final QueryField DIRECTION = field("Chatter", "Direction");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="ID", isRequired = true) String ChatterId;
  private final @ModelField(targetType="String") String Name;
  private final @ModelField(targetType="String", isRequired = true) String CreatedByUser;
  private final @ModelField(targetType="AWSURL") String Photo;
  private final @ModelField(targetType="String") String Number;
  private final @ModelField(targetType="String") String Direction;
  private final @ModelField(targetType="CallLogs") @HasMany(associatedWith = "Chatid", type = CallLogs.class) List<CallLogs> CallLogs = null;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  public String getId() {
      return id;
  }
  
  public String getChatterId() {
      return ChatterId;
  }
  
  public String getName() {
      return Name;
  }
  
  public String getCreatedByUser() {
      return CreatedByUser;
  }
  
  public String getPhoto() {
      return Photo;
  }
  
  public String getNumber() {
      return Number;
  }
  
  public String getDirection() {
      return Direction;
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
  
  private Chatter(String id, String ChatterId, String Name, String CreatedByUser, String Photo, String Number, String Direction) {
    this.id = id;
    this.ChatterId = ChatterId;
    this.Name = Name;
    this.CreatedByUser = CreatedByUser;
    this.Photo = Photo;
    this.Number = Number;
    this.Direction = Direction;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      Chatter chatter = (Chatter) obj;
      return ObjectsCompat.equals(getId(), chatter.getId()) &&
              ObjectsCompat.equals(getChatterId(), chatter.getChatterId()) &&
              ObjectsCompat.equals(getName(), chatter.getName()) &&
              ObjectsCompat.equals(getCreatedByUser(), chatter.getCreatedByUser()) &&
              ObjectsCompat.equals(getPhoto(), chatter.getPhoto()) &&
              ObjectsCompat.equals(getNumber(), chatter.getNumber()) &&
              ObjectsCompat.equals(getDirection(), chatter.getDirection()) &&
              ObjectsCompat.equals(getCreatedAt(), chatter.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), chatter.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getChatterId())
      .append(getName())
      .append(getCreatedByUser())
      .append(getPhoto())
      .append(getNumber())
      .append(getDirection())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("Chatter {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("ChatterId=" + String.valueOf(getChatterId()) + ", ")
      .append("Name=" + String.valueOf(getName()) + ", ")
      .append("CreatedByUser=" + String.valueOf(getCreatedByUser()) + ", ")
      .append("Photo=" + String.valueOf(getPhoto()) + ", ")
      .append("Number=" + String.valueOf(getNumber()) + ", ")
      .append("Direction=" + String.valueOf(getDirection()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()))
      .append("}")
      .toString();
  }
  
  public static ChatterIdStep builder() {
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
  public static Chatter justId(String id) {
    return new Chatter(
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
      ChatterId,
      Name,
      CreatedByUser,
      Photo,
      Number,
      Direction);
  }
  public interface ChatterIdStep {
    CreatedByUserStep chatterId(String chatterId);
  }
  

  public interface CreatedByUserStep {
    BuildStep createdByUser(String createdByUser);
  }
  

  public interface BuildStep {
    Chatter build();
    BuildStep id(String id);
    BuildStep name(String name);
    BuildStep photo(String photo);
    BuildStep number(String number);
    BuildStep direction(String direction);
  }
  

  public static class Builder implements ChatterIdStep, CreatedByUserStep, BuildStep {
    private String id;
    private String ChatterId;
    private String CreatedByUser;
    private String Name;
    private String Photo;
    private String Number;
    private String Direction;
    @Override
     public Chatter build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new Chatter(
          id,
          ChatterId,
          Name,
          CreatedByUser,
          Photo,
          Number,
          Direction);
    }
    
    @Override
     public CreatedByUserStep chatterId(String chatterId) {
        Objects.requireNonNull(chatterId);
        this.ChatterId = chatterId;
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
    
    @Override
     public BuildStep direction(String direction) {
        this.Direction = direction;
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
    private CopyOfBuilder(String id, String chatterId, String name, String createdByUser, String photo, String number, String direction) {
      super.id(id);
      super.chatterId(chatterId)
        .createdByUser(createdByUser)
        .name(name)
        .photo(photo)
        .number(number)
        .direction(direction);
    }
    
    @Override
     public CopyOfBuilder chatterId(String chatterId) {
      return (CopyOfBuilder) super.chatterId(chatterId);
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
    
    @Override
     public CopyOfBuilder direction(String direction) {
      return (CopyOfBuilder) super.direction(direction);
    }
  }
  
}
