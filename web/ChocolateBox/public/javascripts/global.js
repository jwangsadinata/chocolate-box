// Setting up Parse ===================================================
Parse.initialize("259williams");
Parse.serverURL = 'http://chocolate-box-wesleyan.herokuapp.com/parse';

// DOM Ready ==========================================================
$(document).ready(function() {
  // Populate the user table on initial page load
  // populateTable();

  // Username link click
  //$('#userList table tbody').on('click', 'td a.linkshowuser', showUserInfo);

  // Login User button click
  $('#btnLoginUser').on('click', loginUser);

  // Login User button click
  $('#btnRegisterUser').on('click', registerUser);

  // Delete User link click - using jQuery 'on' method
  //$('#userList table tbody').on('click', 'td a.linkdeleteuser', deleteUser);

  // Update User link click
  //$('#userList table tbody').on('click', updateUser);
});

function registerUser() {
  var x = document.getElementById("login-form");
  if (x.elements[0].value == "" || x.elements[1].value == "") {
    alert("username or password cannot be empty");
  }

  else if (x.elements[2].value == "" || x.elements[3].value == "") {
    alert("Please fill your full name and email address")
  }

  else {
    var username = x.elements[0].value;
    var password = x.elements[1].value;
    var fullname = x.elements[2].value;
    var email = x.elements[3].value;
    
    // Setting up user
    var user = new Parse.User();
    user.set("username", username);
    user.set("password", password);

    user.signUp(null, {
      success: function(user) {
        alert("Welcome to ChocolateBox");
      },
      error: function(user, error) {
        // Show the error message somewhere and let the user try again.
        alert("Error: " + error.code + " " + error.message);
      }
    });

    var Matching = Parse.Object.extend("Matching");
    var matching = new Matching();

    matching.set("username", username);
    matching.set("fullname", fullname);
    matching.set("emailAddress", email);
    matching.set("matchedUser", "");
    matching.set("hasMatched", false);
    matching.save();
  }
};

function loginUser() {
  var x = document.getElementById("login-form");
  if (x.elements[0].value == "" || x.elements[1].value == "") {
    alert("username or password cannot be empty");
  }

  else {
    var username = x.elements[0].value;
    var password = x.elements[1].value;
    var fullname = x.elements[2].value;
    var email = x.elements[3].value;

    Parse.User.logIn(username, password, {
      success: function(user) {
        window.location.href = "file:///Users/Jason/Downloads/sample/parse-js-blank/index.html";
      },
      error: function(user, error) {
        alert("Error: " + error.code + " " + error.message);
      }
    });
  }
}; 