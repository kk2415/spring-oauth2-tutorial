package com.oauth2.custom.security.jwt;

import io.jsonwebtoken.security.Keys;
import org.springframework.data.util.Pair;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Map;
import java.util.Random;

public class JwtKey {

    private static final Map<String, String> SECRET_KEY_SET = Map.of(
            "key1", "ASD4sq421ffZas5754sdsDZAF121FF47874AD4sA1A",
            "key2", "dSD4sq421ffASD54s1AQHLPP121FF47874AD4sDDS11",
            "key3", "AASFDFDDBNBger54F4SDGSDFdF121FF47874AD4sADF"
    );

    private static final String[] KID_SET = SECRET_KEY_SET.keySet().toArray(new String[0]);
    private static final Random randomIndex = new Random();


    /**
     * SECRET_KEY_SET 에서 랜덤한 KEY 가져오기
     * */
    public static Pair<String, Key> getRandomKey() {
        String kid = KID_SET[randomIndex.nextInt(KID_SET.length)];
        String secretKey = SECRET_KEY_SET.get(kid);
        return Pair.of(kid, Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * kid로 KEY 찾기
     * */
    public static Key getKey(String kid) {
        String key = SECRET_KEY_SET.getOrDefault(kid, null);

        return key == null ? null : Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));
    }

}
