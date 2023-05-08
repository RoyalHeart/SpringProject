SELECT * FROM ( 
    SELECT *, ROW_NUMBER() OVER (ORDER BY id) as row FROM book
    WHERE
    isDeleted is null
    ) a 
/*BEGIN*/
WHERE 
    /*IF startItem != null*/
    a.row > /*startItem*/0
    /*END*/
    /*IF endItem != null */
    AND a.row <= /*endItem*/10
    /*END*/
/*END*/