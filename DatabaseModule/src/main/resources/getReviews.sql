SELECT DISTINCT c.id as chatId, m.id as messageId,
CASE WHEN c.chat_type = 'PRIVATE' THEN CONCAT(pr.surname, ' ', pr.first_name) ELSE c.title END AS title,
m.sender_id as senderId,
m.create_date as dateLastMessage,
m.state as stateMessage,
CASE WHEN c.chat_type = 'PRIVATE' THEN pr.user_id ELSE NULL END AS companionId,
CASE WHEN c.chat_type = 'PRIVATE' THEN pr.photo ELSE c.photo END AS nameFile,
m.text_message as text
FROM chats c
LEFT JOIN (
    SELECT chat_id, MAX(create_date) AS max_create_date
    FROM messages
    GROUP BY chat_id
) m1 ON m1.chat_id = c.id
LEFT JOIN messages m ON m.chat_id = m1.chat_id AND m.create_date = m1.max_create_date
INNER JOIN participants p ON p.chat_id = c.id
LEFT JOIN (
    SELECT chat_id, profile_id
    FROM participants
    WHERE profile_id <> :userId
) p2 ON p2.chat_id = c.id
LEFT JOIN profiles pr ON pr.user_id = p2.profile_id
WHERE p.profile_id = :userId;