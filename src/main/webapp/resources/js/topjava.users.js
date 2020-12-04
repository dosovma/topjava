var ctx;

// $(document).ready(function () {
$(function () {
    // https://stackoverflow.com/a/5064235/548473
    ctx = {
        ajaxUrl: "admin/users/",
        datatableApi: $("#datatable").DataTable({
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "name"
                },
                {
                    "data": "email"
                },
                {
                    "data": "roles"
                },
                {
                    "data": "enabled"
                },
                {
                    "data": "registered"
                },
                {
                    "defaultContent": "Edit",
                    "orderable": false
                },
                {
                    "defaultContent": "Delete",
                    "orderable": false
                }
            ],
            "order": [
                [
                    0,
                    "asc"
                ]
            ]
        })
    };
    makeEditable();
});

function changeColor(id) {
    var enable;
    if (document.getElementById('check_check').checked) {
        enable = true;
    } else {
        enable = false;
    }
    $.ajax({
        type: "GET",
        url: "rest/admin/users/" + id + "/enabled",
        data: {'enable':enable},
        beforeSend: function (xhr) {
            xhr.setRequestHeader ("Authorization", "Basic " + btoa("admin@gmail.com:admin"));
        },
    }).done(function () {
        $("#"+id).toggleClass('greenSelected');
    });
}