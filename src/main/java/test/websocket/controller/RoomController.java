package test.websocket.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import test.websocket.dto.ChatRoomDTO;
import test.websocket.repository.ChatRoomRepository;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/chat")
@Log4j2
public class RoomController {

    private final ChatRoomRepository repository;

    //채팅방 목록 조회
    @GetMapping(value = "/rooms")
    public ResponseEntity rooms(){

        log.info("# All Chat Rooms");

        List<ChatRoomDTO> roomDTOList = repository.findAllRooms();

        return new ResponseEntity<>(
                roomDTOList,
                HttpStatus.OK);
    }

    @PostMapping(value = "/room")
    public ChatRoomDTO create(@RequestParam String name){
        log.info("# Create Chat Room , name: " + name);
        ChatRoomDTO chatRoomDTO = repository.createChatRoomDTO(name);
        return chatRoomDTO;
    }

    @GetMapping("/room")
    public ChatRoomDTO getRoom(String roomId){
        log.info("# get Chat Room, roomID : " + roomId);

        return repository.findRoomById(roomId);
    }
}
