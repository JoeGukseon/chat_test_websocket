package test.websocket.data;

import lombok.Builder;
import lombok.Getter;
import org.springframework.web.socket.WebSocketSession;
import test.websocket.service.ChatService;

import java.util.HashSet;
import java.util.Set;

@Getter
public class ChatRoom {
    private String roomId;
    private String name;
    private Set<WebSocketSession> sessions = new HashSet<>();

    @Builder
    public ChatRoom(String roomId, String name) {
        this.roomId = roomId;
        this.name = name;
    }

    public void handlerActions(WebSocketSession session, ChatMessage chatMessage, ChatService chatService) {
        if (chatMessage.getType().equals(ChatMessage.MessageType.ENTER)) {  // 룸입장시
            sessions.add(session);  //룸안에 세션리스트에 세션 추가
            chatMessage.setMessage(chatMessage.getSender() + "님이 입장했습니다."); //입장메세지
        }
        sendMessage(chatMessage, chatService); //메세지 처리
    }


    private <T> void sendMessage(T message, ChatService chatService) {
        //세션리스트 안에 세션들에게 메세지 전달
        //세션종료시 리스트 제거 !
        sessions.parallelStream()
                .forEach(session -> {
                    if (session.isOpen()) {
                        chatService.sendMessage(session, message);
                    } else {
                        sessions.remove(session);
                    }
                });
    }
}