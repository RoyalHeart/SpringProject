UPDATE BOOK 
SET 
/* IF book.author != null*/
author = /*book.author*/
/*END*/
/* IF book.title != null*/
, title= /*book.title*/
/*END*/
/* IF book.published != null*/
, published= /*book.published*/
/*END*/
/* IF book.id != null*/
WHERE id = /*book.id*/
/* END */
;