/*******************************************************************************
 * Class        ：
 * Created date ：2023/12/20
 * Lasted date  ：2023/12/20
 * Author       ：TamTH1
 * Change log   ：2023/12/20 01-00 TamTH1 create a new
******************************************************************************/
package com.example.persistence.repository;

import org.springframework.stereotype.Repository;

import com.example.persistence.model.Library;
import com.example.service.database.DbRepository;

/**
 * class
 * 
 * @version 01-00
 * @since 01-00
 * @author TamTH1
 */
@Repository
public interface LibraryRepository extends DbRepository<Library, Long> {

}
