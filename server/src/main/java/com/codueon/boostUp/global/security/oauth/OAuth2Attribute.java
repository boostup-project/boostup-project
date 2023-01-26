package com.codueon.boostUp.global.security.oauth;

import com.codueon.boostUp.global.security.utils.AuthConstants;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
public class OAuth2Attribute {
    private Map<String, Object> attributes;
    private String attributeKey;
    private String email;
    private String name;
    private String picture;
    private final String provider;


    public static OAuth2Attribute of(String provider, String attributeKey, Map<String, Object> attributes) {
        return ofGoogle(attributeKey, attributes);
    }

    public static OAuth2Attribute ofGoogle(String attributeKey, Map<String, Object> attributes) {
        return OAuth2Attribute.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .picture((String) attributes.get("picture"))
                .attributes(attributes)
                .attributeKey(attributeKey)
                .provider(AuthConstants.GOOGLE)
                .build();
    }
}
