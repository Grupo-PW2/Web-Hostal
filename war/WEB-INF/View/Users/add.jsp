<%@ page import="model.User" %>
<%@ page import="java.util.List" %>
<%@ page import="model.Role" %><%--
  Created by IntelliJ IDEA.
  User: Fernando
  Date: 07/06/2018
  Time: 16:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% User user = (User) request.getAttribute("User"); %>
<% List<Role> roles = (List<Role>) request.getAttribute("Roles"); %>
<html lang="es">
<head>
    <title>Add a User - Hotel Services</title>

    <meta name="google-signin-client_id" content="746890482047-c734fgap3p3vb6bdoquufn60bsh2p8l9.apps.googleusercontent.com">

    <script src="https://apis.google.com/js/platform.js" async defer></script>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>

    <link type="text/css" rel="stylesheet" href="../../css/Diseno.css">
    <link type="text/css" rel="stylesheet" href="../../css/materialize.min.css">
    <link type="text/css" rel="stylesheet" href="../../css/Elements.css">

    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css?family=Roboto" rel="stylesheet">

    <script src="../../js/GlobalJs.js" async defer></script>

</head>
<body>

<nav class="nav-extended" style="background-color: #3f51b5">
    <div class="nav-wrapper" style="max-height: 64px">
        <a class="whiteLink hide-on-small-only" href="/" style="padding: 0 0 0 20px; font-family: 'Product Sans', Roboto, serif; font-size: xx-large">Hotel Services</a>
        &nbsp;&nbsp;Empleados
        <div class="right valign-wrapper" style="padding: 0 0 0 10px; cursor: pointer; min-width: 150px;" onclick="changeUserOptions()">

            <span style="min-width: 80px;">
                <%= user.getName()%>
            </span>
            <img src="<%=user.getImgUrl()%>" alt="" class="circle responsive-img" style="padding: 5px" width="50px">
            <i class="material-icons">arrow_drop_down</i>

            <div id="userOptions" style="background-color: white; border:solid 2px #67c9b3; position: absolute;
                width: auto; display: none;">
                <ul style="color: black">

                    <li style="padding: 0 5px;">
                        <a style="color: black" onclick="postRedirect('./users/view',{action:'closeSession'})">Log Out</a>
                    </li>

                    <li id="cerrar" style="padding: 0 5px; cursor: pointer">
                        <i class="small material-icons">arrow_drop_up</i>
                    </li>
                </ul>
            </div>
        </div>

        <ul id="nav-mobile" class="right hide-on-med-and-down">
            <li style="max-height: 62px">
                <a href="https://github.com/Grupo-PW2/Lab08" target="_blank" style="max-height: 62px">
                    <svg style="width: 32px; height: 32px; margin: 20px 0" aria-labelledby="simpleicons-github-icon" roleKey="img" xmlns="http://www.w3.org/2000/svg">
                        <title id="simpleicons-github-icon">
                            GitHub icon
                        </title>
                        <path fill="white" d="M12 .297c-6.63 0-12 5.373-12 12 0 5.303 3.438 9.8 8.205 11.385.6.113.82-.258.82-.577 0-.285-.01-1.04-.015-2.04-3.338.724-4.042-1.61-4.042-1.61C4.422 18.07 3.633 17.7 3.633 17.7c-1.087-.744.084-.729.084-.729 1.205.084 1.838 1.236 1.838 1.236 1.07 1.835 2.809 1.305 3.495.998.108-.776.417-1.305.76-1.605-2.665-.3-5.466-1.332-5.466-5.93 0-1.31.465-2.38 1.235-3.22-.135-.303-.54-1.523.105-3.176 0 0 1.005-.322 3.3 1.23.96-.267 1.98-.399 3-.405 1.02.006 2.04.138 3 .405 2.28-1.552 3.285-1.23 3.285-1.23.645 1.653.24 2.873.12 3.176.765.84 1.23 1.91 1.23 3.22 0 4.61-2.805 5.625-5.475 5.92.42.36.81 1.096.81 2.22 0 1.606-.015 2.896-.015 3.286 0 .315.21.69.825.57C20.565 22.092 24 17.592 24 12.297c0-6.627-5.373-12-12-12">
                        </path>
                    </svg>
                </a>
            </li>
            <li class="active"><a class="whiteLink active" href="../roles">Administración de Usuarios</a></li>
            <li><a class="whiteLink" href="../services">Administración de recursos</a></li>
            <li><a class="whiteLink" href="">Reportes de Ingresos</a></li>
            <li>|</li>
        </ul>

        <!--<div class="dropdown hide-on-large-only" style="padding: 0 10px; font-weight: bold" onclick="toggleDropdown()">Show Services</div>
        <div id="dropdownContent">
            <a href="#" onclick="postRedirect('./roles')">Roles</a>
            <a href="#" style="background-color: lightgray">Users</a>
            <a onclick="postRedirect('./resources')">Resources</a>
            <a onclick="postRedirect('./access')">Access</a>
        </div>-->

    </div>
    <div class="nav-content" style="background-color: #3949a3">
        <ul class="tabs tabs-transparent">
            <li class="tab"><a href="../roles">Roles</a></li>
            <li class="tab active"><a class="active" href="../users">Users</a></li>
            <li class="tab"><a href="../resources">Resources</a></li>
            <li class="tab"><a href="../access">Access</a></li>
        </ul>
    </div>
</nav>

<div class="container">
    <br />
    <span style="font-size: xx-large; font-family: 'Product Sans',Roboto,serif">Create a User</span>
    <br />
    <br />

    <form method="post" action="./add">
        <input name="action" value="create" type="hidden">

        Name of the User:<br />
        <input name="userName" placeholder="Name of the User" required><br />
        Email of the User:<br />
        <input name="userEmail" placeholder="Email" type="email" required>

        <div class="row">
            <div class="col l10 m10">
                Profile Image link<br />
                <input name="userImg" value="https://i.stack.imgur.com/34AD2.jpg" placeholder="Link" required oninput="cambiarImg(this)"><br />
                <br />
            </div>
            <div class="col l2 m2">
                <img id="sourceImg" src="https://i.stack.imgur.com/34AD2.jpg" alt="" width="70px">
            </div>
        </div>

        Role of the User:<br />
        <select name="userRole" class="browser-default" required>
            <option value="" disabled selected>Choose a Role</option>
            <% for(Role roleKey: roles){ %>
                <option value="<%=roleKey.getKey()%>"><%=roleKey.getName()%></option>
            <% } %>
        </select>
        <br />

        <button class="btn waves-effect waves-light indigo darken-1" type="submit" name="action">Submit
            <i class="material-icons right">send</i>
        </button>

    </form>
    <hr />
    <br />
    <a href="../users" class="waves-effect waves-light btn whiteLink indigo darken-1"><i class="material-icons left">arrow_back</i>Go Back</a>


</div>


<script>
    var sourceImg = document.getElementById("sourceImg");

    function cambiarImg(input) {
        sourceImg.src = input.value;
    }
</script>
</body>
</html>
