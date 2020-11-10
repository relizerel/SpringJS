let viewUsersUrl = 'http://localhost:8080/admin/userList';
let authUserUrl = 'http://localhost:8080/user/authUser';
let newUserUrl = 'http://localhost:8080/admin/newUser';
let deleteUserUrl = 'http://localhost:8080/admin/deleteUser/';
let getUserByIdUrl = 'http://localhost:8080/admin/user/';
let updateUserUrl = 'http://localhost:8080/admin/updateUser';

let elementUserTable = document.getElementById('nav-list-tab');
let elementNewUser = document.getElementById('nav-newUser-tab');
let elementCreateUser = document.getElementById('createUser');
let elementCreateUserRoles = document.getElementById('roleSelect');
let elementCloseDeleteModal1 = document.getElementById('closeDeleteModal');
let elementCloseDeleteModal2 = document.getElementById('closeDeleteModal2');
let elementCloseUpdateModal1 = document.getElementById('closeUpdateModal');
let elementCloseUpdateModal2 = document.getElementById('closeUpdateModal2');

let adminUsersTable = $('#userTableJs tbody');
let navbarInfo = $('#navBarInfo div');
let userInfo = $('#userInfoJs tbody');
let deleteButtonInModalForm = $('#deleteButtonInModal div');
let saveButtonInModalForm = $('#updateButtonInModal div');

/*инициализация скриптов при загрузке страницы*/
$(document).ready(function () {
    showAllUsers();
    navbar();
    showUserInfo();
});

elementCreateUser.onclick = function () {
    newUser();
};

elementUserTable.onclick = function () {
    showTheUserTable();
};

/*Можно ли как-то сократить стр. 42-56?
* они отвечают за то, что бы кнопки update и delete не дублировались*/


elementCloseDeleteModal1.onclick = function () {
    document.getElementById('delButtInModal').remove();
};

elementCloseDeleteModal2.onclick = function () {
    document.getElementById('delButtInModal').remove();
};

elementCloseUpdateModal1.onclick = function () {
    document.getElementById('updButtInModal').remove();
};

elementCloseUpdateModal2.onclick = function () {
    document.getElementById('updButtInModal').remove();
};

elementNewUser.onclick = function () {
    hideTheUserTable();
};

function showAllUsers() {

    /*    эти переменные необходимы дабы передать в onclick имя функции!!*/
    /*    ВНИМАНИЕ! НА ВСЯКИЙ СЛУЧАЙ, вдруг еще раз прийдется идти на гребанутый фронт
        JS функцию можно передать как String с любым количеством входящих значений
        На стороне HTML String Object - будет уже функцией JS!!!!*/

    let userIdForDelete = 0;
    let userIdForUpdate = 0;

    fetch(viewUsersUrl)
        .then((response) => {
            if (!response.ok) {
                throw Error("Error: " + response.status);
            }
            return response.json();
        })
        .then((data) => {
            data.map(user => {
                const userRoles = user.roleSet.map(role => {
                    return role.name;
                }).join(", ");

                let buttonDelete = document.createElement('button');
                let buttonEdit = document.createElement('button');

                let tr = document.createElement('tr');

                tr.setAttribute('id', "userDataTable");

                /*счетчик - костыль для заполнения полей таблицы - правильной информацией
                а так-же для передачи ID для заполнения модальных форм в дальнейшем*/

                let counter = 0, td;

                for (let o in user) {
                    let td = document.createElement('td');

                    if (counter === 0) {
                        userIdForDelete = "fillingModalFormDelete" + "(" + user[o] + ")";
                        userIdForUpdate = "fillingModalFormUpdate" + "(" + user[o] + ")";
                    }

                    if (counter < 4) {
                        td.appendChild(document.createTextNode(user[o]));
                    }

                    tr.appendChild(td);
                    counter++;

                    if (counter === 5) {
                        td.appendChild(document.createTextNode(userRoles));
                        tr.appendChild(td);
                    }
                }

                buttonEdit.setAttribute('id', "updateButton");
                buttonEdit.setAttribute('class', "btn btn-info");
                buttonEdit.setAttribute('role', "button");
                buttonEdit.setAttribute('data-toggle', "modal");
                buttonEdit.setAttribute('data-target', "#updateModal");
                buttonEdit.setAttribute('onclick', `${userIdForUpdate}`);
                buttonEdit.appendChild(document.createTextNode("Update"));

                buttonDelete.setAttribute('id', "deleteButton");
                buttonDelete.setAttribute('class', "btn btn-danger");
                buttonDelete.setAttribute('role', "button");
                buttonDelete.setAttribute('data-toggle', "modal");
                buttonDelete.setAttribute('data-target', "#deleteModal");
                buttonDelete.setAttribute('onclick', `${userIdForDelete}`);
                buttonDelete.appendChild(document.createTextNode("Delete"));

                td = document.createElement('td');
                td.appendChild(buttonEdit);
                tr.appendChild(td);
                td = document.createElement('td');
                td.appendChild(buttonDelete);
                tr.appendChild(td);

                adminUsersTable.append(tr);
            });
        })
        .catch(error => {
            console.log(error);
        })
}

