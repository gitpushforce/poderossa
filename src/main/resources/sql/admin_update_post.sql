UPDATE
  POSTS_TBL
SET
  title = :title,
  content = :content,
  post_photo = :post_photo,
  update_date = :update_date
WHERE
  post_id = :post_id