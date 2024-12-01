package org.mediagate.db.model.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class CompositeKey implements Serializable {
    @Column(name = "chat_id")
    Long chatId;
    @ManyToOne
    @JoinColumn(name = "profile_id", referencedColumnName = "id", updatable = false, insertable = false, nullable = false)
    ProfileEntity user;
}
