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

