# Test repo for demonstrating issue with Graal SDK+JS with shadowing and relocation

## Summary

As described in https://github.com/oracle/graal/issues/10907, when using Graal 24.2.0,
it is not possible relocate the `com.oracle.truffle.polyglot` package anymore during shadowing
due to the native bindings that have been introduced in the `JDKSupport` class.

Relocation is necessary in some cases (see https://github.com/oracle/graal/issues/10907) to avoid conflicts with other projects also including GraalSDK+JS.

This repo provides an MRE that uses GraalSDK+JS with shadowing + relocation, which causes a crash.

## Reproducing

To reproduce the issue, the following gradle command can be executed:
```shell
./gradlew runFinalJar
```

The following crash is produced:
```shell
Exception in thread "main" java.lang.UnsatisfiedLinkError: 'void demo.vendors.com.oracle.truffle.runtime.ModulesSupport.addExports0(java.lang.Module, java.lang.String, java.lang.Module)'
        at demo.vendors.com.oracle.truffle.runtime.ModulesSupport.addExports0(Native Method)
        at demo.vendors.com.oracle.truffle.runtime.ModulesSupport.<clinit>(ModulesSupport.java:64)
        at demo.vendors.com.oracle.truffle.runtime.hotspot.HotSpotTruffleRuntimeAccess.createRuntime(HotSpotTruffleRuntimeAccess.java:84)
        at demo.vendors.com.oracle.truffle.runtime.hotspot.HotSpotTruffleRuntimeAccess.getRuntime(HotSpotTruffleRuntimeAccess.java:75)
        at demo.vendors.com.oracle.truffle.api.Truffle.createRuntime(Truffle.java:145)
        at demo.vendors.com.oracle.truffle.api.Truffle$1.run(Truffle.java:176)
        at demo.vendors.com.oracle.truffle.api.Truffle$1.run(Truffle.java:174)
        at java.base/java.security.AccessController.doPrivileged(AccessController.java:319)
        at demo.vendors.com.oracle.truffle.api.Truffle.initRuntime(Truffle.java:174)
        at demo.vendors.com.oracle.truffle.api.Truffle.<clinit>(Truffle.java:63)
        at demo.vendors.com.oracle.truffle.runtime.enterprise.EnterpriseTruffle.supportsEnterpriseExtensions(stripped:22)
        at demo.vendors.com.oracle.truffle.polyglot.enterprise.EnterprisePolyglotImpl.getPriority(stripped:551)
        at java.base/java.util.Comparator.lambda$comparing$77a9974f$1(Comparator.java:473)
        at java.base/java.util.TimSort.countRunAndMakeAscending(TimSort.java:355)
        at java.base/java.util.TimSort.sort(TimSort.java:220)
        at java.base/java.util.Arrays.sort(Arrays.java:1308)
        at java.base/java.util.ArrayList.sort(ArrayList.java:1804)
        at java.base/java.util.Collections.sort(Collections.java:178)
        at demo.vendors.org.graalvm.polyglot.Engine.loadAndValidateProviders(Engine.java:1641)
        at demo.vendors.org.graalvm.polyglot.Engine$1.run(Engine.java:1717)
        at demo.vendors.org.graalvm.polyglot.Engine$1.run(Engine.java:1712)
        at java.base/java.security.AccessController.doPrivileged(AccessController.java:319)
        at demo.vendors.org.graalvm.polyglot.Engine.initEngineImpl(Engine.java:1712)
        at demo.vendors.org.graalvm.polyglot.Engine$ImplHolder.<clinit>(Engine.java:170)
        at demo.vendors.org.graalvm.polyglot.Engine.getImpl(Engine.java:422)
        at demo.vendors.org.graalvm.polyglot.Engine$Builder.build(Engine.java:724)
        at demo.Main.main(Main.java:21)
```

## Setup of this repo

The setup can be found in `app/build.gradle`.

The `com.github.johnrengelman.shadow` plugin is used to shadow the GraalSDK+JS jars, with relocation.
GraalSDK+JS is used to run a simple JS function from Java.
The resulting jar can be started using `runFinalJar`.

## License

All contents of this repo can be reused without restrictions.