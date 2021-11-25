package com.zendesk.client.v1.model.ticket;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.ZonedDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Ticket {

    @JsonProperty("id")
    int id;

    @JsonProperty("subject")
    String subject;

    @JsonProperty("status")
    Status status;

    @JsonProperty("priority")
    Priority priority;

    @JsonProperty("updated_at")
    ZonedDateTime updatedAt;

    public int getId() {
        return id;
    }

    public String getSubject() {
        return subject;
    }

    public Status getStatus() {
        return status;
    }

    public Priority getPriority() {
        return priority;
    }

    public ZonedDateTime getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", subject='" + subject + '\'' +
                ", status=" + status +
                ", priority=" + priority +
                ", dateTime=" + updatedAt +
                '}';
    }
}
