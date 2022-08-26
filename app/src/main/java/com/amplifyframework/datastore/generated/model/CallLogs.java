package com.amplifyframework.datastore.generated.model;

import com.amplifyframework.core.model.temporal.Temporal;
import com.amplifyframework.core.model.annotations.BelongsTo;

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

/** This is an auto generated class representing the CallLogs type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "CallLogs")
@Index(name = "CallLogsByUserId", fields = {"Userid","Chatid","Datetime"})
@Index(name = "CallLogsByChatter", fields = {"Chatid","Userid","Datetime"})
public final class CallLogs implements Model {
  public static final QueryField ID = field("CallLogs", "id");
  public static final QueryField DURATION = field("CallLogs", "Duration");
  public static final QueryField USERID = field("CallLogs", "Userid");
  public static final QueryField CHATID = field("CallLogs", "Chatid");
  public static final QueryField DATETIME = field("CallLogs", "Datetime");
  public static final QueryField DIRECTION = field("CallLogs", "Direction");
  public static final QueryField USER = field("CallLogs", "userCallLogsId");
  public static final QueryField CHATTER = field("CallLogs", "chatterCallLogsId");
  public static final QueryField CALLTIME = field("CallLogs", "Calltime");
  public static final QueryField CREATED_BY_USER = field("CallLogs", "CreatedByUser");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="Int", isRequired = true) Integer Duration;
  private final @ModelField(targetType="ID", isRequired = true) String Userid;
  private final @ModelField(targetType="ID", isRequired = true) String Chatid;
  private final @ModelField(targetType="AWSDateTime", isRequired = true) Temporal.DateTime Datetime;
  private final @ModelField(targetType="String", isRequired = true) String Direction;
  private final @ModelField(targetType="User") @BelongsTo(targetName = "userCallLogsId", type = User.class) User user;
  private final @ModelField(targetType="Chatter") @BelongsTo(targetName = "chatterCallLogsId", type = Chatter.class) Chatter chatter;
  private final @ModelField(targetType="Float", isRequired = true) Double Calltime;
  private final @ModelField(targetType="String", isRequired = true) String CreatedByUser;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  public String getId() {
      return id;
  }
  
  public Integer getDuration() {
      return Duration;
  }
  
  public String getUserid() {
      return Userid;
  }
  
  public String getChatid() {
      return Chatid;
  }
  
  public Temporal.DateTime getDatetime() {
      return Datetime;
  }
  
  public String getDirection() {
      return Direction;
  }
  
  public User getUser() {
      return user;
  }
  
  public Chatter getChatter() {
      return chatter;
  }
  
  public Double getCalltime() {
      return Calltime;
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
  
  private CallLogs(String id, Integer Duration, String Userid, String Chatid, Temporal.DateTime Datetime, String Direction, User user, Chatter chatter, Double Calltime, String CreatedByUser) {
    this.id = id;
    this.Duration = Duration;
    this.Userid = Userid;
    this.Chatid = Chatid;
    this.Datetime = Datetime;
    this.Direction = Direction;
    this.user = user;
    this.chatter = chatter;
    this.Calltime = Calltime;
    this.CreatedByUser = CreatedByUser;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      CallLogs callLogs = (CallLogs) obj;
      return ObjectsCompat.equals(getId(), callLogs.getId()) &&
              ObjectsCompat.equals(getDuration(), callLogs.getDuration()) &&
              ObjectsCompat.equals(getUserid(), callLogs.getUserid()) &&
              ObjectsCompat.equals(getChatid(), callLogs.getChatid()) &&
              ObjectsCompat.equals(getDatetime(), callLogs.getDatetime()) &&
              ObjectsCompat.equals(getDirection(), callLogs.getDirection()) &&
              ObjectsCompat.equals(getUser(), callLogs.getUser()) &&
              ObjectsCompat.equals(getChatter(), callLogs.getChatter()) &&
              ObjectsCompat.equals(getCalltime(), callLogs.getCalltime()) &&
              ObjectsCompat.equals(getCreatedByUser(), callLogs.getCreatedByUser()) &&
              ObjectsCompat.equals(getCreatedAt(), callLogs.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), callLogs.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getDuration())
      .append(getUserid())
      .append(getChatid())
      .append(getDatetime())
      .append(getDirection())
      .append(getUser())
      .append(getChatter())
      .append(getCalltime())
      .append(getCreatedByUser())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("CallLogs {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("Duration=" + String.valueOf(getDuration()) + ", ")
      .append("Userid=" + String.valueOf(getUserid()) + ", ")
      .append("Chatid=" + String.valueOf(getChatid()) + ", ")
      .append("Datetime=" + String.valueOf(getDatetime()) + ", ")
      .append("Direction=" + String.valueOf(getDirection()) + ", ")
      .append("user=" + String.valueOf(getUser()) + ", ")
      .append("chatter=" + String.valueOf(getChatter()) + ", ")
      .append("Calltime=" + String.valueOf(getCalltime()) + ", ")
      .append("CreatedByUser=" + String.valueOf(getCreatedByUser()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()))
      .append("}")
      .toString();
  }
  
  public static DurationStep builder() {
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
  public static CallLogs justId(String id) {
    return new CallLogs(
      id,
      null,
      null,
      null,
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
      Duration,
      Userid,
      Chatid,
      Datetime,
      Direction,
      user,
      chatter,
      Calltime,
      CreatedByUser);
  }
  public interface DurationStep {
    UseridStep duration(Integer duration);
  }
  

  public interface UseridStep {
    ChatidStep userid(String userid);
  }
  

  public interface ChatidStep {
    DatetimeStep chatid(String chatid);
  }
  

  public interface DatetimeStep {
    DirectionStep datetime(Temporal.DateTime datetime);
  }
  

  public interface DirectionStep {
    CalltimeStep direction(String direction);
  }
  

  public interface CalltimeStep {
    CreatedByUserStep calltime(Double calltime);
  }
  

  public interface CreatedByUserStep {
    BuildStep createdByUser(String createdByUser);
  }
  

  public interface BuildStep {
    CallLogs build();
    BuildStep id(String id);
    BuildStep user(User user);
    BuildStep chatter(Chatter chatter);
  }
  

  public static class Builder implements DurationStep, UseridStep, ChatidStep, DatetimeStep, DirectionStep, CalltimeStep, CreatedByUserStep, BuildStep {
    private String id;
    private Integer Duration;
    private String Userid;
    private String Chatid;
    private Temporal.DateTime Datetime;
    private String Direction;
    private Double Calltime;
    private String CreatedByUser;
    private User user;
    private Chatter chatter;
    @Override
     public CallLogs build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new CallLogs(
          id,
          Duration,
          Userid,
          Chatid,
          Datetime,
          Direction,
          user,
          chatter,
          Calltime,
          CreatedByUser);
    }
    
    @Override
     public UseridStep duration(Integer duration) {
        Objects.requireNonNull(duration);
        this.Duration = duration;
        return this;
    }
    
    @Override
     public ChatidStep userid(String userid) {
        Objects.requireNonNull(userid);
        this.Userid = userid;
        return this;
    }
    
    @Override
     public DatetimeStep chatid(String chatid) {
        Objects.requireNonNull(chatid);
        this.Chatid = chatid;
        return this;
    }
    
    @Override
     public DirectionStep datetime(Temporal.DateTime datetime) {
        Objects.requireNonNull(datetime);
        this.Datetime = datetime;
        return this;
    }
    
    @Override
     public CalltimeStep direction(String direction) {
        Objects.requireNonNull(direction);
        this.Direction = direction;
        return this;
    }
    
    @Override
     public CreatedByUserStep calltime(Double calltime) {
        Objects.requireNonNull(calltime);
        this.Calltime = calltime;
        return this;
    }
    
    @Override
     public BuildStep createdByUser(String createdByUser) {
        Objects.requireNonNull(createdByUser);
        this.CreatedByUser = createdByUser;
        return this;
    }
    
    @Override
     public BuildStep user(User user) {
        this.user = user;
        return this;
    }
    
    @Override
     public BuildStep chatter(Chatter chatter) {
        this.chatter = chatter;
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
    private CopyOfBuilder(String id, Integer duration, String userid, String chatid, Temporal.DateTime datetime, String direction, User user, Chatter chatter, Double calltime, String createdByUser) {
      super.id(id);
      super.duration(duration)
        .userid(userid)
        .chatid(chatid)
        .datetime(datetime)
        .direction(direction)
        .calltime(calltime)
        .createdByUser(createdByUser)
        .user(user)
        .chatter(chatter);
    }
    
    @Override
     public CopyOfBuilder duration(Integer duration) {
      return (CopyOfBuilder) super.duration(duration);
    }
    
    @Override
     public CopyOfBuilder userid(String userid) {
      return (CopyOfBuilder) super.userid(userid);
    }
    
    @Override
     public CopyOfBuilder chatid(String chatid) {
      return (CopyOfBuilder) super.chatid(chatid);
    }
    
    @Override
     public CopyOfBuilder datetime(Temporal.DateTime datetime) {
      return (CopyOfBuilder) super.datetime(datetime);
    }
    
    @Override
     public CopyOfBuilder direction(String direction) {
      return (CopyOfBuilder) super.direction(direction);
    }
    
    @Override
     public CopyOfBuilder calltime(Double calltime) {
      return (CopyOfBuilder) super.calltime(calltime);
    }
    
    @Override
     public CopyOfBuilder createdByUser(String createdByUser) {
      return (CopyOfBuilder) super.createdByUser(createdByUser);
    }
    
    @Override
     public CopyOfBuilder user(User user) {
      return (CopyOfBuilder) super.user(user);
    }
    
    @Override
     public CopyOfBuilder chatter(Chatter chatter) {
      return (CopyOfBuilder) super.chatter(chatter);
    }
  }
  
}
