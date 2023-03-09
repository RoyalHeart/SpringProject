SELECT *
FROM book

WHERE
	isDeleted IS NULL	
	/*IF id != null*/
	AND id = /*id*/1
	/*END*/


	/*IF ids != null*/
	AND user_id IN /*ids*/(1, 2, 3)
	/*END*/

	/*IF title != null*/
	AND title = /*title*/'miyamoto'
	/*END*/


/*IF orders != null*/
ORDER BY /*$orders*/user_id
/*END*/

/*BEGIN*/
LIMIT
	/*IF offset != null*/
	/*offset*/0,
	/*END*/

	/*IF size != null*/
	/*size*/10
	/*END*/
/*END*/