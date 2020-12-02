var ctx;

$(function () {
    ctx = {
        ajaxUrl: "authuser/meals/",
        datatableApi: $("#datatable").DataTable({
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "dateTime"
                },
                {
                    "data": "description"
                },
                {
                    "data": "calories"
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
                    "desc"
                ]
            ]
        })
    };
    makeEditable();
});

function clearFilter() {
    $('#filter').find(":input").val("");
    updateFilteredTable()
}

function updateFilteredTable() {
/*    $.get(ctx.ajaxUrl + "filter/", $('#filter').serialize(), function (data) {
        ctx.datatableApi.clear().rows.add(data).draw();
    });*/

        $.ajax({
            type: 'GET',
            url: ctx.ajaxUrl + "filter/",
            data: $('#filter').serialize()
        }).done (function (data) {
            ctx.datatableApi.clear().rows.add(data).draw();
            successNoty("Filtered");
        });
}