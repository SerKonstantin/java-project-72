@import hexlet.code.dto.urls.UrlPage
@param UrlPage page

@template.layout.page(

    content = @`
        <h2>URL: ${page.getUrl().getName()}</h2>
        <p>URL id: ${page.getUrl().getId()}</p>
        <p>Created at: ${page.getUrl().getCreatedAt().toString()}</p>
    `
)
