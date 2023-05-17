package test.websocket.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import test.websocket.data.ChatMessage;
import test.websocket.data.ChatRoom;
import test.websocket.service.ChatService;

@Slf4j
@RequiredArgsConstructor
@Component
public class WebSocketHandler extends TextWebSocketHandler {
    private final ObjectMapper objectMapper;
    private final ChatService chatService;

    @Override //메세지 전송시 처리 메서드
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception { //세션 정보, 요청메세지 전달
        String payload = message.getPayload(); //요청 메세지
        log.info("{}", payload);
        ChatMessage chatMessage = objectMapper.readValue(payload, ChatMessage.class);   //요청 메세지를 Message형태로 변환

        ChatRoom chatRoom = chatService.findRoomById(chatMessage.getRoomId()); //웹소켓안에서 룸id로 룸 찾기
        chatRoom.handlerActions(session, chatMessage, chatService); //룸안의 메세지 처리 (룸안의 세션리스트에게 메세지 전달)
    }

    @Override //웹소켓 연결시 호출 메서드
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info(session.getId() + "님이 접속했습니다.");
        log.info("열결 IP : " + session.getRemoteAddress().getHostName());
        super.afterConnectionEstablished(session);
    }

    @Override //웹소켓 해제시 호출 메서드
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info(session.getId() + "님이 접속해제하였습니다.");
    }
}