package com.vayam.xaconnection.service;


import com.vayam.xaconnection.model.MssqlUser;
import com.vayam.xaconnection.model.OracleUser;
import com.vayam.xaconnection.repository.MssqlUserRepository;
import com.vayam.xaconnection.repository.OracleUserRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    private final OracleUserRepository oracleUserRepository;
    private final MssqlUserRepository sqlServerUserRepository;

    public UserService(OracleUserRepository oracleUserRepository, MssqlUserRepository sqlServerUserRepository) {
        this.oracleUserRepository = oracleUserRepository;
        this.sqlServerUserRepository = sqlServerUserRepository;
    }

    @Transactional(transactionManager = "transactionManager")
    public void saveUser(String name) {
        OracleUser oracleUser = new OracleUser();
        oracleUser.setName(name);
        oracleUserRepository.save(oracleUser);

        MssqlUser sqlServerUser = new MssqlUser();
        sqlServerUser.setName(name);
        sqlServerUserRepository.save(sqlServerUser);
    }
}
