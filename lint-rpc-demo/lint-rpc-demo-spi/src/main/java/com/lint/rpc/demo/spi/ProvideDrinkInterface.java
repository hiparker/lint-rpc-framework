package com.lint.rpc.demo.spi;

import com.lint.rpc.common.spi.LintService;

import java.util.List;

public interface ProvideDrinkInterface extends LintService {

    String choose();

    List<String> getDrinkListAll();

}
