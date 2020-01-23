'use strict';

const baseUrl = 'http://localhost:8001/api/';
const $ = (selector) => document.querySelector(selector);
const $$ = (selector) => document.querySelectorAll(selector);

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
        enableProfile(addedProfile.id);
    };

    const options = {
        body: obj,
        contentType: "application/json",
        method: "POST"
    };

    httpRequest("profile/add", options, handleProfileCreation);
});

$('.add-task').addEventListener('click', () => {
    const profileId = getCookie('profileId');

    if (!profileId)
        return alert('ababa');

    const title = getValueFromInput('#task-title');
    const description = getValueFromInput('#task-description');

    if (!title || !description)
        return alert('Title and Description must be filled');

    const body = {
        'profile-id': parseInt(profileId),
        'title': title,
        'description': description
    };

    const options = {
        body,
        contentType: 'application/json',
        method: 'POST'
    }

    console.log(options);

    const handlerAddTask = (response) => {
        const task = JSON.parse(response.resposeText);
        $(".todo-list ul").append(createTaskElement(task));
    }

    httpRequest('todo/add', options, handlerAddTask);
});

const enableProfile = (profileId, findProfile) => {
    if (findProfile) {
        const options = {
            method: 'GET'
        }

        const handleLoggedIn = (response) => {
            const profile = JSON.parse(response.responseText);
            $("#name-text").innerText = profile.name;
        };

        httpRequest('profile/' + profileId, options, handleLoggedIn);

        const handleFindTasks = (response) => {
            const tasks = JSON.parse(response.responseText);
            const a = tasks.map((t) => createTaskElement(t));
            const list = $('.todo-list ul');
            for (let i = 0; i < a.length; i++)
                list.append(a[i]);
        }

        httpRequest('todo/' + profileId + '/all', options, handleFindTasks);
    }

    $("#name-input").style.display = 'none';
    $("#create-profile").style.display = 'none';
    $(".todo-list").style.display = 'block';
}

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

const createTaskElement = (task) => {
    console.log(task);
    const li = document.createElement("li");
    const p_title = document.createElement("p");
    const p_description = document.createElement("p");
    const button_update = document.createElement("button");
    const button_delete = document.createElement("button");

    p_title.innerText = task.title;
    p_description.innerText = task.description;
    button_update.innerText = "Save";
    button_delete.innerText = "Delete";

    button_update.setAttribute('task-id', task.id);
    button_delete.setAttribute('task-id', task.id);

    li.append(p_title, p_description, button_update, button_delete);
    return li;
}

{
    const profileId = parseInt(getCookie('profileId'));

    if (!isNaN(profileId))
        enableProfile(profileId, true);
}