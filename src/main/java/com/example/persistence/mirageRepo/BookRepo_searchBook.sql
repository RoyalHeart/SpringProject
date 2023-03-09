SELECT * FROM book 
/*BEGIN*/
  WHERE
  /*IF book.author != null */
  author = /*book.author*/'Tom'
  /*END*/
  /*IF book.title != null */
  OR title = /*book.title*/'Mindset'
  /*END*/
  /*IF book.published != null */
   OR published =/*book.published*/2002
  /*END*/
  /*IF from != 0 */
   OR published >=/*from*/2002
  /*END*/
  /*IF to != 0 */
   AND published <=/*to*/2002
  /*END*/
  AND isDeleted IS NULL
/*END*/
ORDER BY ID ASC