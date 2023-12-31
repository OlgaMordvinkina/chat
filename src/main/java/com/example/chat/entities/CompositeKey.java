package com.example.chat.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class CompositeKey implements Serializable {
    @Column(name = "chat_id")
    private Long chatId;
    @ManyToOne
    @JoinColumn(name = "profile_id", referencedColumnName = "id", updatable = false, insertable = false, nullable = false)
    private ProfileEntity user;
}
