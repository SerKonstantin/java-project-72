@import hexlet.code.dto.urls.UrlsPage
@import hexlet.code.util.Routes
@param UrlsPage page

@template.layout.page(
    page = page,
    content = @`
    @if(page.getUrls().isEmpty())
        <p>TODO no urls</p>
    @else
        <h2>TODO urls list name</h2>
        <ul class="list">
            @for(var url : page.getUrls())
                <li><a href="${Routes.urlPath(url.getId())}">${url.getName()}</a>   -   ${String.valueOf(url.getCreatedAt())}</li>
            @endfor
        </ul>
    @endif
    `
)
