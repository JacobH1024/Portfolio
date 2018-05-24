<#include "base.ftl">
<#macro head>
<head>
    <title>Student</title>
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
    <!-- Semester selection -->
    <script>
        function getCourse() {
            var selection = $('#semester').val();
            var studentId = ${student.getUserID()};
            $.ajax({
                type: "get",
                url: "/api/student/" + studentId + "/courses",
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
                        clone.find('.courseGrade').html(returnData.data[i].courseGrade);
                        clone.find('.courseStatus').html(returnData.data[i].courseStatus);
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
                <td><b>User ID</b></td>
                <td>${student.getUserID()}</td>
            </tr>
            <tr>
                <td><b>Email</b></td>
                <td>${student.getUserName()}@aau.edu</td>
            </tr>
            <tr>
                <td><b>Name</b></td>
                <td>${student.getFullName()}</td>
            </tr>
            <tr>
                <td><b>Major</b></td>
                <td>${student.getMajorDegree()}</td>
            </tr>
            <tr>
                <td><b>Minor</b></td>
                <td>${student.getMinorDegree()}</td>
            </tr>
            <tr>
                <td><b>Student Type</b></td>
                <#if student.isGradStudent()>
                    <td>Graduate</td>
                <#else >
                    <td>Undergrad</td>
                </#if>
            </tr>
            </tbody>
        </table>
    </div>

    <!-- Faculty Announcements -->
    <div class="col-md-7 col-md-offset-1">
        <h3 style="text-align: center">Department Announcements</h3>
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

<div class="row" style="margin-top: 1.5cm">
    <!-- Semester select list. -->
    <!-- Selecting a semester will update the courses for that particular semester. -->
    <div class="col-md-2 col-md-offset-1">
        <p>View your courses for a particular semester.</p>
        <form>
            <div class="form-group">
                <label for="semester">Select a semester:</label>
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
    <div class="col-md-9">
        <h3 style="text-align: center">Course Overview</h3>
        <table class="table table-bordered">
            <thead>
            <tr>
                <th>Course ID</th>
                <th>Title</th>
                <th>Schedule</th>
                <th>Credit Hours</th>
                <th>Grade</th>
                <th>Status</th>
            </tr>
            </thead>
            <tbody>
            <tr id="courseRow" hidden>
                <td class="courseID"></td>
                <td class="courseTitle"></td>
                <td class="courseSch"></td>
                <td class="courseCredit"></td>
                <td class="courseGrade"></td>
                <td class="courseStatus"></td>
            </tr>
            </tbody>
        </table>
        <p>Click the course to view its page.</p>
    </div>
</div>
</#macro>
<@display_page userData/>