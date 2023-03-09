function validateUsername() {
  var usernameRegex =
    /^[a-zA-Z0-9]([._-](?![._-])|[a-zA-Z0-9]){0,18}[a-zA-Z0-9]$/;
  var username = document.getElementById("username").value;
  var isValidUsername = usernameRegex.test(username);
  var p = document.createElement("p");
  if (!isValidUsername) {
    p.innerHTML =
      "Please enter a valid username:\n" +
      "Start with alphanumeric characters, then either -._ but not consecutive or an alphanumeric, and end with an alphanumeric, length from 2 to 20";
    username.append(p);
    return false;
  } else {
    return true;
  }
}

function validateInput() {
  var author = document.getElementById("author").value;
  if (author == null) {
    return false;
  } else {
    return true;
  }
}
