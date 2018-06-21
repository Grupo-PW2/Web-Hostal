<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="model.*"%>
<%@ page import="java.util.List"%>
<% User user = (User) request.getAttribute("User"); %>
<%
	List<Role> roles = (List<Role>)request.getAttribute("roles");
	List<Resource> resourses = (List<Resource>)request.getAttribute("resources");
%>
<!DOCTYPE html>
<html lang="en">
<head>

    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Add an access - Hotel Services</title>
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
            <%= user.getName()%>
            <img src="<%=user.getImgUrl()%>" alt="" class="circle responsive-img" style="padding: 5px" width="50px">
            <i class="material-icons right">arrow_drop_down</i>

            <div id="userOptions" style="background-color: white; border:solid 2px #67c9b3; position: absolute; width: auto; display: none;">
                <ul style="color: black">

                    <li style="padding: 0 5px;">
                        <a style="color: black" onclick="postRedirect('./users/view',{action:'closeSession'})">Cerrar Sesion</a>
                    </li>

                    <li id="cerrar" style="padding: 0 5px; cursor: pointer">
                        <i class="small material-icons">arrow_drop_up</i>
                    </li>
                </ul>
            </div>
        </div>

        <ul id="nav-mobile" class="right">
            <li><a class="whiteLink" onclick="postRedirect('../users')">Users</a></li>
            <li><a class="whiteLink" onclick="postRedirect('../roles')">Roles</a></li>
            <li class="active"><a class="whiteLink">Access</a></li>
            <li><a class="whiteLink" onclick="postRedirect('../resources')">Resources</a></li>
        </ul>
    </div>
</nav>

<div class="container">
    <br />
    <span style="font-size: xx-large; font-family: 'Product Sans',Roboto,serif">Add an Access</span>
    <br />
    <br />

    <script>
        var buttonDisabled = false;
    </script>

    <form id="mainForm" name="post" method="post" action="add" onsubmit="return safeSend()">

        <input type="hidden" name="info" value="agregar"/>

        Role:
        <% if(roles.size() > 0) {%>
            <select id="formRole" name="rolesl" class="browser-default" required oninput="accessExists()">
                <option value="!" disabled selected>Choose a Role</option>
                <% for (int i = 0;i<roles.size();i++) { %>
                    <% Role r = roles.get(i); %>
                    <option value="<%= r.getKey() %>"><%= r.getName() %></option>
                <% } %>

            </select>
        <% } else {%>
            <br />
            <span style="color: darkorange; font-size: x-large">No Roles registered.</span>
            <script>buttonDisabled = true</script>
        <% } %>
        <br />

        Resource:

        <% if (resourses.size() > 0) { %>
            <select id="formResource" name="resourcesl" class="browser-default" required oninput="accessExists()">
            <option value="!" disabled selected>Choose a Resource</option>
            <% for (Resource res: resourses) { %>
                <option value="<%= res.getKey() %>"><%= res.getUrl() %></option>
            <% } %>

            </select>
        <% } else { %>
            <br />
            <span style="color: darkorange; font-size: x-large">No Resources registered.</span>
            <script>buttonDisabled = true</script>
        <% } %>

        <br />

        <button id="sendButton" class="btn waves-effect waves-light" type="submit" name="action">Submit
            <i class="material-icons right">send</i>
        </button>

    </form>

    <hr />
    <br />
    <a href="../access" class="waves-effect waves-light btn whiteLink"><i class="material-icons left">arrow_back</i>Go Back</a>


</div>


<script>
    var sourceImg = document.getElementById("sourceImg");
    function cambiarImg(input) {
        sourceImg.src = input.value;
    }

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

    if (buttonDisabled)
        document.getElementById("sendButton").disabled = "disabled";

    var safeToSend = false;

    function accessExists() {

        var role = document.forms["mainForm"]["formRole"].value;
        var resource = document.forms["mainForm"]["formResource"].value;

        var req = new XMLHttpRequest();

        req.onreadystatechange = function () {

            if (req.readyState === 4 && req.status === 200){
                console.log("Response -> " + req.responseText);
                if (req.responseText === "true") {
                    safeToSend = false;
                }
                else
                    safeToSend = true;
                console.log("isSafe? -> " + safeToSend);
            }

        };

        req.open("get", "/access/add?info=check&rolesl=" + role + "&resourcesl=" + resource);
        req.send();

    }
    function safeSend() {
        if (!safeToSend){
            alert("This Access already exists");
            return false;
        } else
            return true;
    }

</script>
</body>

</html>