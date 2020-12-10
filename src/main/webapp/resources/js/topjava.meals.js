var ctx, mealAjaxUrl = "profile/meals/";
const FROM_PATTERN = 'YYYY-MM-DDTHH:mm:ss';
const TO_PATTERN = 'YYYY-MM-DD HH:mm';

function updateFilteredTable() {
    $.ajax({
        type: "GET",
        url: mealAjaxUrl + "filter",
        data: $("#filter").serialize()
    }).done(updateTableByData);
}

function clearFilter() {
    $("#filter")[0].reset();
    $.get(mealAjaxUrl, updateTableByData);
}

$(function () {
    ctx = {
        ajaxUrl: mealAjaxUrl,
        datatableApi: $("#datatable").DataTable({
            "ajax": {
                "url": mealAjaxUrl,
                "dataSrc": ""
            },
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "dateTime",
                    "render": $.fn.dataTable.render.moment(FROM_PATTERN, TO_PATTERN)

                },
                {
                    "data": "description"
                },
                {
                    "data": "calories"
                },
                {
                    "orderable": false,
                    "defaultContent": "",
                    "render": renderEditBtn
                },
                {
                    "orderable": false,
                    "defaultContent": "",
                    "render": renderDeleteBtn
                }
            ],
            "order": [
                [
                    0,
                    "desc"
                ]
            ],
            "createdRow": function (row, data, dataIndex) {
                $(row).attr("data-mealExcess", data.excess);
            }
        }),
        updateTable: updateFilteredTable
    };
    makeEditable();
    $.datetimepicker.setLocale('ru');
    $('#datetimepicker_date_start').datetimepicker({timepicker:false, format:'Y-m-d'});
    $('#datetimepicker_date_end').datetimepicker({timepicker:false, format:'Y-m-d'});
    $('#datetimepicker_time_start').datetimepicker({datepicker:false, format:'H:i'});
    $('#datetimepicker_time_end').datetimepicker({datepicker:false, format:'H:i'});
    $('#dateTime_picker').datetimepicker({format:'Y-m-d H:i'});

});