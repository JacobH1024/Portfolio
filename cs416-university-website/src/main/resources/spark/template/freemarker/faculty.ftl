<#include "base.ftl">
<#macro head>
<head>
    <title>Faculty</title>
    <!-- Custom CSS style -->
    <style>
        table {
            box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19);
        }

        table td {
            padding: 15px;
        }

        th {
            background-color: black;
            color: white;
        }

        table tr.announcement-row:hover td {
            cursor: pointer;
            background-color: #d3d3d3;
        }
    </style>
    <script>
        function postAnnounce() {
            var today = new Date();
            var dd = today.getDate();
            var mm = today.getMonth() + 1; //January is 0!
            var yyyy = today.getFullYear();

            if (dd < 10) {
                dd = '0' + dd
            }

            if (mm < 10) {
                mm = '0' + mm
            }

            today = yyyy + '-' + mm + '-' + dd;

            var data = $('#postAnnounce').serializeArray();
            data.push({name: 'date', value: today});
            <#list professor as professor>
                data.push({name: 'id', value: '${professor.getUserID()}'});
                data.push({name: 'author', value: '${professor.getFullName()}'});
            </#list>
            $.ajax({
                type: "post",
                url: "/api/announcements",
                data: data,
            });
            location.reload();
        }
    </script>
    <!-- Semester selection -->
    <script>
        function getCourse() {
            var selection = document.getElementById('semester').value;
            <#list professor as professor>
                var professorID = ${professor.getUserID()};
            </#list>
            $.ajax({
                type: "get",
                url: "/api/professor/" + professorID + "/courses",
                data: {selection: selection},
                success: function (data) {
                    $("tr[id^='courseRow-clone']").remove(); // Clear previous clones
                    var returnData = JSON.parse(data);
                    var x;
                    for (var i = 0; i < returnData.data.length; i++) {
                        if (i < 1)
                            x = "courseRow";
                        else
                            x = "courseRow-clone" + (i - 1);
                        var clone = $('#courseRow').clone().insertAfter('#' + x); // Create Clones
                        clone.show();
                        clone.prop("id", "courseRow-clone" + i);
                        clone.find('.courseID').html(returnData.data[i].id);
                        clone.find('.courseTitle').html("<a href='/course/" + returnData.data[i].offID + "'>" + returnData.data[i].title + "</a>");
                        clone.find('.courseSch').html(returnData.data[i].schedule);
                        clone.find('.courseCredit').html(returnData.data[i].creditHours);
                    }
                }
            });
        }
    </script>
</head>
</#macro>
<#macro content>
<!-- Professor info -->
<div class="row">
    <div class="col-md-3 col-md-offset-1">
        <h3 style="text-align: center">User Info</h3>
        <table class="table table-bordered table-condensed">
            <tbody>
            <tr>
                <#list professor as professor1>
                    <td><b>User ID</b></td>
                    <td>${professor1.getUserID()}</td>
                </tr>
                <tr>
                    <td><b>Email</b></td>
                    <td>${professor1.getEmailAddress()}</td>
                </tr>
                <tr>
                    <td><b>Name</b></td>
                    <td>${professor1.getFullName()}</td>
                </tr>
                <tr>
                    <td><b>Department</b></td>
                    <td>${professor1.getDepartment()}</td>
                </#list>
            </tr>
            </tbody>
        </table>
    </div>

    <!-- Faculty Announcements -->
    <div class="col-md-7 col-md-offset-1">
        <h3 style="text-align: center">Faculty Announcements</h3>
        <h5 style="text-align: center">Click announcements for more details</h5>
        <table class="table table-bordered">
            <thead>
            <tr>
                <th>Author</th>
                <th>Subject</th>
                <th>Date</th>
            </tr>
            </thead>
            <tbody>
                <#assign x = 1>
                <#list announcements as announcements>
                <tr class="announcement-row" data-toggle="modal" data-id="${announcements.getAnnouncementID()}"
                    data-target="#myModal${x}">
                    <td>${announcements.getAuthor()}</td>
                    <td>${announcements.getSubject()}</td>
                    <td>${announcements.getDate()}</td>
                </tr>
                    <#assign x++>
                </#list>
            </tbody>
        </table>
        <#list professor as professor>
            <#if professor.isDepartmentHead()>
                <button type="button" class="btn btn-info btn-lg" data-toggle="modal" data-target="#postModal">Create
                    Announcement
                </button>
            </#if>
        </#list>
    </div>
</div>

<!-- Modals to display full message after announcement table is clicked. -->
<!-- Modal -->
    <#assign y = 1>
    <#list announcements as announcements>
    <div class="modal fade" id="myModal${y}" role="dialog">
        <div class="modal-dialog">

            <!-- Modal content-->
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">x</button>
                    <h4 class="modal-title">Announcement</h4>
                    <p>By: ${announcements.getAuthor()} Sent: ${announcements.getDate()}</p>
                </div>
                <div class="modal-body">
                    <p>${announcements.getMessage()}</p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                </div>
            </div>

        </div>
    </div>
        <#assign y++>
    </#list>

<!-- Post announcement modal -->

<div class="modal fade" id="postModal" role="dialog">
    <div class="modal-dialog">

        <!-- Modal content-->
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">x</button>
                <h4 class="modal-title">Create Announcement</h4>
            </div>
            <div class="modal-body">
                <form action="javascript:postAnnounce()" id="postAnnounce"></form>
                <label for="subject">Enter subject (50 char max):</label>
                <input type="text" form="postAnnounce" id="subject" name="subject" style="margin-bottom: 20px" placeholder="Enter subject here...">
                <textarea type="text" name="message" style="width: 400px; height: 150px;" form="postAnnounce" placeholder="Enter message here..." required></textarea>
            </div>
            <div class="modal-footer">
                <button type="submit" class="btn btn-success" form="postAnnounce">Post</button>
            </div>
        </div>
    </div>
</div>

<div class="row" style="margin-top: 1.5cm">
    <!-- Semester select list. -->
    <!-- Selecting a semester will update the courses for that particular semester. -->
    <div class="col-md-2 col-md-offset-1">
        <p>View your courses for a particular semester.</p>
        <form>
            <div class="form-group">
                <label for="semester">Select a semester (select one):</label>
                <select class="form-control" id="semester" name="semesterID" onchange="getCourse()">
                    <option selected disabled>Select Semester</option>
                    <#list semesters as semester>
                        <option value="${semester.getSemesterID()}">${semester.toString()}</option>
                    </#list>
                </select>
            </div>
        </form>
    </div>

    <!-- Table to display courses taught by a certain professor. -->
    <div class="col-md-7 col-md-offset-1">
        <h3 style="text-align: center">Course Overview</h3>
        <table class="table table-bordered">
            <thead>
            <tr>
                <th>Course ID</th>
                <th>Title</th>
                <th>Schedule</th>
                <th>Credit Hours</th>
            </tr>
            </thead>
            <tbody>
            <tr id="courseRow" hidden>
                <td class="courseID"></td>
                <td class="courseTitle"></td>
                <td class="courseSch"></td>
                <td class="courseCredit"></td>
            </tr>
            </tbody>
        </table>
        <p>Click the course to view its page.</p>
    </div>
</div>
</#macro>
<@display_page userData/>
