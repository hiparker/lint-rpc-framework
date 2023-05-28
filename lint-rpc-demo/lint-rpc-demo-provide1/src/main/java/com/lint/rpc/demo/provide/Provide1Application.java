package com.lint.rpc.demo.provide;

import com.lint.rpc.common.LintConf;
import com.lint.rpc.common.bootstrap.LintRpcServerApplication;

public class Provide1Application {

    public static void main(String[] args) {
        LintConf conf = new LintConf().setProvideSpiType("local");
        LintRpcServerApplication.run(8080, conf, Provide1Application.class);
    }

}
