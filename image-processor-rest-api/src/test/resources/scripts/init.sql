insert into users (id, name, email, password, subscription_id)
values (2, 'Joe Doe', 'joedoe@mail.com', '$2a$10$DBswMoGLzgmFve2gSF0HAeGAmrnSDHqSPxNB.s9Bf3MR8aUqIOalK', 1);

insert into images (id, content_type, data, file_name)
values (1, 'image/png', '---', 'florest-river.png'),
       (2, 'image/png', '---', 'buterfly.png'),
       (3, 'image/png', '---', 'mountain.png'),
       (4, 'image/png', '---', 'bird.png'),
       (5, 'image/png', '---', 'city.png');

insert into image_transformation_params (id, grayscale, invert_colors, resize_ratio, sepia_intensity)
values (1, false, false, 0.75, 20),
       (2, true, false, 0.65, null),
       (3, false, true, 0.75, null),
       (4, true, false, null, null),
       (5, false, true, 0.45, null);

insert into image_transformations (id, status, transformed_data, image_id, download_code, transformation_params_id, requested_by_id, requested_at, completed_at)
values (1,
        'SUCCESS',
        '***',
        1,
        'a57fc84e4c8f4cef974c7c016c7bb3cf',
        1,
        2,
        timezone('utc', now()) - interval '2 DAY',
        timezone('utc', now()) - interval '2 DAY'),
       (2,
        'SUCCESS',
        '***',
        2,
        '1f26a12d204a4a7dac395ad3f39d72cb',
        2,
        2,
        timezone('utc', now() - interval '2 DAY'),
        timezone('utc', now() - interval '2 DAY')),
       (3,
        'SUCCESS',
        '***',
        3,
        '8fcf768b592a42938e0c0213ddb94ce7',
        3,
        2,
        timezone('utc', now()),
        timezone('utc', now())),
       (4,
        'SUCCESS',
        '***',
        4,
        'b6cde33ae6a04ded83757a5a0cd9aee2',
        4,
        2,
        timezone('utc', now()),
        timezone('utc', now())),
       (5,
        'SUCCESS',
        '***',
        5,
        '2dc22404e1e64a0882ce838733d5bc4e',
        5,
        2,
        timezone('utc', now()),
        timezone('utc', now()));