function handleListResult(resultData) {
    console.log("handleListResult: populating shopping if");

    var price = Number(resultData[0]["totalPrice"]);
    price = price * 10;
    // Populate the star table
    // Find the empty table body by id "star_table_body"
    $("#Total_price2").text(price);
    $("#orderID").text(resultData[0]["UserID"]);
}

// Makes the HTTP GET request and registers on success callback function handleStarResult
jQuery.ajax({
    dataType: "json", // Setting return data type
    method: "GET", // Setting request method
    url: "api/confirmation", // Setting request url, which is mapped by StarsServlet in Stars.java
    success: (resultData) => handleListResult(resultData) // Setting callback function to handle data returned successfully by the StarsServlet
});