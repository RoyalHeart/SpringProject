var isUsernameGuideShow = false;
function validateUsername() {
  var usernameRegex =
    /^[a-zA-Z0-9]([._-](?![._-])|[a-zA-Z0-9]){0,18}[a-zA-Z0-9]$/;
  var username = document.getElementById("username").value;
  var loginform = document.getElementById("loginform");
  var isValidUsername = usernameRegex.test(username);
  var p = document.createElement("p");
  p.innerHTML =
    "Please enter a valid username:\n" +
    "Start with alphanumeric characters, then either -._ but not consecutive or an alphanumeric, and end with an alphanumeric, length from 2 to 20";
  if (!isValidUsername) {
    if (!isUsernameGuideShow) {
      loginform.append(p);
      isUsernameGuideShow = true;
    }
    return false;
  } else {
    return true;
  }
}
var isAuthorInputWarningShow = false;
var isTitleInputWarningShow = false;
var form = document.getElementById("form");
function validateInput() {
  var form = document.getElementById("form");
  var author = document.getElementById("author").value;
  var title = document.getElementById("title").value;
  var authorcell = document.getElementById("authorcell");
  var titlecell = document.getElementById("titlecell");
  var authorWarning = document.createElement("p");
  var titleWarning = document.createElement("p");
  var success = document.createElement("p");
  success.style.color = "green";
  success.innerHTML = "Successfully";
  authorWarning.innerHTML = "Author can not be empty";
  authorWarning.style.fontSize = 10;
  titleWarning.style.fontSize = 10;
  titleWarning.innerHTML = "Title can not be empty";
  if (author == "") {
    if (!isAuthorInputWarningShow) {
      authorcell.append(authorWarning);
      isAuthorInputWarningShow = true;
    }
    return false;
  }
  if (title == "") {
    if (!isTitleInputWarningShow) {
      titlecell.append(titleWarning);
      isTitleInputWarningShow = true;
    }
    return false;
  }
  // form.append(success);
  return true;
}

function delay(time) {
  return new Promise((resolve) => setTimeout(resolve, time));
}
