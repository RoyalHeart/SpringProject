function validateUsername() {
  var usernameRegex =
    /^[a-zA-Z0-9]([._-](?![._-])|[a-zA-Z0-9]){0,18}[a-zA-Z0-9]$/;
  var username = document.getElementById("username").value;
  var isValid = usernameRegex.test(username);
  if (!isValid) {
    alert(
      "Please enter a valid username:\n" +
        "Start with alphanumeric characters, then either -._ but not consecutive or alphanumeric and end with an alphanumeric, length from 2 to 20"
    );
  }
}
