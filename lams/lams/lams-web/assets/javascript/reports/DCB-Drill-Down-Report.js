$(document).ready(function() {

    var level = 1;
    $('#close').on("click", function() {
        window.close();
    })

    $('tbody tr').on("click", function() {
        switch (level) {
            case 1:
                //api call for getting election ward wise report
                alert("Election ward DCB");
                level = 2;
                break;
            case 2:
                alert("Locality ward DCB");
                level = 3;
                break;
            case 3:
                alert("Indivisual DCB");
                $(`table tr th:nth-child(${$('#boundery').index()+1}), table tr td:nth-child(${$('#boundery').index()+1})`).hide();
                $('#agreementNo').text("Agreement Number");
                level = 4;
                break;
            default:

        }
    })

    $('#reset').on("click",function() {
        if(level==4)
        {
          level=1;
          alert("ULB DCB");
          $(`table tr th:nth-child(${$('#boundery').index()+1}), table tr td:nth-child(${$('#boundery').index()+1})`).show();
          $('#agreementNo').text("No. of agreements");
        }

    })


});
