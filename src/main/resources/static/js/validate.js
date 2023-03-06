function validateUsername() {
  var usernameRegex =
    /^[a-zA-Z0-9]([._-](?![._-])|[a-zA-Z0-9]){0,18}[a-zA-Z0-9]$/;
  var username = document.getElementById("username").value;
  var isValidUsername = usernameRegex.test(username);
  if (!isValidUsername) {
    alert(
      "Please enter a valid username:\n" +
        "Start with alphanumeric characters, then either -._ but not consecutive or an alphanumeric, and end with an alphanumeric, length from 2 to 20"
    );
    return false;
  } else {
    return true;
  }
}
