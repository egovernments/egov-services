package org.egov.pgr.v2.contract;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

/**
 * Capture work flow details.
 */
@Validated
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActionHistory   {
  @JsonProperty("media")
  @Valid
  private List<Media> media = null;

  @JsonProperty("comments")
  @Valid
  private List<Comment> comments = null;

  @JsonProperty("statuses")
  @Valid
  private List<Status> statuses = null;

  @JsonProperty("assignees")
  @Valid
  private List<Assignee> assignees = null;

  public ActionHistory media(List<Media> media) {
    this.media = media;
    return this;
  }

  public ActionHistory addMediaItem(Media mediaItem) {
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

  public ActionHistory comments(List<Comment> comments) {
    this.comments = comments;
    return this;
  }

  public ActionHistory addCommentsItem(Comment commentsItem) {
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

  public ActionHistory statuses(List<Status> statuses) {
    this.statuses = statuses;
    return this;
  }

  public ActionHistory addStatusesItem(Status statusesItem) {
    if (this.statuses == null) {
      this.statuses = new ArrayList<Status>();
    }
    this.statuses.add(statusesItem);
    return this;
  }

  /**
   * Get statuses
   * @return statuses
  **/

  @Valid

  public List<Status> getStatuses() {
    return statuses;
  }

  public void setStatuses(List<Status> statuses) {
    this.statuses = statuses;
  }

  public ActionHistory assignees(List<Assignee> assignees) {
    this.assignees = assignees;
    return this;
  }

  public ActionHistory addAssigneesItem(Assignee assigneesItem) {
    if (this.assignees == null) {
      this.assignees = new ArrayList<Assignee>();
    }
    this.assignees.add(assigneesItem);
    return this;
  }

  /**
   * Get assignees
   * @return assignees
  **/

  @Valid

  public List<Assignee> getAssignees() {
    return assignees;
  }

  public void setAssignees(List<Assignee> assignees) {
    this.assignees = assignees;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ActionHistory actionHistory = (ActionHistory) o;
    return Objects.equals(this.media, actionHistory.media) &&
        Objects.equals(this.comments, actionHistory.comments) &&
        Objects.equals(this.statuses, actionHistory.statuses) &&
        Objects.equals(this.assignees, actionHistory.assignees);
  }

  @Override
  public int hashCode() {
    return Objects.hash(media, comments, statuses, assignees);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ActionHistory {\n");
    
    sb.append("    media: ").append(toIndentedString(media)).append("\n");
    sb.append("    comments: ").append(toIndentedString(comments)).append("\n");
    sb.append("    statuses: ").append(toIndentedString(statuses)).append("\n");
    sb.append("    assignees: ").append(toIndentedString(assignees)).append("\n");
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

