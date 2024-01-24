@import hexlet.code.dto.urls.UrlsPage
@param UrlsPage page

@template.layout.page(
    page = page,
    content = @`
    @if(page.getUrls().isEmpty())
        <p>TODO no urls</p>
    @else
        <h2>TODO urls list name</h2>
        <ul>
            @for(var url : page.getUrls())
                <li>${url.getName()}   -   ${String.valueOf(url.getCreatedAt())}</li>
            @endfor
        </ul>
    @endif
    `
)
