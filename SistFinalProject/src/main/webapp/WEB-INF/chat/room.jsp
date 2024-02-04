<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css" rel="stylesheet">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.0/jquery.min.js"></script>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="/messagejscss/room.css" type="text/css" rel="stylesheet">
<%--    <link href="/messagejscss/chat.css" type="text/css" rel="stylesheet">--%>
    <meta charset="UTF-8">
    <title>ChatRoom</title>

    <style>
        *{
            margin:0;
            padding:0;
        }
        .container{
            width: 100%;
            margin: 0 auto;
            padding: 25px;
        }
        .container h1{
            text-align: left;
            padding: 5px 5px 5px 15px;
            color: #f7323f;
            border-left: 3px solid #f7323f;
            margin-bottom: 20px;
        }
        .roomContainer{
            /*background-color: #F6F6F6;*/
            width: 100%;
            height: 500px;
            overflow: auto;
        }
        table.roomList{
            border: 1px solid #f7323f;
        }
        .roomList th{
            border: 1px solid #f7323f;
            background-color: #fff;
            color: #f7323f;
            padding: 3px 10px 3px 10px;
            font-size: 1.2em;
        }
        .roomList td{
            border: 1px solid #f7323f;
            background-color: #fff;
            text-align: left;
            color: #f7323f;
            padding: 3px 10px 3px 10px;
        }
        .roomList .num{
            width: 10%;
            text-align: center;
        }
        .roomList .room{
            /*width: 350px;*/
        }
        .roomList .go{
            width: 10%;
            text-align: center;
        }
        button{
            background-color: #f7323f;
            width: 50px;
            font-size: 14px;
            color: white;
            border: 1px solid gray;
            border-radius: 20px;
            padding: 3px;
            margin: 3px;
        }

        .inputTable th{
            padding: 5px;
        }
        .inputTable input{
            width: 30%;
            height: 25px;
        }

    </style>

</head>
<%
    String sender_id=(String) session.getAttribute("info_id");
%>

<script type="text/javascript">

    function goChatting(room_num,accom_num,roomName){

        location.href="goChattingRoom?room_num="+room_num;
    }


</script>
<body>
    <div class="container">
        <h1>채팅방</h1>
        <div id="roomContainer" class="roomContainer">
            <table id="roomList" class="roomList">
                <tr>
                    <th class="num" width="150">순서</th>
                    <th class="room" width="250">방 이름</th>
                    <th class="num" width="250">id</th>
                    <th class="go"width="100"></th>
                </tr>
                <c:forEach var="chatRoom" items="${chatRoomList}" varStatus="idx">
                    <tr>
                        <td class="num">${idx.count}</td>
                        <td class="room"><b>${chatRoom.accom_name}_${chatRoom.room_num}</b></td>
                        <td class="num">${chatRoom.sender_id}</td>
                        <td class="go"><button type="button" id="goRoomBtn" onclick="goChatting(${chatRoom.room_num})" accom_num="${chatRoom.accom_num}" >참여</button> </td>
                    </tr>
                </c:forEach>
            </table>
        </div>
    </div>
</body>
</html>