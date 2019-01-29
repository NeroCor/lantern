
function setCentralColor(centralColor){
    const textElement = document.getElementById("color-picker__color-text");
    const blockElement = document.getElementById("color-picker__show-block");
    blockElement.style.backgroundColor =  centralColor;

    document.getElementById("color-picker__color-text-hex").innerText =  blockElement.style.backgroundColor.toString();
}