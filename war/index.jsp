<%@ page import="model.User" %>
<%@ page import="model.Service" %>
<%@ page import="java.util.List" %>
<%  boolean isUserLogged;
    try {
        isUserLogged = Boolean.parseBoolean(request.getAttribute("isUserLogged").toString());
    }
    catch (NullPointerException e){
        isUserLogged = false;
    }
    User usuario =  (User) request.getAttribute("User");
    String serverResponse = (String) request.getAttribute("serverResponse");
    List<Service> serviceList = (List<Service>) request.getAttribute("servicesList");
    if (serverResponse == null) serverResponse = "!";
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Inicio - Hotel Service</title>

    <!--<link type="text/css" rel="stylesheet" href="./css/Diseno.css">-->
    <link type="text/css" rel="stylesheet" href="./css/materialize.min.css">
    <link type="text/css" rel="stylesheet" href="./css/Elements.css?v=2">

    <script src="./js/GlobalJs.js" async defer></script>

    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css?family=Roboto" rel="stylesheet">

    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <meta name="google-signin-client_id" content="746890482047-c734fgap3p3vb6bdoquufn60bsh2p8l9.apps.googleusercontent.com">

    <style>
        #mainHeader{
            background: url('http://www.hotelimperialeroma.it/data/mobile/hotel-imperiale-roma-camere-01-2.jpg') no-repeat local center;
            height: 40%;
            text-align: center;
            padding: 100px;
            background-size: 100%;
            overflow: hidden;

            transition: opacity 500ms, max-height 250ms,padding 500ms;
        }
        body{
            margin: 0;
            padding: 0;
            background-color: white;
            font-family: Roboto, serif;
        }
        .transition{
            overflow: hidden;
            height: auto;

            transition: max-height 250ms ease-in;
        }
    </style>

    <script src="https://apis.google.com/js/platform.js" async defer></script>
</head>

<body>

