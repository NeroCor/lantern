setCentralColor("#ABACAF");
function setCentralColor(centralColor){
    const textElement = document.getElementById("color-picker__color-text");
    const blockElement = document.getElementById("color-picker__info-box");
    const lineElement = document.getElementById("color-picker__line-box");
    blockElement.style.borderColor =  centralColor;
    lineElement.style.borderColor =  centralColor;

    document.getElementById("color-picker__color-text").innerText =  centralColor.toLowerCase();
    document.getElementById("color-picker__color-text-hex").innerText =  blockElement.style.borderColor.toString();
}