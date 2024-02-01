@import hexlet.code.dto.urls.UrlPage
@import hexlet.code.util.Routes
@param UrlPage page

@template.layout.page(
    page = page,
    content = @`
        <h2>URL: ${page.getUrl().getName()}</h2>
        <p>URL id: ${page.getUrl().getId()}</p>
        <p>Created at: ${page.getUrl().getCreatedAt().toString()}</p>
        <form action="${Routes.checkUrlPath(page.getUrl().getId())}" method="post">
            <button type="submit" class="btn btn-primary">Check</button>
        </form>

        @if(page.getUrlChecks().isEmpty())
            <p>Checks hasn't performed yet</p>
        @else
            <table>
                <thead>
                <tr>
                    <td>ID</td>
                    <td>Status code</td>
                    <td>Title</td>
                    <td>h1</td>
                    <td>Description</td>
                    <td>Timestamp</td>
                </tr>
                </thead>
                <tbody>
                @for(var urlCheck : page.getUrlChecks())
                    <tr>
                        <td>${urlCheck.getId()}</td>
                        <td>${urlCheck.getStatusCode()}</td>
                        <td>${urlCheck.getTitle()}</td>
                        <td>${urlCheck.getH1()}</td>
                        <td>${urlCheck.getDescription()}</td>
                        <td>${urlCheck.getCreatedAt()}</td>
                    </tr>
                @endfor
                </tbody>
            </table>
        @endif
    `
)
