<#include "base.ftl">
<#macro head>
<head>
    <!-- Custom CSS..for now -->
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
    </style>

    <script type="application/javascript">
        function sendFile(){
            var fd = new FormData($('#upload')[0]);
            var offeredID = ${offeredID};
            fd.append("offeredID", offeredID);
            $.ajax({
                type: "post",
                url: "/api/upload",
                contentType: false,
                processData: false,
                data: fd,
                success: function(){

                }

            })
        }
    </script>
    <script type="application/javascript">
        function deleteFile(fileID){
            $.ajax({
                type: "post",
                url: "/api/delete",
                data: {fileID: fileID}
            });
            location.reload();
        }
    </script>
    <script type="application/javascript">
        var offeredID = ${offeredID};
        var returnDataLength;
        function getGrades(){
            var userID = document.getElementById('userIDSelect').value; // Get selected student
            $('#userID').attr("value", userID); // Send userID to hidden input
            $.ajax({
                type: "get",
                url: "/api/grade/list",
                data: {userID: userID, offeredID: offeredID},
                success: function(data){
                    $('#updateFields').show();
                    $('#addFields').show();
                    $("tr[id^='gradeBook-clone']").remove(); // Clear previous clones
                    var returnData = JSON.parse(data);
                    var x;
                    returnDataLength = returnData.data.length;
                    for (var i = 0; i < returnDataLength; i++) {
                        if (i < 1)
                            x = "gradeBook";
                        else
                            x = "gradeBook-clone" + (i - 1);
                        var clone = $('#gradeBook').clone().insertAfter('#' + x); // Create Clones
                        clone.show();
                        clone.prop("id", "gradeBook-clone" + i);
                        clone.find('.gradeID').html("<p><input type='radio' name='assignID' value='"+ returnData.data[i].assID +"' form='updateGrade' required></p>");
                        clone.find('.gradeName').html("<p>" + returnData.data[i].name + "</p>");
                        clone.find('.gradeScore').html( "<p>" + returnData.data[i].points + "/" + returnData.data[i].possible + "</p>");
                        clone.find('.deleteAss').html("<button onclick='deleteAss(this.value)' value='"+ returnData.data[i].assID +"' class='btn btn-danger btn-sm'>" +
                                                      "<span class='glyphicon glyphicon-remove-sign'></span></button>")
                    }

                }
            })
        }
    </script>
    <script type="application/javascript">
        function updateGrade(){
            $.ajax({
                type: "post",
                url: "/api/grade/update",
                data: $('#updateGrade').serialize(),
                success: function (){
                    $('#newScoreInput').val('');
                    getGrades(); // Refresh table
            }
            });
        }
    </script>
    <script type="application/javascript">
        function addAssignment(){
            $.ajax({
                type: "post",
                url: "/api/assignment/add",
                data: $('#addAssignment').serialize(),
                success: function (){
                    var userID = document.getElementById('userIDSelect').value;
                    $('#addAssignName').val('');
                    $('#addAssignPoints').val('');
                    if(userID != 0) { // If no student selected don't call
                        getGrades(); // Refresh table
                    }
                }
            });
        }
    </script>
    <script type="application/javascript">
        function deleteAss(val){
            var assID = val;
            $.ajax({
                type: "post",
                url: "/api/assignment/delete",
                data: {assID: assID},
                success: function () {
                    getGrades(); // Refresh table
                }
            })
        }
    </script>
    <script>
        function updateHomePage(){
            var data = $('#updateHome').serializeArray();
            data.push({name: 'offeredID', value: offeredID});
            $.ajax({
                type: "post",
                url: "/api/homepage/update",
                data: data,
            });
            location.reload();
        }
    </script>
</head>
</#macro>

