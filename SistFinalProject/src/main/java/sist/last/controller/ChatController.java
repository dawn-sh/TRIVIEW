package sist.last.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import sist.last.chat.Room;
import sist.last.dto.ChatDto;
import sist.last.dto.ChatRoomDto;
import sist.last.mapper.AccomMapperInter;
import sist.last.mapper.ChatMapperInter;
import sist.last.mapper.ChatRoomMapperInter;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Controller // 이 클래스가 Spring MVC의 컨트롤러 임을 나타내는 어노테이션이다.
@RequestMapping("/chat") // 이 컨트롤러의 기본 URL 정보가 "/chat" 임을 지정합니다.
public class ChatController {

    @Autowired
    AccomMapperInter accomMapperInter;
    @Autowired
    ChatRoomMapperInter roomMapperInter;
    @Autowired
    ChatMapperInter chatMapperInter;



    @PostMapping("/createRoom")
    @ResponseBody
    public int createRoom(@RequestParam int accom_num,
                          HttpSession session){
        String sender_id=(String) session.getAttribute("info_id");

        // 판매자의 user_num을 찾을 수 있도록 수정해야 됨
        String business_id="test2";

        // 현재 채팅을 보내려는 사용자가 판매자이면 방을 생성할 수 없음
        if(sender_id.equals(business_id)){
            return 0;
        } else {
            // 기존에 존재하는 채팅방이라면 그곳으로 이동
            Map<String,Object>map=new HashMap<>();
            map.put("sender_id",sender_id);
            map.put("accom_num",accom_num);
            String room=roomMapperInter.checkChatRoom(map);

            if (room!=null){
                return Integer.parseInt(room);
            } else { // 없다면 새로 방 생성후 생성된 방으로 이동
                ChatRoomDto chatRoomDto=new ChatRoomDto();
                chatRoomDto.setAccom_num(accom_num);
                chatRoomDto.setSender_id(sender_id);
                chatRoomDto.setReceiver_id(business_id);
                roomMapperInter.insertRoom(chatRoomDto);
                return roomMapperInter.getMaxNum();

            }

        }
    }

    @GetMapping("/goSellerRooms")
    public ModelAndView goSellerRooms(@RequestParam int accom_num,
                                @RequestParam int room_num,
                                Model model){
        ModelAndView mv=new ModelAndView();

        List<ChatRoomDto> chatRoomList=roomMapperInter.getChatRoomByAccom(accom_num);

        String roomName=accomMapperInter.getOneData(accom_num).getAccom_name();

        mv.addObject("chatRoomList",chatRoomList);
        mv.addObject("roomName",roomName);
        mv.addObject("accom_num",accom_num);
        mv.addObject("room_num",room_num);

        mv.setViewName("/chat/room");

        return mv;
    }

    @GetMapping("/goChattingRoom")
    public String goChattingRoom(@RequestParam int room_num,
                                 Model model){
        String roomName=accomMapperInter.getOneData(roomMapperInter.getChatRoom(room_num).getAccom_num()).getAccom_name();

        model.addAttribute("room_num",room_num);
        model.addAttribute("roomName",roomName);

        return "/chat/chat";
    }

    @GetMapping("/chatting")
    @ResponseBody
    public List<ChatDto> chatting(@RequestParam(required = false)int room_num,
                                  HttpSession session){
        try {
            Thread.sleep(100);// 0.1초 기다리기
        } catch (InterruptedException e){
            e.printStackTrace();
        }

        // 사용자의 num 받기
        String myid=(String) session.getAttribute("myid");
        List<ChatDto> chatList=new ArrayList<>();

        for (ChatDto chatDto:chatList){
            Date today=new Date();
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            sdf.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
            Date writeday=new Date();
            try {
                writeday=sdf.parse(chatDto.getMess_writeday().toString());
            } catch (ParseException e){
                e.printStackTrace();
            }

            long diffSec=(today.getTime()-writeday.getTime());
//            diffSec-=

            // 일, 시, 분, 초
            long day=(diffSec/(60*60*24*1000L))%365;
            long hour=(diffSec/(60*60*1000L))%24;
            long minute=(diffSec/(60*1000L))%60;
            long second=(diffSec/1000L)%60;

            String preTime="";

            if(day!=0){
                // 하루 이상이 지났으면 일수만 표시
                preTime=""+day+"일 전";
            } else {
                if (hour!=0){
                    // 1분 이상 지났으면 분만 표시
                    preTime=""+minute+"분 전";
                } else {
                    // 1분 미만 방금전 표시
                    preTime="방금 전";
                }
            }
            chatDto.setMess_time(preTime);
        }
        return chatList;
    }





    @PostMapping("/fileupload")
    @ResponseBody
    public Map<String,String> fileUpload(@RequestParam MultipartFile upload,
                                         HttpSession session){
        Map<String,String> map=new HashMap<>();

        // 저장 경로
        String path=session.getServletContext().getRealPath("/chatsave");

        // 파일 이름
        String fileName=upload.getOriginalFilename();

        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddhhmmss");

        String uploadName="msg_"+sdf.format(new Date())+fileName; // 저장할 파일명

        try {
            upload.transferTo(new File(path+"/"+uploadName));
        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
        }
        map.put("upload",uploadName);

        return map;
    }

//    @PostMapping("/loginCheckForChat")
//    public ModelAndView loginCheckForChat(HttpSession session,
//                                          @RequestParam int accom_num){
//        // sender_id == info 일때,
//        // sender_id == business 일때,
//        // 위 두가지 모두 상황 보고 넘겨줘야 한다.
//
//        ModelAndView mv=new ModelAndView();
//
//        String info_id=(String) session.getAttribute("info_id");
//        String business_id=(String) session.getAttribute("business_id");
//
//        if (info_id==null && business_id==null){
//            mv.setViewName("/login/loginForm");
//        } else if (info_id!=null && business_id==null) {
//            mv.addObject("sender_id",info_id);
//        } else if (info_id==null && business_id!=null) {
//            mv.addObject("sender_id",business_id);
//        }
//        return mv;
//
//    }

}

