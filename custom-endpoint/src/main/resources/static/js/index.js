$(function () {
    $('#googleLogin').click(() => {
        window.location.href = '/oauth2/authorization/google?redirect_uri=http://localhost:8080/oauth/redirect'
    })
    $('#githubLogin').click(() => {
        window.location.href = '/oauth2/authorization/github?redirect_uri=http://localhost:8080/oauth/redirect'
    })

    $.ajax({
        url: "/api/member",
        method: 'GET',
        async: false,
    })
    .done(function (response) {
        console.log(response)
        if (response !== '' && response !== null) {
            $("#user").html(response);
            $(".authenticated").css('display', 'block')
        }
    })
    .fail(function (error) {
        console.log(error)
    })
})