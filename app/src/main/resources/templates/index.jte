@import hexlet.code.util.Routes
@import hexlet.code.dto.BasePage
@param BasePage page

@template.layout.page(
    page = page,
    content = @`
    <div class="container-fluid p-5">
        <div class="row">
            <div class="col-md-10 col-lg-8 mx-auto">
                <h1 class="display-3 mb-0">Website analyzer</h1>
                <p class="lead">Enter a URL address and check website accessibility!</p>

                <form action="${Routes.urlsPath()}" method="post" class="rss-form text-body">
                    <div class="row">
                        <div class="col">
                            <div class="form-floating">
                                <input id="url_input" type="text" name="url" autofocus required aria-label="url"
                                class="form-control w-100" placeholder="URL" autocomplete="off">
                                <label for="url_input" class="form-label">URL</label>
                            </div>
                        </div>
                        <div class="col-auto">
                            <input type="submit" value="Check" class="h-100 btn btn-lg btn-primary px-sm-5">
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
    `
)