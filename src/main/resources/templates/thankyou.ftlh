<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <title>Thank you!!</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.1/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-iYQeCzEYFbKjA/T2uDLTpkwGzCiq6soy8tYaI1GyVh/UjpbCx/TYkiZhlZB6+fzT" crossorigin="anonymous">

    <link rel="stylesheet" href="css/candidates.css">
</head>
<body>

<div class="container flex flex-col align-center">


    <h1>Thank you for your vote!</h1>
    <main class="flex flex-wrap align-center">
        <div class="card">
            <div class="flex flex-col align-center" href="#">

                <img src="images/${candidate.photo}">
                <p>Thank you for your vote!</p>

            </div>
        </div>
        <div>
            <a class="back flex align-center" href="/">   back to main  </a>
            <p>-----------------------------------------------------</p>
            <a class="back flex align-center" href="/votes">Show total votes</a>
        </div>
    </main>
</div>

</body>

</html>