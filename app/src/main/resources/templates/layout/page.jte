@import gg.jte.Content
@import hexlet.code.util.Routes
@import hexlet.code.dto.BasePage

@param Content content
@param BasePage page = null

<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-kenU1KFdBIe4zVF0s0G1M5b4hcpxyD9F7jL+jjXkk+Q2h455rYXK/7HAuoJl+0I4"
            crossorigin="anonymous"></script>
    <title>Website analyzer</title>
</head>
<body class="d-flex flex-column min-vh-100 bg-image" style="background-image: url('/background-clouds-light.jpg');
background-position: center center; background-size: cover;">
    <nav class="navbar navbar-expand-lg navbar-light" role="navigation">
        <div class="container-fluid">
            <a href="${Routes.rootPath()}" class="navbar-brand">WWebsite analyzer</a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav"
                    aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <div class="navbar-nav">
                    <a href="${Routes.rootPath()}" class="nav-link">Home</a>
                    <a href="${Routes.urlsPath()}" class="nav-link">Websites</a>
                </div>
            </div>
        </div>
    </nav>

    <main class="flex-grow-1">
        @if(page != null && page.getFlash() != null)
            <div class="rounded-0 m-0 alert alert-dismissible fade show alert-${page.getFlashType()}" role="alert">
                <p class="m-0">${page.getFlash()}</p>
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        @endif
        <section class="content">
            ${content}
        </section>
    </main>

    <footer class="footer py-3 mt-5 bg-light">
        <div class="container-xl">
            <div class="text-center">
                Created for educational purposes. Check my
                <a href="https://github.com/SerKonstantin">Github</a>
            </div>
        </div>
    </footer>
</body>
</html>