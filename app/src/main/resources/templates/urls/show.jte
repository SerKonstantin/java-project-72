@import hexlet.code.dto.urls.UrlPage
@import hexlet.code.util.Routes
@param UrlPage page

@template.layout.page(
    page = page,
    content = @`
    <div class="container-lg mt-5">
        <h1>Website: ${page.getUrl().getName()}</h1>
        <table class="table table-bordered table-hover mt-3">
            <tbody>
                <tr>
                    <td>ID</td>
                    <td>${page.getUrl().getId()}</td>
                </tr>
                <tr>
                    <td>URL</td>
                    <td>${page.getUrl().getName()}</td>
                </tr>
                <tr>
                    <td>Date of adding</td>
                    <td>${page.getUrl().getCreatedAt()}</td>
                </tr>
            </tbody>
        </table>

        <h2 class="mt-5">Checks</h2>
        <form action="${Routes.checkUrlPath(page.getUrl().getId())}" method="post">
            <button type="submit" class="btn btn-primary">Perform a check</button>
        </form>

        @if(!page.getUrlChecks().isEmpty())
            <table class="table table-bordered table-hover mt-3">
                <thead>
                <tr>
                    <th class="col-1">ID</th>
                    <th class="col-1">Status code</th>
                    <th>Title</th>
                    <th>h1</th>
                    <th>Description</th>
                    <th class="col-2">Checked at</th>
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
    </div>
    `
)
