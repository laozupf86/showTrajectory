/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


var map;
var mapOptions = {center: new google.maps.LatLng(116,39), zoom: 12, mapTypeId: 
            google.maps.MapTypeId.ROADMAP};
var marks = {};
var tIDs = [];
    
function initialize(){
    map = new google.maps.Map(doucument.getElementById("map_canvas"), mapOptions);
}

function addPoints(){
    
    $.ajax({
        url:"",
        type: "POST",
        dataType: "json",
        data:"",
        error: function(data){
            alert(data);
        },
        success: function(data){
            for(var x in data ){
                marks[x['tid']] = createNewMarker(x['lat'], x['lng']);
                marks[x['tid']].setMap(map);
            }
        }
    });
}

function playTrajectory(){
    
}


function createNewMarker(coordiLat, coordiLng){
    
    var coordinate = new google.maps.LatLng(coordiLat, coordiLng);
    
    var marker = new google.maps.Marker({
        map: map,
        position: coordinate,
        title: ""      
    });
    
    return marker;
}

function pointMove(markers, tIDs, startLat, startLng, endLat, endLng, wait){
    
    var frames = {};
    var length = 0;
    
    //calculate the frame distance
    for (var id in tIDs){
        var frame = new Array();
        for(var i = 0; i < 1; i = i + 0.2){
            var currentLat = startLat[id] + i * (endLat - startLat);
            var currentLng = startLng[id] + i * (endLng - startLng);
            frame.push(new google.maps.LatLng(currentLat, currentLng));
        }
        frames[id] = frame;
    }
    //a move function for moving
    var move = function(markers, latlng, index, wait, tIDs){
        
        //start moving
        if(index !== length){
            for(var id in tIDs){
                markers[id].setPosition(latlng[id][index]);
            }
        }
        setTimeout(function(){
            move(markers, latlng, index + 1, wait, tIDs);
        }, wait);
        
    };
    
    move(markers, frames, 0, wait, tIDs);
    
    
    
}

function getTrajectoryPoint(startTime, endTime){
    
    var currentPoints = {};
    
    $.ajax({
        url:"",
        type: "POST",
        dataType: "json",
        data:startTime+endTime,
        error: function(){
            alert();
        },
        success: function(data){
            //get the sample point from server
            tIDs = new Array();
            for(var x in data['trajectory']){
                currentPoints[x['tID']] = new TP(x['tID'], x['startLng'], x['startLat'],
                x['endLng'], x['endLat'], x['speed']);
                tIDs.push(x['tID']);
            }
        }
    });
    
    return currentPoints;
}