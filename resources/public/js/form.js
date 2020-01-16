'use strict';

const baseUrl = 'http://localhost:8001/api/';

const httpRequest = (endpoint, options, callback) => {
    const Http = new XMLHttpRequest();
    const url = baseUrl + endpoint;

    Http.onreadystatechange = (e) => {
        console.log(e);
    };

    Http.open(options.method, url);
    Http.send();
};

document.querySelectorAll('input').forEach((x) => {
    x.addEventListener('focusout', () => {
        httpRequest("todo/1/all", { method: "GET" });
    });
});

document.querySelector('.add-task').addEventListener('click', () =>
    console.log('add')
);