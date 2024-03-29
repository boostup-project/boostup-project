let stompClient = null;

$(document).ready(function() {
    console.log("Index page is ready");
});

function setAuthorization() {
    let auth = $('#authorization').val();
    let token = "Bearer " + auth;
    console.log(token);
    localStorage.setItem("access_token", token);
}

function clickButton() {
    let socket = new SockJS("http://localhost:8080/ws/chat");
    stompClient = Stomp.over(socket);
    let headers = {Authorization : localStorage.getItem('access_token')};
    stompClient.connect(headers, (frame) => {});
}

function clickGudokButton() {
    let chatRoomId = $("#chatRoomId").val();
    stompClient.subscribe('/topic/rooms/' + chatRoomId, (message) => {
        const data = JSON.parse(message.body);
        showMessage(data);
        console.log(data);
    });

    let memberId = $("#senderId").val();
    stompClient.subscribe('/topic/member/' + memberId, (message) => {
        const data = JSON.parse(message.body);
        console.log(data);
    });
}

function showMessage(message) {
    $("#messages").append(
        "<tr>"
        + "<td>" + message.displayName +"</td>"
        + "<td>" + message.message +"</td>"
        + "<td>" + message.createdAt +"</td>"
        + "</tr>");
}

function sendMessage() {
    console.log("sending message");
    const newMsg = {
        senderId: $("#senderId").val(),
        receiverId: $("#receiverId").val(),
        chatRoomId: $("#chatRoomId").val(),
        memberImage: "https://test.com/test.png",
        messageContent: $("#messageContent").val()
    }
    stompClient.send("/app/rooms", {}, JSON.stringify(newMsg));
}

function updateNotificationDisplay() {
    if (notificationCount == 0) {
        $('#notifications').hide();
    } else {
        $('#notifications').show();
        $('#notifications').text(notificationCount);
    }
}