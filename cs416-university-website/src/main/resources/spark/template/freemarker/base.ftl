<#macro navbar optionalParam=userData>
<!-- Start Nav Bar -->
<nav class="navbar navbar-inverse">
    <div class="container-fluid">
        <div class="navbar-header">
            <a class="navbar-brand" href="/">Above Average University</a>
        </div>
        <ul class="nav navbar-nav">
            <li><a href="/">Home</a></li>
            <!-- Possibly hide some of these pages based on login -->
            <li><a href="/courses">Courses</a></li>
            <li><a href="/directory">Directory</a></li>
            <#if userData.isLoggedIn()>
                <li><a href="/portal">Portal</a></li>
            </#if>
        </ul>
        <!-- Start Login -->
        <ul class="nav navbar-nav navbar-right">
            <#if !userData.isLoggedIn()>
                <li class="dropdown">
                    <a class="dropdown-toggle" data-toggle="dropdown" href="#">
                        <span class="glyphicon glyphicon-user"></span> Login
                    </a>
                    <ul class="dropdown-menu">
                        <li>
                            <form style="padding: 5px;" action="/login" method="post">
                                <div class="form-group">
                                    <label for="username">Username:</label>
                                    <input type="text" class="form-control" id="username" placeholder="Enter username"
                                           name="username">
                                </div>
                                <div class="form-group">
                                    <label for="password">Password:</label>
                                    <input type="password" class="form-control" id="password"
                                           placeholder="Enter password" name="password">
                                </div>
                            <#--<div class="checkbox" style="padding: 5px;">-->
                            <#--<label><input type="checkbox"> Remember Me</label>-->
                            <#--<label style="padding-left: 35px;"><input type="checkbox" id="signup"> Sign Up</label>-->
                            <#--</div>-->
                                <button type="submit" class="btn btn-default">Submit</button>
                            </form>
                        </li>
                    </ul>
                </li>
            <#else>
                <li>
                    <a href="/logout">Logout</a>
                </li>
            </#if>
        </ul>
        <!-- End Login -->
    </div>
</nav>
<!-- End Nav Bar -->
</#macro>

<#macro display_page userData>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" type="text/css" href="/css/bootstrap.css">

    <!-- Bootstrap and jQuery JS files -->
    <script type="text/javascript" src="/js/jquery-3.2.0.js"></script>
    <script type="text/javascript" src="/js/bootstrap.js"></script>
    <!-- Custom CSS -->
    <@head/>
</head>
<body>

    <@navbar userData/>
<div class="container">
    <@content/>
</div>
</body>
</html>
</#macro>