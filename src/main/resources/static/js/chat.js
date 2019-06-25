var name = "";
var messageText = "";
var d = new Date();
var time = "";
var counter = 0;

function sendMessage() {
  counter += 1;
  name = document.getElementById("chatname").value;
  messageText = document.getElementById("defintext").value;

  timeHour = d.getHours();
  timeMin = d.getMinutes();

  time = timeHour + ":" + timeMin;

  var $message = $("<div>");
  if (counter % 2 == 0) {
    $message.addClass("chat client");
  } else {
    $message.addClass("chat administrator");
  }
  $message.append('<div class= "chatarea">' + "</div>");
  //   $message.append('<div class= "user-photo">' + "</div>");
  $message.append(
    '<label id="msg-title">' + "<b>" + name + "</b>" + "</label>"
  );
  $message.append('<p class="chat-message">' + messageText + "</p>");
  $message.append('<span id="msg-time">' + time + "</span>");

  $(".chatlogs").append($message);

  $("#defintext").val("");

  return $message;
}

$("#defintext").on("keyup", function(event) {
  if (event.keyCode == 13) {
    if ($("#chatname").val() == "" || $("#defintext").val() == "") {
      alert("Please fill out all the fields!");
    } else {
      sendMessage();
    }
  }
});

$("#chatname").on("keyup", function(event) {
  if (event.keyCode == 13) {
    if ($("#chatname").val() == "" || $("#defintext").val() == "") {
      alert("Please fill out all the fields!");
    } else {
      sendMessage();
    }
  }
});
