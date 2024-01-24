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
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous">
    <title>Website analyzer</title>
</head>
<body>
    <nav>
        <a href="${Routes.rootPath()}">Simple page analyzer</a>
        <a href="${Routes.rootPath()}">Homepage</a>
        <a href="${Routes.urlsPath()}">Sites</a>
    </nav>

    <main>
        @if(page != null && page.getFlash() != null)
            <div class="alert alert-${page.getFlashType()}" role="alert">
                ${page.getFlash()}
            </div>
        @endif
        
        ${content}
    </main>

    <footer>
        <div>
            Created for educational purposes. Check my
            <a href="https://github.com/SerKonstantin">Github</a>
        </div>
    </footer>
</body>
</html>