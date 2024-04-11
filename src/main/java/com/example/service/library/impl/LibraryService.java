/*******************************************************************************
 * Class        ：
 * Created date ：2023/12/20
 * Lasted date  ：2023/12/20
 * Author       ：TamTH1
 * Change log   ：2023/12/20 01-00 TamTH1 create a new
******************************************************************************/
package com.example.service.library.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.persistence.model.Library;
import com.example.persistence.repository.LibraryRepository;
import com.example.service.library.ILibraryService;

import lombok.extern.slf4j.Slf4j;

/**
 * class
 * 
 * @version 01-00
 * @since 01-00
 * @author TamTH1
 */
@Service
@Transactional(readOnly = false)
@Slf4j
public class LibraryService implements ILibraryService {

    @Autowired
    LibraryRepository libraryRepository;

    @Override
    public void initializeLibrary() {
        log.info(">>> Library init");
        Library userLibrary = new Library();
        // userLibrary.setId(0);
        userLibrary.setName("user library");
        userLibrary = libraryRepository.save(userLibrary);
        System.out.println(userLibrary);
    }

}
