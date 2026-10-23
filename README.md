# purescript-node-net

Java port of the PureScript `node-net` package. See the
[port maintenance guide](../javapurs/docs/port-maintenance.md)
for source layout, local dependency selection and JVM validation.

## JVM tests

`./bin/test` delegates to the [common runner](../javapurs/docs/testing.md#port-particulier) as `node-net`. The wrapper awaits `Test.Main.testNet`, which now joins the final socket close event and checks the server `Nullable` address. The output covers listening, connection, received data, socket end/close and `[OK] node-net suite`.
`./bin/test --help` is read-only and `--clean` rebuilds the backend first. See the [protocol inventory](../javapurs/docs/port-launchers.md) and [M28](../javapurs/docs/testing.md#validation-m28).

[![Latest release](http://img.shields.io/github/release/purescript-node/purescript-node-net.svg)](https://github.com/purescript-node/purescript-node-net/releases)
[![Build status](https://github.com/purescript-node/purescript-node-net/workflows/CI/badge.svg?branch=master)](https://github.com/purescript-node/purescript-node-net/actions?query=workflow%3ACI+branch%3Amaster)
[![Pursuit](https://pursuit.purescript.org/packages/purescript-node-net/badge)](https://pursuit.purescript.org/packages/purescript-node-net)

A wrapper for Node's net API.

## Installation

```
spago install node-net
```

## Documentation

Module documentation is [published on Pursuit](http://pursuit.purescript.org/packages/purescript-node-net).
