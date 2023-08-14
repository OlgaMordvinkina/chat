package com.example.chat.repositories;

import com.example.chat.dto.ChatPreviewDto;
import com.example.chat.entities.ChatEntity;
import com.example.chat.entities.MessageEntity;
import com.example.chat.entities.ParticipantEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class ChatCustomRepositoryImpl implements ChatCustomRepository {
    private final EntityManager manager;

    //TODO: не закончен, неверно работает
    @Override
    public Set<ChatPreviewDto> findChatPreview(Long userId) {
        CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
        CriteriaQuery<ChatPreviewDto> criteriaQuery = criteriaBuilder.createQuery(ChatPreviewDto.class);

//        Root<ChatEntity> chatRoot = criteriaQuery.from(ChatEntity.class);
//        Root<MessageEntity> messageRoot = criteriaQuery.from(MessageEntity.class);
//        Root<ParticipantEntity> participantRoot = criteriaQuery.from(ParticipantEntity.class);
//
//        Join<ChatEntity, MessageEntity> messageJoin = chatRoot.join("chat", JoinType.INNER);
//        Join<ChatEntity, ParticipantEntity> participantJoin = chatRoot.join("participants", JoinType.INNER);

        Root<MessageEntity> messageRoot = criteriaQuery.from(MessageEntity.class);
        Join<MessageEntity, ParticipantEntity> participantJoin = messageRoot.join("chat", JoinType.INNER).join("participants", JoinType.INNER);
        Join<ParticipantEntity, ChatEntity> chatJoin = participantJoin.join("key", JoinType.INNER).join("chat", JoinType.INNER);


        criteriaQuery.select(criteriaBuilder.construct(
                ChatPreviewDto.class,
                chatJoin.get("id"),
                chatJoin.get("title"),
                messageRoot.get("text"),
                messageRoot.get("createDate"),
                messageRoot.get("state")
        ));

        Predicate userPredicate = criteriaBuilder.equal(participantJoin.get("key").get("user").get("id"), userId);
        Subquery<LocalDateTime> subquery = criteriaQuery.subquery(LocalDateTime.class);
        Root<MessageEntity> subqueryRoot = subquery.from(MessageEntity.class);
        subquery.select(criteriaBuilder.max(subqueryRoot.get("createDate")).as(LocalDateTime.class));
        Predicate datePredicate = criteriaBuilder.equal(messageRoot.get("createDate"), subquery);
        criteriaQuery.where(userPredicate, datePredicate);

        criteriaQuery.groupBy(
                chatJoin.get("id"),
                chatJoin.get("title"),
                messageRoot.get("text"),
                messageRoot.get("createDate"),
                messageRoot.get("state")
        );

        TypedQuery<ChatPreviewDto> typedQuery = manager.createQuery(criteriaQuery)
                .setLockMode(LockModeType.PESSIMISTIC_WRITE);

        List<ChatPreviewDto> resultList = typedQuery.getResultList();
        Set<ChatPreviewDto> results = new HashSet<>(resultList);

        return results;
/*        CriteriaBuilder cb = manager.getCriteriaBuilder();
        CriteriaQuery<ChatPreviewDto> cq = cb.createQuery(ChatPreviewDto.class);
        CriteriaQuery<Number> date = cb.createQuery(Number.class);

        Root<ChatEntity> chatRoot = cq.from(ChatEntity.class);
        Root<MessageEntity> messageRoot = cq.from(MessageEntity.class);
        Root<ParticipantEntity> participantRoot = cq.from(ParticipantEntity.class);
        List<Predicate> predicates = new ArrayList<>();

//        Join<ChatEntity, MessageEntity> messageJoin = messageRoot.join("chat", JoinType.INNER);
//        Join<ChatEntity, ParticipantEntity> participantJoin = participantRoot.join("id", JoinType.INNER);

        Subquery<Number> subquery = cq.subquery(Number.class);

        cq.multiselect(
                chatRoot.get("id"),
                chatRoot.get("title"),
                messageRoot.get("text"),
                messageRoot.get("createDate"),
                messageRoot.get("state")
        ).groupBy(
                chatRoot.get("id"),
                chatRoot.get("title"),
                messageRoot.get("text"),
                messageRoot.get("createDate"),
                messageRoot.get("state")
        ).where(
                cb.equal(participantRoot.get("key").get("user").get("id"), userId),
                cb.equal(
                        messageRoot.get("createDate"),
                        subquery.select(cb.max(messageRoot.get("createDate")))
//                        cq.select(cb.greatest(messageRoot.get("createDate"))).from(MessageEntity.class)
                ));
//
//
//        cb.selectCase(cb.equal(chatRoot.get("type"), Availability.GROUP));
//
//        cq.where(cb.and(predicates.toArray(new Predicate[0])))
//                .groupBy(chatRoot.get("id"), chatRoot.get("title"), messageJoin.get("text"), messageJoin.get("createDate"), messageJoin.get("state"));
        return manager.createQuery(cq).getResultList();*/
    }
}
