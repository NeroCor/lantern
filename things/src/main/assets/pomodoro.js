var startTime;

var timerStarted=false;
var buttonDownintervall=null;



function buttonDown(){
    buttonDownintervall = setInterval(buttonPressed, 3000);
    document.getElementById("timer").classList.add('timer-pressing');
};

function buttonPressed(){

    document.getElementById("timer").classList.toggle('timer-go');
    document.getElementById("timer").classList.remove('timer-pressing');
};

function buttonUp(){
    clearInterval(buttonDownintervall);
    document.getElementById("timer").classList.remove('timer-pressing');
};