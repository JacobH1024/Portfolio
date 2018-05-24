<#include "base.ftl">
<#macro head>
<head>
    <meta charset="UTF-8">
    <title>Directory Search</title>
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
            min-width: 100%;
        }
    </style>
    <script type="application/javascript">
        function formSubmit() {
            $.ajax({
                type: "get",
                url: "api/directory",
                data: $('#search').serializeArray(),
                success: function (data) {
                    $("table[id^='data-table-clone']").remove(); // Clear previous
                    var returnData = JSON.parse(data);
                    var x;
                    for (var i = 0; i < returnData.data.length; i++) {
                        if (i < 1)
                            x = "data-table";
                        else
                            x = "data-table-clone" + (i - 1);
                        var clone = $('#data-table').clone().insertAfter('#' + x); // Create Clones
                        clone.show();
                        clone.prop("id", "data-table-clone" + i);
                        clone.attr("href", "#collapse" + i);
                        clone.find('#collapse').attr("id", "collapse" + i)
                        clone.find('.panel-heading').html("<h4 id='"+ returnData.data[i].id +"'>" + returnData.data[i].lastName + ", " + returnData.data[i].firstName + "</h4>");
                        var dHead = (returnData.data[i].isDepartmentHead)? "<p>Head of Department</p>" : "";
                        clone.find('.panel-body').html(dHead + "<p class='text-right'>" + "Department: " + returnData.data[i].department + "</p>" +
                                "<p class='text-right'>" + "Email: " + returnData.data[i].emailAddress + "</p>" +
                                "<p class='text-right'>" + "Phone Number: " + returnData.data[i].phoneNumber + "</p>"
                        );
                        // TODO: Add professor name
                    }
                }
            });
        }
    </script>
</head>
</#macro>
<#macro content>
<div class="row">
    <div class="col-sm-4">
        <form action="javascript:formSubmit()" id="search">
            <table>
                <thead>
                <tr>
                    <th style="text-align: center"><h3>Directory Search</h3></th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td>
                        <label class="control-label" for="department">Department:</label>
                        <select class="form-control" required name="department" id="department">
                            <option selected value="-1">Select Department</option>
                            <#list departments as department>
                                <option value="${department.getDepartmentID()}">${department.getName()}</option>
                            </#list>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td>
                        <label class="control-label" for="searchText">Name:</label>
                        <input class="form-control" type="text" id="searchText" name="searchText">
                    </td>
                </tr>
                <tr>
                    <td>
                        <button type="submit" class="btn btn-success">Search</button>
                    </td>
                </tr>
                </tbody>
            </table>
        </form>
    </div>
    <div class="col-sm-8">
        <h3 style="text-align: center">Results</h3>
        <hr>
        <table id="data-table" data-toggle="collapse" href="#collapse" class="table-bordered"
               style="margin-bottom: 20px; min-width: 100%;" hidden>
            <tbody>
            <tr>
                <td style="min-width: 100%;">
                    <div class="panel-group">
                        <div class="panel-group">
                            <div class="panel-heading">
                                <!-- Name -->

                            </div>
                        </div>
                    </div>
                </td>
            </tr>
            <tr>
                <td style="min-width: 100%;">
                    <div class="panel-group">
                        <div class="panel-group">
                            <div class="panel-body">
                                <!-- Everything else -->

                            </div>
                        </div>
                    </div>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</div>
</#macro>

<#--Renders the page-->
<@display_page userData/>