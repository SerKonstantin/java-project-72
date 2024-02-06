@import hexlet.code.dto.urls.UrlsPage
@import hexlet.code.util.Routes
@param UrlsPage page

@template.layout.page(
    page = page,
    content = @`
    <div class="container-lg mt-5">
        @if(page.getUrls().isEmpty())
            <h5>Please, <a href="${Routes.rootPath()}">add</a> websites to see them in the list</h5>
        @else
            <h1>Websites</h1>
            <table class="table table-bordered table-hover mt-3">
                <thead>
                    <tr>
                        <th class="col-1">ID</th>
                        <th>URL</th>
                        <th class="col-2">Last check at</th>
                        <th class="col-1">Status code</th>
                    </tr>
                </thead>
                <tbody>
                @for(var url : page.getUrls())
                    !{var urlCheck = page.getLatestChecks().get(url.getId());}
                    <tr>
                        <td>${url.getId()}</td>
                        <td><a href="${Routes.urlPath(url.getId())}">${url.getName()}</a></td>
                        <td>${urlCheck != null ? urlCheck.getCreatedAt() : ""}</td>
                        <td>${urlCheck != null ? Integer.toString(urlCheck.getStatusCode()) : ""}</td>
                    </tr>
                @endfor
                </tbody>
            </table>
        @endif
    </div>
    `
)
