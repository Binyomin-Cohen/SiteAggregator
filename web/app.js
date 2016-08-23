var xhr = new XMLHttpRequest();
xhr.open('GET', 'http://localhost:8080/SiteAggregator/GetSitesData');
xhr.send(null);

var response;
var jResponse;

xhr.onreadystatechange = function () {
  var DONE = 4; // readyState 4 means the request is done.
  var OK = 200; // status 200 is a successful return.
  if (xhr.readyState === DONE) {
    if (xhr.status === OK) 
      response = xhr.responseText;
      console.log(response);
      updateDom(response);
    } else {
      console.log('Error: ' + xhr.status); // An error occurred during the request.
    }
  };
  
var updateDom = function(response){
  jResponse = JSON.parse(response);
  var titles = jResponse.googleTrends;
  var bh = jResponse.bh;
  var googleList = document.getElementById("googleList");
  var bhList = document.getElementById("bhList");
  for(var i = 0; i < titles.length; i++){
     var elem = document.createElement("li");
     elem.innerHTML = titles[i];
     googleList.appendChild(elem);
  }
    for(var j = 0; j < titles.length; j++){
     var elem = document.createElement("li");
     var text = bh[j].description + "  -  $" + bh[j].price;
     elem.innerHTML = text;
     bhList.appendChild(elem);
  }
};



