SELECT * FROM book 
/*BEGIN*/
  WHERE
  /*IF author != null */
        author = /*author*/'Tom'
  /*END*/
  /*IF title != null */
  OR title = /*title*/'Mindset'
  /*END*/
  /*IF published != null */
  OR published = /*published*/2002
  /*END*/
/*END*/
ORDER BY ID ASC