package dto;

import lombok.Builder;

@Builder
public class FeaturedSubjects {
    private String id;
    private String name;
    private String thumbnailURL;
}
