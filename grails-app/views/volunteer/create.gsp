<!doctype html>
<html class="no-js" lang="">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="x-ua-compatible" content="ie=edge">
        <title>Aloha Volunteer</title>
        <meta name="description" content="">
        <meta name="viewport" content="width=device-width, initial-scale=1">

        <asset:link rel="icon" href="favicon.ico" type="image/x-ico" />
        <!-- Place favicon.ico in the root directory -->
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/css/bootstrap.min.css" integrity="sha384-/Y6pD6FV/Vv2HJnA6t+vslU6fwYXjCFtcEpHbNJ0lyAFsXTsjBbfaDjzALeQsN6M" crossorigin="anonymous">
        <asset:stylesheet src="freelancer.css"/>
        <script src="https://use.fontawesome.com/3995bb261e.js"></script>
    </head>
    <body>
        <!--[if lte IE 9]>
            <p class="browserupgrade">You are using an <strong>outdated</strong> browser. Please <a href="https://browsehappy.com/">upgrade your browser</a> to improve your experience and security.</p>
        <![endif]-->

        <!-- Add your site or application content here -->
        <nav class="navbar navbar-expand-lg navbar-light fixed-top" id="mainNav">
          <div class="container">
            <a class="navbar-brand js-scroll-trigger" href="#page-top"><i class="fa fa-hand-rock-o" aria-hidden="true"></i> Aloha Volunteer</a>
            <!--<button class="navbar-toggler navbar-toggler-right collapsed" type="button" data-toggle="collapse" data-target="#navbarResponsive" aria-controls="navbarResponsive" aria-expanded="false" aria-label="Toggle navigation">
              Menu
              <i class="fa fa-bars"></i>
            </button>
            <div class="navbar-collapse collapse" id="navbarResponsive" style="">
              <ul class="navbar-nav ml-auto">
                <li class="nav-item">
                  <a class="nav-link js-scroll-trigger" href="#portfolio">Portfolio</a>
                </li>
                <li class="nav-item">
                  <a class="nav-link js-scroll-trigger" href="#about">About</a>
                </li>
                <li class="nav-item">
                  <a class="nav-link js-scroll-trigger" href="#contact">Contact</a>
                </li>
              </ul>
            </div>-->
          </div>
        </nav>
        <section style="margin-top:32px;">
          <div class="container">
              <div id="create-volunteer" class="content scaffold-create" role="main">
                  <g:if test="${flash.message}">
                      <div class="message" role="status">${flash.message}</div>
                  </g:if>
                  <g:hasErrors bean="${this.volunteer}">
                      <ul class="errors" role="alert">
                          <g:eachError bean="${this.volunteer}" var="error">
                              <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
                          </g:eachError>
                      </ul>
                  </g:hasErrors>
                  <g:form controller="volunteer" action="save" method="POST">
                      <fieldset class="form">
                          <f:all bean="volunteer"/>
                          <g:hiddenField name="nonce" value="${this.volunteer.nonce}"/>
                      </fieldset>
                      <input type="hidden" name="user_ref" value="${nonce}" />
                      <button type="submit" class="btn btn-success btn-lg" onclick="FB.AppEvents.logEvent('MessengerCheckboxUserConfirmation', null, {'app_id':'${appId}','page_id':'${pageId}','user_ref':'${nonce}'});">Submit</button>
                  </g:form>
              </div>
            <div class="fb-messenger-checkbox"
              origin="${origin}"
              page_id="${pageId}"
              messenger_app_id="${appId}"
              user_ref="${nonce}"
              prechecked="true"
              allow_login="true"
              size="large"></div>
          </div>
        </section>
        <script type="text/javascript">
         window.fbAsyncInit = function() {
            FB.Event.subscribe('messenger_checkbox', function(e) {
               console.log(e);
               if (e.event === 'rendered') {
                 console.log("Plugin was rendered");
               } else if (e.event === 'checkbox') {
                 var checkboxState = e.state;
                 console.log("Checkbox state: " + checkboxState);
                 if ("checked" === checkboxState) {
                   console.log("checkbox validated");
                 }
               } else if (e.event === 'not_you') {
                 console.log("User clicked 'not you'");
               } else if (e.event === 'hidden') {
                 console.log("Plugin was hidden");
               }
             });
             FB.init({
               appId      : '${appId}',
               xfbml      : true,
               version    : 'v2.10'
             });
          };
          (function(d, s, id){
            var js, fjs = d.getElementsByTagName(s)[0];
              if (d.getElementById(id)) {return;}
              js = d.createElement(s); js.id = id;
              js.src = "//connect.facebook.net/en_US/sdk.js";
              fjs.parentNode.insertBefore(js, fjs);
            }(document, 'script', 'facebook-jssdk')
          );
        </script>
        <script src="https://code.jquery.com/jquery-3.2.1.min.js" integrity="sha256-hwg4gsxgFZhOsEEamdOYGBf13FyQuiTwlAQgxVSNgt4=" crossorigin="anonymous"></script>
        <script>window.jQuery || document.write('<script src="js/vendor/jquery-3.2.1.min.js"><\/script>')</script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/js/bootstrap.min.js" integrity="sha384-h0AbiXch4ZDo7tp9hKZ4TsHbi047NrKGLO3SEJAg45jXxnGIfYzk4Si90RDIqNm1" crossorigin="anonymous"></script>
        <!-- Google Analytics: change UA-XXXXX-Y to be your site's ID.
        <script>
            window.ga=function(){ga.q.push(arguments)};ga.q=[];ga.l=+new Date;
            ga('create','UA-XXXXX-Y','auto');ga('send','pageview')
        </script>
        <script src="https://www.google-analytics.com/analytics.js" async defer></script> -->
    </body>
</html>
