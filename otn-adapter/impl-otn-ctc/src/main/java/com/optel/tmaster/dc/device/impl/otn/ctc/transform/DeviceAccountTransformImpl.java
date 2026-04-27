/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.otn.ctc.transform;

import com.optel.tmaster.dc.device.impl.otn.ctc.transform.base.AbstractCtcTransformer;
import com.optel.tmaster.dc.device.impl.otn.ctc.transform.base.CommonTransform;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.device.rev200425.ModifyAccountInput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.device.rev200425.QueryAccountOutput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.device.rev200425.QueryAccountOutputBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.account.rev231216.accounts.AccountBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.account.rev231216.Accounts;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.account.rev231216.AccountsBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.account.rev231216.UserRole;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.account.rev231216.accounts.Account;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.account.rev231216.accounts.AccountKey;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * <p>
 *      DeviceAccountTransformImpl
 * <p>
 *
 * @author SunYu
 * @date 2024/2/21
 */
public class DeviceAccountTransformImpl extends AbstractCtcTransformer implements CommonTransform {

    public QueryAccountOutput devQueryAccountOutputToApi(Accounts accounts) {
        QueryAccountOutputBuilder queryAccountOutputBuilder = new QueryAccountOutputBuilder();
        Map<AccountKey, Account> accountKeyAccountMap = Optional.ofNullable(accounts)
                .map(Accounts::getAccount)
                .orElse(new HashMap<>());
        Map<org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.account.rev231216.accounts.AccountKey, org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.account.rev231216.accounts.Account> resultMap = new HashMap<>();
        accountKeyAccountMap.forEach((key, value) -> {
            org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.account.rev231216.accounts.AccountKey accountKey = new org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.account.rev231216.accounts.AccountKey(key.getUserName());
            AccountBuilder accountBuilder = new AccountBuilder();
            accountBuilder.setUserName(value.getUserName());
            accountBuilder.setAccountValidPeriod(value.getAccountValidPeriod());
            accountBuilder.setAdditional(value.getAdditional());
            accountBuilder.setPwdValidPeriod(value.getPwdValidPeriod());
            int intValue = value.getRole().getIntValue();
            org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.account.rev231216.UserRole userRole = org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.account.rev231216.UserRole.forValue(intValue);
            accountBuilder.setRole(userRole);
            resultMap.put(accountKey, accountBuilder.build());
        });
        queryAccountOutputBuilder.setAccount(resultMap);
        return queryAccountOutputBuilder.build();
    }

    public Accounts modifyAccountInputToAccounts(ModifyAccountInput input) {
        AccountsBuilder accountsBuilder = new AccountsBuilder();
        Map<AccountKey, Account> accountKeyAccountMap = new HashMap<>();
        org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.account.rev231216.accounts.AccountBuilder accountBuilder = new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.account.rev231216.accounts.AccountBuilder();
        accountBuilder.setUserName(input.getUserName());
        accountBuilder.setAdditional(input.getAdditional());
        accountBuilder.setRole(UserRole.forValue(input.getRole().getIntValue()));
        AccountKey accountKey = new AccountKey(input.getUserName());
        accountKeyAccountMap.put(accountKey, accountBuilder.build());
        accountsBuilder.setAccount(accountKeyAccountMap);
        return null;
    }

}