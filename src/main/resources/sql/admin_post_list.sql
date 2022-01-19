SELECT
  posts.post_id as post_id,
  profes.profe_name as profe_name,
  posts.title as title,
  posts.content as content,
  posts.status as status,
  profes.profe_rango as profe_rango,
  posts.pinned as pinned,
  posts.post_photo as post_photo,
  profes.profe_photo as profe_photo,
  posts.publish_date as publish_date,
  posts.update_date as update_date,
  posts.created_date as created_date
FROM
  POSTS_TBL posts INNER JOIN
  PROFES_TBL profes ON
  posts.profe_id = profes.profe_id
ORDER BY
  posts.pinned DESC,
  posts.update_date DESC,
  posts.created_date DESC