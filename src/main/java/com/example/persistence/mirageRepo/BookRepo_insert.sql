INSERT INTO BOOK (
/* IF book.author != null*/
author,
/*END*/
/* IF book.title != null*/
title,
/*END*/
/* IF book.published != null*/
published,
/*END*/
/* IF book.imported != null*/
imported
/*END*/
)
values(
/* IF book.author != null*/
/*book.author*/
/*END*/
/* IF book.title != null*/
,/*book.title*/
/*END*/
/* IF book.published != null*/
,/*book.published*/
/*END*/
/* IF book.imported != null*/
,/*book.imported*/
/*END*/
)
;