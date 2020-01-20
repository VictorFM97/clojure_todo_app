'use strict';

const baseUrl = 'http://localhost:8001/api/';

const httpRequest = (endpoint, options, callback) => {
    const Http = new XMLHttpRequest();
    const url = baseUrl + endpoint;

    Http.onreadystatechange = (e) => {
        console.log(e);
    };

    Http.open(options.method, url);

    if (options.contentType)
        Http.setRequestHeader("content-type", options.contentType);

    Http.send(JSON.stringify(options.body));
};

const getValueFromInput = (inputName) => (document.querySelector(inputName).value)

document.querySelector("#create-profile").addEventListener('click', () => {
    let obj = {
        name: getValueFromInput("#name")
    };

    httpRequest("profile/add", { body: obj, contentType: "application/json", method: "POST" });
});

document.querySelectorAll('input').forEach((x) => {
    x.addEventListener('focusout', () => {
        httpRequest("todo/1/all", { method: "GET" });
    });
});

document.querySelector('.add-task').addEventListener('click', () =>
    console.log('add')
);