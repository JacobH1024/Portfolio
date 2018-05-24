<#include "base.ftl">
<#macro head>
<head>
    <meta charset="UTF-8">
    <title>Course Search</title>
    <!-- Custom CSS -->
    <style>
        .dropdown-menu {
            width: 250px;
        }

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

        hr.style2 {
            border-top: 3px double #8c8b8b;
            width: 555px;
        }
    </style>
    <script type="application/javascript">
        var disabled = []; // Keep track of disabled buttons
        function formSubmit() {
            $.ajax({
                type: "get",
                url: "api/courses",
                data: $('#search').serializeArray(),
                success: function (data) {
                    $("table[id^='data-table-clone']").remove(); // Clear previous
                    var returnData = JSON.parse(data);
                    var x;
                    var nextSemesterID = Number($('#nextSemesterID').val());
                    for (var i = 0; i < returnData.data.length; i++) {
                        if (i < 1)
                            x = "data-table";
                        else
                            x = "data-table-clone" + (i - 1);
                        var clone = $('#data-table').clone().insertAfter('#' + x); // Create Clones
                        clone.show();
                        clone.prop("id", "data-table-clone" + i);
                        clone.attr("href", "#collapse" + i);
                        clone.find('#collapse').attr("id", "collapse" + i);
                        <#if userData.isStudent()>
                            if (nextSemesterID === Number($('#semester').val())) {
                                clone.find('.panel-heading').html("<h4 id='" + returnData.data[i].id + "'>" + returnData.data[i].id + ": " + returnData.data[i].name + "</h4>" +
                                        "<p class='text-right'>" + "Professor: " + returnData.data[i].profID + "</p>" +
                                        "<p class='text-right'>" + "Credit Hours: " + returnData.data[i].creditHours + "</p>" +
                                        "<p class='text-right'>" + "Time: " + returnData.data[i].time + "</p>" +
                                        "<button id='registerButton"+returnData.data[i].offeredID +"' onclick='register(this)' name='offeredID' type='button' class='btn btn-primary btn-md' value='" +
                                                        returnData.data[i].offeredID + "'>Register</button>&nbsp;&nbsp;" +
                                        "<a class='btn btn-primary' data-toggle='collapse' href='#collapse" + i + "'>Expand</a>"
                                );
                            } else {
                                clone.find('.panel-heading').html("<h4 id='" + returnData.data[i].id + "'>" + returnData.data[i].id + ": " + returnData.data[i].name + "</h4>" +
                                        "<p class='text-right'>" + "Credit Hours: " + returnData.data[i].creditHours + "</p>" +
                                        "<p class='text-right'>" + "Time: " + returnData.data[i].time + "</p>" +
                                        "<a class='btn btn-primary' data-toggle='collapse' href='#collapse" + i + "'>Expand</a>"
                                );
                            }
                            <#if taken??>
                                <#list taken as takenCourse>
                                    $('#registerButton' + ${takenCourse.getOfferedID()}).prop("disabled", true); // Disables initially
                                </#list>
                            </#if>
                            for(var j = 0; j < disabled.length; j++){
                                $('#registerButton' + disabled[j]).prop("disabled", true); // Disables button when searched again but not refreshed
                            }
                        <#else>
                            clone.find('.panel-heading').html("<h4 id='" + returnData.data[i].id + "'>" + returnData.data[i].id + ": " + returnData.data[i].name + "</h4>" +
                                    "<p class='text-right'>" + "Credit Hours: " + returnData.data[i].creditHours + "</p>" +
                                    "<p class='text-right'>" + "Time: " + returnData.data[i].time + "</p>" +
                                    "<a class='btn btn-primary' data-toggle='collapse' href='#collapse" + i + "'>Expand</a>"
                            );
                        </#if>
                        clone.find('.panel-body').html("<p>" + returnData.data[i].description + "</p>");
                    }
                }
            });
        }

    </script>
    <script type="application/javascript">
        function register(button) {
            var offeredID = button.value;
            disabled.push(offeredID); // Adds to disabled list
            $.ajax({
                type: "post",
                url: "api/courses/register",
                dataType: "json",
                data: {"offeredID": offeredID},
                success: function (data) {
                    $('#registerSuccess').show(); // Show success
                    $('#registerSuccess').empty(); // Remove previous
                    $('#registerSuccess').html("<strong>Success!</strong> Congratulations you've registered for " + data.data[0].name);
                    $('#registerButton' + offeredID).prop("disabled", true); // Disable on click
                }
            })
        }
    </script>
</head>
</#macro>
<#macro content>
<div id="registerSuccess" class="alert alert-success" hidden>

</div>
<div class="row">
    <div class="col-sm-4">
        <!-- Search area -->
        <form action="javascript:formSubmit()" id="search">
            <table class="table-bordered" width="230px">
                <thead>
                <tr>
                    <th style="text-align: center"><h3>Search Courses</h3></th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td>
                        <label class="control-label" for="semester">Semester:</label>
                        <select required name="semester" id="semester" class="form-control">
                            <#list semesters as semester>
                                <option value="${semester.getSemesterID()}">${semester.toString()}</option>
                            </#list>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td>
                        <label class="control-label" for="categories">Categories:</label>
                        <select class="form-control" required name="categories" multiple id="categories"
                                style="align-content: center; height: 150px">
                            <#list categories as category>
                                <option value="${category.getCategoryID()}">${category.getName()}</option>
                            </#list>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td>
                        <label class="control-label" for="name">Name:</label>
                        <input type="text" name="name" id="name" class="form-control">
                    </td>
                </tr>
                <tr>
                    <td>
                        <input type="submit" class="btn btn-success">
                    </td>
                </tr>
                </tbody>
            </table>
        </form>
    </div>
    <!-- End Search -->
    <div class="col-sm-8">
        <!-- Display area -->
        <h3 style="text-align: center">Results</h3>
        <hr>
        <!-- Display courses -->
        <table id="data-table" class="table-bordered"
               style="margin-bottom: 40px;" hidden>
            <thead>

            </thead>
            <tbody>
            <tr>
                <td width="555px" height="50px">
                    <div class="panel-group">
                        <div class="panel-group">
                            <div class="panel-heading">
                                <!-- Class name and ID displayed here -->

                            </div>

                            <!-- Hidden part -->
                            <div id="collapse" class="panel-collapse collapse" style="margin-top: 30px;">
                                <div class="panel-body">
                                    <!-- Class Description, hours, professor name, etc here -->

                                </div>
                            </div>

                        </div>
                    </div>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</div>
<input type="hidden" id="nextSemesterID" value="${nextSemesterID}"/>
</#macro>

<#--Renders the page-->
<@display_page userData/>