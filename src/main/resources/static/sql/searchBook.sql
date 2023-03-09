SELECT * FROM book 
/*BEGIN*/
  WHERE
  /*IF author != null */
  author = /*author*/
  /*END*/
  /*IF title != null */
  OR title = /*title*/
  /*END*/
  /*IF published != null */
   OR published =/*published*/2002
  /*END*/
  /*IF from != 0 */
   OR published >=/*from*/2002
  /*END*/
  /*IF to != 0 */
   AND published <=/*to*/2002
  /*END*/
/*END*/
ORDER BY ID ASC