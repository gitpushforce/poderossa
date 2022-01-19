package com.school.poderossa.service;

import com.school.poderossa.bean.PostBean;
import com.school.poderossa.common.SQLCommon;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdminService {
    @Autowired
    SQLCommon sqlCommon;

    public List<PostBean> getPosts() {
        Optional<List<Map<String, Object>>> list = Optional.ofNullable(sqlCommon.getListSQL("admin_post_list", new HashMap<>()));

        return list.map(maps -> maps.stream().filter(Objects::nonNull).map(y -> {
            PostBean post = new PostBean();
            post.setPostId((String) y.get("post_id"));
            post.setName((String) y.get("profe_name"));
            post.setTitle(Objects.isNull(y.get("title"))
                    ? StringUtils.EMPTY
                    : y.get("title").toString());
            post.setContent(Objects.isNull(y.get("content")) ? StringUtils.EMPTY : y.get("content").toString());
            post.setUpdateFecha(Objects.isNull(y.get("update_date"))
                    ? StringUtils.EMPTY
                    : getDate(y.get("update_date").toString())[0]);
            post.setUpdateTime(Objects.isNull(y.get("update_date"))
                    ? StringUtils.EMPTY
                    : getDate(y.get("update_date").toString())[1]);
            post.setWriterPhoto(Objects.isNull(y.get("profe_photo"))
                    ? StringUtils.EMPTY
                    : y.get("profe_photo").toString());
            post.setPinned((Boolean)y.get("pinned"));
            post.setStatus((Boolean)y.get("status"));
            return post;
        }).collect(Collectors.toList())).orElse(Collections.emptyList());
    }

    public PostBean getPost(String postId) {
        Map<String, Object> params =  new HashMap<>();
        params.put("post_id", postId);

        Optional<Map<String, Object>> postOptional = Optional.ofNullable(sqlCommon.getObjectSQL("admin_post", params));

        if (postOptional.isPresent()) {
            PostBean post = new PostBean();
            post.setPostId(postOptional.get().get("post_id").toString());
            post.setName(postOptional.get().get("profe_name").toString());
            post.setTitle(Objects.isNull(postOptional.get().get("title"))
                    ? StringUtils.EMPTY
                    : postOptional.get().get("title").toString());
            post.setContent(Objects.isNull(postOptional.get().get("content"))
                    ? StringUtils.EMPTY
                    : postOptional.get().get("content").toString());
            post.setPostPhoto(Objects.isNull(postOptional.get().get("post_photo"))
                    ? StringUtils.EMPTY
                    : postOptional.get().get("post_photo").toString());
//            post.setUpdateFecha(Objects.isNull(postOptional.get().get("update_date"))
//                    ? StringUtils.EMPTY
//                    : postOptional.get().get("update_date").toString());
            return post;
        }
        return null;
    }

    public String createNewPost(String profeId) {
        Optional<Map<String, Object>> check = null;
        String randomString;
        do {
            randomString = getRandomAlphanumericString();
            Map<String, Object> params = new HashMap<>();
            params.put("post_id", randomString);
            check = Optional.ofNullable(sqlCommon.getObjectSQL("admin_search_post_by_post_id", params));
        } while (check.isPresent());

        Map<String, Object> params = new HashMap<>();
        params.put("post_id", randomString);
        params.put("profe_id", profeId);

        if (Objects.equals(sqlCommon.insertOrUpdateRow("admin_insert_new_post", params), 1)) {
            // returning new post id
            return randomString;
        }
        return StringUtils.EMPTY;
    }

    public int updatePost(String postId, String title, String content, String postPhoto, String updateTimeStamp) {
        Map<String, Object> params =  new HashMap<>();
        params.put("title", title);
        params.put("content", content);
        params.put("post_photo", postPhoto);
        params.put("update_date", updateTimeStamp);
        params.put("post_id", postId);
        return sqlCommon.insertOrUpdateRow("admin_update_post", params);
    }

    public String getRandomAlphanumericString() {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 8;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        return generatedString;
    }

    public int setPin(String postId, Boolean pinned) {
        Map<String, Object> params =  new HashMap<>();
        params.put("pinned", pinned);
        params.put("post_id", postId);
        return sqlCommon.insertOrUpdateRow("admin_update_pin", params);
    }

    public int setStatus(String postId, Boolean status) {
        Map<String, Object> params =  new HashMap<>();
        params.put("status", status);
        params.put("post_id", postId);
        return sqlCommon.insertOrUpdateRow("admin_update_publish", params);
    }

    private String[] getDate(String updateDate) {
        String date = updateDate.substring(0, 19);
        return date.split(" ");
    }
}
