<%@ page import="model.User" %>
<%@ page import="model.Role" %>
<%@ page import="java.util.List" %>
<%--
  Created by IntelliJ IDEA.
  User: Fernando
  Date: 07/06/2018
  Time: 16:39
  To change this template use File | Settings | File Templates.
--%>
<%  User user = (User) request.getAttribute("User");
    User userLogged = (User) request.getAttribute("UserLogged");
    boolean editAllowed = (Boolean) request.getAttribute("editAllowed");
    String action = (String) request.getAttribute("action");%>
<% List<Role> roles = (List<Role>) request.getAttribute("Roles"); %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title><%=action%> a Role - Hotel Services</title>

    <meta name="google-signin-client_id" content="746890482047-c734fgap3p3vb6bdoquufn60bsh2p8l9.apps.googleusercontent.com">

    <script src="https://apis.google.com/js/platform.js" async defer></script>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>

    <link type="text/css" rel="stylesheet" href="../css/Diseno.css">
    <link type="text/css" rel="stylesheet" href="../css/materialize.min.css">

    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css?family=Roboto" rel="stylesheet">

    <style>
        .postLink{
            color: blue;
            font-size: large;
            cursor: pointer;

            transition: color 250ms ease-in;
        }
        .postLink:hover{
            color: green;
            font-size: larger;
        }
        body{
            margin: 0;
            padding: 0;
            background-color: white;
            font-family: Roboto, serif;
        }
        .whiteLink{
            color: white;
        }
        .whiteLink:hover{
            color: white;
        }
    </style>
</head>
<body>

<nav style="background-color: #67c9b3">
    <div class="nav-wrapper">
        <a class="whiteLink" href="../" style="padding: 0 0 0 20px; font-family: 'Product Sans', Roboto, serif; font-size: xx-large">Hotel Services</a>

        <div class="right valign-wrapper" style="padding: 0 0 0 10px; cursor: pointer;" onclick="changeUserOptions()">
            <%= userLogged.getName()%>
            <img src="<%=userLogged.getImgUrl()%>" alt="" class="circle responsive-img" style="padding: 5px" width="50px">
            <i class="material-icons right">arrow_drop_down</i>

            <div id="userOptions" style="background-color: white; border:solid 2px #67c9b3; position: absolute; width: auto; display: none;">
                <ul style="color: black">

                    <li style="padding: 0 5px;">
                        <a style="color: black" onclick="postRedirect('/users/view',{action:'closeSession'})">Cerrar Sesion</a>
                    </li>

                    <li id="cerrar" style="padding: 0 5px; cursor: pointer">
                        <i class="small material-icons">arrow_drop_up</i>
                    </li>
                </ul>
            </div>
        </div>

        <ul id="nav-mobile" class="right">
            <li class="active"><a class="whiteLink" href="">Users</a></li>
            <li><a class="whiteLink" onclick="postRedirect('../roles')">Roles</a></li>
            <li><a class="whiteLink" onclick="postRedirect('../access')">Access</a></li>
            <li><a class="whiteLink" onclick="postRedirect('../resources')">Resources</a></li>
        </ul>
    </div>
</nav>

<div class="container">
    <br />
    <span style="font-size: xx-large; font-family: 'Product Sans',Roboto,serif"><%=action%> a Role</span>
    <br />
    <br />

    <% if (editAllowed) {%>

    <form action="./add" method="post">

        <input name="userID" value="<%=user.getId()%>" type="hidden">
        <input name="action" value="update" type="hidden">

        Name:<br />
        <input name="userName" value="<%=user.getName()%>" placeholder="Name" required><br/>
        <br/>
        Email:<br />
        <input name="userEmail" value="<%=user.getEmail()%>" placeholder="Email" type="email" required><br />
        <br />

        <div class="row">
            <div class="col l10 m10">
                Profile Image link<br />
                <input name="userImg" value="<%=user.getImgUrl()%>" placeholder="Link" required oninput="cambiarImg(this)"><br />
                <br />
            </div>
            <div class="col l2 m2">
                <img id="sourceImg" src="<%=user.getImgUrl()%>" alt="" width="70px">
            </div>
        </div>

        Role of the User:<br />
        <select name="userRole" class="browser-default" required>
            <option value="" disabled selected>Choose a Role</option>
            <% for(Role role: roles){ %>
            <option value="<%=role.getKey()%>"><%=role.getName()%></option>
            <% } %>
        </select>
        <br />

        <button class="btn waves-effect waves-light" type="submit" name="action">Submit
            <i class="material-icons right">send</i>
        </button>

    </form>

    <% } else {%>

        <div class="row">
            <div class="col l8 m8" style="font-size: x-large">
                Name: <%=user.getName()%><br />
                Email: <%=user.getEmail()%><br />
                Role: <%= user.getRoleName()%><br />
            </div>
            <div class="col l4 m4">
                <img src="<%=user.getImgUrl()%>" width="96px"><br />
            </div>

        </div>

    <% } %>

    <hr />
    <br />
    <a href="../users" class="waves-effect waves-light btn whiteLink"><i class="material-icons left">arrow_back</i>Go Back</a>


</div>

<script>
    var sourceImg = document.getElementById("sourceImg");

    function cambiarImg(input) {
        sourceImg.src = input.value;
    }
</script>
<script>
    var userOptions = document.getElementById("userOptions");

    var isUserOptionsEnable = true;

    document.getElementById("cerrar").addEventListener("click", changeUserOptions());

    function changeUserOptions() {
        if (isUserOptionsEnable){
            userOptions.style.display = "none";
        } else {
            userOptions.style.display = "block";
        }

        isUserOptionsEnable = !isUserOptionsEnable;
    }
</script>
<script>
    function postRedirect(url, postData){

        var postForm = document.createElement("form");
        postForm.action = url;
        postForm.method = "POST";

        postForm.style.display = "none";

        for (var key in postData){
            if (postData.hasOwnProperty(key)){
                var input = document.createElement("input");
                input.type = "hidden";
                input.name = key;
                input.value = postData[key];
                postForm.appendChild(input);
            }
        }

        document.body.appendChild(postForm);

        postForm.submit();

    }
</script>
</body>
</html>
