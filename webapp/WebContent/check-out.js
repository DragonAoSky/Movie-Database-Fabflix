let pay_form = $("#pay_form");

/**
 * Handle the data returned by LoginServlet
 * @param resultDataString jsonObject
 */

function handleListResult(resultData) {
    console.log("handleListResult: populating star table from resultData");

    var price = Number(resultData[0]["total"]);
    price = price * 10;
    // Populate the star table
    // Find the empty table body by id "star_table_body"
    $("#Total_price").text(price);
}


function handleLoginResult2(resultDataString) {
    let resultDataJson = JSON.parse(resultDataString);

    console.log("handle payment response");
    console.log(resultDataJson);
    console.log(resultDataJson["status"]);
    // If login succeeds, it will redirect the user to index.html
    if (resultDataJson["status"] === "success") {
        window.location.replace("confirmation.html");
    } else {
        // If login fails, the web page will display
        // error messages on <div> with id "login_error_message"
        console.log("show error message");
        console.log(resultDataJson["message"]);
        $("#login_error_message").text(resultDataJson["message"]);
    }
}

/**
 * Submit the form content with POST method
 * @param formSubmitEvent
 */
function submitLoginForm2(formSubmitEvent) {
    console.log("submit pay form");
    /**
     * When users click the submit button, the browser will not direct
     * users to the url defined in HTML form. Instead, it will call this
     * event handler when the event is triggered.
     */
    formSubmitEvent.preventDefault();

    $.ajax(
        "api/checkout", {
            method: "POST",
            // Serialize the login form to the data sent by POST request
            data: pay_form.serialize(),
            success: handleLoginResult2
        }
    );
}

// Bind the submit action of the form to a handler function
pay_form.submit(submitLoginForm2);

// Makes the HTTP GET request and registers on success callback function handleStarResult
jQuery.ajax({
    dataType: "json", // Setting return data type
    method: "GET", // Setting request method
    url: "api/checkout", // Setting request url, which is mapped by StarsServlet in Stars.java
    success: (resultData) => handleListResult(resultData) // Setting callback function to handle data returned successfully by the StarsServlet
});

