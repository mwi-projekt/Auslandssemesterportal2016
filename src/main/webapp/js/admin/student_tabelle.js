$(document).ready(function () {

    myTable = $('#example').DataTable({
        ajax: '/getUser?rolle=3',
        columns: [
            {data: 'vorname'},
            {data: 'nachname'},
            {data: 'email'},
            {data: 'tel'},
            {data: 'mobil'},
            {data: 'studiengang'},
            {data: 'kurs'},
            {data: 'matrikelnummer'},
            {data: 'standort'},
            {
                data: null,
                className: "center",
                defaultContent: '<i style="cursor: pointer" id="edit" class="fas fa-edit" data-toggle="modal" data-target="#exampleModal"></i>'
            },
            {
                data: null,
                className: "center",
                defaultContent: '<i style="cursor: pointer" id="delete" class="fas fa-trash"></i>'
            }
        ],
        select: 'single',
        responsive: true
    });

    $("#example").on("mousedown", "#edit", function (e) {
        var myData = myTable.row($(this).parents('tr')).data();
        $("#vorname").val(myData.vorname);
        $("#nachname").val(myData.nachname);
        $("#email").val(myData.email);
        $("#telefonnummer").val(myData.tel);
        $("#mobilnummer").val(myData.mobil);
        $("#studiengang").val(myData.studiengang);
        $("#kurs").val(myData.kurs);
        $("#martikelnummer").val(myData.martikelnummer);
        $("#standort").val(myData.standort);
    })
});