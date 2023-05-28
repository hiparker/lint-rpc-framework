package com.lint.rpc.demo.provide;

import com.lint.rpc.common.LintConf;
import com.lint.rpc.common.bootstrap.LintRpcServerApplication;

public class Provide2Application {

    public static void main(String[] args) {
        LintConf conf = new LintConf().setProvideSpiType("local");
        LintRpcServerApplication.run(9090, conf, Provide2Application.class);
    }

}
