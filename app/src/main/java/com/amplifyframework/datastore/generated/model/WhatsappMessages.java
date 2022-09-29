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

/** This is an auto generated class representing the WhatsappMessages type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "WhatsappMessages")
@Index(name = "messagesByUserId", fields = {"Userid","LastMessage","Chatid","Sentbyid","Message","Datetime"})
@Index(name = "messagesBychatterId", fields = {"Chatid","LastMessage","Userid","Sentbyid","Message","Datetime"})
public final class WhatsappMessages implements Model {
  public static final QueryField ID = field("WhatsappMessages", "id");
  public static final QueryField MESSAGE_ID = field("WhatsappMessages", "MessageId");
  public static final QueryField MESSAGE = field("WhatsappMessages", "Message");
  public static final QueryField FILE = field("WhatsappMessages", "File");
  public static final QueryField SENTBYID = field("WhatsappMessages", "Sentbyid");
  public static final QueryField ACK = field("WhatsappMessages", "Ack");
  public static final QueryField DATETIME = field("WhatsappMessages", "Datetime");
  public static final QueryField DIRECTION = field("WhatsappMessages", "Direction");
  public static final QueryField USER = field("WhatsappMessages", "Userid");
  public static final QueryField CHATTER = field("WhatsappMessages", "Chatid");
  public static final QueryField LAST_MESSAGE = field("WhatsappMessages", "LastMessage");
  public static final QueryField CREATED_BY_USER = field("WhatsappMessages", "CreatedByUser");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="ID", isRequired = true) String MessageId;
  private final @ModelField(targetType="String") String Message;
  private final @ModelField(targetType="AWSURL") String File;
  private final @ModelField(targetType="ID", isRequired = true) String Sentbyid;
  private final @ModelField(targetType="Int", isRequired = true) Integer Ack;
  private final @ModelField(targetType="AWSDateTime", isRequired = true) Temporal.DateTime Datetime;
  private final @ModelField(targetType="String", isRequired = true) String Direction;
  private final @ModelField(targetType="WhatsappUser") @BelongsTo(targetName = "Userid", type = WhatsappUser.class) WhatsappUser user;
  private final @ModelField(targetType="WhatsappChatter") @BelongsTo(targetName = "Chatid", type = WhatsappChatter.class) WhatsappChatter chatter;
  private final @ModelField(targetType="Int", isRequired = true) Integer LastMessage;
  private final @ModelField(targetType="String", isRequired = true) String CreatedByUser;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  public String getId() {
      return id;
  }
  
  public String getMessageId() {
      return MessageId;
  }
  
  public String getMessage() {
      return Message;
  }
  
  public String getFile() {
      return File;
  }
  
  public String getSentbyid() {
      return Sentbyid;
  }
  
  public Integer getAck() {
      return Ack;
  }
  
  public Temporal.DateTime getDatetime() {
      return Datetime;
  }
  
  public String getDirection() {
      return Direction;
  }
  
  public WhatsappUser getUser() {
      return user;
  }
  
  public WhatsappChatter getChatter() {
      return chatter;
  }
  
  public Integer getLastMessage() {
      return LastMessage;
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
  
  private WhatsappMessages(String id, String MessageId, String Message, String File, String Sentbyid, Integer Ack, Temporal.DateTime Datetime, String Direction, WhatsappUser user, WhatsappChatter chatter, Integer LastMessage, String CreatedByUser) {
    this.id = id;
    this.MessageId = MessageId;
    this.Message = Message;
    this.File = File;
    this.Sentbyid = Sentbyid;
    this.Ack = Ack;
    this.Datetime = Datetime;
    this.Direction = Direction;
    this.user = user;
    this.chatter = chatter;
    this.LastMessage = LastMessage;
    this.CreatedByUser = CreatedByUser;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      WhatsappMessages whatsappMessages = (WhatsappMessages) obj;
      return ObjectsCompat.equals(getId(), whatsappMessages.getId()) &&
              ObjectsCompat.equals(getMessageId(), whatsappMessages.getMessageId()) &&
              ObjectsCompat.equals(getMessage(), whatsappMessages.getMessage()) &&
              ObjectsCompat.equals(getFile(), whatsappMessages.getFile()) &&
              ObjectsCompat.equals(getSentbyid(), whatsappMessages.getSentbyid()) &&
              ObjectsCompat.equals(getAck(), whatsappMessages.getAck()) &&
              ObjectsCompat.equals(getDatetime(), whatsappMessages.getDatetime()) &&
              ObjectsCompat.equals(getDirection(), whatsappMessages.getDirection()) &&
              ObjectsCompat.equals(getUser(), whatsappMessages.getUser()) &&
              ObjectsCompat.equals(getChatter(), whatsappMessages.getChatter()) &&
              ObjectsCompat.equals(getLastMessage(), whatsappMessages.getLastMessage()) &&
              ObjectsCompat.equals(getCreatedByUser(), whatsappMessages.getCreatedByUser()) &&
              ObjectsCompat.equals(getCreatedAt(), whatsappMessages.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), whatsappMessages.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getMessageId())
      .append(getMessage())
      .append(getFile())
      .append(getSentbyid())
      .append(getAck())
      .append(getDatetime())
      .append(getDirection())
      .append(getUser())
      .append(getChatter())
      .append(getLastMessage())
      .append(getCreatedByUser())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("WhatsappMessages {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("MessageId=" + String.valueOf(getMessageId()) + ", ")
      .append("Message=" + String.valueOf(getMessage()) + ", ")
      .append("File=" + String.valueOf(getFile()) + ", ")
      .append("Sentbyid=" + String.valueOf(getSentbyid()) + ", ")
      .append("Ack=" + String.valueOf(getAck()) + ", ")
      .append("Datetime=" + String.valueOf(getDatetime()) + ", ")
      .append("Direction=" + String.valueOf(getDirection()) + ", ")
      .append("user=" + String.valueOf(getUser()) + ", ")
      .append("chatter=" + String.valueOf(getChatter()) + ", ")
      .append("LastMessage=" + String.valueOf(getLastMessage()) + ", ")
      .append("CreatedByUser=" + String.valueOf(getCreatedByUser()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()))
      .append("}")
      .toString();
  }
  
  public static MessageIdStep builder() {
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
  public static WhatsappMessages justId(String id) {
    return new WhatsappMessages(
      id,
      null,
      null,
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
      MessageId,
      Message,
      File,
      Sentbyid,
      Ack,
      Datetime,
      Direction,
      user,
      chatter,
      LastMessage,
      CreatedByUser);
  }
  public interface MessageIdStep {
    SentbyidStep messageId(String messageId);
  }
  

  public interface SentbyidStep {
    AckStep sentbyid(String sentbyid);
  }
  

  public interface AckStep {
    DatetimeStep ack(Integer ack);
  }
  

  public interface DatetimeStep {
    DirectionStep datetime(Temporal.DateTime datetime);
  }
  

  public interface DirectionStep {
    LastMessageStep direction(String direction);
  }
  

  public interface LastMessageStep {
    CreatedByUserStep lastMessage(Integer lastMessage);
  }
  

  public interface CreatedByUserStep {
    BuildStep createdByUser(String createdByUser);
  }
  

  public interface BuildStep {
    WhatsappMessages build();
    BuildStep id(String id);
    BuildStep message(String message);
    BuildStep file(String file);
    BuildStep user(WhatsappUser user);
    BuildStep chatter(WhatsappChatter chatter);
  }
  

  public static class Builder implements MessageIdStep, SentbyidStep, AckStep, DatetimeStep, DirectionStep, LastMessageStep, CreatedByUserStep, BuildStep {
    private String id;
    private String MessageId;
    private String Sentbyid;
    private Integer Ack;
    private Temporal.DateTime Datetime;
    private String Direction;
    private Integer LastMessage;
    private String CreatedByUser;
    private String Message;
    private String File;
    private WhatsappUser user;
    private WhatsappChatter chatter;
    @Override
     public WhatsappMessages build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new WhatsappMessages(
          id,
          MessageId,
          Message,
          File,
          Sentbyid,
          Ack,
          Datetime,
          Direction,
          user,
          chatter,
          LastMessage,
          CreatedByUser);
    }
    
    @Override
     public SentbyidStep messageId(String messageId) {
        Objects.requireNonNull(messageId);
        this.MessageId = messageId;
        return this;
    }
    
    @Override
     public AckStep sentbyid(String sentbyid) {
        Objects.requireNonNull(sentbyid);
        this.Sentbyid = sentbyid;
        return this;
    }
    
    @Override
     public DatetimeStep ack(Integer ack) {
        Objects.requireNonNull(ack);
        this.Ack = ack;
        return this;
    }
    
    @Override
     public DirectionStep datetime(Temporal.DateTime datetime) {
        Objects.requireNonNull(datetime);
        this.Datetime = datetime;
        return this;
    }
    
    @Override
     public LastMessageStep direction(String direction) {
        Objects.requireNonNull(direction);
        this.Direction = direction;
        return this;
    }
    
    @Override
     public CreatedByUserStep lastMessage(Integer lastMessage) {
        Objects.requireNonNull(lastMessage);
        this.LastMessage = lastMessage;
        return this;
    }
    
    @Override
     public BuildStep createdByUser(String createdByUser) {
        Objects.requireNonNull(createdByUser);
        this.CreatedByUser = createdByUser;
        return this;
    }
    
    @Override
     public BuildStep message(String message) {
        this.Message = message;
        return this;
    }
    
    @Override
     public BuildStep file(String file) {
        this.File = file;
        return this;
    }
    
    @Override
     public BuildStep user(WhatsappUser user) {
        this.user = user;
        return this;
    }
    
    @Override
     public BuildStep chatter(WhatsappChatter chatter) {
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
    private CopyOfBuilder(String id, String messageId, String message, String file, String sentbyid, Integer ack, Temporal.DateTime datetime, String direction, WhatsappUser user, WhatsappChatter chatter, Integer lastMessage, String createdByUser) {
      super.id(id);
      super.messageId(messageId)
        .sentbyid(sentbyid)
        .ack(ack)
        .datetime(datetime)
        .direction(direction)
        .lastMessage(lastMessage)
        .createdByUser(createdByUser)
        .message(message)
        .file(file)
        .user(user)
        .chatter(chatter);
    }
    
    @Override
     public CopyOfBuilder messageId(String messageId) {
      return (CopyOfBuilder) super.messageId(messageId);
    }
    
    @Override
     public CopyOfBuilder sentbyid(String sentbyid) {
      return (CopyOfBuilder) super.sentbyid(sentbyid);
    }
    
    @Override
     public CopyOfBuilder ack(Integer ack) {
      return (CopyOfBuilder) super.ack(ack);
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
     public CopyOfBuilder lastMessage(Integer lastMessage) {
      return (CopyOfBuilder) super.lastMessage(lastMessage);
    }
    
    @Override
     public CopyOfBuilder createdByUser(String createdByUser) {
      return (CopyOfBuilder) super.createdByUser(createdByUser);
    }
    
    @Override
     public CopyOfBuilder message(String message) {
      return (CopyOfBuilder) super.message(message);
    }
    
    @Override
     public CopyOfBuilder file(String file) {
      return (CopyOfBuilder) super.file(file);
    }
    
    @Override
     public CopyOfBuilder user(WhatsappUser user) {
      return (CopyOfBuilder) super.user(user);
    }
    
    @Override
     public CopyOfBuilder chatter(WhatsappChatter chatter) {
      return (CopyOfBuilder) super.chatter(chatter);
    }
  }
  
}