/*вывод информации об авторизированном пользователе на странице /admin/userTable и /user/userinfo*/
function navbar() {
    fetch(authUserUrl)
        .then((response) => {
            if (!response.ok) {
                throw Error("Error : " + response.status);
            }
            return response.json();
        })
        .then((data) => {
            const userRoles = data.roleSet.map(role => {
                return role.name;
            }).join(", ");

            let a = document.createElement('a');

            a.setAttribute('class', "navbar-text text-white");
            a.appendChild(document.createTextNode(data.userEmail + " with roles : " + `${userRoles.toString()}`));
            navbarInfo.append(a);
        })
        .catch(error => {
            console.log(error);
        });
}

/*создание нового пользователя*/
async function newUser() {


    document.getElementById('hideTheUsersTable').hidden = true;
    document.getElementById('hideTheCreateUserForm').hidden = false;

    let roleSelectedValues = Array.from(elementCreateUserRoles.selectedOptions).map(el => el.value);
    let roleArray = convertToRoleSet(roleSelectedValues);

    let data = {

        userName: $('#userName').val(),
        userPassword: $('#password').val(),
        userEmail: $('#email').val(),

        roleSet: roleArray

    };

    const response = await fetch(newUserUrl, {
        method: 'POST',
        mode: 'cors',
        cache: 'no-cache',
        credentials: 'same-origin',
        headers: {
            'Content-Type': 'application/json'
        },
        redirect: 'follow',
        referrerPolicy: 'no-referrer',
        body: JSON.stringify(data)
    })
        .catch(error => {
            console.log(error);
        });


    return response.json();
}

/*отображение информации на странице пользователя*/
function showUserInfo() {
    fetch(authUserUrl)
        .then((response) => {
            if (!response.ok) {
                throw Error("Error : " + response.status);
            }
            return response.json();
        })
        .then((data) => {

            const userRoles = data.roleSet.map(role => {
                return role.name;
            }).join(", ");

            let tr = document.createElement('tr');

            let counter = 0;

            for (let o in data) {

                let td = document.createElement('td');

                if (counter < 4) {
                    td.appendChild(document.createTextNode(data[o]));
                    tr.appendChild(td);
                }

                counter++;

                if (counter === 5) {
                    td.appendChild(document.createTextNode(userRoles));
                    tr.appendChild(td);
                }
            }

            userInfo.append(tr);

        })
        .catch(error => {
            console.log(error);
        });
}

/*событие ловит кнопка в модальном окне и редактирует пользователя*/
async function updateUser(value) {

    let userID = '#updUserID';
    let name = '#updUserName';
    let password = '#updUserPassword';
    let email = '#updUserEmail';

    let elementUpdateUserRoles = document.getElementById('userUpdRoles');

    let roleSelectedValues = Array.from(elementUpdateUserRoles.selectedOptions).map(el => el.value);
    let roleArray = convertToRoleSet(roleSelectedValues);

    let data = {
        userID: $(userID).val(),
        userName: $(name).val(),
        userPassword: $(password).val(),
        userEmail: $(email).val(),

        roleSet: roleArray

    };

    const response = await fetch(updateUserUrl, {
        method: 'PUT',
        mode: 'cors',
        cache: 'no-cache',
        credentials: 'same-origin',
        headers: {
            'Content-Type': 'application/json'
        },
        redirect: 'follow',
        referrerPolicy: 'no-referrer',
        body: JSON.stringify(data)
    });

    document.getElementById('updButtInModal').remove();
    clearTable();
    showAllUsers();
    return response.json();
}

