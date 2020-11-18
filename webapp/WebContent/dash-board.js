let add_star_form = $("#add_star");

/**
 * Handle the data returned by LoginServlet
 * @param resultDataString jsonObject
 */
function handleListResult(resultData) {
    console.log("handleListResult: populating genres info");

    let starTableBodyElement = jQuery("#metadata");

    var temptable = '';
    var tempstring = '';
    var counter = 0;
    for (let i = 0; i <resultData.length; i++)
    {
        var longstring = "";

        //set first table
        if(counter == 0)
        {
            temptable = resultData[i]["tableName"];
            tempstring = '<h4>' + temptable + '</h4>';
            starTableBodyElement.append(tempstring);
            //tempstring = '<br>';
            //starTableBodyElement.append(tempstring);
        }
        var tablename = resultData[i]["tableName"];
        var colname = resultData[i]["COLUMN_NAME"];
        var typename = resultData[i]["TYPE_NAME"];
        var colsize = resultData[i]["COLUMN_SIZE"];
        var nullable = resultData[i]["NULLABLE"];

        //same table col
        if(tablename == temptable)
        {
            longstring = 'Column name: ' + colname + "\t\t\t" + 'Type: ' + typename + "\t\t\t" + 'Size: ' + colsize + "\t\t\t";
            if(nullable == 1)
                longstring += ' It can be null.';
            else
                longstring += ' It cannot be null.';
            starTableBodyElement.append(longstring);
            starTableBodyElement.append(document.createElement("br"));


        }
        //new table
        else
        {
            temptable = tablename;
            //set table name
            tempstring = '<br>';
            starTableBodyElement.append(tempstring);
            tempstring = '<h4>' + temptable + '</h4>';
            starTableBodyElement.append(tempstring);
            //insert col
            longstring = 'Column name: ' + colname + "\t\t\t" + 'Type: ' + typename + "\t\t\t" + 'Size: ' + colsize + "\t\t\t";
            if(nullable == 1)
                longstring += 'It can be null.';
            else
                longstring += 'It cannot be null.';
            starTableBodyElement.append(longstring);
            starTableBodyElement.append(document.createElement("br"));
        }





        // longstring += resultData[i]["info"];
        // longstring += '<br>';
        // starTableBodyElement.append(longstring);
        counter++;
    }

}


function handleLoginResult(resultDataString) {

    let resultDataJson = JSON.parse(resultDataString);

    console.log("handle add star response");
    console.log(resultDataJson);
    console.log(resultDataJson["status"]);

    // If login succeeds, it will redirect the user to index.html
    if (resultDataJson["status"] === "success") {
        console.log("show success message");
        console.log(resultDataJson["message"]);
        //$("#addstar_error_message").text(resultDataJson["message"]);
        alert(resultDataJson["message"]);
    }
    else {
        // If login fails, the web page will display
        // error messages on <div> with id "login_error_message"
        console.log("show error message");
        console.log(resultDataJson["message"]);
        //$("#addstar_error_message").text(resultDataJson["message"]);
        alert(resultDataJson["message"]);
    }
}

/**
 * Submit the form content with POST method
 * @param formSubmitEvent
 */
function submitLoginForm(formSubmitEvent) {
    console.log("submit add star form");
    /**
     * When users click the submit button, the browser will not direct
     * users to the url defined in HTML form. Instead, it will call this
     * event handler when the event is triggered.
     */
    formSubmitEvent.preventDefault();

    $.ajax(
        "dash-board", {
            method: "GET",
            // Serialize the login form to the data sent by POST request
            data: add_star_form.serialize(),
            success: handleLoginResult
        }
    );
}

// Bind the submit action of the form to a handler function
add_star_form.submit(submitLoginForm);

// Makes the HTTP GET request and registers on success callback function handleStarResult
jQuery.ajax({
    dataType: "json", // Setting return data type
    method: "POST", // Setting request method
    url: "dash-board", // Setting request url, which is mapped by StarsServlet in Stars.java
    success: (resultData) => handleListResult(resultData) // Setting callback function to handle data returned successfully by the StarsServlet
});

