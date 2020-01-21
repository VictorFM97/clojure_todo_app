'use strict';

const baseUrl = 'http://localhost:8001/api/';
const $ = (selector) => document.querySelector(selector);
const $$ = (selector) => document.querySelectorAll(selector);
let profileId = 0;

const httpRequest = (endpoint, options, callback) => {
    const Http = new XMLHttpRequest();
    const url = baseUrl + endpoint;

    Http.onreadystatechange = (e) => {
        if (e.target.readyState == 4)
            callback(e.target);
    };

    Http.open(options.method, url);

    if (options.contentType)
        Http.setRequestHeader("content-type", options.contentType);

    Http.send(JSON.stringify(options.body));
};

const getValueFromInput = (inputName) => ($(inputName).value)

$("#create-profile").addEventListener('click', () => {
    let obj = {
        name: getValueFromInput("#name-input")
    };

    const handleProfileCreation = (response) => {
        const responseData = JSON.parse(response.responseText);
        const addedProfile = responseData[responseData.length - 1];

        $("#name-text").innerText = addedProfile.name;
        setCookie('profileId', addedProfile.id);
        profileId = addedProfile.id;
    };

    const options = {
        body: obj,
        contentType: "application/json",
        method: "POST"
    };

    httpRequest("profile/add", options, handleProfileCreation);
});

$$('input').forEach((x) => {
    x.addEventListener('focusout', () => {
        //httpRequest("todo/1/all", { method: "GET" });
    });
});

$('.add-task').addEventListener('click', () =>
    console.log('add')
);

const setCookie = (name, value, expiration) => {
    document.cookie += name + '=' + value + ';';
}

const getCookie = (name) => {
    const cookies = document.cookie.split(';');

    for (let i = 0; i < cookies.length; i++) {
        const keyValue = cookies[i].split('=');

        if (keyValue[0] === name) {
            return keyValue[1];
        }
    }
}

profileId = parseInt(getCookie('profileId'));

if (!isNaN(profileId)) {
    $("#name-input").style.display = 'none';
    $("#create-profile").style.display = 'none';

    const options = {
        method: 'GET'
    }

    const handleLoggedIn = (response) => {
        const profile = JSON.parse(response.responseText);
        $("#name-text").innerText = profile.name;
    };

    httpRequest('profile/' + profileId, options, handleLoggedIn);
}