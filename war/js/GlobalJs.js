var dropdownOpen = false;
var dropdown = document.getElementById("dropdownContent");
function toggleDropdown() {
    (dropdownOpen)? dropdown.style.maxHeight = "0": dropdown.style.maxHeight = "300px";
    dropdownOpen = !dropdownOpen;
}

var userOptions = document.getElementById("userOptions");
var isUserOptionsEnable = true;
document.getElementById("cerrar").addEventListener("click", changeUserOptions());
function changeUserOptions() {
    if (isUserOptionsEnable){
        userOptions.style.display = "none";
    } else {
        userOptions.style.display = "block";
    }
    isUserOptionsEnable = !isUserOptionsEnable;
}

function postRedirect(url, postData){
    var postForm = document.createElement("form");
    postForm.action = url;
    postForm.method = "POST";
    postForm.style.display = "none";
    for (var key in postData){
        if (postData.hasOwnProperty(key)){
            var input = document.createElement("input");
            input.type = "hidden";
            input.name = key;
            input.value = postData[key];
            postForm.appendChild(input);
        }
    }
    document.body.appendChild(postForm);
    postForm.submit();
}

var maxLenght = 16;

function isVowel(letter) {
    var vowels = ["a","e","i","o","u"];

    for (a in vowels){
        var vowel = vowels[a];
        if (letter.toLowerCase() === vowel)
            return true;
    }

    return false;
}

(function formatName() {

    var name;

    try{
        name = document.getElementById("nombreUsuario").innerText;
    } catch (e) {}


    if(name !== undefined){

        name = name.trim();

        console.log("El nombre -> " + name);

        var words = name.split("");

        var result = "";
        var isLonger = true;

        if (maxLenght > words.length){
            maxLenght = words.length;
            isLonger = false;
        }

        var isFirstWord = true;
        for (var i=0; i<maxLenght; i++){

            if (words[i] === " "){
                result += " ";
                isFirstWord = true;
            } else if (isFirstWord) {
                result += words[i].toUpperCase();
                isFirstWord = false;
            } else {
                result += words[i].toLowerCase();
            }

        }

        if (isLonger){
            words = result.split("");
            for (var c = words.length; c > maxLenght-3 ; c--){
                if (isVowel(words[c-1])){
                    words[c] = "";
                } else if (words[c-1] === " "){
                    words[c] = "";
                    words[c-1] = "";
                    break;
                } else {
                    words[c] = ".";
                }
            }
            result = words.join("");
        }

        document.getElementById("nombreUsuario").innerHTML = result;
    }

})();
