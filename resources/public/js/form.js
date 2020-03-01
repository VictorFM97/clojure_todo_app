'use strict';

const baseUrl = 'http://localhost:8001/api/';
const $ = (selector) => document.querySelector(selector);
const $$ = (selector) => document.querySelectorAll(selector);

const httpRequest = (endpoint, options, callback) => {
    const Http = new XMLHttpRequest();
    const url = baseUrl + endpoint;

    Http.onreadystatechange = (e) => {
        if (e.target.readyState === 4)
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
        if (response.status === 422) {
            alert("Invalid name");
        } else {
            const addedProfile = JSON.parse(response.responseText);

            $("#name-text").innerText = addedProfile.name;
            setCookie('profileId', addedProfile.id);
            enableProfile(addedProfile.id);
        }
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

    if (!profileId) {
        alert('Please reload the page and log in again');
        location.reload();
    }

    const title = getValueFromInput('#task-title');
    const description = getValueFromInput('#task-description');

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

    const handlerAddTask = (response) => {
        if (response.status === 422) {
            alert("Title and Description must be filled");
        } else {
            const task = JSON.parse(response.responseText);
            $(".todo-list ul").append(createTaskElement(task));
        }
    }

    httpRequest('task/add', options, handlerAddTask);
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

        httpRequest('task/' + profileId + '/all', options, handleFindTasks);
    }

    $("h2").classList.add("hidden");
    $("#name-input").classList.add("hidden");
    $("#create-profile").classList.add("hidden");
    $(".todo-list").style.display = 'block';
}

const markTaskAsDone = (el, id) => {

    const handleMarkAsDone = (response) => {
        if (response.status === 404) {
            el.checked = false;
            alert('task not found');
        }
    };

    const body = {
        id,
        done: el.checked
    };

    const options = {
        method: "PUT",
        contentType: "application/json",
        body,
    }

    httpRequest('task/done', options, handleMarkAsDone);
};

const deleteTask = (el, id) => {
    const handleDeleteTask = (response) => {
        if (response.status === 404) {
            alert('task not found');
        } else {
            el.parentNode.remove();
        }
    };

    const body = {
        id,
    };

    const options = {
        method: "DELETE",
        contentType: "application/json",
        body,
    }

    httpRequest('task/delete', options, handleDeleteTask);
};

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
    const clone = $("#task-clone").cloneNode(true);
    clone.classList.remove("hidden");
    clone.querySelector(".title").innerText = task.title;
    clone.querySelector(".description").innerText = task.description;

    const doneCheck = clone.querySelector(".done");
    doneCheck.addEventListener("change", (event) => markTaskAsDone(event.target, task.id));
    doneCheck.checked = task.done;

    const saveButton = clone.querySelector(".save");
    saveButton.classList.add("hidden");
    // saveButton.setAttribute("task-id", task.id);
    // saveButton.addEventListener("change", updateTask);

    const deleteButton = clone.querySelector(".delete");
    deleteButton.setAttribute("task-id", task.id);
    deleteButton.addEventListener("click", (event) => deleteTask(event.target, task.id));

    return clone;
}

{
    const profileId = parseInt(getCookie('profileId'));

    if (!isNaN(profileId))
        enableProfile(profileId, true);
}