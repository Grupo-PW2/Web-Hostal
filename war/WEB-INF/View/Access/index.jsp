<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.List"%>
<%@ page import="model.*"%>
<%
	List<Access> accesses = (List<Access>)request.getAttribute("accesses");
%>
<% User usuario = (User) request.getAttribute("User"); %>
<!DOCTYPE html>
<html>
<head>

    <title>Access - Hotel Services</title>

    <meta name="google-signin-client_id" content="746890482047-c734fgap3p3vb6bdoquufn60bsh2p8l9.apps.googleusercontent.com">

    <script src="https://apis.google.com/js/platform.js" async defer></script>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>

    <link type="text/css" rel="stylesheet" href="/css/Diseno.css">
    <link type="text/css" rel="stylesheet" href="/css/materialize.min.css">

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
        <a class="whiteLink hide-on-med-and-down" href="../" style="padding: 0 0 0 20px; font-family: 'Product Sans', Roboto, serif; font-size: xx-large">Hotel Services</a>

        <div class="right valign-wrapper" style="padding: 0 0 0 10px; cursor: pointer;" onclick="changeUserOptions()">
            <%= usuario.getName()%>
            <img src="<%=usuario.getImgUrl()%>" alt="" class="circle responsive-img" style="padding: 5px" width="50px">
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
            <li><a class="whiteLink" onclick="postRedirect('./users')">Users</a></li>
            <li><a class="whiteLink" onclick="postRedirect('./roles')">Roles</a></li>
            <li class="active"><a class="whiteLink">Access</a></li>
            <li><a class="whiteLink" onclick="postRedirect('./resources')">Resources</a></li>
        </ul>
    </div>
</nav>

<div class="container">
    <br />
    <span style="font-size: xx-large; font-family: 'Product Sans',Roboto,serif">Access</span>
    <br />
    <br />

    <a class="waves-effect waves-light btn whiteLink" onclick="postRedirect('/access/add')"><i class="material-icons left">add</i>Create</a>
    <br />
    <br />


    <% if (accesses.size() > 0) { %>

        <table class="striped responsive-table">
            <thead>
                <tr>
                    <td>ID</td>
                    <td>Role</td>
                    <td>Resource</td>
                    <td>Status</td>
                    <td>Actions</td>
                </tr>
            </thead>

            <tbody>

            <% for (Access e: accesses) { %>

                <tr>

                    <td nowrap><%= e.getId() %></td>
                    <td><%= e.getRoleName() %></td>
                    <td><%= e.getResourceName() %></td>
                    <td><%= e.getStatus() %></td>
                    <td>
                        <a href="/access/view?id=<%= e.getId() %>">View</a> |
                        <a href="/access/edit?id=<%= e.getId() %>">Edit</a> |

                        <form name="post_<%= e.getId() %>" style="display:none;" method="post" action="/access/delete"><input type="hidden" name="accessId" value="<%= e.getId() %>"/>
                        </form>
                        <a href="#" onclick="if (confirm('Are you sure you want to delete # <%= e.getId() %>?')) { document.post_<%= e.getId() %>.submit(); } event.returnValue = false; return false;">Delete</a></td>
                </tr>
            <% } %>

            </tbody>
        </table>
    <% } else { %>

    <span class="heading">No Accesses registered.</span>
    <% } %>

</div>

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