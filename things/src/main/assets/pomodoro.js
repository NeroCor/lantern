var secondsPassed=0;

var working=null;
var buttonDownInterval=null;
var timerInterval=null;
var workTime=1500;

function startWorking(){
    secondsPassed = 0;
    document.getElementById("timer").classList.add('timer-go');
    document.getElementById("caption").textContent=workStrings[Math.floor(Math.random()*workStrings.length)];
    working = true;
    updateView()
}

function startPause(){
    secondsPassed = 0;
    document.getElementById("timer").classList.remove('timer-go');
    document.getElementById("caption").textContent=pauseStrings[Math.floor(Math.random()*pauseStrings.length)];
    working = false;
    updateView()
}

function buttonDown(){
    if(!buttonDownInterval){
        buttonDownInterval = setInterval(buttonPressed, 1500);
    }
    document.getElementById("timer").classList.add('timer-pressing');
};

function buttonPressed(){
    clearInterval(buttonDownInterval);
    buttonDownInterval = null;
    document.getElementById("timer").classList.remove('timer-pressing');

    if(working === null){
        timerInterval = setInterval(incTime, 1000);
    }
    
    if (working) {
        startPause();

    } else {
        startWorking();
    }
};

function incTime(){
    secondsPassed += 1;

    if(working && secondsPassed >= workTime){
        startPause();
    } else {
        updateView();
    }
}

function updateView(){
    if(working) {
        document.getElementById("time-text").textContent="Working: "+new Date((workTime-secondsPassed)*1000).toString().substring(19, 24);
        document.getElementById("button-text").textContent="Stop Working";
    } else {
        document.getElementById("time-text").textContent="Pause since "+new Date(secondsPassed*1000).toString().substring(19, 24);
        document.getElementById("button-text").textContent="Start Working";
    }
}

function buttonUp(){
    clearInterval(buttonDownInterval);
    buttonDownInterval = null;
    document.getElementById("timer").classList.remove('timer-pressing');
};

var workStrings=[
    "work! ...you piece of shit!",
    "lets hack!",
    "get shit done!",
    "Focus",
];

var pauseStrings=[
    "ohhh... PowerNap!",
    "420, blaze it",
    "Beer'o'clock!",
    "check Tinder",
    "go get yourself a Coffee",
];