package ganizo.w;

public class SovTechAPI {

    private String joke, imageUrl, dateCreated;

    public SovTechAPI(String joke, String imageUrl, String dateCreated) {
        this.joke = joke;
        this.imageUrl = imageUrl;
        this.dateCreated = dateCreated;
    }

    public String getJoke() {
        return joke;
    }
    public String getImageUrl() {
        return imageUrl;
    }
    public String getDateCreated() {
        return dateCreated;
    }
}