<#macro content>
<!-- Start Menu -->
<div class="row">
    <div class="col-md-2 col-md-offset-1">
        <h2>Menu</h2>
        <table width="200px">
            <tbody>
                <tr>
                    <td>
                        <div class="row">
                            <div class="col-md-12">
                                <ul class="nav nav-pills nav-stacked">
                                    <!-- These menus will load separate pages -->
                                    <li class="active"><a data-toggle="tab" href="#home">Home</a></li>
                                    <li><a data-toggle="tab" href="#files">Files</a></li>
                                    <li><a data-toggle="tab" href="#grades">Grades</a></li>
                                    <li><a data-toggle="tab" href="#contact">Contact</a></li>
                                </ul>
                            </div>
                        </div>
                    </td>
                </tr>
            </tbody>
        </table>
    </div>
<!-- End Menu -->

<!-- Start menu items content -->
    <div class="col-md-6 col-md-offset-1">
        <div class="tab-content">
            <div id="home" class="tab-pane fade in active">
                <h2>Home</h2>
                <table width="600px">
                    <tbody>
                        <tr>
                            <td>
                                <#list courseName as course>
                                    <h3>Welcome to ${course.getName()} ${course.getCourseID()}</h3>
                                </#list>
                            </td>
                        </tr>
                    </tbody>
                </table>
                </br>
                <#if student == "1">
                    <table width="600px">
                        <thead>

                        </thead>
                        <tbody>
                            <tr>
                                <td><p id="homePageText" style="word-wrap: break-word;">${text}</p></td>
                            </tr>
                        </tbody>
                    </table>
                </#if>
                <#if professor == "1">
                    <table width="600px">
                        <thead>

                        </thead>
                        <tbody>
                        <tr>
                            <td><p id="homePageText" style="word-wrap: break-word;">${text}</p></td>
                        </tr>
                        </tbody>
                    </table>
                    </br>
                    <table width="600px">
                        <thead>

                        </thead>
                        <tbody>
                        <form action="javascript:updateHomePage()" id="updateHome"></form>
                            <tr>
                                <td>
                                    <textarea type="text" name="text" style="width: 400px; height: 150px;" form="updateHome" required>${text}</textarea>
                                </td>
                                <td><button type="submit" class="btn btn-success" form="updateHome">Update</button></td>
                            </tr>
                        </tbody>
                    </table>
                </#if>
            </div>
            <div id="files" class="tab-pane fade">
                <h2>Files</h2>
                <table width="600px">
                    <tbody>
                    <tr>
                        <td>
                            <#if professor == "1" >
                                <!-- Upload for professor -->
                                <form id="upload" enctype="multipart/form-data" method="get" onsubmit="sendFile()">
                                    <label>Upload File: </label>
                                    <label class="btn btn-default btn-file">
                                        <input id="fileInput" type="file" name="file" required hidden>
                                    </label>
                                    <input type="submit" value="Upload">
                                </form>
                                </br>

                                <!-- List downloads for professor -->
                                <table class="table table-striped">
                                    <thead>
                                        <th>File List</th>
                                        <th></th>
                                    </thead>
                                    <tbody>
                                        <#list courseFiles as files>
                                            <tr>
                                                <td><a style="cursor:pointer" href="/api/download?fileID=${files.getFileID()}">${files.getFileName()}</a> </td>
                                                <td><button onclick="deleteFile(this.value)" value="${files.getFileID()}" class="btn btn-danger btn-sm"><span class="glyphicon glyphicon-remove-sign"></span></button></td>
                                            </tr>
                                        </#list>
                                    </tbody>
                                </table>
                            </#if>
                                </br>
                            <#if student == "1">
                                <!-- List downloads for student -->
                                <table class="table table-striped">
                                    <thead>
                                        <th>File List</th>
                                    </thead>
                                    <tbody>
                                        <#list courseFiles as files>
                                            <tr>
                                                <td><a style="cursor:pointer" href="/api/download?fileID=${files.getFileID()}">${files.getFileName()}</a> </td>
                                            </tr>
                                        </#list>
                                    </tbody>
                                </table>
                            </#if>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>

            <!-- Grades Section -->
            <div id="grades" class="tab-pane fade">
                <h2>Grades</h2>

                <!-- Grades for professors -->
                <#if professor == "1">
                <table class="table table-striped" style="width: 400px">
                    <select class="btn-primary" name="userID" id="userIDSelect" onchange="getGrades()">
                        <option value="0" selected disabled>Select Student</option>
                        <#list studentList as student>
                            <option value="${student.getUserID()}">${student.getFirstName()} ${student.getLastName()}</option>
                        </#list>
                    </select>
                    <thead>
                        <th></th>
                        <th>Assignment</th>
                        <th>Score</th>
                        <th></th>
                    </thead>
                    <tbody>
                    <form action="javascript:updateGrade()" id="updateGrade">
                        <tr id="gradeBook" hidden>
                            <td class="gradeID"></td>
                            <td class="gradeName"></td>
                            <td class="gradeScore"></td>
                            <td class="deleteAss"></td>
                        </tr>
                        <input id="userID" name="userID" value="" form="updateGrade" hidden>
                    </tbody>
                    </form>
                </table>
                <div id="updateFields" hidden>
                    <!-- Update Box Here -->
                    <table>
                        <thead>

                        </thead>
                        <tbody>
                            <tr>
                                <td><label>Enter new score: </label></td>
                                <td><input id="newScoreInput" name="newScore" style="width: 50px" form="updateGrade" pattern="[0-9]{1,4}" required></td>
                                <td><button type="submit" class="btn btn-success" form="updateGrade">Update</button></td>
                            </tr>
                        </tbody>
                    </table>
                </div>
                </br>
                <div id="addFields">
                    <table>
                        <thead>

                        </thead>
                        <tbody>
                        <form action="javascript:addAssignment()" id="addAssignment">
                            <tr>
                                <td><input id="addAssignName" type="text" name="name" style="width: 200px" placeholder="Enter new assignment.." form="addAssignment" required></td>
                                <td><input id="addAssignPoints" type="text" name="pointsPossible" style="width: 70px" placeholder="Score.." pattern="[0-9]{1,4}" form="addAssignment" required></td>
                                <td><button type="submit" class="btn btn-success" form="addAssignment">Add</button></td>
                            </tr>
                        </tbody>
                        <input type="text" name="offeredID" value="${offeredID}" form="addAssignment" hidden>
                    </table>
                </div>
                </#if>

                <!-- Grades for Students -->
                <#if student == "1">
                    <table class="table table-striped" style="width: 300px">
                        <thead>
                            <th>Assignment</th>
                            <th>Score</th>
                        </thead>
                        <tbody>
                            <#if gradeList??>
                                <#list gradeList as g>
                                    <tr>
                                        <td >${g.getAssignmentName()}</td>
                                        <td style="width: 50px">${g.getPoints()}/${g.getPointsPossible()}</td>
                                    </tr>
                                </#list>
                                <tr>
                                    <th>Total</th>
                                    <#if totalAchieved?? && totalPossible??>
                                        <td>${totalAchieved}/${totalPossible}</td>
                                    </#if>
                                </tr>
                            </#if>
                        </tbody>
                    </table>
                </#if>
            </div>
            <div id="contact" class="tab-pane fade">
                <h2>Contact</h2>
                <!-- TODO: Fill in professor contact information -->
                <table width="600px">
                    <tbody>
                        <tr>
                            <#list profInfo as professor>
                                <td>
                                    <h4>Name: </h4><p style="padding-left: 30px">${professor.getFullName()}</p>
                                    <h4>Email: </h4><p style="padding-left: 30px">${professor.getEmailAddress()}</p>
                                    <h4>Phone number: </h4><p style="padding-left: 30px">${professor.getPhoneNumber()}</p>
                                </td>
                            </#list>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
    <!-- End menu items content -->
</div>
</#macro>

<#--Renders the page-->
<@display_page userData/>