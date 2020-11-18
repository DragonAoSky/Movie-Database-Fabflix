/**
 * This example is following frontend and backend separation.
 *
 * Before this .js is loaded, the html skeleton is created.
 *
 * This .js performs two steps:
 *      1. Use jQuery to talk to backend API to get the json data.
 *      2. Populate the data to correct html elements.
 */


/**
 * Handles the data returned by the API, read the jsonObject and populate data into html elements
 * @param resultData jsonObject
 */
function handleListResult(resultData) {
    console.log("handleListResult: populating star table from resultData");

    // Populate the star table
    // Find the empty table body by id "star_table_body"
    let starTableBodyElement = jQuery("#star_table_body");


    for (let i = 0; i < Math.min(100, resultData.length); i++) {
        var quantity = resultData[i]["quantity"];
        var price = quantity * 10;
        let rowHTML = "";
        rowHTML += "<tr>";
        rowHTML += "<th>" + resultData[i]["movie_title"] + "</th>";
        rowHTML += "<th>" + price + "</th>";
        rowHTML += "<th>" + "<input id="+resultData[i]["movie_id"]+" type='button' " + "value='-' onclick='dec(this)'>" + " "
            + "<label>"+resultData[i]["quantity"]+"</label>" + " "
            + "<input id="+resultData[i]["movie_id"]+" type='button' " + "value='+' onclick='inc(this)'>" + "</th>";

        rowHTML +="<th><input id="+resultData[i]["movie_id"]+" type='button' " +
            "value='Delete' onclick='del(this)'></th>"
        // rowHTML += "<th>" + resultData[i]["movie_title"] + "</th>";
        // rowHTML += "<th>" + resultData[i]["price"] + "</th>";
        // rowHTML+="<th>" + resultData[i]["price"]*resultData[i]["quantity"] + "</th>";
        // rowHTML+="<th><input id="+resultData[i]["movie_id"]+" type='button' value='+' onclick='add(this)'>   " +
        //     "<label>"+resultData[i]["quantity"]+"</label>  " +
        //     " <input id="+resultData[i]["movie_id"]+" type='button' value='-' onclick='dec(this)'></th>"
        // rowHTML +="<th><input id="+resultData[i]["movie_id"]+" type='button' value='delete' onclick='del(this)'></th>"
        rowHTML += "</tr>";

        // Append the row created to the table body, which will refresh the page
        starTableBodyElement.append(rowHTML);
    }
}


/**
 * Once this .js is loaded, following scripts will be executed by the browser
 */

// Makes the HTTP GET request and registers on success callback function handleStarResult
jQuery.ajax({
    dataType: "json", // Setting return data type
    method: "GET", // Setting request method
    url: "api/showcart", // Setting request url, which is mapped by StarsServlet in Stars.java
    success: (resultData) => handleListResult(resultData) // Setting callback function to handle data returned successfully by the StarsServlet
});

function del(node) {
    var tr = $(node).parent().parent();
    var tb = $(tr).parent();
    var id  = $(node).attr("id");
    $.get("api/delete",{"id":id},function(data){
        if(data){
            $(tr).remove();
            alert("Successfully deleted!");
        }else{
            alert("Deleted failed");
        }
    });
}

function dec(node) {
    var tempC = $(node).parent().find("label:eq(0)").text();
    var count = $(node).parent().find("label:eq(0)").text();
    count--;
    if(count < 0)
    {
        alert("Quantity cannot below 1");
        count = 0;
    }
    $(node).parent().find("label:eq(0)").text(count);

    var price = count * 10;
    $(node).parent().parent().find("th:eq(1)").text(price);

    if(tempC >0)
    {
        var id  = $(node).attr("id");
        $.get("api/decreaseitem",{"id":id},function(data){
            if(data){
                //alert("Successfully dec!");
            }else{
                alert("Failed");
            }
        });
    }


}

function inc(node) {

    var count = $(node).parent().find("label:eq(0)").text();
    count++;
    $(node).parent().find("label:eq(0)").text(count);

    var price = count * 10;

    $(node).parent().parent().find("th:eq(1)").text(price);

    var id  = $(node).attr("id");
    $.get("api/increaseitem",{"id":id},function(data){
        if(data){
            //alert("Successfully inc!");
        }else{
            alert("Failed");
        }
    });
}



