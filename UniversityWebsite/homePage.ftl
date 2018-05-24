<#include "base.ftl">
<#macro head>
<head>
    <meta charset="UTF-8">
    <title>AAU Home</title>
    <style type="text/css">
        .list-group-item:hover {
            cursor: pointer;
            background-color: #d3d3d3;
        }
    </style>
</head>
</#macro>
<#macro content>

<style>
    .slideshow-container {
        width: 100%;
    }

    .mySlides {
        display: none;
    }
</style>

    <#if loginError>
    <div class="alert alert-danger">Login information incorrect. Please try again.</div>
    </#if>

    <#if logout>
    <div class="alert alert-success">Logout successful.</div>
    </#if>

<!-- Carousel -->
<div class="slideshow-container">
    <div class="mySlides">
        <div class="jumbotron jumbotron-fluid">
            <div class="container">
                <h1 class="display-3">Welcome to AAU!</h1>
                <p class="lead">Where your education is above average</p>
            </div>
        </div>
    </div>
    <div class="mySlides">
        <div class="jumbotron jumbotron-fluid">
            <div class="container">
                <h1 class="display-3">Register now!</h1>
                <p class="lead">Fall courses are now available for registration</p>
            </div>
        </div>
    </div>
    <div class="mySlides">
        <div>
            <div class="jumbotron jumbotron-fluid">
                <div class="container">
                    <h1 class="display-3">Bill Nye Lecture</h1>
                    <p>Join us 4/29/17 for an exclusive live lecture from the Bill Nye the Science Guy</p>
                </div>
            </div>
        </div>
    </div>
</div>


<div class="container">


</div>
<table class="table ">
    <tr>
        <td colspan="3"><h1 class="display-3">Why are we above average?</h1></td>
    </tr>
    <tr>
        <td><p class="lead">Your education is above average! We have only the best Professors</p></td>
        <td><p class="lead">Higher graduation rates! We have a 90% graduation rate!</p></td>
        <td><p class="lead">Low tuition! At AAU, tuition is among the lowest in the country</p></td>
        <td><p class="lead">Affordable housing! We have the best shanties in the state</p></td>
    </tr>
</table>

<script>
    var slideIndex = 0;
    function nextSlide() {
        var slides = document.getElementsByClassName("mySlides");
        slides[slideIndex].style.display = "none";
        slideIndex++;
        if (slideIndex > slides.length - 1) {
            slideIndex = 0
        }
        slides[slideIndex].style.display = "block";
    }
    var slides = document.getElementsByClassName("mySlides");
    slides[slideIndex].style.display = "block";
    setInterval(nextSlide, 7000);
</script>

<div class="row">
    <div class="col-lg-7 col-lg-offset-3">
        <form class="form-inline" action="javascript:updateEventList()" id="event-form">
            <label class="control-label" for="year">Year:</label>
            <select class="form-control" id="year">
                <option value="2017" selected>2017</option>
                <option value="2016">2016</option>
                <option value="2015">2015</option>
            </select>
            <label class="control-label" for="month">Month:</label>
            <select class="form-control" id="month">
                <option value="1">January</option>
                <option value="2">February</option>
                <option value="3">March</option>
                <option value="4" selected>April</option>
                <option value="5">May</option>
                <option value="6">June</option>
                <option value="7">July</option>
                <option value="8">August</option>
                <option value="9">September</option>
                <option value="10">October</option>
                <option value="11">November</option>
                <option value="12">December</option>
            </select>
            <label class="control-label" for="day">Day:</label>
            <select class="form-control" id="day">
                <option value="1">1</option>
                <option value="2">2</option>
                <option value="3">3</option>
                <option value="4">4</option>
                <option value="5">5</option>
                <option value="6">6</option>
                <option value="7">7</option>
                <option value="8">8</option>
                <option value="9">9</option>
                <option value="10">10</option>
                <option value="11">11</option>
                <option value="12">12</option>
                <option value="13">13</option>
                <option value="14">14</option>
                <option value="15">15</option>
                <option value="16">16</option>
                <option value="17">17</option>
                <option value="18">18</option>
                <option value="19">19</option>
                <option value="20">20</option>
                <option value="21">21</option>
                <option value="22">22</option>
                <option value="23">23</option>
                <option value="24">24</option>
                <option value="25">25</option>
                <option value="26" selected>26</option>
                <option value="27">27</option>
                <option value="28">28</option>
                <option value="29">29</option>
                <option value="30">30</option>
                <option value="31">31</option>
            </select>
            <button class="btn btn-primary" type="submit">Search</button>
            <#if userData.isDepartmentHead()>
                <button class="btn btn-success" type="button" data-toggle="modal" data-target="#createEventModal">
                    Create Event
                </button>
            </#if>
        </form>
    </div>
    <div class="modal fade" id="createEventModal" tabindex="-1" role="dialog">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">
                        <span>&times;</span>
                    </button>
                    <h4 class="modal-title" id="createEventTitle">Create New Event</h4>
                </div>
                <form action="javascript:newEvent()">
                    <div class="modal-body">
                        <div id="newEventAlert"></div>
                        <div>
                            <label for="new-event-title">Title:</label>
                            <input type="text" id="new-event-title" name="title"/>
                        </div>
                        <div>
                            <label for="new-event-desc">Description:</label>
                            <textarea id="new-event-desc" name="description" rows="2"
                                      cols="30">Description...</textarea><br>
                        </div>
                        <div>
                            <label class="control-label" for="new-event-begindate">Begin Date/Time:</label>
                            <input class="form-control" type="text" id="new-event-begindate" name="begindate"/>
                        </div>
                        <div>
                            <label class="control-label" for="new-event-enddate">End Date/Time:</label>
                            <input class="form-control" type="text" id="new-event-enddate" name="enddate"/>
                        </div>
                        <div>
                            <label class="control-label" for="new-event-studentcap">Student Capacity:</label>
                            <input class="form-control" type="text" id="new-event-studentcap" name="studentCapacity"/>
                        </div>
                        <div>
                            <label class="control-label" for="new-event-profcap">Professor Capacity:</label>
                            <input class="form-control" type="text" id="new-event-profcap" name="professorCapacity"/>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                        <button type="submit" class="btn btn-primary">Create Event</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