/*событие ловит кнопка в модальном окне и удаляет пользователя*/
async function deleteData(value) {

    await fetch(deleteUserUrl + value, {
        method: 'DELETE',
        mode: 'cors',
        cache: 'no-cache',
        credentials: 'same-origin',
        headers: {
            'Content-Type': 'application/json'
        },
        redirect: 'follow',
        referrerPolicy: 'no-referrer',
    });

    document.getElementById('delButtInModal').remove();
    clearTable();
    showAllUsers();
}

/*создаем массив из значений полученных с селектора при создании нового пользователя*/
function convertToRoleSet(Array) {
    let roleArray = [];

    if (Array.indexOf("USER") !== -1) {
        roleArray.unshift({id: 2, name: "USER"});
    }
    if (Array.indexOf("ADMIN") !== -1) {
        roleArray.unshift({id: 1, name: "ADMIN"});
    }
    return roleArray;
}

function hideTheUserTable() {
    document.getElementById('hideTheUsersTable').hidden = true;
    document.getElementById('hideTheCreateUserForm').hidden = false;
    clearTable();
}

function showTheUserTable() {
    if (document.getElementById("userDataTable") == null) {
        showAllUsers();
    }
    document.getElementById('hideTheUsersTable').hidden = false;
    document.getElementById('hideTheCreateUserForm').hidden = true;
}

function clearTable() {
    while (document.getElementById("userDataTable") != null) {
        document.getElementById("userDataTable").remove();
    }
}

/*заполнение модальной формы на удаление*/
function fillingModalFormDelete(id) {

    let deleteButtonInModal = document.createElement('button');
    let userIdForDeleteButton = "deleteData" + "(" + id + ")";

    deleteButtonInModal.setAttribute('type', "button");
    deleteButtonInModal.setAttribute('id', "delButtInModal");
    deleteButtonInModal.setAttribute('class', "btn btn-danger");
    deleteButtonInModal.setAttribute('data-dismiss', "modal");
    deleteButtonInModal.setAttribute('onclick', `${userIdForDeleteButton}`);
    deleteButtonInModal.appendChild(document.createTextNode("Delete"));

    deleteButtonInModalForm.append(deleteButtonInModal);

    fetch(getUserByIdUrl + id).then(function (response) {
        response.json().then(function (data) {

            const userRoles = data.roleSet.map(role => {
                return role.name;
            }).join(", ");

            $('#delUserID').val(id);
            $('#delUserName').val(data.userName);
            $('#delUserPassword').val(data.userPassword);
            $('#delUserEmail').val(data.userEmail);
            $('#delUserRoles').val(userRoles);
        });
    });

}

/*заполнение модальной формы на редактирование*/
function fillingModalFormUpdate(id) {

    let updateButtonInModal = document.createElement('button');
    let userIdForUpdateButton = "updateUser" + "(" + id + ")";

    updateButtonInModal.setAttribute('type', "button");
    updateButtonInModal.setAttribute('id', "updButtInModal");
    updateButtonInModal.setAttribute('class', "btn btn-success");
    updateButtonInModal.setAttribute('data-dismiss', "modal");
    updateButtonInModal.setAttribute('onclick', `${userIdForUpdateButton}`);
    updateButtonInModal.appendChild(document.createTextNode("Save"));

    saveButtonInModalForm.append(updateButtonInModal);

    fetch(getUserByIdUrl + id).then(function (response) {
        response.json().then(function (data) {

            $('#updUserID').val(id);
            $('#updUserName').val(data.userName);
            $('#updUserPassword').val(data.userPassword);
            $('#updUserEmail').val(data.userEmail);

        });
    });
}