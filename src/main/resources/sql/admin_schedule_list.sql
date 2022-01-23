SELECT
  event_id as event_id,
  event_name as event_name,
  event_place as event_place,
  event_date as event_date,
  event_time as event_time,
  event_datetime as event_datetime
FROM
  SCHEDULE_TBL schedule
WHERE
  event_datetime >= CURDATE()
ORDER BY
  event_datetime ASC