<div class="row">
    <!-- News -->
    <div class="col-lg-6" style="overflow-y: auto; margin-left: auto; margin-right: auto">
        <ul class="list-group" id="event-list"></ul>
    </div>
    <div id="modal-dump"></div>
    <script type="text/javascript">
        function newEvent() {
            var event = {};
            event.title = $('#new-event-title').val();
            event.description = $('#new-event-desc').val();
            event.beginDate = $('#new-event-begindate').val();
            event.endDate = $('#new-event-enddate').val();
            event.studentCapacity = $('#new-event-studentcap');
            event.professorCapacity = $('#new-event-profcap');

            $.post('/api/events/new', event, function (result) {
                var newEventAlert = $('#neweventalert');
                newEventAlert.empty();

                if (result === 'true') {
                    newEventAlert.append('<div class="alert alert-success">Event creation succeeded</div>');
                } else {
                    newEventAlert.append('<div class="alert alert-danger">Event creation failed</div>');
                }
            }, 'text');
        }

        function registerEvent(eventID) {
            $.get("/api/events/register", {eventID: eventID}, function (result) {

                var modalAlert = $('#eventRegisterAlert' + eventID);
                modalAlert.empty();

                if (result === 'true') {
                    modalAlert.append('<div class="alert alert-success">Register succeeded</div>');
                } else {
                    modalAlert.append('<div class="alert alert-danger">Register failed</div>');
                }

            }, 'text');
        }

        function updateEventList() {
            var eventList = $('#event-list');
            var modalDump = $('#modal-dump');
            var requestParams = {};
            requestParams.year = $('#year').val();
            requestParams.month = $('#month').val();
            requestParams.day = $('#day').val();
            $.get('/api/events', requestParams, function (events) {

                eventList.empty();
                modalDump.empty();
                eventList.prepend('<li class="list-group-item active">' +
                        '<h3 class=list-group-item-heading>News & Events (Upcoming Week)</h3>' +
                        '</li>');

                $.each(events.data, function (index, event) {
                    var eventID = event.eventID;
                    var beginDate = event.beginDate;
                    var endDate = event.endDate;
                    var title = event.title;
                    var description = event.description;
                    var department = event.department;
                    var creatorName = event.creatorName;
                    var studentCapacity = event.studentCapacity;
                    var professorCapacity = event.professorCapacity;
                    var studentsAttending = event.studentsAttending;
                    var professorsAttending = event.professorsAttending;

                    eventList.append('<li class="list-group-item" data-toggle="modal" data-target="#eventModal' + eventID + '">' +
                            '<b class="pull-left">' + beginDate + '</b><b class="pull-right">' + endDate + '</b><br>' +
                            '<h5>' + title + '</h5>' +
                            '</li>');

                    modalDump.append('<div class="modal fade" id="eventModal' + eventID + '" tabindex="-1" role="dialog">' +
                            '<div class="modal-dialog" role="document">' +
                            '<div class="modal-content">' +
                            '<div class="modal-header">' +
                            '<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>' +
                            '<h4 class="modal-title">' + title + '</h4>' +
                            '</div>' +
                            '<div class="modal-body">' +
                            '<div id="eventRegisterAlert' + eventID + '"></div>' +
                            '<h4>Beginning date/time:</h4>' + beginDate +
                            '<h4>Ending date/time:</h4>' + endDate +
                            '<h4>Hosted By: </h4>' + department + ' - ' + creatorName +
                            '<h4>Student Capacity: </h4>' + studentsAttending + '/' + studentCapacity +
                            '<h4>Professor Capacity: </h4>' + professorsAttending + '/' + professorCapacity +
                            '<hr>' +
                            '<p>' + description + '</p>' +
                            '</div>' +
                            '<div class="modal-footer">' +
                            '    <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>' +
                            '    <button type="button" class="btn btn-primary" onclick="registerEvent(' + eventID + ')">Register to Attend</button>' +
                            '</div></div></div></div>');
                });
            }, 'json');
        }

        updateEventList();
    </script>
</div>


</#macro>
<#--Renders the page-->
<@display_page userData/>