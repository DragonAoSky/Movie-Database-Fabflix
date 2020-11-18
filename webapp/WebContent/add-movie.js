let add_movie_form = $("#add_movie");

/**
 * Handle the data returned by LoginServlet
 * @param resultDataString jsonObject
 */
function handleLoginResult(resultDataString) {

    let resultDataJson = JSON.parse(resultDataString);

    console.log("handle add movie response");
    console.log(resultDataJson);
    console.log(resultDataJson["status"]);

    // If login succeeds, it will redirect the user to index.html
    if (resultDataJson["status"] === "success") {
        console.log("show success message");
        console.log(resultDataJson["message"]);
        //$("#addmovie_error_message").text(resultDataJson["message"]);
        alert(resultDataJson["message"]);
    }
    else {
        // If login fails, the web page will display
        // error messages on <div> with id "login_error_message"
        console.log("show error message");
        console.log(resultDataJson["message"]);
        //$("#addmovie_error_message").text(resultDataJson["message"]);
        alert(resultDataJson["message"]);
    }
}

/**
 * Submit the form content with POST method
 * @param formSubmitEvent
 */
function submitLoginForm(formSubmitEvent) {
    console.log("submit add movie form");
    /**
     * When users click the submit button, the browser will not direct
     * users to the url defined in HTML form. Instead, it will call this
     * event handler when the event is triggered.
     */
    formSubmitEvent.preventDefault();

    $.ajax(
        "add-movie", {
            method: "GET",
            // Serialize the login form to the data sent by POST request
            data: add_movie_form.serialize(),
            success: handleLoginResult
        }
    );
}

// Bind the submit action of the form to a handler function
add_movie_form.submit(submitLoginForm);