<% if(!isUserLogged){ %>

<div id="mainHeader">
    <h1 style="font-size: 50px; color: white; display: inline; font-weight: bold; font-family: 'Product Sans',serif">Hotel Services</h1>
</div>
<nav style="background-color: #67c9b3">
    <div class="nav-wrapper">
        <a href="./" class="brand-logo" style="padding: 0 0 0 20px; font-family: 'Product Sans', Roboto, serif">Hotel Services</a>
    </div>
</nav>

<br />
<div class="container">

    <div style="font-size: x-large">
        <span id="mainText">Bienvenido! Inicia Sesion para empezar.</span>
        <br />
        <br />
        <div id="mainDiv" class="transition" style="max-height: 400px">
            <div style="font-size: large">
                Con Google:
                <div class="g-signin2" data-onsuccess="onSignIn" style="margin: 10px"></div>
                <br />
                <br />

            </div>
        </div>
        <div id="loadingDiv" class="transition" style="max-height: 0">
            <div class="preloader-wrapper big active">
                <div class="spinner-layer spinner-blue">
                    <div class="circle-clipper left">
                        <div class="circle"></div>
                    </div><div class="gap-patch">
                    <div class="circle"></div>
                </div><div class="circle-clipper right">
                    <div class="circle"></div>
                </div>
                </div>

                <div class="spinner-layer spinner-red">
                    <div class="circle-clipper left">
                        <div class="circle"></div>
                    </div><div class="gap-patch">
                    <div class="circle"></div>
                </div><div class="circle-clipper right">
                    <div class="circle"></div>
                </div>
                </div>

                <div class="spinner-layer spinner-yellow">
                    <div class="circle-clipper left">
                        <div class="circle"></div>
                    </div><div class="gap-patch">
                    <div class="circle"></div>
                </div><div class="circle-clipper right">
                    <div class="circle"></div>
                </div>
                </div>

                <div class="spinner-layer spinner-green">
                    <div class="circle-clipper left">
                        <div class="circle"></div>
                    </div><div class="gap-patch">
                    <div class="circle"></div>
                </div><div class="circle-clipper right">
                    <div class="circle"></div>
                </div>
                </div>
            </div>
        </div>
    </div>
    <hr />
    Desarrollado por:<br />
    Fernando Araoz, 20173373<br />
    Jose Rodriguez, 20101650<br />
    Gustavo Turpo, 20173374<br />

</div>

<script>
    "use strict";

    function onSignIn(googleUser) {

        document.getElementById("mainDiv").style.maxHeight = "0";
        document.getElementById("loadingDiv").style.maxHeight = "100px";

        var profile = googleUser.getBasicProfile();

        document.getElementById("mainText").innerText = "Iniciaste sesion, te estamos redirigiendo...";

        document.getElementById("mainHeader").style.maxHeight = "0";
        document.getElementById("mainHeader").style.padding = "0";

        setTimeout(function () {
            postRedirect("/e/users/add",{
                userEmail : profile.getEmail(),
                userName : profile.getName(),
                userImg : profile.getImageUrl(),
                userRole : "Cliente",
                action : "logIn"
            });
        },500);

    }
</script>

<% } else { %>

<nav style="background-color: #67c9b3">
    <div class="nav-wrapper">
        <a class="whiteLink hide-on-small-only" href="#" style="padding: 0 0 0 20px; font-family: 'Product Sans', Roboto, serif; font-size: xx-large">Hotel Services</a>

        <div class="right valign-wrapper" style="padding: 0 0 0 10px; cursor: pointer; min-width: 180px;" onclick="changeUserOptions()">

            <span id="nombreUsuario" style="min-width: 80px;">
                <%= usuario.getName()%>
            </span>
            <img src="<%=usuario.getImgUrl()%>" alt="" class="circle responsive-img" style="padding: 5px" width="50px">
            <i class="material-icons">arrow_drop_down</i>

            <div id="userOptions" style="background-color: white; border:solid 2px #67c9b3; position: absolute;
                width: auto; display: none;">
                <ul style="color: black">

                    <li style="padding: 0 5px;">
                        <a style="color: black" onclick="postRedirect('./e/users/view',{action:'closeSession'})">Cerrar Sesion</a>
                    </li>

                    <li id="cerrar" style="padding: 0 5px; cursor: pointer">
                        <i class="small material-icons">arrow_drop_up</i>
                    </li>
                </ul>
            </div>
        </div>

        <ul id="nav-mobile" class="right hide-on-med-and-down">
            <li>
                <a href="https://github.com/Grupo-PW2/Web-Hostal" target="_blank">
                    <svg style="width: 32px; height: 32px; margin: 20px 0" aria-labelledby="simpleicons-github-icon" roleKey="img" xmlns="http://www.w3.org/2000/svg">
                        <title id="simpleicons-github-icon">
                            GitHub icon
                        </title>
                        <path fill="white" d="M12 .297c-6.63 0-12 5.373-12 12 0 5.303 3.438 9.8 8.205 11.385.6.113.82-.258.82-.577 0-.285-.01-1.04-.015-2.04-3.338.724-4.042-1.61-4.042-1.61C4.422 18.07 3.633 17.7 3.633 17.7c-1.087-.744.084-.729.084-.729 1.205.084 1.838 1.236 1.838 1.236 1.07 1.835 2.809 1.305 3.495.998.108-.776.417-1.305.76-1.605-2.665-.3-5.466-1.332-5.466-5.93 0-1.31.465-2.38 1.235-3.22-.135-.303-.54-1.523.105-3.176 0 0 1.005-.322 3.3 1.23.96-.267 1.98-.399 3-.405 1.02.006 2.04.138 3 .405 2.28-1.552 3.285-1.23 3.285-1.23.645 1.653.24 2.873.12 3.176.765.84 1.23 1.91 1.23 3.22 0 4.61-2.805 5.625-5.475 5.92.42.36.81 1.096.81 2.22 0 1.606-.015 2.896-.015 3.286 0 .315.21.69.825.57C20.565 22.092 24 17.592 24 12.297c0-6.627-5.373-12-12-12">
                        </path>
                    </svg>
                </a>
            </li>
            <li><a class="whiteLink" href="./me/history">Historial de Compras</a></li>
            <li>|</li>
        </ul>

    </div>
</nav>

<div class="container">
    <br />
    <span style="font-size: xx-large; font-family: 'Product Sans',Roboto,serif">Inicio</span>
    <br />
    <br />

    <%if (!serverResponse.equals("!")){ %>

    <div id="serverResponse">
        <div style="margin: 10px"></div>
    </div>
    <script>
        var respDiv = document.getElementById("serverResponse");

        var responseData = JSON.parse('<%=serverResponse%>');

        respDiv.style.backgroundColor = responseData["color"];
        respDiv.innerHTML = "<div style=\"margin: 10px\">" + responseData["response"] + "</div>";

        setTimeout(function () {
            respDiv.style.maxHeight = "500px";
            setTimeout(function () {
                respDiv.style.maxHeight = "0";
            },1500);
        },300);

    </script>

    <% } %>
    <br />
    <br />

    <div style="font-size: x-large">
        Bienvenido <%=usuario.getName()%>.<br />

        <% if (Boolean.parseBoolean(request.getAttribute("hasAccess").toString())){ %>
            <div style="margin: 20px 0;border-radius: 2px; box-shadow: 2px 2px lightgray; background-color: #3f51b5; padding: 10px;
                color: white; cursor: pointer" onclick="window.location.href = ('/e/')">
                <i class="material-icons left" style="font-size: 2.5rem">how_to_reg</i>
                Ir al Area de Empleados
            </div>
        <% } %>

        <div class="card" style="padding: 10px">
            <span style="font-family: 'Product Sans',Roboto, serif; font-size: xx-large">Contrata un Servicio:</span>
            <div>
                <% for (Service service: serviceList) {%>

                <div class="row" style="padding: 0 25px">
                    <%=service.getName()%>
                    <br />
                    <div class="col l8 m8" style="font-size: large">
                        <%=service.getDescription()%>
                    </div>
                    <div class="col l4 m4">
                        <%=service.getPrice()%> S/.&nbsp;&nbsp;&nbsp;&nbsp;
                        <a class="waves-effect waves-light btn whiteLink" style="background-color: #64c2ad"
                           onclick="postRedirect('./e/trans/add',{'action':'userAddTransaction','serviceKey':'<%=service.getKey()%>'})">
                            <i class="material-icons left">add_shopping_cart</i>
                            Comprar
                        </a>
                    </div>
                </div>
                <hr />

                <% } %>
            </div>
        </div>
        <br />


    </div>


</div>

<% } %>

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
