/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


var map;
var mapOptions = {center: new google.maps.LatLng(116,39), zoom: 12, mapTypeId: 
            google.maps.MapTypeId.ROADMAP};
var marks = [];

    
function initialize(){
    map = new google.maps.Map(doucument.getElementById("map_canvas"), mapOptions);
    
}

function getTrajectoryPoint(startTime, endTime){
    $.ajax({
        url:"",
        type: "POST",
        dataType: "json",
        data:startTime+endTime,
        error: function(data){
            alert();
        },
        success: function(data){
            
        }
    });
}