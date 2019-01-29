var loaded = 0;
var weatherData = {};
var locationData = {};

//load data
fetch('https://api.openweathermap.org/data/2.5/weather?q=munich&appid=04d18076374a7ea9bbf6530d85f1864d')
  .then(function (response) {
    return response.json();
  })
  .then(function (wData) {
    weatherData = wData;
    checkAfterLoad();
  });

fetch('https://overpass-api.de/api/interpreter?data=[out:json];node[amenity=biergarten](48.114709327783,11.543626785278,48.161390966207,11.622848510742);out;')
  .then(function (response) {
    return response.json();
  })
  .then(function (lData) {
    locationData = lData
    checkAfterLoad();
  })
  
function checkAfterLoad() {
  loaded++;
  
  if(loaded >= 2){
    handleWeather();
  }
}

function handleWeather() {
  //show temperatur... kelvin to celsius
  var temp = parseInt(weatherData.main.temp - 273.15);
  var tempEl = document.getElementById('temp-txt');
  tempEl.innerHTML = temp + '°';

  //show weather icon
  var wIcon = weatherData.weather[weatherData.weather.length-1].id;
  var wIconEl = document.getElementById('w-icon');

  wIconEl.classList.add('wi-owm-'+wIcon);

  //add city name
  var cityName = weatherData.name;
  var cnEl = document.getElementById('city-name');
  cnEl.innerHTML = cityName;

  handleBiergarten(temp, wIcon);
}

function handleBiergarten(temp, type) {
  // if(type === 800 && temp >= 18) {
    document.body.classList.add('biergarten');
    var findGarten = true;
    while (findGarten) {
      var randomGarten = getRandomInt(locationData.elements.length - 1);
      var gardenData = locationData.elements[randomGarten].tags;
      if (gardenData['addr:street'] && gardenData['addr:housenumber'] && gardenData['name']){
        findGarten = false;
        var biergartenEl = document.getElementById('biergarten-data');
        biergartenEl.innerHTML = gardenData['addr:street'] + ' ' + gardenData['addr:housenumber'] + ', ' + gardenData['name'];
      }
    }
  // }
}

function getRandomInt(max) {
  return Math.floor(Math.random() * Math.floor(max));
}