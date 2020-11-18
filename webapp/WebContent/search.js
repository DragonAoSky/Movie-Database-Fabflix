function handleListResult(resultData) {
    console.log("handleListResult: populating genres info");

    let starTableBodyElement = jQuery("#genre");
    var longstring = "";

    for (let i = 0; i <resultData.length; i++)
    {
        longstring += '<a href="index.html?mode=2&browse=' + resultData[i]["genres"] + '&sort=1&num=25">' + resultData[i]["genres"] + '</a>';
        longstring += ' ';
    }
    starTableBodyElement.append(longstring);
}

// Makes the HTTP GET request and registers on success callback function handleStarResult
jQuery.ajax({
    dataType: "json", // Setting return data type
    method: "POST", // Setting request method
    url: "search", // Setting request url, which is mapped by StarsServlet in Stars.java
    success: (resultData) => handleListResult(resultData) // Setting callback function to handle data returned successfully by the StarsServlet
});