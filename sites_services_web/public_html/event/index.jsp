<!DOCTYPE html>
<%@ page contentType="text/html;charset=windows-1252"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
        <title>POST Event Attachment Sample</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
        <script src="../js/jquery-1.11.1.min.js"></script>
        <script src="../js/bootstrap.min.js"></script>
        <link href="../css/bootstrap.min.css" rel="stylesheet"/>
        <link href="../css/application.css" rel="stylesheet"/>
        <link rel="shortcut icon" type="image/png" href="../favicon.ico"/>
        <script>
        
            $(function() {
                jQuery.ajax({
                    url: '/cs/ContentServer?d=&pagename=get-events',
                    type: 'GET',
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/json'
                    },
                    'dataType': 'json',
                    success: function(result) {
                        var eventObject = document.getElementById("event");
                        $.each(result, function (i, obj) {
                            opt = document.createElement("option");
                            opt.text  = obj.name;
                            opt.value = obj.type + ':' + obj.id;
                            eventObject.appendChild(opt);
                        });
                    },
                     error: function (xhr, ajaxOptions, thrownError) {
                        alert ('Error:' + thrownError);
                     },
                    async:   false
                });
                var imageData = 'R0lGODlhDwAPAKECAAAAzMzM/////wAAACwAAAAADwAPAAACIISPeQHsrZ5ModrLlN48CXF8m2iQ3YmmKqVlRtW4MLwWACH+H09wdGltaXplZCBieSBVbGVhZCBTbWFydFNhdmVyIQAAOw==';
                $('#picture').attr('src', 'data:image/gif;base64,' + imageData);
                $("#btnCreate").click( function() {
                    var eventAttachment = {  
                        "comment" : $('#comment').val(),
                        "picture" : imageData,
                        "fullname": $('#fullname').val(),
                        "email"   : $('#email').val(),
                        "phone"   : $('#phone').val(),
                        "event"   : $('#event').val()
                    };
                    //Call jQuery ajax
                    $.ajax({
                        type: "POST",
                        contentType: "application/json; charset=utf-8",
                        url: '${pageContext.request.contextPath}/rest/event/create-attachment',
                        data: JSON.stringify(eventAttachment),
                        dataType: "json",
                        success: function (msg) {
                            alert('Success');
                        },
                        error: function (xhr, ajaxOptions, thrownError) {
                          alert(xhr.status);
                          alert(thrownError);
                        }
                    });
                });
            });
        
            
        
        </script>
    </head>
    <body>
        <form>
            <div class="form-group">
                <label for="event">Event:</label>
                <select class="form-control" id="event">
                </select>
            </div>
            <div class="form-group">
                <label for="comment">Comments</label>
                <textarea class="form-control" id="comment" name="comment" placeholder="Enter Comments...">I am interested in this cloud solution.</textarea>
            </div>
            <div class="form-group">
                <img id="picture" alt="Base64 encoded image" width="150" height="150"/>
            </div>
            <div class="form-group">
                <label for="fullname" class="col-sm-3 tp-caption label_white_bold">Full Name</label>
                <div class="col-sm-9">
                    <input class="form-control" id="fullname" name="fullname" value="Full Name" placeholder="Full Name"/>
                    <span class="help-block text-danger"></span>
                </div>
            </div>
            <div class="form-group">
                <label for="email" class="col-sm-3 tp-caption label_white_bold">Email</label>
                <div class="col-sm-9">
                    <input type="email" class="form-control" id="email" name="email" value="test@test.com" placeholder="Email"/>
                    <span class="help-block text-danger"></span>
                </div>
            </div>
            <div class="form-group">
                <label for="phone" class="col-sm-3 tp-caption label_white_bold">Phone</label>
                <div class="col-sm-9">
                    <input type="tel" class="form-control" id="phone" name="phone" value="0411354838" placeholder="Phone"/>
                    <span class="help-block text-danger"></span>
                </div>
            </div>
            <button type="button" id="btnCreate" class="btn btn-primary">Submit</button>
        </form>
    </body>
</html>