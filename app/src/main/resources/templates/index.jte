@import hexlet.code.util.Routes
@import hexlet.code.dto.BasePage
@param BasePage page

@template.layout.page(
    page = page,
    content = @`
        <h1>Websites analyser</h1>
        <p>TODO description</p>
    
        <form action="${Routes.urlsPath()}" method="post">
            <label for="url_input">Link</label>
            <input id="url_input" type="text" name="url" required>
            <input type="submit" value="Check">
        </form>
    `
)