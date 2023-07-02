var stompClient = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#receivedMessages").html("");
}

function connect() {
    var socket = new SockJS('/prototype');
    var headers = {};
    headers[$("#header").val()] = $("#token").val();

    stompClient = Stomp.over(socket);
    stompClient.connect(headers, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);

//        //채팅방 구별 X
//        stompClient.subscribe('/topic/group-chat', function (message) {
//            //showMessages(message); //문자열만 주고 받는 경우
//            showMessages(JSON.parse(message.body).content); //json 으로 주고 받는 경우
//        });

//        // 특정 채팅방 구독(입장)
//        var subRoomId = $("#subRoom").val();
//        stompClient.subscribe('/topic/group-chat/'+subRoomId, function (message) {
//            showMessages(JSON.parse(message.body).content); //json 으로 주고 받는 경우
//        });

        //여러개의 채팅방 구독(입장)
        stompClient.subscribe('/topic/group-chat/1', function (message) {
            showMessages(JSON.parse(message.body).content); //json 으로 주고 받는 경우
        });
        stompClient.subscribe('/topic/group-chat/2', function (message) {
            showMessages(JSON.parse(message.body).content); //json 으로 주고 받는 경우
        });

    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendMessage() {
    var pubRoomId = $("#pubRoom").val();

//    //문자열만 전송하는 경우
//    stompClient.send("/app/group-chat", {}, "just String!");

//    //JSON 으로 메세지 주고 받는 경우 & 채팅방 구별 x
//    stompClient.send("/app/group-chat", {}, JSON.stringify({'content': $("#message").val()}));

    //JSON 으로 메세지 주고 받는 경우 & 채팅방 구별 o
    stompClient.send("/app/group-chat/"+pubRoomId, {}, JSON.stringify({'content': $("#message").val()}));
}

function showMessages(message) {
    $("#receivedMessages").append("<tr><td>" + message + "</td></tr>");
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendMessage(); });
});