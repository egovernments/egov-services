package org.egov.pgr.v2.contract;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

/**
 * Capture work flow details.
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActionInfo   {
  @JsonProperty("serviceRequestId")
  private String serviceRequestId = null;

  @JsonProperty("action")
  private String action = null;

  @JsonProperty("assignee")
  private String assignee = null;

  @JsonProperty("media")
  @Valid
  private List<Media> media = null;

  @JsonProperty("comments")
  @Valid
  private List<Comment> comments = null;

  public ActionInfo serviceRequestId(String serviceRequestId) {
    this.serviceRequestId = serviceRequestId;
    return this;
  }

  /**
   * Reference to the unique Id of the service request this action is linked to.
   * @return serviceRequestId
  **/

@Size(min=2,max=64) 
  public String getServiceRequestId() {
    return serviceRequestId;
  }

  public void setServiceRequestId(String serviceRequestId) {
    this.serviceRequestId = serviceRequestId;
  }

  public ActionInfo action(String action) {
    this.action = action;
    return this;
  }

  /**
   * Get action
   * @return action
  **/


  public String getAction() {
    return action;
  }

  public void setAction(String action) {
    this.action = action;
  }

  public ActionInfo assignee(String assignee) {
    this.assignee = assignee;
    return this;
  }

  /**
   * Get assignee
   * @return assignee
  **/


  public String getAssignee() {
    return assignee;
  }

  public void setAssignee(String assignee) {
    this.assignee = assignee;
  }

  public ActionInfo media(List<Media> media) {
    this.media = media;
    return this;
  }

  public ActionInfo addMediaItem(Media mediaItem) {
    if (this.media == null) {
      this.media = new ArrayList<Media>();
    }
    this.media.add(mediaItem);
    return this;
  }

  /**
   * Get media
   * @return media
  **/

  @Valid

  public List<Media> getMedia() {
    return media;
  }

  public void setMedia(List<Media> media) {
    this.media = media;
  }

  public ActionInfo comments(List<Comment> comments) {
    this.comments = comments;
    return this;
  }

  public ActionInfo addCommentsItem(Comment commentsItem) {
    if (this.comments == null) {
      this.comments = new ArrayList<Comment>();
    }
    this.comments.add(commentsItem);
    return this;
  }

  /**
   * Get comments
   * @return comments
  **/

  @Valid

  public List<Comment> getComments() {
    return comments;
  }

  public void setComments(List<Comment> comments) {
    this.comments = comments;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ActionInfo actionInfo = (ActionInfo) o;
    return Objects.equals(this.serviceRequestId, actionInfo.serviceRequestId) &&
        Objects.equals(this.action, actionInfo.action) &&
        Objects.equals(this.assignee, actionInfo.assignee) &&
        Objects.equals(this.media, actionInfo.media) &&
        Objects.equals(this.comments, actionInfo.comments);
  }

  @Override
  public int hashCode() {
    return Objects.hash(serviceRequestId, action, assignee, media, comments);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ActionInfo {\n");
    
    sb.append("    serviceRequestId: ").append(toIndentedString(serviceRequestId)).append("\n");
    sb.append("    action: ").append(toIndentedString(action)).append("\n");
    sb.append("    assignee: ").append(toIndentedString(assignee)).append("\n");
    sb.append("    media: ").append(toIndentedString(media)).append("\n");
    sb.append("    comments: ").append(toIndentedString(comments)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

