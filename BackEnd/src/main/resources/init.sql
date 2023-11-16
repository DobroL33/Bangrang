UPDATE z_ngii_n3a_g0010000 SET geom = ST_SetSRID(geom, 4326);

INSERT INTO korea_border_area (korea_border_area_idx, korea_border_area_shape , korea_border_area_bjcd , korea_border_area_name)
SELECT gid, geom, bjcd, name
FROM z_ngii_n3a_g0010000;